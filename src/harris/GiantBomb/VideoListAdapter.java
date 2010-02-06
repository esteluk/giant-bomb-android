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

	static class ViewHolder {
		ImageView thumb;
		TextView title;
		TextView desc;
	}

	// Sets up title, thumbnail, and description.
	// Thumbnails are loaded in a new thread for performance
	@Override
	public View getView(int i, View convertView, ViewGroup parent) {
		final int id = videos.get(i).getId();
		final String thumbLink = videos.get(i).getThumbLink();

		View v = convertView;
		final ViewHolder holder;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.videorow, null);
			holder = new ViewHolder();
			holder.thumb = (ImageView) v.findViewById(R.id.thumb);
			holder.title = (TextView) v.findViewById(R.id.videotitle);
			holder.desc = (TextView) v.findViewById(R.id.desc);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		if (videos.get(i) != null) {
			if (id == -1) {
				holder.title.setText(videos.get(i).getTitle());
				holder.desc.setText("");
				holder.thumb.setImageDrawable(null);
			} else {
				holder.title.setText(videos.get(i).getTitle());
				holder.desc.setText(videos.get(i).getDesc());

				final Handler handler = new Handler() {
					public void handleMessage(Message message) {
						holder.thumb.setImageDrawable((Drawable) message.obj);
					}
				};
				
				Thread thread = new Thread() {
					@Override
					public void run() {
						//holder.thumb.setImageResource(R.drawable.loading);
						Drawable drawable = null;
						try {
							drawable = fetchDrawable(thumbLink, id);
						} catch (MalformedURLException e) {
						} catch (IOException e) {
						}
						Message message = handler.obtainMessage(0, drawable);
						handler.sendMessage(message);
					}
				};
				thread.start();
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