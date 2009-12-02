package harris.GiantBomb;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class NewsList extends ListActivity{
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
		Intent myIntent = new Intent(this, WebPlayer.class);
		Bundle bundle = new Bundle();
		bundle.putString("URL", news.get(position).getLink());
		bundle.putString("data", news.get(position).getContent());
		myIntent.putExtras(bundle);
		NewsList.this.startActivity(myIntent);
	}
    
	private void loadFeed(){
    	try{
	    	NewsFeedParser parser = new NewsFeedParser("http://feeds.feedburner.com/GiantBombNews?format=xml");
	    	news = (ArrayList<News>) parser.parse();
	    	//ArrayAdapter<News> adapter = new ArrayAdapter<News>(this, R.layout.newsrow, news);
	    	
	    	NewsListAdapter adapter = new NewsListAdapter(this, R.layout.newsrow, news);
	    	this.setListAdapter(adapter);
	    	
	    	//this.setListAdapter(adapter);
    	} catch (Throwable t){
    	}
    }
    
}
