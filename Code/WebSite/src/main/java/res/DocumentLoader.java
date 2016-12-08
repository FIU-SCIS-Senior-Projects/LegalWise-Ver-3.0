package res;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Part;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.watson.developer_cloud.document_conversion.v1.model.Answers;

import data.ConnectFactory;
import data.ConnectType;
import data.IConnect;
import qa.DocumentConverter;
import qa.RetrieveAndRankWrapper;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import wrapper.Document;
import wrapper.DocumentStatus;
import wrapper.SearchResult;
import wrapper.SolrDoc;
import wrapper.User;

public class DocumentLoader {

	private IConnect connectMdb;

	public DocumentLoader() {
		connectMdb = new ConnectFactory().getConnector(ConnectType.MongoDB);
	}

	public void addDocs(User user, List<wrapper.File> files) {
		for (wrapper.File file : files) {
			try {
				addDoc(user, file);
				System.out.println("Succesfull added it, file:" + file.getName());
			} catch (Exception e) {
				System.out.println("Error processing file: " + file.getName());
				e.printStackTrace();
			}
		}
	}

	public Document addDoc(User user, wrapper.File file) throws Exception {
		// create the file and write
		Path p = Paths.get(file.getName());
		Files.write(p, file.getBody());

		// create File instance to invoke conversion
		File f = new File(p.toUri());

		// Document configuration load
		JsonParser jsonParser = new JsonParser();
		String doctConfig = null;
		doctConfig = ResourceManager.getResourceAsText("doctConv","configdocument.json" );
		JsonObject jsonConfig = jsonParser.parse(doctConfig).getAsJsonObject();

		DocumentConverter d = new DocumentConverter();

		Answers answers = d.convertToAnswers(f, null, jsonConfig);

		Document document = new Document(null, new Date(), user, file, null, // plainText,
				DocumentStatus.PENDING_ACTIVATION);
		
		//save the document to get a fileID and documentID index in solr, IMPORTANT!!!
		connectMdb.addDocument(document);

		RetrieveAndRankWrapper ranker = new RetrieveAndRankWrapper();
		document.setStatus(
				ranker.addDocumentswithPatrs(document, answers) ? DocumentStatus.ACTIVE : DocumentStatus.INACTIVE);

		// update the document
		connectMdb.updateDocument(document);
		return document;
	}

	private boolean updateDoc(Document doc) {
		connectMdb.updateDocument(doc);
		return true;
	}

	private void getFile(int fileId) {
		wrapper.File file = connectMdb.getFile(fileId);
		System.out.println(file.getName());
	}

	private void getDocByOffset(int offset) {
		Document[] docs = connectMdb.getDocuments(offset);
		for (int i = 0; i < docs.length; i++) {
			System.out.println(docs[i].getAsJson());
		}
	}

	public List<wrapper.File> getPDFfilefromFolder(String path) {
		ArrayList<wrapper.File> files = new ArrayList<>();
		File folder = new File(path);
		String mimeType = "application/pdf";

		File[] listOfFiles = folder.listFiles();
		// skip the ones that are !pdf
		for (File file : listOfFiles) {
			String filename = file.getName();
			if (filename.contains(".pdf")) {
				byte[] bytes = loadFile(file);
				files.add(new wrapper.File(null, filename, file.length(), mimeType, bytes));
			}
		}
		return files;
	}
	
	public List<wrapper.File> getPDFfilefromSQL(String strfileIDs){
		ArrayList<wrapper.File> files = new ArrayList<>();
		IConnect connectSQL = new ConnectFactory().getConnector(ConnectType.SQL);	
		String[] strfileID = strfileIDs.split(",");
		
		for (String strid : strfileID) {
			int fileID = Integer.parseInt(strid.trim());
			wrapper.File file = connectSQL.getFile(fileID);
			files.add(file);
		}
		return files;
	}

	private byte[] loadFile(File file) {
		try {
			FileInputStream in = new FileInputStream(file);
			byte[] bs = new byte[in.available()];
			in.read(bs);
			in.close();
			return bs;
		} catch (Exception e) {
			System.out.println("Error LoadFile !!!");
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		String path = "D:/C Folder/Documents/School/Fiu/Fall 2016/CEN 5011/DoctConversion/LegalWise";
		String fileIDs = "7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28,"
				+ " 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50,"
				+ " 51, 52, 53, 54, 55, 56, 57, 58, 59, 60";
		DocumentLoader docL = new DocumentLoader();
		UserLoader userL = new UserLoader();
		User user = userL.getUser("mroge037@fiu.edu", "123");
		System.out.println(user);
		//List<wrapper.File> files  = docL.getPDFfilefromFolder(path);
		List<wrapper.File> files  = docL.getPDFfilefromSQL(fileIDs);

		docL.addDocs(user, files);
		docL.getDocByOffset(0);
		System.out.println("done!!");
	}

}
