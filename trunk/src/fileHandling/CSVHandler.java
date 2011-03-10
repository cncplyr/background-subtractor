package fileHandling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * Uses openCSV 2.2 - http://opencsv.sourceforge.net/
 * 
 * 
 * @author cncplyr
 * @version 0.2
 * 
 */
public class CSVHandler {
	private String csvFolder = "output";
	CSVWriter writer;

	public void openCSVStream() {
		try {
			writer = new CSVWriter(new FileWriter(csvFolder + File.separator + "boundingboxes.csv"), ',');
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeCSVStream() {
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeCSVLine(int[] entries) {
		int size = entries.length;
		String[] convertedEntries = new String[size];
		for (int i = 0; i < size; i++) {
			convertedEntries[i] = Integer.toString(entries[i]);
		}
		writer.writeNext(convertedEntries);
	}


	public void readCSV() {
		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader("yourfile.csv"));
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				// nextLine[] is an array of values from the line
				System.out.println(nextLine[0] + nextLine[1] + "etc...");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void writeCSV(List<int[]> input) {
		CSVWriter writer;
		try {
			// creates a tab-separated file
			writer = new CSVWriter(new FileWriter(csvFolder + File.separator + "boundingboxes.csv"), ',');
			// feed in your array (or convert your data to an array)
			String[] entries = "first#second#third".split("#");
			writer.writeNext(entries);
			String[] newEntries = new String[] { "512", "240" };
			writer.writeNext(newEntries);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
