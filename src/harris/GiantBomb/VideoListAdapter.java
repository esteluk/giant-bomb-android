package harris.GiantBomb;

import java.io.FileNotFoundException;
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
import android.os.AsyncTask;
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
    private byte buf[];
    private Drawable drawable;
    //private FileOutputStream fis;
    private InputStream is;
    private int len;
	private DefaultHttpClient httpClient;
	private HttpGet request;
	private HttpResponse response;
	private LayoutInflater mInflater;
	private ViewHolder holder;
	private ThumbInfo thumbInfo;
    
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
	      drawable = null;
	      //fis = null;
	      is = null;
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
				
				thumbInfo = new ThumbInfo();
				thumbInfo.imageview = holder.thumb;
				thumbInfo.url = videos.get(i).getThumbLink();
				thumbInfo.id = i;
				new DownloadThumbsTask().execute(thumbInfo);
				
			}
		}
		return convertView;
	}

	static class ThumbInfo {
		static ImageView imageview;
		static String url;
		static int id;
		static Drawable drawable;
	}
	
	private class DownloadThumbsTask extends AsyncTask<ThumbInfo, Integer, ThumbInfo> {

		@Override
		protected ThumbInfo doInBackground(ThumbInfo... params) {
			try {
			      buf = null;
			      drawable = null;
			      //fis = null;
			      is = null;
				  httpClient = null;
				  request = null;
				  response = null;
            List<String> files = Arrays.asList(getContext().fileList());
            if (files.indexOf(Integer.toString(ThumbInfo.id)) == -1) {
            		FileOutputStream fis = getContext().openFileOutput(
                                    Integer.toString(ThumbInfo.id), 0);
            		httpClient = new DefaultHttpClient();
            		request = new HttpGet(ThumbInfo.url);
            		response = httpClient.execute(request);
                    InputStream is = response.getEntity().getContent();
                    byte buf[] = new byte[1024];
                    int len = 0;
                    while ((len = is.read(buf)) > 0)
                            fis.write(buf, 0, len);
                    fis.close();
                    is.close();
            }

            InputStream is = getContext().openFileInput(Integer.toString(ThumbInfo.id));
            Drawable drawable = Drawable.createFromStream(is, "src");
            drawableMap.put(ThumbInfo.url, drawable);
            ThumbInfo.drawable = drawable;
            return params[0];

			} catch (MalformedURLException e) {
            } catch (IOException e) {
            }
            return null;
		}
		
		protected void onPostExecute(ThumbInfo thumbinfo) {
			thumbinfo.imageview.setImageDrawable(thumbinfo.drawable);
		}
	}
}