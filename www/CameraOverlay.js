
    var CameraOverlay = function() {
        CameraOverlay.oParams = {};
        CameraOverlay.fotoDict = '';
    };


    CameraOverlay.prototype.showCamera = function() {
        success = function(fotoDict){
        	if(fotoDict.state == 'CANCELLED'){
        		return;
        	}
            CameraOverlay.fotoDict = fotoDict;
            //Check connection, else show alertview
            if(PeaceMaker.connected){
                CameraOverlay.prepareUploadPhoto();
            }else{
                if(PeaceMaker.deviceOS == 'iOS'){
                    CameraOverlay.showAlert();
                    cordova.exec(null, null, "CameraOverlay", "hideActivityLoader",[]);
                }else{
                    CameraOverlay.prepareUploadPhoto();
                }
            }

            window.plugins.CameraOverlay.refreshCallbackId(); 

        };
        fail = function(){
            console.log('FAIL CALLBACK CAMERASTARTUP');
            console.log('failure');
        };

        return cordova.exec(success, fail, "CameraOverlay", "showCamera",[]);
    };

    CameraOverlay.prototype.refreshCallbackId = function() {
        success = function(fotoDict){
            console.log('SUCCESS CALLBACK REFRESH')
        	if(fotoDict.state == 'CANCELLED'){
        		return;
        	}
            console.log('refreshCallbackId success');
 
            CameraOverlay.fotoDict = fotoDict;

            if(PeaceMaker.connected){
                CameraOverlay.prepareUploadPhoto();
            }else{
                if(PeaceMaker.deviceOS == 'iOS'){
                    CameraOverlay.showAlert();
                    cordova.exec(null, null, "CameraOverlay", "hideActivityLoader",[]);
                }else{
                    CameraOverlay.prepareUploadPhoto();
                }
            }
 
            window.plugins.CameraOverlay.refreshCallbackId();
        };
        fail = function(){
            console.log('FAIL CALLBACK REFRESH');
            console.log('refreshCallbackId failure');
        };

        return cordova.exec(success, fail, "CameraOverlay", "refreshCallBackId",[]);
    };


    // Geo lat lng erbij ophalen
    CameraOverlay.prototype.prepareUploadPhoto = function() {

        GeoLocation.init(CameraOverlay.onSuccessGeoLocation, CameraOverlay.onErrorGeoLocation);
        GeoLocation.getCurrentPosition();
    };

    CameraOverlay.prototype.onErrorGeoLocation = function() {

        console.log('CameraOverlay.onErrorGeoLocation');

        // Add geo error
        CameraOverlay.oParams.error                = GeoLocation.error;

        CameraOverlay.uploadData();
    };

    CameraOverlay.prototype.onSuccessGeoLocation = function() {

        console.log('CameraOverlay.onSuccessGeoLocation');

        // Add geo
        CameraOverlay.oParams.lat                  = GeoLocation.lat;
        CameraOverlay.oParams.lng                  = GeoLocation.lng;
        CameraOverlay.oParams.error                = GeoLocation.error;

        console.log(CameraOverlay.oParams);
        CameraOverlay.uploadData();
    };

 
 
    // Handle foto versturen naar server
    CameraOverlay.prototype.uploadData = function() {
        var db_user_id = JSON.parse(window.localStorage.getItem("db_user_id"));
        console.log('CameraOverlay uploaddata for dbuserID: '+db_user_id);
        var sUrl = PeaceMaker.mediaUploadUrl + 'uploadimage.php?';
        sUrl += "db_user_id=" + db_user_id;
        sUrl += "&key=" + PeaceMaker.apiKey;

        console.log('SURL UPLOAD:: '+sUrl);
        // De alertview wordt native aangeroepen

        // Key + device info
        CameraOverlay.oParams.key                   = PeaceMaker.uploadKey;
        CameraOverlay.oParams.devicePlatform        = PeaceMaker.devicePlatform;
        CameraOverlay.oParams.deviceVersion         = PeaceMaker.deviceVersion;
        CameraOverlay.oParams.deviceOS              = PeaceMaker.deviceOS;
        CameraOverlay.oParams.state                 = CameraOverlay.fotoDict.state;

        // Start uploading
        Filecommunication.init(CameraOverlay.onSuccessFilecommunication, CameraOverlay.onErrorFilecommunication);
        Filecommunication.fileURI                   = CameraOverlay.fotoDict.uri;
        Filecommunication.uploadUrl                 = sUrl;
        Filecommunication.params                    = CameraOverlay.oParams;
        Filecommunication.uploadFile();
    };
 
    CameraOverlay.prototype.onSuccessFilecommunication = function(sMessage) {

        console.log('CameraOverlay.onSuccessFilecommunication = ' + sMessage);

        if(!PeaceMaker.connected){
            CameraOverlay.showAlert();
        }

        // Verberg de native loading message
        return cordova.exec(null, null, "CameraOverlay", "hideActivityLoader",[]);
    };
 
    CameraOverlay.prototype.onErrorFilecommunication = function(oError, sMessage) {
        if(!PeaceMaker.connected){
           CameraOverlay.showAlert();
        }

        // Verberg de native loading message
        return cordova.exec(null, null, "CameraOverlay", "hideActivityLoader",[]);
    };

    CameraOverlay.prototype.showAlert =function(){
        Alertview.alert("No network", "This app needs a working network connection. Check the network connection and try again.");
    };

    if (!window.plugins) {
        window.plugins = {};
    }

    if (!window.plugins.CameraOverlay) {
        window.plugins.CameraOverlay = new CameraOverlay();
    }
    if (module.exports) {
        module.exports = new CameraOverlay();
    }

