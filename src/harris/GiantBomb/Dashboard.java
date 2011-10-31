package harris.GiantBomb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Dashboard extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dashboard);		
		LinearLayout iconGrid = (LinearLayout) this.findViewById(R.id.iconGrid);
		DashboardIcon news = new DashboardIcon(R.drawable.iconnews, "News", NewsActivity.class);
		DashboardIcon reviews = new DashboardIcon(R.drawable.iconreview, "Reviews", ReviewList.class);
		DashboardIcon videos = new DashboardIcon(R.drawable.iconvideo, "Videos", VideoList.class);
		DashboardIcon bombcast = new DashboardIcon(R.drawable.iconpodcast, "Bombcast", BombcastList.class);
		List<DashboardIcon> icons = new ArrayList<DashboardIcon>();
		icons.add(news);
		icons.add(reviews);
		icons.add(videos);
		icons.add(bombcast);
		
		Iterator<DashboardIcon> it = icons.iterator();
		while (it.hasNext()) {
			LinearLayout row = new LinearLayout(this);
			row.setOrientation(LinearLayout.HORIZONTAL);
			LinearLayout.LayoutParams params  = new LinearLayout.LayoutParams(
				     LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			row.setLayoutParams(params);
			row.setGravity(Gravity.CENTER_HORIZONTAL);
			View icon = getIconView(it.next(), this);
			icon.setPadding(10, 0, 10, 0);
			row.addView(icon);
			if (it.hasNext()) {
				icon = getIconView(it.next(), this);
				icon.setPadding(10, 0, 10, 0);
				row.addView(icon);
			}
			
			iconGrid.addView(row);
		}
		
		Button searchbar = (Button) this.findViewById(R.id.searchBar);
		searchbar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				onSearchRequested();
			}
			
		});
				
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.homemenu, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final Context context = this;
		
		// So we'll do what we want with each of our two menu items here
		switch(item.getItemId()) {
		case R.id.menu_settings:
			startSettingsActivity(context);
			return true;
		case R.id.menu_about:
			showAboutDialog(context);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	boolean startSettingsActivity(Context ctxt) {
		Intent preferences = new Intent(ctxt, Preferences.class);
		ctxt.startActivity(preferences);
		return true;
	}
		
	boolean showAboutDialog(final Context ctxt) {
		// Inflate the dialog's layout
		LinearLayout aboutView = (LinearLayout) LinearLayout.inflate(ctxt, R.layout.about, null);
		
		// Get the list of developers from our resources
		final String[] devs = ctxt.getResources().getStringArray(R.array.about_developers);
		
		// Parse it peculiarly to do stuff with it
		for (int i = 0; i < devs.length; i = i+3) {
			final int index = i;
			LinearLayout aboutRow = (LinearLayout) LinearLayout.inflate(ctxt, R.layout.aboutrow, null);
			ImageView twitterIcon = (ImageView) aboutRow.findViewById(R.id.twitterIcon);
			twitterIcon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(devs[index + 2]));
					ctxt.startActivity(viewIntent);
				}
				
			});
			TextView name = (TextView) aboutRow.findViewById(R.id.name);
			name.setText(devs[i]);
			TextView role = (TextView) aboutRow.findViewById(R.id.role);
			role.setText(devs[i+1]);
			aboutView.addView(aboutRow);
		}

		AlertDialog alert = new AlertDialog.Builder(ctxt).setView(aboutView).create();
		alert.setTitle("About");
		alert.setButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		alert.show();

		return true;
	}
	
	public View getIconView(final DashboardIcon icon, final Context activity) {
		View ret = View.inflate(activity, R.layout.dashboardicon, null);
		ImageView pic = (ImageView) ret.findViewById(R.id.iconpic);
		pic.setImageResource(icon.getResId());
		TextView text = (TextView) ret.findViewById(R.id.icontext);
		text.setText(icon.getTitle());
		
		ret.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent myIntent = new Intent(activity, icon.getClazz());
				activity.startActivity(myIntent);
			}
			
		});
		return ret;
	}

}
