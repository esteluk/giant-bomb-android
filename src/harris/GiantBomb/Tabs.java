package harris.GiantBomb;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class Tabs extends TabActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Intent news = new Intent(this, NewsList.class);
        Intent videos = new Intent(this, VideoList.class);
        
        TabHost tabs = getTabHost();

        tabs.addTab(tabs.newTabSpec("news").setIndicator("News").setContent(news));
        tabs.addTab(tabs.newTabSpec("videos").setIndicator("Videos").setContent(videos));
        
        tabs.setCurrentTab(0);
    }

}