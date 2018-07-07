package de.ghse.schnittstelle;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;



public class SimpleDataSending {
	
	//Globales Socket Objekt
	Socket clientSocket = null;
    BufferedReader inFromServer = null;
    DataOutputStream outToServer=null;
    int counter=0;

    // Disconnect from Server
	public void disconnect() {
		try {
			clientSocket.close();
			clientSocket=null;
			inFromServer.close();
			inFromServer=null;
			outToServer.close();
			outToServer=null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	// Connect To Server
	public void connectToServer(String ip,int port) {
		try {
			clientSocket = new Socket(ip, port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
		} // Serveradresse wird hier angegeben beim simulieren

        try {
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try
        {
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
           

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

	// Stringbuilder mit Frame Daten und wiederholungsIndex
	public void stringbuilder(boolean[] matrix, int iterationsIndex) {


		for (int iterations = 0; iterations < iterationsIndex; iterations++) {

			int sendchar = 0, multiplierIndex = 0;

			short[] multiplier = { 128, 64, 32, 16, 8, 4, 2, 1 };


			char[] chardataarray = new char[65];
			chardataarray[64] = '\n';

			// Umwandeln des boolean Arrays in einen 64 Character String
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

			// String bauen
			String sendstring = new String(chardataarray);
			//ystem.out.println(sendstring);




            // String senden
            try {
            	outToServer.writeBytes(sendstring);
            } catch (IOException e) {
                e.printStackTrace();
            }



           
            try {
                String rueckgabewert = inFromServer.readLine(); // Input vom Cube damit die Daten nicht zu schnell gesendet werden
            } catch (IOException e) {
                e.printStackTrace();
            }
            counter++;
            //System.out.println(rueckgabewert);
            System.out.println("Senden"+counter);

		}

	}
}

