package de.ghse.steuerung;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JFileChooser;

public class FileManager {
	
	String info2 = "420 x 512 Zahlen	20 Sekunden Laufzeit bei 24fps	Beschreibung: Dieses Programm stellt einen groesser werdenden Kreis dar";
	String data2 = "";
	JFileChooser chooser = new JFileChooser();
	private File file;
		
	
public boolean[] openFileArray() {//Rueckgabewert hinzugefuegt boolean[] Array

	String data, info, line;
	
	boolean[] savedmatrix=new boolean[512]; // Boolean Array das zurueckgegeben wird von 0 anfangen zu zaehlen
	for (int matrixsetup = 0; matrixsetup <512; matrixsetup++) {//Um bugs vermeiden wird array erstmal mit 0 beschrieben 
		savedmatrix[matrixsetup] = false;
		System.out.println("matrix["+savedmatrix[matrixsetup]+"] = true");
		
	}
	
	int rueckgabewert = chooser.showDialog(null, "Open");
	
	if (rueckgabewert == JFileChooser.APPROVE_OPTION) {
		file = chooser.getSelectedFile();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			FileReader fileReader = new FileReader(file);
			StringBuffer stringBuffer = new StringBuffer();

			info = reader.readLine(); 	// erste zeile lesen weil dort info steht

			while ((line = reader.readLine()) != null) { // er macht mit zeile 2 weiter, liest solange bis eine															// leere zeile kommt
				stringBuffer.append(line);
			}
			
			fileReader.close();
			reader.close();
			
			data = stringBuffer.toString();
			System.out.println("Info:  " + info);
			System.out.println("Daten: " + data);

		} catch (Exception error1) {
			error1.printStackTrace();// nichts ausgeben
		}
	} // end of if
return savedmatrix; // Return of array
}


public boolean[] UpdateSavedmatrix(boolean[] matrix) {
	boolean[] savedmatrix=new boolean[512];	
	for (int i = 0; i <= 511; i++) {		
		if (matrix[i]==true) {				//wenn der wert des booleans = true ist, ist die LED an dieser stelle AN
			savedmatrix[i]=true;
		} else {							//ansonsten AUS
			savedmatrix[i]=false;
		}	
	}	
	return savedmatrix;
}



public String UpdateData2(boolean[] matrix) {
	for (int i = 0; i <= 511; i++) {		
		if (matrix[i]==true) {				//wenn der wert des booleans = true ist, ist die LED an dieser stelle AN
			data2=data2+"1";
		} else {							//ansonsten AUS
			data2=data2+"0";
		}	
	}
	return data2;
}

public void SaveArraytoFile(boolean[] matrix) {
	
	chooser.setSelectedFile(new File(".cube"));
	chooser.showSaveDialog(chooser);
	file = chooser.getSelectedFile();
	try {
		FileWriter fw = new FileWriter(file);
		fw.write(info2);
		fw.write("\n");							//neue zeile
		for (int i = 0; i <= 511; i++) {		//die werte von den angeklickten buttons übernehmen und in die .cube datei schreiben
			if (matrix[i]==true) {				//wenn der wert des booleans = true ist, ist die LED an dieser stelle AN
				data2=data2+"1";				
			} else {							//ansonsten AUS
				data2=data2+"0";
			}	
		}
		fw.write(data2);						//in die .cube datei schreiben
		fw.close();								//file writer schliessen

	} catch (IOException error2) {
		//nichts
	}
}

}
