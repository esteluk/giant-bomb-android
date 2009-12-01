package harris.GiantBomb;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * Lists reviews, launches video player when one is clicked
 *
 */
public class ReviewList extends ListActivity implements api{
	
	private ArrayList<Review> reviews;
	private int offset = 25;
	
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
			ReviewFeedParser parser = new ReviewFeedParser("http://api.giantbomb.com/reviews/?api_key=" + API_KEY + "&sort=-publish_date&limit=25&format=xml&offset=" + offset);
			ArrayList<Review> add = new ArrayList<Review>(25);
			add = (ArrayList<Review>) parser.parse();
			for(Review i : add) {
				reviews.add(i);
			}
			offset = offset + 25;
			loadFeed();
			this.setSelection(offset - 25);
		} else {
			Intent myIntent = new Intent(this, WebPlayer.class);
			Bundle bundle = new Bundle();
			bundle.putString("URL", reviews.get(position).getLink());
			bundle.putString("data", reviews.get(position).getContent());
			myIntent.putExtras(bundle);
			ReviewList.this.startActivity(myIntent);
		}
	} 

	private void loadFeed(){
    	try{
    		if(offset == 25) {
    			ReviewFeedParser parser = new ReviewFeedParser("http://api.giantbomb.com/reviews/?api_key=" + API_KEY + "&sort=-publish_date&limit=25&format=xml");
    			reviews = (ArrayList<Review>) parser.parse();
	    	}
	    	Review loadMore = new Review();
	    	loadMore.setTitle("Load 25 More...");
	    	loadMore.setScore(-1);
	    	reviews.add(loadMore);
	    	ReviewListAdapter adapter = new ReviewListAdapter(this, R.layout.videorow, reviews);
	    	this.setListAdapter(adapter);
    	} catch (Throwable t){
    	}
    }
}