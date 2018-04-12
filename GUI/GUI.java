package GUI;

import java.awt.EventQueue;
import java.awt.Graphics;
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
import java.awt.Color;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import javafx.stage.FileChooser;
import jdk.management.resource.internal.inst.FileOutputStreamRMHooks;
import javax.swing.JLabel;


public class GUI extends JFrame implements MenuListener, ActionListener{

	private JFrame frame;
	private File file;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
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
	public GUI() {
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
		
		PaintPanel aniPanel = new PaintPanel();	// benötigt zum malen mit paintComponent graphics g
		frame.add(aniPanel);
				
	}
	
	
	public class PaintPanel extends JPanel{
	    public void paintComponent(Graphics g){
	    	
	    	g.setColor(Color.RED);
	    	g.fillOval(100, 100, 100, 100);
	    	g.setColor(Color.black);
	    	g.drawString("lol", 145, 150);
	   
	    }
	  }
	
	
	@Override
	public void actionPerformed(ActionEvent e) {	// ein button oder ähnliches wurde angeklickt!
		
		JFileChooser chooser = new JFileChooser("C:\\Users\\Miles\\Documents");
		String quelle = e.getActionCommand();
		String data,info,line;
		
		if(quelle == "Exit") {
			System.exit(0);
		}
		
		if(quelle == "Open File...") {
			
			int rueckgabewert = chooser.showDialog(null,"Öffnen");
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
				String info2 = "420 x 512 Zahlen	20 Sekunden Laufzeit bei 60fps	Beschreibung: Dieses Programm stellt einen größer werdenden Kreis dar";
				String data2 = "1100110010101010";
				fw.write(info2);
				fw.write("\n" + data2);
				fw.close();
				
			} catch ( IOException error2) {
				// TODO: handle exception
			}
			
		}
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

	