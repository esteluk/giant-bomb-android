package harris.GiantBomb;

public class DashboardIcon {
	private int resId;
	private String title;
	private Class clazz;
	
	public DashboardIcon(int resId, String title, Class clazz) {
		this.resId = resId;
		this.title = title;
		this.setClazz(clazz);
	}
	
	public int getResId() {
		return resId;
	}
	public void setResId(int resId) {
		this.resId = resId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}
	public Class getClazz() {
		return clazz;
	}	
}
