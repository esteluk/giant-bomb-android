package harris.GiantBomb;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SearchListAdapter extends ArrayAdapter<String> {

	ArrayList<GBObject> results = new ArrayList<GBObject>();

	@SuppressWarnings("unchecked")
	public SearchListAdapter(Context context, int textViewResourceId,
			ArrayList<GBObject> results) {
		super(context, textViewResourceId, (List) results);
		this.results = results;
	}

	public View getView(int i, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.searchrow, null);
		}
		
		if (i % 2 == 0) {
			v.setBackgroundResource(R.color.rowColor1);
		} else {
			v.setBackgroundResource(R.color.rowColor2);
		}

		final TextView title = (TextView) v.findViewById(R.id.itemtitle);
		final TextView type = (TextView) v.findViewById(R.id.itemtype);

		title.setText(results.get(i).getName());
		type.setText(results.get(i).getType().toString());

		return v;
	}
}