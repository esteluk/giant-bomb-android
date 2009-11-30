package harris.GiantBomb;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class NewsView extends Activity{

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		WebView web = new WebView(this);
		setContentView(web);
		web.getSettings().setJavaScriptEnabled(true);
		web.loadDataWithBaseURL(bundle.getString("URL"), bundle.getString("data"), null, "utf-8", null);
	}
}
