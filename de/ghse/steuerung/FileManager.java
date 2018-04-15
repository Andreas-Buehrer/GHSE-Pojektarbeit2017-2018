package de.ghse.steuerung;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;

public class FileManager {
	String info2 = "420 x 512 Zahlen	20 Sekunden Laufzeit bei 24fps	Beschreibung: Dieses Programm stellt einen größer werdenden Kreis dar";
	String data2 = "11101010110";
	JFileChooser chooser = new JFileChooser();
	private File file;
	
public void openFileArray() {
	
	String data, info, line;
	
	//private boolean[] savedmatrix=new boolean[513];
	
	int rueckgabewert = chooser.showDialog(null, "Open");
	
	if (rueckgabewert == JFileChooser.APPROVE_OPTION) {
		file = chooser.getSelectedFile();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			FileReader fileReader = new FileReader(file);
			StringBuffer stringBuffer = new StringBuffer();

			info = reader.readLine(); // erste zeile lesen weil dort info steht

			while ((line = reader.readLine()) != null) { // er macht mit zeile 2 weiter, liest solange bis eine
															// leere zeile kommt
				stringBuffer.append(line);
				stringBuffer.append("\n");
			}
			fileReader.close();
			reader.close();
			
			data = stringBuffer.toString();
			System.out.println("Info: " + info);
			System.out.println("Daten: " + data);

		} catch (Exception error1) {
			error1.printStackTrace();// nichts ausgeben
		}
	} // end of if

}
public void SaveArraytoFile() {
	
	
	chooser.setSelectedFile(new File(".cube"));
	chooser.showSaveDialog(chooser);
	file = chooser.getSelectedFile();
	try {
		FileWriter fw = new FileWriter(file);
		fw.write(info2);
		fw.write(data2);
		fw.close();

	} catch (IOException error2) {
		
	}
}
}
