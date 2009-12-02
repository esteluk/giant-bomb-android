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
    
	@SuppressWarnings("unchecked")
	private void loadFeed(){
        final ListActivity list = this;
        final ProgressDialog dialog = ProgressDialog.show(NewsList.this, "", 
                "Loading. Please wait...", true);
        dialog.show();
        
        final Handler handler = new Handler() {
        	@Override
        	public void handleMessage(Message message) {
                dialog.dismiss();
        		list.setListAdapter(((ArrayAdapter) message.obj));
        	}
        };
        
        Thread thread = new Thread() {
        	@Override
        	public void run() {
        		
        		try{
        			NewsFeedParser parser = new NewsFeedParser("http://feeds.feedburner.com/GiantBombNews?format=xml");
        			news = (ArrayList<News>) parser.parse();
            		Message message;
            		message = handler.obtainMessage(-1, new NewsListAdapter(list, R.layout.newsrow, news));
            		handler.sendMessage(message);
        		} catch (Throwable t){
        		}
        	}
        };
        thread.start();
    }
    
}
