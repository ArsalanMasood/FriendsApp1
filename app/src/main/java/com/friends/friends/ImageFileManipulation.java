package com.friends.friends;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;

public class ImageFileManipulation {

	private Activity context = null;
	public static final String EXTERNAL_MEMORY = "/sdcard/";
	public static final String EXTERNAL_MEMORY_PROFILE = "/sdcard/.LoYakk/profile/";
	public static final String AT_EXIT = "atexit";
	public static final String DAILY = "daily";
	public static final String WEEKLY = "weekly";
	public static final String MONTHLY = "monthly";
	public static final String NEVER = "never";
	public static int MESSAGE_IMAGE_HEIGHT_TO_UPLOAD = 150;
	public static int MESSAGE_IMAGE_WIDTH_TO_UPLOAD = 150;
	public static String DELETE_CACHE_INTERVAL = WEEKLY;
	public static int PROFILE_IMAGE_HEIGHT_TO_UPLOAD = 60;
	public static int PROFILE_IMAGE_WIDTH_TO_UPLOAD = 60;
	public static double IAMGE_COMPRESSION_RATIO = 0.3;

	public ImageFileManipulation(Activity contx) {
		context = contx;
	}

	public Bitmap createBitmapFromUri(Uri uri) {
		Bitmap imageBitmap = null;
		try {
			ContentResolver contentResolver = context.getContentResolver();
			InputStream inputStream = contentResolver.openInputStream(uri);
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inTempStorage = new byte[16 * 1024];
			opt.inSampleSize = 1;
			opt.outWidth = 50;
			opt.outHeight = 40;
			imageBitmap = BitmapFactory.decodeStream(inputStream, new Rect(), null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return imageBitmap;
	}

	public String saveImage(Uri image, int width, int height) {
		String imagePath = null;
		try {

			byte[] data = readFileThroughUri(image);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.outWidth = height;
			options.outHeight = width;
			options.inSampleSize = 2;
			Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length, options);
			Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, width, height, false);
			bmp.recycle();
			bmp = null;
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			scaledBmp.compress(Bitmap.CompressFormat.JPEG, (int) (100 * ImageFileManipulation.IAMGE_COMPRESSION_RATIO), bytes);
			imagePath = ImageFileManipulation.EXTERNAL_MEMORY + getFileName() + ".jpeg";
			boolean isFileCreate = createFileInData(imagePath, bytes.toByteArray());
			if (!isFileCreate)// if file is not created return null
				return null;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return imagePath;
	}

	public static String getFileName() {
		try {
			Calendar cal = Calendar.getInstance();
			return cal.get(Calendar.YEAR) + "" + (cal.get(Calendar.MONTH) + 1) + "" + cal.get(Calendar.DAY_OF_MONTH) + "_" + cal.get(Calendar.HOUR) + "" + cal.get(Calendar.MINUTE) + "" + cal.get(Calendar.SECOND);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public boolean createFileInData(String path, byte[] buffer) {
		FileOutputStream fileOutputStream = null;
		try {
			File file = new File(path);
			if (file.createNewFile()) {
				fileOutputStream = new FileOutputStream(file);
				fileOutputStream.write(buffer, 0, buffer.length);
				fileOutputStream.flush();
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
					fileOutputStream = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	public byte[] readFileThroughUri(Uri uri) {
		Cursor cursor = null;
		File file = null;
		FileInputStream fInputStream = null;
		byte[] fileBuffer = null;
		try {
			String[] proj = { MediaColumns.DATA, BaseColumns._ID };
			cursor = context.managedQuery(uri, // The URI of the content provider to query
					proj, // Which columns to return
					null, // WHERE clause; which rows to return (all rows)
					null, // WHERE clause selection arguments (none)
					null); // Order-by clause (ascending by name)

			int file_ColumnIndex = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			if (cursor.moveToFirst()) {
				file = new File(cursor.getString(file_ColumnIndex));
				fInputStream = new FileInputStream(file);
				byte[] byteArray = new byte[fInputStream.available()];
				int length = byteArray.length;
				fInputStream.read(byteArray, 0, length);
				BitmapFactory.Options o2 = new BitmapFactory.Options();
				o2.inPurgeable = true;
				o2.inPreferredConfig = Bitmap.Config.ARGB_8888;
				Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, length, o2);
				Bitmap temp = ThumbnailUtils.extractThumbnail(bmp, MESSAGE_IMAGE_WIDTH_TO_UPLOAD, MESSAGE_IMAGE_HEIGHT_TO_UPLOAD);
				if (bmp != null) {
					bmp.recycle();
					bmp = null;
				}
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				temp.compress(Bitmap.CompressFormat.JPEG, (int) (100 * ImageFileManipulation.IAMGE_COMPRESSION_RATIO), stream);
				fileBuffer = stream.toByteArray();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (cursor != null) {
					cursor.close();
				}
				if (file != null) {
					file = null;
				}
				if (fInputStream != null) {
					fInputStream.close();
					fInputStream = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return fileBuffer;
	}

	public boolean deleteFileFromData(String path) {
		try {
			File file = new File(path);
			if (file.exists() && file.delete())
				return true;
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public Bitmap decodeThumbFile(String path) {
		Bitmap bmp = null;
		Log.v("File Path", path);
		File f = new File(path);
		try {
			// Decode image size
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inTempStorage = new byte[16 * 1024];
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, options);
			int IMAGE_MAX_SIZE = 640;
			int scale = 1;
			if (options.outHeight > IMAGE_MAX_SIZE || options.outWidth > IMAGE_MAX_SIZE) {
				scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(options.outHeight, options.outWidth)) / Math.log(0.5)));
			}
			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			o2.inPurgeable = true;
			o2.outHeight = 480;
			o2.outWidth = 640;

			Bitmap temp = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			temp.compress(Bitmap.CompressFormat.PNG, (int) (100 * ImageFileManipulation.IAMGE_COMPRESSION_RATIO), baos);
			temp.recycle();
			temp = null;
			options = new BitmapFactory.Options();
			options.inDither = true;
			options.inPurgeable = true;
			options.inInputShareable = true;
			options.inSampleSize = scale;
			options.inTempStorage = new byte[32 * 1024];
			bmp = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.size(), options);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bmp;
	}

	public Bitmap getThumbnail(Uri uri, int THUMBNAIL_SIZE) throws FileNotFoundException, IOException {
		InputStream input = context.getContentResolver().openInputStream(uri);

		BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
		onlyBoundsOptions.inJustDecodeBounds = true;
		onlyBoundsOptions.inDither = true;// optional
		onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
		BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
		input.close();
		if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
			return null;
		int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;
		double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;
		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
		bitmapOptions.inDither = true;// optional
		bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
		input = context.getContentResolver().openInputStream(uri);
		Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
		int width=bitmap.getWidth();
		int height=bitmap.getHeight();
		input.close();
		return bitmap;
	}

	public static int getPowerOfTwoForSampleRatio(double ratio) {
		int k = Integer.highestOneBit((int) Math.floor(ratio));
		if (k == 0)
			return 1;
		else
			return k;
	}

	public String getPath(Uri uri) {
		Cursor cursor = null;
		int column_index = 0;
		try {
			String[] projection = { MediaColumns.DATA };
			cursor = context.managedQuery(uri, projection, null, null, null);
			column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			cursor.moveToFirst();
		} catch (Exception e) {
			Log.d("Error", "Exception Occured", e);
		}
		return cursor.getString(column_index);
	}

	public String saveBitmap(Bitmap bmp) {
		String fileName = ImageFileManipulation.EXTERNAL_MEMORY + getFileName() + ".jpeg";
		try {
			FileOutputStream out = new FileOutputStream(fileName);
			bmp.compress(Bitmap.CompressFormat.JPEG, (int) (100 * ImageFileManipulation.IAMGE_COMPRESSION_RATIO), out);
			out.flush();
			out.close();
			out = null;
		} catch (Exception e) {
			e.printStackTrace();
			fileName = "NA";
		}

		return fileName;
	}

	public Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap output = null;
		try {
			output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			final RectF rectF = new RectF(rect);
			final float roundPx = 4;
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return output;
	}

}
