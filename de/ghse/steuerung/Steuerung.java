package de.ghse.steuerung;

import de.ghse.schnittstelle.Schnittstelle;

public class Steuerung {
	

	
	short data_leds[][] = new short[9][9]; 
	//zahlen (an_aus) von 1-255 sollen reingeladen werden 

	Schnittstelle schnitttigesache = new Schnittstelle();
	boolean ledfeld[][][] = new boolean[9][9][9];
	public void knopfGedrueckt(int buttons,boolean an_aus){
	String Status;
	
	
	int LED,rest,rest2,z,y,x;
    do {
      //System.out.println("Geben Sie eine Zahl zwischen 1 und 511 ein:"+buttons);
      LED =buttons;
    } while (LED<1 || LED>512); // end of do-while
    
    if (LED%64==0) {
      rest=0;
      z=(LED-rest)/64;
      y=8;
      x=8;
    } // end of if else 
    else {
      rest = LED%64;
      z = (LED-rest)/64+1;
      rest2 = LED%8;
      y= ((LED%64)-rest2) /8+1;
      if (LED%8==0) {
        x = 8;
        y= ((LED%64)-rest2) /8;
      } // end of if
      else {
        x = (LED%64)%8;
      } // end of if-else
      
    } // end of if-else
	System.out.println("Ebene "+z+" Reihe "+y+" Stelle "+x+" Satus "+an_aus);
	
	
		ledfeld[z][y][x]=an_aus;
    
    
	
	
		
	}
	public void setupArray() 
	{
		for (int ebene = 1; ebene <= 8; ebene++) {
			for (int reihe = 1; reihe <= 8; reihe++) {
				
				for (int stelle = 1; stelle <= 8; stelle++) {
					
					ledfeld[ebene][reihe][stelle]=false;
				}
				
			}
		}
	}
	public void uebertragen() {
		schnitttigesache.get2dArray(ledfeld);//Mit dieser Methode wird ein 2Dimensionales array erstellt
		
	}
	
}