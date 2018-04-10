package tcp_netzwerktest;

import java.io.*;
import java.net.*;
import java.net.Socket;
import java.util.Locale;



class TCPServer {
  public static void main(String argv[]) throws IOException {
    String clientSentence="1";
    String capitalizedSentence;
    ServerSocket welcomeSocket = new ServerSocket(6789);
    BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
    Socket connectionSocket = welcomeSocket.accept();
   
    while (clientSentence!=null){
      
      BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
      DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
      clientSentence = inFromClient.readLine();
      System.out.println("Received: " + clientSentence);
      
      System.out.println("Daten zum zuruecksenden");
      String clientSentence2=inFromUser.readLine();
      
      capitalizedSentence = clientSentence2.toUpperCase() + '\n';
      
      //System.out.println("Hier werden Daten vom Server zurueckgesendet.");
      outToClient.writeBytes(capitalizedSentence);
      outToClient.flush();
      
      
      
     
    }
    System.out.println("socket connection going to be closed");    ///Test
    connectionSocket.close();
  }
}