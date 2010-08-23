package harris.GiantBomb;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.TextView.OnEditorActionListener;

/**
 * Lists videos, launches video player when one is clicked
 * 
 */
public class VideoList extends ListActivity implements api {

	private ArrayList<Video> videos;
	ProgressDialog pd;
	private int offset = 0;
	private int lastItemIndex = 0;
	
	private String searchString;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.videolist);
		videos = new ArrayList<Video>();
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			searchString = bundle.getString("searchString");
		}
	
		loadFeed();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (videos.get(position).getId() == -1) {
			videos.remove(position);
			loadFeed();
		} else {
			Intent myIntent = new Intent(this, VidPlayer.class);
			Bundle bundle = new Bundle();
			bundle.putString("URL", videos.get(position).getLink());
			bundle.putString("title", videos.get(position).getTitle());
			bundle.putString("siteDetailURL", videos.get(position)
					.getSiteDetailURL());
			myIntent.putExtras(bundle);
			VideoList.this.startActivity(myIntent);
		}
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, 1, 0, "Share");
		menu.add(0, 3, 0, "Share (Shortend)");
		menu.add(0, 2, 0, "Download");
	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		if (item.getItemId() == 1 || item.getItemId() == 3) {
			System.out.println(info.id);
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			if (item.getItemId() == 1) {
			shareIntent.putExtra(Intent.EXTRA_TEXT, videos.get((int) info.id)
					.getSiteDetailURL());
			} else {
				shareIntent.putExtra(Intent.EXTRA_TEXT, Bitly.getShortUrl(videos.get((int) info.id)
						.getSiteDetailURL()));
			}
			startActivity(Intent.createChooser(shareIntent,
					"Share link with..."));
		} else if (item.getItemId() == 2) {
			Intent myIntent = new Intent(this, DownloadView.class);
			Bundle bundle = new Bundle();
			bundle.putString("URL", videos.get((int) info.id).getLink());
			bundle.putString("title", videos.get((int) info.id).getTitle());
			myIntent.putExtras(bundle);
			VideoList.this.startActivity(myIntent);
		}
		return super.onContextItemSelected(item);
	}

	@SuppressWarnings("unchecked")
	private void loadFeed() {
		final ListActivity list = this;
		//Toast.makeText(VideoList.this, "Loading..." , Toast.LENGTH_SHORT).show();
		pd = ProgressDialog.show(this, "Loading...", "Please wait");
		pd.setCancelable(true);
		pd.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface arg0) {
				list.finish();
			}
			
		});
		
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				list.setListAdapter(((ArrayAdapter) message.obj));
				registerForContextMenu(getListView());
				list.setSelection(lastItemIndex);
				pd.dismiss();
			}
		};

		Thread thread = new Thread() {
			@Override
			public void run() {

				try {
					lastItemIndex = videos.size() - 1;
					VideoFeedParser parser = new VideoFeedParser(
							"http://api.giantbomb.com/videos/?api_key="
									+ API_KEY
									+ "&sort=-publish_date&limit=25&field_list=name,deck,id,url,image,site_detail_url&format=xml&offset="
									+ offset, searchString, offset);
					offset = offset + 25;
					ArrayList<Video> add = new ArrayList<Video>(25);
					add = (ArrayList<Video>) parser.parse();
					for (Video v : add) {
						videos.add(v);
					}
					Video loadMore = new Video();
					

					loadMore.setTitle("Load 25 More...");

					loadMore.setId(-1);
					videos.add(loadMore);
					Message message;
					message = handler.obtainMessage(-1, new VideoListAdapter(
							list, R.layout.videorow, videos));
					handler.sendMessage(message);
				} catch (Throwable t) {
				}
			}
		};
		thread.start();
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		final Context context = this;
		menu.clear();
		
		SubMenu searchVideos = menu.addSubMenu("Search Videos").setIcon(android.R.drawable.ic_menu_search);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String searchStrings = prefs.getString("searchStrings", "Trailer, Review, Quick Look, Endurance Run");
		String[] tokens = searchStrings.split(",");
		for (final String search : tokens) {
			if (search == null || "".equals(search.trim())) {
				continue;
			}
			MenuItem mi = searchVideos.add(search.trim());
			mi.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem arg0) {
					Intent myIntent = new Intent(context, VideoList.class);
					Bundle bundle = new Bundle();
					bundle.putString("searchString", search.trim());
					myIntent.putExtras(bundle);
					context.startActivity(myIntent);
					return true;
				}
			});
		}
		
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
		
		return true;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (pd.isShowing()) {
			pd.dismiss();
		}
	}
}