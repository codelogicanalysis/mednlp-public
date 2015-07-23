/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package EECE.These.Annotation;

import EECE.Theses.FilesReader.ExcelReader;
import EECE.Theses.FilesReader.TxtReader;
import EECE.Theses.FilesReader.XmlParser;
import EECE.Theses.Highlighter.HighlightPainter;
import annotationtest.excelreader_vtest;
import com.google.common.collect.Multimap;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.commons.lang3.StringUtils;
//import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author msabra
 */
public class SAMNAFrame {
    
    /*
    * To change this license header, choose License Headers in Project Properties.
    * To change this template file, choose Tools | Templates
    * and open the template in the editor.
    */
    
    // <editor-fold defaultstate="collapsed" desc="Static component  ">
    static JTabbedPane tabbedPane;
    static JPopupMenu Pmenu;
    static JMenuItem addProtein;
    static JMenuItem addNewLable;
    static JMenuItem removeLocaly;
    static JMenuItem nonProtin;
     static JMenuItem removeAddedLable;
       static JMenuItem removeAbstract;
    static JMenuItem undoLastMove;
    static boolean specialCase;
    static String ProteinFileName="formoh-labels";
    static ArrayList <ACTIONS> lastAcion= new ArrayList <ACTIONS>();
    
    static  ArrayList <Multimap <String,ArrayList<String>>> matchingAndSimilarWords;
    static   Multimap <String,ArrayList<String>> matchingWords;
      static   Multimap <String,ArrayList<String>> SimilarWords;
      static   Multimap <String,ArrayList<String>> SimilarWords_2;
        static   Multimap <String,ArrayList<String>> SimilarWords_3;
    static HashMap<String, ArrayList <String>> annotationsLocations=null;
    static ArrayList<HashMap<String, String>> abstracts = new ArrayList();
       static ArrayList<ImageCanvas> allFiguresabstracts = new ArrayList();
    static int abstractindex=0;
    // </editor-fold>
    //public SAMNAFrame(String workspace)
    /**
     * main run function first called.
     * @param workspace
     */
    
    public void RunZSAFrame(final String workspace,final JTextArea  Abstract, ArrayList<HashMap<String, String>> loadedabstracts,ArrayList< ImageCanvas> loadedallFiguresabstracts)  {
          try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }
abstracts=loadedabstracts;
allFiguresabstracts=loadedallFiguresabstracts;
        specialCase=true;
       // PropertyConfigurator.configure(workspace+"\\log4j.properties");
        final  ExcelReader excel=new ExcelReader();
        final excelreader_vtest excel2= new excelreader_vtest();
        // final ExcelReader excel2=new ExcelReader();
        excel.open(workspace+"\\results.xlsx");
        final ArrayList <String> AllLables= excel.GetAllLables(workspace+"\\Labels.xlsx");
        final ArrayList  <Multimap  <String,ArrayList<String>>> matchingLables=new  ArrayList  <Multimap  <String,ArrayList<String>>>();
        final ArrayList<HighlightPainter>  lable1higliter=new ArrayList<HighlightPainter>();
        final JTextField jt1 = new JTextField(10);
        final JTextField jt2 = new JTextField(10);
        //   jt2.setEnabled(false);
        jt2.setEditable(false);
        matchingAndSimilarWords = excel2.GetAllProteinAndSimilarWords(workspace);// get all protein words from the excel file and put them in a multihashmap
    // matchingWords= excel.GetAllProtein(workspace+"\\formoh-labels.xlsx");
        matchingWords=matchingAndSimilarWords.get(0);
        SimilarWords=matchingAndSimilarWords.get(1);
//      for (String key : SimilarWords.keySet()) {
//          float totalkeyscore=(float) 0.0;
//          int times= 0;
//           Collection<ArrayList<String>> wordArrayLists=SimilarWords.get(key);
//            List< ArrayList<String> > list = new ArrayList< ArrayList<String> >( wordArrayLists );
//            int loop=0;
//            if (key.equals("GLUTATHIONE"))
//            {
//            int g=0;
//            }
//                  for (int r=0;r<list.size();r++)
//                 {
//                  totalkeyscore=totalkeyscore+ Float.parseFloat(list.get(r).get(0));
//                  times=times+ Integer.parseInt(list.get(r).get(1));
//                  loop++;
//                  }
//                //  System.out.println(SimilarWords.get(key) );
//    System.out.println(key + "\t" + totalkeyscore+"\t"+times+"\t"+loop);
//}
     //   System.out.println("-----------------------------------------------------");
         SimilarWords_2=matchingAndSimilarWords.get(2);
//         for (String key : SimilarWords_2.keySet()) {
//         // float totalkeyscore=(float) 0.0;
//        //  int times= 0;
//           Collection<ArrayList<String>> wordArrayLists=SimilarWords_2.get(key);
//            List< ArrayList<String> > list = new ArrayList< ArrayList<String> >( wordArrayLists );
//           
//                  for (int r=0;r<list.size();r++)
//                 {
//                        System.out.println(key + "\t" + list.get(r).get(0));
//                  }
//                //  System.out.println(SimilarWords.get(key) );
// 
//}
         
         
         
            SimilarWords_3=matchingAndSimilarWords.get(3);
            
      //      System.out.println("sizes are "+matchingWords.size()+";" +SimilarWords.size()+";" +SimilarWords_2.size()+";" +SimilarWords_3.size());
        Color ProteinhiglightColor=Color.RED;
         Color suggestedProteinhiliterColor=Color.LIGHT_GRAY;
        Color AddedProteinhiglightColor=Color.YELLOW;
        Color NonProteinhiglightColor=Color.green;
        final HighlightPainter Proteinhigliter=new HighlightPainter(ProteinhiglightColor);
        final HighlightPainter suggestedProteinhiliter=new HighlightPainter(suggestedProteinhiliterColor);
        final HighlightPainter AddedProteinhigliter=new HighlightPainter(AddedProteinhiglightColor);
        final HighlightPainter NonProteinhigliter=new HighlightPainter(NonProteinhiglightColor);
        final JFrame frame = new JFrame("SAMNA");
        ImageIcon img = new ImageIcon(workspace+"\\SAMNA_2.png");
        
        frame.setIconImage(img.getImage());
        Container content = frame.getContentPane();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      //  final JTextArea  Abstract = new JTextArea ();
         final JTextArea  Result = new JTextArea ();
          final JTextArea  Figures = new JTextArea ();
        final JTextArea  pmidDate = new JTextArea (20,20);
           final JTextArea  statistics = new JTextArea ();
        final DefaultListModel higlightedWordslistModel = new DefaultListModel();
        final JList higlightedWords= new JList(higlightedWordslistModel);
        final JTextArea  Title = new JTextArea (4,20);
        //   higlightedWords.setLayoutOrientation(JList.VERTICAL_WRAP);
      statistics.setEditable(false);
        pmidDate.setEditable(false);
        Abstract.setEditable(false);
        Abstract.getCaret().setVisible(true);
        Abstract.getCaret().setSelectionVisible(true);
        
         Result.setEditable(false);
        Result.getCaret().setVisible(true);
        Result.getCaret().setSelectionVisible(true);
        
         Figures.setEditable(false);
        Figures.getCaret().setVisible(true);
        Figures.getCaret().setSelectionVisible(true);
        // higlightedWords.setEditable(false);
        Title.setEditable(false);
        Font font = new Font (null, Font.ROMAN_BASELINE, 22);
        Font smallerFont = new Font (null, Font.ROMAN_BASELINE, 18);
        Abstract.setFont(font);
        Result.setFont(font);
          Figures.setFont(font);
        pmidDate.setFont(smallerFont);
        statistics.setFont(smallerFont);
        higlightedWords.setFont(smallerFont);
        Title.setFont(font);
        //  jep.setForeground(Color.blue);
        Title.setLineWrap(true);
        Title.setWrapStyleWord(true);
        Title.setInheritsPopupMenu(true);
        Title.setMargin(new Insets(2,2,2,2) );
        Abstract.setLineWrap(true);
        Abstract.setWrapStyleWord(true);
        Abstract.setInheritsPopupMenu(true);
        Abstract.setMargin(new Insets(10,10,10,10) );
         Figures.setLineWrap(true);
        Figures.setWrapStyleWord(true);
        Figures.setInheritsPopupMenu(true);
        Figures.setMargin(new Insets(10,10,10,10) );
        
