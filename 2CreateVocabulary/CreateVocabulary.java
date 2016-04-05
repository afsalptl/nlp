import java.io.*;
import java.util.*;

public class CreateVocabulary
{
	private static BufferedWriter bw = null;
	
	public static void main(String[] args)
	{
		Map<String, Integer> wordMap = new HashMap<String, Integer>();

		FileWriter fw = null;
    	try {
			fw = new FileWriter("vocabulary.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		bw = new BufferedWriter(fw);	

		Scanner inScanner=null;
		try {
			inScanner = new Scanner(new FileInputStream("../RemoveStopWords/cleandata.txt"));
		} catch (FileNotFoundException e) {
			System.err.println("cleandata.txt : File Not Found");
		}

		Integer count;
		String line;
		String[] words;
		while(inScanner!=null && inScanner.hasNextLine())
		{
			line = inScanner.nextLine();
			words = line.split(" ");
			for (int i = 1; i < words.length; i++)
			{	
				count = wordMap.get(words[i]);
				if(count == null)
					wordMap.put(words[i], 1);
				else
					wordMap.put(words[i], count+1);
			}
		}

		for(Map.Entry<String, Integer> entry : wordMap.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();

			if(value >= 2)
				printlnToFile(key);
		}

		try {
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void printlnToFile(String line)
	{
		try {
			bw.write(line + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}