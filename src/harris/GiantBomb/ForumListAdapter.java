package harris.GiantBomb;

import harris.GiantBomb.Forum.fThread;

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
public class ForumListAdapter extends ArrayAdapter<String> {

	ArrayList<fThread> threads;
	private final Map<String, Drawable> drawableMap;
	private LayoutInflater mInflater;
    
	@SuppressWarnings("unchecked")
	public ForumListAdapter(Context context, int textViewResourceId,
			ArrayList<fThread> threads) throws OptionalDataException,
			ClassNotFoundException, IOException {
		super(context, textViewResourceId, (List) threads);
		mInflater = LayoutInflater.from(context);
		this.threads = threads;
		drawableMap = new HashMap();
	}

	static class ViewHolder {
		ImageView thumb;
		TextView title;
		TextView desc;
		TextView byline;
		TextView bylinedate;
		TextView lastbyline;
		TextView lastbylinedate;
		TextView info;
	}

	// Sets up title, thumbnail, and description.
	// Thumbnails are loaded in a new thread for performance
	@Override
	public View getView(final int i, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.forumrow, null);
			holder = new ViewHolder();
			holder.thumb = (ImageView) convertView.findViewById(R.id.thumb);
			holder.title = (TextView) convertView.findViewById(R.id.forumtitle);
			holder.desc = (TextView) convertView.findViewById(R.id.desc);
			holder.byline = (TextView) convertView.findViewById(R.id.byline);
			holder.bylinedate = (TextView) convertView.findViewById(R.id.bylinedate);
			holder.lastbyline = (TextView) convertView.findViewById(R.id.lastbyline);
			holder.lastbylinedate = (TextView) convertView.findViewById(R.id.lastbylinedate);
			holder.info = (TextView) convertView.findViewById(R.id.info);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (threads.get(i) != null) {
				holder.title.setText(threads.get(i).title);
				holder.desc.setText(threads.get(i).section);
				holder.byline.setText(threads.get(i).author);
				holder.bylinedate.setText(threads.get(i).authorDate);
				holder.lastbyline.setText("Last post by " + threads.get(i).lastPoster);
				holder.lastbylinedate.setText(threads.get(i).lastPosterDate);
				holder.info.setText(threads.get(i).views + " views, " + threads.get(i).posts + " posts");

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
							drawable = fetchDrawable(threads.get(i).thumbUrl);
						} catch (MalformedURLException e) {
						} catch (IOException e) {
						}
						handler.sendMessage(handler.obtainMessage(0, drawable));
					}
				}.start();
		}
		return convertView;
	}

	public Drawable fetchDrawable(String urlString)
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