package com.zooma.plugins.cameraoverlay;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.zooma.peacemaker.R;
import com.zooma.peacemaker.R.id;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class CameraActivity extends Activity {
    protected static final String TAG = "Debug";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
	private Camera mCamera;
    private CameraPreview mPreview;
    private String state = "";
    public static final int CAMERA_ACTIVITY_RESULT = 0;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("SnapCamera plugin","Camera activity onCreate");
        setContentView(R.layout.camerapreview);

        // Create an instance of Camera
        mCamera = getCameraInstance();
    
        
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        
        final PictureCallback mPicture = new PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                if (pictureFile == null){
                    Log.d(TAG, "Error creating media file, check storage permissions");
                    return;
                }
                try {
					FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    Log.d(TAG, data.length+  " written");
                    fos.close();
                } catch (FileNotFoundException e) {
                    Log.d(TAG, "File not found: " + e.getMessage());
                } catch (IOException e) {
                    Log.d(TAG, "Error accessing file: " + e.getMessage());
                }
                Intent resultIntent = new Intent();
                Log.d(TAG, "URI : " + "file://" + pictureFile.getPath());
                resultIntent.putExtra("ImageURI", "file://" + pictureFile.getPath());
                resultIntent.putExtra("ImageFile", pictureFile.getPath());
                resultIntent.putExtra("State", CameraActivity.this.state);
                setResult(Activity.RESULT_OK, resultIntent);
                Log.d(TAG, "file size = " + pictureFile.length());
                finish();
            }
        };
        
        
        ImageButton imokButton = (ImageButton) findViewById(id.button_noworries);
        ImageButton closeButton = (ImageButton) findViewById(id.button_close);
        ImageButton helpButton = (ImageButton) findViewById(id.button_help);
        
        imokButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                    	CameraActivity.this.state = "OK";
                        mCamera.takePicture(null, null, mPicture);
                    }
                }
            );
        
        closeButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // return immediately
                        setResult(Activity.RESULT_CANCELED);
                        finish();
                    }
                }
            );
        
        helpButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	CameraActivity.this.state = "HELP";
                    // get an image from the camera
                    mCamera.takePicture(null, null, mPicture);
                }
            }
        );
    }

    //Release camera, else it will crash or get stuck.
    @Override
	protected void onDestroy() {
    	
        releaseCamera();
        super.onDestroy();
	}
    
    @Override
	protected void onResume() {
    	super.onResume();
    };
    
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
        releaseCamera();
    }

	/** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "HelpImOK-App");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        } else {
        	Log.i("SnapCamera Plugin", "Media storage exists");
        }
        Log.d(TAG, "(mediaStorageDir.getPath() = " + (mediaStorageDir.getPath()));
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
    
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
    
            Parameters params = c.getParameters();
            List <Camera.Size> sizes = params.getSupportedPictureSizes();
            int w = 0;
            int h = 0;
            for (int i=0;i<sizes.size();i++){
            	if (w == 0 || w > 1000) {
                	w = sizes.get(i).width;
                	h = sizes.get(i).height;
            	}
                Log.i("PictureSize", "Supported Size: " +sizes.get(i).width + "height : " + sizes.get(i).height);
            }
            
            params.set("jpeg-quality", 50);
            params.setPictureFormat(PixelFormat.JPEG);
            params.setPictureSize(w, h);
            params.setRotation(90);
            
            c.setParameters(params);
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        	Log.d("Debug", "CameraActivity : error "+e);
        }
        Log.d("Debug", "CameraActivity : Returning "+c);
        return c; // returns null if camera is unavailable
    }
    
    private void releaseCamera(){
        if (mCamera != null){
            mCamera.stopPreview();
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }
}