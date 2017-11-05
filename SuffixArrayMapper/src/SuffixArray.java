import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import ngsep.genome.GenomicRegion;
import ngsep.genome.ReferenceGenome;
import ngsep.sequences.QualifiedSequence;
import ngsep.sequences.RawRead;

public class SuffixArray 
{	
	private String referenceGenomeFile;
	private String reads;

	private ArrayList<SuffixArraySimple> suffixesArray = new ArrayList<>();
	
	public SuffixArray(String fileReferenceGenome, String fileReads) throws IOException 
	{
		referenceGenomeFile = fileReferenceGenome;
		reads = fileReads;
		
		//lee el genoma de referencia
		ReferenceGenome referenceGenome = new ReferenceGenome(referenceGenomeFile);
		int n = referenceGenome.getNumSequences();
		
		for (int i = 0; i < n; i++) 
		{
			QualifiedSequence q = referenceGenome.getSequenceByIndex(i);
			//Procesar las secuencias del fasta
			suffixesArray.add(new SuffixArraySimple(q));
//			System.out.println(q.getName());
		}
		
		//lee los fastaq
		FileInputStream fis = new FileInputStream(reads);
		BufferedReader in = new BufferedReader(new InputStreamReader(fis));
		RawRead read = RawRead.load(in);
		while(read!=null) 
		{
			search(read);
			read = RawRead.load(in);
		}
		
		fis.close();
		
	}

	private void search(RawRead read) 
	{
		System.out.println(read.getName() + " found in:");
		for (int i = 0; i < suffixesArray.size(); i++) 
		{
			SuffixArraySimple current= suffixesArray.get(i);
			ArrayList<GenomicRegion> arr = current.search(read.getSequenceString());
			if(arr.size()>0)
			{
				System.out.println("\t"+suffixesArray.get(0).getName());
				for (int j = 0; j < arr.size(); j++) 
				{
					System.out.println("\t\t"+arr.get(j).getFirst());
				}
			}
		}
	}

	public static void main(String[] args) throws IOException 
	{
		String fileReferenceGenome= args[0];
		String fileReads=args[1];
		SuffixArray s = new SuffixArray(fileReferenceGenome, fileReads);
	}

}
