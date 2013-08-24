//
//  SocialMessage.m
//  Copyright (c) 2013 Lee Crossley - http://ilee.co.uk
//

#import "Cordova/CDV.h"
#import "SocialMessage.h"

@implementation SocialMessage

- (void) send:(CDVInvokedUrlCommand*)command;
{
    NSMutableDictionary *args = [command.arguments objectAtIndex:0];
    NSString *message = [args objectForKey:@"message"];
    NSArray *activityTypes = [[args objectForKey:@"activityTypes"] componentsSeparatedByString:@","];

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

@end