         Result.setLineWrap(true);
        Result.setWrapStyleWord(true);
        Result.setInheritsPopupMenu(true);
        Result.setMargin(new Insets(10,10,10,10) );
        
        
        pmidDate.setLineWrap(true);
        pmidDate.setWrapStyleWord(true);
        pmidDate.setInheritsPopupMenu(true);
         statistics.setLineWrap(true);
        statistics.setWrapStyleWord(true);
        statistics.setInheritsPopupMenu(true);
        //  higlightedWords.setLineWrap(true);
        // higlightedWords.setWrapStyleWord(true);
        higlightedWords.setInheritsPopupMenu(true);
        
        JScrollPane Abstractpane = new JScrollPane(Abstract);
        JScrollPane pmidDatepane = new JScrollPane(pmidDate);
        JScrollPane higlightedWordspane = new JScrollPane(higlightedWords);
        JScrollPane Titlepane = new JScrollPane(Title);
         JScrollPane statisticspane = new JScrollPane(statistics);
         JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
            pmidDatepane, statisticspane);
          
         tabbedPane = new JTabbedPane();
          tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
       
         tabbedPane.addTab("Abstract", null, new JScrollPane(Abstract),
                "Does nothing");
         tabbedPane.addTab("Result", null, new JScrollPane(Result),
                "Does nothing");
         tabbedPane.addTab("Figures captions", null, new JScrollPane(Figures),
                "Does nothing");
     //     JComponent panel2 = makeTextPanel("Abstract");
       //   panel2.add(Abstract,BorderLayout.CENTER);
        //   panel2.setPreferredSize(new Dimension(410, 50));
        
       //  Abstractpane.add(tabbedPane);
         content.add(tabbedPane, BorderLayout.CENTER);
        content.add(splitPane, BorderLayout.LINE_START);
        // content.add(statisticspane, BorderLayout.LINE_START);
        content.add(higlightedWordspane, BorderLayout.LINE_END);
        content.add(Titlepane, BorderLayout.PAGE_START);
        
           CreatPopupMenuOnRightClick(pmidDate,Abstract,Result,Figures,higlightedWordslistModel,Title,Proteinhigliter,NonProteinhigliter,AddedProteinhigliter,
                jt2,frame,matchingLables,lable1higliter,AllLables,workspace, excel,suggestedProteinhiliter);
        
        //add(tabbedPane);
        JPanel jPanel = new JPanel(new GridLayout(2, 6));
        JRadioButton SpecialCase;
        JRadioButton NOSpecialCase;
        ButtonGroup buttonGroup = new ButtonGroup();
        SpecialCase = new JRadioButton("SpecialCase");
        buttonGroup.add(SpecialCase);
        SpecialCase.setSelected(true);
        NOSpecialCase = new JRadioButton("NO SpecialCase");
        buttonGroup.add(NOSpecialCase);
        NOSpecialCase.setSelected(false);
        SpecialCase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                specialCase=true;
                RenderFrame(pmidDate,Abstract,Result,Figures,higlightedWordslistModel,Title,Proteinhigliter,AddedProteinhigliter,jt2,matchingLables,lable1higliter,AllLables,workspace,excel,suggestedProteinhiliter);
                
            }
        });
        
        //add disallow listener
        NOSpecialCase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                specialCase=false;
                RenderFrame(pmidDate,Abstract,Result,Figures,higlightedWordslistModel,Title,Proteinhigliter,AddedProteinhigliter,jt2,matchingLables,lable1higliter,AllLables,workspace,excel,suggestedProteinhiliter);
                
            }
        });
        
        // ButtonGroup.
        jPanel.add(SpecialCase);
        jPanel.add(NOSpecialCase);
        
      
        Action nextabsAction = new AbstractAction() {
            
            @Override
            
            public void actionPerformed(ActionEvent event) {
                
                try {
                    if (abstracts.size()==0)
                    {
                        JOptionPane.showMessageDialog(null, "you did not loaded an abstract yet\n you should load an abstract first");
                    }
                    else if ((abstractindex+1)==abstracts.size())
                    {
                        JOptionPane.showMessageDialog(null, "there is no next abstract");
                    }
                    else {
                        abstractindex++;
                        
                        RenderFrame(pmidDate,Abstract,Result,Figures,higlightedWordslistModel,Title,Proteinhigliter,AddedProteinhigliter,jt2,matchingLables,lable1higliter,AllLables,workspace,excel,suggestedProteinhiliter);
                    }
                } catch (Exception e1) {
                    
                    e1.printStackTrace();
                    
                    
                }
                
            }
            
            
        };
        
        nextabsAction.putValue(Action.NAME, "Next Abstract");
        
        JButton nextAbsButton = new JButton(nextabsAction);
        /*      component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F2"),
        * action.getValue(Action.NAME));*/
        nextAbsButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed RIGHT"), nextabsAction.getValue(Action.NAME));
        nextAbsButton.getActionMap().put( nextabsAction.getValue(Action.NAME),nextabsAction);
        //  nextAbsButton.getInputMap(Abstract.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed RIGHT"), nextabsAction.getValue(Action.NAME));
        jPanel.add(nextAbsButton);
        
        
        
        Action prevabsAction = new AbstractAction() {
            
            @Override
            
            public void actionPerformed(ActionEvent event) {
                
                try {
                    if (abstracts.size()==0)
                    {
                        JOptionPane.showMessageDialog(null, "you did not loaded an abstract yet\n you should load an abstract first");
                    }
                    else if ((abstractindex)==0)
                    {
                        JOptionPane.showMessageDialog(null, "there is no prev abstract");
                    }
                    else {
                        abstractindex--;
                        RenderFrame(pmidDate,Abstract,Result,Figures,higlightedWordslistModel,Title,Proteinhigliter,AddedProteinhigliter,jt2,matchingLables,lable1higliter,AllLables,workspace,excel,suggestedProteinhiliter);
                    }
                } catch (Exception e1) {
                    
                    e1.printStackTrace();
                    
                }
                
            }
            
        };
        
        prevabsAction.putValue(Action.NAME, "Prev Abstract");
        
        JButton prevAbsButton = new JButton(prevabsAction);
        prevAbsButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed LEFT"), prevabsAction.getValue(Action.NAME));
          // prevAbsButton.getInputMap(Abstract.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed LEFT"), prevabsAction.getValue(Action.NAME));
        prevAbsButton.getActionMap().put( prevabsAction.getValue(Action.NAME),prevabsAction);
        
        /*     prevAbsButton.getInputMap().put(KeyStroke.getKeyStroke("pressed LEFT"),"doSomething");
        * prevAbsButton.getActionMap().put("doSomething",prevabsAction);*/
        jPanel.add(prevAbsButton);
        
        
        Action autoabsAction = new AbstractAction() {
            
            @Override
            
            public void actionPerformed(ActionEvent event) {
                
                try {
                    frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    ArrayList<String> allsuggestedwords= new  ArrayList<String>();
                       long startTime = System.currentTimeMillis();
                    while (abstractindex<abstracts.size()-1)
                        
                    {
                        abstractindex++;
          //   allsuggestedwords.addAll(  RenderFrame_Simulation(pmidDate,Abstract,higlightedWordslistModel,Title,Proteinhigliter,AddedProteinhigliter,jt2,matchingLables,lable1higliter,AllLables,workspace,excel,suggestedProteinhiliter)); //simulation test
                      RenderFrame(pmidDate,Abstract,Result,Figures,higlightedWordslistModel,Title,Proteinhigliter,AddedProteinhigliter,jt2,matchingLables,lable1higliter,AllLables,workspace,excel,suggestedProteinhiliter);     //abstractindex++;
                     
//        if (abstractindex % 4000 == 0)
//        {
//              System.out.println(allsuggestedwords);
//              allsuggestedwords= new  ArrayList<String>();
//	
//       // startTime = System.currentTimeMillis();
//        }
                    }
                    System.out.println(allsuggestedwords);
              allsuggestedwords= new  ArrayList<String>();
                       long endTime = System.currentTimeMillis();
	long elapsedTime = endTime - startTime;
        System.out.println("rendering 200 frame number till "+abstractindex+" is taking "+elapsedTime+" ms.");
                    frame.setCursor(Cursor.getDefaultCursor());
                  
                    //  abstractindex--;
                } catch (Exception e1) {
                    
                    e1.printStackTrace();
                    
                }
                
            }
            
        };
        
        autoabsAction.putValue(Action.NAME, "Auto Generate results");
        
        JButton autoabsButton = new JButton(autoabsAction);
        
        jPanel.add(autoabsButton);
        
        Action gotoabsAction = new AbstractAction() {
            
            @Override
            
            public void actionPerformed(ActionEvent event) {
                
                String input1 = jt1.getText();
                int abstractnumber = Integer.parseInt(input1);
                if (abstractnumber>0 && abstractnumber<=abstracts.size() )
                {
                    abstractindex =abstractnumber-1;
                    RenderFrame(pmidDate,Abstract,Result,Figures,higlightedWordslistModel,Title,Proteinhigliter,AddedProteinhigliter,jt2,matchingLables,lable1higliter,AllLables,workspace,excel,suggestedProteinhiliter);
                }
                
            }
            
        };
        
        gotoabsAction.putValue(Action.NAME, "go to abstract");
        
        JButton gotoabsButton = new JButton(gotoabsAction);
        
        jPanel.add(gotoabsButton);
        jPanel.add(jt1);
        jPanel.add(jt2);
        
        
        content.add(jPanel, BorderLayout.PAGE_END);
        //    jPanel.setMinimumSize(new Dimension(10,10));
        //    jPanel.setSize(new Dimension(800,800));
        
          higlightedWords.addMouseListener(new MouseAdapter() {
    public void mouseClicked(MouseEvent evt) {
        JList list = (JList)evt.getSource();
        if (evt.getClickCount() == 2) {
           String annotation= (String)list.getSelectedValue();
 if (annotationsLocations==null)
 {
         frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
           
FillAnnotationsLocations(pmidDate,Abstract,higlightedWordslistModel,Title,Proteinhigliter,AddedProteinhigliter,jt2,matchingLables,lable1higliter,AllLables,workspace,excel);
 
     frame.setCursor(Cursor.getDefaultCursor());
       RenderFrame(pmidDate,Abstract,Result,Figures,higlightedWordslistModel,Title,Proteinhigliter,AddedProteinhigliter,jt2,matchingLables,lable1higliter,AllLables,workspace,excel,suggestedProteinhiliter);
 }
 else {
     statistics.setText("this annotation existe in "+annotationsLocations.get(annotation).size()+" abstracts\n abstracts numbers are \n "+annotationsLocations.get(annotation));
 //   JOptionPane.showMessageDialog(null, annotationsLocations.get(annotation));
 }
        
        } 
    }
});
        
      
        Action AddSelectionProteinKeyborad = new AbstractAction( ) {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String selection=Abstract.getSelectedText();
                AddLabel( selection,  pmidDate,  Abstract,Result, Figures, higlightedWordslistModel, Title, Proteinhigliter, NonProteinhigliter,AddedProteinhigliter,jt2,
                            matchingLables,lable1higliter,AllLables,workspace,excel,ProteinFileName,0,false,suggestedProteinhiliter);
                RenderFrame(pmidDate,Abstract,Result,Figures,higlightedWordslistModel,Title,Proteinhigliter,AddedProteinhigliter,jt2,matchingLables,lable1higliter,AllLables,workspace,excel,suggestedProteinhiliter); 
            }
        };
        
        Action AddProteinKeyborad = new AbstractAction( ) {
            
            @Override
            
            public void actionPerformed(ActionEvent event) {
                
                String selection=Abstract.getSelectedText();
                if (selection!=null && !selection.equals(""))
                {
                    int numberofWords=6;
                    if (event.getActionCommand()!=null)
                    {
                        numberofWords= Integer.parseInt(event.getActionCommand());
                    }
                    
                    if(selection==null){
                        return;
                    }
                    String [] Words =StringUtils.split(selection);
                    ArrayList<String> wordArrayList = new ArrayList<String>();
                    String[] splited= new String [numberofWords];
                    String ProteinName="";
                    for (int k=0;k<numberofWords;k++)
                    {
                        splited[k]=Words[Words.length-numberofWords+k];
                        if (k==numberofWords-1)
                        {
                            ProteinName=ProteinName+Words[Words.length-numberofWords+k];
                        }
                        else {
                            ProteinName=ProteinName+Words[Words.length-numberofWords+k]+" ";
                        }
                    }
                     AddLabel( ProteinName,  pmidDate,  Abstract, Result,Figures, higlightedWordslistModel, Title, Proteinhigliter, NonProteinhigliter,AddedProteinhigliter,jt2,
                            matchingLables,lable1higliter,AllLables,workspace,excel,ProteinFileName,0,false,suggestedProteinhiliter);

                }
            }
            
        };
        
        Action RemoveProteinKeyborad = new AbstractAction( ) {
            
            @Override
            
            public void actionPerformed(ActionEvent event) {
                String selection=Abstract.getSelectedText();
                RemoveLabel( selection,  pmidDate,  Abstract, Result,Figures, higlightedWordslistModel, Title, Proteinhigliter, NonProteinhigliter,AddedProteinhigliter,jt2,
                        matchingLables, lable1higliter,AllLables,workspace, excel,ProteinFileName,0,false,suggestedProteinhiliter);
               
            }
        };
        
        
        Action RemoveAbstract = new AbstractAction( ) {
            
            @Override
            
            public void actionPerformed(ActionEvent event) {
                  excel.AddProteinToList(workspace+"\\exludedAbstracts.xlsx", abstracts.get(abstractindex).get("PMID"));
                excel.RemovePMIDFromResults(workspace+"\\results.xlsx", abstracts.get(abstractindex).get("PMID"));
                 RenderFrame(pmidDate,Abstract,Result,Figures,higlightedWordslistModel,Title,Proteinhigliter,AddedProteinhigliter,jt2,matchingLables,lable1higliter,AllLables,workspace,excel,suggestedProteinhiliter);
               
            }
        };
        
        Action RemoveLocalyProteinKeyborad = new AbstractAction( ) {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                
                String selection=Abstract.getSelectedText();
                
                if(selection==null){
                    return;
                }
                // unhiglight localy
                Proteinhigliter.removeHighlights(Abstract,selection,abstracts.get(abstractindex).get("PMID"),abstracts.get(abstractindex).get("DATE"),"PROTEIN",workspace);
                ACTIONS currentAction= new ACTIONS(3,"removing protein locally","","",0);
                
                currentAction.releatedWord=selection;
                lastAcion.add(currentAction);
                //  lastAcion=ACTIONS.REMOVELOCALLY;
                //    lastAcion.releatedWord=selection;
                RenderFrame(pmidDate,Abstract,Result,Figures,higlightedWordslistModel,Title,Proteinhigliter,AddedProteinhigliter,jt2,matchingLables,lable1higliter,AllLables,workspace,excel,suggestedProteinhiliter);
                
                
            }
        };
        
        Action UndoKeyborad = new AbstractAction( ) {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                
                if(lastAcion.size()>0)
                {
                    int reply = JOptionPane.showConfirmDialog(null, "your last move was "+lastAcion.get(lastAcion.size()-1).fullName+"\n are you sure you want to undo it", lastAcion.get(lastAcion.size()-1).releatedWord, JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        
                        if (lastAcion.get(lastAcion.size()-1).value==1)
                        {
                            RemoveLabel( lastAcion.get(lastAcion.size()-1).releatedWord,  pmidDate,  Abstract,Result, Figures, higlightedWordslistModel, Title, Proteinhigliter, NonProteinhigliter,AddedProteinhigliter,jt2,
                                    matchingLables,lable1higliter,AllLables,workspace,excel,lastAcion.get(lastAcion.size()-1).label,lastAcion.get(lastAcion.size()-1).labelindex,true,suggestedProteinhiliter);
                        }
                        else if (lastAcion.get(lastAcion.size()-1).value==4)
                        {
                            
                            AddLabel( lastAcion.get(lastAcion.size()-1).releatedWord,  pmidDate,  Abstract, Result,Figures, higlightedWordslistModel, Title, Proteinhigliter, NonProteinhigliter,AddedProteinhigliter,jt2,
                                    matchingLables,lable1higliter,AllLables,workspace,excel,lastAcion.get(lastAcion.size()-1).label,lastAcion.get(lastAcion.size()-1).labelindex,true,suggestedProteinhiliter);
                        }
                        else if (lastAcion.get(lastAcion.size()-1).value==3)
                        {
                            excel.RemoveProteinRecordFromFile(workspace+"\\LOCALNonProtein.xlsx",abstracts.get(abstractindex).get("PMID"),abstracts.get(abstractindex).get("DATE"),lastAcion.get(lastAcion.size()-1).releatedWord);
                            
                            //  Proteinhigliter.NotremoveHighlights(Abstract,lastAcion.get(lastAcion.size()-1).releatedWord,abstracts.get(abstractindex).get("PMID"),abstracts.get(abstractindex).get("DATE"),"PROTEIN",workspace);
                            
                            RenderFrame(pmidDate,Abstract,Result,Figures,higlightedWordslistModel,Title,Proteinhigliter,AddedProteinhigliter,jt2,matchingLables,lable1higliter,AllLables,workspace,excel,suggestedProteinhiliter);
                            
                        }
                        lastAcion.remove(lastAcion.size()-1);
                    }
                    
                    
                }
                else {
                    JOptionPane.showMessageDialog(null, "No more Actions to undo");
                }
            }
        };
        
        
        // Create KeyStroke that will be used to invoke the action.
        KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_DOWN_MASK);
        KeyStroke keyStroke2 = KeyStroke.getKeyStroke( KeyEvent.VK_2 , InputEvent.CTRL_DOWN_MASK );
        KeyStroke keyStroke3 = KeyStroke.getKeyStroke( KeyEvent.VK_3 , InputEvent.CTRL_DOWN_MASK );
        KeyStroke keyStroke4 = KeyStroke.getKeyStroke( KeyEvent.VK_4 , InputEvent.CTRL_DOWN_MASK );
        KeyStroke keyStroke5 = KeyStroke.getKeyStroke( KeyEvent.VK_5 , InputEvent.CTRL_DOWN_MASK );
        KeyStroke keyStroke6 = KeyStroke.getKeyStroke( KeyEvent.VK_6 , InputEvent.CTRL_DOWN_MASK );
        KeyStroke keyStroke7 = KeyStroke.getKeyStroke( KeyEvent.VK_7 , InputEvent.CTRL_DOWN_MASK );
        
        KeyStroke keyStroke8 = KeyStroke.getKeyStroke( KeyEvent.VK_8 , InputEvent.CTRL_DOWN_MASK );
        KeyStroke keyStroke9 = KeyStroke.getKeyStroke( KeyEvent.VK_9 , InputEvent.CTRL_DOWN_MASK );
        
        KeyStroke keyStrokeS = KeyStroke.getKeyStroke( KeyEvent.VK_S , InputEvent.CTRL_DOWN_MASK );
        
        KeyStroke keyStrokeR = KeyStroke.getKeyStroke( KeyEvent.VK_R , InputEvent.CTRL_DOWN_MASK );
        
        
        KeyStroke keyStrokeL = KeyStroke.getKeyStroke( KeyEvent.VK_L , InputEvent.CTRL_DOWN_MASK );
         KeyStroke keyStrokeD = KeyStroke.getKeyStroke( KeyEvent.VK_D , InputEvent.CTRL_DOWN_MASK );
        
        KeyStroke keyStrokeUndo = KeyStroke.getKeyStroke( KeyEvent.VK_Z , InputEvent.CTRL_DOWN_MASK );
        // Register Action in component's ActionMap.
        Abstract.getActionMap().put("Do It", AddProteinKeyborad);
        Abstract.getActionMap().put("ADD Selection", AddSelectionProteinKeyborad);
        Abstract.getActionMap().put("Remove Selection", RemoveProteinKeyborad);
         Abstract.getActionMap().put("Remove Abstract", RemoveAbstract);
        Abstract.getActionMap().put("Undo", UndoKeyborad);
        
        Abstract.getActionMap().put("Remove Lcally", RemoveLocalyProteinKeyborad);
        
        
        // Now register KeyStroke used to fire the action.  I am registering this with the
        // InputMap used when the component's parent window has focus.
        Abstract.getInputMap(Abstract.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, "Do It");
        Abstract.getInputMap(Abstract.WHEN_IN_FOCUSED_WINDOW).put(keyStroke2, "Do It");
        Abstract.getInputMap(Abstract.WHEN_IN_FOCUSED_WINDOW).put(keyStroke3, "Do It");
        Abstract.getInputMap(Abstract.WHEN_IN_FOCUSED_WINDOW).put(keyStroke4, "Do It");
        Abstract.getInputMap(Abstract.WHEN_IN_FOCUSED_WINDOW).put(keyStroke5, "Do It");
        Abstract.getInputMap(Abstract.WHEN_IN_FOCUSED_WINDOW).put(keyStroke6, "Do It");
        Abstract.getInputMap(Abstract.WHEN_IN_FOCUSED_WINDOW).put(keyStroke7, "Do It");
        Abstract.getInputMap(Abstract.WHEN_IN_FOCUSED_WINDOW).put(keyStroke8, "Do It");
        Abstract.getInputMap(Abstract.WHEN_IN_FOCUSED_WINDOW).put(keyStroke9, "Do It");
        Abstract.getInputMap(Abstract.WHEN_IN_FOCUSED_WINDOW).put(keyStrokeS, "ADD Selection");
        Abstract.getInputMap(Abstract.WHEN_IN_FOCUSED_WINDOW).put(keyStrokeR, "Remove Selection");
        Abstract.getInputMap(Abstract.WHEN_IN_FOCUSED_WINDOW).put(keyStrokeL, "Remove Lcally");
         Abstract.getInputMap(Abstract.WHEN_IN_FOCUSED_WINDOW).put(keyStrokeD, "Remove Abstract");
        
        Abstract.getInputMap(Abstract.WHEN_IN_FOCUSED_WINDOW).put(keyStrokeUndo, "Undo");
