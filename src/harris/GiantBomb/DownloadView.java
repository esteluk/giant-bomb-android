package harris.GiantBomb;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DownloadView extends Activity {

	String url;

	ProgressBar progressView;
	int progress = 0;
	private Handler mHandler = new Handler();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download);

		TextView descriptionView = (TextView) this
				.findViewById(R.id.description);
		progressView = (ProgressBar) this.findViewById(R.id.progressBar);

		Bundle bundle = getIntent().getExtras();
		String title = bundle.getString("title");
		url = bundle.getString("URL");

		descriptionView.setText("Downloading " + title + "...");

		// Start lengthy operation in a background thread
		new Thread(new Runnable() {
			public void run() {

				URL item = null;
				URLConnection conn = null;
				try {
					item = new URL(url);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					conn = item.openConnection();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				progressView.setMax(conn.getContentLength());
				BufferedInputStream bis = null;
				try {
					bis = new BufferedInputStream(conn.getInputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				File root = Environment.getExternalStorageDirectory();
				File file = new File(root, /*"giantbomb/"
						+*/ item.getPath().substring(
								item.getPath().lastIndexOf('/') + 1));
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(file);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);
				byte data[] = new byte[1024];

				int count;
				try {
					while ((count = bis.read(data, 0, 1024)) != -1) {
						bos.write(data, 0, count);
						progress += count;

						// Update the progress bar
						mHandler.post(new Runnable() {
							public void run() {
								progressView.setProgress(progress);
							}
						});
					}
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}).start();
	}
}
