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
	private int offset = 25;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videolist);
        videos = new ArrayList<Video>();
        loadFeed();
    }
    
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (videos.get(position).getId() == -1) {
			videos.remove(position);
			VideoFeedParser parser = new VideoFeedParser("http://api.giantbomb.com/videos/?api_key=" + API_KEY + "&sort=-publish_date&limit=25&format=xml&offset=" + offset);
			ArrayList<Video> add = new ArrayList<Video>(25);
			add = (ArrayList<Video>) parser.parse();
			for(Video i : add) {
				videos.add(i);
			}
			offset = offset + 25;
			loadFeed();
			this.setSelection(offset - 25);
		} else {
			Intent myIntent = new Intent(this, VidPlayer.class);
			Bundle bundle = new Bundle();
			bundle.putString("URL", videos.get(position).getLink());
			bundle.putString("title", videos.get(position).getTitle());
			myIntent.putExtras(bundle);
			VideoList.this.startActivity(myIntent);
		}
	} 

	private void loadFeed(){
    	try{
    		if(offset == 25) {
    			VideoFeedParser parser = new VideoFeedParser("http://api.giantbomb.com/videos/?api_key=" + API_KEY + "&sort=-publish_date&limit=25&format=xml");
    			videos = (ArrayList<Video>) parser.parse();
	    	}
	    	Video loadMore = new Video();
	    	loadMore.setTitle("Load 25 More...");
	    	loadMore.setId(-1);
	    	videos.add(loadMore);
	    	VideoListAdapter adapter = new VideoListAdapter(this, R.layout.videorow, videos);
	    	this.setListAdapter(adapter);
    	} catch (Throwable t){
    	}
    }
}