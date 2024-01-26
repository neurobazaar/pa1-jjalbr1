package csc435.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class SortWords{
	// Recording time and words read
	public static long startTime = System.nanoTime();
	public static int words = 0;

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

		// Make call to process files
		processDataset(inpFolder, outFolder);

		// Calculate execution time and words
		long time = System.nanoTime();
		double seconds = (time - startTime) / 1.0e9;

		// Output data
		System.out.println("Time: " + seconds + " seconds");
		System.out.println("Read: " + words);
		System.out.println(words / seconds + " Words/Second");
	}

	// Method takes directory and sorts words in files
	private static void processDataset(File inFolder, File outFolder) {

		// If output folder does not already exists, create it
		if (!outFolder.exists()) {
			outFolder.mkdir();
		}

		// Will store the folders containing files to be sorted
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
						sortWords(file, outputFile);
					}
				}
			}
		}
	}

	// Sort words in file
	private static void sortWords(File inputFile, File outputFile) {

		// HashMap with key and value to store frequencies of words
		Map<String, Integer> wordCounter = new HashMap<>();

		try {

			// Create buffers and writers
			BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
			String line;

			// loop while there are more lines in the text document
			while ((line = bufferedReader.readLine()) != null) {

				// increase words processed counter
				words++;

				// Each line has 2 parts. Unique word and frequency
				String[] parts = line.split(" ");
				String word = parts[0];
				int count = Integer.parseInt(parts[1]);
				wordCounter.put(word, count);
			}

			// Sort
			List<Map.Entry<String, Integer>> sortedWords = new ArrayList<>(wordCounter.entrySet());
			sortedWords.sort(Map.Entry.<String, Integer>comparingByValue().reversed());

			// Write to file
			for (int i = 0; i < sortedWords.size(); i++) {
				Map.Entry<String, Integer> entry = sortedWords.get(i);
				bufferedWriter.write(entry.getKey() + " " + entry.getValue());
				bufferedWriter.newLine();
			}

			// Close
			bufferedWriter.close();
			bufferedReader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
