package de.ghse.oberflaeche;

import de.ghse.schnittstelle.SimpleDataSending;
import de.ghse.steuerung.FileManager;
import de.ghse.steuerung.Steuerung;
import de.ghse.steuerung.Undo;
import de.ghse.werkzeuge.Stoppuhr;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.StyledEditorKit.ForegroundAction;
import javax.swing.JLabel;
import javax.swing.JList;

public class GUI extends JFrame implements ActionListener {

	ImageIcon blau = new ImageIcon("pictures/BlauerPunkt.png");
	ImageIcon grau = new ImageIcon("pictures/GrauerPunkt.png");	
	final int LEDS = 64; // Anzahl der LEDS
	int layer = 0, countm, countn, button,counter=12,selectedItem,anzahlItems = 0;;
	boolean[] doppelt = new boolean[20];
	private JButton[] buttons;
	private JLabel CurrentEbenetext;
	private JButton output,ebeneup,ebenedown,reset,sframe;
	private boolean[] geklickt = new boolean[LEDS];
	private boolean[] matrix = new boolean[512];
	public boolean[] matrixTemp = new boolean[512];
	private int CurrentEbene = 0;
	Steuerung obj = new Steuerung();
	Boolean an_aus;
	private JFrame frame;
	public JTextField textField;
	public JList list;
	

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
		
		MatrixInit();
		
		int frameWidth = 1800;
		int frameHeight = 1000;

