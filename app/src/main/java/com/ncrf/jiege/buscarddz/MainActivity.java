package com.ncrf.jiege.buscarddz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ncrf.jiege.buscarddz.adapter.leftMyAdapter;
import com.ncrf.jiege.buscarddz.adapter.rightMyAdapter;
import com.ncrf.jiege.buscarddz.adapter.shangMyAdapter;
import com.ncrf.jiege.buscarddz.application.MyApplication;
import com.ncrf.jiege.buscarddz.server.SerialPortService;
import com.ncrf.jiege.buscarddz.tools.CopyFile;
import com.ncrf.jiege.buscarddz.tools.GetLineMsg;
import com.ncrf.jiege.buscarddz.tools.HorizontalListView;
import com.ncrf.jiege.buscarddz.util.SiteMsg_Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	public static Context context;
	private HorizontalListView listView2;
	private ListView listView1, listView3;
	private TextView LineWord;
	// 系统根路径
	public static String RootPath = Environment.getExternalStorageDirectory().toString();
	// 总路径
	public static String ZongFilePath = RootPath + File.separator + "Advert";
	// 配置文件路径
	public static String ConfigureFilePath = RootPath + File.separator + "Advert" + File.separator + "ConfigureFile" + File.separator;
	public static File fwyyfile = new File(ConfigureFilePath + "fwyy.txt");
	public FrameLayout xllist;// 线路list的布局
	public LinearLayout dzts;// 到站提示的布局
	public TextView zhuangtai, zhanming;// 到理站，站点名称
	public Intent intent;
	public TextView tv_isdaozhan, tv_dzname;
	public static boolean isdayu30;
	public boolean ishavemsg = true;
	public int ishave = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 隐藏状态栏
		setContentView(R.layout.main_layout);
		createfile();// 创建文件夹
		init();// 初始化控件
		getLineMsg(4);// 设置站点信息
		// 接收串口数据服务
		Intent intent = new Intent(MainActivity.this, SerialPortService.class);
		startService(intent);

	}

	public void copyfile() {
		if (MyApplication.line_util.getLineWord() == null) {
			Log.v(TAG, "readLineMsgToSqlite");
			CopyFile.CopyAssetsFile("stationline.ini", ConfigureFilePath);
			GetLineMsg.readLineMsgToSqlite();
			getLineMsg(1);
		}
		if (MyApplication.sxlist.size() == 0) {
			Log.v(TAG, "MyApplication.sxlist.size():" + MyApplication.sxlist.size());
			CopyFile.CopyAssetsFile("stationlines.ini", ConfigureFilePath);
			GetLineMsg.readsxLineMsgToSqlite();
			getLineMsg(2);
		}
		if (MyApplication.xxlist.size() == 0) {
			Log.v(TAG, "readxxLineMsgToSqlite");
			CopyFile.CopyAssetsFile("stationlinex.ini", ConfigureFilePath);
			GetLineMsg.readxxLineMsgToSqlite();
			getLineMsg(3);
		}

	}

	// 设置站点信息
	/**
	 *
	 * @param i
	 *            判断要设置哪个信息1 ：线路信息 2：上行 3：下行 4:全部
	 */
	public void getLineMsg(final int i) {
		new Thread() {
			public void run() {
				// 判断文件是否存在
				switch (i) {
					case 1:
						GetLineMsg.getline();
						break;
					case 2:
						GetLineMsg.getsxsite();
						break;
					case 3:
						GetLineMsg.getxxsite();
						break;
					case 4:
						GetLineMsg.getline();
						GetLineMsg.getsxsite();
						GetLineMsg.getxxsite();
						break;
					default:
						break;
				}
				// 设置listview
				handler.sendEmptyMessage(0x2153);
			};
		}.start();
	}

	/**
	 *
	 * @param index
	 *            到站的序号
	 * @param list
	 *            设置上下行的list
	 * @param isdlz
	 *            是到站还是离站
	 */
	public void setDrawin(int index, List<SiteMsg_Util> list, int isdlz) {
		if (list.size() <= 30) {
			isdayu30 = true;
		}
		index--;
		Log.v(TAG, "到站序号：" + index);
		Log.v(TAG, "isdlz:" + isdlz);
		tv_isdaozhan.setText(isdlz == 1 ? "到站：" : "下一站：");
		tv_dzname.setText(list.get(index).getStationName());
		int a = 0;
		int leftindex = 0;
		int centreindex = 0;
		int rightindex = 0;
		boolean isdayu40 = false;
		if (list.size() < 40) {
			MyApplication.left_listview = new ArrayList<SiteMsg_Util>();
			MyApplication.right_listview = new ArrayList<SiteMsg_Util>();
			MyApplication.centre_listview = new ArrayList<SiteMsg_Util>();
			MyApplication.centre_listview = list;
			centreindex = index;
			a = 40;
		} else {
			setlistnum(list);
			a = 290;
			isdayu40 = true;
			if (index < 7) {
				leftindex = index;
				centreindex = -1;
				rightindex = 8;
			} else if (7 <= index && index <= (list.size() - 8)) {
				leftindex = 8;
				centreindex = index - 7;
				rightindex = 8;
			} else if (index > (list.size() - 8)) {
				leftindex = 8;
				centreindex = index;
				rightindex = 6 - (index - (list.size() - 7));
			}
		}
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(a, 0, 0, 0);// 4个参数按顺序分别是左上右下
		listView2.setLayoutParams(layoutParams);
		listView1.setAdapter(new leftMyAdapter(MyApplication.left_listview, MainActivity.this, leftindex, isdlz));
		listView2.setAdapter(new shangMyAdapter(MyApplication.centre_listview, MainActivity.this, centreindex, isdlz, isdayu40));
		listView3.setAdapter(new rightMyAdapter(MyApplication.right_listview, MainActivity.this, rightindex, isdlz));
	}

	public String getxlh() {
		String xlname = LineWord.getText().toString();
		if (!(xlname.substring(xlname.length() - 1, xlname.length()).equals("路"))) {
			xlname = xlname + "路";
		}
		return xlname;
	}

	private void createfile() {
		// 存放广告的路径
		try {
			File file = new File(ZongFilePath);
			if (!file.exists()) {
				file.mkdirs();
				Log.v(TAG, "创建总文件夹成功");
			} else {
				Log.v(TAG, "文件夹已存在");
				Log.v(TAG, "FolderPath:" + ZongFilePath);
			}
			file = new File(ConfigureFilePath);
			if (!file.exists()) {
				file.mkdirs();
				Log.v(TAG, "创建配置文件夹成功");
			} else {
				Log.v(TAG, "文件夹已存在");
				Log.v(TAG, "FolderPath:" + ConfigureFilePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.v(TAG, "创建文件夹失败");
		}
	}

	// 初始化控件
	private void init() {
		context = MainActivity.this;
		LineWord = (TextView) findViewById(R.id.LineWord);
		listView2 = (HorizontalListView) findViewById(R.id.list);
		listView1 = (ListView) findViewById(R.id.list_left);
		listView3 = (ListView) findViewById(R.id.list_right);
		tv_isdaozhan = (TextView) findViewById(R.id.isdaozhan);
		tv_dzname = (TextView) findViewById(R.id.dzname);
	}

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 0x2153:
					// 设置listview
					if (MyApplication.sxlist.size() == 0) {
						ishavemsg = false;
						if (ishave < 3) {
							getLineMsg(4);
						}
						ishave++;
					} else {
						setDrawin(2, MyApplication.sxlist, 2);
						ishavemsg = true;
					}
					setxlmsg();
					copyfile();
					break;
				case 0x8181:
					xllist.setVisibility(View.VISIBLE);
					dzts.setVisibility(View.GONE);
					break;
				default:
			}
		}

		// 设置线路信息
		private void setxlmsg() {
			try {
				String str = MyApplication.line_util.getLineWord().substring(0, 1);
			} catch (Exception e) {
				return;
			}
			if (MyApplication.line_util.getLineWord()
					.substring(MyApplication.line_util.getLineWord().length() - 1, MyApplication.line_util.getLineWord().length()).equals("路")) {
				LineWord.setText(MyApplication.line_util.getLineWord());
			} else {
				LineWord.setText(MyApplication.line_util.getLineWord() + "路");
			}

		};
	};

	private Boolean CopyAssetsFile(String filename, String des) {
		Boolean isSuccess = true;
		// 复制安卓apk的assets目录下任意路径的单个文件到des文件夹，注意是否对des有写权限
		AssetManager assetManager = this.getAssets();

		InputStream in = null;
		OutputStream out = null;
		try {
			in = assetManager.open(filename);
			String newFileName = des + "/" + filename;
			out = new FileOutputStream(newFileName);

			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
		} catch (Exception e) {
			e.printStackTrace();
			isSuccess = false;
		}

		return isSuccess;

	}

	/**
	 * @param list
	 *            设置左中右list的个数
	 */
	public void setlistnum(List<SiteMsg_Util> list) {
		if (list.size() > 40) {
			MyApplication.left_listview = new ArrayList<SiteMsg_Util>();
			MyApplication.right_listview = new ArrayList<SiteMsg_Util>();
			MyApplication.centre_listview = new ArrayList<SiteMsg_Util>();
			List<SiteMsg_Util> list2 = new ArrayList<SiteMsg_Util>();
			for (int i = 0; i < list.size(); i++) {
				if (i < 7) {
					MyApplication.left_listview.add(list.get(i));
				} else if (i > list.size() - 8) {
					list2.add(list.get(i));
				} else {
					MyApplication.centre_listview.add(list.get(i));
				}
			}
			for (int i = list2.size() - 1; i >= 0; i--) {
				MyApplication.right_listview.add(list2.get(i));
			}
		}
	}
}
