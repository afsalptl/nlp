import java.io.*;
import java.util.*;

public class CreateVocabulary
{
	private static BufferedWriter bw = null;
	
	public static void main(String[] args)
	{
		Map<String, Integer> unigramWordMap = new HashMap<String, Integer>();
		Map<String, Integer> bigramWordMap = new HashMap<String, Integer>();

		FileWriter fw = null;
    	try {
			fw = new FileWriter("vocabulary.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		bw = new BufferedWriter(fw);	

		Scanner inScanner=null;
		try {
			inScanner = new Scanner(new FileInputStream("../1RemoveStopWords/cleandata.txt"));
		} catch (FileNotFoundException e) {
			System.err.println("cleandata.txt : File Not Found");
		}

		Integer count;
		String line,bigram;
		String[] words;
		while(inScanner!=null && inScanner.hasNextLine())
		{
			line = inScanner.nextLine();
			words = line.split(" ");
			for (int i = 1; i < words.length; i++)
			{	
				count = unigramWordMap.get(words[i]);
				if(count == null)
					unigramWordMap.put(words[i], 1);
				else
					unigramWordMap.put(words[i], count+1);

				if(i < words.length - 1)
				{
					bigram = words[i] + " " + words[i+1];
					count = bigramWordMap.get(bigram);
					if(count == null)
						bigramWordMap.put(bigram, 1);
					else
						bigramWordMap.put(bigram, count+1);
				}
			}
		}

		for(Map.Entry<String, Integer> entry : unigramWordMap.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();

			if(value >= 2)
				printlnToFile(key);
		}

		for(Map.Entry<String, Integer> entry : bigramWordMap.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();

			if(value >= 3)
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