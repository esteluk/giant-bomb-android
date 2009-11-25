package harris.GiantBomb;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Video player class
 *
 */
public class VidPlayer extends Activity {

	public VideoView vid;
	private String title;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		vid = new VideoView(this);
		this.setRequestedOrientation(0); // landscape
		Bundle bundle = getIntent().getExtras();
		title = bundle.getString("title");
		setTitle("Tap and press play to start video."); //user instructions for clarity
		MediaController controller = new MediaController(this);
		vid.setMediaController(controller);
		setContentView(vid);
		vid.setVideoURI(Uri.parse(bundle.getString("URL")));
		controller.show();
	}
	//changes title to video title once they tap the screen
	public boolean onTouchEvent(MotionEvent ev)
	{
		setTitle(title);
		return true;
	}
}