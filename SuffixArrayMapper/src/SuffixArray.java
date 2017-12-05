import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import ngsep.genome.GenomicRegion;
import ngsep.genome.ReferenceGenome;
import ngsep.sequences.QualifiedSequence;
import ngsep.sequences.RawRead;

public class SuffixArray 
{	
	private String referenceGenomeFile;
	private String reads;
	private PrintWriter writer;
	private int contador;

	private ArrayList<SuffixArraySimple> suffixesArray = new ArrayList<>();
	private ReferenceGenome referenceGenome;
	
	public SuffixArray(String fileReferenceGenome, String fileReads) throws IOException 
	{
		contador=0;
		File f = new File (fileReads+".statistics");
		f.getParentFile().mkdirs();
		writer = new PrintWriter(f);
		referenceGenomeFile = fileReferenceGenome;
		reads = fileReads;
		
		//lee el genoma de referencia
		referenceGenome = new ReferenceGenome(referenceGenomeFile);
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
		int fastqs=0;
		while(read!=null) 
		{
			fastqs++;
			search(read);
			read = RawRead.load(in);
		}
		writer.println("Se mapearon "+contador + "de " +fastqs);
		fis.close();
		writer.close();
	}

	private void search(RawRead read) 
	{
		
//		System.out.println(read.getName() + " found in:");
		for (int i = 0; i < suffixesArray.size(); i++) 
		{
			SuffixArraySimple current= suffixesArray.get(i);
			ArrayList<GenomicRegion> arr = current.search(read.getSequenceString());
			if(arr.size()>0)
			{
//				System.out.println("\t"+suffixesArray.get(0).getName());
				for (int j = 0; j < arr.size(); j++) 
				{
//					writer.println(read.getName() + " found in:"+"\t"+suffixesArray.get(0).getName()+"pos: "+arr.get(j).getFirst());
//					System.out.println("\t\t"+arr.get(j).getFirst());
					System.out.println(
							//1.query name
							read.getName()+"\t"+
									
							//2.Flag
							"0\t"+
							
							//3.reference sequence name
							suffixesArray.get(0).getName()+
							
							//4.POS
							arr.get(j).getFirst()+
							
							//5.MAPQ
							"255\t"+
							
							//6.CIGAR
							read.getLength()+"M\t"+
							
							//7. RNEXT
							"*\t"+
							
							//8. PNEXT
							"0\t"+
							
							//9. TLEN
							"0\t"+
							
							//10. SEQ
							read.getSequenceString()+"\t"+
							
							//11. QUAL
							read.getQualityScores()+"*"
							
							);
				}
				contador++;
			}
		}
		
	}

	public static void main(String[] args) throws IOException 
	{
		String fileReferenceGenome= args[0];
		String fileReads=args[1];
		SuffixArray s = new SuffixArray(fileReferenceGenome, fileReads);
//		s.prinSuffixArray();
	}
	public void prinSuffixArray()
	{
		//suffixesArray
		for (int i = 0; i < suffixesArray.get(0).getSufixes().size(); i++) 
		{
			System.out.println(referenceGenome.getSequenceCharacters(0).subSequence(suffixesArray.get(0).getSufixes().get(i), referenceGenome.getSequenceCharacters(0).length()-1));
		}
	}
}
