package de.ghse.schnittstelle;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;


public class Schnittstelle {

	private static final DataOutputStream outToServer = null;
	String daten,rueckgabewert,capitalizedSentence;
	int daten_stellen_led[]={0,1,2,4,8,16,32,64,128};
	int data_test[][]=new int[9][9];
	int test;
	
	public void get2dArray(boolean array3d[][][])
	{
	for (int ebene = 1; ebene <= 8; ebene++) {
		for (int reihe = 1; reihe <= 8; reihe++) {
			int temp_data_reihe=0;
			for (int stelle = 1; stelle <= 8; stelle++) {
				if (array3d[reihe][ebene][stelle]) {
						temp_data_reihe=temp_data_reihe+daten_stellen_led[stelle];
				}
			}
			data_test[ebene][reihe]=temp_data_reihe;
		}
	}
	try {
		Aufbereiten(data_test);// Jede Stelle wird in einen String umgewandelt und dieser wird an die Arduino übertragen
	} catch (IOException | InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	public Schnittstelle() {
		// TODO Auto-generated constructor stub
	}

	 
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException 	//MAAAAIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIINNNNNNNNNNNN
	{
		
		
		
		Schnittstelle send=new Schnittstelle();
		String go_On;
		send.RandomLeds();
		//send.ManualSenden();
		
			System.out.println("Senden");
			//send.RandomLeds();
			//send.JedeLED();
			System.out.println("Erfolg!");
			//Thread.sleep(20000);
			for (int i = 0; i < 9; i++) {
				send.RandomDrop();
			}
			
			//System.out.println("Senden");
			//send.ResetLeds();
			//System.out.println("Erfolg!");
			//Thread.sleep(20000);
			
		
		
		
	}
	
	public void Aufbereiten(int data_led[][]) throws UnknownHostException, IOException, InterruptedException  
	{
		
		
		Socket clientSocket = new Socket("192.168.2.5", 23);	// Serveradresse wird hier angegeben beim simulieren muss hier localhost eingetragen werden
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // Eine Referenz Buffered Reade um den einkommenden Datenverkehr zu verarbeiten
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); // Referenz um ausgehenden Datenverkehr zu kontrollieren
	for (int ebene = 1; ebene <=8; ebene++) 
	{
		for (int reihe = 1; reihe <=8; reihe++) 
		{	
			//System.out.println("Wert"+data_led[ebene][reihe]);
			String spacer;
			
			if(data_led[ebene][reihe]<100)							//String muss immer 5 Zeichen lang sein
			{
				spacer="0"+Integer.toString(data_led[ebene][reihe]);
			}else
			{
				spacer=Integer.toString(data_led[ebene][reihe]);	
			}
			if(data_led[ebene][reihe]<10)							// Wenn der Datenwert nur 1 Zeichen lang ist
			{
				spacer="00"+Integer.toString(data_led[ebene][reihe]);
			}
			if(data_led[ebene][reihe]==0)
			{
				spacer="000";										// Wenn der Datenwert genau 0 ist
			}
			System.out.println(spacer);
			
			daten=Integer.toString(ebene)+Integer.toString(reihe)+spacer; // String wird zum übertragen wird generiert
			capitalizedSentence = daten.toUpperCase() + '\n';			//Der String wird in Großbuchstaben gewandelt und ein Newline wird hinzugefügt "\n"
			
			System.out.println("zu uebertagender Wert "+daten);
			outToServer.writeBytes(capitalizedSentence); // Daten werden per tcp an die 
			
			
			System.out.println("Daten senden "+capitalizedSentence);
			//outToServer.flush();
			
			
			   
			rueckgabewert = inFromServer.readLine(); 
			
			//System.out.println("Daten empfangen "+rueckgabewert );
			
			
			
            
			
			
		}	
	}
	clientSocket.close();
	
	
	}
	public void RandomLeds() throws UnknownHostException, IOException, InterruptedException
	{
		Random random = new Random();
		
		int data_test[][]=new int[9][9];
		// TODO Auto-generated method stub
		
		for (int ebene = 1; ebene <=8; ebene++) 
		{
			for (int reihe = 1; reihe <=8; reihe++) 
			{
				
				
				data_test[ebene][reihe]=random.nextInt(255);//daten_stellen_led[ebene];//wert_led;
				
				
			}
		}
		Aufbereiten(data_test);
	}
	public void RandomDrop() throws UnknownHostException, IOException, InterruptedException
	{
	Random random = new Random();
		
		
		// TODO Auto-generated method stub
		
		for (int ebene = 1; ebene <=8; ebene++) 
		{
			for (int reihe = 1; reihe <=8; reihe++) 
			{
				
				
				if(ebene==8)
				{
				data_test[ebene][reihe]=random.nextInt(255);//daten_stellen_led[ebene];//wert_led;
				}
				else
				{
					int swtichebene=ebene+1;
					data_test[ebene][reihe]=data_test[swtichebene][reihe];
					
				}
				
				
			}
		}
		Aufbereiten(data_test);
	}
	public void ResetLeds() throws UnknownHostException, IOException, InterruptedException
	{
		int data_test[][]=new int[9][9];
		// TODO Auto-generated method stub
		
		for (int ebene = 1; ebene <=8; ebene++) 
		{
			for (int reihe = 1; reihe <=8; reihe++) 
			{
				
				data_test[ebene][reihe]=0;

			}
		}
		Aufbereiten(data_test);
	}
	public void JedeLED() throws UnknownHostException, IOException, InterruptedException
	{
		int data_test[][]=new int[9][9];
		// TODO Auto-generated method stub
		
		for (int z = 1; z <=8; z++) 
		{
			for (int y = 1; y <=8; y++) 
			{
				for (int x = 1; x <=8 ; x++) 
				{
				data_test[z][y]=data_test[z][y]+daten_stellen_led[x];
				Aufbereiten(data_test);
				}
			}
		}
		
	}
	}


