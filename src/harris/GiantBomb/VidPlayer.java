package harris.GiantBomb;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
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
		VideoView vid = new VideoView(this);
		setContentView(vid);
		MediaController controller = new MediaController(this);
		vid.setMediaController(controller);
		Bundle bundle = getIntent().getExtras();
		vid.setVideoURI(Uri.parse(bundle.getString("URL")));
	}
}