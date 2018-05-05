package de.ghse.oberflaeche;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import de.ghse.schnittstelle.SimpleDataSending;
import de.ghse.steuerung.FileManager;
import de.ghse.steuerung.Steuerung;
import de.ghse.steuerung.Undo;
import de.ghse.werkzeuge.Stoppuhr;

public class GUI extends JFrame implements ActionListener {

  ImageIcon blau = new ImageIcon("pictures/BlauerPunkt.png");
  ImageIcon grau = new ImageIcon("pictures/GrauerPunkt.png"); 
  int layer = 0, countm, countn, button,counter=12,selectedItem,anzahlItems = 0,frameNummer = 0,CurrentEbene = 0,LEDS = 64,videoZaehler=0;  
  private JButton[] buttons;
  private JLabel CurrentEbenetext;
  private JButton output,ebeneup,ebenedown,reset,allOn;
  private boolean[] geklickt = new boolean[LEDS];
  private boolean[] matrix = new boolean[512];
  public boolean[] matrixTemp = new boolean[512];
  public boolean[] matrixAnzeige = new boolean[512];
  public boolean[][] matrixArray = new boolean[512][15];  //max 15 Frames können gespeichert werden   
  public Boolean an_aus;
  private JFrame frame;
  public JTextField textField;
  public JList list;
  final DefaultListModel model = new DefaultListModel();
  final DefaultListModel model2 = new DefaultListModel(); 
  final DefaultListModel model3 = new DefaultListModel(); 
  
  SimpleDataSending netsend = new SimpleDataSending();  //Konstruktoren
  FileManager getdatafile = new FileManager();    
  Steuerung obj = new Steuerung();
  Stoppuhr time = new Stoppuhr();
  Undo undo = new Undo(); 
    

  /**
   * Launch the application.
   */

