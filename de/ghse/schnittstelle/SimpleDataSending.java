package de.ghse.schnittstelle;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

import de.ghse.werkzeuge.Stoppuhr;

public class SimpleDataSending {

	public void Stringbuilder(boolean[] matrix, int iterationsIndex) throws UnknownHostException, IOException {
		
		Stoppuhr time = new Stoppuhr();
		time.StartTimer();
		Socket clientSocket = new Socket("192.168.2.5", 23); // Serveradresse wird hier angegeben beim simulieren
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
																											
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); // Referenz um ausgehenden Datenverkehr zu kontrollieren
			outToServer.writeBytes(sendstring);

			// String rueckgabewert = inFromServer.readLine();

			// System.out.println(rueckgabewert);
			System.out.println("Uebertragungszeit " + time.StoppTimer() + "ms");

		}
		clientSocket.close();
	}
}
