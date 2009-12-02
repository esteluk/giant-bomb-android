package harris.GiantBomb;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Lists videos, launches video player when one is clicked
 *
 */
public class VideoList extends ListActivity implements api{
	
	private ArrayList<Video> videos;
	private int offset = 0;
	
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
			loadFeed();
		} else {
			Intent myIntent = new Intent(this, VidPlayer.class);
			Bundle bundle = new Bundle();
			bundle.putString("URL", videos.get(position).getLink());
			bundle.putString("title", videos.get(position).getTitle());
			bundle.putString("siteDetailURL", videos.get(position).getSiteDetailURL());
			myIntent.putExtras(bundle);
			VideoList.this.startActivity(myIntent);
		}
	} 

	@SuppressWarnings("unchecked")
	private void loadFeed(){
        final ListActivity list = this;
        final ProgressDialog dialog = ProgressDialog.show(VideoList.this, "", 
                "Loading. Please wait...", true);
        dialog.show();
        
        final Handler handler = new Handler() {
        	@Override
        	public void handleMessage(Message message) {
                dialog.dismiss();
        		list.setListAdapter(((ArrayAdapter) message.obj));
        		list.setSelection(offset - 25);
        	}
        };
        
        Thread thread = new Thread() {
        	@Override
        	public void run() {
        		
        		try{
            		VideoFeedParser parser = new VideoFeedParser("http://api.giantbomb.com/videos/?api_key=" + API_KEY + "&sort=-publish_date&limit=25&field_list=name,deck,id,url,image,site_detail_url&format=xml&offset=" + offset);
        			offset = offset + 25;
        			ArrayList<Video> add = new ArrayList<Video>(25);
        			add = (ArrayList<Video>) parser.parse();
        			for(Video i : add) {
        				videos.add(i);
        			}
        			Video loadMore = new Video();
        	    	loadMore.setTitle("Load 25 More...");
        	    	loadMore.setId(-1);
        	    	videos.add(loadMore);
            		Message message;
            		message = handler.obtainMessage(-1, new VideoListAdapter(list, R.layout.videorow, videos));
            		handler.sendMessage(message);
        		} catch (Throwable t){
        		}
        	}
        };
        thread.start();
    }
}