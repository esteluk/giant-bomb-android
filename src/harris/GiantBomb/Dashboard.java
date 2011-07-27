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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
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
		DashboardIcon news = new DashboardIcon(R.drawable.iconnews, "News", NewsList.class);
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
		
		//iconGrid.setAdapter(new IconAdapter(icons));		
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		final Context context = this;
		
		menu.clear();
		
		MenuItem settings = menu.add("Settings");
		settings.setIcon(android.R.drawable.ic_menu_preferences);
		settings.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				Intent preferences = new Intent(context, Preferences.class);
				context.startActivity(preferences);
				return true;
			}
			
		});
		
		MenuItem about = menu.add("About").setIcon(android.R.drawable.ic_menu_info_details);

		about.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				LinearLayout aboutView = (LinearLayout) LinearLayout.inflate(context, R.layout.about, null);
				
				final String[] devs = {"Harris Munir", "Programming", "http://www.twitter.com/Hanoran", "Drew Schrauf", "Programming", "http://www.twitter.com/drewschrauf", "poserdonut", "Programming", "http://www.twitter.com/poserdonut", "Jojo Mendoza", "Dashboard Icons", "http://twitter.com/deleket"};
				for (int i = 0; i < devs.length; i = i+3) {
					final int index = i;
					LinearLayout aboutRow = (LinearLayout) LinearLayout.inflate(context, R.layout.aboutrow, null);
					ImageView twitterIcon = (ImageView) aboutRow.findViewById(R.id.twitterIcon);
					twitterIcon.setOnClickListener(new OnClickListener() {
	
						@Override
						public void onClick(View arg0) {
							Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(devs[index + 2]));
							context.startActivity(viewIntent);
						}
						
					});
					TextView name = (TextView) aboutRow.findViewById(R.id.name);
					name.setText(devs[i]);
					TextView role = (TextView) aboutRow.findViewById(R.id.role);
					role.setText(devs[i+1]);
					aboutView.addView(aboutRow);
				}
				AlertDialog alert = new AlertDialog.Builder(context).setView(aboutView).create();
				alert.setTitle("About");
				//alert.setMessage(context.getString(R.string.about));
				alert.setButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				});
				alert.show();

				return true;
			}

		});
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
	
	private class IconAdapter extends BaseAdapter {
		List<DashboardIcon> icons;
		
		public IconAdapter(List<DashboardIcon> icons) {
			this.icons = icons;
		}

		@Override
		public int getCount() {
			return icons.size();
		}

		@Override
		public Object getItem(int arg0) {
			return icons.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			final Context activity = arg2.getContext();
			final int i = arg0;
			
			View ret = View.inflate(activity, R.layout.dashboardicon, null);
			ImageView pic = (ImageView) ret.findViewById(R.id.iconpic);
			pic.setImageResource(icons.get(arg0).getResId());
			TextView text = (TextView) ret.findViewById(R.id.icontext);
			text.setText(icons.get(arg0).getTitle());
			
			ret.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent myIntent = new Intent(activity, icons.get(i).getClazz());
					activity.startActivity(myIntent);
				}
				
			});
			return ret;
		}
		
	}

}
