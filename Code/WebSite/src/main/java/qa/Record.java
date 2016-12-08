package qa;

public class Record {

	private String query;
	private String ids;
	private int value;
	
	public Record(String query, String ids, int value) {
		super();
		this.query = query;
		this.ids = ids;
		this.value = value;
	}
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	
}
