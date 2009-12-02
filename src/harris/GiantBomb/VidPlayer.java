package harris.GiantBomb;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Video player class
 *
 */
public class VidPlayer extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(0); // landscape
		final VideoView vid = new VideoView(this);
		setContentView(vid);
		MediaController controller = new MediaController(this);
		vid.setMediaController(controller);
		Bundle bundle = getIntent().getExtras();
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
				if(message.what == -1) {
					dialog.dismiss();
				}
			}
		};

		Thread thread = new Thread() {
			@Override
			public void run() {
				
				try{
					Boolean buffering = true;
					while(buffering) {
						if(vid.isPlaying()) 
							buffering = false;
					}
					Message message;
            		message = handler.obtainMessage(-1);
            		handler.sendMessage(message);
				} catch (Throwable t){
				}
			}
		};
		thread.start();
		}
}



