package de.ghse.werkzeuge;

public class Stoppuhr {
	private long starttime,endtime;
public void StartTimer()
{
	 starttime= System.currentTimeMillis();
}
public double StoppTimer()
{
	
	endtime=System.currentTimeMillis();
	double elapsedtime=(double)(endtime-starttime);
	return elapsedtime;
}
}
