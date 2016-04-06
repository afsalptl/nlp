import java.io.*;
import java.util.*;

public class NBModelTesting
{
	private static NBModel newModel = null;

	public static void main(String[] args)
	{	
		if (args.length != 1) 
   		{
    		System.err.println("Invalid Format\nUse : java NBModelTesting <filename>");
    		return;
    	}

		try
		{
			FileInputStream fileIn = new FileInputStream("NBModel.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
		 	newModel = (NBModel) in.readObject();
			in.close();
			fileIn.close();
		}
		catch(IOException i)
		{
			i.printStackTrace();
		 	return;
		}
		catch(ClassNotFoundException c)
		{
		 	System.out.println("NBModel class not found");
		 	c.printStackTrace();
		 	return;
		}

		Test(args[0]);
	}

	public static void Test(String filename)
	{
		Scanner inScanner=null;
		try {
			inScanner = new Scanner(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			System.err.println(filename + " : File Not Found");
		}

		String line;
		char actual,observed;
		Integer tp=0,tn=0,fp=0,fn=0,count=0;
		Double accuracy;

		while(inScanner!=null && inScanner.hasNextLine())
		{
			line = inScanner.nextLine();
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
			System.out.println(observed + " [" + line + "]");
		}
		accuracy = 1.0*(tp+tn)/(tp+tn+fp+fn);
		System.out.println("TP-"+tp+" TN-"+tn+" FN-"+fn+" FP-"+fp);
		System.out.println("Accuracy is " + accuracy);
	}

	private static char pSentiment (String line)
	{
		Double pPositive, pNegative;
		pPositive = newModel.pPositive;
		pNegative = newModel.pNegative;
		String[] words = line.split(" ");
		for (int i = 1; i < words.length; i++)
		{	
			if (newModel.vocabularyList.contains(words[i]))
			{
				pPositive += newModel.wordMapPositive.get(words[i]);
				pNegative += newModel.wordMapNegative.get(words[i]);
			}
		}
		if(pPositive >= pNegative)
			return '+';
		else
			return '-';
	}
}