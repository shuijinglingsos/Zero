package com.elf.zerodemo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * 异常收集
 */
public class CrashHandler implements UncaughtExceptionHandler {

	public final String TAG = CrashHandler.class.getSimpleName();

	private Context mContext;
	/**
	 * 异常日志文件保存地址
	 */
	private static String crashSavePath = "crash" + File.separator;
	/**
	 * CrashHandler 实例
	 */
	private static CrashHandler INSTANCE = new CrashHandler();


	/**
	 * 系统默认的 UncaughtException 处理类
	 */
	private UncaughtExceptionHandler mDefaultHandler;

	/**
	 * 用来存储设备信息和异常信息
	 */
	private Map<String, String> infos = new HashMap<String, String>();

	/**
	 * 用于格式化日期,作为日志文件名的一部分
	 */
	private DateFormat sdf_crash = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
	private static SimpleDateFormat myLogSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 日志的输出格式
	
	private static int SDCARD_LOG_FILE_SAVE_DAYS = 2;// sd卡中日志文件的最多保存天数
	private static final int MEMORY_LOG_FILE_MAX_SIZE = 3 * 1024 * 1024;           //内存中日志文件最大值，3M  

	/** 保证只有一个 CrashHandler 实例 */
	private CrashHandler() {
	}

	/** 获取 CrashHandler 实例 ,单例模式 */
	public static CrashHandler getInstance() {
		return INSTANCE;
	}


	public void init(Context context) {
		mContext = context;
		// 获取系统默认的 UncaughtException 处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该 CrashHandler 为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当 UncaughtException 发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Log.e(TAG, "error : ", e);
			}

			// 退出程序
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
	}

	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		final String msg = ex.getLocalizedMessage();
		// 使用 Toast 来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, String.format("很抱歉，程序出现异常：(%s)", !TextUtils.isEmpty(msg) ? msg : "null"), Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();

		// 收集设备参数信息
		collectDeviceInfo(mContext);
		// 保存日志文件
		saveCrashInfoToFile(ex);
		return true;
	}

	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);

			if (pi != null) {
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}

		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}
	}

	private String saveCrashInfoToFile(Throwable ex) {
		StringBuffer sb = new StringBuffer();
		Date nowtime = new Date();
		sb.append("\n\n\n");
		sb.append(myLogSdf.format(nowtime));
		sb.append("\n");
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}

		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();

		String result = writer.toString();
		sb.append(result);
		try {
			long timestamp = System.currentTimeMillis();
			String time = sdf_crash.format(new Date(timestamp));
			String fileName = "crash_" + time + "_error" + ".log";
			
			if (mContext.getExternalCacheDir()!=null) {
//				String path = mContext.getExternalCacheDir().getPath() +  crashSavePath;
				File file = new File(mContext.getExternalCacheDir(), crashSavePath+fileName);
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}else{					
					if(file.length() >= MEMORY_LOG_FILE_MAX_SIZE){
						file.deleteOnExit();
					}
				}
				
				try {
					FileWriter filerWriter = new FileWriter(file, true);//后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
					BufferedWriter bufWriter = new BufferedWriter(filerWriter);
					bufWriter.write(sb.toString());
					bufWriter.newLine();
					bufWriter.close();
					filerWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				//崩溃日志上传
			}
			return fileName;
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing file...", e);
		}

		return null;
	}
//
//	/**
//	 * 删除过期的日志文件
//	 * */
//	public void delOutTimeFile() {// 删除日志文件
//		try {
//			String path = DeviceUtil.getSDcardDir() + DeviceUtil.BASE_PATH_SDCARD_DIR + crashSavePath;
//			File logDir = new File(path);
//			if(logDir.exists() && logDir.isDirectory() && logDir.listFiles().length > 0){
//				File logFiles[] = logDir.listFiles();
//				for(File logFile : logFiles){
//					if (logFile.isDirectory()) {
//						continue;
//					}
//					String logFileName = logFile.getName();
//					String []seps = logFileName.split("_");
//					if (seps.length > 1) {
//						if(compareDaysDiff(seps[1]) >= SDCARD_LOG_FILE_SAVE_DAYS){//删除过期日志
//							logFile.delete();
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	public static long compareDaysDiff(String beginDate){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try{
		    Date d_beginDate = df.parse(beginDate);
		    Date d_endDate = new Date(System.currentTimeMillis());
		    long diff = d_endDate.getTime() - d_beginDate.getTime();
		    long months = diff / (1000 * 60 * 60 * 24);
		    return months;
		}catch (Exception e){
		}
		return 0;
	}
}