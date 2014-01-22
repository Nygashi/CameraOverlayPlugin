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

@interface CameraOverlay : CDVPlugin <CameraOverlayViewControllerDelegate, MBProgressHUDDelegate>
{
    NSString *callbackId;
    NSString *loaderCallbackId;
    MBProgressHUD *HUD;
 
}

@property (nonatomic, strong) MBProgressHUD *HUD;
@property (nonatomic, strong) NSString *callbackId;
@property (nonatomic, strong) NSString *loaderCallbackId;

- (void)showCamera:(CDVInvokedUrlCommand *)command;
- (void)hideActivityLoader:(CDVInvokedUrlCommand *)command;
- (void)refreshCallBackId:(CDVInvokedUrlCommand *)command;




@end
