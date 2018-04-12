package de.ghse.oberflaeche;



import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
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

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.ghse.steuerung.Steuerung;
import javafx.stage.FileChooser;
import jdk.management.resource.internal.inst.FileOutputStreamRMHooks;
import javax.swing.JLabel;


public class Cube_Programmer_Main_GUI extends JFrame implements MenuListener, ActionListener, ItemListener{

	final int LEDS = 64; //Anzahl der LEDS
	int layer = 0,countm,countn,button;
	private JButton[] buttons;
	private JTextField display;
	private JTextField CurrentEbenetext;
	private JButton output;
	private JButton ebeneup;
	private JButton ebenedown;
	private boolean[] geklickt = new boolean[LEDS];
	private boolean[] matrix = new boolean[513];
	private int CurrentEbene=0;
	Steuerung obj = new Steuerung();
	Boolean an_aus;
	 
	
	
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
		
		
	}//lol

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		int frameWidth = 1000; 
		int frameHeight = 1000;
		
		frame = new JFrame();
		frame.setSize(frameWidth,frameHeight);
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
		
		
	    //Button Panel
		for (int matrixsetup = 0; matrixsetup < 513; matrixsetup++) {// Setting up the the save array
			matrix[matrixsetup]=false;
			
		}
	    	    
	    
	    buttons = new JButton[LEDS];
	    
	    JPanel buttonPanel = new JPanel();
	    buttonPanel.setPreferredSize(new Dimension(800,800));
	    //buttonPanel.setLayout( new GridLayout(9,8,15,15));
	    
	    for (int i = 0; i < buttons.length; i++)
	    {
	        String LED = String.valueOf(i+1);  //+1 Damit die Buttons von 1-64 gezählt werden
	        JButton button = new JButton(LED);
	        
	        button.setBackground(new Color(255,255,255));
	        button.addActionListener( this );
	        button.setMnemonic( LED.charAt(0) );
	        
	        buttons[i] = button;
	        buttonPanel.add( buttons[i] );
	    }
	    
	    output = new JButton("Weiter");
	    buttonPanel.add(output);
	    output.addActionListener(this);
	    output.setBackground(new Color(255,0,255));
	    
	    
	    ebeneup = new JButton("UP");
	    buttonPanel.add(ebeneup);
	    ebeneup.addActionListener(this);
	    ebeneup.setBackground(new Color(255,0,255));
	   
	    
	    
	    ebenedown = new JButton("DOWN");
	    buttonPanel.add(ebenedown);
	    ebenedown.addActionListener(this);
	    ebenedown.setBackground(new Color(255,0,255));
	    
	    
	    
	    int displayEbene=CurrentEbene+1;
	    
	    CurrentEbenetext= new JTextField("Ebene = "+displayEbene);
	    buttonPanel.add(CurrentEbenetext);

	    UIManager.put("Button.margin", new Insets(10, 10, 10, 10) ); //gibt die Form der Buttons vor
	    
	    frame.getContentPane().add(buttonPanel,BorderLayout.SOUTH);
	    //Button Panel Setup zu Ende
	    PaintPanel aniPanel = new PaintPanel();    // benötigt zum malen mit paintComponent graphics g
	    frame.add(aniPanel);
		
	}
	
	public class PaintPanel extends JPanel{

        public void paintComponent(Graphics g){

            g.setColor(Color.red);
            g.fillOval(150, 150, 200, 200);

        }
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
		ButtonPanelActionListener(quelle);
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
	public void ButtonPanelActionListener(String quelle)  //actionlistener um herasuzufinden welcher button gedr�ckt wurde. jeder Button Teilt sich einen ActionListener
	{   
		System.out.println(quelle);
			int zahl=0;  
		
		//JButton source = (JButton)e.getSource(); //findet raus welcher Button den EventListener ausgel�st hat
	      if (quelle== "Weiter") {
	    	
	    	
	    	  
	    	  
	      }else if (quelle=="UP") {//UP Knopf
	    	  if(CurrentEbene<7)
	    		  {
	    		  CurrentEbene++;
	    		  EbeneUpdate();
	    		  }
	      }else if (quelle=="DOWN") {//DOWN Knopf
	    	  if(CurrentEbene>0)
	    	  {
	    	  CurrentEbene--;
	    	  EbeneUpdate();
	    	  }
	      }else {
			
		
	    	 
	    	  zahl = Integer.parseInt((quelle))-1;
	    	  buttons[zahl].setBackground(new Color(2,2,52));
	    	  if (!geklickt[zahl]) {
	          geklickt[zahl] = true;  
	          //display.replaceSelection(" | " + quelle);
	          an_aus=true;
	          //obj.knopfGedrueckt(button, an_aus); 			//Gibt Knopfdaten weiter
	      }else {
	    	  geklickt[zahl] = false;
	    	  buttons[zahl].setBackground(new Color(255,255,255));
	      }	
	    	 
	    	  
	    }
	      ButtonState(geklickt[zahl],zahl);  
	}

	void ButtonState(boolean state,int button_nr) { // Updated Button State Array
		int CurrentOffset=(CurrentEbene)*64;
		
		matrix[button_nr+CurrentOffset]=state;
		
		}
	void EbeneUpdate() { //Methode die die Buttons updated
		
		CurrentEbenetext.setText("Ebene = "+Integer.toString(CurrentEbene+1));//Display der derzeitigen Ebene
		int CurrentOffset=(CurrentEbene)*64;//Button Offset
		
		for (int i = 0; i <64; i++) {
			
			if(matrix[i+CurrentOffset])	// Findet den Status der Knöpfe heraus
			{
				geklickt[i]=true;
			buttons[i].setBackground(new Color(2,2,52)); // Setzt den Button entsprechend
			}else
			{
				geklickt[i]=false;
				
			buttons[i].setBackground(new Color(255,255,255));// Setzt den Button entsprechend
			}
					
					
		}
		
		
			
	}	
	}

	

