package harris.GiantBomb;

import harris.GiantBomb.Forum.Post;

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
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ThreadListAdapter extends ArrayAdapter<String> {

	ArrayList<Post> posts;
	private final Map<String, Drawable> drawableMap;
	private LayoutInflater mInflater;

	@SuppressWarnings("unchecked")
	public ThreadListAdapter(Context context, int textViewResourceId,
			ArrayList<Post> posts) throws OptionalDataException,
			ClassNotFoundException, IOException {
		super(context, textViewResourceId, (List) posts);
		mInflater = LayoutInflater.from(context);
		this.posts = posts;
		drawableMap = new HashMap();
	}

	static class ViewHolder {
		ImageView thumb;
		WebView post;
		TextView desc;
	}

	public View getView(final int i, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.postrow, null);
			holder = new ViewHolder();
			holder.thumb = (ImageView) convertView.findViewById(R.id.thumb);
			holder.desc = (TextView) convertView.findViewById(R.id.desc);
			holder.post = (WebView) convertView.findViewById(R.id.post);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.thumb.setImageResource(R.drawable.loading);
		System.out.println(i);
		if (posts.get(i) != null) {
			System.out.println(posts.get(i).post);
			holder.post.loadData(posts.get(i).post, "text/html", "utf-8");
			holder.desc.setText("By " + posts.get(i).author + ", "
					+ posts.get(i).authorDate);
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
						drawable = fetchDrawable(posts.get(i).thumbUrl);
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
		if (drawableMap.containsKey(urlString)) {
			return drawableMap.get(urlString);
		}
		drawableMap.put(urlString, Drawable.createFromStream(fetch(urlString),
				"src"));
		return drawableMap.get(urlString);
	}

	private InputStream fetch(String urlString) throws MalformedURLException,
			IOException {
		return new DefaultHttpClient().execute(new HttpGet(urlString))
				.getEntity().getContent();
	}

}
