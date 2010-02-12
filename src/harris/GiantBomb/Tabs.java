package harris.GiantBomb;

import com.nullwire.trace.ExceptionHandler;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class Tabs extends TabActivity {
	public static final int MENU_SEARCH = Menu.FIRST;
	public static final int MENU_ABOUT = Menu.FIRST + 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExceptionHandler.register(this, "http://harrism.com/GB/server.php"); 
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

	public boolean onPrepareOptionsMenu(Menu menu) {
		final Context context = this;
		
		menu.clear();
		
		MenuItem search = menu.add("Search GiantBomb").setIcon(android.R.drawable.ic_menu_search);

		search.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				onSearchRequested();

				return true;
			}
		});
		
		TabHost tabs = getTabHost();
		if (tabs.getCurrentTab() == 2) {
			SubMenu searchVideos = menu.addSubMenu("Search Videos").setIcon(android.R.drawable.ic_media_play);
			
			MenuItem rv = searchVideos.add("Review");
			rv.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem arg0) {
					Intent myIntent = new Intent(context, VideoList.class);
					Bundle bundle = new Bundle();
					bundle.putString("searchString", "review");
					myIntent.putExtras(bundle);
					context.startActivity(myIntent);
					return true;
				}
			});
			
			MenuItem tr = searchVideos.add("Trailer");
			tr.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem arg0) {
					Intent myIntent = new Intent(context, VideoList.class);
					Bundle bundle = new Bundle();
					bundle.putString("searchString", "trailer");
					myIntent.putExtras(bundle);
					context.startActivity(myIntent);
					return true;
				}
			});
			
			MenuItem ql = searchVideos.add("Quick Look");
			ql.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem arg0) {
					Intent myIntent = new Intent(context, VideoList.class);
					Bundle bundle = new Bundle();
					bundle.putString("searchString", "quick look");
					myIntent.putExtras(bundle);
					context.startActivity(myIntent);
					return true;
				}
			});
			
			MenuItem er = searchVideos.add("Endurance Run");
			er.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem arg0) {
					Intent myIntent = new Intent(context, VideoList.class);
					Bundle bundle = new Bundle();
					bundle.putString("searchString", "endurance run");
					myIntent.putExtras(bundle);
					context.startActivity(myIntent);
					return true;
				}
			});
			
			MenuItem cm = searchVideos.add("Custom...");
			cm.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem arg0) {
					final EditText et = new EditText(context);					
					
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setView(et);
					builder.setTitle("Search Videos");
					builder.setMessage("Enter search term");
					
					et.setOnEditorActionListener(new OnEditorActionListener() {
						@Override
						public boolean onEditorAction(TextView arg0, int arg1,
								KeyEvent arg2) {
							if (arg1 == EditorInfo.IME_NULL) {
								Intent myIntent = new Intent(context, VideoList.class);
								Bundle bundle = new Bundle();
								bundle.putString("searchString", et.getText().toString());
								myIntent.putExtras(bundle);
								context.startActivity(myIntent);
								return true;
							}
							return false;
						}
					});
					
					AlertDialog dialog = builder.create();
					dialog.setButton("Search", new OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							Intent myIntent = new Intent(context, VideoList.class);
							Bundle bundle = new Bundle();
							bundle.putString("searchString", et.getText().toString());
							myIntent.putExtras(bundle);
							context.startActivity(myIntent);
						}						
					});
					dialog.setButton2("Cancel", new OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
						}						
					});
					dialog.show();
					
					return true;
				}
			});
		}

		MenuItem about = menu.add("About").setIcon(android.R.drawable.ic_menu_info_details);

		about.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				AlertDialog alert = new AlertDialog.Builder(context).create();
				alert.setTitle("About");
				alert.setMessage(context.getString(R.string.about));
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
		
		MenuItem forum = menu.add("Forum").setIcon(android.R.drawable.ic_menu_send);

		forum.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent myIntent = new Intent(context, ForumList.class);
				context.startActivity(myIntent);
				return true;
			}

		});

		return true;
	}

}