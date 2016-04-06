import java.io.*;
import java.util.*;

public class NBModelTraining
{
	static ArrayList<String> vocabularyList;

	public static void main(String[] args)
	{
		if (args.length != 1) 
   		{
    		System.err.println("Invalid Format\nUse : java NBModelSerialize <filename>");
    		return;
    	}

		vocabularyList = createList("../2CreateVocabulary/vocabulary.txt");

    	NBSerialize(args[0]);
	}

	public static void NBSerialize(String filename)
	{
		Map<String, Double> wordMapPositive = new HashMap<String, Double>();
		Map<String, Double> wordMapNegative = new HashMap<String, Double>();
		Double pPositive,pNegative;

		String word;
		for (int i = 0; i < vocabularyList.size(); i++) 
		{
			word = vocabularyList.get(i);
			wordMapPositive.put(word,1.0);
			wordMapNegative.put(word,1.0);
		}

		Scanner inScanner=null;
		try {
			inScanner = new Scanner(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			System.err.println(filename + " : File Not Found");
		}

		char type;
		String line;
		String[] words;
		Integer countPositive=vocabularyList.size(),countNegative=vocabularyList.size();
		Integer typeNegative=0,typePositive=0;
		while(inScanner!=null && inScanner.hasNextLine())
		{
			line = inScanner.nextLine();
			type = line.charAt(0);
			if(type=='+')
				typePositive++;
			else
				typeNegative++;

			words = line.split(" ");
			for (int i = 1; i < words.length; i++)
			{	
				if(vocabularyList.contains(words[i]))
				{
					if (type == '+')
					{
						wordMapPositive.put(words[i], wordMapPositive.get(words[i])+1);
						++countPositive;
					}
					else
					{
						wordMapNegative.put(words[i], wordMapNegative.get(words[i])+1);
						++countNegative;
					}
				}
			}
		}

		pPositive = Math.log(1.0*typePositive/(typePositive+typeNegative));
		pNegative = Math.log(1.0*typeNegative/(typePositive+typeNegative));

		for(Map.Entry<String, Double> entry : wordMapPositive.entrySet()) 
		{
			String key = entry.getKey();
			Double value = entry.getValue();
			wordMapPositive.put(key, Math.log(value/countPositive));
		}

		for(Map.Entry<String, Double> entry : wordMapNegative.entrySet()) 
		{
			String key = entry.getKey();
			Double value = entry.getValue();
			wordMapNegative.put(key, Math.log(value/countNegative));
		}

		NBModel newModel = new NBModel();
		newModel.pPositive = pPositive;
		newModel.pNegative = pNegative;
		newModel.wordMapPositive = wordMapPositive;
		newModel.wordMapNegative = wordMapNegative;
		newModel.vocabularyList = vocabularyList;

		try
		{
			FileOutputStream fileOut = new FileOutputStream("NBModel.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(newModel);
			out.close();
			fileOut.close();
			System.out.println("Serialized Naive Bayes Model is saved in NBModel.ser");
		}
		catch(IOException e)
		{
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

}