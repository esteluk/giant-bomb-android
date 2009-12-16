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
			Review review = reviews.get(position);
			Intent myIntent = new Intent(this, WebPlayer.class);
			Bundle bundle = new Bundle();

			// add the star image to the content (not working...)
			// String imageLink = "file:///android_asset/star" +
			// review.getScore()
			// + ".png";
			// String data = "<h1>Review: " + review.getTitle() + "</h1>By "
			// + review.getReviewer()
			// + "<br><img height=\"18\" width=\"86\" src=\"" + imageLink
			// + "\"/><br>" + review.getContent();

			String data = "<h1>Review: " + review.getGame().getName() + "</h1>By "
					+ review.getReviewer() + "<br>" + review.getScore()
					+ " / 5<br>" + review.getContent();
			bundle.putString("URL", review.getLink());
			bundle.putString("data", data);
			bundle.putString("gameId", review.getGame().getId());
			myIntent.putExtras(bundle);
			ReviewList.this.startActivity(myIntent);
		}
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, 1, 0, "Share");
		menu.add(0, 2, 1, "View Game");
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
		if (item.getItemId() == 2) {
			Intent myIntent = new Intent(this, GBObjectViewer.class);
			Bundle bundle = new Bundle();
			bundle.putString("id", reviews.get((int) info.id).getGame().getId());
			bundle.putString("type", reviews.get((int)info.id).getGame().getType().toString());
			myIntent.putExtras(bundle);
			ReviewList.this.startActivity(myIntent);
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
									+ "&sort=-publish_date&limit=25&field_list=game,site_detail_url,score,description,reviewer&format=xml&offset="
									+ offset);
					offset = offset + 25;
					ArrayList<Review> add = new ArrayList<Review>(25);
					add = (ArrayList<Review>) parser.parse();
					for (Review i : add) {
						reviews.add(i);
					}
					Review loadMore = new Review();
					loadMore.getGame().setName("Load 25 More...");
					loadMore.setScore(-1);
					loadMore.setReviewer(" ");
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