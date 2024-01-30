Program 1 : CleanDataSet.java

Description: This program receives as arguments an input directory and an output directory. The Datasets are stored and accessed from the root of the project
where each Dataset directory contains folders which contain a series of text files.
The program then cleans the text files from the input directory and writes the cleaned files to the output directory.
File cleaning will contain the process: The input files are TXT files that contain words separated by separators and by delimiters.
Words are defined as any sequence of alphanumerical characters (0-9a-zA-Z).
Delimiters are defined as the space, tab and new line characters (’\’, ’\t’, ’\n’, ’\r\n’, ’\r’)
and any other character is considered a separator. any ’\r’ character has to be eliminated;
any repeating sequence of delimiters must be replaced with the last delimiter in the
sequence. For example, if your program encounters ”\r\n\r\n”, it must replace it with
”\n”, because ’\n’ was the last character in the delimiter sequence;
any separator must be eliminated. For example, if your program encounters ”document01.txt”, it must replace it with ”document01txt”, because ’-’ and ’.’ are separator
characters (they are not word characters or delimiter characters);

Example run: 
java -cp target/app-java-1.0-SNAPSHOT.jar csc435.app.CleanDataset Dataset1 ~/pa1-jjalbr1/app-java-CleanedDataset1

Program 2: CountWords.java

Description: This program receives as arguments an input directory and an output directory, Where the input is the cleaned output from program one. The Datasets are stored and accessed from the root of the project
where each Dataset directory contains folders which contain a series of text files. This program simply count the words in the input text files. It outputs the words to the desired output text file location as specified
in the arguments. Each word will have its own seperaste line containing the word itself along with the number of occurences of the word, seperated by a space.

Example run: 
java -cp target/app-java-1.0-SNAPSHOT.jar csc435.app.CountWords CleanedDataset1 ~/pa1-jjalbr1/app-java-CountedDataset1

Program 3: SortWords.java

Description: This program receives as arguments an input directory and an output directory. The Datasets are stored and accessed from the root of the project
where each Dataset directory contains folders which contain a series of text files. The program uses the output from the previous program as input (CountedDataset1).
This program simply reads in the dataset and sorts each line by the number of occurences of each word. It writes this sorted output to the desired output file specified in the arguments.

Example run: 
java -cp target/app-java-1.0-SNAPSHOT.jar csc435.app.SortWords CountedDataset1 ~/pa1-jjalbr1/app-java-SortedDataset1

