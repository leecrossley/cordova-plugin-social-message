//
//  SocialMessage.m
//  Copyright (c) 2013 Lee Crossley (http://ilee.co.uk). All rights reserved.
//

#import "SocialMessage.h"

@implementation SocialMessage

- (void) send:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options
{
    NSString *message = [options objectForKey:@"message"];
    NSArray *activityTypes = [[options objectForKey:@"activityTypes"] componentsSeparatedByString:@","];

    UIActivityViewController *activity = [[UIActivityViewController alloc] initWithActivityItems:@[message] applicationActivities:Nil];

    NSMutableArray *exclusions = [[NSMutableArray alloc] init];

    if (![activityTypes containsObject:@"PostToFacebook"])
    {
        [exclusions addObject: UIActivityTypePostToFacebook];
    }
    if (![activityTypes containsObject:@"PostToTwitter"])
    {
        [exclusions addObject: UIActivityTypePostToTwitter];
    }
    if (![activityTypes containsObject:@"PostToWeibo"])
    {
        [exclusions addObject: UIActivityTypePostToWeibo];
    }
    if (![activityTypes containsObject:@"Message"])
    {
        [exclusions addObject: UIActivityTypeMessage];
    }
    if (![activityTypes containsObject:@"Mail"])
    {
        [exclusions addObject: UIActivityTypeMail];
    }
    if (![activityTypes containsObject:@"Print"])
    {
        [exclusions addObject: UIActivityTypePrint];
    }
    if (![activityTypes containsObject:@"CopyToPasteboard"])
    {
        [exclusions addObject: UIActivityTypeCopyToPasteboard];
    }
    if (![activityTypes containsObject:@"AssignToContact"])
    {
        [exclusions addObject: UIActivityTypeAssignToContact];
    }
    if (![activityTypes containsObject:@"SaveToCameraRoll"])
    {
        [exclusions addObject: UIActivityTypeSaveToCameraRoll];
    }

    activity.excludedActivityTypes = exclusions;

    [self.viewController presentViewController:activity animated:YES completion:Nil];
}

- (void) mailComposeController:(MFMailComposeViewController*)controller didFinishWithResult:(MFMailComposeResult)result error:(NSError*)error
{
    [self.viewController dismissModalViewControllerAnimated:TRUE];
}

- (IBAction) showNoMailMessage
{
    UIAlertView *message = [[UIAlertView alloc] initWithTitle:@"Email Configuration" message:@"You must have an email account setup on your device to use the share feature." delegate:nil cancelButtonTitle:@"Close" otherButtonTitles:nil];
    [message show];
}

@end