package tcp_netzwerktest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCP_Client {

	public TCP_Client() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		String sentence;
	      String modifiedSentence;
	      Socket clientSocket = new Socket("localhost", 6789); // new Socket("192.168.1.100", 80);
	      System.out.println("Enter your ASCII code here");
	      BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
	      sentence = inFromUser.readLine();
	      System.out.println(sentence);

	          while(!(sentence.isEmpty()))
	          {          
	              DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
	              String capitalizedSentence = sentence.toUpperCase() + '\n';
	              outToServer.writeBytes(capitalizedSentence);
	              outToServer.flush();
	            

	              BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	              modifiedSentence = inFromServer.readLine();

	                  while(!(modifiedSentence.isEmpty()))
	                  {                   
	                      System.out.println("FROM SERVER: " + modifiedSentence);
	                      break;
	                  }

	              System.out.println("Enter your ASCII code here");
	              inFromUser = new BufferedReader( new InputStreamReader(System.in));
	              sentence = inFromUser.readLine();
	          }

	      System.out.println("socket connection going to be closed");    
	      clientSocket.close();
	}

}
