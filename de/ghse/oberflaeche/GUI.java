package de.ghse.oberflaeche;

import de.ghse.schnittstelle.SimpleDataSending;
import de.ghse.steuerung.FileManager;
import de.ghse.steuerung.Steuerung;
import de.ghse.steuerung.Undo;
import de.ghse.werkzeuge.Stoppuhr;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.sun.prism.Image;

import javax.swing.JLabel;

public class GUI extends JFrame implements ActionListener {

	ImageIcon blau = new ImageIcon("C:\\Users\\Miles\\Desktop\\Java\\Projektarbeit\\GUI\\Icons\\BlauerPunkt.png");
	ImageIcon grau = new ImageIcon("C:\\Users\\Miles\\Desktop\\Java\\Projektarbeit\\GUI\\Icons\\GrauerPunkt.png");	
	final int LEDS = 64; // Anzahl der LEDS
	int layer = 0, countm, countn, button;
	private JButton[] buttons;
	private JLabel CurrentEbenetext;
	private JButton output;
	private JButton ebeneup;
	private JButton ebenedown;
	private JButton reset;
	private boolean[] geklickt = new boolean[LEDS];
	private boolean[] matrix = new boolean[512];
	public boolean[] matrixTemp = new boolean[512];
	private int CurrentEbene = 0;
	Steuerung obj = new Steuerung();
	Boolean an_aus;
	private JFrame frame;
	
	

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
		
		int frameWidth = 1000;
		int frameHeight = 1000;

		frame = new JFrame();
		frame.setSize(frameWidth, frameHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));		

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu File = new JMenu("File");
		menuBar.add(File);

		JMenuItem OpenFile = new JMenuItem("Open File...");
		ImageIcon openFile = new ImageIcon("openFile.png");
		OpenFile.setIcon(openFile);
		File.add(OpenFile);
		OpenFile.addActionListener(this);

		JMenuItem SaveAs = new JMenuItem("Save as...");
		ImageIcon saveAs = new ImageIcon("saveAs.png");
		SaveAs.setIcon(saveAs);
		File.add(SaveAs);
		SaveAs.addActionListener(this);

		JMenuItem Exit = new JMenuItem("Exit");
		File.add(Exit);
		Exit.addActionListener(this);
		
		JMenu Edit = new JMenu("Edit");
		menuBar.add(Edit);
		
		JMenuItem Undo = new JMenuItem("Undo");
		ImageIcon undo = new ImageIcon("undo.png");
		Undo.setIcon(undo);
		Edit.add(Undo);
		Undo.addActionListener(this);
		
		
		JMenuItem Redo = new JMenuItem("Redo");
		ImageIcon redo = new ImageIcon("redo.png");
		Redo.setIcon(redo);
		Edit.add(Redo);
		Undo.addActionListener(this);

		buttons = new JButton[LEDS];

		JPanel buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(200, 900));
		buttonPanel.setLayout(new GridLayout(9, 8, -1, -1));

		for (int i = 0; i < buttons.length; i++) {
			String LED = String.valueOf(i + 1); // +1 Damit die Buttons von 1-64 gezählt werden
			JButton button = new JButton(LED,grau);

			//button.setBackground(new Color(255, 255, 255));
			
			
			button.addActionListener(this);
			button.setMnemonic(LED.charAt(0));

			buttons[i] = button;
			buttonPanel.add(buttons[i]);
		}

		output = new JButton("Weiter");
		buttonPanel.add(output);
		output.addActionListener(this);
		output.setBackground(new Color(255, 70, 0));

		ebeneup = new JButton("Ebene hoch");
		buttonPanel.add(ebeneup);
		ebeneup.addActionListener(this);
		ebeneup.setBackground(new Color(255, 70, 0));

		ebenedown = new JButton("Ebene runter");
		buttonPanel.add(ebenedown);
		ebenedown.addActionListener(this);
		ebenedown.setBackground(new Color(255, 70, 0));

		reset= new JButton("Reset");
		buttonPanel.add(reset);
		reset.addActionListener(this);
		reset.setBackground(new Color(255, 70, 0));
		
		int displayEbene = CurrentEbene + 1;
		CurrentEbenetext = new JLabel("Ebene = " + displayEbene);
		buttonPanel.add(CurrentEbenetext);

		UIManager.put("Button.margin", new Insets(10, 10, 10, 10)); // gibt die Form der Buttons vor

		frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
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
				netsend.Stringbuilder(matrix,1000);
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
