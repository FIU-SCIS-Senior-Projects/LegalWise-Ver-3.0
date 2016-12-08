package qa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sun.tools.javac.code.Attribute.Array;

import data.ConnectFactory;
import data.ConnectType;
import data.IConnect;
import it.unimi.dsi.fastutil.Arrays;
import res.CSVFile;
import wrapper.Document;
import wrapper.SolrDoc;

public class TrainSystem {

	public boolean createCSV(String path, List<Record> records) {
		// Skip duplicates
		Set<Record> entry = new HashSet<Record>();
		CSVFile csv = new CSVFile();
		
		for (Record record : records) {
			if (!entry.contains(record)) {
				entry.add(record);
				csv.createQuery(record);
			}
		}
		boolean result = csv.writeCSV(path);
		return result;
	}


	public List<Record> questionByTitle(Document[] docs) {
		String whatis = "\"What is ";
		String questionMark = "?\"";
		ArrayList<Record> result = new ArrayList<Record>();
		int maxValue = 5;
		
		for (Document doc : docs) {
			for (SolrDoc solrDoc : doc.getParts()) {
				String question = whatis + 
								solrDoc.getTitle().getValue().toString().replaceAll("\"", "") + " "+ 
								solrDoc.getSub_title().getValue().toString().replaceAll("\"", "") + 
								questionMark;
								
				result.add(new Record(question, (String)solrDoc.getId().getValue(), maxValue));
			}
		}
		return result;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String path = "D:/C Folder/Documents/School/Fiu/Fall 2016/CEN 5011/R&R/test.csv";
		IConnect connect = new ConnectFactory().getConnector(ConnectType.MongoDB);
		Document[] docs = connect.getDocumentsWithParts(0);
		TrainSystem train = new TrainSystem();
		
		List<Record> records = train.questionByTitle(docs);
		train.createCSV(path, records);
		System.out.println("done!!");
	}
}
