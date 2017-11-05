import java.util.Comparator;
import java.lang.Math;

public class ComparatorString implements Comparator<Integer> 
{
	private String word;
	public ComparatorString(String word) 
	{
		super();
		this.word = word;
	}
	
	@Override
	public int compare(Integer o1, Integer o2) 
	{
		//o1>o2?
		int r = 0;
		int max = Math.max(o1, o2);
		for (int i = 0; max < word.length(); i++) 
		{
			if(word.charAt(o1)>word.charAt(o2))
			{
				return 1;
			}
			else if(word.charAt(o1)<word.charAt(o2))
			{
				return -1;
			}
			o1++;
			o2++;
			max++;
		}
		return r;
	}

}
