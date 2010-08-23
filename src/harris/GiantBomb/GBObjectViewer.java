package harris.GiantBomb;

import harris.GiantBomb.GBObject.ObjectType;

import org.apache.commons.lang.StringEscapeUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.MenuItem.OnMenuItemClickListener;
import android.webkit.WebView;

public class GBObjectViewer extends Activity {
	private GBObject item;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gbobjectviewer);
		
		String id = null;
		ObjectType type = null;
		
		
		Bundle bundle = getIntent().getExtras();
		id = bundle.getString("id");
		type = ObjectType.valueOf(bundle.getString("type"));

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
