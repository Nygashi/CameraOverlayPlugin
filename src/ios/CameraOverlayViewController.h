//
//  ViewController.h
//  PeacemakerPlugin
//
//  Created by Tijmen van Linden on 3/28/13.
//  Copyright (c) 2013 Tijmen van Linden. All rights reserved.
//

#import <UIKit/UIKit.h>


@protocol CameraOverlayViewControllerDelegate <NSObject>

- (void)didPressDismissCamera;
- (void)showActivityLoader;
- (void)didFinishTakingPhotoWithImage:(UIImage *)image buttonID:(NSInteger)buttonID andFileURL:(NSString *)fileURL;

@end


@interface CameraOverlayViewController : UIViewController <UIImagePickerControllerDelegate, UINavigationControllerDelegate >{

    UIImagePickerController *imagePicker;
    NSInteger buttonID;
    
    UIButton *helpButton;
    UIButton *okayButton;
}


@property (nonatomic, strong) IBOutlet UIView *cameraOverlayView;

@property (nonatomic, strong) UIImagePickerController *imagePicker;
@property (nonatomic, assign) NSInteger buttonID;

@property (nonatomic, strong) IBOutlet UIButton* helpButton;
@property (nonatomic, strong) IBOutlet UIButton* okayButton;
@property (nonatomic, strong) IBOutlet UIView* blueBackground;
@property (weak, nonatomic) IBOutlet UIImageView *previewImageView;
@property (weak, nonatomic) IBOutlet UIView *imagePreviewView;

@property (nonatomic, unsafe_unretained) id <CameraOverlayViewControllerDelegate> delegate;

@property BOOL animationEnabled;

- (IBAction)helpButtonPressed:(id)sender;
- (IBAction)okayButtonPressed:(id)sender;
- (IBAction)closeButtonPressed:(id)sender;
-(void)releaseCamera;
@end
