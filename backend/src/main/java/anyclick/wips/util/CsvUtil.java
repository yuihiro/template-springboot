package anyclick.wips.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.opencsv.CSVWriter;

public class CsvUtil {

	static public void writeCsv(List<String[]> data, String file_name) {
		try {
			CSVWriter cw = new CSVWriter(new FileWriter(file_name), ',', '"');
			Iterator<String[]> it = data.iterator();
			try {
				while (it.hasNext()) {
					String[] s = (String[]) it.next();
					cw.writeNext(s);
				}
			} finally {
				cw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
