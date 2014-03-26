package com.zooma.plugins.cameraoverlay;

//import org.apache.cordova.api.Plugin;
//import org.apache.cordova.api.PluginResult;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.util.Log;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

/**
 * This class echoes a string called from JavaScript.
 */
@SuppressWarnings("deprecation")
public class CameraOverlay extends CordovaPlugin {
	protected static final String TAG = "Debug";
    private String callbackId;
	private ProgressDialog mProgressDialog;
	private CallbackContext currentCallbackContext;

	/**
     * Executes the request and returns PluginResult.
     *
     * @param action        The action to execute.
     * @param args          JSONArry of arguments for the plugin.
     * @param callbackId    The callback id used when calling back into JavaScript.
     * @return              A PluginResult object with a status and message.
     */
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
    	Log.d(TAG, "plugin working");
    	Log.d(TAG, "action = " + action);
    	
    	this.currentCallbackContext = callbackContext;
    	
		if (action.equals("showCamera")){
        	//todo, write the codes yo
        	Log.i(TAG, "starting camera");
        	Context context= cordova.getActivity().getApplicationContext();
            Intent intent= new Intent(context, CameraActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            cordova.setActivityResultCallback(CameraOverlay.this); 
            cordova.startActivityForResult(this, intent, CameraActivity.CAMERA_ACTIVITY_RESULT);


            return true;
        } 
        else if(action.equals("hideActivityLoader")){
        	Log.d(TAG, "hideActivityLoader");

        	currentCallbackContext.success();
        }
		return false;
    }
    

    
    /**
     * Called when the intent completes
     *
     * @param requestCode       The request code originally supplied to startActivityForResult(),
     *                          allowing you to identify who this result came from.
     * @param resultCode        The integer result code returned by the child activity through its setResult().
     * @param intent            An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d(TAG, "In onActivityResult");
        
        if (requestCode == CameraActivity.CAMERA_ACTIVITY_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
            	
                String imageURI = intent.getStringExtra("ImageURI");
                String imageFile = intent.getStringExtra("ImageFile");
               
                Context context= cordova.getActivity().getApplicationContext();
                Intent confirmIntent = new Intent(context, ConfirmActivity.class);
                confirmIntent.putExtra("ImageFile", imageFile);
                confirmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                context.startActivity(confirmIntent);
                
                String state = intent.getStringExtra("State");
                JSONObject jsonResult = new JSONObject();
                try {
					jsonResult.put("uri", imageURI);
					jsonResult.put("state", state);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					this.currentCallbackContext.error("JSONException : "+e);
				}
				this.currentCallbackContext.success(jsonResult);
                
            } else if (resultCode == Activity.RESULT_CANCELED) {
            	JSONObject jsonResult = new JSONObject();
                try {
                	jsonResult.put("uri", "");
					jsonResult.put("state", "CANCELLED");
				} catch (JSONException e) {
					this.currentCallbackContext.error("JSONException : "+e);
				}            	
        		this.currentCallbackContext.success(jsonResult);
            } else {
            	this.currentCallbackContext.error("Unknown Error in OnActivityResult");
            }
        }
    }
}
