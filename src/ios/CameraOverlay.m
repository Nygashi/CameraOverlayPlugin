//
//  CameraOverlay.m
//  Peacemaker
//
//  Created by Tijmen van Linden on 3/29/13.
//
//

#import "CameraOverlay.h"
#import "AppDelegate.h"

@implementation CameraOverlay
@synthesize callbackId;
@synthesize loaderCallbackId;
@synthesize HUD;
@synthesize cameraOverlayViewController;


#pragma mark Cordova Calls
- (void)showCamera:(CDVInvokedUrlCommand *)command
{
    NSLog(@"SHOW CAMERA CALLED!");
    self.callbackId = command.callbackId;
    
    if (cameraOverlayViewController == nil) {
        self.cameraOverlayViewController = [[CameraOverlayViewController alloc]initWithNibName:@"CameraOverlayViewController" bundle:nil];
        self.cameraOverlayViewController.delegate = self;
        self.cameraOverlayViewController.animationEnabled = YES;
        [self.viewController presentModalViewController:cameraOverlayViewController animated:self.cameraOverlayViewController.animationEnabled];
    }
}
- (void)hideActivityLoader:(CDVInvokedUrlCommand *)command{
    
    self.loaderCallbackId = command.callbackId;
    
    [self.confirmViewController setNewUploadStatus:@"Finished uploading!"];
    
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:self.loaderCallbackId];
}

//Gets called when a picture is taken, so we can use the callback again
-(void)refreshCallBackId:(CDVInvokedUrlCommand *)command{

    self.callbackId = command.callbackId;
}

-(void)successWithMessage:(NSDictionary *)messageDict;
{

    CDVPluginResult* pluginResult = nil;
 
    if (messageDict != nil && [messageDict count] > 0) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:messageDict];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }
  
    [self.commandDelegate sendPluginResult:pluginResult callbackId:self.callbackId];
}

#pragma mark CameraOverlayViewController Delegate Methods
- (void)didPressDismissCamera{
    self.cameraOverlayViewController = nil;
    [self.viewController dismissModalViewControllerAnimated:YES];
}


- (void)didFinishTakingPhotoWithImage:(UIImage *)image buttonID:(NSInteger)buttonID andFileURL:(NSString *)fileURL{

    [self.HUD hide:YES];
    //Present preview view
    [self.viewController dismissModalViewControllerAnimated:NO];

    self.confirmViewController = [[ConfirmViewController alloc]initWithNibName:@"ConfirmViewController" image:image bundle:nil];
    self.confirmViewController.delegate = self;
    [self.viewController presentModalViewController:self.confirmViewController animated:NO];
   
    
    //Upload picture
    NSString *keyName = nil;
    if (buttonID == 0) {
        keyName = @"okay";
    }else{
        keyName = @"help";
    }
    
    NSDictionary *messageDict = [[NSDictionary alloc]initWithObjectsAndKeys:fileURL, @"uri", keyName, @"state", nil];
    [self successWithMessage:messageDict];
}

-(void)showActivityLoader{
    
    AppDelegate *appDelegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    
    self.HUD = [[MBProgressHUD alloc] initWithView:[appDelegate window]];
	[[appDelegate window] addSubview:HUD];
    [self.viewController.view bringSubviewToFront:HUD];
	
	HUD.delegate = self;
	HUD.labelText = @"Taking picture...";
	
	// myProgressTask uses the HUD instance to update progress
	[HUD show:YES];
}

#pragma mark ConfirmOverlay Delegate
-(void)didPressContinue{

    [self.confirmViewController dismissModalViewControllerAnimated:NO];
    self.cameraOverlayViewController.animationEnabled = NO;
    [self.viewController presentModalViewController:self.cameraOverlayViewController animated:NO];
    
}

#pragma mark -
#pragma mark MBProgressHUDDelegate methods

- (void)hudWasHidden:(MBProgressHUD *)hud {
	// Remove HUD from screen when the HUD was hidded
	[HUD removeFromSuperview];

	HUD = nil;
}


@end
