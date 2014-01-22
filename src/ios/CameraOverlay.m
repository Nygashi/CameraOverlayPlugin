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



#pragma mark Cordova Calls
- (void)showCamera:(CDVInvokedUrlCommand *)command
{
    
//    NSLog(@"command %@", command);
    self.callbackId = command.callbackId;
    
    CameraOverlayViewController *camview = [[CameraOverlayViewController alloc]initWithNibName:@"CameraOverlayViewController" bundle:nil];
    camview.delegate = self;
    [self.viewController presentModalViewController:camview animated:NO];

}
- (void)hideActivityLoader:(CDVInvokedUrlCommand *)command{
    
    self.loaderCallbackId = command.callbackId;
   
    [self.HUD hide:YES];
}

//Gets called when a picture is taken, so we can use the callback again
-(void)refreshCallBackId:(CDVInvokedUrlCommand *)command{

    self.callbackId = command.callbackId;
}

-(void)successWithMessage:(NSDictionary *)messageDict;
{
//    [self.HUD hide:YES];
    
    CDVPluginResult* pluginResult = nil;
 
    if (messageDict != nil && [messageDict count] > 0) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:messageDict];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }
  
    [self.commandDelegate sendPluginResult:pluginResult callbackId:self.callbackId];
        
}

#pragma mark CameraOverlayViewController Delegate Methods
- (void)didSavePictureWithFileURL:(NSString *)fileURL andButtonID:(NSInteger)buttonID{

    NSString *keyName = nil;
    if (buttonID == 0) {
        keyName = @"okay";
    }else{
        keyName = @"help";
    }
    
    //NSDictionary *messageDict = [[NSDictionary alloc]initWithObjectsAndKeys:fileURL ,keyName, nil];
    NSDictionary *messageDict = [[NSDictionary alloc]initWithObjectsAndKeys:fileURL, @"uri", keyName, @"state", nil];
    
//    NSLog(@"messageDict %@", messageDict);
    
    [self successWithMessage:messageDict];


}
- (void)didPressDismissCamera{
    [self.viewController dismissModalViewControllerAnimated:YES];
}

-(void)showActivityLoader{
    
    AppDelegate *appDelegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    
    self.HUD = [[MBProgressHUD alloc] initWithView:[appDelegate window]];
	[[appDelegate window] addSubview:HUD];
    [self.viewController.view bringSubviewToFront:HUD];
	
	HUD.delegate = self;
	HUD.labelText = @"Sending photo...";
	
	// myProgressTask uses the HUD instance to update progress
	[HUD show:YES];
    
}

#pragma mark -
#pragma mark MBProgressHUDDelegate methods

- (void)hudWasHidden:(MBProgressHUD *)hud {
	// Remove HUD from screen when the HUD was hidded
	[HUD removeFromSuperview];

	HUD = nil;
}


@end