  public static void main(String[] args) {    
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          GUI window = new GUI();
          window.frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the application.
   */
  public GUI() {
    initialize();
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {
    
    MatrixInit();
    
    int frameWidth = 1920;
    int frameHeight = 1042;

    frame = new JFrame();
    frame.setSize(frameWidth, frameHeight);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     

    JMenuBar menuBar = new JMenuBar();
    frame.setJMenuBar(menuBar);

    JMenu File = new JMenu("File");
    menuBar.add(File);

    JMenuItem OpenFile = new JMenuItem("Open File . . .");
    ImageIcon openFile = new ImageIcon("pictures/openFile.png");
    OpenFile.setIcon(openFile);
    File.add(OpenFile);
    OpenFile.addActionListener(this);

    JMenuItem SaveAs = new JMenuItem("Save as . . .");
    ImageIcon saveAs = new ImageIcon("pictures/saveAs.png");
    SaveAs.setIcon(saveAs);
    File.add(SaveAs);
    SaveAs.addActionListener(this);

    JMenuItem Exit = new JMenuItem("Exit");
    File.add(Exit);
    Exit.addActionListener(this);
    
    JMenu Edit = new JMenu("Edit");
    menuBar.add(Edit);
    
    JMenuItem Undo = new JMenuItem("Undo reset");
    ImageIcon undo = new ImageIcon("pictures/undo.png");
    Undo.setIcon(undo);
    Edit.add(Undo);
    Undo.addActionListener(this);
                    
    JPanel panel = new JPanel();              //UNTEN DRUNTER
    frame.getContentPane().add(panel);
    panel.setLayout(null);
        
    JPanel buttonPanel = new JPanel();            //CUBE
    buttonPanel.setBounds(0, 0, 800, 800);
    buttonPanel.setLayout(new GridLayout(8, 8, -1, -1));
    panel.add(buttonPanel);
    
    JPanel panel_2 = new JPanel();              //MENUELEISTE
    panel_2.setBounds(900, 1000, 1800, 1200);
    panel.add(panel_2);       
    
    buttons = new JButton[LEDS];
    
    for (int i = 0; i < buttons.length; i++) {
      
      String LED = String.valueOf(i + 1);      // +1 Damit die Buttons von 1-64 gezaehlt werden
      JButton button = new JButton(LED,grau);     
      
      button.setVerticalTextPosition(SwingConstants.TOP);
      button.addActionListener(this);
      button.setMnemonic(LED.charAt(0));
      buttons[i] = button;
      buttonPanel.add(buttons[i]);
    }
    
    output = new JButton("Weiter");
    output.setBounds(30,830,100,30);    
    output.addActionListener(this);
    output.setBackground(Color.white);
    panel.add(output);

    ebeneup = new JButton("Ebene hoch");
    ebeneup.setBounds(130,830,100,30);    
    ebeneup.addActionListener(this);
    ebeneup.setBackground(Color.green);
    panel.add(ebeneup);

    ebenedown = new JButton("Ebene runter");
    ebenedown.setBounds(230,830,120,30);    
    ebenedown.addActionListener(this);
    ebenedown.setBackground(Color.red);
    panel.add(ebenedown);

    reset= new JButton("Reset");
    reset.setBounds(350,830,100,30);    
    reset.addActionListener(this);
    reset.setBackground(Color.white);
    panel.add(reset);
    
    allOn = new JButton("All on");
    allOn.setBounds(30,870,100,30);    
    allOn.addActionListener(this);
    allOn.setBackground(Color.white);
    panel.add(allOn);
    
    int displayEbene = CurrentEbene + 1;
    CurrentEbenetext = new JLabel("Ebene = " + displayEbene);
    CurrentEbenetext.setBounds(480,820,100,50);
    panel.add(CurrentEbenetext);
        
       
    final JList list = new JList(model);    
    list.setBounds(810,100,250,235);
    list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    panel.add(list);
                 
    final JList list2 = new JList(model2);    
    list2.setBounds(810,375,250,235);
    list2.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    panel.add(list2);
              
    final JList list3 = new JList(model3);    
    list3.setBounds(810,650,250,235);
    list3.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    panel.add(list3);
       
    
    JButton addButton= new JButton("Save current frame");
    addButton.setBounds(810,3,250,57);
    panel.add(addButton);
    addButton.addActionListener(this);
    addButton.setBackground(Color.white);
    
    JButton addToCombiner = new JButton("Add to combiner");
    addToCombiner.setBounds(810,340,250,30);
    addToCombiner.setBackground(Color.green);
    panel.add(addToCombiner);  
    
    JButton addToVideo = new JButton("Combine and create video");
    addToVideo.setBounds(810,615,250,30);
    addToVideo.setBackground(Color.green);
    panel.add(addToVideo);
                               
    JButton deselectButton = new JButton("Save & Deselect item");
    deselectButton.setBounds(810,65,250,30);
    deselectButton.setBackground(Color.gray);
    panel.add(deselectButton);
                  
     
      final JPopupMenu popupMenu = new JPopupMenu();           
      JMenuItem save = new JMenuItem("Save as...");
      JMenuItem rename = new JMenuItem("Rename");
      JMenuItem delete = new JMenuItem("Delete");
      popupMenu.add(delete);
      popupMenu.add(rename);
      popupMenu.add(save);
      
      final JPopupMenu popupMenu2 = new JPopupMenu();           
      JMenuItem save2 = new JMenuItem("Save as...");
      JMenuItem rename2 = new JMenuItem("Rename");
      JMenuItem delete2 = new JMenuItem("Delete");
      popupMenu2.add(delete2);
      popupMenu2.add(rename2);
      popupMenu2.add(save2);
           
      
      list.addMouseListener(new MouseAdapter() {
          public void mouseClicked(java.awt.event.MouseEvent me) {
          // if right mouse button clicked (or me.isPopupTrigger())
          if (SwingUtilities.isRightMouseButton(me)
          && !list.isSelectionEmpty()
          && list.locationToIndex(me.getPoint())
          == list.getSelectedIndex()) {
          popupMenu.show(list, me.getX(), me.getY());
                }
              }
          }
          );
      save.addActionListener(this);
      rename.addActionListener(this);
      delete.addActionListener(this);
      
      list2.addMouseListener(new MouseAdapter() {
          public void mouseClicked(java.awt.event.MouseEvent me) {
          // if right mouse button clicked (or me.isPopupTrigger())
          if (SwingUtilities.isRightMouseButton(me)
          && !list2.isSelectionEmpty()
          && list2.locationToIndex(me.getPoint())
          == list2.getSelectedIndex()) {
          popupMenu2.show(list2, me.getX(), me.getY());
                }
              }
          }
          );
      save2.addActionListener(this);
      rename2.addActionListener(this);
      delete2.addActionListener(this);
      
      
      deselectButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            
            int index = list.getSelectedIndex();    // get item number
                                      
            for (int i = 0; i <= 511; i++) {               
                matrixArray[i][index+1] = matrix[i];                    
        }
            
            list.clearSelection();
            list2.clearSelection();
            list3.clearSelection();
            MatrixInit();
            EbeneUpdate();
                        
          }
        });
          
