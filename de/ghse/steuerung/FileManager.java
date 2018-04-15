package de.ghse.steuerung;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JFileChooser;

public class FileManager {
	
	String info2 = "420 x 512 Zahlen	20 Sekunden Laufzeit bei 24fps	Beschreibung: Dieses Programm stellt einen groesser werdenden Kreis dar";
	
	JFileChooser chooser = new JFileChooser();
	private File file;
		
	
public boolean[] openFileArray() {//Rueckgabewert hinzugefuegt boolean[] Array

	String data="";
	Scanner scannerdata;
	
	boolean[] savedmatrix=new boolean[512]; // Boolean Array das zurueckgegeben wird von 0 anfangen zu zaehlen
	for (int matrixsetup = 0; matrixsetup <512; matrixsetup++) {//Um bugs vermeiden wird array erstmal mit 0 beschrieben 
		savedmatrix[matrixsetup] = false;
		
	}
	
	int rueckgabewert = chooser.showDialog(null, "Open");
	try {
		
		
		
	if (rueckgabewert == JFileChooser.APPROVE_OPTION) {
		file = chooser.getSelectedFile();

		
			//BufferedReader reader = new BufferedReader(new FileReader(file));
			//FileReader fileReader = new FileReader(file);
			//StringBuffer stringBuffer = new StringBuffer();

			//reader.readLine(); 	// erste zeile lesen weil dort info steht
			
			
			
			
			//reader.close();
			//fileReader.close();
			
			scannerdata= new Scanner(file);
			while(scannerdata.hasNext())
			{
				data=scannerdata.next();
			}
			//data = stringBuffer.toString();
			
			
			
	}

		} catch (Exception error1) {
			error1.printStackTrace();// nichts ausgeben
		}
	
		
	for (int i = 0; i < 512; i++) {		
		
		int temp=Integer.parseInt(data.substring(i,i+1));
		
		if (temp==1) 
		{				//wenn der wert des booleans = true ist, ist die LED an dieser stelle AN
			savedmatrix[i]=true;
			
		} else {							//ansonsten AUS
			savedmatrix[i]=false;
		}	
		
	}	
	
return savedmatrix; // Return of array
}







public void SaveArraytoFile(boolean[] matrix) {
	
	String data="";
	
	for (int i = 0; i < 512; i++) {		
		if (matrix[i]==true) {				//wenn der wert des booleans = true ist, ist die LED an dieser stelle AN
			data=data+"1";
		} else {							//ansonsten AUS
			data=data+"0";
		}	
	}
	
	chooser.setSelectedFile(new File(".cube"));
	chooser.showSaveDialog(chooser);
	file = chooser.getSelectedFile();
	try {
		FileWriter fw = new FileWriter(file);
		//fw.write(info2);
		//fw.write("\n");							//neue zeile
		for (int i = 0; i < 512; i++) {		//die werte von den angeklickten buttons �bernehmen und in die .cube datei schreiben
			if (matrix[i]) {					//wenn der wert des booleans = true ist, ist die LED an dieser stelle AN
				data=data+"1";				
			} else {							//ansonsten AUS
				data=data+"0";
			}
		
		}
		fw.write(data);						//in die .cube datei schreiben
		fw.close();								//file writer schliessen

	} catch (IOException error2) {
		//nichts
	}
}

}
