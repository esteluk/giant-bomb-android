package harris.GiantBomb;

public class News implements Comparable<News>{
	private String title;
	private String link;
	private String content;
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
}
