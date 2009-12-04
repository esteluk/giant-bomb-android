package harris.GiantBomb;

public abstract class WikiObject {

	public enum ObjectType {
		GAME, FRANCHISE, CHARACTER, CONCEPT, OBJECT, LOCATION, PERSON, COMPANY
	};

	private ObjectType type;
	private String name;
	private String id;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
