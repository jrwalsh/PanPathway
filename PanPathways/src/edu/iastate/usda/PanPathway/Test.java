package edu.iastate.usda.PanPathway;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;

import edu.iastate.javacyco.Compound;
import edu.iastate.javacyco.Frame;
import edu.iastate.javacyco.Gene;
import edu.iastate.javacyco.JavacycConnection;
import edu.iastate.javacyco.PtoolsErrorException;

public class Test {
	public static void main(String args[]) {
		Long start = System.currentTimeMillis();
		System.out.println("start!");
		
//		test();
//		parseSubgenome();
		checkCompounds();
		
		System.out.println("done!");
		
		Long stop = System.currentTimeMillis();
		Long runtime = (stop - start) / 1000;
		System.out.println("Runtime is " + runtime + " seconds.");
	}
	
//	private static void parseSubgenome() {
//		InputStream inputStream;
//		try {
//			inputStream = new FileInputStream("/home/jesse/Dropbox/SynMap Subgenomes/31607_34889.CDS-CDS.lastz.tdd10.cs0.filtered.dag.all.go_D20_g10_A5.aligncoords.gcoords.ks");
//			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
//			String line = in.readLine();
//			while ((line = in.readLine()) != null) {
//				System.out.println(line);
//			}
//			
//			in.close();
//			inputStream.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public static void checkCompounds() {
		int port = 4444;
		String organism = "CORN";
		JavacycConnection conn = new JavacycConnection("localhost", port);
		conn.selectOrganism(organism);
		try {
			String out = "";
			for (Frame compound : conn.getAllGFPInstances(Compound.GFPtype)) {
				out += compound.getLocalID() + "\t" + compound.getCommonName() + "\t" + compound.getSlotValue("SMILES") + "\t" + compound.getSlotValue("InChI") + "\t" + compound.isClassFrame() + "\n";
			}
			printString(new File("compounds.tab"), out);
		} catch (PtoolsErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void test() {
		int port = 4444;
		String organism = "MAIZE";
		JavacycConnection conn = new JavacycConnection("jrwalsh.usda.iastate.edu", port);
		conn.selectOrganism(organism);
		try {
			String out = "";
			for (Frame gene : conn.getAllGFPInstances(Gene.GFPtype)) {
				out += gene.getLocalID() + "\t" + gene.getCommonName() + "\t" + gene.getSlotValue("Accession-1") + "\t" + gene.getSlotValue("Accession-2") + "\n";
			}
			InputStream inputStream = new FileInputStream("/home/jesse/Dropbox/temp/maize1.txt");
//			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
//			String line = in.readLine();
//		
//			while ((line = in.readLine()) != null) {
//				ArrayList<Frame> returns = conn.search(line, Gene.GFPtype);
//				System.out.println(conn.pathwaysOfGene(returns.get(1).getLocalID()));
//			}
//			
//			in.close();
			printString(new File("out.old"), out);
			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PtoolsErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Simple function to print a string to the specified file location.
	 * 
	 * @param fileName
	 * @param printString
	 */
	protected static void printString(File file, String printString) {
		PrintStream o = null;
		try {
			o = new PrintStream(file);
			o.println(printString);
			o.close();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
