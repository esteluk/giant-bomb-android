package harris.GiantBomb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Video player class
 *
 */
public class VidPlayer extends Activity {
	public static final int MENU_SHARE = Menu.FIRST;
	private String siteDetailURL;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(0); // landscape
		final VideoView vid = new VideoView(this);
		setContentView(vid);
		MediaController controller = new MediaController(this);
		vid.setMediaController(controller);
		Bundle bundle = getIntent().getExtras();
		siteDetailURL = bundle.getString("siteDetailURL");
		vid.setVideoURI(Uri.parse(bundle.getString("URL")));
		
		final ProgressDialog dialog = new ProgressDialog(VidPlayer.this, ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("Buffering. Please wait...");
		dialog.setButton(ProgressDialog.BUTTON_NEGATIVE,"Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				VidPlayer.this.finish();
			}
		});
        dialog.show();
		
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
					dialog.dismiss();
			}
		};

		Thread thread = new Thread() {
			@Override
			public void run() {
				try{
					while(!vid.isPlaying()) {}
					
            		handler.sendEmptyMessage(0);
				} catch (Throwable t){
				}
			}
		};
		thread.start();
		}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem share = menu.add(0, MENU_SHARE, MENU_SHARE, "Share");
		
		share.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				
				Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(Intent.EXTRA_TEXT, siteDetailURL);				
				startActivity(Intent.createChooser(shareIntent, "Share link with..."));
				
				return true;
			}
		});
		
		return true;
	}
}



