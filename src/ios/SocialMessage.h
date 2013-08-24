//
//  SocialMessage.h
//  Copyright (c) 2013 Lee Crossley - http://ilee.co.uk
//

#import "Cordova/CDV.h"
#import "Foundation/Foundation.h"

@interface SocialMessage : CDVPlugin

- (void) send:(CDVInvokedUrlCommand*)command;

@end