//        frame.add(Abstractpane);
        frame.add(tabbedPane);
      
        frame.pack();
          Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
 
        frame.setSize(1000, 600);
        //  frame.pack();
        frame.setVisible(true);
        //    frame.setMinimumSize(frame.getSize());
        //'public is the word to highligh'
               RenderFrame(pmidDate,Abstract,Result,Figures,higlightedWordslistModel,Title,Proteinhigliter,AddedProteinhigliter,jt2,matchingLables,lable1higliter,AllLables,workspace,excel,suggestedProteinhiliter);
//             
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            
            public void run() {
                
                excel.close(workspace+"\\results.xlsx");
                // Do what you want when the application is stopping
            }
        }));
        
    }
 
    /**
     *
     * @param pmidDate
     * @param Abstract
     * @param higlightedWordslistModel
     * @param Title
     * @param Proteinhigliter
     * @param AddedProteinhigliter
     * @param jt2
     * @param matchingLables
     * @param lable1higliter
     * @param AllLables
     * @param workspace
     * @param excel
     */
    private static  void RenderFrame(JTextArea  pmidDate,JTextArea  Abstract,JTextArea  Result,JTextArea  Figures,DefaultListModel  higlightedWordslistModel,JTextArea Title,HighlightPainter Proteinhigliter, HighlightPainter AddedProteinhigliter,JTextField jt2,
            ArrayList  <Multimap  <String,ArrayList<String>>> matchingLables, ArrayList<HighlightPainter>  lable1higliter, ArrayList <String> AllLables,String workspace,
            ExcelReader excel,HighlightPainter suggestedProteinhiliter)
    {
           long startTime = System.currentTimeMillis();
        boolean addToResult=true;
        higlightedWordslistModel.clear();
        Proteinhigliter.removeHighlights(Abstract);
        pmidDate.setText("PMID: "+abstracts.get(abstractindex).get("PMID")+ "\nDate: "+abstracts.get(abstractindex).get("DATE"));
 Result.setText("Result: "+abstracts.get(abstractindex).get("Result"));
          Figures.setText(abstracts.get(abstractindex).get("Figures"));
        Abstract.setText("\nTitle: "+abstracts.get(abstractindex).get("Title")+"\n ABSTRACT: "+abstracts.get(abstractindex).get("Abstract"));
        // frame.setTitle("Abstract "+(abstractindex+1)+" of "+abstracts.size());
        jt2.setText((abstractindex+1)+" of "+abstracts.size());
        //     higlightedWordslistModel.add.setText( "Highlighted words:\n");
        ArrayList <String> alreadyHighlightWord=new ArrayList();
        alreadyHighlightWord=  Proteinhigliter.highlight_version2(abstracts.get(abstractindex).get("PMID"),abstracts.get(abstractindex).get("DATE"),
                Abstract, matchingWords,Proteinhigliter,AddedProteinhigliter,"PROTEIN",specialCase,workspace,excel,addToResult,"Abstract");
      if (alreadyHighlightWord==null)
      {
          Abstract.setEnabled(false);
          alreadyHighlightWord=new ArrayList();
      }
      else {
        Abstract.setEnabled(true);
      }
       ArrayList <String> alreadyHighlightWordResult=new ArrayList();



        alreadyHighlightWordResult=  Proteinhigliter.highlight_version2(abstracts.get(abstractindex).get("PMID"),abstracts.get(abstractindex).get("DATE"),
                Result, matchingWords,Proteinhigliter,AddedProteinhigliter,"PROTEIN",specialCase,workspace,excel,addToResult,"Result");
         ArrayList <String> alreadyHighlightWordFigures=new ArrayList();
        alreadyHighlightWordFigures=  Proteinhigliter.highlight_version2(abstracts.get(abstractindex).get("PMID"),abstracts.get(abstractindex).get("DATE"),
                Figures, matchingWords,Proteinhigliter,AddedProteinhigliter,"PROTEIN",specialCase,workspace,excel,addToResult,"Figure Caption");

     //  ArrayList <String> RETURNSuggestedWords=new ArrayList();
         ArrayList <String> SuggestedWords=new ArrayList();
          ArrayList <String> SuggestedWords_2=new ArrayList();
             ArrayList <String> SuggestedWords_3=new ArrayList();
        SuggestedWords=  Proteinhigliter.highlight_version2_suggestion(abstracts.get(abstractindex).get("PMID"),abstracts.get(abstractindex).get("DATE"),
                Abstract, SimilarWords,suggestedProteinhiliter,AddedProteinhigliter,"PROTEIN",specialCase,workspace,excel,false);
         SuggestedWords_2=  Proteinhigliter.highlight_version2_2words(abstracts.get(abstractindex).get("PMID"),abstracts.get(abstractindex).get("DATE"),
                Abstract, SimilarWords_2,suggestedProteinhiliter,AddedProteinhigliter,"PROTEIN",specialCase,workspace,excel,false);
           SuggestedWords_3=  Proteinhigliter.highlight_version2_3words(abstracts.get(abstractindex).get("PMID"),abstracts.get(abstractindex).get("DATE"),
                Abstract, SimilarWords_3,suggestedProteinhiliter,AddedProteinhigliter,"PROTEIN",specialCase,workspace,excel,false);
     
        Title.setText("\nTitle: "+abstracts.get(abstractindex).get("Title"));
        
        if (matchingLables!=null && lable1higliter!=null)
        {
            for (int i=0;i<lable1higliter.size();i++)
            {
                   ArrayList <String> alreadyHighlightWord2=new ArrayList();
                final String [] Splited=StringUtils.split(AllLables.get(i), "/");
             alreadyHighlightWord2=   Proteinhigliter.highlight_version2(abstracts.get(abstractindex).get("PMID"),abstracts.get(abstractindex).get("DATE"),Abstract,
                        matchingLables.get(i),lable1higliter.get(i),lable1higliter.get(i),Splited[0],specialCase,workspace,excel,addToResult,"Abstract");
          alreadyHighlightWord.addAll(alreadyHighlightWord2);
            }
        }
         AddToHighlightedWordsSection(alreadyHighlightWord,higlightedWordslistModel);
        Abstract.getCaret().setVisible(true);
        Abstract.getCaret().setSelectionVisible(true);
       // return alreadyHighlightWord;
        
//            long endTime = System.currentTimeMillis();
//	long elapsedTime = endTime - startTime;
//        if (abstractindex % 200 == 0)
//        {
//	System.out.println("rendering frame number "+abstractindex+" is taking "+elapsedTime+" ms.");
//        }
//         if (SuggestedWords_3.size()>0)
//       {
//             SuggestedWords.addAll(SuggestedWords_3);
//      //    for (int h=0;h<SuggestedWords_3.size();h++)
//      //    {
//     //  System.out.println("**************************"+SuggestedWords_3.get(h));
//         // }
//       }
//          if (SuggestedWords_2.size()>0)
//       {
//           SuggestedWords.addAll(SuggestedWords_2);
////           for (int h=0;h<SuggestedWords_2.size();h++)
////           {
////       System.out.println(SuggestedWords_2.get(h));
////           }
//       }
//            if (SuggestedWords.size()>0)
//       {
//           for (int h=0;h<SuggestedWords.size();h++)
//           {
//       System.out.println(SuggestedWords.get(h));
//           }
//       }
       //   return SuggestedWords;
        if (allFiguresabstracts !=null)
        {
            int tabscount=tabbedPane.getTabCount();
            for (int j=tabscount;j>3;j--)
            {
                tabbedPane.remove(j-1);//.remove(j);
            }
            
            for (int i=0;i<allFiguresabstracts.get(abstractindex).getImages().size();i++)
            {
         JLabel label = new JLabel(new ImageIcon(allFiguresabstracts.get(abstractindex).getImages().get(i)));
           tabbedPane.addTab("Figure "+i, null, new JScrollPane(label),
                "Does nothing");
            }
       //  f.getContentPane().add(label);
        }
    }
    
    private static  ArrayList <String> RenderFrame_Simulation(JTextArea  pmidDate,JTextArea  Abstract,DefaultListModel  higlightedWordslistModel,JTextArea Title,HighlightPainter Proteinhigliter, HighlightPainter AddedProteinhigliter,JTextField jt2,
            ArrayList  <Multimap  <String,ArrayList<String>>> matchingLables, ArrayList<HighlightPainter>  lable1higliter, ArrayList <String> AllLables,String workspace,  ExcelReader excel,HighlightPainter suggestedProteinhiliter)
    {
        
        boolean addToResult=true;
       // higlightedWordslistModel.clear();
       // Proteinhigliter.removeHighlights(Abstract);
      //  pmidDate.setText("PMID: "+abstracts.get(abstractindex).get("PMID")+ "\nDate: "+abstracts.get(abstractindex).get("DATE"));
        Abstract.setText("\nTitle: "+abstracts.get(abstractindex).get("Title")+"\n ABSTRACT: "+abstracts.get(abstractindex).get("Abstract"));
        // frame.setTitle("Abstract "+(abstractindex+1)+" of "+abstracts.size());
        jt2.setText((abstractindex+1)+" of "+abstracts.size());
        //     higlightedWordslistModel.add.setText( "Highlighted words:\n");
      //  ArrayList <String> alreadyHighlightWord=new ArrayList();
        //alreadyHighlightWord=  Proteinhigliter.highlight_version2(abstracts.get(abstractindex).get("PMID"),abstracts.get(abstractindex).get("DATE"),
            //    Abstract, matchingWords,Proteinhigliter,AddedProteinhigliter,"PROTEIN",specialCase,workspace,excel,addToResult);
       //  ArrayList <String> RETURNSuggestedWords=new ArrayList();
         ArrayList <String> SuggestedWords=new ArrayList();
          ArrayList <String> SuggestedWords_2=new ArrayList();
             ArrayList <String> SuggestedWords_3=new ArrayList();
        SuggestedWords=  Proteinhigliter.highlight_version2_suggestion(abstracts.get(abstractindex).get("PMID"),abstracts.get(abstractindex).get("DATE"),
                Abstract, SimilarWords,suggestedProteinhiliter,AddedProteinhigliter,"PROTEIN",specialCase,workspace,excel,false);
         SuggestedWords_2=  Proteinhigliter.highlight_version2_simulation(abstracts.get(abstractindex).get("PMID"),abstracts.get(abstractindex).get("DATE"),
                Abstract, SimilarWords_2,suggestedProteinhiliter,AddedProteinhigliter,"PROTEIN",specialCase,workspace,excel,false,"Abstract");
           SuggestedWords_3=  Proteinhigliter.highlight_version2_simulation(abstracts.get(abstractindex).get("PMID"),abstracts.get(abstractindex).get("DATE"),
                Abstract, SimilarWords_3,suggestedProteinhiliter,AddedProteinhigliter,"PROTEIN",specialCase,workspace,excel,false,"Abstract");
     
        Title.setText("\nTitle: "+abstracts.get(abstractindex).get("Title"));
        
//        if (matchingLables!=null && lable1higliter!=null)
//        {
//            for (int i=0;i<lable1higliter.size();i++)
//            {
//                   ArrayList <String> alreadyHighlightWord2=new ArrayList();
//                final String [] Splited=StringUtils.split(AllLables.get(i), "/");
//             alreadyHighlightWord2=   Proteinhigliter.highlight_version2(abstracts.get(abstractindex).get("PMID"),abstracts.get(abstractindex).get("DATE"),Abstract,
//                        matchingLables.get(i),lable1higliter.get(i),lable1higliter.get(i),Splited[0],specialCase,workspace,excel,addToResult);
//          alreadyHighlightWord.addAll(alreadyHighlightWord2);
//            }
//        }
//         AddToHighlightedWordsSection(alreadyHighlightWord,higlightedWordslistModel);
//        Abstract.getCaret().setVisible(true);
//        Abstract.getCaret().setSelectionVisible(true);
       // return alreadyHighlightWord;
        
          
         if (SuggestedWords_3.size()>0)
       {
             SuggestedWords.addAll(SuggestedWords_3);
      //    for (int h=0;h<SuggestedWords_3.size();h++)
      //    {
     //  System.out.println("**************************"+SuggestedWords_3.get(h));
         // }
       }
          if (SuggestedWords_2.size()>0)
       {
           SuggestedWords.addAll(SuggestedWords_2);
//           for (int h=0;h<SuggestedWords_2.size();h++)
//           {
//       System.out.println(SuggestedWords_2.get(h));
//           }
       }
//            if (SuggestedWords.size()>0)
//       {
//           for (int h=0;h<SuggestedWords.size();h++)
//           {
//       System.out.println(SuggestedWords.get(h));
//           }
//       }
          return SuggestedWords;
    }
    
    
    


    /**
     *
     * @param excelfie excel file path of the protein database
     * @param Protein protein name
     */
    private static void AddToProteinDatabase(String excelfie,String Protein)
    {
        ExcelReader excel=new ExcelReader();
        excel.AddProteinToList(excelfie,Protein);
    }
    private static void AddLableToDatabase(String excelfie,String Lable,String Color)
    {
        ExcelReader excel=new ExcelReader();
        excel.AddLableToList(excelfie,Lable,Color);
    }
    private static void RemoveProteinFromDatabase(String excelfie,String Protein)
    {
        ExcelReader excel=new ExcelReader();
        excel.RemoveProteinFromDatabase(excelfie, Protein);
        
    }
    private static void RemoveProteinFromResult(String Protein)
    {
        ExcelReader excel=new ExcelReader();
        excel.RemoveProteinFromResults(Protein);
        
    }
    /**
     * fill the list on the right that contain highlighted words
     * @param alreadyHighlightWord list of highlighted words
     * @param higlightedWordslistModel
     */
    private static void AddToHighlightedWordsSection(ArrayList<String> alreadyHighlightWord, DefaultListModel higlightedWordslistModel) {
        
        for (int i=0; i<alreadyHighlightWord.size();i++)
        {
            higlightedWordslistModel.addElement(alreadyHighlightWord.get(i));
            
        }
        
    }
    
    /**
     * handle the pop up menu options
     * @param jep the current loaded text are
     * @param Proteinhigliter the protein highlighter needed if we want to add a protein
     * @param NonProteinhigliter the non protein highlighter needed if we want to lable a non protein
     */
    private static void CreatPopupMenuOnRightClick(final JTextArea  pmidDate,final JTextArea  Abstract,final JTextArea  Result,final JTextArea  Figures,final DefaultListModel  higlightedWordslistModel,
            final JTextArea Title,final HighlightPainter Proteinhigliter,final HighlightPainter NonProteinhigliter,final HighlightPainter AddedProteinhigliter,
            final JTextField jt2, final JFrame frame,final ArrayList  <Multimap  <String,ArrayList<String>>> matchingLables,
            final ArrayList<HighlightPainter> lable1higliter,final ArrayList <String> AllLables,final String workspace,final ExcelReader excel,final HighlightPainter suggestedProteinhiliter)
    {
        RenderPopupMenu(pmidDate,Abstract,Result,Figures,higlightedWordslistModel,Title,Proteinhigliter,NonProteinhigliter,AddedProteinhigliter,jt2,frame,matchingLables,
                lable1higliter,AllLables,workspace,excel,suggestedProteinhiliter);
        addProtein.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(e.getActionCommand().contains("add to protein table"))
                {                    
                    String selection=Abstract.getSelectedText();                 
                    AddLabel( selection,  pmidDate,  Abstract, Result,Figures, higlightedWordslistModel, Title, Proteinhigliter, NonProteinhigliter,AddedProteinhigliter,jt2,
                            matchingLables,lable1higliter,AllLables,workspace,excel,ProteinFileName,0,false,suggestedProteinhiliter);  
                }             
            }
        });
        addNewLable.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String input =  JOptionPane.showInputDialog("Enter the new lable name" ,"");
                if (input !=null)
                {
                    try {
                        String[] choices = { "ORANGE", "BLACK", "BLUE", "MAGENTA", "PINK", "CYAN", "GRAY"};
                        String input2 = (String) JOptionPane.showInputDialog(null, "Please choose the desiered Color For this lable",
                                "The Choice of of the lable Color", JOptionPane.QUESTION_MESSAGE, null, // Use             
                                choices, // Array of choices
                                choices[1]); // Initial choice
                        AddLableToDatabase(workspace+"\\Labels.xlsx",input,input2);
                        String filename=workspace+"\\"+input+".xlsx" ;
                        XSSFWorkbook hwb=new XSSFWorkbook();
                        Sheet sheet = hwb.createSheet();
                        FileOutputStream fileOut =  new FileOutputStream(filename);
                        hwb.write(fileOut);
                        fileOut.close();
                        AllLables.add(input+"/"+input2);        
                            Color [] lable1Color= new Color[1];
                Field field = Color.class.getField(input2);
                lable1Color[0] = (Color)field.get(null);
           lable1higliter.add(new HighlightPainter(lable1Color[0]));
                matchingLables.add(AllLables.size()-1, excel.GetAllProtein(workspace+"\\"+input+".xlsx"));
                   //     final ArrayList<HighlightPainter>  lable1higliter=new HighlightPainter[AllLables.size()];
                        CreatPopupMenuOnRightClick(pmidDate,Abstract,Result,Figures,higlightedWordslistModel,Title,Proteinhigliter,NonProteinhigliter,AddedProteinhigliter,jt2,
                                frame,matchingLables,lable1higliter,AllLables,workspace,excel,suggestedProteinhiliter);
                    } catch (IOException ex) {
                        Logger.getLogger(SAMNAFrame.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(SAMNAFrame.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(SAMNAFrame.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NoSuchFieldException ex) {
                        Logger.getLogger(SAMNAFrame.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SecurityException ex) {
                        Logger.getLogger(SAMNAFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }  
        });
        removeLocaly.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (e.getActionCommand().contains("remove label locally"))
                {
                    String selection=Abstract.getSelectedText();
                    if(selection==null){
                        return;
                    }
                    Proteinhigliter.removeHighlights(Abstract,selection,abstracts.get(abstractindex).get("PMID"),abstracts.get(abstractindex).get("DATE"),"PROTEIN",workspace);
                    ACTIONS currentAction= new ACTIONS(3,"removing protein locally","","",0);
                    currentAction.releatedWord=selection;
                    lastAcion.add(currentAction);
                    RenderFrame(pmidDate,Abstract,Result,Figures,higlightedWordslistModel,Title,Proteinhigliter,AddedProteinhigliter,jt2,matchingLables,lable1higliter,AllLables,workspace,excel,suggestedProteinhiliter);
               }
            }
        });
        nonProtin.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(e.getActionCommand().contains("NOT A Protein")){
                    
                    String selection=Abstract.getSelectedText();
                    RemoveLabel( selection,  pmidDate,  Abstract,Result,Figures,  higlightedWordslistModel, Title, Proteinhigliter, NonProteinhigliter,AddedProteinhigliter,jt2,
                            matchingLables,lable1higliter,AllLables,workspace,excel,ProteinFileName,0,false,suggestedProteinhiliter);
                }
                
                
            }
        });
         removeAddedLable.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                    try {
                        Object[] choices =  AllLables.toArray();
                        if (AllLables.size()==0)
                        {
                           JOptionPane.showMessageDialog(null, "there is no new label to delete");
                        }
                        else if (AllLables.size()==1)
                        {
                         int reply = JOptionPane.showConfirmDialog(null, "there is only one new label do you want to delete it ?","delete last new label", JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION){
                     final String [] Splited=StringUtils.split(AllLables.get(0), "/");
                        String color=excel.RemoveLabelFromDatabase(workspace+"\\Labels.xlsx",Splited[0]);
                          boolean success = (new File (workspace+"\\"+Splited[0]+".xlsx")).delete();
                       int index=AllLables.indexOf(0);
                          AllLables.remove(0);    

           lable1higliter.remove(0);
                matchingLables.remove(0);
                    }
                        }
                        else {
                        String input2 = (String) JOptionPane.showInputDialog(null, "Please choose the label you want to remove",
                                "Removing labels", JOptionPane.QUESTION_MESSAGE, null, // Use             
                                choices, // Array of choices
                                choices[1]); // Initial choice
                      final String [] Splited=StringUtils.split(input2, "/");
                        String color=excel.RemoveLabelFromDatabase(workspace+"\\Labels.xlsx",Splited[0]);
                          boolean success = (new File (workspace+"\\"+Splited[0]+".xlsx")).delete();
                       int index=AllLables.indexOf(input2);
                          AllLables.remove(input2);    

           lable1higliter.remove(index);
                matchingLables.remove(index);
                        }
                        RenderFrame(pmidDate,Abstract,Result,Figures,higlightedWordslistModel,Title,Proteinhigliter,AddedProteinhigliter,jt2,matchingLables,lable1higliter,AllLables,workspace,excel,suggestedProteinhiliter);
        
                   //     final ArrayList<HighlightPainter>  lable1higliter=new HighlightPainter[AllLables.size()];
                        CreatPopupMenuOnRightClick(pmidDate,Abstract,Result,Figures,higlightedWordslistModel,Title,Proteinhigliter,NonProteinhigliter,AddedProteinhigliter,jt2,
                                frame,matchingLables,lable1higliter,AllLables,workspace,excel,suggestedProteinhiliter);
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(SAMNAFrame.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SecurityException ex) {
                        Logger.getLogger(SAMNAFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
               
            }  
        });
         removeAbstract.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {               
                excel.AddProteinToList(workspace+"\\exludedAbstracts.xlsx", abstracts.get(abstractindex).get("PMID"));
                excel.RemovePMIDFromResults(workspace+"\\results.xlsx", abstracts.get(abstractindex).get("PMID"));
                 RenderFrame(pmidDate,Abstract,Result,Figures,higlightedWordslistModel,Title,Proteinhigliter,AddedProteinhigliter,jt2,matchingLables,lable1higliter,AllLables,workspace,excel,suggestedProteinhiliter);
        
            }
        });
         
        undoLastMove.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                
                if(lastAcion.size()>0)
                {
                    int reply = JOptionPane.showConfirmDialog(null, "your last move was "+lastAcion.get(lastAcion.size()-1).fullName+"\n are you sure you want to undo it", lastAcion.get(lastAcion.size()-1).releatedWord, JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        
                        if (lastAcion.get(lastAcion.size()-1).value==1)
                        {
                            RemoveLabel( lastAcion.get(lastAcion.size()-1).releatedWord,  pmidDate,  Abstract,Result,Figures,  higlightedWordslistModel, Title, Proteinhigliter, NonProteinhigliter,AddedProteinhigliter,jt2,
                                    matchingLables,lable1higliter,AllLables,workspace,excel,lastAcion.get(lastAcion.size()-1).label,lastAcion.get(lastAcion.size()-1).labelindex,true,suggestedProteinhiliter);
                        }
                        else if (lastAcion.get(lastAcion.size()-1).value==4)
                        {
                            
                            AddLabel( lastAcion.get(lastAcion.size()-1).releatedWord,  pmidDate,  Abstract,Result, Figures, higlightedWordslistModel, Title, Proteinhigliter, NonProteinhigliter,AddedProteinhigliter,jt2,
                                    matchingLables,lable1higliter,AllLables,workspace,excel,lastAcion.get(lastAcion.size()-1).label,lastAcion.get(lastAcion.size()-1).labelindex,true,suggestedProteinhiliter);
                        }
                        else if (lastAcion.get(lastAcion.size()-1).value==3)
                        {
                            excel.RemoveProteinRecordFromFile(workspace+"\\LOCALNonProtein.xlsx",abstracts.get(abstractindex).get("PMID"),abstracts.get(abstractindex).get("DATE"),lastAcion.get(lastAcion.size()-1).releatedWord);
                            
                            // Proteinhigliter.NotremoveHighlights(Abstract,lastAcion.get(lastAcion.size()-1).releatedWord,abstracts.get(abstractindex).get("PMID"),abstracts.get(abstractindex).get("DATE"),"PROTEIN",workspace);
                            
                            RenderFrame(pmidDate,Abstract,Result,Figures,higlightedWordslistModel,Title,Proteinhigliter,AddedProteinhigliter,jt2,matchingLables,lable1higliter,AllLables,workspace,excel,suggestedProteinhiliter);
                            
                        }
                        lastAcion.remove(lastAcion.size()-1);
                    }
                    
                    
                }
                else {
                    JOptionPane.showMessageDialog(null, "No more Actions to undo");
                }
            }
        });
        Abstract.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent me){
                if(me.isPopupTrigger()){
                    Pmenu.show(me.getComponent(), me.getX(), me.getY());
                }
            }
            public void mouseReleased(MouseEvent Me){
                if(Me.isPopupTrigger()){
                    Pmenu.show(Me.getComponent(), Me.getX(), Me.getY());
                }
            }
        });
    }
    private static void RenderPopupMenu(final JTextArea  pmidDate,final JTextArea  Abstract,final JTextArea  Result,final JTextArea  Figures,final DefaultListModel  higlightedWordslistModel,
            final JTextArea Title,final HighlightPainter Proteinhigliter,final HighlightPainter NonProteinhigliter,
            final HighlightPainter AddedProteinhigliter,final JTextField jt2,final JFrame frame,
            final ArrayList  <Multimap  <String,ArrayList<String>>> matchingLables, final ArrayList<HighlightPainter>  lable1higliter,
            final ArrayList <String> AllLables, final String workspace,final ExcelReader excel,final HighlightPainter suggestedProteinhiliter)
    {
        Pmenu = new JPopupMenu();
        addProtein = new JMenuItem("add to protein table  ((Ctrl + number) | (Ctrl + s))");
        Pmenu.add(addProtein);
        //  ExcelReader excel=new ExcelReader();
        
        JMenuItem [] lables = new JMenuItem [AllLables.size()];
        Color [] lable1Color= new Color[AllLables.size()];
        
        
        for ( int k=0;k<AllLables.size();k++)
        {
            //     JMenuItem lable1;
            
            final String [] Splited=StringUtils.split(AllLables.get(k), "/");
            lables[k] = new JMenuItem("add to "+Splited[0]+" table");
            if (matchingLables.size()!=AllLables.size() )
            {
            matchingLables.add(k, excel.GetAllProtein(workspace+"\\"+Splited[0]+".xlsx"));
            try {
                Field field = Color.class.getField(Splited[1]);
                lable1Color[k] = (Color)field.get(null);
            } catch (Exception e) {
                lable1Color[k] = null; // Not defined
            }
            //  lable1Color[k]=Color.getColor(Splited[1]);
            lable1higliter.add(new HighlightPainter(lable1Color[k]));
            }
            Pmenu.add( lables[k]);
            final int j=k;
            lables[k].addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    
                    String selection=Abstract.getSelectedText();
                    AddLabel( selection,  pmidDate,  Abstract,Result, Figures, higlightedWordslistModel, Title, Proteinhigliter, NonProteinhigliter,AddedProteinhigliter,jt2,
                            matchingLables,lable1higliter,AllLables,workspace,excel,Splited[0],j,false,suggestedProteinhiliter);          
                }
            });
        }
        
        addNewLable=new JMenuItem("add new lable");
        Pmenu.add(addNewLable);
        Pmenu.addSeparator();
        removeLocaly = new JMenuItem("remove label locally (Ctrl + L)");
        Pmenu.add(removeLocaly);
        nonProtin = new JMenuItem("NOT A Protein (Ctrl + R)");
        Pmenu.add(nonProtin);
        
        JMenuItem [] removingLables = new JMenuItem [AllLables.size()];
        for ( int k=0;k<AllLables.size();k++)
        {
            //     JMenuItem lable1;
            
            final String [] Splited=StringUtils.split(AllLables.get(k), "/");
            removingLables[k] = new JMenuItem("Not a "+Splited[0]);
            
            Pmenu.add( removingLables[k]);
            final int j=k;
            removingLables[k].addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    
                    String selection=Abstract.getSelectedText();
                    RemoveLabel( selection,  pmidDate,  Abstract,Result,Figures,  higlightedWordslistModel, Title, Proteinhigliter, NonProteinhigliter,AddedProteinhigliter,jt2,
                            matchingLables,lable1higliter,AllLables,workspace,excel,Splited[0],j,false, suggestedProteinhiliter);

                }
            });
        }
        removeAddedLable=new JMenuItem("remove new lable");
        Pmenu.add(removeAddedLable);
        removeAbstract=new JMenuItem("remove Abstract (Ctrl + D)");
        Pmenu.add(removeAbstract);
        Pmenu.addSeparator();
        undoLastMove=new JMenuItem("undo last move (Ctrl + Z)");
        Pmenu.add(undoLastMove);
        
    }
    private static void RemoveLabel(String selection,JTextArea  pmidDate,JTextArea  Abstract,JTextArea  Result,JTextArea  Figures,DefaultListModel  higlightedWordslistModel,JTextArea Title,
            HighlightPainter Proteinhigliter,HighlightPainter NonProteinhigliter,HighlightPainter AddedProteinhigliter,JTextField jt2,
            final ArrayList  <Multimap  <String,ArrayList<String>>> matchingLables, final ArrayList<HighlightPainter>  lable1higliter,
            ArrayList <String> AllLables,String workspace,ExcelReader excel,String LabelName,int matchingLablesIndex,boolean undo,HighlightPainter suggestedProteinhiliter)
            
    {
        if(selection==null){
            return;
        }
        RemoveProteinFromDatabase(workspace+"\\"+LabelName+".xlsx",selection);
        ArrayList<String> wordArrayList = new ArrayList<String>();
        String[] splited = selection.split("\\s+");
        for (int j=1;j<splited.length;j++){
            wordArrayList.add(splited[j].toUpperCase());
        }
        if (LabelName.equals(ProteinFileName))
        {
            matchingWords.remove(splited[0].toUpperCase(),wordArrayList);
            wordArrayList.add("ADDED");
            matchingWords.remove(splited[0].toUpperCase(),wordArrayList);
           // excel.RemoveProteinFromResults(selection);
        }
        else{
            matchingLables.get(matchingLablesIndex).remove(splited[0].toUpperCase(),wordArrayList);
            wordArrayList.add("ADDED");
            matchingLables.get(matchingLablesIndex).remove(splited[0].toUpperCase(),wordArrayList);
            
        }
        if (!undo)
        {
         ACTIONS currentAction=new ACTIONS(4,"removing from the table","",LabelName,matchingLablesIndex);
                
                currentAction.releatedWord=selection;
                lastAcion.add(currentAction);
        }
                RenderFrame(pmidDate,Abstract,Result,Figures,higlightedWordslistModel,Title,Proteinhigliter,AddedProteinhigliter,jt2,matchingLables,lable1higliter,AllLables,workspace,excel,suggestedProteinhiliter);
        NonProteinhigliter.highlightNonProtein(Abstract, selection,NonProteinhigliter);
        excel.RemoveProteinFromResults(selection);
        
    }
    private static void AddLabel(String selection,JTextArea  pmidDate,JTextArea  Abstract,JTextArea  Result,JTextArea  Figures,DefaultListModel  higlightedWordslistModel,JTextArea Title,
            HighlightPainter Proteinhigliter,HighlightPainter NonProteinhigliter,HighlightPainter AddedProteinhigliter,JTextField jt2,
            final ArrayList  <Multimap  <String,ArrayList<String>>> matchingLables, final ArrayList<HighlightPainter>  lable1higliter,
            ArrayList <String> AllLables,String workspace,ExcelReader excel,String LabelName,int matchingLablesIndex, boolean undo,HighlightPainter suggestedProteinhiliter)
            
    {
        if(selection==null){
            return;
        }
        ArrayList<String> wordArrayList = new ArrayList<String>();
        AddToProteinDatabase(workspace+"\\"+LabelName+".xlsx",selection);
        String[] splited = selection.split("\\s+");
        for (int j=1;j<splited.length;j++){
            wordArrayList.add(splited[j].toUpperCase());
        }
        wordArrayList.add("ADDED");
        if (LabelName.equals(ProteinFileName))
        { 
            matchingWords.put(splited[0].toUpperCase(),wordArrayList);
       
        }
        else {
            
            matchingLables.get(matchingLablesIndex).put(splited[0].toUpperCase(),wordArrayList);
        }
        if (!undo)
        {
          ACTIONS currentAction=new ACTIONS(1,"Addition to a table","",LabelName,matchingLablesIndex);
                currentAction.releatedWord=selection;
                lastAcion.add(currentAction);
        }
             RenderFrame(pmidDate,Abstract,Result,Figures,higlightedWordslistModel,Title,Proteinhigliter,AddedProteinhigliter,jt2,matchingLables,lable1higliter,AllLables,workspace,excel,suggestedProteinhiliter);      
    }
   
    private void FillAnnotationsLocations(JTextArea pmidDate, JTextArea Abstract, DefaultListModel higlightedWordslistModel, JTextArea Title, HighlightPainter Proteinhigliter, HighlightPainter AddedProteinhigliter, JTextField jt2, ArrayList<Multimap<String, ArrayList<String>>> matchingLables, ArrayList<HighlightPainter> lable1higliter, ArrayList<String> AllLables, String workspace, ExcelReader excel) 
    {
    
      annotationsLocations=new HashMap<String, ArrayList <String>>();
      int i =0;
         while (i<abstracts.size()-1)
         {             
                 
         ArrayList <String> annotations=new ArrayList <String>();
      annotations= RenderFrame2(pmidDate,Abstract,higlightedWordslistModel,Title,Proteinhigliter,AddedProteinhigliter,jt2,matchingLables,lable1higliter,AllLables,workspace,excel,i);
             for (int j=0;j<annotations.size();j++)
             {
             if (annotationsLocations.containsKey(annotations.get(j)))
             {
             annotationsLocations.get(annotations.get(j)).add(""+(i+1));
             }
             else {
             annotationsLocations.put(annotations.get(j), new ArrayList(Arrays.asList((i+1))));
             }
             }
              i++;
      }
      
    }
    private static  ArrayList <String> RenderFrame2(JTextArea  pmidDate,JTextArea  Abstract,DefaultListModel  higlightedWordslistModel,JTextArea Title,HighlightPainter Proteinhigliter, HighlightPainter AddedProteinhigliter,JTextField jt2,
            ArrayList  <Multimap  <String,ArrayList<String>>> matchingLables, ArrayList<HighlightPainter>  lable1higliter, ArrayList <String> AllLables,String workspace,  ExcelReader excel, int abstractnumber)
    {
       ArrayList <String> alreadyHighlightWord=new ArrayList();
        boolean addToResult=false;
        Abstract.setText("\nTitle: "+abstracts.get(abstractnumber).get("Title")+"\n ABSTRACT: "+abstracts.get(abstractnumber).get("Abstract"));

       
        alreadyHighlightWord=  Proteinhigliter.highlight_version2(abstracts.get(abstractnumber).get("PMID"),abstracts.get(abstractnumber).get("DATE"),
                Abstract, matchingWords,Proteinhigliter,AddedProteinhigliter,"PROTEIN",specialCase,workspace,excel,addToResult,"Abstract");
        //AddToHighlightedWordsSection(alreadyHighlightWord,higlightedWordslistModel);
       // Title.setText("\nTitle: "+abstracts.get(abstractnumber).get("Title"));
        
        if (matchingLables!=null && lable1higliter!=null)
        {
            for (int i=0;i<lable1higliter.size();i++)
            {
                  ArrayList <String> alreadyHighlightWord2=new ArrayList();
                final String [] Splited=StringUtils.split(AllLables.get(i), "/");
               alreadyHighlightWord2= Proteinhigliter.highlight_version2(abstracts.get(abstractnumber).get("PMID"),abstracts.get(abstractnumber).get("DATE"),Abstract,
                        matchingLables.get(i),lable1higliter.get(i),lable1higliter.get(i),Splited[0],specialCase,workspace,excel,addToResult,"Abstract");
               alreadyHighlightWord.addAll(alreadyHighlightWord2);
            }
        }
                  
       // Abstract.getCaret().setVisible(true);
       // Abstract.getCaret().setSelectionVisible(true);
        if (alreadyHighlightWord==null)
        {
        alreadyHighlightWord=new ArrayList();
        }
        return alreadyHighlightWord;
    }

    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }
}

