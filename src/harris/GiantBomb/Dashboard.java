package harris.GiantBomb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class Dashboard extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dashboard);
		final Context activity = this;
		
		ImageView news = (ImageView) this.findViewById(R.id.news);
		news.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent myIntent = new Intent(activity, NewsList.class);
				activity.startActivity(myIntent);
			}			
		});
		ImageView reviews = (ImageView) this.findViewById(R.id.reviews);
		reviews.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent myIntent = new Intent(activity, ReviewList.class);
				activity.startActivity(myIntent);
			}			
		});
		ImageView videos = (ImageView) this.findViewById(R.id.videos);
		videos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent myIntent = new Intent(activity, VideoList.class);
				activity.startActivity(myIntent);
			}			
		});
		ImageView bombcast = (ImageView) this.findViewById(R.id.bombcast);
		bombcast.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent myIntent = new Intent(activity, BombcastList.class);
				activity.startActivity(myIntent);
			}			
		});
	}

}
