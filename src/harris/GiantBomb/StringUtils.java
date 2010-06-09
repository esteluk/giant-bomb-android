package harris.GiantBomb;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	
	public static List<String> removeEmbeds(String in) {
		List<String> results = new ArrayList<String>();
		
		// testing code for extracting post when writing regexes
		/*try {
			FileWriter fstream = new FileWriter("/sdcard/out.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(in);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        
		Pattern youtubePattern = Pattern.compile("<div[^>]+?rel=\"video\".*?>.+?src=\"(http://www.youtube.com.+?)\".+?div>", Pattern.DOTALL);
		Matcher youtubeMatcher = youtubePattern.matcher(in);
		
		while (youtubeMatcher.find()) {
			results.add(youtubeMatcher.group(1));
		}
		
		Pattern gbPattern = Pattern.compile("<div id=\"\".+status= (http://.+?)\".+<div class=\"ft\"></div>.*?</div>", Pattern.DOTALL);
		//Pattern gbPattern = Pattern.compile("<div[^>]+?rel=\"(video|embed)\".*?>.+?paramsURI=(http%3A//www.giantbomb.com.+?)\".+?div>", Pattern.DOTALL);
		Matcher gbMatcher = gbPattern.matcher(youtubeMatcher.replaceAll("<i>Video removed, open Menu to view</i>"));
		
		while (gbMatcher.find()) {
			results.add(gbMatcher.group(1));
		}
		
		Pattern gbPattern2 = Pattern.compile("<object.*?\"whiskey-video-id\" value=\"(2587)\".*?</object>", Pattern.DOTALL);
		//Pattern gbPattern = Pattern.compile("<div[^>]+?rel=\"(video|embed)\".*?>.+?paramsURI=(http%3A//www.giantbomb.com.+?)\".+?div>", Pattern.DOTALL);
		Matcher gbMatcher2 = gbPattern2.matcher(gbMatcher.replaceAll("<i>Video removed, open Menu to view</i>"));
		
		while (gbMatcher2.find()) {
			results.add("http://www.giantbomb.com/video/17-" + gbMatcher2.group(1) + "/");
		}
		
		Pattern gbPattern3 = Pattern.compile("<div[^>]+?rel=\"(video|embed)\".*?>.+?paramsURI=(http%3A//www.giantbomb.com.+?)\".+?div>", Pattern.DOTALL);
        Matcher gbMatcher3 = gbPattern3.matcher(gbMatcher2.replaceAll("<i>Video removed, open Menu to view</i>"));
        
        while (gbMatcher3.find()) {
                results.add(gbMatcher3.group(2).replace("%3A", ":"));
        }
		
		results.add(0, gbMatcher3.replaceAll("<i>Video removed, open Menu to view</i>"));		
		
		return results;
		
	}

}
