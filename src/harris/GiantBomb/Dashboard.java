package harris.GiantBomb;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Dashboard extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dashboard);		
		GridView iconGrid = (GridView) this.findViewById(R.id.iconGrid);
		
		DashboardIcon news = new DashboardIcon(R.drawable.news, "News", NewsList.class);
		DashboardIcon reviews = new DashboardIcon(R.drawable.reviews, "Reviews", ReviewList.class);
		DashboardIcon videos = new DashboardIcon(R.drawable.videos, "Videos", VideoList.class);
		DashboardIcon bombcast = new DashboardIcon(R.drawable.bombcast, "Bombcast", BombcastList.class);
		List<DashboardIcon> icons = new ArrayList<DashboardIcon>();
		icons.add(news);
		icons.add(reviews);
		icons.add(videos);
		icons.add(bombcast);
		
		LinearLayout searchbar = (LinearLayout) this.findViewById(R.id.searchBar);
		searchbar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				onSearchRequested();
			}
			
		});
		
		iconGrid.setAdapter(new IconAdapter(icons));		
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		final Context context = this;
		
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
		return true;
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
