package data.mongo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import data.IConnect;
import wrapper.Document;
import wrapper.File;
import wrapper.History;
import wrapper.User;
import wrapper.mongodb.Sequence;
import wrapper.mongodb.Settings;

public class MongoDAO implements IConnect {

	private Datastore connect;

	public MongoDAO() {
		// TODO Auto-generated constructor stub
		connect = MongoConnector.getConnector().getDataStore();
	}

	@Override
	public User[] getUsers(int offset, String textFilter) {
		List<User> userQuery = connect.find(User.class).field("firstName").contains(textFilter).asList();

		Query<User> query = connect.find(User.class);
		query.or(query.criteria("firstName").containsIgnoreCase(textFilter), query.criteria("lastName").containsIgnoreCase(textFilter),
				query.criteria("companyName").containsIgnoreCase(textFilter), query.criteria("email").containsIgnoreCase(textFilter));
		query.order("firstName");
		query.offset(offset);
		query.limit(50);

		List<User> userList = query.asList();

		if (userList.size() > 0) {
			return userList.toArray(new User[userList.size()]);
		}

		return null;
	}

	@Override
	public boolean createUser(User user) {
		int seqUser = (int) getNext("UserID");
		user.setUserId(seqUser);
		connect.save(user);
		return true;
	}

	@Override
	public boolean updateUser(User user) {
		connect.save(user);
		return true;
	}

	@Override
	public User getUserByCredentials(String un, String pw) {
		if (un != null && pw != null) {
			List<User> userQuery = connect.createQuery(User.class).filter("email ==", un).filter("password", pw)
					.asList();
			if (userQuery.size() == 1) {
				return userQuery.get(0);
			}
		}
		return null;
	}
	
	public User getUserByEmail(String email) {
		if (email != null) {
			List<User> userQuery = connect.createQuery(User.class).filter("email ==", email).asList();
			if (userQuery.size() == 1) {
				return userQuery.get(0);
			}
		}
		return null;
	}

	@Override
	public boolean addHistory(History history) {
		connect.save(history);
		return true;
	}

	@Override
	public History[] getHistory(User user) {
		List<History> historyQuery = connect.createQuery(History.class).field("user").equal(connect.getKey(user))
				.retrievedFields(true).asList();
		return historyQuery.toArray(new History[historyQuery.size()]);
	}

	@Override
	public Map<String, String> getSettings() {
		Map<String, String> map = new HashMap<>();
		Query<Settings> query = connect.createQuery(Settings.class);
		List<Settings> sets = query.asList();

		for (Settings settings : sets) {
			map.put(settings.getName(), settings.getValue());
		}
		return map;
	}

	@Override
	public boolean saveSettings(Map<String, String> settings) {
		// TODO Auto-generated method stub
		for (Map.Entry<String, String> entry : settings.entrySet()) {
			Settings setting = new Settings();
			setting.setName(entry.getKey());
			setting.setValue(entry.getValue());
			connect.save(setting);
		}
		return true;
	}

	@Override
	public boolean addDocument(Document document) {
		int seqFile = (int) getNext("fileID");
		int seqDoc = (int) getNext("docID");
		document.getFile().setFileId(seqFile);
		document.setDocumentId(seqDoc);
		connect.save(document);
		return true;
	}

	@Override
	public boolean updateDocument(Document document) {
		connect.save(document);
		return true;
	}

	@Override
	public File getFile(int fileId) {
		File file = new File(fileId, "", new Long(0), "", new byte[0]);
		List<Document> docQuery = connect.createQuery(Document.class).field("file._id").equal(fileId)
				.retrievedFields(true, "file").asList();
		if (docQuery.size() > 0) {
			return docQuery.get(0).getFile();
		}
		return null;
	}

	@Override
	public Document[] getDocuments(int offset) {
		List<Document> docQuery = connect.createQuery(Document.class).offset(offset)
				.retrievedFields(true, "id", "documentId", "uploadedOn", "uploadedBy", "file", "status").asList();
		return docQuery.toArray(new Document[docQuery.size()]);
	}
	
	//TODO create a method to retrive just solr documents and not file.

	@Override
	public Map<Integer, Document> getDocuments(Set<Integer> docIds) {
		Map<Integer, Document> map = new HashMap<>();
		if (!docIds.isEmpty()) {
			List<Document> docQuery = connect.createQuery(Document.class).field("documentId").in(docIds).asList();
			for (Iterator iterator = docQuery.iterator(); iterator.hasNext();) {
				Document document = (Document) iterator.next();
				map.put(document.getDocumentId(), document);
			}
			return map;
		}
		return null;
	}

	@Override
	public String getLastError() {
		// TODO Auto-generated method stub
		return null;
	}

	private long getNext(String collection) {
		Sequence seq = connect.findAndModify(connect.find(Sequence.class, "key = ", collection), // query
				connect.createUpdateOperations(Sequence.class).inc("seqNumber") // update
		);

		// create a sequence record for your collection if not found
		if (seq == null) {
			seq = new Sequence(collection, 1);
			connect.save(seq);
		}

		return seq.getSeqNumber();
	}

	@Override
	public Document[] getDocumentsWithParts(int offset) {
		List<Document> docQuery = connect.createQuery(Document.class).offset(offset)
				.retrievedFields(true, "id", "documentId", "status", "parts").asList();
		return docQuery.toArray(new Document[docQuery.size()]);
	}

}
