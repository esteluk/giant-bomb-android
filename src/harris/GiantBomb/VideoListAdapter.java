package harris.GiantBomb;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OptionalDataException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Custom adapter
 * 
 */
public class VideoListAdapter extends ArrayAdapter<String> {

	ArrayList<Video> videos;
	private final Map<String, Drawable> drawableMap;

	@SuppressWarnings("unchecked")
	public VideoListAdapter(Context context, int textViewResourceId,
			ArrayList<Video> videos) throws OptionalDataException,
			ClassNotFoundException, IOException {
		super(context, textViewResourceId, (List) videos);
		this.videos = videos;
		drawableMap = new HashMap();
	}

	// Sets up title, thumbnail, and description.
	// Thumbnails are loaded in a new thread for performance
	@Override
	public View getView(int i, View convertView, ViewGroup parent) {
		final int id = videos.get(i).getId();
		final String thumbLink = videos.get(i).getThumbLink();

		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.videorow, null);
		}

		final ImageView thumb = (ImageView) v.findViewById(R.id.thumb);
		final TextView title = (TextView) v.findViewById(R.id.videotitle);
		final TextView desc = (TextView) v.findViewById(R.id.desc);

		if (videos.get(i) != null) {
			if (videos.get(i).getId() == -1) {
				title.setText(videos.get(i).getTitle());
				desc.setText("");
				thumb.setImageDrawable(null);
			} else {
				if (title != null) {
					title.setText(videos.get(i).getTitle());
				}
				if (desc != null) {
					desc.setText(videos.get(i).getDesc());
					if (title.getText().length() > 37) { // check if the title
															// is going to be 2
															// lines or not
						desc.setMaxLines(1); // set max lines for desc
												// accordingly
					} else {
						desc.setMaxLines(2);
					}
				}
				if (thumb != null) {
					final Handler handler = new Handler() {
						@Override
						public void handleMessage(Message message) {
							if (message.what == -1) {
								thumb.setImageResource(R.drawable.loading); // set
																			// black
																			// loading
																			// image
							} else {
								thumb.setImageDrawable((Drawable) message.obj);
							}
						}
					};

					Thread thread = new Thread() {
						@Override
						public void run() {
							Message message;
							message = handler.obtainMessage(-1, null); // tell
																		// handler
																		// to
																		// set
																		// black
																		// loading
																		// image
							handler.sendMessage(message);
							Drawable drawable = null;
							try {
								drawable = fetchDrawable(thumbLink, id);
							} catch (MalformedURLException e) {
							} catch (IOException e) {
							}
							message = handler.obtainMessage(id, drawable);// set
																			// actual
																			// image,
																			// once
																			// it's
																			// loaded
							handler.sendMessage(message);
						}
					};
					thread.start();
				}
			}
		}
		return v;
	}

	public Drawable fetchDrawable(String urlString, int id)
			throws MalformedURLException, IOException {
		List<String> files = Arrays.asList(getContext().fileList());
		if (files.indexOf(Integer.toString(id)) == -1) {
			FileOutputStream fis = getContext().openFileOutput(
					Integer.toString(id), 0);
			InputStream is = fetch(urlString);
			byte buf[] = new byte[1024];
			int len;
			while ((len = is.read(buf)) > 0)
				fis.write(buf, 0, len);
			fis.close();
			is.close();
		}

		InputStream is = getContext().openFileInput(Integer.toString(id));
		Drawable drawable = Drawable.createFromStream(is, "src");
		drawableMap.put(urlString, drawable);
		return drawable;
	}

	private InputStream fetch(String urlString) throws MalformedURLException,
			IOException {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(urlString);
		HttpResponse response = httpClient.execute(request);
		return response.getEntity().getContent();
	}
}