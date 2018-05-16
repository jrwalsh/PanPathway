package edu.iastate.usda.PanPathway;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.stream.Stream;

import edu.iastate.javacyco.Frame;
import edu.iastate.javacyco.Gene;
import edu.iastate.javacyco.JavacycConnection;
import edu.iastate.javacyco.Protein;
import edu.iastate.javacyco.PtoolsErrorException;

public class Random {
	public static void main(String args[]) {
		Long start = System.currentTimeMillis();
		System.out.println("start!");
		
		testUniLinks();
//		getCornCycGenes();
//		getGOTerms();
//		mapv3_v4();
		
		System.out.println("done!");
		
		Long stop = System.currentTimeMillis();
		Long runtime = (stop - start) / 1000;
		System.out.println("Runtime is " + runtime + " seconds.");
	}

	public static void testUniLinks() {
		int port = 4444;
		String organism = "CORN";
		JavacycConnection conn = new JavacycConnection("jrwalsh.usda.iastate.edu", port);
		conn.selectOrganism(organism);
		try {
			String out = "";
			int i = 1;
			for (Frame gene : conn.getAllGFPInstances(Gene.GFPtype)) {
				ArrayList<String> uniLinks = (ArrayList<String>) gene.getSlotValues("DBLINKS");
				for (Object uniLink : uniLinks) {
					out += gene.getLocalID() + "\t" + gene.getCommonName() + "\t" + gene.getSlotValue("Accession-1") + "\t" + uniLink.toString() + "\n";
				}
			}
			printString(new File("/home/jesse/Desktop/unilinks.tab"), out);
		} catch (PtoolsErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void getCornCycGenes() {
		int port = 4444;
		String organism = "CORN";
		JavacycConnection conn = new JavacycConnection("jrwalsh.usda.iastate.edu", port);
		conn.selectOrganism(organism);
		try {
			String out = "";
			int i = 1;
			for (Frame protein : conn.getAllGFPInstances(Protein.GFPtype)) {
				Protein prot = (Protein) protein;
				ArrayList<Gene> genes = prot.getGenes();
				for (Gene gene : genes) {
					out += gene.getLocalID() + "\t" + gene.getCommonName() + "\t" + gene.getSlotValue("ACCESSION-1") + "\t" + protein.getLocalID() + "\n";
				}
			}
			printString(new File("/home/jesse/Dropbox/Transfer Curations to CornCyc/corncyc8.tab"), out);
		} catch (PtoolsErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void getGOTerms() {
		int port = 4444;
		String organism = "MAIZE";
		JavacycConnection conn = new JavacycConnection("jrwalsh.usda.iastate.edu", port);
		conn.selectOrganism(organism);
		try {
			String out = "";
			int i = 1;
			for (Frame protein : conn.getAllGFPInstances(Protein.GFPtype)) {
				ArrayList<String> goterms = protein.getSlotValues("GO-TERMS");
				for (String goterm : goterms) {
					ArrayList<String> citations = protein.getAnnotations("GO-TERMS", goterm, "CITATIONS");
					for (String citation : citations) {
						Protein prot = (Protein) protein;
						ArrayList<Gene> genes = prot.getGenes();
						for (Gene gene : genes) {
							out += gene.getLocalID() + "\t" + gene.getCommonName() + "\t" + gene.getSlotValue("ACCESSION-1") + "\t" + protein.getLocalID() + "\t" + goterm + "\t" + citation + "\n";
						}
					}
				}
			}
			printString(new File("/home/jesse/Dropbox/Transfer Curations to CornCyc/corncyc8.tab"), out);
		} catch (PtoolsErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void mapv3_v4() {
		HashMap<String, HashSet<String>> map = new HashMap<String, HashSet<String>>();
		String out = "";
		try (BufferedReader br = new BufferedReader(new FileReader("/home/jesse/Dropbox/Transfer Curations to CornCyc/MaizeGDB_v3_v4.map.csv"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       String[] words = line.split("\t");
		       if (!map.containsKey(words[0])) {
		    	   HashSet<String> temp = new HashSet<String>();
		    	   temp.add(words[1]);
		    	   map.put(words[0], temp);
		       } else {
		    	   HashSet<String> temp = map.get(words[0]);
		    	   temp.add(words[1]);
		    	   map.put(words[0], temp);
		       }
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try (BufferedReader br = new BufferedReader(new FileReader("/home/jesse/Dropbox/Transfer Curations to CornCyc/go-terms_in_maizecyc.tab"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       String[] words = line.split("\t");
		       if (map.containsKey(words[2])) {
		    	   for (String item : map.get(words[2])) {
		    		   out += item + "\t" + line + "\n";
		    	   }
		       }
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		printString(new File("/home/jesse/Dropbox/Transfer Curations to CornCyc/mapped.tab"), out);
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
