package harris.GiantBomb;

import harris.GiantBomb.Forum.fThread;

import java.io.IOException;
import java.io.OptionalDataException;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ThreadPlayer extends ListActivity{
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.threadplayer);
		final Bundle bundle = getIntent().getExtras();
		((TextView) findViewById(R.id.threadtitle)).setText(bundle.getString("title"));
		final ProgressDialog dialog = ProgressDialog.show(ThreadPlayer.this, "",
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
					handler.sendMessage(handler.obtainMessage(-1, new ThreadListAdapter(
							list, R.layout.postrow, Forum.loadPosts(bundle.getString("URL")))));
				} catch (OptionalDataException e) {
				} catch (ClassNotFoundException e) {
				} catch (IOException e) {
				}
			}
		}.start();
	}
}
