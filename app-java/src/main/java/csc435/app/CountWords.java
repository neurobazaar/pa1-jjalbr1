package csc435.app;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.io.*;

public class CountWords{
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
			System.out.println("First arg should be a directory.");
			return;
		}

		// Process files
		processDataset(inpFolder, outFolder);

		// Calculate data and rate
		long time = System.nanoTime();
		double seconds = (time - startTime) / 1.0e9;
		double rate = (bytes / 1024.0 / 1024.0) / seconds;

		// Output data
		System.out.println("Time: " + seconds + " seconds");
		System.out.println("Read: " + bytes + " Bytes");
		System.out.println("Rate of: " + rate + " MiB/second");
	}

	// Method takes directory and counts words in files
	private static void processDataset(File inFolder, File outFolder) {

		// If output folder does not already exists, create it
		if (!outFolder.exists()) {
			outFolder.mkdir();
		}

		// Will store the folders containing files to be counted
		Queue<File> queue = new LinkedList<>();

		// Add folder to queue
		queue.add(inFolder);

		// Loop until all folders have been scanned
		while (!queue.isEmpty()) {

			// Pop queue
			File currFolder = queue.poll();
			File outCurrFolder = new File(outFolder, currFolder.getName());

			// Create output folder if it does not exist
			if (!outCurrFolder.exists()) {
				outCurrFolder.mkdirs();
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
					} else if (file.isFile() && file.getName().endsWith(".txt")) {
						// Clean txt files
						File outputFile = new File(outCurrFolder, file.getName());
						countWords(file, outputFile);
					}
				}
			}
		}
	}

	// Count file based on requirements
	private static void countWords(File inputFile, File outputFile) {

		// HashMap with key and value to store frequencies of words
		Map<String, Integer> wordCounter = new HashMap<>();

		try {

			// Create buffers and writers
			BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
			String txtLine;

			// loop while there are more lines in the text document
			while ((txtLine = bufferedReader.readLine()) != null) {

				// Split words
				String[] words = txtLine.split("\\s+");

				// Loop through words
				for (int i = 0; i < words.length; i++) {
					bytes = bytes + 4;
					String word = words[i];
					if (!word.isEmpty()) {
						// getOrDefault method used to check if word is existing in HashMap. If it does
						// not exist we give default 0 and enter it. Otherwise increment by 1
						wordCounter.put(word, wordCounter.getOrDefault(word, 0) + 1);
					}
				}
			}

			// Write to output file
			// Parse HashMap, write words to file
			Object[] words = wordCounter.keySet().toArray();
			for (int i = 0; i < words.length; i++) {
				String key = (String) words[i];
				bufferedWriter.write(key + " " + wordCounter.get(key));
				bufferedWriter.newLine();
			}

			// Close
			bufferedReader.close();
			bufferedWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
