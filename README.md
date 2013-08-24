## Social Message Plugin for Apache Cordova

Cordova Plugin to utilise native share features. Developed for Apache Cordova CLI >= 3.0.0.

## 1 step install

```
cordova plugin add https://github.com/leecrossley/cordova-plugin-social-message.git
```

## Usage

You **do not** need to reference any JavaScript, the Cordova plugin architecture will add a socialmessage object to your root automatically when you build.

```
cordova plugin add https://github.com/leecrossley/cordova-plugin-social-message.git
```

After your deviceready event has been fired, you can simply pass your message as an argument:

```
window.socialmessage.send("This is a test message");
```

Which will allow the message to be shared with all built in activity types.

If you want to specify activity types to include, you can add an array argument such as:

```
window.socialmessage.send("This is a test message", ["PostToFacebook", "PostToTwitter"]);
```

This example will only allow the user to post the message to Facebook or Twitter.

A complete list of activity types can be found [here](http://developer.apple.com/library/ios/#documentation/UIKit/Reference/UIActivity_Class/Reference/Reference.html#//apple_ref/occ/cl/UIActivity). You should omit the "UIActivityType" prefix when adding to your array.

## Platforms

Currently iOS only - check back soon for Android and Windows Phone 8 support.