      list.addListSelectionListener(new ListSelectionListener() {         //wenn item angeklickt wird
            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) { 
                                                    
                  int index = list.getSelectedIndex();          // get item number  
                  list2.clearSelection();
                list3.clearSelection();
                
                    for (int i = 0; i <= 511; i++) {               
                            matrix[i] = matrixArray[i][index+1];                              
                  }
                          EbeneUpdate();                                                        
                }
            }
        });
      
      list2.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) { 
                                                    
                  int index = list2.getSelectedIndex();     // get item number
                  list.clearSelection();
                list3.clearSelection();
                  
                  for (int i = 0; i <= 511; i++) {               
                    matrix[i] = matrixArray[i][index+1];    
                    
          }
                  EbeneUpdate();
                  
                }
            }
        });
      
      list3.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) { 
                                                    
                  int index = list3.getSelectedIndex();     // get item number
                  list.clearSelection();
                list2.clearSelection();
                  
                  for (int i = 0; i <= 511; i++) {               
                    matrix[i] = matrixArray[i][index+1];    
                    
          }
                  EbeneUpdate();
                  
                }
            }
        });
          
      addButton.addActionListener(new ActionListener() {    
          public void actionPerformed(ActionEvent e) {
                                    
            frameNummer++;            
            
            for (int j = 0; j <= 511; j++) {
              matrixArray[j][frameNummer] = matrix[j];  //die jetzige matrix wird in ein weiteres Array gespewichert und kann immer wieder abgerufen werden. 
        } 
            
            model.addElement("Frame " + frameNummer);          
            
            MatrixInit();
            EbeneUpdate();
   
          }
        });
      
        
        addToCombiner.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            
              Object selectedItem = list.getSelectedValue();
              int index = list.getSelectedIndex();
              
              if (selectedItem != null) {               //nur wenn ein item ausgewählt wurde  
                model2.setSize(15);                                                                       
                model2.set(index, "Importiertes Frame:   " + selectedItem);
          }                                       
            }
          });
        
        addToVideo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {                          
              videoZaehler++;
              model3.addElement("Video" + videoZaehler);
              model2.removeAllElements();
                        
            }
          });
        
                                                
      list.addMouseListener(new MouseAdapter() {
          public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
              int index = list.locationToIndex(e.getPoint());
              Object item = model.getElementAt(index);
              String text = JOptionPane.showInputDialog("Rename item", item);
              String newitem = "";
              if (text != null)
                newitem = text.trim();
              else
                return;

              if (!newitem.isEmpty()) {
                model.remove(index);
                model.add(index, newitem);
                ListSelectionModel selmodel = list.getSelectionModel();
                selmodel.setLeadSelectionIndex(index);
              }
            }
          }
        }); 
      
      list2.addMouseListener(new MouseAdapter() {
          public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
              int index = list2.locationToIndex(e.getPoint());
              Object item = model2.getElementAt(index);
              String text = JOptionPane.showInputDialog("Rename item", item);
              String newitem = "";
              if (text != null)
                newitem = text.trim();
              else
                return;

              if (!newitem.isEmpty()) {
                model2.remove(index);
                model2.add(index, newitem);
                ListSelectionModel selmodel = list2.getSelectionModel();
                selmodel.setLeadSelectionIndex(index);
              }
            }
          }
        }); 
      
      rename.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            ListSelectionModel selmodel = list.getSelectionModel();
            int index = selmodel.getMinSelectionIndex();
            if (index == -1)
              return;
            Object item = model.getElementAt(index);
            String text = JOptionPane.showInputDialog("Rename frame", item);
            String newitem = null;

            if (text != null) {
              newitem = text.trim();        
            } else
              return;

            if (!newitem.isEmpty()) {
              model.remove(index);
              model.add(index, newitem);
            }
          }
        });
      
      rename2.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            ListSelectionModel selmodel = list2.getSelectionModel();
            int index = selmodel.getMinSelectionIndex();
            if (index == -1)
              return;
            Object item = model2.getElementAt(index);
            String text = JOptionPane.showInputDialog("Rename frame", item);
            String newitem = null;

            if (text != null) {
              newitem = text.trim();        
            } else
              return;

            if (!newitem.isEmpty()) {
              model2.remove(index);
              model2.add(index, newitem);
            }
          }
        });
        
      delete.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent event) {
            ListSelectionModel selmodel = list.getSelectionModel();
            int index = selmodel.getMinSelectionIndex();
            if (index >= 0)
              model.remove(index);
          }

        });
      
      delete2.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent event) {
            ListSelectionModel selmodel = list2.getSelectionModel();
            int index = selmodel.getMinSelectionIndex();
            if (index >= 0)
              model2.remove(index);
          }

        });
                
  } //ende initialize
 
  
  public void actionPerformed(ActionEvent e) {        
  
    String quelle = e.getActionCommand();   
    
    switch (quelle) {
    case "Exit":
      System.exit(0);       //exit program
      break;
      
    case "Open File . . .":
      matrix = getdatafile.openFileArray(); // Array wird empfangen
      EbeneUpdate();
      break;
      
    case "Save as . . .":
      getdatafile.SaveArraytoFile(matrix);
      break;
      
    case "Undo reset":
      matrix=undo.undoTempToMatrix();
      EbeneUpdate();
      break;          
      
    case "Save as...":
      getdatafile.SaveArraytoFile(matrix);
      break;
      
    case "All on":
        MatrixAn();
        EbeneUpdate();
        break;  
                     
    default:
      break;
    }
    
    ButtonPanelActionListener(quelle); // Uebergibt string "quelle" an methode ButtonPanelActionListener
  }


  public void ButtonPanelActionListener(String quelle) { // actionlistener um herasuzufinden welcher button gedrueckt wurde
        
    int zahl = 0;

     if (quelle.length()<3) {     //dann ist es eine LED
                
        zahl=Integer.parseInt(quelle)-1;
        buttons[zahl].setIcon(blau);

        if (!geklickt[zahl]) {
          geklickt[zahl] = true;        
          an_aus = true;
          
        } else {
          geklickt[zahl] = false;
          buttons[zahl].setIcon(grau);
        }

      }
    
    switch (quelle) {
    
    case "Reset":
      undo.undoMatrixToTemp(matrix);
      MatrixInit();
      EbeneUpdate();
      break;
    case "Weiter":
      time.StartTimer();
      try {
        netsend.Stringbuilder(matrix,1);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      System.out.println(" Gesamte Zeit"+time.StoppTimer()+"ms");
      break;
      
    case "Ebene hoch":
      if (CurrentEbene < 7) {
        CurrentEbene++;
        EbeneUpdate();
      }
      break;
      
    case "Ebene runter":
      if (CurrentEbene > 0) {
        CurrentEbene--;
        EbeneUpdate();
      }
      break;
      
    default:
      break;
    }
        
    ButtonState(geklickt[zahl], zahl);  //alle LEDs aktualisieren
  }

  void ButtonState(boolean state, int button_nr) { // Updated Button State Array
    int CurrentOffset = (CurrentEbene) * 64;

    matrix[button_nr + CurrentOffset] = state;
  }

  void EbeneUpdate() {        // Methode die die Buttons der sichtbaren ebene updated
    
    CurrentEbenetext.setText("Ebene = " + Integer.toString(CurrentEbene + 1));// Display der derzeitigen Ebene
    int CurrentOffset = (CurrentEbene) * 64;// Button Offset

    for (int i = 0; i < 64; i++) {
      
      if (matrix[i + CurrentOffset]) // Findet den Status der Knoepfe heraus
      {
        geklickt[i] = true;
                      
        buttons[i].setIcon(blau);
      } else {
        geklickt[i] = false;
        buttons[i].setIcon(grau);// LED aus also graues png aufsetzen
      }
    }
  }
  
  
  void MatrixInit(){
    for (int matrixsetup = 0; matrixsetup <512; matrixsetup++) {// Setting up the the save array
      matrix[matrixsetup] = false;    
    }
  }

  void MatrixAn(){
	    for (int matrixsetup = 0; matrixsetup <512; matrixsetup++) {// Setting up the the save array
	      matrix[matrixsetup] = true;    
	    }
	  }
}
