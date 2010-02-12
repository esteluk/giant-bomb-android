package harris.GiantBomb;

 

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.lang.StringEscapeUtils;

public class Forum {
    private static String data;
    private static String[] datas;
    public ArrayList<fThread> threads;
    private static ArrayList<Post> posts;
    
    static class Post {
        String author;
        String authorDate;
        String post;
        String thumbUrl;
        
        public String toString() {
            String r = "";
            r = r + "author: " + author + "\n";
            r = r + "authorDate: " + authorDate + "\n";
            r = r +  "thumbUrl: " + thumbUrl + "\n";
            r = r +  "post: " + post + "\n";
            return r;
           }
       }
    
    static class fThread {
        String title;
        String author;
        String authorDate;
        String lastPoster;
        String lastPosterDate;
        String views;
        String posts;
        String section;
        String thumbUrl;
        String url;
        
        public String toString() {
            String r = "";
            r = r + "title: " + title + "\n";
            r = r + "author: " + author + "\n";
            r = r + "authorDate: " + authorDate + "\n";
            r = r + "lastPoster: " + lastPoster + "\n";
            r = r + "lastPosterDate: " + lastPosterDate + "\n";
            r = r + "views: " + views + "\n";
            r = r + "posts: " + posts + "\n";
            r = r + "section: " + section + "\n";
            r = r +  "thumbUrl: " + thumbUrl + "\n";
            r = r +  "url: " + url + "\n";
            return r;
          }
    }
    public static ArrayList<fThread> loadThreads(String boardUrl) {
    	String data = "";
        try {
            URL url = new URL(boardUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()), 1000000);
            char[] cbuf = new char[1000000];
            in.read(cbuf, 0, 1000000);
            data = new String(cbuf);
            
            in.close();
            in = null;
            cbuf = null;
            
        } catch (IOException e) {
        }

        data = StringEscapeUtils.unescapeHtml(data.substring(data.indexOf("<tr class=\"js-topic-row\" ") + 25, data.indexOf("</tbody>")));

        String[] datas = data.split("</tr>");
        datas[datas.length - 1] = null;
        ArrayList<fThread> threads = new ArrayList<fThread>();
        fThread t;
        for (String i : datas) {
           if (i != null) {
            t = new fThread();
            t.thumbUrl = i.substring(i.indexOf("<img src=\"") + 10, i.indexOf("\" alt=\"\" /></td>"));
            
            i = i.substring(i.indexOf("<td class=\"title\">") + 18);
            t.url = "http://www.giantbomb.com" + i.substring(i.indexOf("<a href=\"") + 9, i.indexOf("\">"));
            t.title = i.substring(i.indexOf("\">") + 2, i.indexOf("</a>"));
            i = i.substring(i.indexOf("class=\"board\">") + 14);
            t.section = i.substring(0, i.indexOf("</a>"));
            i = i.substring(i.indexOf("<td class=\"time\">") + 17);
            t.author = i.substring(i.indexOf("\">") + 2, i.indexOf("</a>"));
            t.authorDate = i.substring(i.indexOf("<div>") + 5, i.indexOf("</div>"));
            i = i.substring(i.indexOf("<td class=\"time\">") + 17);
            t.lastPoster = i.substring(i.indexOf("\">") + 2, i.indexOf("</a>"));
            t.lastPosterDate = i.substring(i.indexOf("<div>") + 5, i.indexOf("</div>"));
            t.views = i.substring(i.indexOf("<td class=\"count\"><div>") + 23, i.indexOf("</div></td>"));
            i = i.substring(i.indexOf("</div></td>") + 12);
            t.posts = i.substring(i.indexOf("<td class=\"count\"><div>") + 23, i.indexOf("</div></td>"));
            threads.add(t);
        }
        }
        return threads;
    }
    
    public static ArrayList<Post> loadPosts(String threadUrl) {
        System.out.println("url: " + threadUrl);
        data = "";
        try {
            URL url = new URL(threadUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()), 1000000);
            char[] cbuf = new char[100000];
            in.read(cbuf, 0, 100000);
            data = new String(cbuf);
            in.close();
            in = null;
            cbuf = null;
            
        } catch (IOException e) {
        	System.out.println("IOEXCEPTION");
        }
        data = data.substring(data.indexOf("class=\"post\">") + 13, data.indexOf("<div id=\"\" class=\"content-pod menu\">"));
        datas = data.split("class=\"post\">");
        //datas[datas.length - 1] = null;
        posts = new ArrayList<Post>();
        for (String i : datas) {
           if (i != null) {
               Post p = new Post();
               i = i.substring(i.indexOf("Post by <a href=\"/profile/"));
               p.author = i.substring(i.indexOf("\">") + 2, i.indexOf("</a>"));
               p.thumbUrl = i.substring(i.indexOf("style=\"background-image: url(") + 29, i.indexOf(");\"></a>"));
               i = i.substring(i.indexOf("class=\"wiki-content\">"));
               p.post = "";
               p.post = i.substring(21, i.indexOf("</div>")).trim();
               System.out.println(i.indexOf("</span>"));
               i = i.substring(i.indexOf("<span class=\"post-date\">"));
               p.authorDate = i.substring(24, i.indexOf("</span>"));
               posts.add(p);
            }
    }
        return posts;
}
}