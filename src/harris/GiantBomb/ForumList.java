package harris.GiantBomb;

import harris.GiantBomb.Forum.fThread;

import java.io.IOException;
import java.io.OptionalDataException;
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

public class ForumList extends ListActivity {
	
	private ArrayList<fThread> threads;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.videolist);

		final ProgressDialog dialog = ProgressDialog.show(ForumList.this, "",
				"Loading. Please wait...", true);
		dialog.show();
		final ListActivity list = this;
		
		final Handler handler = new Handler() {
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message message) {
				dialog.dismiss();
				list.setListAdapter(((ArrayAdapter<fThread>) message.obj));
				registerForContextMenu(getListView());
			}
		};
		
		new Thread() {
			public void run() {
				try {
					threads = Forum.loadThreads("http://www.giantbomb.com/forums/");
					handler.sendMessage(handler.obtainMessage(-1, new ForumListAdapter(
							list, R.layout.forumrow, threads)));
				} catch (OptionalDataException e) {
				} catch (ClassNotFoundException e) {
				} catch (IOException e) {
				}
			}
		}.start();
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent myIntent = new Intent(this, ThreadPlayer.class);
		Bundle bundle = new Bundle();
		bundle.putString("URL", threads.get(position).url);
		bundle.putString("title", threads.get(position).title);
		myIntent.putExtras(bundle);
		ForumList.this.startActivity(myIntent);
	}
}