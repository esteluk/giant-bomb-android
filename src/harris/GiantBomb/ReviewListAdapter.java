package harris.GiantBomb;

import java.io.IOException;
import java.io.OptionalDataException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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
public class ReviewListAdapter extends ArrayAdapter<String> {

	ArrayList<Review> reviews;

	@SuppressWarnings("unchecked")
	public ReviewListAdapter(Context context, int textViewResourceId,
			ArrayList<Review> reviews) throws OptionalDataException,
			ClassNotFoundException, IOException {
		super(context, textViewResourceId, (List) reviews);
		this.reviews = reviews;
	}

	@Override
	public View getView(int i, View convertView, ViewGroup parent) {
		final int score = reviews.get(i).getScore();

		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.reviewrow, null);
		}
		
		if (i % 2 == 0) {
			v.setBackgroundResource(R.color.rowColor1);
		} else {
			v.setBackgroundResource(R.color.rowColor2);
		}

		final ImageView stars = (ImageView) v.findViewById(R.id.stars);
		final TextView title = (TextView) v.findViewById(R.id.reviewtitle);
		final TextView reviewername = (TextView) v.findViewById(R.id.reviewername);

		if (reviews.get(i) != null) {
			if (reviews.get(i).getScore() == -1) {
				title.setText(reviews.get(i).getGame().getName());
				reviewername.setText("");
				stars.setImageDrawable(null);
			} else {
				if (title != null) {
					title.setText(reviews.get(i).getGame().getName());
				}
				if (reviewername != null) {
					reviewername.setText("By " + reviews.get(i).getReviewer());
				}
				if (stars != null) {
					if (score == 0)
						stars.setImageResource(R.drawable.star0);
					else if (score == 1)
						stars.setImageResource(R.drawable.star1);
					else if (score == 2)
						stars.setImageResource(R.drawable.star2);
					else if (score == 3)
						stars.setImageResource(R.drawable.star3);
					else if (score == 4)
						stars.setImageResource(R.drawable.star4);
					else if (score == 5)
						stars.setImageResource(R.drawable.star5);
				}
			}
		}
		return v;
	}
}