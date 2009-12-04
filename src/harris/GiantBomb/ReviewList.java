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

/**
 * Lists reviews, launches video player when one is clicked
 * 
 */
public class ReviewList extends ListActivity implements api {

	private ArrayList<Review> reviews;
	private int offset = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reviewlist);
		reviews = new ArrayList<Review>();
		loadFeed();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (reviews.get(position).getScore() == -1) {
			reviews.remove(position);
			loadFeed();
		} else {
			Intent myIntent = new Intent(this, WebPlayer.class);
			Bundle bundle = new Bundle();
			bundle.putString("URL", reviews.get(position).getLink());
			bundle.putString("data", reviews.get(position).getContent());
			myIntent.putExtras(bundle);
			ReviewList.this.startActivity(myIntent);
		}
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
			shareIntent.putExtra(Intent.EXTRA_TEXT, reviews.get((int) info.id)
					.getLink());
			startActivity(Intent.createChooser(shareIntent,
					"Share link with..."));
		}
		return super.onContextItemSelected(item);
	}

	@SuppressWarnings("unchecked")
	private void loadFeed() {

		final ListActivity list = this;
		final ProgressDialog dialog = ProgressDialog.show(ReviewList.this, "",
				"Loading. Please wait...", true);
		dialog.show();

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				dialog.dismiss();
				list.setListAdapter(((ArrayAdapter) message.obj));
				registerForContextMenu(getListView());
				list.setSelection(offset - 25);
			}
		};

		Thread thread = new Thread() {
			@Override
			public void run() {

				try {
					ReviewFeedParser parser = new ReviewFeedParser(
							"http://api.giantbomb.com/reviews/?api_key="
									+ API_KEY
									+ "&sort=-publish_date&limit=25&field_list=game,site_detail_url,score,description&format=xml&offset="
									+ offset);
					offset = offset + 25;
					ArrayList<Review> add = new ArrayList<Review>(25);
					add = (ArrayList<Review>) parser.parse();
					for (Review i : add) {
						reviews.add(i);
					}
					Review loadMore = new Review();
					loadMore.setTitle("Load 25 More...");
					loadMore.setScore(-1);
					reviews.add(loadMore);
					Message message;
					message = handler.obtainMessage(-1, new ReviewListAdapter(
							list, R.layout.videorow, reviews));
					handler.sendMessage(message);
				} catch (Throwable t) {
				}
			}
		};
		thread.start();
	}
}