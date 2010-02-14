package harris.GiantBomb;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.nullwire.trace.ExceptionHandler;

/**
 * Video player class
 * 
 */
public class VidPlayer extends Activity {
	public static final int MENU_SHARE = Menu.FIRST;
	private String siteDetailURL;
	private VideoView vid;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.videoview);
		vid = (VideoView) findViewById(R.id.surface_view);
		ExceptionHandler.register(this, "http://harrism.com/GB/server.php"); 
		Bundle bundle = getIntent().getExtras();
		vid.setVideoURI(Uri.parse(bundle.getString("URL")));
		vid.setMediaController(new MediaController(this));
		vid.requestFocus();
		siteDetailURL = bundle.getString("siteDetailURL");
	}
	
	public void onResume() {

		Toast.makeText(VidPlayer.this, "Buffering..." , Toast.LENGTH_SHORT).show();
		SharedPreferences resume = VidPlayer.this.getSharedPreferences("VideoResume", MODE_WORLD_READABLE);
		if(resume.contains(siteDetailURL)) {
			vid.seekTo(resume.getInt(siteDetailURL, 0));
			Toast.makeText(VidPlayer.this, "Resuming Video" , Toast.LENGTH_SHORT).show();
		}
		vid.start();

		super.onResume();
	}
	
	public void onPause() {
		SharedPreferences.Editor editor = VidPlayer.this.getSharedPreferences("VideoResume", MODE_WORLD_WRITEABLE).edit();
		editor.putInt(siteDetailURL, vid.getCurrentPosition());
		editor.commit();
		super.onPause();
	}
	
	public void onStop() {
		Toast.makeText(VidPlayer.this, "Video Position Saved", Toast.LENGTH_SHORT).show();
		super.onStop();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem share = menu.add(0, MENU_SHARE, MENU_SHARE, "Share");
		
		share.setIcon(android.R.drawable.ic_menu_share);

		share.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {

				Intent shareIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(Intent.EXTRA_TEXT, siteDetailURL);
				startActivity(Intent.createChooser(shareIntent,
						"Share link with..."));

				return true;
			}
		});

		return true;
	}
	
}
