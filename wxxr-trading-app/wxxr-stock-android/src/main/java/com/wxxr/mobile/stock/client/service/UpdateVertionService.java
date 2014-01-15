package com.wxxr.mobile.stock.client.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.IAndroidPageNavigator;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IWorkbench;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.client.R;

public class UpdateVertionService extends AbstractModule<IStockAppContext>
		implements IUpdateVertionService, OnClickListener {

	private final int DownApk = 100;
	private static final int DOWNLOAD_PREPARE = 0;
	private static final int DOWNLOAD_WORK = 1;
	private static final int DOWNLOAD_OK = 2;
	private static final int DOWNLOAD_ERROR = 3;

	public static final int NETTYPE_WIFI = 0x01;
	public static final int NETTYPE_CMWAP = 0x02;
	public static final int NETTYPE_CMNET = 0x03;
	private Notification notification;

	/** 是否正在下载 */
	private boolean isDownloading = false;

	boolean isDownloadFinish = false;

	/** 应用目录 */
	private String appDir = "wxxr/dxfdj/download";

	private String sdCardDir = "/sdcard/";

	/** 下载URL */
	private String downloadUrl = "";

	/** apk下载路径--本地 */
	private String apkFilePath;

	/** 下载文件路径 */
	private String downloadFilePath;
	/** 文件大小 */
	int fileSize = 0;

	/** 下载大小 */
	int downloadSize = 0;
	NotificationManager mNotifiManager;

	Dialog dialog;

	// Context context;
	Context androidContext;

	@Override
	public void startDownload(String downloadUrl) {

		// downloadUrl = intent.getStringExtra("downUrl");

		if (StringUtils.isBlank(downloadUrl))
			return;
		String apkName = downloadUrl
				.substring(downloadUrl.lastIndexOf("/") + 1);

		this.downloadUrl = downloadUrl;
		// SD卡存在
		/** sd卡目录 */

		String fileDir = sdCardDir + appDir;
		File file = new File(fileDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		downloadFilePath = fileDir + "/" + apkName + ".tmp";
		apkFilePath = fileDir + "/" + apkName;
		if (!isNetworkConnected()) {
			Toast.makeText(androidContext, "网络不可用", Toast.LENGTH_SHORT).show();
			return;
		}

		/*
		 * if (getNetworkType() != 1) { alertUserSaveFlow(); return; }
		 */

		downloadFile();

	}

	/** * 下载文件 */
	private void downloadFile() {

		new Thread() {
			public void run() {
				isDownloading = true;
				InputStream is = null;
				FileOutputStream fos = null;
				File downloadFile = null;
				try {
					URL url = new URL(downloadUrl);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setConnectTimeout(15 * 1000);
					conn.setAllowUserInteraction(true);
					fileSize = conn.getContentLength();
					if (fileSize <= 0) {
						sendMessage(DOWNLOAD_ERROR);
					} else {
						sendMessage(DOWNLOAD_PREPARE);
						downloadFile = new File(downloadFilePath);
						if (!downloadFile.exists()) {
							downloadFile.createNewFile();
						}
						fos = new FileOutputStream(downloadFile);
						byte[] bytes = new byte[1024];
						int len = -1;
						is = conn.getInputStream();

						while ((len = is.read(bytes)) != -1) {
							fos.write(bytes, 0, len);
							fos.flush();
							downloadSize += len;
							sendMessage(DOWNLOAD_WORK);
						}

						if (downloadSize >= fileSize) {
							isDownloadFinish = true;
						}
					}
				} catch (Exception e) {
					sendMessage(DOWNLOAD_ERROR);
					e.printStackTrace();
				} finally {
					try {
						
						if (is != null) {
							is.close();
							is = null;
						}

						if (fos != null) {
							fos.close();
							fos = null;
						}
						/** 重命名 */
						if (downloadFile != null && isDownloadFinish) {
							downloadFile.renameTo(new File(apkFilePath));
							sendMessage(DOWNLOAD_OK);
						}
					}

					catch (IOException e) {
						e.printStackTrace();
					}
				}
			};

		}.start();

	}

	private void sendMessage(int what) {
		Message m = new Message();
		m.what = what;
		handler.sendMessage(m);
	}

	/**
	 * handler
	 */
	private Handler handler = null;

	private void installApk(String filename) {
		File file = new File(filename);
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		String type = "application/vnd.android.package-archive";
		intent.setDataAndType(Uri.fromFile(file), type);
		IAndroidPageNavigator navigator = (IAndroidPageNavigator) AppUtils
				.getService(IWorkbenchManager.class).getPageNavigator();
		navigator.startActivity(intent);
		// startActivity(intent);
	}

	private void alertUserSaveFlow() {
		dialog = new Dialog(androidContext, R.style.myDialogStyle);
		View view = View
				.inflate(androidContext, R.layout.download_dialog, null);
		dialog.setContentView(view);
		Button done = (Button) view.findViewById(R.id.download_done);
		Button cancel = (Button) view.findViewById(R.id.download_cancel);
		done.setOnClickListener(this);
		cancel.setOnClickListener(this);
		dialog.show();
	}

	/**
	 * 获取当前网络类型
	 * 
	 * @return 1：WIFI网络 2：WAP网络 3：NET网络
	 */
	private int getNetworkType() {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) androidContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if (!StringUtils.isEmpty(extraInfo)) {
				if (extraInfo.toLowerCase().equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}

	private boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) context.getApplication()
				.getAndroidApplication()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	@Override
	protected void initServiceDependency() {
		addRequiredService(IWorkbenchManager.class);
		// androidContext = context.getApplication().getAndroidApplication();
	}

	@Override
	protected void startService() {
		androidContext = context.getApplication().getAndroidApplication();
		/*
		 * homeActivity =((IAndroidPageNavigator)AppUtils
		 * 
		 * .getService(IWorkbenchManager.class).getPageNavigator()).getPageActivity
		 * (AppUtils.getService(IWorkbench.class).getPage("")).getActivity();
		 */
		context.registerService(IUpdateVertionService.class, this);

		handler = new Handler(androidContext.getMainLooper()) {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case DOWNLOAD_PREPARE:
					mNotifiManager = (NotificationManager) androidContext
							.getSystemService(Context.NOTIFICATION_SERVICE);
					notification = new Notification(R.drawable.icon,
							"短线放大镜新版本下载中……", System.currentTimeMillis());

					PendingIntent contentIntent = PendingIntent.getActivity(
							androidContext, 0, new Intent(),
							PendingIntent.FLAG_UPDATE_CURRENT);
					RemoteViews contentView = new RemoteViews(
							androidContext.getPackageName(),
							R.layout.download_notifi);
					notification.contentView = contentView;
					notification.contentView.setProgressBar(R.id.pb, fileSize,
							0, false);
					notification.contentIntent = contentIntent;
					mNotifiManager.notify(DownApk, notification);
					break;
				case DOWNLOAD_WORK:
					if (System.currentTimeMillis() % 100 == 0) {
						notification.contentView.setProgressBar(R.id.pb,
								fileSize, downloadSize, false);
						int res = downloadSize * 100 / fileSize;
						notification.contentView.setTextViewText(R.id.downing,
								"已下载：" + res + "%");
						mNotifiManager.notify(DownApk, notification);
					}
					break;
				case DOWNLOAD_OK:
					downloadSize = 0;
					fileSize = 0;
					mNotifiManager.cancel(DownApk);
					Toast.makeText(androidContext, "下载成功", Toast.LENGTH_SHORT)
							.show();
					installApk(apkFilePath);
					// DownloadApkService.this.stopSelf();
					break;
				case DOWNLOAD_ERROR:
					downloadSize = 0;
					fileSize = 0;
					Toast.makeText(androidContext, "下载失败", Toast.LENGTH_SHORT)
							.show();

					break;
				}
				super.handleMessage(msg);
			}
		};
	}

	@Override
	protected void stopService() {
		context.unregisterService(IUpdateVertionService.class, this);
	}

	private Activity homeActivity = null;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
