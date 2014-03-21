//
//  CameraOverlay.h
//  Peacemaker
//
//  Created by Tijmen van Linden on 3/29/13.
//
//

#import <Foundation/Foundation.h>
#import <Cordova/CDV.h>
#import "CameraOverlayViewController.h"
#import "MBProgressHUD.h"
#import "ConfirmViewController.h"



@interface CameraOverlay : CDVPlugin <CameraOverlayViewControllerDelegate, ConfirmViewControllerDelegate, MBProgressHUDDelegate>
{
    NSString *callbackId;
    NSString *loaderCallbackId;
    MBProgressHUD *HUD;
    ConfirmViewController *confirmViewController;
    CameraOverlayViewController *cameraOverlayViewController;
 
}

@property (nonatomic, strong) MBProgressHUD *HUD;
@property (nonatomic, strong) NSString *callbackId;
@property (nonatomic, strong) NSString *loaderCallbackId;
@property (nonatomic, strong) ConfirmViewController *confirmViewController;
@property (nonatomic, strong) CameraOverlayViewController *cameraOverlayViewController;

- (void)showCamera:(CDVInvokedUrlCommand *)command;
- (void)hideActivityLoader:(CDVInvokedUrlCommand *)command;
- (void)refreshCallBackId:(CDVInvokedUrlCommand *)command;




@end
