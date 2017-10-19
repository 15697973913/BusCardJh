package com.ncrf.jiege.buscarddz.server;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.ncrf.jiege.buscarddz.MainActivity;
import com.ncrf.jiege.buscarddz.application.MyApplication;
import com.ncrf.jiege.buscarddz.tools.CopyFile;
import com.ncrf.jiege.buscarddz.tools.GetFile;
import com.ncrf.jiege.buscarddz.tools.GetLineMsg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AutoStartBroadcastService extends Service {
	public static AutoStartBroadcastService service;

	public static class AutoStartBroadcastReceiver extends BroadcastReceiver {
		private final String ACTION = "android.intent.action.BOOT_COMPLETED";
		private final String MOUNTED = "android.intent.action.MEDIA_MOUNTED";
		private final String UNMOUNTED = "android.intent.action.MEDIA_UNMOUNTED";
		private final String TAG="BroadcastReceiver";
		private String USBPATH = "";
		private final String BENDIPATH = Environment
				.getExternalStorageDirectory() + "/Advert";// 1A09-2B6C
		private boolean ishavasd;
		private Context context;
		public List<String> storagelist = new ArrayList<String>();
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ACTION)
					|| intent.getAction().equals(MOUNTED)
					|| intent.getAction().equals(UNMOUNTED)) {
				if (MainActivity.context == null) {
					Intent newIntent = context.getPackageManager()
							.getLaunchIntentForPackage("com.example.buscardzz");
					context.startActivity(newIntent);
					((MyApplication) MyApplication.context).chongqi();
				}
			}
			if (intent.getAction().equals(MOUNTED)) {
				handler.sendEmptyMessage(0x5151);
				this.context = context;
				Log.v("TAG", "SD卡插入");
			}
		}

		Handler handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
					case 0x5151:
						storagelist = GetFile.getstoragefilelis();
						String sdpath = dblist(storagelist, MyApplication.storagefilelist);
						Log.v(TAG, "sdpath:" + sdpath);
						if (sdpath == null) {
							handler.sendEmptyMessageDelayed(0x5151, 5000);
							return;
						}
						USBPATH = sdpath + "/BusCard";
						File file = new File(USBPATH);
						Log.v(TAG, "file:" + USBPATH);
						ishavasd = file.exists();
						Log.v(TAG, "ishavasd:" + ishavasd);
						Log.v(TAG, "有存储卡");
						String sdapkpath = USBPATH + "/Apk/BusCardDz.apk";
						String bdapkpath = BENDIPATH + "/Apk/BusCardDz.apk";
						File filejia = new File(BENDIPATH + "/Apk");
						if (!filejia.exists()) {
							filejia.mkdirs();
						}
						File bdapk = new File(bdapkpath);
						// 复制安装包
						String sdconfigpath = USBPATH + "/ConfigureFile";
						String bdconfigpath = BENDIPATH + "/ConfigureFile";
						File sdconfigfile = new File(sdconfigpath);
						if (sdconfigfile.exists()) {
							CopyFile.copyFolder(sdconfigpath, bdconfigpath);
							new Thread(){
								@Override
								public void run() {
									super.run();
									GetLineMsg.readfwyyMsgToSqlite();
									GetLineMsg.readLineMsgToSqlite();
									GetLineMsg.readsxLineMsgToSqlite();
									GetLineMsg.readxxLineMsgToSqlite();
								}
							}.start();
						}
						Log.v(TAG, "sdapkpath:" + sdapkpath);
						if (new File(sdapkpath).exists()) {
							Log.v(TAG, "有安装包");
							if (bdapk.exists()) {
								bdapk.delete();
							}
							CopyFile.copyFile(sdapkpath, bdapkpath);
							Intent intent = new Intent("android.intent.action.SILENT_INSTALL_PACKAGE");
							intent.putExtra("apkFilePath", bdapkpath);
							context.sendBroadcast(intent);
						} else {
							Log.v(TAG, "无安装包");
						}
						break;
					default:
						break;
				}
			};
		};
	}

	/**
	 * 对比两个list，多出来的项
	 *
	 * @param
	 */
	public static String dblist(List<String> list, List<String> list1) {
		for (String s2 : list) {
			boolean flag = false;
			for (String s1 : list1) {
				if (s2.equals(s1)) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				return s2;
			}
		}
		return null;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		service = AutoStartBroadcastService.this;
	}
}
