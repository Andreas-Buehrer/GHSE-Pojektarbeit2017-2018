package de.ghse.oberflaeche;

import de.ghse.steuerung.Steuerung;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;


/**
  Der erste Code von Dennis, er soll ein Array f�r jedes Layer erzeugen und dann in ein Array schreiben. So wird das mit jedem layer gemacht.
  */
 class Gui_noch_noch_ohne_vorlaufenden_Zahlen extends JFrame implements ActionListener {
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
	 
public Gui_noch_noch_ohne_vorlaufenden_Zahlen(String title) { 
	super(title);
	
	for (int matrixsetup = 0; matrixsetup < 513; matrixsetup++) {// Setting up the the save array
		matrix[matrixsetup]=false;
		
	}
	
	
	display = new JTextField(": Geklickt");
    display.setEditable(false);
    display.setHorizontalAlignment(JTextField.RIGHT);
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    int frameWidth = 1000; 
    int frameHeight = 1000;
    setSize(frameWidth, frameHeight);
    setResizable(false);
    buttons = new JButton[LEDS];
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout( new GridLayout(9,8,15,15));
    for (int i = 0; i < buttons.length; i++)
    {
        String LED = String.valueOf(i+1);  //+1 Damit die Buttons von 1-64 gezählt werden
        JButton button = new JButton(LED);
        button.setBackground(new Color(255,255,255));
        button.addActionListener( this );
        button.setMnemonic( LED.charAt(0) );
        buttons[i] = button;
        buttonPanel.add( button );
    }
    output = new JButton("Weiter");
    buttonPanel.add(output);
    output.addActionListener(this);
    output.setBackground(new Color(255,0,255));
    getContentPane().add(display, BorderLayout.NORTH); // Erzeugt das Ausgabefeld im fenster
    getContentPane().add(buttonPanel);
    
    ebeneup = new JButton("UP");
    buttonPanel.add(ebeneup);
    ebeneup.addActionListener(this);
    ebeneup.setBackground(new Color(255,0,255));
    getContentPane().add(display, BorderLayout.NORTH); // Erzeugt das Ausgabefeld im fenster
    getContentPane().add(buttonPanel);
    
    ebenedown = new JButton("DOWN");
    buttonPanel.add(ebenedown);
    ebenedown.addActionListener(this);
    ebenedown.setBackground(new Color(255,0,255));
    getContentPane().add(display, BorderLayout.NORTH); // Erzeugt das Ausgabefeld im fenster
    getContentPane().add(buttonPanel);
    int displayEbene=CurrentEbene+1;
    CurrentEbenetext= new JTextField("Ebene = "+displayEbene);
    
    buttonPanel.add(CurrentEbenetext);
    
    setResizable( false );
    setVisible(true);
  } // end of public Main2

public static void main(String[] args) {
	
	UIManager.put("Button.margin", new Insets(10, 10, 10, 10) ); //gibt die Form der Buttons vor
    new Gui_noch_noch_ohne_vorlaufenden_Zahlen("LED Cube Programmer Alpha v1");
  } 
  
 
public void actionPerformed(ActionEvent e)  //actionlistener um herasuzufinden welcher button gedr�ckt wurde. jeder Button Teilt sich einen ActionListener
{   int zahl=0;  
	JButton source = (JButton)e.getSource(); //findet raus welcher Button den EventListener ausgel�st hat
	      if (source.getActionCommand()== "Weiter") {
	    	
	    	
	    	  
	    	  
	      }else if (source.getActionCommand()=="UP") {//UP Knopf
	    	  if(CurrentEbene<7)
	    		  {
	    		  CurrentEbene++;
	    		  EbeneUpdate();
	    		  }
	      }else if (source.getActionCommand()=="DOWN") {//DOWN Knopf
	    	  if(CurrentEbene>0)
	    	  {
	    	  CurrentEbene--;
	    	  EbeneUpdate();
	    	  }
	      }else {
			
		
	    	  source.setBackground(new Color(2,2,52));
	    	  zahl = Integer.parseInt((source.getActionCommand()))-1;
	    	  if (!geklickt[zahl]) {
	          geklickt[zahl] = true;  
	          display.replaceSelection(" | " + source.getActionCommand());
	          an_aus=true;
	          //obj.knopfGedrueckt(button, an_aus); 			//Gibt Knopfdaten weiter
	      }else {
	    	  geklickt[zahl] = false;
	    	  source.setBackground(new Color(255,255,255));
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
void Matrix() {
	
}
}
