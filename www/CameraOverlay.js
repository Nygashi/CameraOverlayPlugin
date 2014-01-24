    var CameraOverlay = function() {
        CameraOverlay.prototype.oParams = {};
        CameraOverlay.prototype.fotoDict = '';
    };


    CameraOverlay.prototype.showCamera = function() {

        success = function(fotoDict){

            console.log('Success Callback.');
        	if(fotoDict.state == 'CANCELLED'){
        		return;
        	}

            CameraOverlay.prototype.fotoDict = fotoDict;

            //Check connection, else show alertview
            if(PeaceMaker.connected){
                console.log('Connected - Preparing upload...');
                CameraOverlay.prototype.prepareUploadPhoto();
            }else{
                if(PeaceMaker.deviceOS == 'iOS'){
                    CameraOverlay.prototype.showAlert();
                    cordova.exec(null, null, "CameraOverlay", "hideActivityLoader",[]);
                }else{
                    CameraOverlay.prototype.prepareUploadPhoto();
                }
            }
            CameraOverlay.prototype.refreshCallbackId();
        };

        fail = function(){
            console.log('FAIL CALLBACK CAMERASTARTUP');
            console.log('failure');
        };

        return cordova.exec(success, fail, "CameraOverlay", "showCamera",[]);
    };

    CameraOverlay.prototype.refreshCallbackId = function() {
        success = function(fotoDict){
        	if(fotoDict.state == 'CANCELLED'){
        		return;
        	}
            console.log('refreshCallbackId success');

            CameraOverlay.prototype.fotoDict = fotoDict;

            if(PeaceMaker.connected){
                CameraOverlay.prototype.prepareUploadPhoto();
            }else{
                if(PeaceMaker.deviceOS == 'iOS'){
                    CameraOverlay.prototype.showAlert();
                    cordova.exec(null, null, "CameraOverlay", "hideActivityLoader",[]);
                }else{
                    CameraOverlay.prototype.prepareUploadPhoto();
                }
            }

            CameraOverlay.prototype.refreshCallbackId();
        };
        fail = function(){
            console.log('FAIL CALLBACK REFRESH');
            console.log('refreshCallbackId failure');
        };

        return cordova.exec(success, fail, "CameraOverlay", "refreshCallBackId",[]);
    };


    // Geo lat lng erbij ophalen
    CameraOverlay.prototype.prepareUploadPhoto = function() {
        console.log('Initting Geolocation');
        GeoLocation.init(CameraOverlay.prototype.onSuccessGeoLocation, CameraOverlay.prototype.onErrorGeoLocation);
        console.log('Getting current position');
        GeoLocation.getCurrentPosition();
    };

    CameraOverlay.prototype.onErrorGeoLocation = function() {

        console.log('CameraOverlay.onErrorGeoLocation');

        // Add geo error
        CameraOverlay.prototype.oParams.error                = GeoLocation.error;

        CameraOverlay.prototype.uploadData();
    };

    CameraOverlay.prototype.onSuccessGeoLocation = function() {

        console.log('CameraOverlay.onSuccessGeoLocation');

        // Add geo
        CameraOverlay.prototype.oParams.lat                  = GeoLocation.lat;
        CameraOverlay.prototype.oParams.lng                  = GeoLocation.lng;
        CameraOverlay.prototype.oParams.error                = GeoLocation.error;

        console.log(CameraOverlay.oParams);
        console.log('Ready to upload data..');
        CameraOverlay.prototype.uploadData();
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
        CameraOverlay.prototype.oParams.key                   = PeaceMaker.uploadKey;
        CameraOverlay.prototype.oParams.devicePlatform        = PeaceMaker.devicePlatform;
        CameraOverlay.prototype.oParams.deviceVersion         = PeaceMaker.deviceVersion;
        CameraOverlay.prototype.oParams.deviceOS              = PeaceMaker.deviceOS;
        CameraOverlay.prototype.oParams.state                 = CameraOverlay.prototype.fotoDict.state;

        console.log('oParams filled.. continueing filecommunication..');

        // Start uploading
        Filecommunication.init(CameraOverlay.prototype.onSuccessFilecommunication, CameraOverlay.prototype.onErrorFilecommunication);
        Filecommunication.fileURI                   = CameraOverlay.prototype.fotoDict.uri;
        Filecommunication.uploadUrl                 = sUrl;
        Filecommunication.params                    = CameraOverlay.prototype.oParams;

        console.log('Uploading file..');
        Filecommunication.uploadFile();
    };
 
    CameraOverlay.prototype.onSuccessFilecommunication = function(sMessage) {

        console.log('CameraOverlay.onSuccessFilecommunication = ' + sMessage);

        if(!PeaceMaker.connected){
            CameraOverlay.prototype.showAlert();
        }

        // Verberg de native loading message
        return cordova.exec(null, null, "CameraOverlay", "hideActivityLoader",[]);
    };
 
    CameraOverlay.prototype.onErrorFilecommunication = function(oError, sMessage) {
        if(!PeaceMaker.connected){
            CameraOverlay.prototype.showAlert();
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