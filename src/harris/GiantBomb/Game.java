package harris.GiantBomb;

import java.io.IOException;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Game extends Activity {
	private WikiGame game;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
				
		Bundle bundle = getIntent().getExtras();
		try {
			game = WikiObjectParser.getGame(bundle.getString("id"));
		} catch (Exception e) {}
		
		((TextView)this.findViewById(R.id.name)).setText(game.getName());
		((TextView)this.findViewById(R.id.description)).setText(game.getDescription());
		
	}
}
