package harris.GiantBomb;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class SearchList extends ListActivity implements api {

	ArrayList<WikiObject> results = new ArrayList<WikiObject>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Intent queryIntent = getIntent();
		final String queryAction = queryIntent.getAction();
		if (Intent.ACTION_SEARCH.equals(queryAction)) {
			setContentView(R.layout.searchlist);
			String searchKeywords = queryIntent
					.getStringExtra(SearchManager.QUERY);

			getSearchResults(searchKeywords);

			ListActivity list = this;
			list.setListAdapter(new SearchListAdapter(list, R.layout.searchrow,
					results));
		}
	}

	private void getSearchResults(String queryString) {
		SearchResultParser parser = new SearchResultParser(
				"http://api.giantbomb.com/search/?api_key="
						+ API_KEY
						+ "&query="
						+ queryString.replace(' ', '+')
						+ "&limit=25&resources=game&field_list=name,id&format=xml");

		ArrayList<WikiObject> add = new ArrayList<WikiObject>(25);
		try {
			add = (ArrayList<WikiObject>) parser.parse();
		} catch (Exception e) {
			System.err.println("Parsing failed" + e);
		}
		for (WikiObject i : add) {
			results.add(i);
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent myIntent = new Intent(this, Game.class);
		Bundle bundle = new Bundle();
		bundle.putString("id", results.get(position).getId());
		myIntent.putExtras(bundle);
		SearchList.this.startActivity(myIntent);
	}
}
