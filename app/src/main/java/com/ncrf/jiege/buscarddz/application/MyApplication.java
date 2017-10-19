package com.ncrf.jiege.buscarddz.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Handler;

import com.ncrf.jiege.buscarddz.tools.GetFile;
import com.ncrf.jiege.buscarddz.tools.MySqlHelper;
import com.ncrf.jiege.buscarddz.util.LineMsg_Util;
import com.ncrf.jiege.buscarddz.util.SiteMsg_Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MyApplication extends Application {
	// 线路信息
	public static LineMsg_Util line_util;
	// 上行站点list
	public static List<SiteMsg_Util> sxlist = new ArrayList<>();
	// 下行站点list
	public static List<SiteMsg_Util> xxlist = new ArrayList<>();
	// activity对象列表,用于activity统一管理
	private List<Activity> activityList;
	// 异常捕获
	protected boolean isNeedCaughtExeption = true;// 是否捕获未知异常
	// 左边list显示的
	public static List<SiteMsg_Util> left_listview = new ArrayList<>();
	// 中间list显示的
	public static List<SiteMsg_Util> centre_listview = new ArrayList<>();
	// 右边list显示的
	public static List<SiteMsg_Util> right_listview = new ArrayList<>();

	public static List<String> storagefilelist = new ArrayList<String>();
	private MyUncaughtExceptionHandler uncaughtExceptionHandler;
	private String packgeName;
	public static Context context;
	public static SQLiteDatabase db;
	public static MySqlHelper helper;

	public void onCreate() {
		super.onCreate();
		activityList = new ArrayList<Activity>();
		packgeName = getPackageName();
		storagefilelist = GetFile.getstoragefilelis();
		context = this;
		helper = new MySqlHelper(this, "linemessage.db", null, 1);
		db = helper.getWritableDatabase();
//		if (isNeedCaughtExeption) {
//			cauchException();
//		}
	}

	// -------------------异常捕获-----捕获异常后重启系统-----------------//
	private void cauchException() {
		Intent intent = new Intent();
		// 参数1：包名，参数2：程序入口的activity
		intent.setClassName(packgeName, packgeName + ".LoginActivity");
		// 程序崩溃时触发线程
		uncaughtExceptionHandler = new MyUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
	}

	// 创建服务用于捕获崩溃异常
	private class MyUncaughtExceptionHandler implements UncaughtExceptionHandler {
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			// 保存错误日志
			saveCatchInfo2File(ex);
			handler.sendEmptyMessage(0x1414);
		}
	};

	public void chongqi() {
		// 关闭当前应用
		finishAllActivity();
		finishProgram();
		Intent newIntent = getPackageManager().getLaunchIntentForPackage("com.example.buscarddz");
		startActivity(newIntent);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 0x1414:
					Intent newIntent = getPackageManager().getLaunchIntentForPackage("com.example.buscardzz");
					startActivity(newIntent);
					// 关闭当前应用
					finishAllActivity();
					finishProgram();
					break;
				default:
					break;
			}
		};
	};

	/**
	 * 保存错误信息到文件中
	 *
	 * @return 返回文件名称
	 */
	private String saveCatchInfo2File(Throwable ex) {
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String sb = writer.toString();
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			String time = formatter.format(new Date());
			String fileName = time + ".txt";
			System.out.println("fileName:" + fileName);
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				String filePath = Environment.getExternalStorageDirectory() + "Advert" + "/HKDownload/" + "/crash/";
				File dir = new File(filePath);
				if (!dir.exists()) {
					if (!dir.mkdirs()) {
						// 创建目录失败: 一般是因为SD卡被拔出了
						return "";
					}
				}
				System.out.println("filePath + fileName:" + filePath + fileName);
				FileOutputStream fos = new FileOutputStream(filePath + fileName);
				fos.write(sb.getBytes());
				fos.close();
				// 文件保存完了之后,在应用下次启动的时候去检查错误日志,发现新的错误日志,就发送给开发者
			}
			return fileName;
		} catch (Exception e) {
			System.out.println("an error occured while writing file..." + e.getMessage());
		}
		return null;
	}

	// ------------------------------activity管理-----------------------//

	// activity管理：从列表中移除activity
	public void removeActivity(Activity activity) {
		activityList.remove(activity);
	}

	// activity管理：添加activity到列表
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// activity管理：结束所有activity
	public void finishAllActivity() {
		for (Activity activity : activityList) {
			if (null != activity) {
				activity.finish();
			}
		}
	}

	// 结束线程,一般与finishAllActivity()一起使用
	// 例如: finishAllActivity;finishProgram();
	public void finishProgram() {
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}