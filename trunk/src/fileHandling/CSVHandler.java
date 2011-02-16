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
 * Currently just example code here.
 * 
 * @author cncplyr
 * @version 0.1
 * 
 */
public class CSVHandler {
	private String csvFolder = "output";
	
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
			writer = new CSVWriter(new FileWriter(csvFolder + File.separator + "boundingboxes.csv"), '\t');
			// feed in your array (or convert your data to an array)
			String[] entries = "first#second#third".split("#");
			writer.writeNext(entries);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



}
