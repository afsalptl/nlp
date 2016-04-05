import java.io.*;
import java.util.*;
import java.lang.*;

public class Rmstopwords
{
	private static BufferedWriter bw = null;

	public static void main(String[] args)
	{
		ArrayList<String> stopWords = new ArrayList<String>();
		stopWords = createList("stopwords.txt");

		FileWriter fw = null;
    	try {
			fw = new FileWriter("cleandata.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		bw = new BufferedWriter(fw);

		Scanner inScanner=null;
		try {
			inScanner = new Scanner(new FileInputStream("data.txt"));
		} catch (FileNotFoundException e) {
			System.err.println("data.txt : File Not Found");
		}

		String line,lineOut;
		char type;
		String[] words;
		while(inScanner!=null && inScanner.hasNextLine())
		{
			line = inScanner.nextLine();
			type = line.charAt(0);
			line = formatLine(line);
			words = line.split(" ");
			lineOut = "";
			for (int i = 0; i < words.length; i++) 
			{
		        if(words[i].length() > 1 && !stopWords.contains(words[i]))
		        {
		        	lineOut = lineOut + " " + words[i] ;
		        }
		    }
			printlnToFile(type + lineOut);
		}

		try {
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static ArrayList<String> createList(String filename)
	{
		Scanner scanner=null;
		ArrayList<String> tempList = new ArrayList<String>();
		String line;

		try {
			scanner = new Scanner(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			System.err.println(filename + " File Not Found");
		}

		while(scanner!=null && scanner.hasNextLine())
		{
			line = scanner.nextLine();
			tempList.add(line);
		}
		return tempList;
	}

	private static void printlnToFile(String line)
	{
		try {
			bw.write(line + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String formatLine(String in)
	{
		String temp;
		return in.replaceAll("\'","").replaceAll("[^A-Za-z]"," ").trim().replaceAll("(\\s)+"," ").toLowerCase();
	}
}