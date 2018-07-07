package de.ghse.steuerung;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFileChooser;

public class FileManager {
	int line;



	int lineold;
	
	private String name;
	  
	  

	  
	  
	
	  
	  public int getFrameCount() {
		  return line;
	  }
	  
	public boolean[][] openFileArray() {// Rueckgabewert hinzugefuegt boolean[frameNr][Cube Stelle] Array

		// erste Dimension steht für die Frame Anzahl 1024 individuelle Frames können
		// eingelesen werden
		// zweite Dimesion steht für die 512 einzelnen LEDS
		boolean savedmatrix[][] = new boolean[1024][512];

		// JFileChooser Referenz
		JFileChooser chooser = new JFileChooser();

		// File Objekt um den Pfad zu speichern
		File file;

		int rueckgabewert = chooser.showDialog(null, "Open"); // Returns if file was choosen or not

		try {

			if (rueckgabewert == JFileChooser.APPROVE_OPTION) {

				file = chooser.getSelectedFile();

				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);

				line = 0;
				lineold = 0;
				boolean isnext = true;
				A: while (isnext) {

					String Zeile = br.readLine();
					if (Zeile == null) {
						System.out.println("Ende:" + Zeile);
						br.close();
						isnext = false;
						break A;
					}
					for (int i = 0; i < 512; i++) {

						int temp = Integer.parseInt(Zeile.substring(i, i + 1));

						if (temp == 1) { // wenn der wert des booleans = true ist, ist die LED an dieser stelle AN
							savedmatrix[line][i] = true;

						} else { // ansonsten AUS
							savedmatrix[line][i] = false;
						}
						/*
						 * System.out.print(savedmatrix[line][i]); if(line>lineold) { lineold=line;
						 * System.out.println(); }
						 */
					}
					line++;
				}

				// System.out.println("No more Data");
			}

		} catch (Exception error1) {
			error1.printStackTrace();// nichts ausgeben
		}
		return savedmatrix; // Return of array
	}

	  
	  
	  
	  public void SaveArraytoFile(boolean[][] matrix,String name,int length) {
		
		
		JFileChooser chooser = new JFileChooser();
		
		File file;
	    			//bisher leer
		
	    
	    chooser.setSelectedFile(new File(name+".cube"));	//File auswaehlen, indem der String gespeichert werden soll
	    chooser.showSaveDialog(chooser);			//Fenster zum abspeichern anzeigen
	    file = chooser.getSelectedFile();			//
	    
	    try {
	      FileWriter fw = new FileWriter(file);
	      for (int l = 0; l < length; l++) {
			
	    	  String data ="";
	    	  for (int i = 0; i < 512; i++) { 	// die werte von den angeklickten buttons uebernehmen und in die .cube datei schreiben
	                    
	    		  if (matrix[l][i]) {		 // wenn der wert des booleans = true ist, ist die LED an dieser stelle AN
	    			  data = data + "1";
	    		  } else { // ansonsten AUS
	    			  data = data + "0";
	    		  }
	    	  }
	    	  data=data+"\n";
	    	  fw.write(data); 	// in die .cube datei schreiben
	      }
	      fw.close(); 		// file writer schliessen

	    } catch (IOException error2) {
	      // nichts
	    }
	  }
}
