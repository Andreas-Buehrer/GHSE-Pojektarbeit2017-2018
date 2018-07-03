package tcp_netzwerktest;

import java.io.IOException;
import java.net.UnknownHostException;

import de.ghse.schnittstelle.SimpleDataSending;

public class MusterTest {
	private SimpleDataSending netsend = new SimpleDataSending();
	
	private boolean[] matrixl=new boolean[512];
	private boolean temp=true;
	public void connecttocube()
	{	String l ="0";
		netsend.connectToServer("lel",l );
	}
	public void AnAus() {
	
	if (temp) {
	temp=false;
	}else {
	temp=true;
	}
	for (int i = 0; i < 512; i++) {
		if(temp) {
		matrixl[i]=true;
		
		}else {
		matrixl[i]=false;
		}
	}
	try {
		netsend.Stringbuilder(matrixl, 1);
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
}
