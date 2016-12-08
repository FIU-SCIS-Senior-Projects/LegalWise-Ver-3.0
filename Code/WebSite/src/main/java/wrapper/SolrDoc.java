package wrapper;

import java.util.HashMap;
import java.util.Map;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Property;


/**
 * @author Michel
 *
 */

@Embedded("SolrDoc")

public class SolrDoc {
	
	private static final int  DEFAULTBOOST = 1;

	@Id
	private String id;
	@Property
	private int fileID;
	@Property
	private String fileName;
	@Property
	private String title;
	@Property
	private String sub_title;
	@Property
	private String body;
	@Property
	private int boost;
	
	
	private SolrDoc (){
				
	}
	
	public SolrDoc (String id, int fileID, String fileName, String title, String sub_title, String body, int boost){
		this.id = id;
		this.fileID = fileID;
		this.fileName = fileName;
		this.title = title;
		this.sub_title = sub_title;
		this.body = body;
		this.boost = boost;		
	}
	
	public SolrDoc (String id, int fileID, String fileName, String title, String sub_title, String body){
		this(id, fileID, fileName, title, sub_title, body, DEFAULTBOOST);
	}
	
	public SolrDoc (String id, int fileID, String fileName, String title, String body){
		this(id, fileID, fileName, title, "", body, DEFAULTBOOST);
	}
	
	public SolrDoc (String id, String title, String body){
		this(id, 0, "", title, "", body, DEFAULTBOOST);
	}

	public SolrInputField getId() {
		SolrInputField id =  new SolrInputField("id");
		id.setValue(this.id, DEFAULTBOOST);
		return id;
	}

	public SolrInputField getFileID() {
		SolrInputField fileID =  new SolrInputField("fileID");
		fileID.setValue(this.fileID, DEFAULTBOOST);
		return fileID;
	}

	public SolrInputField getFileName() {
		SolrInputField fileName = new SolrInputField("fileName");
		fileName.setValue(this.fileName, DEFAULTBOOST);
		return fileName;
	}

	public SolrInputField getTitle() {
		SolrInputField title = new SolrInputField("title");
		title.setValue(this.title, DEFAULTBOOST);
		return title;
	}

	public SolrInputField getSub_title() {
		SolrInputField sub_title = new SolrInputField("sub_title");
		sub_title.setValue(this.sub_title, DEFAULTBOOST);
		return sub_title;
	}

	public SolrInputField getBody() {
		SolrInputField body = new SolrInputField("body");
		body.setValue(this.body, DEFAULTBOOST);
		return body;
	}
	
	public Map<String, SolrInputField> getMapDoc (){
		Map<String, SolrInputField> docMap = new HashMap<>();
		
		docMap.put(getId().getName(), getId());
		docMap.put(getFileID().getName(), getFileID());
		docMap.put(getFileName().getName(), getFileName());
		docMap.put(getTitle().getName(), getTitle());
		docMap.put(getSub_title().getName(), getSub_title());
		docMap.put(getBody().getName(), getBody());
	
		return docMap;
	}
	
	public SolrInputDocument getDoc (){
		
		SolrInputDocument solrDoc =new SolrInputDocument();
		solrDoc.putAll(getMapDoc());
		
		return solrDoc;
	}
	
}
