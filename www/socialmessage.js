
var exec = require("cordova/exec");

var SocialMessage = function () {
    this.name = "SocialMessage";
};

SocialMessage.prototype.send = function (message) {
    if (!message) {
        return;
    }
    if (message.activityTypes !== undefined)
      message.activityTypes = message.activityTypes.join(",");

    if (message.blackActivityTypes !== undefined )
      message.blackActivityTypes = message.blackActivityTypes.join(",");

    exec(null, null, "SocialMessage", "send", [message]);
};

module.exports = new SocialMessage();
