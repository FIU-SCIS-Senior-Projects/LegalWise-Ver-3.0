package res;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import qa.Record;

public class CSVFile {

	private StringBuffer query;
	// Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";

	public CSVFile() {
		query = new StringBuffer();
	}

	public void createQuery(Record record) {
		query.append(record.getQuery());
		query.append(COMMA_DELIMITER);
		query.append(record.getIds());
		query.append(COMMA_DELIMITER);
		query.append(Integer.toString(record.getValue()));
		query.append(NEW_LINE_SEPARATOR);
	}

	public boolean writeCSV(String path) {

		PrintWriter filew = null;
		try {
			File file = new File(path);
			filew = new PrintWriter(path, "UTF-8");

			if (file.exists()) {
				file.delete();
			}

			filew.append(query.toString());

		} catch (IOException ex) {
			System.out.println("ERROR appending to the file!!!");
			ex.printStackTrace();
			return false;
		} finally {
			filew.flush();
			filew.close();
		}
		return true;
	}
}
