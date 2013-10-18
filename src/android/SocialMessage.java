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

import android.annotation.SuppressLint;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

@SuppressLint("DefaultLocale")
public class SocialMessage extends CordovaPlugin {

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		JSONObject json = args.getJSONObject(0);
		String text = getJSONProperty(json, "text");
		String subject = getJSONProperty(json, "subject");
		String url = getJSONProperty(json, "url");
		String image = getJSONProperty(json, "image");
		if (url != null && url.length() > 0) {
			if (text == null) {
				text = url;
			} else {
				text = text + " " + url;
			}
		}
		try {
			doSendIntent(text, subject, image);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	private String getJSONProperty(JSONObject json, String property) throws JSONException {
		if (json.has(property)) {
			return json.getString(property);
		}
		return null;
	}

	private void doSendIntent(String text, String subject, String image) throws IOException {
		final Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
		if (text != null && text.length() > 0) {
			sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
		}
		if (subject != null && subject.length() > 0) {
			sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		}
		if (image != null && image.length() > 0) {
			sendIntent.setType("image/*");
			final URL url = new URL(image);
			String storageDir = Environment.getExternalStorageDirectory().getPath();
			final String path = storageDir + "/" + image.substring(image.lastIndexOf("/") + 1, image.length());
			cordova.getThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					try {
						saveImage(url, path);
						sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
						cordova.getActivity().startActivityForResult(sendIntent, 0);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} else {
			sendIntent.setType("text/plain");
			cordova.startActivityForResult(this, sendIntent, 0);
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