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
	 private JButton output;
	 private boolean[] geklickt = new boolean[LEDS];
	 private boolean[] matrix = new boolean[512];
	 Steuerung obj = new Steuerung();
	 Boolean an_aus;
public Gui_noch_noch_ohne_vorlaufenden_Zahlen(String title) { 
	super(title);
	display = new JTextField(": Geklickt");
    display.setEditable(false);
    display.setHorizontalAlignment(JTextField.RIGHT);
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    int frameWidth = 700; 
    int frameHeight = 700;
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
    setResizable( false );
    setVisible(true);
  } // end of public Main2

public static void main(String[] args) {
	
	UIManager.put("Button.margin", new Insets(10, 10, 10, 10) ); //gibt die Form der Buttons vor
    new Gui_noch_noch_ohne_vorlaufenden_Zahlen("LED Cube Programmer Alpha v1");
  } 
  
 
public void actionPerformed(ActionEvent e)  //actionlistener um herasuzufinden welcher button gedr�ckt wurde. jeder Button Teilt sich einen ActionListener
{     JButton source = (JButton)e.getSource(); //findet raus welcher Button den EventListener ausgel�st hat
	      if (source.getActionCommand()== "Weiter") {
	    	  Matrix();
	    	  
	      }else {
	    	  source.setBackground(new Color(2,2,52));
	    	  int zahl = Integer.parseInt((source.getActionCommand()));
	    	  if (!geklickt[zahl]) {
	          geklickt[zahl] = true;  
	          display.replaceSelection(" | " + source.getActionCommand());
	          an_aus=true;
	          //obj.knopfGedrueckt(button, an_aus); 			//Gibt Knopfdaten weiter
	      }else {
	    	  geklickt[zahl] = false;
	    	  source.setBackground(new Color(255,255,255));
	      }
	    	  System.out.println("Zahl"+zahl);
	    }
	  }

void Matrix() {

	}
}
