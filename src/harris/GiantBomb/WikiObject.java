package harris.GiantBomb;

public abstract class WikiObject {
	
	public enum ObjectType { GAME, FRANCHISE, CHARACTER, CONCEPT, OBJECT, LOCATION, PERSON, COMPANY };

	private ObjectType type;
	private String name;
	
	public WikiObject() {
		super();
	}
	
	public WikiObject(ObjectType type, String name) {
		super();
		this.type = type;
		this.name = name;
	}
	
	public ObjectType getType() {
		return type;
	}
	public void setType(ObjectType type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
