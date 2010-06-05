package harris.GiantBomb;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class NewsList extends ListActivity {
	private ArrayList<News> news;
	ProgressDialog pd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newslist);
		loadFeed();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		News newsItem = news.get(position);
		Intent myIntent = new Intent(this, WebPlayer.class);
		Bundle bundle = new Bundle();
		bundle.putString("URL", newsItem.getLink());

		List<String> processedContent = StringUtils.removeEmbeds(newsItem
				.getContent());
		String data = "<h1>" + newsItem.getTitle() + "</h1>By "
				+ newsItem.getAuthor() + " on " + newsItem.getPubdate()
				+ "<br><br>" + processedContent.get(0);
		bundle.putString("data", data);

		// bundle extra videos
		String videos = "";
		int size = processedContent.size();
		for (int i = 1; i < size; i++) {
			videos += processedContent.get(i);
			if (i < (size - 1)) {
				videos += ",";
			}
		}
		bundle.putString("videos", videos);

		myIntent.putExtras(bundle);
		NewsList.this.startActivity(myIntent);
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, 1, 0, "Share");
		menu.add(0, 2, 0, "Share (Shortend)");
	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
			System.out.println(info.id);
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			if (item.getItemId() == 1) {
			shareIntent.putExtra(Intent.EXTRA_TEXT, news.get((int) info.id)
					.getLink());
			} else {
				shareIntent.putExtra(Intent.EXTRA_TEXT, Bitly.getShortUrl(news.get((int) info.id)
					.getLink()));
			}
			startActivity(Intent.createChooser(shareIntent,
					"Share link with..."));
		return super.onContextItemSelected(item);
	}

	@SuppressWarnings("unchecked")
	private void loadFeed() {
		final ListActivity list = this;
		
		//Toast.makeText(NewsList.this, "Loading..." , Toast.LENGTH_SHORT).show();
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
				pd.dismiss();
			}
		};

		Thread thread = new Thread() {
			@Override
			public void run() {

				try {
					NewsFeedParser parser = new NewsFeedParser(
							"http://feeds.feedburner.com/GiantBombNews?format=xml");
					news = (ArrayList<News>) parser.parse();
					Message message;
					message = handler.obtainMessage(-1, new NewsListAdapter(
							list, R.layout.newsrow, news));
					handler.sendMessage(message);
				} catch (Throwable t) {
				}
			}
		};
		thread.start();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (pd.isShowing()) {
			pd.dismiss();
		}
	}

}
