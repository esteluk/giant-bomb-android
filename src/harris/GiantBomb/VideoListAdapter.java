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
    private byte buf[] = new byte[1024];
    //private Handler handler;
    private Thread thread;
    private Drawable drawable;
    private Message message;
    private FileOutputStream fis;
    private InputStream is;
    private int len;
	private DefaultHttpClient httpClient;
	private HttpGet request;
	private HttpResponse response;
	private LayoutInflater mInflater;
	private ViewHolder holder;
    
	@SuppressWarnings("unchecked")
	public VideoListAdapter(Context context, int textViewResourceId,
			ArrayList<Video> videos) throws OptionalDataException,
			ClassNotFoundException, IOException {
		super(context, textViewResourceId, (List) videos);
		mInflater = LayoutInflater.from(context);
		this.videos = videos;
		drawableMap = new HashMap();
		holder = new ViewHolder();
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
		
	      buf = null;
	      //handler = null;
	      thread = null;
	      drawable = null;
	      message = null;
	      fis = null;
	      is = null;
	      len = 0;
		  httpClient = null;
		  request = null;
		  response = null;
		
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
				
				thread = new Thread() {
					@Override
					public void run() {
						try {
							drawable = fetchDrawable(videos.get(i).getThumbLink(), videos.get(i).getId());
						} catch (MalformedURLException e) {
						} catch (IOException e) {
						}
						message = handler.obtainMessage(0, drawable);
						handler.sendMessage(message);
					}
				};
				thread.start();
			}
		}
		return convertView;
	}

	public Drawable fetchDrawable(String urlString, int id)
			throws MalformedURLException, IOException {
		//if(drawableMap.containsKey(urlString)) {
		//	return drawableMap.get(urlString);
		//}
		List<String> files = Arrays.asList(getContext().fileList());
		if (files.indexOf(Integer.toString(id)) == -1) {
			fis = getContext().openFileOutput(
					Integer.toString(id), 0);
			is = fetch(urlString);
			buf = new byte[1024];
			len = 0;
			while ((len = is.read(buf)) > 0)
				fis.write(buf, 0, len);
			fis.close();
			is.close();
		}

		is = getContext().openFileInput(Integer.toString(id));
		drawable = Drawable.createFromStream(is, "src");
		drawableMap.put(urlString, drawable);
		return drawable;
	}
	
	private InputStream fetch(String urlString) throws MalformedURLException,
			IOException {
		httpClient = new DefaultHttpClient();
		request = new HttpGet(urlString);
		response = httpClient.execute(request);
		return response.getEntity().getContent();
	}
}