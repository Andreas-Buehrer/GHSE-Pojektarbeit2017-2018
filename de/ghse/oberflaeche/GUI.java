package de.ghse.oberflaeche;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import de.ghse.schnittstelle.SimpleDataSending;
import de.ghse.steuerung.FileManager;
import de.ghse.steuerung.Steuerung;
import de.ghse.steuerung.Undo;
import de.ghse.werkzeuge.Stoppuhr;

public class GUI extends JFrame implements ActionListener {

  int layer = 0, countm, countn, button,counter=12,selectedItem,anzahlItems = 0,frameNummer = 0,CurrentEbene = 0,LEDS = 64,videoZaehler=0,zaehler=0;  
  private JButton[] buttons;
  private JLabel CurrentEbenetext,sliderLabel;
  private JButton output,ebeneup,ebenedown,reset,allOn;
  private boolean[] geklickt = new boolean[LEDS];
  private boolean[] matrix = new boolean[512];				//Anhand dieses Arrays wird das GUI "angemalt". Dies geschieht vor allem mit EbeneUpdate();
  public boolean[] matrixTemp = new boolean[512];
  public boolean[] matrixAnzeige = new boolean[512];		
  public boolean[] IndexInVideo = new boolean[15];			//mehr als 15 frames wird ein user für ein video nicht brauchen
  public boolean[][] matrixArray = new boolean[512][15];    //max 15 Frames können gespeichert werden
  public int[] TimePerFrame = new int[15];					//mehr als 15 frames wird ein user für ein video nicht brauchen
  public Boolean an_aus;
  private JFrame frame;
  public JTextField textField;
  public String ip,port;
  
  final DefaultListModel model = new DefaultListModel();	//Initialisierung der DefaultListModel muss global geschehen, damit man von ueberall Zugriff darauf hat
  final DefaultListModel model2 = new DefaultListModel(); 
  final DefaultListModel model3 = new DefaultListModel(); 
  
  final JList list = new JList(model);						//Initialisierung der JLists muss global geschehen, damit man von ueberall Zugriff darauf hat
  final JList list2 = new JList(model2); 
  final JList list3 = new JList(model3); 
  
  final JSlider slider = new JSlider(JSlider.HORIZONTAL,42,10000,1000);		//geht von 42-10000 und hat den standartwert 1000  ,  global um slider.getValue() machen zu können
  
  ImageIcon blau = new ImageIcon("pictures/BlauerPunkt.png");		//Bild blau steht fuer einen blauen Button (=LED an)
  ImageIcon grau = new ImageIcon("pictures/GrauerPunkt.png"); 		//Bild grau steht fuer einen grauen Button (=LED aus)
  
  SimpleDataSending netsend = new SimpleDataSending(); 	 			//Konstruktoren
  FileManager getdatafile = new FileManager();
  MeinZeichenPanel panel = new MeinZeichenPanel();
  Steuerung str = new Steuerung();
  Stoppuhr time = new Stoppuhr();
  Undo undo = new Undo(); 
    
  /*
   * Launch the application.
   */

