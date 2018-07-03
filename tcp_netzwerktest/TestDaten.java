package tcp_netzwerktest;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Random;

import de.ghse.schnittstelle.SimpleDataSending;
import de.ghse.steuerung.FileManager;

public class TestDaten {
	
	int offset=0;
	static int counter=0;
	static boolean[] matrix=new boolean[512];
	boolean[] matrixs=new boolean[512];
	static boolean [][]welle=new boolean[12][512];
	static TestDaten c=new TestDaten();
	static SimpleDataSending netsend1 = new SimpleDataSending();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Random r=new Random();
		String ib="0";
		MusterTest mus=new MusterTest();
		
		
		FileManager op=new FileManager();
		matrix=op.openFileArray();
		//mus.connecttocube();
		for (int i = 0; i < 12; i++) {
			System.out.println("Frame nr"+i+1);
			welle[i]=op.openFileArray();
		}
		
		netsend1.connectToServer("test",ib);
		while(true) {
			//mus.AnAus();
		c.Einsbisacht();
		c.Welle();
		counter++;
		System.out.println(counter);
			
		}
		
		
	}
	public void Welle() {
		int tes=1;
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 3; j++) {
				
			try {
				netsend1.Stringbuilder(welle[j], tes);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			
			
			
		}
		for (int j = 0; j < 5; j++) {
			for (int j2 = 3; j2 < 12; j2++) {
				try {
					netsend1.Stringbuilder(welle[j2], tes);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public void Einsbisacht()
	{	for (int f = 0; f < 8; f++) {
		
	
		if(offset>=512)
		{
			
			offset=0;
			//c.Pause();
		}
		for (int i = 0; i < matrixs.length; i++) {
			matrixs[i]=false;
			
		}
		for (int i = 0; i < 64; i++) {
			matrixs[i+offset]=matrix[i+offset];
		}
		offset=offset+64;
		try {
			netsend1.Stringbuilder(matrixs, 1);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	offset=0;
	for (int f = 7; f > -1; f--) {
		
		
		if(offset==0)
		{
			
			offset=448;
			//c.Pause();
		}
		for (int i = 0; i < matrixs.length; i++) {
			matrixs[i]=false;
			
		}
		for (int i = 0; i < 64; i++) {
			matrixs[i+offset]=matrix[i+offset];
		}
		offset=offset-64;
		try {
			netsend1.Stringbuilder(matrixs, 1);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}offset=0;
	}
	public void Pause()
	{	boolean[] matrixsv=new boolean[512];
		for (int i = 0; i < matrixsv.length; i++) {
			matrixsv[i]=true;
			
		}
		
		try {
			netsend1.Stringbuilder(matrixsv, 1);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
