package com.jarvis.tbaseviewlib.widget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jarvis.tbaseviewlib.R;
import com.jarvis.tbaseviewlib.data.CacheData;
import com.jarvis.tbaseviewlib.widget.TDialog.DialogListener;

/**
 * 下载更新工具类
 * 
 * @author tansheng QQ:717549357
 * @date 2015-12-2 下午1:57:32
 */
public class UpdateUtils {

	private Context mContext;
	/** 新版本的版本号*/
	private String downVersion;
	/** 提示语 */
	private String updateInfo = "";
	/** 安装包下载的url */
	private String downUrl = "";
	/** 安装包保存路径 */
	private String savePath = CacheData.getApkPath();
	/** 保存名称 */
	private String saveFileName = "tbaseviewlib.apk";
	/** 下载中 */
	private static final int DOWN_UPDATE = 1;
	/** 下载完毕 */
	private static final int DOWN_OVER = 2;
	/** 当前下载进度 */
	private int progress;
	/** 是否中断下载 */
	private boolean interceptFlag = false;
	/** 下载的apk文件的大小 */
	protected int apkSize;
	private Thread downLoadThread;
	private ProgressBar mProgress;
	private TextView mTextView;
	private Dialog downloadDialog;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				mTextView.setText(progress+"%");
				break;
			case DOWN_OVER:
				installApk();
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 功能说明:
	 * 
	 * @author 作者：jarvisT
	 * @date 创建日期：2015-3-17 下午2:50:58
	 * @param context
	 *            下载地址
	 */
	public UpdateUtils(Context context) {
		this.mContext = context;
		updateInfo = mContext.getResources().getString(R.string.update_info);
	}

	private void showNoticeDialog(final CancelListener cancel) {

		new TDialog(mContext, mContext.getResources().getString(R.string.update_title), updateInfo, mContext.getResources().getString(R.string.update_cancle), mContext.getResources().getString(R.string.update_down), new DialogListener() {

			@Override
			public void okClick(int position) {
				if (checkIsDown(savePath)) {
					// 已经下载，可直接安装
					installApk();
				} else {
					// 开始下载
					showDownloadDialog(cancel);
				}

			}

			@Override
			public void dissmissClick() {

			}

			@Override
			public void cancelClick() {
				if (null != cancel) {
					cancel.cancelAction();
				}
			}

		});
	}

	/**
	 * 提示下载中的弹窗
	 * 
	 * @author tansheng QQ:717549357
	 * @date 2015-12-2 下午2:11:26
	 * @param cancel
	 *            取消监听,null则不设置取消按钮
	 */
	private void showDownloadDialog(final CancelListener cancel) {
		Builder builder = new Builder(mContext);
		builder.setTitle(mContext.getResources().getString(R.string.update_loading));
		builder.setCancelable(false);
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.update_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_utils_progress);
		mTextView= (TextView) v.findViewById(R.id.update_utils_text);
		builder.setView(v);
		if (cancel != null) {
			builder.setNegativeButton("取消", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					interceptFlag = true;
					dialog.dismiss();
					cancel.cancelAction();
				}
			});
		}
		downloadDialog = builder.create();
		downloadDialog.show();

		downloadApk();
	}

	/**
	 * 下载apk
	 * 
	 * @param url
	 */
	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(downUrl);

				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				apkSize = conn.getContentLength();// 获得下载的文件大小
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdir();
				}
				String apkFile = savePath + saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成通知安装
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载.

				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	

	/**
	 * 检查是否下载过此版本的apk
	 * @author tansheng QQ:717549357
	 * @date 2015-12-2 下午3:16:48
	 * @param filePath
	 *            检查的文件夹
	 * @return true:已经下载，不需要重新下载，false:未下载
	 */
	private boolean checkIsDown(String filePath) {
		try {
			File f = new File(filePath);
			File[] files = f.listFiles();// 列出所有文件
			// 将所有文件存入list中
			if (files != null) {
				//获得下载的apk的信息
				PackageManager pm = mContext.getPackageManager();
				PackageInfo downAppInfo = null;
				for (int i = 0; i < files.length; i++) {
					if (files[i].getName().contains(saveFileName) || files[i].getName().contains(".apk")) {
						downAppInfo = pm.getPackageArchiveInfo(files[i].getAbsolutePath(), PackageManager.GET_ACTIVITIES);
						// 如果下载文件的版本号与当前已经下载的文件的版本号相同,并且包名相同，则不需要重新下载
						if (downAppInfo!=null&&downVersion.equals(downAppInfo.versionName)&&downAppInfo.packageName.equals(mContext.getPackageName())) {
							return true;
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * 安装apk
	 */
	private void installApk() {
		File apkfile = new File(savePath + saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
	}

	public interface CancelListener {
		/** 取消的监听 */
		public void cancelAction();
	}

	// ------------------------------------------------以下为提供的可调用方法--------------------------------------------------------------------------
	/**
	 * 开始下载
	 * 
	 * @author tansheng QQ:717549357
	 * @date 2015-12-2 下午2:19:59
	 * @param downUrl
	 *            下载路径
	 * @param newVersion
	 *            新版本号
	 * @param cancel
	 *            取消监听，若为null,则没有取消键
	 */
	public void startDownApk(String downUrl, String newVersion,CancelListener cancel) {
		this.downUrl = downUrl;
		downVersion=newVersion;
		showNoticeDialog(cancel);
	}

	/**
	 * * 开始下载
	 * 
	 * @author tansheng QQ:717549357
	 * @date 2015-12-2 下午2:19:59
	 * @param downUrl
	 *            下载路径
	 * @param newVersion
	 *            新版本号
	 * @param updateInfo
	 *            升级提示语
	 * @param savePath
	 *            保存路径
	 * @param saveFileName
	 *            保存文件名,需要添加apk后缀
	 * @param cancel
	 *            取消监听，若为null,则没有取消键
	 */
	public void startDownApk(String downUrl, String newVersion, String updateInfo, String savePath, String saveFileName, CancelListener cancel) {
		this.downUrl = downUrl;
		downVersion=newVersion;
		setUpdateInfo(updateInfo);
		setSavePath(savePath);
		setSaveFileName(saveFileName);
		showNoticeDialog(cancel);
	}

	/** 设置弹窗提示用语 */
	public void setUpdateInfo(String updateInfo) {
		this.updateInfo = updateInfo;
	}

	/** 设置下载后apk的保存路径 */
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	/** 设置下载后apk的文件名 */
	public void setSaveFileName(String saveFileName) {
		this.saveFileName = saveFileName;
	}

	
}
