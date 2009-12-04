package harris.GiantBomb;

import harris.GiantBomb.WikiObject.ObjectType;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class WikiObjectParser implements api {

	public static WikiGame getGame(String id)
			throws ParserConfigurationException, FactoryConfigurationError,
			SAXException, IOException {
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		URL url = new URL("http://api.giantbomb.com/game/" + id + "/?api_key="
				+ API_KEY + "&field_list=id,name,description&format=xml");
		Document doc = docBuilder.parse(url.openConnection().getInputStream());

		return parseGame((Element) doc.getElementsByTagName("results").item(0));
	}

	public static WikiGame parseGame(Element el) {
		WikiGame game = new WikiGame();
		game.setName(el.getElementsByTagName("name").item(0).getFirstChild()
				.getNodeValue());
		game.setId(el.getElementsByTagName("id").item(0).getFirstChild()
				.getNodeValue());

		try {
			game.setDescription(el.getElementsByTagName("description").item(0)
					.getFirstChild().getNodeValue());
		} catch (Exception e) {
		}
		;

		game.setType(ObjectType.GAME);

		return game;
	}
}
