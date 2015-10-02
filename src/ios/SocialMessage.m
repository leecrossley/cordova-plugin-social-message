//
//  SocialMessage.m
//  Copyright (c) 2013 Lee Crossley - http://ilee.co.uk
//

#import "SocialMessage.h"

@implementation SocialMessage {
    NSArray *activityArray ;
    NSArray *blackActivityArray ;
}

- (void) send:(CDVInvokedUrlCommand*)command;
{
    NSMutableDictionary *args = [command.arguments objectAtIndex:0];
    NSString *text = [args objectForKey:@"text"];
    NSString *url = [args objectForKey:@"url"];
    NSString *image = [args objectForKey:@"image"];
    NSString *subject = [args objectForKey:@"subject"];
    NSString *activityTypes = [args objectForKey:@"activityTypes"];
    NSString *blackActivityTypes = [args objectForKey:@"blackActivityTypes"];

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

    if (activityTypes)
        activityArray = [activityTypes componentsSeparatedByString:@","];

    if (blackActivityTypes)
        blackActivityArray = [blackActivityTypes componentsSeparatedByString:@","];


    UIActivityViewController *activity = [[UIActivityViewController alloc] initWithActivityItems:items applicationActivities:Nil];
    [activity setValue:subject forKey:@"subject"];

    NSMutableArray *exclusions = [self manageExclusions];
    activity.excludedActivityTypes = exclusions;




    [self.viewController presentViewController:activity animated:YES completion:Nil];
}

-(Boolean)isAllowed:(NSString *) name
{
    if ( [blackActivityArray count] != 0 && [blackActivityArray containsObject: name] )
        return false;

    if ( [activityArray count] != 0 && ![activityArray containsObject: name] )
        return false ;

    return true;
}

-(NSMutableArray *)manageExclusions
 {
    NSMutableArray *exclusions = [[NSMutableArray alloc] init];

    if (![self isAllowed:@"PostToFacebook"])
    {
        [exclusions addObject: UIActivityTypePostToFacebook];
    }
    if (![self isAllowed:@"PostToTwitter"])
    {
        [exclusions addObject: UIActivityTypePostToTwitter];
    }
    if (![self isAllowed:@"PostToWeibo"])
    {
        [exclusions addObject: UIActivityTypePostToWeibo];
    }
    if (![self isAllowed:@"Message"])
    {
        [exclusions addObject: UIActivityTypeMessage];
    }
    if (![self isAllowed:@"Mail"])
    {
        [exclusions addObject: UIActivityTypeMail];
    }
    if (![self isAllowed:@"Print"])
    {
        [exclusions addObject: UIActivityTypePrint];
    }
    if (![self isAllowed:@"CopyToPasteboard"])
    {
        [exclusions addObject: UIActivityTypeCopyToPasteboard];
    }
    if (![self isAllowed:@"AssignToContact"])
    {
        [exclusions addObject: UIActivityTypeAssignToContact];
    }
    if (![self isAllowed:@"SaveToCameraRoll"])
    {
        [exclusions addObject: UIActivityTypeSaveToCameraRoll];
    }

    if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 7.0)
    {
        if (![self isAllowed:@"AddToReadingList"])
        {
            [exclusions addObject: UIActivityTypeAddToReadingList];
        }
        if (![self isAllowed:@"PostToFlickr"])
        {
            [exclusions addObject: UIActivityTypePostToFlickr];
        }
        if (![self isAllowed:@"PostToVimeo"])
        {
            [exclusions addObject: UIActivityTypePostToVimeo];
        }
        if (![self isAllowed:@"TencentWeibo"])
        {
            [exclusions addObject: UIActivityTypePostToTencentWeibo];
        }
        if (![self isAllowed:@"AirDrop"])
        {
            [exclusions addObject: UIActivityTypeAirDrop];
        }
    }

    return exclusions;
}

@end
