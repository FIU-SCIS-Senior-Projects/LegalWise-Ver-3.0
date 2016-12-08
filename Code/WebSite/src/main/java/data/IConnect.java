package data;

import java.util.Map;
import java.util.Set;

import wrapper.Document;
import wrapper.File;
import wrapper.History;
import wrapper.User;

public interface IConnect {

	public User[] getUsers(int offset, String textFilter);
	public boolean createUser(User user);
	public boolean updateUser(User user);
	public User getUserByCredentials(String un, String pw);
	public User getUserByEmail(String email);
	public Map<String, String> getSettings();
	public boolean saveSettings(Map<String, String> settings);
	public boolean addDocument(Document document);
	public boolean updateDocument(Document document);
	public boolean addHistory(History history);
	public History[] getHistory(User user);
	public File getFile(int fileId);
	public Document[] getDocuments(int offset);
	public Document[] getDocumentsWithParts (int offset);
	public Map<Integer, Document> getDocuments(Set<Integer> docIds);
	public String getLastError();
	
}
