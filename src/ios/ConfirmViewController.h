//
//  ConfirmViewController.h
//  Peacemaker
//
//  Created by Zooma on 3/21/14.
//
//

#import <UIKit/UIKit.h>
@protocol ConfirmViewControllerDelegate <NSObject>

- (void)didPressContinue;

@end

@interface ConfirmViewController : UIViewController

@property (strong, nonatomic) UIImage *previewImage;
@property (strong, nonatomic) NSString *uploadStatus;
@property (weak, nonatomic) IBOutlet UILabel *uploadStatusLabel;
@property (weak, nonatomic) IBOutlet UIImageView *previewImageView;
@property (nonatomic, unsafe_unretained) id <ConfirmViewControllerDelegate> delegate;

- (IBAction)continuePressed:(id)sender;

- (void)setNewUploadStatus:(NSString *)status;
- (id)initWithNibName:(NSString *)nibNameOrNil image:(UIImage *)xPreviewImage bundle:(NSBundle *)nibBundleOrNil;

@end
