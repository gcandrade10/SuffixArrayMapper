import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Statistics {

	public static void main(String[] args) throws IOException 
	{
		String path = args[0];
		String pathFastas=path+"/fastas/";
		String pathFastqs=path+"/fastqs/";
		
		File f = new File(pathFastas);
		File[] files =f.listFiles();
		for (int i = 0; i < files.length; i++) 
		{
			String actualNameFile = files[i].getName();
			actualNameFile=actualNameFile.split(".fa")[0];
			System.out.println(actualNameFile);
			String[] p = {pathFastas+actualNameFile+".fa", pathFastqs+"50000"+actualNameFile+".fastq"};
			
			long time = System.currentTimeMillis();
			SuffixArray.main(p);
			time=System.currentTimeMillis()-time;
			File fil = new File (pathFastqs+"50000"+actualNameFile+".fastq"+".statistics");
			f.getParentFile().mkdirs();
			FileWriter writer = new FileWriter(fil,true);
			writer.write("time "+time);
			writer.close();
		}
		
	}

}
