package harris.GiantBomb;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class parses the feed given to it, and returns a list of video objects
 * 
 */
public class SearchResultParser implements api {

	private final URL feedUrl;

	public SearchResultParser(String feedUrl) {
		try {
			this.feedUrl = new URL(feedUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<WikiObject> parse() throws SAXException, IOException,
			ParserConfigurationException, FactoryConfigurationError {
		final List<WikiObject> wikiObjects = new ArrayList<WikiObject>();

		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc = docBuilder.parse(getInputStream());

		NodeList results = doc.getElementsByTagName("results").item(0)
				.getChildNodes();

		for (int i = 0; i < results.getLength(); i++) {
			Node node = results.item(i);
			if (node.getNodeName().equals("game")) {
				wikiObjects.add(WikiObjectParser.parseGame((Element) node));
			}
		}

		return wikiObjects;
	}

	private InputStream getInputStream() {
		try {
			return feedUrl.openConnection().getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
