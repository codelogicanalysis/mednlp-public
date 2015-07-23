/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EECE.These.Annotation;

import static EECE.These.Annotation.SAMNAFrame.abstracts;
import static EECE.These.Annotation.SAMNAFrame.allFiguresabstracts;
import EECE.Theses.FilesReader.TxtReader;
import EECE.Theses.FilesReader.XmlParser;
//import EECE.Theses.FilesReader.XmlParser;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author msabra
 */
public class LoadingFrame extends JFrame implements ActionListener {
   JButton button1, button2, button3,button4,button5;
    public LoadingFrame(String samna,String workspace) {
       this.setTitle(samna); //To change body of generated methods, choose Tools | Templates.
        RunLoadingFrame(workspace);
    }
     
     public void RunLoadingFrame( final String workspace)  {
         try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }
 
            button1 = new JButton("Load Serialized object");
        button2 = new JButton("Load XML file from PubMed");
        button3 = new JButton("Load CSV file Scupos");
         button4 = new JButton("Load auto extracted Paper");
          button5 = new JButton("Load TXT file Scupos");
          button1.setPreferredSize(new Dimension(200, 30));
           button2.setPreferredSize(new Dimension(200, 30));
            button3.setPreferredSize(new Dimension(200, 30));
             button4.setPreferredSize(new Dimension(200, 30));
              button5.setPreferredSize(new Dimension(200, 30));
        button1.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent event) {
              ArrayList<HashMap<String, String>> abstracts = new ArrayList();
               ArrayList<ImageCanvas> allFiguresabstracts = new ArrayList();
              JTextArea  Abstract = new JTextArea ();
                 AutoExtractedPapers autoExtractedPapers= new AutoExtractedPapers();
              autoExtractedPapers=BrowsLoadAbstractserialized(Abstract);
                abstracts=   autoExtractedPapers.getAbstracts();
        allFiguresabstracts=autoExtractedPapers.getAllFiguresAbstracts();
           SAMNAFrame ZSAframe=new SAMNAFrame();
         ZSAframe.RunZSAFrame(workspace,Abstract,abstracts,allFiguresabstracts);
      dispose();
      }
   });
        button2.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent event) {
            ArrayList<HashMap<String, String>> abstracts = new ArrayList();
              JTextArea  Abstract = new JTextArea ();
             abstracts=  BrowsLoadAbstract(Abstract);
           SAMNAFrame ZSAframe=new SAMNAFrame();
         ZSAframe.RunZSAFrame(workspace,Abstract,abstracts,null);                                   
           dispose();
      }
   });
        button3.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent event) {
            ArrayList<HashMap<String, String>> abstracts = new ArrayList();
              JTextArea  Abstract = new JTextArea ();
        abstracts= BrowsLoadCSVAbstract(Abstract);
           SAMNAFrame ZSAframe=new SAMNAFrame();
         ZSAframe.RunZSAFrame(workspace,Abstract,abstracts,null);                                   
           dispose();
      }
   });
                button4.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent event) {
            ArrayList<HashMap<String, String>> abstracts = new ArrayList();
            ArrayList<ImageCanvas> allFiguresabstracts = new ArrayList();
              JTextArea  Abstract = new JTextArea ();
              AutoExtractedPapers autoExtractedPapers= new AutoExtractedPapers();
              autoExtractedPapers=   BrowsLoadPaper(Abstract);
        abstracts=   autoExtractedPapers.getAbstracts();
        allFiguresabstracts=autoExtractedPapers.getAllFiguresAbstracts();
           SAMNAFrame ZSAframe=new SAMNAFrame();
         ZSAframe.RunZSAFrame(workspace,Abstract,abstracts,allFiguresabstracts);                                   
           dispose();
      }
   });
                 button5.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent event) {
            ArrayList<HashMap<String, String>> abstracts = new ArrayList();
              JTextArea  Abstract = new JTextArea ();
        abstracts=     BrowsLoadTxtAbstract(Abstract);
           SAMNAFrame ZSAframe=new SAMNAFrame();
         ZSAframe.RunZSAFrame(workspace,Abstract,abstracts,null);                                   
           dispose();
      }
   });
        
         JPanel card1 = new JPanel();
        card1.add(button1);
        card1.add(button2);
        card1.add(button3);
         card1.add(button4);
          card1.add(button5);
        ImageIcon img = new ImageIcon(workspace+"\\SAMNA_2.png");
        
        this.setIconImage(img.getImage());
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
      this.add(card1);
        this.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        this.setSize(300, 250);
        //  this.pack();
        this.setVisible(true);
      
     }
     
      /**
     * brows load abstract open a file chooser consol to choose a serialized object
     * @param Abstract
     */
 private AutoExtractedPapers BrowsLoadAbstractserialized(JTextArea  Abstract)  {
        
        final JFileChooser fc = new JFileChooser();
         fc.setDialogTitle("Choose the serialized object");
        int returnVal = fc.showOpenDialog(Abstract);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filepath = fc.getSelectedFile().getAbsolutePath();
            //   abstracts=
          return  LoadAbstractserialized(filepath);
        }
        return null;
    }
 
 
 /**
     *
     * @param inputFile serialized object path to read from
     */
 private  AutoExtractedPapers LoadAbstractserialized(String inputFile) {
        
        XmlParser xmlparser=new XmlParser();
          ArrayList<HashMap<String, String>> abstracts = new ArrayList();
          AutoExtractedPapers autoExtractedPapers= new AutoExtractedPapers();
        autoExtractedPapers=xmlparser.GetXmlAbstractsserialized(inputFile); //load all abstract in the array list of hash map
        
        System.out.println("finish loading abstracts");
        return autoExtractedPapers;
    }
 
 
    /**
     * brows load abstract open a file chooser consol to choose a XML file
     * @param Abstract
     */
  private  ArrayList<HashMap<String, String>> BrowsLoadAbstract(JTextArea  Abstract)  {
        
        final JFileChooser fc = new JFileChooser(".");
          fc.setDialogTitle("Choose the XML file retreived from PubMed");
        int returnVal = fc.showOpenDialog(Abstract);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filepath = fc.getSelectedFile().getAbsolutePath();
            //   abstracts=
           return LoadAbstract(filepath);
        }
        return null;
    }
  
  /**
     *
     * @param inputFile XML file path to read from
     */
   private  ArrayList<HashMap<String, String>> LoadAbstract( String inputFile) {
        
        XmlParser xmlparser=new XmlParser();
        abstracts=xmlparser.GetXmlAbstracts(inputFile); //load all abstract in the array list of hash map
        System.out.println("finish loading abstracts");
        return abstracts;
    }
   
   /**
     * brows load abstract open a file chooser consol to choose a csv file
     * @param Abstract
     *
     */
    private ArrayList<HashMap<String, String>>  BrowsLoadCSVAbstract(JTextArea  Abstract)  {
        
        final JFileChooser fc = new JFileChooser();
           fc.setDialogTitle("Choose the CSV file retreived From Scopus");
        int returnVal = fc.showOpenDialog(Abstract);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filepath = fc.getSelectedFile().getAbsolutePath();
            //   abstracts=
        return    LoadCSVAbstract(filepath);
        }
        return null;
    }
   /**
     *
     * @param inputFile CSV file path to read from
     */
    private ArrayList<HashMap<String, String>>  LoadCSVAbstract( String inputFile) {
        
        TxtReader txtReader=new TxtReader();
        abstracts=txtReader.GetTxtAbstractsFromCSV(inputFile); //load all abstract in the array list of hash map
        System.out.println("finish loading abstracts FROM CSV");
        return abstracts;
    }
    
    
     private AutoExtractedPapers BrowsLoadPaper(JTextArea  Abstract)  {
        
        final JFileChooser fc = new JFileChooser();
        
           fc.setDialogTitle("Choose the Directory where the paper retreived are stored");
         fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
       int returnVal = fc.showOpenDialog(Abstract);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filepath = fc.getSelectedFile().getAbsolutePath();
           return LoadPaper(filepath);
        }
      return null;
    }
     
      private AutoExtractedPapers  LoadPaper( String inputFile) {
        
        TxtReader txtReader=new TxtReader();
        AutoExtractedPapers autoExtractedPapers=new AutoExtractedPapers();
        autoExtractedPapers=txtReader.GetPaperAbstracts(inputFile); //load all abstract in the array list of hash map
        System.out.println("finish loading abstracts");
        return autoExtractedPapers;
    }
      
      /**
     * brows load abstract open a file chooser consol to choose a txt file
     * @param Abstract
     */
    private ArrayList<HashMap<String, String>> BrowsLoadTxtAbstract(JTextArea  Abstract)  {
        
        final JFileChooser fc = new JFileChooser();
           fc.setDialogTitle("Choose the TXT file retreived From Scopus");
        int returnVal = fc.showOpenDialog(Abstract);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filepath = fc.getSelectedFile().getAbsolutePath();
           
          return  LoadTxtAbstract(filepath);
        }
        return null;
    }

    /**
     *
     * @param inputFile TXT file path to read from
     */
    private ArrayList<HashMap<String, String>> LoadTxtAbstract( String inputFile) {
        
        TxtReader txtReader=new TxtReader();
        abstracts=txtReader.GetTxtAbstracts(inputFile); //load all abstract in the array list of hash map
        System.out.println("finish loading abstracts");
        return abstracts;
    }
    
      
      
    @Override
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == button1){    
        this.setVisible(false);
        }
    }
}
