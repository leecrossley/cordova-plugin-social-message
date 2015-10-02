//
//  SocialMessage.java
//  Copyright (c) 2013 Lee Crossley - http://ilee.co.uk
//

package uk.co.ilee.socialmessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Collections;
import java.util.Arrays;
import java.util.Comparator;


import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;


import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.Intent;
import android.net.Uri;

import android.os.Environment;
import android.os.Parcelable;

import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;


@SuppressLint("DefaultLocale")
public class SocialMessage extends CordovaPlugin {

	private static final String TAG = "socialmessage";

	ArrayList<String> whitelist = new ArrayList<String>();
	ArrayList<String> blacklist = new ArrayList<String>();

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		Log.v(TAG, "start execute");

		try {
			doSendIntent(args);
		} catch (IOException e) {
			Log.v(TAG, e.getMessage() ) ;
		}
		return true;
	}

	private String getJSONProperty(JSONObject json, String property) throws JSONException {
		if (json.has(property)) {
			return json.getString(property);
		}
		return null;
	}

	private void initList(ArrayList<String> list, String stringList) throws JSONException {
		if (stringList == null) return;

		String[] jsonlist = stringList.split(",");

		for (String activity : jsonlist ) {

				if ( activity.equals("PostToFacebook") ) {
					list.add("com.facebook.katana");
				}
				else if ( activity.equals("PostToTwitter") ) {
					list.add("com.twitter.android");
				}
		}
	}

	private boolean isPackageAllowed(String packageName) {
		Log.v(TAG, "isPackageAllowed " + packageName);
		if ( !blacklist.isEmpty() && blacklist.contains(packageName) ) {
			Log.v(TAG, "black listed " + packageName);
			return false;
		}

		if ( !whitelist.isEmpty() && !whitelist.contains(packageName) ) {
			Log.v(TAG, "not white listed " + packageName);
			return false;
		}

		Log.v(TAG, "allowed " + packageName);
		return true;
	}

	// Method:
 public Intent customChooserIntent(Intent target, String shareTitle) {

	 	Log.v(TAG, "customChooserIntent");


		if (whitelist.isEmpty() && blacklist.isEmpty() ) {
			Log.v(TAG, "default chooser");
			return Intent.createChooser(target, shareTitle);
		}

		// blacklist
		List<Intent> targetedShareIntents = new ArrayList<Intent>();
		List<HashMap<String, String>> intentMetaInfo = new ArrayList<HashMap<String, String>>();
		Intent chooserIntent;

		Intent dummy = new Intent(target.getAction());
		dummy.setType(target.getType());
		PackageManager packageManager = cordova.getActivity().getPackageManager();
		List<ResolveInfo> resInfo = packageManager.queryIntentActivities(dummy, 0);

		if (!resInfo.isEmpty()) {
		    for (ResolveInfo resolveInfo : resInfo) {
					Log.v(TAG, "resolveInfo.activityInfo.packageName: "+resolveInfo.activityInfo.packageName);
					Log.v(TAG, "packageName: " + resolveInfo.activityInfo.packageName);
					Log.v(TAG, "className: " + resolveInfo.activityInfo.name);
					Log.v(TAG, "simpleName: " + String.valueOf(resolveInfo.activityInfo.loadLabel(packageManager)));

					if (resolveInfo.activityInfo != null && isPackageAllowed(resolveInfo.activityInfo.packageName)) {
						HashMap<String, String> info = new HashMap<String, String>();
		        info.put("packageName", resolveInfo.activityInfo.packageName);
		        info.put("className", resolveInfo.activityInfo.name);
		        info.put("simpleName", String.valueOf(resolveInfo.activityInfo.loadLabel(packageManager)));
		        intentMetaInfo.add(info);
					}

		    }

		    if (!intentMetaInfo.isEmpty()) {
		        // sorting for nice readability
		        Collections.sort(intentMetaInfo, new Comparator<HashMap<String, String>>() {
		            @Override
		            public int compare(HashMap<String, String> map, HashMap<String, String> map2) {
		                return map.get("simpleName").compareTo(map2.get("simpleName"));
		            }
		        });

		        // create the custom intent list
		        for (HashMap<String, String> metaInfo : intentMetaInfo) {
		            Intent targetedShareIntent = (Intent) target.clone();
		            targetedShareIntent.setPackage(metaInfo.get("packageName"));
		            targetedShareIntent.setClassName(metaInfo.get("packageName"), metaInfo.get("className"));
		            targetedShareIntents.add(targetedShareIntent);
		        }

		        chooserIntent = Intent.createChooser(targetedShareIntents.remove(targetedShareIntents.size() - 1), shareTitle);
		        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
		        return chooserIntent;
		    }
		}

		return Intent.createChooser(target, shareTitle);
	}

	private void doSendIntent(JSONArray args) throws IOException, JSONException {
		JSONObject json = args.getJSONObject(0);
		String text = getJSONProperty(json, "text");
		String subject = getJSONProperty(json, "subject");
		String url = getJSONProperty(json, "url");
		String image = getJSONProperty(json, "image");
		String activityTypes = getJSONProperty(json, "activityTypes");
		String blackActivityTypes = getJSONProperty(json, "blackActivityTypes");

		Log.v(TAG, "text " + text);
		Log.v(TAG, "subject " + subject);
		Log.v(TAG, "url " + url);
		Log.v(TAG, "image " + image);
		Log.v(TAG, "activityTypes " + activityTypes);
		Log.v(TAG, "blackActivityTypes " + blackActivityTypes);

		initList(whitelist, activityTypes);
		Log.v(TAG, "whitelist:  " + whitelist.toString());

		initList(blacklist, blackActivityTypes);
		Log.v(TAG, "blacklist:  " + blacklist.toString());

		if (url != null && url.length() > 0) {
			text = ((text==null) ? "" : text + " ") + url;
		}
		Log.v(TAG, "text " + text);

		final Intent sendIntent = new Intent(Intent.ACTION_SEND);
		if (text != null && text.length() > 0) {
			sendIntent.putExtra(Intent.EXTRA_TEXT, text);
		}

		if (subject != null && subject.length() > 0) {
			sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		}

		if (image != null && image.length() > 0) {
			Log.v(TAG, "Send image");

			sendIntent.setType("image/*");
			final URL url_image = new URL(image);
			String storageDir = Environment.getExternalStorageDirectory().getPath();
			final String path = storageDir + "/" + image.substring(image.lastIndexOf("/") + 1, image.length());
			cordova.getThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					try {
						saveImage(url_image, path);
						sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
						cordova.getActivity().startActivityForResult(customChooserIntent(sendIntent, "Partager"), 0);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} else {
			Log.v(TAG, "Send text");
			sendIntent.setType("text/plain");
			cordova.startActivityForResult(this, customChooserIntent(sendIntent, "Partager"), 0);
		}
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
