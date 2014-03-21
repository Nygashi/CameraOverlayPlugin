//
//  ConfirmViewController.m
//  Peacemaker
//
//  Created by Zooma on 3/21/14.
//
//

#import "ConfirmViewController.h"

@interface ConfirmViewController ()

@end

@implementation ConfirmViewController
@synthesize delegate;
@synthesize previewImageView;
@synthesize uploadStatus;

- (id)initWithNibName:(NSString *)nibNameOrNil image:(UIImage *)xPreviewImage bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
        self.previewImage = xPreviewImage;
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    self.previewImageView.image = self.previewImage;
    [self setNewUploadStatus:@"Uploading image..."];
}


- (IBAction)continuePressed:(id)sender {
    NSLog(@"Continue pressed!");
    if ([self.delegate respondsToSelector:@selector(didPressContinue)]) {
        [self.delegate didPressContinue];
    }
}

- (void)setNewUploadStatus:(NSString *)status{
    [self.uploadStatusLabel setText:status];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