  public static void main(String[] args) {    
    EventQueue.invokeLater(new Runnable() {		//Ist wichtig, damit das GUI auf Klicks weiter antworten kann
      public void run() {
        try {
          GUI window = new GUI();
          window.frame.setVisible(true);		//Es muss hier hier sichtbar (setVisible) gemacht werden, da es von ueberall sonst schwere Fehler gibt
        } catch (Exception e) {
          e.printStackTrace();	//nichts tun
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
   * Initialisiere den Inhalt des Frames
   */
  private void initialize() {
    
    MatrixAus();							  //alle LEDs AUS um Bugs zu vermeiden
     
    frame = new JFrame();				      //Fenster erstellen								
    frame.setSize(1920, 1042);				  //mit der Groesse 1920 x 1042 (Ca. Vollbild)
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     

    JPanel panel = new JPanel();              //Großes Panel, auf dem alle anderen Panels und Buttons etc. aufbauen
    JPanel buttonPanel = new JPanel();        //nur für die 8x8 buttons
    frame.getContentPane().add(panel);		  //Panel zum Frame hinzugefügt
    panel.setLayout(null);					  //Kein Layout --> Direkte Koordinatenangabe der Items benötigt
        
    
    buttonPanel.setBounds(0, 0, 800, 800);	 				 //Groesse des Panels für die Buttons festlegen
    buttonPanel.setLayout(new GridLayout(8, 8, -1, -1));	 //Als Layout natuerlich das GridLayout, welches ein 8x8 "Schachfeld" macht. Zwischen den Quadraten ist die Lücke -1, also keine Lücke
    panel.add(buttonPanel);									 //Das ButtonPanel auf das grosse panel setzen		
     
    buttons = new JButton[LEDS];			   //Initialisierung eines Button-Arrays
    
    for (int i = 0; i < buttons.length; i++) { //alle 64 Buttons entwerfen und auf das Panel setzen   	
      String LED = String.valueOf(i + 1);      // +1 Damit die Buttons von 1-64 gezaehlt werden und nicht von 0-63
      JButton button = new JButton(LED,grau);  //64 Buttons mit dem Parameter LED (=Nummer des Buttons) und das Bild "grau" wird aufgesetzt             
      button.addActionListener(this);          //Action Listener zu jedem Button hinzufügen 
      buttons[i] = button;
      buttonPanel.add(buttons[i]);
    }

    
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX    Menubar
   
    JMenuBar menuBar = new JMenuBar();								//Menubar deklariert
    frame.setJMenuBar(menuBar);										//Menubar zum Frame hinzugefügt

    JMenu file = new JMenu("File");									//Neues Menu initialisiert
    JMenu Edit = new JMenu("Edit");									//Neues Menu initialisiert	
    JMenu connection = new JMenu("Connection");						//Neues Menu initialisiert
    
    menuBar.add(file);      										//Menu zur Menubar hinzugefügt
    menuBar.add(Edit);       										
    menuBar.add(connection);										
    
    JMenuItem connect2 = new JMenuItem("Connect");					//MenuItem deklariert
    JMenuItem disconnect = new JMenuItem("Disconnect");				
    JMenuItem OpenFile = new JMenuItem("Open File . . .");			
    JMenuItem SaveAs = new JMenuItem("Save as . . .");				
    JMenuItem Exit = new JMenuItem("Exit");							
    JMenuItem Undo = new JMenuItem("Undo: Clear all");				
    	
    ImageIcon openFile = new ImageIcon("pictures/openFile.png");	//Neues Image deklariert
    ImageIcon saveAs = new ImageIcon("pictures/saveAs.png");			
    ImageIcon undo = new ImageIcon("pictures/undo.png");			
    
    OpenFile.setIcon(openFile);										//Image zum JMenuItem zugefügt
    SaveAs.setIcon(saveAs); 										
    Undo.setIcon(undo);												
    
    connection.add(connect2);  										//MenuItems werden den Menus zugewiesen
    connection.add(disconnect);
    file.add(OpenFile);
    file.add(SaveAs);
    file.add(Exit);
    Edit.add(Undo);
    
    disconnect.addActionListener(this);								//Jedes MenuItem bekommt einen ActionListener
    connect2.addActionListener(this);                
    OpenFile.addActionListener(this);            
    SaveAs.addActionListener(this);      
    Exit.addActionListener(this);                    
    Undo.addActionListener(this);
             
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX		Buttons
   
    reset= new JButton("Clear all");						//Neuer Button mit der Beschriftung "Clear all"
    reset.setBounds(33,900,150,60);							//Position: X=33 Y=900 		Breite=150  Hoehe=60
    reset.setFont (reset.getFont ().deriveFont (25.0f));	//Schriftart und Groesse aendern
    reset.addActionListener(this);							//ActionListener hinzufuegen
    reset.setBackground(Color.red);							//Hintergrundfarbe auf rot 
    panel.add(reset);										//Zum panel hinzufuegen
    
    allOn = new JButton("All on");
    allOn.setBounds(33,830,150,60);
    allOn.setFont (allOn.getFont ().deriveFont (25.0f));
    allOn.addActionListener(this);
    allOn.setBackground(Color.green);
    panel.add(allOn);
        
    ebeneup = new JButton("Row   ++");
    ebeneup.setBounds(223,830,150,60);
    ebeneup.setFont (ebeneup.getFont ().deriveFont (25.0f));
    ebeneup.addActionListener(this);
    ebeneup.setBackground(Color.green);
    panel.add(ebeneup);

    ebenedown = new JButton("Row   --");
    ebenedown.setBounds(223,900,150,60);
    ebenedown.setFont (ebenedown.getFont ().deriveFont (25.0f));
    ebenedown.addActionListener(this);
    ebenedown.setBackground(Color.red);
    panel.add(ebenedown);
     
    output = new JButton("Send");
    output.setBounds(1633,900,250,60);
    output.setFont (output.getFont ().deriveFont (25.0f));
    output.addActionListener(this);
    output.setBackground(Color.white);
    panel.add(output);
    
    int displayEbene = CurrentEbene + 1;
    CurrentEbenetext = new JLabel("Row = " + displayEbene);						//Label das die aktuelle dargestellte Reihe an LEDs angeben soll
    CurrentEbenetext.setFont (CurrentEbenetext.getFont ().deriveFont (32.0f));	
    CurrentEbenetext.setBounds(400,845,300,100);
    panel.add(CurrentEbenetext);
    
    sliderLabel = new JLabel("Time: " + slider.getValue() + " ms");				//Slider mit dem man die Zeit, wielange ein Bild angezeigt werden soll, einstellen kann
    sliderLabel.setFont (sliderLabel.getFont ().deriveFont (25.0f));
    sliderLabel.setBounds(805,920,300,50);
    panel.add(sliderLabel);
            
    list.setBounds(810,100,250,235);
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);					//Man kann nur ein Item anklicken
    panel.add(list);
                      
    list2.setBounds(810,375,250,235);
    list2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    panel.add(list2);
                 
    list3.setBounds(810,650,250,235);
    list3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    panel.add(list3);
       
    
    JButton addButton= new JButton("Save current frame");
    addButton.setFont (addButton.getFont ().deriveFont (18.0f));
    addButton.setBounds(810,3,250,57);
    addButton.addActionListener(this);
    addButton.setBackground(Color.green);
    panel.add(addButton);
    
    JButton addToCombiner = new JButton("Add");
    addToCombiner.setFont (addToCombiner.getFont ().deriveFont (18.0f));
    addToCombiner.setBounds(810,340,250,30);
    addToCombiner.setBackground(Color.green);
    panel.add(addToCombiner);  
    
    JButton addToVideo = new JButton("Combine and create gif");
    addToVideo.setFont (addToVideo.getFont ().deriveFont (17.0f));
    addToVideo.setBounds(810,615,250,30);
    addToVideo.setBackground(Color.green);
    panel.add(addToVideo);
                               
    JButton deselectButton = new JButton("Save & Deselect item");
    deselectButton.setFont (deselectButton.getFont ().deriveFont (18.0f));
    deselectButton.setBounds(810,65,250,30);
    deselectButton.setBackground(Color.white);
    panel.add(deselectButton);
                  
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX		PopupMenu 
																				  
    
    final JPopupMenu popupMenu = new JPopupMenu();   	//Popupmenu für Liste 1        
    JMenuItem save = new JMenuItem("Save as...");
    JMenuItem rename = new JMenuItem("Rename");
    JMenuItem delete = new JMenuItem("Delete");
    popupMenu.add(delete);
    popupMenu.add(rename);
    popupMenu.add(save);
      
    final JPopupMenu popupMenu2 = new JPopupMenu();    //Popupmenu für Liste 2       
    JMenuItem save2 = new JMenuItem("Save as...");
    JMenuItem rename2 = new JMenuItem("Rename");
    JMenuItem delete2 = new JMenuItem("Delete");
    popupMenu2.add(delete2);
    popupMenu2.add(rename2);
    popupMenu2.add(save2);
      
    final JPopupMenu popupMenu3 = new JPopupMenu();   //Popupmenu für Liste 3         
    JMenuItem save3 = new JMenuItem("Save as...");
    JMenuItem rename3 = new JMenuItem("Rename");
    JMenuItem delete3 = new JMenuItem("Delete");
    popupMenu3.add(delete3);
    popupMenu3.add(rename3);
    popupMenu3.add(save3);
    
    save.addActionListener(this);			//action listener für alle popupMenu items hinzugefügt
    rename.addActionListener(this);
    delete.addActionListener(this);
    
    save2.addActionListener(this);
    rename2.addActionListener(this);
    delete2.addActionListener(this);
    
    save3.addActionListener(this);
    rename3.addActionListener(this);
    delete3.addActionListener(this);

    
    slider.setMajorTickSpacing(1000);
    slider.setPaintTicks(true);
    slider.setBounds(803,880,267,50);
    panel.add(slider);
      
      slider.addChangeListener(new ChangeListener() {					//ActionListener für den Slider. Er wird ausgeloest sobald der Slider sich bewegt
          public void stateChanged(ChangeEvent e) {        	  
            sliderLabel.setText("Time: " + slider.getValue() + " ms");	//Das label aktualisieren
          }
        });
         
      deselectButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {        	  	        	  	
        	  	       	  
        	  int index = list.getSelectedIndex();   			// klick kam von welchem item?   	  
        	  TimePerFrame[index+1]=slider.getValue();			//zeit des sliders wird in array gespeichert                           
              SaveAndDeselectButtons(index);					//Buttons werden gespeichert und alle Items in den 3 Listen werden wieder "normal" dargestellt. 
          }														//Wenn man ein Item in einer Liste anklickt, wird dieses Blau umrandet
        });
      
      addToCombiner.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
          
            Object selectedItem = list.getSelectedValue();		 //angeklicktes Item herausfinden
            int index = list.getSelectedIndex();				 //Index (geht von 0-14) bekommen
            
            if (selectedItem != null) {              			 //nur wenn ein item ausgewählt wurde  
            	model2.setSize(15);                                                                       
            	model2.set(index, "Importiertes Frame:   " + selectedItem);	//In der zweiten Liste ein neues Item hinzufuegen
            	SaveAndDeselectButtons(index);		
            } 
            
            list.clearSelection();			//macht, dass das item in der liste nichtmehr blau markiert ist			
            list2.clearSelection();
            list3.clearSelection();
          }
        });
          
