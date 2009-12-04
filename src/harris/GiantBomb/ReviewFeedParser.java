package harris.GiantBomb;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

/**
 * This class parses the feed given to it, and returns a list of video objects
 * 
 */
public class ReviewFeedParser implements api {

	private final URL feedUrl;

	public ReviewFeedParser(String feedUrl) {
		try {
			this.feedUrl = new URL(feedUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Review> parse() {
		final Review currentReview = new Review();
		RootElement root = new RootElement("response");
		final List<Review> reviews = new ArrayList<Review>();
		Element channel = root.getChild("results");
		Element item = channel.getChild("review");
		item.setEndElementListener(new EndElementListener() {
			public void end() {
				reviews.add(currentReview.copy());
			}
		});
		item.getChild("game").getChild("name").setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currentReview.setTitle(body);
					}
				});
		item.getChild("site_detail_url").setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currentReview.setLink(body);
					}
				});
		item.getChild("score").setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currentReview.setScore(Integer.decode(body));
					}
				});
		item.getChild("description").setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						body = StringEscapeUtils.unescapeHtml(body);
						currentReview.setContent(body);
					}
				});
		try {
			Xml.parse(this.getInputStream(), Xml.Encoding.UTF_8, root
					.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return reviews;
	}

	private InputStream getInputStream() {
		try {
			return feedUrl.openConnection().getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
