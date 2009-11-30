package harris.GiantBomb;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * Lists videos, launches video player when one is clicked
 *
 */
public class VideoList extends ListActivity implements api{
	
	private ArrayList<Video> videos;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videolist);
        loadFeed();
    }

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent myIntent = new Intent(this, VidPlayer.class);
		Bundle bundle = new Bundle();
		bundle.putString("URL", videos.get(position).getLink());
		bundle.putString("title", videos.get(position).getTitle());
		myIntent.putExtras(bundle);
		VideoList.this.startActivity(myIntent);
	}

	private void loadFeed(){
    	try{
	    	VideoFeedParser parser = new VideoFeedParser("http://api.giantbomb.com/videos/?api_key=" + API_KEY + "&sort=-publish_date&limit=25&format=xml");
	    	videos = (ArrayList<Video>) parser.parse();
	    	VideoListAdapter adapter = new VideoListAdapter(this, R.layout.videorow, videos);
	    	this.setListAdapter(adapter);
    	} catch (Throwable t){
    	}
    }
}