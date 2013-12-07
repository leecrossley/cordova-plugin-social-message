## Social Message Plugin for Apache Cordova

Cordova Plugin to utilise native share features. Developed for Apache Cordova CLI >= 3.0.0. Share text, images and urls to Facebook, Twitter and more.

## 1 step install

```
cordova plugin add https://github.com/leecrossley/cordova-plugin-social-message.git
```

## Usage

You **do not** need to reference any JavaScript, the Cordova plugin architecture will add a socialmessage object to your root automatically when you build.

Ensure you use the plugin after your deviceready event has been fired.

### Share some text

Pass a message object with a "text" property as an argument to the send function:

```
var message = {
    text: "This is a test message"
};
window.socialmessage.send(message);
```

This example will allow the message to be shared with all built in activity types.

### Specify activity types (iOS only)

To specify activity types to include as options for the user, add an "activityTypes" array to your message:

```
var message = {
    text: "This is a test message",
    activityTypes: ["PostToFacebook", "PostToTwitter"]
};
window.socialmessage.send(message);
```

This example will only allow the user to post the message to Facebook or Twitter.

A complete list of activity types can be found [here](http://developer.apple.com/library/ios/#documentation/UIKit/Reference/UIActivity_Class/Reference/Reference.html#//apple_ref/occ/cl/UIActivity). You should omit the "UIActivityType" prefix when adding to your array.

**NB:** Some activity types will only show as an option when they have accounts configured in the iOS settings on the device (e.g. Facebook and Twitter). This is different from having the apps installed.

### Share a message with a subject

To add a subject to your message, use the "subject" property (used with activity types such as *Mail*):

```
var message = {
    subject: "Test Subject",
    text: "This is a test message",
    activityTypes: ["Mail"]
};
window.socialmessage.send(message);
```

### Share a message with a url

To add a link to your message, use the "url" property:

```
var message = {
    text: "Link test",
    url: "http://ilee.co.uk"
};
window.socialmessage.send(message);
```

### Share a message with an image

To add an image to your message, use the "image" property:

```
var message = {
    text: "Image test",
    image: "http://cordova.apache.org/images/cordova_bot.png"
};
window.socialmessage.send(message);
```

## Platforms

Currently supporting iOS and Android - check back soon for Windows Phone 8 support.

## License

[MIT License](http://ilee.mit-license.org)