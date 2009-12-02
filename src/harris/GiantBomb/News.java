package harris.GiantBomb;

public class News implements Comparable<News>{
	private String title;
	private String link;
	private String content;
	private String pubdate;
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}
	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}
	@Override
	public int compareTo(News another) {
		if (title == another.getTitle())
			return 1;
		return 0;
	}
	public News copy() {
		News copy = new News();
		copy.title = title;
		copy.link = link;
		copy.content = content;
		copy.pubdate = pubdate;
		return copy;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param pubdate the pubdate to set
	 */
	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}
	/**
	 * @return the pubdate
	 */
	public String getPubdate() {
		return pubdate;
	}
}
