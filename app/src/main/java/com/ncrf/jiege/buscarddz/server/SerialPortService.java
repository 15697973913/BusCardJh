package com.ncrf.jiege.buscarddz.server;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.ncrf.jiege.buscarddz.MainActivity;
import com.ncrf.jiege.buscarddz.application.MyApplication;
import com.ncrf.jiege.buscarddz.tools.MyFunc;
import com.ncrf.jiege.buscarddz.tools.SqliteUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;

public class SerialPortService extends Service {

	protected SerialPort mSerialPort;
	protected OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadThread mReadThread;
	private String TAG = "SerialPortService";
	public String strResponse = "";

	private class ReadThread extends Thread {

		public void run() {
			super.run();
			while (!isInterrupted()) {
				int size;
				try {
					byte[] buffer = new byte[512];
					if (mInputStream == null)
						return;
					size = mInputStream.read(buffer);
					byte[] bRec = new byte[size];
					for (int i = 0; i < size; i++) {
						bRec[i] = buffer[i];
					}
					// 截取1E与1F中间的字符
					if (size > 0) {
						String getmsg = MyFunc.ByteArrToHex(bRec);
						getmsg(getmsg);
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}

	/**
	 * 截取1e-1f之间的数据
	 *
	 * @return 截取的数据
	 */
	public void getmsg(String str) {
		strResponse += str;
		if (strResponse.length() < 25) {
			return;
		} else {
			int btindex = strResponse.indexOf("1E60");
			if (btindex == -1) {
				strResponse = "";
				return;
			}
			strResponse = strResponse.substring(btindex, strResponse.length());
			btindex = strResponse.indexOf("1E60");
			// 截取"1E60"后面的数据
			String strtemp = strResponse.substring(btindex + 4, strResponse.length());
			// 数据帧长度
			if (strtemp.length() < 15) {
				return;
			}
			int sjzcd = MyFunc.HexToInt(strtemp.substring(8, 12));
			Log.v(TAG, "信息帧长度:" + strtemp.substring(8, 12));
			if (sjzcd > 1200) {
				strResponse = "";
				return;
			}
			if (strResponse.length() < (sjzcd + 10) * 2) {
				return;
			}

			// 判断最后一位十是否是“1F”
			if (strResponse.length() == (sjzcd + 10) * 2) {
				// 截取最后两位
				String wfzhlw = strResponse.substring(strResponse.length() - 2, strResponse.length());
				if (!wfzhlw.equalsIgnoreCase("1F")) {
					strResponse = "";
					return;
				}
				if (jiaoyan(strResponse)) {
					Message message = new Message();
					message.obj = strResponse.substring(4, strResponse.length());
					message.what = 0x1313;
					handler.sendMessage(message);
				}
				strResponse = "";
				return;
			} else {
				// 如果不是就截取对应长度，保留后面的数据
				String aa = strResponse.substring(0, (sjzcd + 10) * 2);
				String wfzhlw1 = aa.substring(aa.length() - 2, aa.length());
				if (!wfzhlw1.equalsIgnoreCase("1F")) {
					strResponse = strResponse.substring(strResponse.indexOf("1F1E") + 4, strResponse.length());
					return;
				}
				if (strResponse.length() > (sjzcd + 10) * 2) {
					// 把多余的截取出来
					strResponse = strResponse.substring((sjzcd + 10) * 2, strResponse.length());
				}
				Log.v(TAG, "aa:" + aa);
				if (jiaoyan(aa)) {
					Message message = new Message();
					message.obj = aa.substring(4, aa.length());
					message.what = 0x1313;
					handler.sendMessage(message);
				}
				return;
			}
		}
	}

	/**
	 * 校验的方法
	 *
	 * @param str  要校验的数据
	 * @return 校验结果
	 */
	public boolean jiaoyan(String str) {
		Log.v(TAG, "校验前数据为：" + str);
		int jiaoyan = MyFunc.HexToInt(str.substring(str.length() - 4, str.length() - 2));
		int yhzhi = Integer.parseInt(str.substring(0, 2), 16);
		for (int i = 2; i < str.length() - 4; i += 2) {
			int bb = Integer.parseInt(str.substring(i, i + 2), 16);
			yhzhi = yhzhi ^ bb;
		}
		if ((jiaoyan ^ yhzhi) == 0) {
			return true;
		} else {
			Log.v(TAG, "校验失败");
			return false;
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0x1313:
					try {
						String message = msg.obj.toString();
						String lsh = message.substring(6, 8);
						String ydz = message.substring(2, 4);
						// 目标地址
						String mbdz = message.substring(0, 2);
						Log.v(TAG, "流水号：" + lsh);
						if ((mbdz.equals("20") || mbdz.equals("10") || mbdz.equalsIgnoreCase("ff"))) {
							if (message.substring(0, 2).equals("20") || message.substring(0, 2).equals("02")) {
								String sjz = "1E60" + ydz + "2005" + lsh + "00040C000101";
								Log.v(TAG, "校验位前的数据：" + sjz);
								if (message.equalsIgnoreCase("1E602003010100005D1F")) {
									// 巡检回复
									sjz = "1E60" + ydz + "2005" + lsh + "0008090001010A000101";
								}
								int yhzhi = Integer.parseInt(sjz.substring(0, 2), 16);
								for (int i = 2; i < sjz.length(); i += 2) {
									int bb = Integer.parseInt(sjz.substring(i, i + 2), 16);
									yhzhi = yhzhi ^ bb;
								}
								Log.v(TAG, "校验位：" + Integer.toHexString(yhzhi));
								String sendmsg = sjz + Integer.toHexString(yhzhi) + "1F";
								Log.v(TAG, "发送的串口为：" + sendmsg);
								sendHex(sendmsg);
							}
							// 将路牌调为下行
							if (message.equalsIgnoreCase("FF020701000402000101831F")) {
								((MainActivity) MainActivity.context).setDrawin(2, MyApplication.xxlist, 0);
								// 将路牌调为上行
							} else if (message.equalsIgnoreCase("FF020701000402000100821F")) {
								((MainActivity) MainActivity.context).setDrawin(2, MyApplication.sxlist, 0);
							} else {
								// 截取消息帧流水号
								String xxz = message.substring(4, 6);
								Log.v(TAG, "消息帧流水号：" + xxz);
								if (xxz.equals("03")) {
									int xxzleng = MyFunc.HexToInt(message.substring(8, 12));
									// 截取消息帧
									String msgz = message.substring(12, 12 + xxzleng * 2);
									Log.v(TAG, "消息帧:" + msgz);
									// 到、离站 到站： 1 离站： 2;

									int islz = getxxz(getleng(msgz, 4), "04");
									// 截取站点号
									int sxxbz = getxxz(msgz, "02");
									// 站点序号
									int dzxh = getxxz(getleng(msgz, 5), "05");
									Log.v(TAG, sxxbz == 1 ? "下行" : "上行");
									Log.v(TAG, "获取的到站序号：" + dzxh);
									// 下行
									if (sxxbz == 1) {
										((MainActivity) MainActivity.context).setDrawin(MyApplication.xxlist.size() - dzxh + 1, MyApplication.xxlist,
												islz);
									} else {
										((MainActivity) MainActivity.context).setDrawin(dzxh, MyApplication.sxlist, islz);
									}
								} else if (xxz.equals("06")) {
									// 判断是上行还是下行 上行：0e 下行：0f
									String sxx = message.substring(12, 14);
									Log.v(TAG, "上下行标识：" + sxx);
									// 获取长度
									int leng = MyFunc.HexToInt(message.substring(14, 18));
									Log.v(TAG, "截取数据长度：" + message.substring(14, 18));
									Log.v(TAG, "截取完长度：" + leng);
									String cshmsg = message.substring(18, 18 + leng * 2);
									Log.v(TAG, "cshmsg:" + cshmsg);
									Log.v(TAG, "cshmsg长度：" + cshmsg.length() / 2);
									if (sxx.equalsIgnoreCase("0e")) {
										SqliteUtil.DeleteLine(MyApplication.db, "up");
										for (int i = 1;; i++) {
											if (MyFunc.HexToInt((cshmsg.substring(0, 2))) != i) {
												Log.v(TAG, "TiaoChuXunHuan");
												break;
											}
											// 站点名称
											String zdname = getxxz1(cshmsg);
											// 站点序号
											int zdxh = MyFunc.HexToInt((cshmsg.substring(0, 2)));
											SqliteUtil.InsertLine(MyApplication.db, "up", zdname, zdxh + "");
											cshmsg = getleng1(cshmsg);
											if (cshmsg.length() < 4) {
												Log.v(TAG, "TiaoChuXunHuan");
												break;
											}
										}
										((MainActivity) MainActivity.context).getLineMsg(2);
									} else if (sxx.equalsIgnoreCase("0f")) {
										int ktxh = MyFunc.HexToInt((cshmsg.substring(0, 2)));
										SqliteUtil.DeleteLine(MyApplication.db, "down");
										for (int i = ktxh;; i--) {
											if (MyFunc.HexToInt((cshmsg.substring(0, 2))) != i) {
												Log.v(TAG, "TiaoChuXunHuan");
												break;
											}
											// 站点名称
											String zdname = getxxz1(cshmsg);
											// 站点序号
											int zdxh = MyFunc.HexToInt((cshmsg.substring(0, 2)));
											Log.v(TAG, "站名：" + zdname);
											SqliteUtil.InsertLine(MyApplication.db, "down", zdname, zdxh + "");
											cshmsg = getleng1(cshmsg);
											if (cshmsg.length() < 4) {
												Log.v(TAG, "TiaoChuXUn");
												break;
											}
										}
										((MainActivity) MainActivity.context).getLineMsg(3);
									} else if (sxx.equalsIgnoreCase("1b")) {
										String bb = message.substring(26, message.length());
										String xlh = getxxz1(bb);
										int xhleng = MyFunc.HexToInt(bb.substring(2, 4));
										bb = bb.substring(4 + xhleng * 2, bb.length());
										String qd = getxxz1(bb);
										xhleng = MyFunc.HexToInt(bb.substring(2, 4));
										bb = bb.substring(4 + xhleng * 2, bb.length());
										String zd = getxxz1(bb);
										Log.v(TAG, "线路号：" + xlh + "  起点站：" + qd + "  终点站:" + zd);
										SqliteUtil.DeleteLineNum(MyApplication.db);
										SqliteUtil.insertMsg(MyApplication.db, xlh, qd, zd);
										((MainActivity) MainActivity.context).getLineMsg(1);
									}
								}
							}
						}
					} catch (Exception e) {
						Log.e(TAG, "有异常");
					}
					break;
				default:
					break;
			}
		};
	};

	public int getxxz(String msg, String str) {
		int xh = msg.indexOf(str) + 2;
		int xhleng = MyFunc.HexToInt(msg.substring(xh, xh + 4));
		Log.v(TAG, "长度：" + xhleng);
		int a = MyFunc.HexToInt(msg.substring(xh + 4, xh + 4 + (xhleng * 2)));
		return a;
	}

	public String getleng(String msg, int a) {
		String str = msg;
		for (int i = 2; i < a; i++) {
			int xh = msg.indexOf("0" + i) + 2;
			int xhleng = MyFunc.HexToInt(msg.substring(xh, xh + 4));
			str = msg.substring(xh + 6 + xhleng - 1, msg.length());
		}
		return str;
	}

	public String getleng1(String msg) {
		String str = msg;
		int xhleng = MyFunc.HexToInt(msg.substring(2, 4));
		str = msg.substring(2 + 4 + xhleng * 2, msg.length());
		return str;
	}

	public String getxxz1(String msg) {
		int xhleng = MyFunc.HexToInt(msg.substring(2, 4));
		Log.v(TAG, "长度：" + xhleng);
		String a = MyFunc.HexStringTOString(msg.substring(4, 4 + (xhleng * 2)));
		return a;
	}

	public void onCreate() {
		super.onCreate();
		try {
			mSerialPort = new SerialPort(new File("/dev/ttyS1"), 9600, 0);
			mOutputStream = mSerialPort.getOutputStream();
			mInputStream = mSerialPort.getInputStream();
			mReadThread = new ReadThread();
			mReadThread.start();
		} catch (Exception e) {
		}
	}

	public void sendHex(String sHex) {
		byte[] bOutArray = MyFunc.HexToByteArr(sHex);
		try {
			mOutputStream.write(bOutArray);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void SendMsg(String str) {
		int i;
		CharSequence t = str;
		char[] text = new char[t.length()];
		for (i = 0; i < t.length(); i++) {
			text[i] = t.charAt(i);
		}
		try {
			mOutputStream.write(new String(text).getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeSerialPort() {
		if (mSerialPort != null) {
			mSerialPort.close();
			mSerialPort = null;
		}
	}

	public IBinder onBind(Intent intent) {
		return null;
	}

}
