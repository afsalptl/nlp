import java.io.*;
import java.util.*;

public class NBCrossValidation
{	
	static ArrayList<String> vocabularyList,vocabularyList1,vocabularyList2;
	static Map<String, Double> wordMapPositive,wordMapNegative;
	static Integer typePositive,typeNegative;

	public static void main(String[] args)
	{
		if (args.length != 1) 
   		{
    		System.err.println("Invalid Format\nUse : java NBCrossValidation <filename>");
    		return;
    	}

		vocabularyList = createList("../5CreateVocabulary/vocabulary.txt");

    	CrossValidate(args[0]);

    	vocabularyList1 = new ArrayList<String>();
		vocabularyList2 = new ArrayList<String>();

		String word;
		for (int i = 0; i < vocabularyList.size(); i++) 
		{
			word = vocabularyList.get(i);
			if(word.contains(" "))
				vocabularyList2.add(word);
			else
				vocabularyList1.add(word);
		}
	}

	public static void CrossValidate(String filename)
	{
		Scanner inScanner=null;
		try {
			inScanner = new Scanner(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			System.err.println(filename + " : File Not Found");
		}

		ArrayList<String> validationSet;
		String line;
		char actual,observed;
		Integer tp,tn,fp,fn;
		Double accuracy,avgAccuracy=0.0;

		for(int i=0;i<10;i++)
		{
			tp=0;
			tn=0;
			fp=0;
			fn=0;
			validationSet = createValidationSet(filename,i*50);
			NaiveBayesTraining(filename,i*50);
			for (int j = 0; j < validationSet.size(); j++)
			{
				line = validationSet.get(j);
				actual = line.charAt(0);
				observed = pSentiment(line);
				if(actual == observed)
				{
					if(observed == '+')
						tp++;
					else
						tn++;
				}
				else
				{
					if(observed == '+')
						fp++;
					else
						fn++;
				}
			}
			accuracy = 1.0*(tp+tn)/(tp+tn+fp+fn);
			avgAccuracy += accuracy;
			System.out.println(i+1 + " " + accuracy);
		}
		avgAccuracy /= 10;
		System.out.println(avgAccuracy);
	}
	
	private static char pSentiment (String line)
	{
		Double pPositive, pNegative;
		pPositive = Math.log(1.0*typePositive/(typePositive+typeNegative));
		pNegative = Math.log(1.0*typeNegative/(typePositive+typeNegative));
		String[] words = line.split(" ");
		String bigram="";
		for (int i = 1; i < words.length; i++)
		{	
			if(i < words.length-1)
			{
				bigram = words[i] + " " + words[i+1];
				if (vocabularyList2.contains(bigram))
				{
					pPositive += wordMapPositive.get(bigram);
					pNegative += wordMapNegative.get(bigram);
				}
				else if(vocabularyList1.contains(words[i]))
				{
					pPositive += wordMapPositive.get(words[i]);
					pNegative += wordMapNegative.get(words[i]);
				}
			}
			else if(vocabularyList1.contains(words[i]))
			{
				pPositive += wordMapPositive.get(words[i]);
				pNegative += wordMapNegative.get(words[i]);
			}

		}
		if(pPositive >= pNegative)
			return '+';
		else
			return '-';
	}

	private static ArrayList<String> createValidationSet(String filename, Integer start)
	{
		Scanner scanner=null;
		ArrayList<String> tempList = new ArrayList<String>();
		String line;

		try {
			scanner = new Scanner(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			System.err.println(filename + " File Not Found");
		}

		Integer count = 0;

		while(scanner!=null && scanner.hasNextLine())
		{
			line = scanner.nextLine();
			if(count >= start && count < start + 50)
				tempList.add(line);
			count++;
		}
		return tempList;
	}

	public static void NaiveBayesTraining(String filename,Integer avoid)
	{

		vocabularyList1 = new ArrayList<String>();
		vocabularyList2 = new ArrayList<String>();

		wordMapPositive = new HashMap<String, Double>();
		wordMapNegative = new HashMap<String, Double>();

		String word;
		for (int i = 0; i < vocabularyList.size(); i++) 
		{
			word = vocabularyList.get(i);
			wordMapPositive.put(word,1.0);
			wordMapNegative.put(word,1.0);
			if(word.contains(" "))
				vocabularyList2.add(word);
			else
				vocabularyList1.add(word);
		}

		Scanner inScanner=null;
		try {
			inScanner = new Scanner(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			System.err.println(filename + " : File Not Found");
		}

		char type;
		String line,x="";
		String[] words;
		Integer countPositive=vocabularyList.size(),countNegative=vocabularyList.size();
		Integer count=0;
		typePositive=0;
		typeNegative=0;
		while(inScanner!=null && inScanner.hasNextLine())
		{
			line = inScanner.nextLine();
			if(count >= avoid && count < avoid+50 )
			{
				count++;
				continue;
			}
			type = line.charAt(0);
			if(type=='+')
				typePositive++;
			else
				typeNegative++;

			words = line.split(" ");
			for (int i = 1; i < words.length; i++)
			{	

				if(i <  words.length - 1)
				{
					x= words[i] + " " + words[i+1];
					if(vocabularyList2.contains(x))
					{
						if (type == '+')
						{
							wordMapPositive.put(x, wordMapPositive.get(x)+1);
							++countPositive;
						}
						else
						{
							wordMapNegative.put(x, wordMapNegative.get(x)+1);
							++countNegative;
						}
					}
					else if(vocabularyList1.contains(words[i]))
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
				else if(vocabularyList1.contains(words[i]))
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
			count++;
		}

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