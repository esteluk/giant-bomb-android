package harris.GiantBomb;

import android.app.Activity;
import android.app.ProgressDialog;
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
		vid.stopPlayback();
		
		final ProgressDialog dialog = ProgressDialog.show(VidPlayer.this, "", 
                "Buffering. Please wait...", true);
        dialog.show();
		
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				if(message.what == -1) {
					dialog.dismiss();
					vid.start();
				}
			}
		};

		Thread thread = new Thread() {
			@Override
			public void run() {
				
				try{
					Boolean buffering = true;
					int buffer = 0;
					while(buffering) {
						if(buffer < vid.getBufferPercentage())
							buffer = vid.getBufferPercentage();
						if(buffer > 24)
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



