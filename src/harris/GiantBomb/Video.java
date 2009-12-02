package harris.GiantBomb;


/**
 * Video object
 *
 */
public class Video implements Comparable<Video>{
	private String title;
	private String link;
	private String thumbLink;
	private String desc;
	private String siteDetailURL;
	private int id;

	

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title.trim();
	}
	
	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}

	public String getThumbLink() {
		return thumbLink;
	}

	public void setThumbLink(String thumbLink) {
		this.thumbLink = thumbLink;
	}
	
	public Video copy(){
		Video copy = new Video();
		copy.title = title;
		copy.desc = desc;
		copy.link = link;
		copy.thumbLink = thumbLink;
		copy.id = id;
		copy.siteDetailURL = siteDetailURL;
		return copy;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Title: ");
		sb.append(title);
		sb.append('\n');
		sb.append('\n');
		sb.append("Link: ");
		sb.append(link);
		sb.append('\n');
		sb.append("Description: ");
		sb.append(thumbLink);
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((thumbLink == null) ? 0 : thumbLink.hashCode());
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Video other = (Video) obj;
		if (thumbLink == null) {
			if (other.thumbLink != null)
				return false;
		} else if (!thumbLink.equals(other.thumbLink))
			return false;
		if (link == null) {
			if (other.link != null)
				return false;
		} else if (!link.equals(other.link))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public int compareTo(Video another) {
		if (this.hashCode() == another.hashCode()) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param siteDetailURL the siteDetailURL to set
	 */
	public void setSiteDetailURL(String siteDetailURL) {
		this.siteDetailURL = siteDetailURL;
	}

	/**
	 * @return the siteDetailURL
	 */
	public String getSiteDetailURL() {
		return siteDetailURL;
	}

}
