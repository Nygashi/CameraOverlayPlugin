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
	protected static final String TAG = "CameraOverlay plugin (CameraOverlay)";
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
    	Log.i(TAG, "action = " + action);
    	
    	this.currentCallbackContext = callbackContext;
    	
    	try {
    		Log.i(TAG, action);
            if (action.equals("echo")) {
                String echo = args.getString(0); 
                if (echo != null && echo.length() > 0) { 
                	PluginResult result = new PluginResult(PluginResult.Status.OK, echo);
                    callbackContext.sendPluginResult(result);
                    return true;
//                    return new  PluginResult(PluginResult.Status.OK, e.getMessage());
                } else {
                	PluginResult result = new PluginResult(PluginResult.Status.ERROR);
                    callbackContext.sendPluginResult(result);
                    return false;
//                    return new PluginResult(PluginResult.Status.ERROR);
                }
            }else if (action.equals("showCamera")){
            	//todo, write the codes yo
            	Log.i(TAG, "starting camera");
            	//Camera c = this.getCameraInstance();
            	Context context= cordova.getActivity().getApplicationContext();
                //or Context context=cordova.getActivity().getApplicationContext();
                Intent intent= new Intent(context, CameraActivity.class);
                //context.startActivity(intent);
                cordova.setActivityResultCallback(CameraOverlay.this); 
                cordova.startActivityForResult(this, intent, CameraActivity.CAMERA_ACTIVITY_RESULT);
                //or cordova.getActivity().startActivity(intent);
                PluginResult r = new PluginResult(PluginResult.Status.NO_RESULT);
                this.callbackId = callbackContext.getCallbackId();
                r.setKeepCallback(true);
//                return r;
                return true;
            } 
            else if (action.equals("refreshCallBackId")){
            	Log.i(TAG, "refreshCallBackId");
                PluginResult r = new PluginResult(PluginResult.Status.NO_RESULT);
                this.callbackId = callbackContext.getCallbackId();
                r.setKeepCallback(true);
//                return r;
                callbackContext.sendPluginResult(r);
                return true;
            }
            else if(action.equals("hideActivityLoader")){
            	Log.i(TAG, "hideActivityLoader");
            	mProgressDialog.dismiss();
                Context context= cordova.getActivity().getApplicationContext();
                //or Context context=cordova.getActivity().getApplicationContext();
                Intent camIntent= new Intent(context, CameraActivity.class);
                //context.startActivity(intent);
                cordova.setActivityResultCallback(CameraOverlay.this); 
                cordova.startActivityForResult(this, camIntent, CameraActivity.CAMERA_ACTIVITY_RESULT);
                PluginResult r = new PluginResult(PluginResult.Status.NO_RESULT);
                //this.callbackId = callbackId;
                //r.setKeepCallback(true);
                callbackContext.sendPluginResult(r);
                return true;
//                return r;            	
            }
    	    else {
    	    	PluginResult result = new PluginResult(PluginResult.Status.INVALID_ACTION);
                callbackContext.sendPluginResult(result);
    	    	return false;
//                return new PluginResult(PluginResult.Status.INVALID_ACTION);   
            }
        } catch (JSONException e) {
        	PluginResult result = new PluginResult(PluginResult.Status.JSON_EXCEPTION);
        	Log.e("Snapcamera Plugin", e.getMessage());
        	callbackContext.sendPluginResult(result);
        	return false;
//            return result;
        }
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
        Log.i("Snapcamera Plugin", "In onActivityResult");
        
        if (requestCode == CameraActivity.CAMERA_ACTIVITY_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
            	
            	mProgressDialog = ProgressDialog.show(cordova.getActivity(),  "Please wait", "sending please wait...", true);
            	mProgressDialog.setCancelable(false);
                String imageURI = intent.getStringExtra("ImageURI");
                Log.i(TAG, imageURI);
                String state = intent.getStringExtra("State");
                JSONObject jsonResult = new JSONObject();
                try {
					jsonResult.put("uri", imageURI);
					jsonResult.put("state", state);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					PluginResult pluginResult = new PluginResult(PluginResult.Status.ERROR , this.callbackId);
					this.currentCallbackContext.sendPluginResult(pluginResult);
					
//					this.error(new PluginResult(PluginResult.Status.ERROR, e.getMessage()), this.callbackId);
				}

                // -----------------------
                PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, jsonResult);
				this.currentCallbackContext.sendPluginResult(pluginResult);
				
//                this.success(new PluginResult(PluginResult.Status.OK, jsonResult), this.callbackId);
                
            } else if (resultCode == Activity.RESULT_CANCELED) {
            	JSONObject jsonResult = new JSONObject();
                try {
                	jsonResult.put("uri", "");
					jsonResult.put("state", "CANCELLED");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					 PluginResult pluginResult = new PluginResult(PluginResult.Status.ERROR, this.callbackId);
					this.currentCallbackContext.sendPluginResult(pluginResult);
//					this.error(new PluginResult(PluginResult.Status.ERROR, e.getMessage()), this.callbackId);
				}            	
                PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, jsonResult);
       			this.currentCallbackContext.sendPluginResult(pluginResult);
//                this.success(new PluginResult(PluginResult.Status.OK, jsonResult), this.callbackId);
            } else {
                PluginResult pluginResult = new PluginResult(PluginResult.Status.ERROR, this.callbackId);
       			this.currentCallbackContext.sendPluginResult(pluginResult);
//                this.error(new PluginResult(PluginResult.Status.ERROR), this.callbackId);
            }
        }
    }
}
