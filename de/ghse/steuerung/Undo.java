package de.ghse.steuerung;

public class Undo {
  
  private static boolean[] matrixTemp = new boolean[512];
  private static boolean[] matrix = new boolean[512];
  
  public boolean[] undoMatrixToTemp(boolean matrix[]) {   //uebertraegt matrix zu matrixTemp
    
    for (int i = 0; i <= 511; i++) {      //matrix in neue variable uebertragen
      matrixTemp[i]=matrix[i];
    }   
    return matrix;
  }
  

  public boolean[] undoTempToMatrix() {     //uebertraegt matrix zu matrixTemp
    
    for (int i = 0; i <= 511; i++) {      //matrix in neue variable uebertragen
      matrix[i]=matrixTemp[i];
    }   
    return matrix;
  }

}
