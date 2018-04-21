package de.ghse.schnittstelle;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

public class SimpleDataSending {

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		while(true)
		{
		Scanner scan =new Scanner(System.in);
		int input = scan.nextInt();
		
		}
	}

	public void Stringbuilder(boolean[] matrix) throws UnknownHostException, IOException
	{
		int sendchar=0,multiplierIndex=0;
		short[] multiplier={1,2,4,8,16,32,64,128};
		String returnString="";
		//Random r = new Random();
		//Socket clientSocket = new Socket("192.168.43.242", 23);	// Serveradresse wird hier angegeben beim simulieren muss hier localhost eingetragen werden
		//DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); // Referenz um ausgehenden Datenverkehr zu kontrollieren
		
		for (int i = 0; i < matrix.length; i++) {
			
			
			if (matrix[i])
				{
				sendchar=sendchar+multiplier[multiplierIndex];
				System.out.println(sendchar);
				}
			multiplierIndex++;
			if (multiplierIndex==8) {
				multiplierIndex=0;
				
				char senddata=(char)sendchar;
				returnString=returnString+String.valueOf(senddata);
				sendchar=0;
				
			}
			
		}
		//returnString=returnString.toUpperCase();
		returnString=returnString+'\n';
		
		System.out.println(returnString);
		
		
		
		//outToServer.writeBytes(returnString);
		
		//String rueckgabewert = inFromServer.readLine(); 
		//System.out.println(rueckgabewert);
		
		//clientSocket.close();
	
	
	}
}