		frame = new JFrame();
		frame.setSize(frameWidth, frameHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));		

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu File = new JMenu("File");
		menuBar.add(File);

		JMenuItem OpenFile = new JMenuItem("Open File...");
		ImageIcon openFile = new ImageIcon("pictures/openFile.png");
		OpenFile.setIcon(openFile);
		File.add(OpenFile);
		OpenFile.addActionListener(this);

		JMenuItem SaveAs = new JMenuItem("Save as...");
		ImageIcon saveAs = new ImageIcon("pictures/saveAs.png");
		SaveAs.setIcon(saveAs);
		File.add(SaveAs);
		SaveAs.addActionListener(this);

		JMenuItem Exit = new JMenuItem("Exit");
		File.add(Exit);
		Exit.addActionListener(this);
		
		JMenu Edit = new JMenu("Edit");
		menuBar.add(Edit);
		
		JMenuItem Undo = new JMenuItem("Undo");
		ImageIcon undo = new ImageIcon("pictures/undo.png");
		Undo.setIcon(undo);
		Edit.add(Undo);
		Undo.addActionListener(this);
				
		JMenuItem Redo = new JMenuItem("Redo");
		ImageIcon redo = new ImageIcon("pictures/redo.png");
		Redo.setIcon(redo);
		Edit.add(Redo);
		Undo.addActionListener(this);

		buttons = new JButton[LEDS];
		
		JPanel panel = new JPanel();							//UNTEN DRUNTER
		frame.getContentPane().add(panel);
		panel.setLayout(null);
				
		JPanel buttonPanel = new JPanel();						//CUBE
		buttonPanel.setBounds(0, 0, 800, 800);
		buttonPanel.setLayout(new GridLayout(8, 8, -1, -1));
		panel.add(buttonPanel);
		
		JPanel panel_2 = new JPanel();							//MENUELEISTE
		panel_2.setBounds(900, 1000, 1800, 1200);
		panel.add(panel_2);				
		
		for (int i = 0; i < buttons.length; i++) {
			String LED = String.valueOf(i + 1); // +1 Damit die Buttons von 1-64 gezählt werden
			JButton button = new JButton(LED,grau);			
			
			button.setVerticalTextPosition(SwingConstants.TOP);
			//button.setHorizontalTextPosition(SwingConstants.CENTER);
			button.addActionListener(this);
			button.setMnemonic(LED.charAt(0));

			buttons[i] = button;
			buttonPanel.add(buttons[i]);
		}
		
		output = new JButton("Weiter");
		output.setBounds(30,830,100,30);
		panel.add(output);
		output.addActionListener(this);
		output.setBackground(Color.white);

		ebeneup = new JButton("Ebene hoch");
		ebeneup.setBounds(130,830,100,30);
		panel.add(ebeneup);
		ebeneup.addActionListener(this);
		ebeneup.setBackground(Color.green);

		ebenedown = new JButton("Ebene runter");
		ebenedown.setBounds(230,830,120,30);
		panel.add(ebenedown);
		ebenedown.addActionListener(this);
		ebenedown.setBackground(Color.red);

		reset= new JButton("Reset");
		reset.setBounds(350,830,100,30);
		panel.add(reset);
		reset.addActionListener(this);
		reset.setBackground(Color.white);
		
		int displayEbene = CurrentEbene + 1;
		CurrentEbenetext = new JLabel("Ebene = " + displayEbene);
		CurrentEbenetext.setBounds(480,830,100,50);
		panel.add(CurrentEbenetext);
		
		 	final DefaultListModel model = new DefaultListModel(); 
			final JList list = new JList(model);		
		    list.setBounds(810,100,250,200);
		    list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		    panel.add(list);
		    
		    final DefaultListModel model2 = new DefaultListModel(); 
			final JList list2 = new JList(model2);		
		    list2.setBounds(810,375,250,200);
		    list2.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		    panel.add(list2);
		    
		    final DefaultListModel model3 = new DefaultListModel(); 
			final JList list3 = new JList(model3);		
		    list3.setBounds(810,650,250,200);
		    list3.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		    panel.add(list3);
		
		JButton addButton= new JButton("Save current frame");
		addButton.setBounds(810,10,250,80);
		panel.add(addButton);
		addButton.addActionListener(this);
		addButton.setBackground(Color.white);
		
	    JButton addToCombiner = new JButton("Add to combiner");
		addToCombiner.setBounds(810,340,250,30);
		addToCombiner.setBackground(Color.green);
		panel.add(addToCombiner);	 
		
		JButton addToVideo = new JButton("Add to video");
		addToVideo.setBounds(810,615,250,30);
		addToVideo.setBackground(Color.green);
		panel.add(addToVideo);
		
		JButton saveVid = new JButton("Save video as...");
		saveVid.setBounds(810,890,250,30);
		saveVid.setBackground(Color.green);
		panel.add(saveVid);
		    	    
	    JButton removeButton = new JButton("Remove frame");
	    removeButton.setBounds(810,305,250,30);
	    removeButton.setBackground(Color.red);
	    panel.add(removeButton);
	    
	    JButton removeButton2 = new JButton("Remove element");
	    removeButton2.setBounds(810,580,250,30);
	    removeButton2.setBackground(Color.red);
	    panel.add(removeButton2);
	    
	    JButton removeButton3 = new JButton("Remove video");
	    removeButton3.setBounds(810,855,250,30);
	    removeButton3.setBackground(Color.red);
	    panel.add(removeButton3);
	    	   	    	   	    
	    for (int i = 0; i < 8; i++) {
	        model.addElement("Frame " + i);
	    }
	    
	    addButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	
	          model.addElement("Frame " + counter);
	          counter++;
	        }
	      });
	    
	      removeButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        		        		        	
	        	model.remove(list.getSelectedIndex());											        		        		        		        						
	        }       		        	
	      });
	      
	      removeButton2.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		        		        		        	
		        	model2.remove(list2.getSelectedIndex());											        		        		        		        						
		        }       		        	
		      });
	      
	      removeButton3.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		        		        		        	
		        	model3.remove(list3.getSelectedIndex());											        		        		        		        						
		        }       		        	
		      });
	      
	      addToCombiner.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		        
		        	Object selectedItem = list.getSelectedValue();		        			    
		        	model2.addElement("Importieres  " + selectedItem);  
		         		          
		        }
		      });
	      
	      addToVideo.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		        
		        	Object selectedItem = list2.getSelectedValue();		        			    
		        	model3.addElement("Importieres Video  mit: " + selectedItem);  
		         		          
		        }
		      });
	      
	      saveVid.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		        
		        	Object selectedItem = list3.getSelectedValue();		        			    
		        	  
		         		          
		        }
		      });
	      	      	      
	      
		     		      		       		
		    
	}

	public void actionPerformed(ActionEvent e) {
		
		
		FileManager getdatafile = new FileManager();		//KONSTRUKTOREN //////////////////////////		
		Undo undo = new Undo();
		
		String quelle = e.getActionCommand();		
		
		switch (quelle) {
		case "Exit":
			System.exit(0);
			break;
			
		case "Open File...":
			matrix = getdatafile.openFileArray(); // Array wird empfangen
			EbeneUpdate();
			break;
			
		case "Save as...":
			getdatafile.SaveArraytoFile(matrix);
			break;
			
		case "Undo":
			matrix=undo.undoTempToMatrix();
			EbeneUpdate();
			break;
			
		case "Redo":
			EbeneUpdate();
			break;
			
		default:
			break;
		}
		
		ButtonPanelActionListener(quelle); // übergibt string "quelle" an methode ButtonPanelActionListener
	}


	public void ButtonPanelActionListener(String quelle) { // actionlistener um herasuzufinden welcher button gedr�ckt
															// wurde. jeder Button Teilt sich einen ActionListener
		Undo undo = new Undo();								
		int zahl = 0;

		 if (quelle.length()<3) {
								
				zahl=Integer.parseInt(quelle)-1;
				buttons[zahl].setIcon(blau);

				if (!geklickt[zahl]) {
					geklickt[zahl] = true;				
					an_aus = true;
					
				} else {
					geklickt[zahl] = false;
					buttons[zahl].setIcon(grau);
				}

			}
		
		switch (quelle) {
		
		case "Reset":
			undo.undoMatrixToTemp(matrix);
			MatrixInit();
			EbeneUpdate();
			break;
		case "Weiter":
			SimpleDataSending netsend = new SimpleDataSending();
			Stoppuhr time=new Stoppuhr();
			time.StartTimer();
			try {
				netsend.Stringbuilder(matrix,1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Gesamte Zeit"+time.StoppTimer()+"ms");
			break;
			
		case "Ebene hoch":
			if (CurrentEbene < 7) {
				CurrentEbene++;
				EbeneUpdate();
			}
			break;
			
		case "Ebene runter":
			if (CurrentEbene > 0) {
				CurrentEbene--;
				EbeneUpdate();
			}
			break;
			
		default:
			break;
		}
				
		ButtonState(geklickt[zahl], zahl);	//alle LEDs aktualisieren
	}

	void ButtonState(boolean state, int button_nr) { // Updated Button State Array
		int CurrentOffset = (CurrentEbene) * 64;

		matrix[button_nr + CurrentOffset] = state;
	}

	void EbeneUpdate() { // Methode die die Buttons updated
		
		CurrentEbenetext.setText("Ebene = " + Integer.toString(CurrentEbene + 1));// Display der derzeitigen Ebene
		int CurrentOffset = (CurrentEbene) * 64;// Button Offset

		for (int i = 0; i < 64; i++) {
			
			if (matrix[i + CurrentOffset]) // Findet den Status der Knöpfe heraus
			{
				geklickt[i] = true;
				//buttons[i].setBackground(new Color(0, 100, 255)); // Setzt den Button entsprechend
				buttons[i].setIcon(blau);
			} else {
				geklickt[i] = false;
				buttons[i].setIcon(grau);// Setzt den Button entsprechend
			}
		}
	}
	
	
	void MatrixInit()
	{
		for (int matrixsetup = 0; matrixsetup <512; matrixsetup++) {// Setting up the the save array
			matrix[matrixsetup] = false;
			
		}
	}

}
