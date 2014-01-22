//
//  ViewController.m
//  PeacemakerPlugin
//
//  Created by Tijmen van Linden on 3/28/13.
//  Copyright (c) 2013 Tijmen van Linden. All rights reserved.
//

#import "CameraOverlayViewController.h"
#import <AssetsLibrary/AssetsLibrary.h>
#import "UIImage+Resize.h"

@interface CameraOverlayViewController ()

@end

@implementation CameraOverlayViewController
@synthesize imagePicker;
@synthesize delegate;
@synthesize cameraOverlayView;
@synthesize buttonID;
@synthesize helpButton, okayButton, blueBackground;

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    
    //iPhone 5 check
    CGRect screenRect = [[UIScreen mainScreen] bounds];
    CGFloat screenHeight = screenRect.size.height;
    
    if( screenHeight == 568.0){
        //iPhone5 img
        self.helpButton.frame       = CGRectMake(self.helpButton.frame.origin.x, 495, self.helpButton.frame.size.width, self.helpButton.frame.size.height);
        self.okayButton.frame       = CGRectMake(self.okayButton.frame.origin.x, 495, self.helpButton.frame.size.width, self.helpButton.frame.size.height);
        self.blueBackground.frame   = CGRectMake(self.blueBackground.frame.origin.x, 470, self.blueBackground.frame.size.width, self.blueBackground.frame.size.height);
    }
}

- (void)viewDidAppear:(BOOL)animated{

    [super viewDidAppear:animated];

    [self setupcamera];

}

-(void)setupcamera{
    
    if([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera]){
        
        self.imagePicker = [[UIImagePickerController alloc] init];
        imagePicker.delegate = self;
        imagePicker.sourceType = UIImagePickerControllerSourceTypeCamera;
        imagePicker.showsCameraControls = NO;
        imagePicker.wantsFullScreenLayout = YES;
        imagePicker.modalPresentationStyle = UIModalPresentationFullScreen;
        
        imagePicker.cameraOverlayView = cameraOverlayView;
        
        [self.cameraOverlayView setHidden:NO];
        
        [self presentModalViewController:self.imagePicker animated:YES];

    }
}

- (IBAction)helpButtonPressed:(id)sender {
    if ([self.delegate respondsToSelector:@selector(showActivityLoader)]) {
        [self.delegate showActivityLoader];
    }
    
    [imagePicker takePicture];
    self.buttonID = 1;
  
}

- (IBAction)okayButtonPressed:(id)sender {
    if ([self.delegate respondsToSelector:@selector(showActivityLoader)]) {
        [self.delegate showActivityLoader];
    }
    
    [imagePicker takePicture];
    
    self.buttonID = 0;
 
}

- (IBAction)closeButtonPressed:(id)sender {
    
      if ([self.delegate respondsToSelector:@selector(didPressDismissCamera)]) {

        [self.delegate didPressDismissCamera];
    }
}

- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info{
    
    //Obtaining saving path
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *imagePath = [documentsDirectory stringByAppendingPathComponent:@"picture.jpg"];
    
    UIImage *image = [info objectForKey:UIImagePickerControllerOriginalImage];
    
    if (image) {
        
//        NSLog(@"Original image (%@): %@",image,NSStringFromCGSize(image.size));
        CGFloat imageWidth = image.size.width * 0.10;
        CGFloat imageHeight = image.size.height * 0.10;
        CGRect newImageSize = CGRectMake(0, 0, imageWidth, imageHeight);
        
        UIImage* scaledImage = [image resizedImageToFitInSize:newImageSize.size scaleIfSmaller:NO];
//        NSLog(@"Scaled image (%@): %@",scaledImage,NSStringFromCGSize(scaledImage.size));
        
        [UIImageJPEGRepresentation(scaledImage, 0.4) writeToFile:imagePath atomically:YES];
        
        NSString *imageURL = [NSString stringWithFormat:@"%@",[NSURL fileURLWithPath:imagePath] ];
//        NSLog(@"PATH: %@", imageURL);
        if ([self.delegate respondsToSelector:@selector(didSavePictureWithFileURL:andButtonID:)]) {
            
                [self.delegate didSavePictureWithFileURL:imageURL andButtonID:self.buttonID];
            }
    }
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewDidUnload {
    
    [super viewDidUnload];
}




@end
