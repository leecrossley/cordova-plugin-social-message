//
//  SocialMessage.java
//  Copyright (c) 2013 Lee Crossley - http://ilee.co.uk
//

package uk.co.ilee.socialmessage;

import org.json.JSONArray;
import org.json.JSONException;
import android.content.Intent;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;

public class SocialMessage extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        JSONObject json = args.getJSONObject(0);
        doSendIntent(json.getString("text"), json.getString("subject")); 
        return true;
    }

    private void doSendIntent(String text, String subject) {
        Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject)
        this.cordova.startActivityForResult(this, sendIntent, 0);
    }

}