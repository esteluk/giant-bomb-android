package harris.GiantBomb;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.TabHost;

public class Tabs extends TabActivity {
	public static final int MENU_SEARCH = Menu.FIRST;	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Intent news = new Intent(this, NewsList.class);
		Intent reviews = new Intent(this, ReviewList.class);
		Intent videos = new Intent(this, VideoList.class);
		Intent bombcast = new Intent(this, BombcastList.class);

		TabHost tabs = getTabHost();

		tabs.addTab(tabs.newTabSpec("news").setIndicator("News",
				getResources().getDrawable(R.drawable.news)).setContent(news));
		tabs.addTab(tabs.newTabSpec("reviews").setIndicator("Reviews",
				getResources().getDrawable(R.drawable.reviews)).setContent(
				reviews));
		tabs.addTab(tabs.newTabSpec("videos").setIndicator("Videos",
				getResources().getDrawable(R.drawable.videos)).setContent(
				videos));
		tabs.addTab(tabs.newTabSpec("bombcast").setIndicator("Bombcast",
				getResources().getDrawable(R.drawable.bombcast)).setContent(
				bombcast));
		tabs.setCurrentTab(0);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem share = menu.add(0, MENU_SEARCH, MENU_SEARCH, "Search GiantBomb");

		share.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				onSearchRequested();

				return true;
			}
		});

		return true;
	}

}