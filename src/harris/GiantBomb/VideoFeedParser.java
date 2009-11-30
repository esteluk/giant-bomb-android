package harris.GiantBomb;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

/**
 * This class parses the feed given to it, and returns a list of video objects
 *
 */
public class VideoFeedParser implements api{

	private final URL feedUrl;
	public VideoFeedParser(String feedUrl) {
		try {
			this.feedUrl = new URL(feedUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Video> parse() {
		final Video currentVideo = new Video();
		RootElement root = new RootElement("response");
		final List<Video> videos = new ArrayList<Video>();
		Element channel = root.getChild("results");
		Element item = channel.getChild("video");
		item.setEndElementListener(new EndElementListener(){
			public void end() {
				videos.add(currentVideo.copy());
			}
		});
		item.getChild("name").setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentVideo.setTitle(body);
			}
		});
		item.getChild("deck").setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentVideo.setDesc(body);
			}
		});
		item.getChild("id").setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentVideo.setId(Integer.decode(body));
			}
		});
		item.getChild("url").setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				//template: http://media.giantbomb.com/video/vf_buzzquizworld_ql_ip.m4v?api_key=98a36880538752a0bc32691ff737a408bc82fd94
				if(body.indexOf(".flv") != -1) { //Get the iPhone formatted GB video. Some videos are formatted a little different.
					body = "http://media.giantbomb.com/video/" + body.substring(0, body.indexOf(".flv")) + "_ip.m4v?api_key=" + API_KEY;
				} else {
					body = "http://media.giantbomb.com/video/" + body + "_ip.m4v?api_key=" + API_KEY;
				}
				currentVideo.setLink(body);
			}
		});
		item.getChild("image").getChild("small_url").setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentVideo.setThumbLink(body);
			}
		});
		try {
			Xml.parse(this.getInputStream(), Xml.Encoding.UTF_8, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return videos;
	}

	private InputStream getInputStream() {
		try {
			return feedUrl.openConnection().getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
