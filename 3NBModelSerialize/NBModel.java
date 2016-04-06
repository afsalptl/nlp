import java.io.*;
import java.util.*;

public class NBModel implements java.io.Serializable
{
	public ArrayList<String> vocabularyList;
	public Double pPositive;
	public Double pNegative;
	public Map<String, Double> wordMapPositive = new HashMap<String, Double>();
	public Map<String, Double> wordMapNegative = new HashMap<String, Double>();
}