      for (int i = 0; i <= 14; i++) {
    	  IndexInVideo[i]=false;		//kein frame gehört bisher in das nächste video rein also ist das gesamte Array auf "false"
	  }
      
      addToVideo.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {        	  	        	  	
        	  	zaehler++;       	  	                        //der zaehler soll zaehlen, wieviele "Video" Items es bereits gibt. Das ist wichtig um herauszufinden, welche Frames denn bei einem Klick dargestellt werden sollen 		                                              
          		model3.addElement("Video " + zaehler);			//In der dritten Liste wird ein Item hinzugefuegt
          		
          		for (int i = 0; i <= 14; i++) {
          			if (model2.getElementAt(i) != null) {		//geht jedes tabellenitem durch und guckt ob es einen inhalt hat, wenn ja ....
          				IndexInVideo[i+1]=true;					//markiert welche frames in das video kommen werden						
					}
          			
				}
          	
          		
          }
        });
                                          
      list.addListSelectionListener(new ListSelectionListener() {         //wenn ein item in liste 1 angeklickt wird
            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) { 						  //wenn der rückgabewert des events =true ist
                                                    
                	int index = list.getSelectedIndex();           		  //soll geschaut werden von wo der klick kam
                	list2.clearSelection();								  //und macht, dass das item in der liste nichtmehr blau markiert ist
                	list3.clearSelection();								  //und macht, dass das item in der liste nichtmehr blau markiert ist
                
                	for (int i = 0; i <= 511; i++) {               
                          matrix[i] = matrixArray[i][index+1];                              
                	}
                	EbeneUpdate();    							 		  //GUI aktualisieren                                                    
                	}
            }
        });
      
      list2.addListSelectionListener(new ListSelectionListener() {		 //wenn ein item in liste 2 angeklickt wird
            @Override
            public void valueChanged(ListSelectionEvent arg0) {
            	
            	int index = list2.getSelectedIndex();    		    //soll geschaut werden von wo der klick kam
                if (!arg0.getValueIsAdjusting()) { 					//wenn der rückgabewert des events =true ist
                                                    		               	
                	list.clearSelection();							//und macht, dass das item in der liste nichtmehr blau markiert ist
                	list3.clearSelection();							//und macht, dass das item in der liste nichtmehr blau markiert ist
                  
                	for (int i = 0; i <= 511; i++) {               
                		matrix[i] = matrixArray[i][index+1];        //Datenaustausch weil mit EbeneUpdate() das matrix[] Array angesprochen wird und nicht das Hilfsarray, in dem jedes frame gespeichert ist.                 
                	}
                	EbeneUpdate();									//GUI aktualisieren                 
                	}
                
            }
        });
      
      list3.addListSelectionListener(new ListSelectionListener() {		//wenn ein item in liste 3 angeklickt wird
            @Override
            public void valueChanged(ListSelectionEvent arg0) {	
                if (!arg0.getValueIsAdjusting()) { 						//wenn der rückgabewert des events =true ist	
                                                    
                	int index = list3.getSelectedIndex();    			//soll geschaut werden von wo der klick kam
                	list.clearSelection();								//macht, dass das item in der liste nichtmehr blau markiert ist
                	list2.clearSelection();								//macht, dass das item in der liste nichtmehr blau markiert ist
                	int indexSammler[] = new int[15];
                	int indexZaehler=0;                	
                	
                	for (int i = 0; i <= 13; i++) {
						if (IndexInVideo[i+1]=true) {
							indexZaehler++;
							indexSammler[indexZaehler]=index+1;	//enthält alle index die ins video kommen müssen
						}
					}
                                      	                	                	                	                	
                	     
                	for (int i = 0; i <= 511; i++) {               
                		matrix[i] = matrixArray[i][1];                       
                	}
            		EbeneUpdate();	
                	          		
                /*  double elapsedtime = 0;
                	long starttime= System.currentTimeMillis();                  	
                	do {                    										
                	    long endtime=System.currentTimeMillis();
                	    elapsedtime=(double)(endtime-starttime);                	       
                	} while (elapsedtime<=1500);

                	
                	for (int i = 0; i <= 511; i++) {               
                		matrix[i] = matrixArray[i][2];                       
                	}
                	EbeneUpdate();									//GUI aktualisieren       
                */
                	  
                	
                	
                	}
            }
        });
                                                                                                
      list.addMouseListener(new MouseAdapter() {								//frame lässt sich per doppelklick "renamen"   in liste 1
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
      
      list2.addMouseListener(new MouseAdapter() {								//frame lässt sich per doppelklick "renamen"   in liste 2
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
      	
      list3.addMouseListener(new MouseAdapter() {								//frame lässt sich per doppelklick "renamen"   in liste 3
          public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
            	int index = list3.locationToIndex(e.getPoint());
            	Object item = model3.getElementAt(index);
            	String text = JOptionPane.showInputDialog("Rename video", item);
            	String newitem = "";
            	if (text != null)
            		newitem = text.trim();
            	else
            		return;

            	if (!newitem.isEmpty()) {
            		model3.remove(index);
            		model3.add(index, newitem);
            		ListSelectionModel selmodel = list3.getSelectionModel();
            		selmodel.setLeadSelectionIndex(index);
            		}
            	}
          }
        }); 
      
      list.addMouseListener(new MouseAdapter() {
          public void mouseClicked(java.awt.event.MouseEvent me) {
          // if right mouse button clicked
          if (SwingUtilities.isRightMouseButton(me)			//wenn rechte maustaste gedrückt wurde
          && !list.isSelectionEmpty()						//UND wenn NICHT nichts angeklickt ist
          && list.locationToIndex(me.getPoint())			//UND der ursprung des klicks 
          == list.getSelectedIndex()) {						//dem ausgewählten item enspricht
          popupMenu.show(list, me.getX(), me.getY());		// --> zeige popupMenu an Mausposition
                }
              }
          }
          );
            
      list2.addMouseListener(new MouseAdapter() {
          public void mouseClicked(java.awt.event.MouseEvent me) {
          // if right mouse button clicked 
          if (SwingUtilities.isRightMouseButton(me)			//wenn rechte maustaste gedrückt wurde
          && !list2.isSelectionEmpty()						//wenn NICHT nichts angeklickt ist
          && list2.locationToIndex(me.getPoint())			//UND der ursprung des klicks 
          == list2.getSelectedIndex()) {					//dem ausgewählten item enspricht
          popupMenu2.show(list2, me.getX(), me.getY());		// --> zeige popupMenu an Mausposition
                }
              }
          }
          );    
      
      list3.addMouseListener(new MouseAdapter() {
          public void mouseClicked(java.awt.event.MouseEvent me) {
          // if right mouse button clicked 
          if (SwingUtilities.isRightMouseButton(me)			//wenn rechte maustaste gedrückt wurde
          && !list3.isSelectionEmpty()						//wenn NICHT nichts angeklickt ist
          && list3.locationToIndex(me.getPoint())			//UND der ursprung des klicks
          == list3.getSelectedIndex()) {					//dem ausgewählten item enspricht
          popupMenu3.show(list3, me.getX(), me.getY());		// --> zeige popupMenu an Mausposition
                }
              }
          }
          );
      
      rename.addActionListener(new ActionListener() {					//action listener für popupmenu item "rename" in liste 1
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
      
      rename2.addActionListener(new ActionListener() {					//action listener für popupmenu item "rename" in liste 2
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
      
      rename3.addActionListener(new ActionListener() {					//action listener für popupmenu item "rename" in liste 3
          public void actionPerformed(ActionEvent e) {
        	  ListSelectionModel selmodel = list3.getSelectionModel();
        	  int index = selmodel.getMinSelectionIndex();
        	  if (index == -1)
        		  return;
        	  Object item = model3.getElementAt(index);
        	  String text = JOptionPane.showInputDialog("Rename video", item);
        	  String newitem = null;

        	  if (text != null) {
        		  newitem = text.trim();        
        	  } else
        		  return;

        	  if (!newitem.isEmpty()) {
        		  model3.remove(index);
        		  model3.add(index, newitem);
        	  }
          }
        });
        
      delete.addActionListener(new ActionListener() {					//action listener für popupmenu item "delete" in liste 1
          public void actionPerformed(ActionEvent event) {
        	  ListSelectionModel selmodel = list.getSelectionModel();
        	  int index = selmodel.getMinSelectionIndex();
        	  if (index >= 0)
        		  model.remove(index);
          	  }

        });
      
      delete2.addActionListener(new ActionListener() {					//action listener für popupmenu item "delete" in liste 2
          public void actionPerformed(ActionEvent event) {
        	  ListSelectionModel selmodel = list2.getSelectionModel();
        	  int index = selmodel.getMinSelectionIndex();
        	  if (index >= 0)
        		  model2.remove(index);
          	  }

        });
      
      delete3.addActionListener(new ActionListener() {					//action listener für popupmenu item "delete" in liste 3
          public void actionPerformed(ActionEvent event) {
        	  ListSelectionModel selmodel = list3.getSelectionModel();
        	  int index = selmodel.getMinSelectionIndex();
        	  if (index >= 0)
        		  model3.remove(index);
          	  }

        });
             
         
     
  } //ende initialize
 
  @SuppressWarnings("serial")
class MeinZeichenPanel extends JPanel{
	    public void paintComponent(Graphics g){
	      	
	    	g.setColor(Color.RED);
	    	g.fillRect(1000, 200, 500, 500);
	    }
	  }
  
  
  
  public void actionPerformed(ActionEvent e) {        
  
    String quelle = e.getActionCommand(); 
    
    int zahl = 0;
    if (quelle.length()<3) {    			 //dann ist es eine LED
        
        zahl=Integer.parseInt(quelle)-1;	//LED Nummer herausfinden
        buttons[zahl].setIcon(blau);		//diesem button das bild mit dem blauen punkt geben

        if (!geklickt[zahl]) {
          geklickt[zahl] = true;        
          an_aus = true;					
          
        } else {
          geklickt[zahl] = false;			//button ist AUS
          buttons[zahl].setIcon(grau);		//diesem button das bild mit dem grauen punkt geben
        }
      }
    
    
  switch (quelle) {
    
      case "Clear all":
        undo.undoMatrixToTemp(matrix);
        MatrixAus();
        EbeneUpdate();
        break;
        
      case "Send":
        time.StartTimer();
        try {
          netsend.Stringbuilder(matrix,1);
        } catch (IOException error) {
          // TODO Auto-generated catch block
          error.printStackTrace();	//nichts tun
        }
        System.out.println(" Gesamte Zeit"+time.StoppTimer()+"ms");
        break;
        
      case "Row   ++":
        if (CurrentEbene < 7) {
          CurrentEbene++;
          EbeneUpdate();
        }
        break;
        
      case "Row   --":
        if (CurrentEbene > 0) {
          CurrentEbene--;
          EbeneUpdate();
        }
        break;
    
      case "Exit":
        System.exit(0);       //exit program
        break;
      
      case "Open File . . .":
        matrix = getdatafile.openFileArray();  // Array wird empfangen
        EbeneUpdate();						 //GUI aktualisieren
        break;
      
      case "Save as . . .":
        getdatafile.SaveArraytoFile(matrix);
        break;
      
      case "Undo reset":
        matrix=undo.undoTempToMatrix();
        EbeneUpdate();						 //GUI aktualisieren
        break;          
      
      case "Save as...":
        getdatafile.SaveArraytoFile(matrix);
        break;
      
      case "Connect":
    	  if (ip==null && port==null) {												//wenn in den variablen kein Inhalt ist...
    		  JFrame connectFrame = new JFrame();  	   
        	  ip = JOptionPane.showInputDialog(connectFrame, "Enter IP-Address");    
        	  JFrame connectFrame2 = new JFrame();  	   
        	  port = JOptionPane.showInputDialog(connectFrame2, "Enter port");    
		}else {
			
		}
    	  
          break;   
      case "Disconnect":
    	    
          break;        
        
      case "All on":
          MatrixAn();							 //alle LEDs an
          EbeneUpdate();						 //GUI aktualisieren
          break;
               	
      case "Save current frame":
      	  SaveCurrentFrame();						   //GUI aktualisieren
          break;
     
        
      default:
          break;
    }
      
  
    ButtonState(geklickt[zahl], zahl);  //alle LEDs aktualisieren
  }

void SaveAndDeselectButtons(int index) {	
	for (int i = 0; i <= 511; i++) {               
        matrixArray[i][index+1] = matrix[i];    	// speichere angeklicktes frame in hilfsArray ab               
    }
  
    list.clearSelection();			//macht, dass das item in der liste nichtmehr blau markiert ist			
    list2.clearSelection();
    list3.clearSelection();
    MatrixAus();					//alles LEDs aus
    EbeneUpdate();					//GUI aktualisieren
}

void SaveCurrentFrame() {
	frameNummer++;                       
    for (int j = 0; j <= 511; j++) {
      matrixArray[j][frameNummer] = matrix[j];  //die jetzige matrix wird in ein weiteres Array gespeichert und kann immer wieder abgerufen werden. 
     }            
    model.addElement("Frame " + frameNummer);  //zur liste 3 ein neues item mit dem namen Frame + FrameNummer hinzufügen                   
    MatrixAus();							   //Alle LEDs aus
    EbeneUpdate();  
}

  void ButtonState(boolean state, int button_nr) { // Updated Button State Array
    int CurrentOffset = (CurrentEbene) * 64;

    matrix[button_nr + CurrentOffset] = state;
  }

  
  void EbeneUpdate() {        // Methode die die Buttons der sichtbaren ebene updated
    
    CurrentEbenetext.setText("Row = " + Integer.toString(CurrentEbene + 1));// Display der derzeitigen Ebene
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
  
  
  void MatrixAus(){
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
