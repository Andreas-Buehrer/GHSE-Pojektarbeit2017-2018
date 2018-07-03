package tcp_netzwerktest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFileChooser;

public class FileReaderLines {
	int line;



	int lineold;
	
	private String name;
	  
	  

	  
	  
	  
	  public void gibNamen(String name) {
			this.name = name;
		}
	  
	  public int FrameCount() {
		  return line;
	  }
	  
	  public boolean[][] openFileArray() {// Rueckgabewert hinzugefuegt boolean[frameNr][Cube Stelle] Array

			boolean savedmatrix[][] = new boolean[1024][512];
			JFileChooser chooser = new JFileChooser();
			File file;
			
			 int rueckgabewert = chooser.showDialog(null, "Open");
			    
			    try {

			      if (rueckgabewert == JFileChooser.APPROVE_OPTION) {
			        file = chooser.getSelectedFile();

			        FileReader fr=new FileReader(file);
					BufferedReader br=new BufferedReader(fr);
					
					line=0;
					lineold=0;
					boolean isnext=true;
					A:
					while (isnext) {
						
						String Zeile=br.readLine();
						if(Zeile==null)
						{
							System.out.println("Ende:"+Zeile);
							br.close();
							isnext=false;
							break A;
						}
						for (int i = 0; i < 512; i++) {

						      int temp = Integer.parseInt(Zeile.substring(i, i + 1));

						      if (temp == 1) { // wenn der wert des booleans = true ist, ist die LED an dieser stelle AN
						        savedmatrix[line][i] = true;

						      } else { // ansonsten AUS
						        savedmatrix[line][i] = false;
						      }
						      System.out.print(savedmatrix[line][i]);
						      if(line>lineold) {
						    	  lineold=line;
						    	  System.out.println();
						      }
						    }
						line++;
					}
					
						System.out.println("No more Data");
			      }

			    } catch (Exception error1) {
			      error1.printStackTrace();// nichts ausgeben
			    }
	    return savedmatrix; // Return of array
	  }

	  
	  
	  
	  public void SaveArraytoFile(boolean[] matrix) {

	    String data = "";			//bisher leer

	    for (int i = 0; i < 512; i++) {
	      if (matrix[i] == true) { // wenn der wert des booleans = true ist, ist die LED an dieser stelle AN
	        data = data + "1";
	      } else { 			// ansonsten AUS
	        data = data + "0";
	      }
	    }
	    
	    chooser.setSelectedFile(new File(name+".cube"));	//File auswaehlen, indem der String gespeichert werden soll
	    chooser.showSaveDialog(chooser);			//Fenster zum abspeichern anzeigen
	    file = chooser.getSelectedFile();			//
	    
	    try {
	      FileWriter fw = new FileWriter(file);
	      
	      for (int i = 0; i < 512; i++) { 	// die werte von den angeklickten buttons uebernehmen und in die .cube datei schreiben
	                    
	        if (matrix[i]) {		 // wenn der wert des booleans = true ist, ist die LED an dieser stelle AN
	          data = data + "1";
	        } else { // ansonsten AUS
	          data = data + "0";
	        }
	      }

	      fw.write(data); 	// in die .cube datei schreiben
	      fw.close(); 		// file writer schliessen

	    } catch (IOException error2) {
	      // nichts
	    }
	  }
}
