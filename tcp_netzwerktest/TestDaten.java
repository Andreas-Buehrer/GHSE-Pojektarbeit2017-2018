package tcp_netzwerktest;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import de.ghse.schnittstelle.SimpleDataSending;
import de.ghse.steuerung.FileManager;

public class TestDaten {

	static FileManager rd = new FileManager();
	int currentframe = 0;
	boolean timerActive = false;

	static boolean[][] frames = new boolean[1024][512];
	static TestDaten c = new TestDaten();
	static SimpleDataSending sds = new SimpleDataSending();

	Timer timer = null;
	int delaytime = 300;

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		// TODO Auto-generated method stub

		String ib = "0";

		frames = rd.openFileArray();
		// System.out.println(rd.getFrameCount()); // FrameAnzahl
		sds.connectToServer("192.168.2.6", 23); // Zum Server Connecten

		c.RunVideo(frames); // video abspielen
		
	}

	public void RunVideo(boolean frame[][]) throws UnknownHostException, IOException, InterruptedException {
		
		

		

		boolean video[][];
		video = frame;

		int allframes = rd.getFrameCount();
		
		

			currentframe++;

			// Timer Objekt zuweisen
			timer = new Timer();

			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {

					sds.stringbuilder(video[currentframe], 1); // Senden des Videos mit Methode aus SimpleDataSending

					currentframe++;// FrameCounter erhöhen

					// Wenn die Gesamtanzahl der Frames mit dem derzeitigen Frame übereinstimmt,
					// wird der Timer gestoppt
					if (currentframe == allframes) {
						timer.cancel();
						timer.purge();
						timer = null;
						currentframe = 0;
						timerActive = false;
						sds.disconnect();
						
					}
				}
			}, 100, delaytime); // 100 ms nachdem der Timer aufgesetzt wurde wir die Timer Methode aufgerufen
								// delaytime ist die Pause zwischen dem ausführen

		
	}

}
