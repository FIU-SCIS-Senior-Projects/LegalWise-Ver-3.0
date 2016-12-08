package wrapper.mongodb;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

@Entity("Sequence")
public class Sequence {

	@Id
	private String key;
	@Property
	private long seqNumber;
	
	public Sequence (){
		
	}

	public Sequence(String key, long number) {
		this.key = key;
		this.seqNumber = number;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public long getSeqNumber() {
		return seqNumber;
	}

	public void setSeqNumber(long seqNumber) {
		this.seqNumber = seqNumber;
	}
	
	

}
