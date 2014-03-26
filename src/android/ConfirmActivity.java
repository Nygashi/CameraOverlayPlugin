package com.zooma.plugins.cameraoverlay;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.zooma.peacemaker.R;

public class ConfirmActivity extends Activity {
	public static final int CONFIRM_ACTIVITY_RESULT = 7777;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm);
		
		
		Log.d("Debug", "ConfirmActivity started!");
		Intent receivedIntent = getIntent();
		String imagePath = receivedIntent.getStringExtra("ImageFile");
		Log.d("Debug", "Received image path : "+ imagePath);
		
		Bitmap bitmap;
		ImageView photoPreviewImage = (ImageView)findViewById(R.id.PhotoPreviewImage);
		try {
	        File f = new File(imagePath);
	        ExifInterface exif = new ExifInterface(f.getPath());
	        int orientation = exif.getAttributeInt(
	                ExifInterface.TAG_ORIENTATION,
	                ExifInterface.ORIENTATION_NORMAL);

	        int angle = 0;

	        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
	            angle = 90;
	        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
	            angle = 180;
	        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
	            angle = 270;
	        }

	        Matrix mat = new Matrix();
	        mat.postRotate(angle);
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inSampleSize = 1;

	        Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f),
	                null, options);
	        Log.d("Debug", "Bitmap WIDTH :: "+bmp.getWidth());
	        bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
	                bmp.getHeight(), mat, true);
	        ByteArrayOutputStream outstudentstreamOutputStream = new ByteArrayOutputStream();
	        bitmap.compress(Bitmap.CompressFormat.PNG, 100,
	                outstudentstreamOutputStream);
	        photoPreviewImage.setImageBitmap(bitmap);

	    } catch (IOException e) {
	        Log.w("TAG", "-- Error in setting image");
	    } catch (OutOfMemoryError oom) {
	        Log.w("TAG", "-- OOM Error in setting image");
	    }
	}
}
