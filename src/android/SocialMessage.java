//
//  SocialMessage.java
//  Copyright (c) 2013 Lee Crossley - http://ilee.co.uk
//

package uk.co.ilee.socialmessage;

import java.util.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import android.annotation.SuppressLint;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.Intent;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.text.TextUtils;
import android.os.Environment;
import android.os.Parcelable;
import android.app.AlertDialog;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import org.apache.cordova.LOG;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

@SuppressLint("DefaultLocale")
public class SocialMessage extends CordovaPlugin {

	Context ctx;
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		ctx = this.cordova.getActivity().getApplicationContext();
		JSONObject json = args.getJSONObject(0);
		String text = getJSONProperty(json, "text");
		String subject = getJSONProperty(json, "subject");
		String url = getJSONProperty(json, "url");
		String shortUrl = getJSONProperty(json, "short_url");
		String image = getJSONProperty(json, "image");
		String etiktId = getJSONProperty(json, "etikt_id");
		try {
			String returnString = doSendIntent(text, subject, image, url, shortUrl, etiktId);

			PluginResult result = new PluginResult(PluginResult.Status.OK, returnString);
			result.setKeepCallback(true);
		callbackContext.sendPluginResult(result);
		} catch (IOException e) {
			e.printStackTrace();
			PluginResult result = new PluginResult(PluginResult.Status.OK, "ERROR");
			result.setKeepCallback(true);
		}
		return true;
	}
	
	private String getJSONProperty(JSONObject json, String property) throws JSONException {
		if (json.has(property)) {
			return json.getString(property);
		}
		return null;
	}

	private String doSendIntent(String text, String subject, String image, String url, String shortUrl, String etiktId) throws IOException {
        List<Intent> targetedShareIntents = new ArrayList<Intent>();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        // intent.putExtra(Intent.EXTRA_SUBJECT, "Foo bar"); // NB: has no effect!
        String returnString="";

		Uri picturePath = null;

		if (image != null && image.length() > 0) {
			final URL imageUrl = new URL(image);
			String storageDir = Environment.getExternalStorageDirectory().getPath();
			final String path = storageDir + "/" + image.substring(image.lastIndexOf("/") + 1, image.length());
			cordova.getThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					try {
						saveImage(imageUrl, path);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			picturePath =  Uri.fromFile(new File(path));
		}

        List<ResolveInfo> matches = ctx.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
			if (info.activityInfo.packageName != null && info.activityInfo.packageName.length() > 0)
			{
				final Intent targetedShareIntent = new Intent(Intent.ACTION_SEND);
				targetedShareIntent.setClassName(info.activityInfo.packageName,info.activityInfo.name);
				if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook")) {
					targetedShareIntent.putExtra(Intent.EXTRA_TEXT, url);
					targetedShareIntent.setType("text/plain");
				}
				else if (info.activityInfo.packageName.toLowerCase().startsWith("com.ionicframework.etiktmobile614257")) {
					targetedShareIntent.putExtra(Intent.EXTRA_TEXT, "ETIKT_ID="+etiktId);
					targetedShareIntent.setType("text/plain");
				}
				else
				{
					if (subject != null && subject.length() > 0) {
						targetedShareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
					}
					if (text != null && text.length() > 0) {
						targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, subject+ " | " + text + "\n" + shortUrl);
					}
					if (picturePath != null) {
						targetedShareIntent.setType("image/*");
						targetedShareIntent.putExtra(Intent.EXTRA_STREAM, picturePath);
					}
					else {
						targetedShareIntent.setType("text/plain");
					}
				}
				targetedShareIntents.add(targetedShareIntent);
            }
        }

		Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "Partager une annonce");
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
        cordova.getActivity().startActivityForResult(chooserIntent, 0);

		return returnString;
	}
	
	public static void saveImage(URL url, String outputPath) throws IOException {
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(outputPath);
		byte[] b = new byte[2048];
		int length;
		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}
		is.close();
		os.close();
	}
}