package harris.GiantBomb;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class SearchList extends ListActivity implements api {

	ArrayList<GBObject> results = new ArrayList<GBObject>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final Activity activity = this;
		final Intent queryIntent = getIntent();
		final String queryAction = queryIntent.getAction();
		if (Intent.ACTION_SEARCH.equals(queryAction)) {
			setContentView(R.layout.searchlist);
			String searchKeywords = queryIntent
					.getStringExtra(SearchManager.QUERY);

			getSearchResults(searchKeywords);

			if (results.size() > 0) {
				TextView title = (TextView) findViewById(R.id.title);
				title.setText("Results for \"" + searchKeywords + "\"");
				ListActivity list = this;
				list.setListAdapter(new SearchListAdapter(list,
						R.layout.searchrow, results));
			} else {
				AlertDialog alert = new AlertDialog.Builder(this).create();
				alert.setTitle("Search");
				alert.setMessage("No results found!");
				alert.setButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						activity.finish();
					}
				});
				alert.show();
			}
		}
	}

	private void getSearchResults(String queryString) {
		SearchResultParser parser = new SearchResultParser(
				"http://api.giantbomb.com/search/?api_key="
						+ API_KEY
						+ "&query="
						+ queryString.replace(' ', '+')
						+ "&limit=25&resources=game,franchise,character,concept,object,location,person,company&field_list=name,id,site_detail_url&format=xml");

		ArrayList<GBObject> add = new ArrayList<GBObject>(25);
		try {
			add = (ArrayList<GBObject>) parser.parse();
		} catch (Exception e) {
			System.err.println("Parsing failed" + e);
		}
		for (GBObject i : add) {
			results.add(i);
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent myIntent = new Intent(this, GBObjectViewer.class);
		Bundle bundle = new Bundle();
		bundle.putString("id", results.get(position).getId());
		bundle.putString("type", results.get(position).getType().toString());
		myIntent.putExtras(bundle);
		SearchList.this.startActivity(myIntent);
	}
}
