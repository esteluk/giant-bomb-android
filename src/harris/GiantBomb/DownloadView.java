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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DownloadView extends Activity {

	String url;
	String fileLocation;

	ProgressBar progressView;
	int progress = 0;
	private Handler mHandler = new Handler();
	Button button;
	Context context = this;

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

		if (!url.endsWith("mp3")) {
			this.findViewById(R.id.disclaimer).setVisibility(View.GONE);
			this.findViewById(R.id.urlBox).setVisibility(View.GONE);
		}

		button = (Button) this.findViewById(R.id.button);
		button.setText("Cancel");

		// Start lengthy operation in a background thread
		final EndableThread downloadThread = new EndableThread() {
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

				// create giantbomb directory if it doesn't exist
				File gbDir = new File(root, "giantbomb");
				if (!gbDir.exists()) {
					gbDir.mkdir();
				}

				File file = new File(root, "giantbomb/"
						+ item.getPath().substring(
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
					while (isDone == false
							&& (count = bis.read(data, 0, 1024)) != -1) {
						bos.write(data, 0, count);
						progress += count;

						// Update the progress bar
						mHandler.post(new Runnable() {
							public void run() {
								progressView.setProgress(progress);
							}
						});
					}

					// delete the file if it was cancelled
					if (isDone) {
						file.delete();
					} else {
						fileLocation = "file://" + file.getAbsolutePath();

						mHandler.post(new Runnable() {
							public void run() {

								if (fileLocation.endsWith("mp3")) {
									button.setText("Listen");
								} else {
									button.setText("View");
								}

								button
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View arg0) {
												String type;
												if (fileLocation
														.endsWith("mp3")) {
													type = "audio/mp3";
												} else {
													type = "video";
												}

												Intent intent = new Intent(
														android.content.Intent.ACTION_VIEW);
												intent.setDataAndType(Uri
														.parse(fileLocation),
														type);
												startActivity(intent);
											}
										});
							}
						});
					}
				} catch (IOException ioe) {
					mHandler.post(new Runnable() {
						public void run() {
							button.setEnabled(false);
							
							AlertDialog alert = new AlertDialog.Builder(context)
									.create();
							alert.setTitle("Error");
							alert.setMessage("Error downloading item from "
									+ url);
							alert.setButton("OK",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											return;
										}
									});
							alert.show();
						}
					});
				}
			}
		};
		downloadThread.start();

		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				downloadThread.end();
				button.setEnabled(false);
			}
		});
	}
}
