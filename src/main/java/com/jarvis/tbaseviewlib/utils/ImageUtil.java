package com.jarvis.tbaseviewlib.utils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jarvis.tbaseviewlib.data.CacheData;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * 图像处理辅助类
 */
@SuppressLint({ "DefaultLocale", "SimpleDateFormat" })
@SuppressWarnings("unused")
public class ImageUtil {
	/**
	 * 请求从相册中获取图片
	 * 
	 * @param fragment
	 * @param requestCode
	 */
	public static void pickImage(Fragment fragment, int requestCode) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		fragment.startActivityForResult(intent, requestCode);
	}

	/**
	 * 从相机中获取图片
	 * 
	 * @param fragment
	 * @param requestCode
	 */
	public static void takePhoto(Fragment fragment, int requestCode) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fragment.startActivityForResult(intent, requestCode);
	}

	/**
	 * 进行裁剪的图像大小
	 * 
	 * @param fragment
	 * @param imgSize
	 * @param uri
	 * @param file
	 * @param requestCode
	 */
	public static void cropImage(Fragment fragment, int imgSize, Uri uri, File file, int requestCode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");

		// //////////////////////////////////////////////////////////////
		// 1.宽高和比例都不设置时,裁剪框可以自行调整(比例和大小都可以随意调整)
		// //////////////////////////////////////////////////////////////
		// 2.只设置裁剪框宽高比(aspect)后,裁剪框比例固定不可调整,只能调整大小
		// //////////////////////////////////////////////////////////////
		// 3.裁剪后生成图片宽高(output)的设置和裁剪框无关,只决定最终生成图片大小
		// //////////////////////////////////////////////////////////////
		// 4.裁剪框宽高比例(aspect)可以和裁剪后生成图片比例(output)不同,此时,
		// 会以裁剪框的宽为准,按照裁剪宽高比例生成一个图片,该图和框选部分可能不同,
		// 可能是截取框选的一部分,也可能超出框选部分,由框选部分顶端向下延伸补足
		// //////////////////////////////////////////////////////////////

		// aspectX aspectY 是裁剪框宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪后生成图片的宽高
		intent.putExtra("outputX", imgSize);
		intent.putExtra("outputY", imgSize);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		intent.putExtra("return-data", true);
		fragment.startActivityForResult(intent, requestCode);
	}

	/** 重新计算图片框架尺寸 **/
	public static void resizeImageView(ImageView imgView, float scale) {
		imgView.measure(0, 0);
		int width = imgView.getMeasuredWidth();
		ViewGroup.LayoutParams lp = imgView.getLayoutParams();
		lp.height = (int) (width / scale);
		imgView.setLayoutParams(lp);
	}

	/**
	 * 图片转成十六进制
	 * 
	 * @author Administrator
	 * 
	 */
	public static String ImageToHex(String picPath) {
		String str = "";
		try {
			FileInputStream fis = new FileInputStream(picPath);
			BufferedInputStream bis = new BufferedInputStream(fis);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			byte[] buff = new byte[1024];
			int len = 0;
			while ((len = fis.read(buff)) != -1) {
				bos.write(buff, 0, len);
			}
			// 得到图片的字节数组
			byte[] result = bos.toByteArray();

			// System.out.println("图片转字节：" + byte2HexStr(result));
			// 字节数组转成十六进制
			str = bytesToHexString(result);
			/*
			 * 将十六进制串保存到txt文件中
			 */
			// PrintWriter pw = new PrintWriter(new
			// FileWriter(CacheData.INFO_CACHE+"image16test.txt"));
			// pw.println(str);
			// pw.close();
			bis.close();
			fis.close();
		} catch (IOException e) {
		}
		return str;

	}

	/**
	 * 实现字节数组向十六进制的转换方法一
	 */
	private static String byte2HexStr(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}

	/**
	 * 实现字节数组向十六进制的转换的方法二
	 */
	private static String bytesToHexString(byte[] b) {

		StringBuilder stringBuilder = new StringBuilder("");
		if (b == null || b.length <= 0) {
			return null;
		}
		for (int i = 0; i < b.length; i++) {
			int v = b[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();

	}

	/**
	 * 图片转成Base64
	 * 
	 * @param imgPath
	 *            图片路径
	 * @return
	 */
	public static String imgToBase64(String imgPath) {
		Bitmap bitmap = null;
		if (imgPath != null && imgPath.length() > 0) {
			bitmap = BitmapFactory.decodeFile(imgPath);
		}
		if (bitmap == null) {
			// bitmap not found!!
			return null;
		}
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

			out.flush();
			out.close();

			byte[] imgBytes = out.toByteArray();
			return Base64.encodeToString(imgBytes, Base64.DEFAULT);
		} catch (Exception e) {
			return null;
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Base64转成图片
	 * 
	 * @param base64Data
	 * @param imgName
	 *            保存图片的名称
	 */
	public static void base64ToBitmap(String base64Data, String imgName) {
		byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
		Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		if (TextUtils.isEmpty(imgName)) {
			imgName = getFormatTimeByCustom(System.currentTimeMillis(), "yyyyMMdd_HHmmss");
		}
		File myCaptureFile = new File(CacheData.getImagesCache(), imgName + ".jpg");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(myCaptureFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		boolean isTu = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
		if (isTu) {
			// fos.notifyAll();
			try {
				fos.flush();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 旋转图片
	 * 
	 * @param angle
	 *            旋转角度
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	/** 从给定的路径加载图片，并指定是否自动旋转方向 为正常方向 */
	public static Bitmap loadBitmap(String imgpath, boolean adjustOritation) {
		if (!adjustOritation) {
			return BitmapFactory.decodeFile(imgpath);
		} else {
			Bitmap bm = BitmapFactory.decodeFile(imgpath);
			int digree = 0;
			ExifInterface exif = null;
			try {
				exif = new ExifInterface(imgpath);
			} catch (IOException e) {
				e.printStackTrace();
				exif = null;
			}
			if (exif != null) {
				// 读取图片中相机方向信息
				int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
				// 计算旋转角度
				switch (ori) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					digree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					digree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					digree = 270;
					break;
				default:
					digree = 0;
					break;
				}
			}
			if (digree != 0) {
				// 旋转图片
				Matrix m = new Matrix();
				m.postRotate(digree);
				bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
			}
			return bm;
		}
	}

	/**
	 * 功能说明： 简单的时间格式
	 * <p>
	 * 作者: jarvisT
	 * 
	 * @param time
	 *            需要格式化的时间
	 * @param format
	 *            可为null或者“” 默认格式化的模版（"MM-dd HH:mm"）
	 * @return 返回模版化后的时间
	 */
	private static String getFormatTimeByCustom(long time, String format) {
		if (0 == time) {
			return "";
		}
		SimpleDateFormat mDateFormat = null;
		if (!TextUtils.isEmpty(format)) {
			mDateFormat = new SimpleDateFormat(format);
		} else {
			mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
		}
		return mDateFormat.format(new Date(time));

	}

}