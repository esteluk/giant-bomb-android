package harris.GiantBomb;

import harris.GiantBomb.GBObject.ObjectType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WhiskeyWebView extends WebView {

	public WhiskeyWebView(Context context) {
		super(context);
		setup(context);
	}
	
	public WhiskeyWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setup(context);
	}
	
	public WhiskeyWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setup(context);
	}
	
	private void setup(final Context context) {
		this.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				String id = null;
				GBObject.ObjectType type = null;
				
				Pattern pattern = Pattern.compile("/(\\d+)-(\\d+)/");
				Matcher matcher = pattern.matcher(url);
				if (matcher.find()) {
					id = matcher.group(2);
					switch (Integer.parseInt(matcher.group(1))) {
						case 60: type = ObjectType.PLATFORM;
						break;
						case 61: type = ObjectType.GAME;
						break;
						case 62: type = ObjectType.FRANCHISE;
						break;
						case 94: type = ObjectType.CHARACTER;
						break;
						case 92: type = ObjectType.CONCEPT;
						break;
						case 93: type = ObjectType.OBJECT;
						break;
						case 95: type = ObjectType.LOCATION;
						break;
						case 72: type = ObjectType.PERSON;
						break;
						case 65: type = ObjectType.COMPANY;
						break;
						default: type = null;
					}
				}
				if (type != null) {
					Intent myIntent = new Intent(context, GBObjectViewer.class);
					Bundle bundle = new Bundle();
					bundle.putString("id", id);
					bundle.putString("type", type.toString());
					myIntent.putExtras(bundle);
					context.startActivity(myIntent);
				} else {
					Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
					context.startActivity(browserIntent);
				}
				return true;
			}
		});
	}

}
