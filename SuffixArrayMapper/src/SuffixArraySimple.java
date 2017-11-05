import java.util.ArrayList;
import java.util.Collections;

import javax.swing.plaf.synth.Region;

import ngsep.genome.GenomicRegion;
import ngsep.genome.GenomicRegionImpl;
import ngsep.sequences.QualifiedSequence;

public class SuffixArraySimple 
{
	private String seqName;
	private String word;
	private ArrayList<Integer> sufixes;

	public SuffixArraySimple(QualifiedSequence q) 
	{
		seqName=q.getName();
		word=q.getCharacters().toString();
		createSuffixArray();
		sort();
	}

	private void createSuffixArray() 
	{
		sufixes = new ArrayList<Integer>();
		for (int i = 0; i < word.length(); i++) 
		{
			sufixes.add(i);
		}
	}

	private void sort()
	{
		Collections.sort(sufixes,new ComparatorString(word));
	}

	public ArrayList<GenomicRegion> search(String busqueda)
	{

		ArrayList<GenomicRegion> r = new ArrayList<GenomicRegion>();
		int begin = binarySearch(busqueda);
		boolean c = begin!=-1;
		if(c)
		{

			for (int i = begin; i < sufixes.size() && c; i++) 
			{
				GenomicRegionImpl g = new GenomicRegionImpl(seqName, sufixes.get(i), sufixes.get(i)+busqueda.length());
				r.add(g);
				String suffix=word.substring(sufixes.get(i+1));
				c=suffix.startsWith(busqueda);
			}
			String suffix=word.substring(sufixes.get(begin-1));
			c=suffix.startsWith(busqueda);
			for (int i = begin-1; i < sufixes.size() && c; i--) 
			{
				GenomicRegionImpl g = new GenomicRegionImpl(seqName, sufixes.get(i), sufixes.get(i)+busqueda.length());
				r.add(g);
				suffix=word.substring(sufixes.get(i+1));
				c=suffix.startsWith(busqueda);
			}
		}

		return r;
	}

	/**
	 * Returns first position of first Suffix
	 * @param data
	 * @return
	 */
	private int binarySearch( String data)
	{
		int n = sufixes.size();
		int center,inf=0,sup=n-1;
		while(inf<=sup)
		{
			center=(sup+inf)/2;
			String suffix=word.substring(sufixes.get(center));
			int compare=data.compareTo( suffix );
			if(suffix.startsWith(data)) return center;		     
			else if(compare<0)
			{
				sup=center-1;
			}
			else 
			{
				inf=center+1;
			}
		}
		return -1;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return seqName;
	}


}
