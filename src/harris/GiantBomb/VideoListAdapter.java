package harris.GiantBomb;

import java.io.IOException;
import java.io.InputStream;
import java.io.OptionalDataException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private LayoutInflater mInflater;
    
	@SuppressWarnings("unchecked")
	public VideoListAdapter(Context context, int textViewResourceId,
			ArrayList<Video> videos) throws OptionalDataException,
			ClassNotFoundException, IOException {
		super(context, textViewResourceId, (List) videos);
		mInflater = LayoutInflater.from(context);
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
	public View getView(final int i, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.videorow, null);
			holder = new ViewHolder();
			holder.thumb = (ImageView) convertView.findViewById(R.id.thumb);
			holder.title = (TextView) convertView.findViewById(R.id.videotitle);
			holder.desc = (TextView) convertView.findViewById(R.id.desc);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.thumb.setImageResource(R.drawable.loading);
		if (videos.get(i) != null) {
			if (videos.get(i).getId() == -1) {
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
				
				new Thread() {
					@Override
					public void run() {
						Drawable drawable = null;
						try {
							drawable = fetchDrawable(videos.get(i).getThumbLink(), videos.get(i).getId());
						} catch (MalformedURLException e) {
						} catch (IOException e) {
						}
						handler.sendMessage(handler.obtainMessage(0, drawable));
					}
				}.start();
			}
		}
		return convertView;
	}

	public Drawable fetchDrawable(String urlString, int id)
			throws MalformedURLException, IOException {
		if(drawableMap.containsKey(urlString)) {
			return drawableMap.get(urlString);
		}
		drawableMap.put(urlString, Drawable.createFromStream(fetch(urlString), "src"));
		return drawableMap.get(urlString);
	}
	
	private InputStream fetch(String urlString) throws MalformedURLException,
			IOException {
		return new DefaultHttpClient().
			execute(new HttpGet(urlString)).
			getEntity().
			getContent();
	}
}