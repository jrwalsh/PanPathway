package edu.iastate.usda.PanPathway;

import java.util.ArrayList;
import java.util.TreeSet;

import edu.iastate.javacyco.JavacycConnection;
import edu.iastate.javacyco.Pathway;
import edu.iastate.javacyco.PtoolsErrorException;

public class Main {

	
	public static void main(String args[]) {
		Long start = System.currentTimeMillis();
		
//		test();
		compounds();
		
		System.out.println("done!");
		
		Long stop = System.currentTimeMillis();
		Long runtime = (stop - start) / 1000;
		System.out.println("Runtime is " + runtime + " seconds.");
	}
	
	public static void test() {
		int port = 4444;
		String organism = "PLANT";
		JavacycConnection conn = new JavacycConnection("jrwalsh.usda.iastate.edu", port);
		conn.selectOrganism(organism);
		
		try {
			
//			Frame.load(conn,  "TAX-37682").print();
			
			
			System.out.println(conn.getClassAllInstances(Pathway.GFPtype).size());
			
//			for (Frame pwy : conn.getAllGFPInstances(Pathway.GFPtype)) {
//				System.out.println(pwy.getLocalID() + " :: " + pwy.getSlotValues("Species"));
//			};
			
			
		} catch (PtoolsErrorException e) {
			e.printStackTrace();
		}
	}
	
	public static void compounds() {
		int port = 4444;
		String organism = "CORN";
		JavacycConnection conn = new JavacycConnection("localhost", port);
		conn.selectOrganism(organism);
		
		TreeSet<String> metabolites = new TreeSet<String>();
		
		try {
			ArrayList<String> pathways = (ArrayList<String>) conn.getClassAllInstances(Pathway.GFPtype);
			for (String pathway : pathways) {
				ArrayList<String> compounds = (ArrayList<String>) conn.compoundsOfPathway(pathway);
				metabolites.addAll(compounds);
			};
		} catch (PtoolsErrorException e) {
			e.printStackTrace();
		}
		
		for (String metabolite : metabolites) {
			System.out.println(metabolite);
		}
	}
}
