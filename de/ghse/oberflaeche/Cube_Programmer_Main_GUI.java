package de.ghse.oberflaeche;



import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import javafx.stage.FileChooser;
import jdk.management.resource.internal.inst.FileOutputStreamRMHooks;
import javax.swing.JLabel;


public class Cube_Programmer_Main_GUI extends JFrame implements MenuListener, ActionListener, ItemListener{

	private JFrame frame;
	private File file;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Cube_Programmer_Main_GUI window = new Cube_Programmer_Main_GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Cube_Programmer_Main_GUI() {
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 449, 349);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JLabel label = new JLabel("");
		frame.getContentPane().add(label, BorderLayout.CENTER);
		
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		
		JMenu File = new JMenu("File");
		menuBar.add(File);
		
		JMenuItem OpenFile = new JMenuItem("Open File...");
		File.add(OpenFile);
		OpenFile.addActionListener(this);
		
		JMenuItem SaveAs = new JMenuItem("Save as...");
		File.add(SaveAs);
		SaveAs.addActionListener(this);
		
		JMenuItem Exit = new JMenuItem("Exit");
		File.add(Exit);
		Exit.addActionListener(this);
		
		
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		JFileChooser chooser = new JFileChooser("C:\\Users\\Miles\\Documents");
		String quelle = e.getActionCommand();
		String data,info,line;
		
		if(quelle == "Exit") {
			System.exit(0);
		}
		
		if(quelle == "Open File...") {
			
			int rueckgabewert = chooser.showDialog(null,"Open");
			if(rueckgabewert == JFileChooser.APPROVE_OPTION) {
				file = chooser.getSelectedFile();
				
				try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				FileReader fileReader = new FileReader(file);
				StringBuffer stringBuffer = new StringBuffer();
				
				info = reader.readLine();	//erste zeile lesen weil dort info steht
			
				while ((line = reader.readLine()) != null) {	//er macht mit zeile 2 weiter
					stringBuffer.append(line);
					stringBuffer.append("\n");
				}
				fileReader.close();
				data = stringBuffer.toString();
				System.out.println("Info: " + info);
				System.out.println("Daten: " + data);
				
					
				} catch (Exception error1) {
					error1.printStackTrace();//nichts ausgeben
				}					
			}//end of if
			
		}//end of if
		
		if(quelle == "Save as...") {
			
			chooser.setSelectedFile(new File("*.cube"));
			chooser.showSaveDialog(this);
			File file = chooser.getSelectedFile();
			try {
				FileWriter fw = new FileWriter(file);
				String info2 = "420 x 512 Zahlen	20 Sekunden Laufzeit bei 24fps	Beschreibung: Dieses Programm stellt einen gr��er werdenden Kreis dar";
				String data2 = "1100110010101010";
				fw.write(info2);
				fw.write(data2);
				fw.close();
				
			} catch ( IOException error2) {
				// TODO: handle exception
			}
			
		}
	}
	
	
	
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void menuCanceled(MenuEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void menuDeselected(MenuEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void menuSelected(MenuEvent e) {
		// TODO Auto-generated method stub
		
	}
		
	}

	

