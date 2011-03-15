package fileHandling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * Uses openCSV 2.2 - http://opencsv.sourceforge.net/
 * 
 * Provides methods to interact simply with csv files, wrapping around methods
 * from the openCSV 2.2 library.
 * 
 * @author cncplyr
 * @version 0.4
 * 
 */
public class CSVHandler {
	CSVWriter writer;
	private String csvFolder = "output";
	private String fileName = "boundingBoxes";

	/**
	 * Reads the currently selected file and returns it.
	 * 
	 * @return The requested file in the form of a List of arrays of strings.
	 *         Each array represents a single row.
	 */
	public List<String[]> readCSV() {
		CSVReader reader;
		List<String[]> myEntries = new ArrayList<String[]>();
		try {
			reader = new CSVReader(new FileReader(getCSVFolder() + File.separator + getFileName() + ".csv"));
			myEntries = reader.readAll();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return myEntries;
	}

	/**
	 * Opens a <code>CSVWriter</code> to write to, using the current output
	 * folder and file name.
	 * 
	 * This method must be called before writeCSVLine() can be called.
	 * 
	 */
	public void openCSVStream() {
		try {
			writer = new CSVWriter(new FileWriter(getCSVFolder() + File.separator + getFileName() + ".csv"), ',');
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Closes the current writer.
	 */
	public void closeCSVStream() {
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends a single row to be written to the csv file.
	 * 
	 * @param entries
	 *            The array of integers to write to file.
	 */
	public void writeCSVLine(int[] entries) {
		int size = entries.length;
		String[] convertedEntries = new String[size];
		for (int i = 0; i < size; i++) {
			convertedEntries[i] = Integer.toString(entries[i]);
		}
		writer.writeNext(convertedEntries);
	}

	public String getCSVFolder() {
		return csvFolder;
	}

	public String getFileName() {
		return fileName;
	}

	public void setCSVFolder(String csvFolder) {
		this.csvFolder = csvFolder;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
