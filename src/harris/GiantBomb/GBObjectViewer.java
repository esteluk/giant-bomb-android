package harris.GiantBomb;

import harris.GiantBomb.GBObject.ObjectType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.MenuItem.OnMenuItemClickListener;
import android.webkit.WebView;
import android.widget.Toast;

public class GBObjectViewer extends Activity {
	private GBObject item;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gbobjectviewer);
		
		String id = null;
		ObjectType type = null;
		
		// see if this was launched via an intent filter
		String url = getIntent().getDataString();
		if (url != null) {
			Pattern pattern = Pattern.compile("/(\\d+)-(\\d+)/");
			Matcher matcher = pattern.matcher(url);
			if (matcher.find()) {
				id = matcher.group(2);
				switch (Integer.parseInt(matcher.group(1))) {
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
					default: Toast.makeText(this, "Content type not supported", Toast.LENGTH_LONG).show();
					showErrorMessage(url);
					return;
				}
				
			} else {
				showErrorMessage(url);
				return;
			}
		} else {
			Bundle bundle = getIntent().getExtras();
			id = bundle.getString("id");
			type = ObjectType.valueOf(bundle.getString("type"));
		}

		try {
			item = GBObject.getGBObject(id, type);
		} catch (Exception e) {
		}

		String predata = "<html><head><style type='text/css'>"
				+ this.getString(R.string.css) + "</style></head><body>";
		String postdata = "</body></html>";
		String content = predata + "<h1>" + item.getName() + "</h1>" + "<i>"
				+ item.getDeck() + "</i><br>"
				+ StringEscapeUtils.unescapeHtml(item.getDescription())
				+ postdata;

		WebView web = (WebView) this.findViewById(R.id.content);
		web.getSettings().setJavaScriptEnabled(true);
		web.loadDataWithBaseURL(item.getUrl(), content, null, "utf-8", null);
	}
	
	public void showErrorMessage(final String url) {
		final Activity activity = this;
		AlertDialog alert = new AlertDialog.Builder(activity).create();
		alert.setTitle(activity.getString(R.string.linkErrorTitle));
		alert.setMessage(activity.getString(R.string.linkErrorText));
		alert.setButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				PackageManager pm = getPackageManager();
				pm.clearPackagePreferredActivities("harris.GiantBomb");
				Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
				startActivity(browserIntent);
			}
		});
		alert.setButton2("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				activity.finish();
			}
		});
		alert.show();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem share = menu.add("Share");

		share.setIcon(android.R.drawable.ic_menu_share);

		share.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {

				Intent shareIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(Intent.EXTRA_TEXT, item.getUrl());
				startActivity(Intent.createChooser(shareIntent,
						"Share link with..."));

				return true;
			}
		});

		SubMenu related = menu.addSubMenu("Related");
		for (ObjectType ot : ObjectType.values()) {
			for (GBObject obj : item.getRelated(ot)) {
				final GBObject o = obj;
				final Activity activity = this;
				MenuItem mi = related
						.add(ot.toString() + " - " + obj.getName());
				mi.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						Intent myIntent = new Intent(activity,
								GBObjectViewer.class);
						Bundle bundle = new Bundle();
						bundle.putString("id", o.getId());
						bundle.putString("type", o.getType().toString());
						myIntent.putExtras(bundle);
						activity.startActivity(myIntent);
						return true;
					}

				});
			}
		}

		return true;
	}
}
