package Processing;

public class Processing {
	

	private boolean an_aus[][][] = new boolean[9][9][9]; //asd
	private short data_leds[][] = new short[9][9]; //zahlen (an_aus) von 1-255 sollen reingeladen werden 

	
	public static void knopfGedrueckt(int buttons,boolean an_aus){
	String Status;
	boolean ledfeld[][][] = new boolean[9][9][9];
	if(an_aus==true)
	{
		int zeile=buttons/8;
		int stelle=buttons%8;
		int ebene=1; //noch nicht definiert
		
		ledfeld[zeile][stelle][ebene] = true;
		Status="an";
	
	} else
	{
		int zeile=buttons/8;
		int stelle=buttons%8;
		int ebene=1; //noch nicht definiert
		
		ledfeld[zeile][stelle][ebene] = false;
		Status="aus";
	}
	
	System.out.println("Knopf Nr"+buttons+"="+Status);
	
	
	}
	

}
