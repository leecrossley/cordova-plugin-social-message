//
//  SocialMessage.h
//  Copyright (c) 2013 Lee Crossley (http://ilee.co.uk). All rights reserved.
//

#import "Foundation/Foundation.h"
#import "MessageUI/MFMailComposeViewController.h"
#import "Cordova/CDVPlugin.h"

@interface SocialMessage : CDVPlugin <MFMailComposeViewControllerDelegate> {

}

- (void) send:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;

@end