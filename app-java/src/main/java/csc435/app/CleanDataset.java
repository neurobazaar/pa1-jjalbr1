package csc435.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class CleanDataset{

	// Recording time and amount of data read
	public static long startTime = System.nanoTime();
	public static long bytes = 0;

	public static void main(String[] args) {
		// Check for two arguments
		if (args.length != 2) {
			System.out.println(
					"You must specify two arguments which will be two paths. The first being the path of the input dataset. The second being the path of the output.");
			return;
		}

		// Input directory path
		File inpFolder = new File(args[0]);

		// Output directory path
		File outFolder = new File(args[1]);

		// In the directions it seems that we are only taking in full data sets (the
		// folders). Not individual txt files. Therefore I am making sure there is a
		// folder to be consumed
		if (!inpFolder.isDirectory()) {
			System.out.println("First argument should be a directory.");
			return;
		}

		// Make call to process files
		processDataSet(inpFolder, outFolder);

		// Calculate data and rate
		long time = System.nanoTime();
		double seconds = (time - startTime) / 1.0e9;
		double rate = (bytes / 1024.0 / 1024.0) / seconds;

		// Output data
		System.out.println("Time: " + seconds + " seconds");
		System.out.println("Read: " + bytes + " Bytes");
		System.out.println("Rate of: " + rate + " MiB/second");
	}

	// Method takes directory and cleans files
	private static void processDataSet(File inFolder, File outFolder) {

		// If output folder does not already exists, create it
		if (!outFolder.exists()) {
			outFolder.mkdir();
		}

		// Will store the folders containing files to be cleaned
		Queue<File> queue = new LinkedList<>();

		// Add start folder to queue
		queue.add(inFolder);

		// Loop until all folders have been scanned
		while (!queue.isEmpty()) {

			// Pop queue
			File currFolder = queue.poll();
			File outCurrFolder = new File(outFolder, currFolder.getName());

			// Create output folder if it does not exist
			if (!outCurrFolder.exists()) {
				outCurrFolder.mkdir();
			}

			// Store all files/folders in array
			File[] files = currFolder.listFiles();
			if (files != null) {

				// Scan files/folder
				for (int i = 0; i < files.length; i++) {
					File file = files[i];

					// Distinguish between file and folder. add folders to queue to be searched.
					// Files get cleaned
					if (file.isDirectory()) {
						queue.add(file);
					} else if (file.getName().endsWith(".txt")) {
						// Clean txt files
						File cleanedFile = new File(outCurrFolder, file.getName());
						cleanFile(file, cleanedFile);
					}
				}
			}
		}
	}

	// Clean file based on requirements
	private static void cleanFile(File inFile, File outFile) {

		// Get folder. It will exist, but if not create it
		File outDir = outFile.getParentFile();
		if (!outDir.exists()) {
			outDir.mkdir();
		}

		try {

			// Create buffers and writers
			BufferedReader bufferedReader = new BufferedReader(new FileReader(inFile));
			FileWriter fileWriter = new FileWriter(outFile);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			// Keep track if previous delimiter
			boolean prevDelim = false;
			int character;

			// Loop while more characters
			while ((character = bufferedReader.read()) != -1) {

				// increase byte counter
				bytes++;

				char aChar = (char) character;

				// Skip/throw away /r
				if (aChar == '\r') {
					continue;
				}

				// Check if the character is a delimiter. If it is we want to use it. write to
				// the file
				if (aChar == ' ' || aChar == '\n' || aChar == '\t') {
					if (!prevDelim) {
						bufferedWriter.write(aChar);
						prevDelim = true;
					}
				}

				// Write valid words
				else if ((aChar >= 'a' && aChar <= 'z') || (aChar >= 'A' && aChar <= 'Z')
						|| (aChar >= '0' && aChar <= '9')) {
					bufferedWriter.write(aChar);
					prevDelim = false;
				}
			}

			// Close
			bufferedWriter.close();
			bufferedReader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
