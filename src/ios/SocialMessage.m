//
//  SocialMessage.m
//  Copyright (c) 2013 Lee Crossley - http://ilee.co.uk
//

#import "SocialMessage.h"

@implementation SocialMessage

- (void) send:(CDVInvokedUrlCommand*)command;
{
    NSMutableDictionary *args = [command.arguments objectAtIndex:0];
    NSString *text = [args objectForKey:@"text"];
    NSString *url = [args objectForKey:@"url"];
    NSString *image = [args objectForKey:@"image"];
    NSString *subject = [args objectForKey:@"subject"];
    NSArray *activityTypes = [[args objectForKey:@"activityTypes"] componentsSeparatedByString:@","];

    NSMutableArray *items = [NSMutableArray new];
    if (text)
    {
        [items addObject:text];
    }
    if (url)
    {
        NSURL *formattedUrl = [NSURL URLWithString:[NSString stringWithFormat:@"%@", url]];
        [items addObject:formattedUrl];
    }
    if (image)
    {
        UIImage *imageFromUrl = [UIImage imageWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@", image]]]];
        [items addObject:imageFromUrl];
    }

    UIActivityViewController *activity = [[UIActivityViewController alloc] initWithActivityItems:items applicationActivities:Nil];
    [activity setValue:subject forKey:@"subject"];

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

    if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 7.0)
    {
        if (![activityTypes containsObject:@"AddToReadingList"])
        {
            [exclusions addObject: UIActivityTypeAddToReadingList];
        }
        if (![activityTypes containsObject:@"PostToFlickr"])
        {
            [exclusions addObject: UIActivityTypePostToFlickr];
        }
        if (![activityTypes containsObject:@"PostToVimeo"])
        {
            [exclusions addObject: UIActivityTypePostToVimeo];
        }
        if (![activityTypes containsObject:@"TencentWeibo"])
        {
            [exclusions addObject: UIActivityTypePostToTencentWeibo];
        }
        if (![activityTypes containsObject:@"AirDrop"])
        {
            [exclusions addObject: UIActivityTypeAirDrop];
        }
    }

    activity.excludedActivityTypes = exclusions;

    [self.viewController presentViewController:activity animated:YES completion:Nil];
}

@end