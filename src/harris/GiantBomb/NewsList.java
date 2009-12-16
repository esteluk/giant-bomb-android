package harris.GiantBomb;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class NewsList extends ListActivity {
	private ArrayList<News> news;

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
		String data = "<h1>" + newsItem.getTitle() + "</h1>By "
				+ newsItem.getAuthor() + "<br><br>"
				+ StringUtils.removeEmbeds(newsItem.getContent());
		bundle.putString("data", data);
		myIntent.putExtras(bundle);
		NewsList.this.startActivity(myIntent);
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, 1, 0, "Share");
	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		if (item.getItemId() == 1) {
			System.out.println(info.id);
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(Intent.EXTRA_TEXT, news.get((int) info.id)
					.getLink());
			startActivity(Intent.createChooser(shareIntent,
					"Share link with..."));
		}
		return super.onContextItemSelected(item);
	}

	@SuppressWarnings("unchecked")
	private void loadFeed() {
		final ListActivity list = this;
		final ProgressDialog dialog = ProgressDialog.show(NewsList.this, "",
				"Loading. Please wait...", true);
		dialog.show();

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				dialog.dismiss();
				list.setListAdapter(((ArrayAdapter) message.obj));
				registerForContextMenu(getListView());
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

}
