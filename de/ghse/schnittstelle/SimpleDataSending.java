package de.ghse.schnittstelle;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import de.ghse.werkzeuge.Stoppuhr;

public class SimpleDataSending {

	public static void connectToServer(String ipadresse,String port) {
		try {
			Socket clientSocket = new Socket("192.168.2.5", 23);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Serveradresse wird hier angegeben beim simulieren
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); // Referenz um ausgehenden Datenverkehr zu kontrollieren
	}
	public void disconnect() {
		clientSocket.close();
	}
  public void Stringbuilder(boolean[] matrix, int iterationsIndex) throws UnknownHostException, IOException {
    
    Stoppuhr time = new Stoppuhr();
    time.StartTimer();
    
    for (int iterations = 0; iterations < iterationsIndex; iterations++) {

      int sendchar = 0, multiplierIndex = 0;

      short[] multiplier = { 1, 2, 4, 8, 16, 32, 64, 128 };

      char[] chardataarray = new char[65];
      chardataarray[64] = '\n';

      for (int i = 0; i < matrix.length; i++) {

        if (matrix[i]) {
          sendchar = sendchar + multiplier[multiplierIndex];

        }
        multiplierIndex++;
        if (multiplierIndex == 8) {
          multiplierIndex = 0;
          chardataarray[i / 8] = (char) sendchar;

          // System.out.println((int)senddata);

          sendchar = 0;

        }

      }

      String sendstring = new String(chardataarray);
      System.out.println(sendstring);
                                  
      //BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
                                                      
      
      outToServer.writeBytes(sendstring);

      // String rueckgabewert = inFromServer.readLine();

      // System.out.println(rueckgabewert);
      System.out.println("Uebertragungszeit " + time.StoppTimer() + "ms");

    }
    
  }
}
