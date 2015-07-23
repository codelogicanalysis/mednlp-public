/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package EECE.Theses.Highlighter;

import EECE.Theses.FilesReader.ExcelReader;
import com.google.common.collect.Multimap;
import de.linguatools.disco.DISCO;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import org.apache.commons.lang3.StringUtils;


/**
 *
 * @author msabra
 */

public class HighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
    
    // <editor-fold defaultstate="collapsed" desc="Methodes">
    
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(getClass());
    
    public HighlightPainter(Color color) {
        super(color);
    }
    /**
     * no code use it, i only left it because it was the original highlight function taken from the internet.
     */
    private void highlight(JTextComponent textComp, String pattern,HighlightPainter myHighlightPainter) {
        // First remove all old highlights
        removeHighlights(textComp);
        
        try {
            Highlighter hilite = textComp.getHighlighter();
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());
            int pos = 0;
            
            // Search for pattern
            while ((pos = text.indexOf(pattern, pos)) >= 0) {
                // Create highlighter using private painter and apply around pattern
                hilite.addHighlight(pos, pos + pattern.length(), myHighlightPainter);
                pos += pattern.length();
            }
        } catch (BadLocationException e) {
            System.out.println(e);
        }
    }
    public  ArrayList <String> highlight_version2_simulation(String PMID,String Date,JTextComponent textComp, Multimap <String,ArrayList<String>> pattern,
            HighlightPainter myHighlightPainter,HighlightPainter myaddedHighlightPainter,String Type,boolean SpecialCase,String workspace, 
            ExcelReader excel, boolean addToResult, String SectionName)
    {
        // First remove all old highlights
        //  ExcelReader excel=new ExcelReader();
        //  removeHighlights(textComp);
        ArrayList <String> alreadyHighlightWord=new ArrayList(); // to not pass over the same word again
        
        try {
            
            Highlighter hilite = textComp.getHighlighter();
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());
            
            /**
             * word i an arrary of all words in text splited when space is found
             */
            String [] word = StringUtils.split(text);//text.split("\\s+");
            
            for (int j=0; j<word.length;j++) {
                
                /**
                 * Made for debugging perpuses
                 */
                if (word[j].equals("surgical"))
                {
                    log.debug("find it");
                }
                
                String wordToComparewith="";
                
                ArrayList <String>  pluWord=new ArrayList();
                pluWord.add(word[j]);
                /**
                 * to see if the word is composed of two words separated with special characters
                 */
                if (SpecialCase)
                {
                    ArrayList <String> isSpecialCase=this.ContainSpecialCase(word[j]);
                    if (isSpecialCase.get(0).equals("1"))
                    {
                        wordToComparewith=word[j];
                        
                    }
                    else if (isSpecialCase.get(0).equals("2"))
                    {
                        wordToComparewith= isSpecialCase.get(1);
                        
                    }
                    else if (isSpecialCase.get(0).equals("3"))
                    {
                        wordToComparewith= isSpecialCase.get(1);
                        pluWord.add(isSpecialCase.get(2));
                        
                    }
                    
                    
                    pluWord.add(wordToComparewith);
                }
                String modifiedWordj=word[j];
                /**
                 * to see if the word end or begin with some special character to be ignore
                 */
                while (StringUtils.endsWithAny(modifiedWordj, new String [] {".",",",")","]"}))
                {
                    modifiedWordj=modifiedWordj.substring(0, modifiedWordj.length()-1);
                }
                while  (StringUtils.startsWithAny(modifiedWordj, new String [] {".",",","(","["}))
                {
                    modifiedWordj=modifiedWordj.substring(1, modifiedWordj.length());
                }
                
                if(!modifiedWordj.equals(word[j]))
                {
                    pluWord.add(modifiedWordj);
                }
                
                
                /**
                 * add the plural word case
                 * special case for protein named nos
                 */
                if (SpecialCase)
                {
                    if (!wordToComparewith.toLowerCase().equals("no"))
                    {
                        pluWord.add(wordToComparewith+"s");
                    }
                    if (wordToComparewith.endsWith("s"))
                    {
                        pluWord.add(wordToComparewith.substring(0, wordToComparewith.length()-1));
                    }
                }
                /**
                 * pass through all words in the pluword array
                 */
                outerloop:
                for (int L=0 ;L<pluWord.size();L++)
                {
                    boolean added =false;
                    if (pattern.containsKey(pluWord.get(L).toUpperCase()))
                    {
                        /*
                        * get colletion of protein that begin with that word and arrange them from longer to smaller words
                        */
                        Collection<ArrayList<String>> wordArrayLists = pattern.get(pluWord.get(L).toUpperCase());
                        
                        //Sorting wordarraylist from higher size to lower one.
                        List< ArrayList<String> > list = new ArrayList< ArrayList<String> >( wordArrayLists );
                        //      List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
                        Comparator<ArrayList<String>> comparator = new Comparator<ArrayList<String>>() {
                            public int compare(ArrayList<String> c1, ArrayList<String> c2) {
                                return c2.size() - c1.size(); // use your logic
                            }
                        };
                        
                        Collections.sort(list, comparator); // use the comparator as much as u want
                        //System.out.println(list);
                        
                        for (ArrayList<String> wordArrayList : list) {
                            String allprotein=word[j];
                            boolean match=true;
                            int addition=0;
                            int pos = 0;
                            /**
                             * if protein size is only one word
                             */
                            if (wordArrayList.size()==0)
                            {
                                // check next word
                                String toHiglightWord=word[j];
                                int localNotProtein=0;
                                if (!alreadyHighlightWord.contains(toHiglightWord))
                                {
                                    while ((pos = text.indexOf(toHiglightWord, pos)) >= 0) {
                                        // Create highlighter using private painter and apply around pattern
                                        if (toHiglightWord.toUpperCase().equals("MR"))
                                        {
                                            boolean containMRI=false;
                                            for (int p=0;p<word.length;p++)
                                            {
                                                if (word[p].toUpperCase().equals("MRI") || word[p].toUpperCase().equals("IMAGING"))
                                                {
                                                    containMRI=true;
                                                }
                                            }
                                            if (containMRI)
                                            {
                                                break outerloop;
                                            }
                                        }
                                        localNotProtein=excel.findRow(workspace+"\\LOCALNonProtein.xlsx", PMID, Date, toHiglightWord.toUpperCase());
                                        if (localNotProtein==-1)
                                        {
                                            // Create highlighter using private painter and apply around pattern
                                            
                                          //  hilite.addHighlight(pos, pos + toHiglightWord.length(), myHighlightPainter);
                                            
                                            //     hilite.addHighlight(pos+1, pos+1 + word[j].length(), myHighlightPainter);
                                            if (addToResult)
                                            {
                                            excel.AddProteinRecordToResults(PMID,Date,toHiglightWord.toUpperCase(),Type,pluWord.get(L),SectionName,0,0,0);
                                            }
                                        }
                                        pos += toHiglightWord.length()+1;
                                    }
                                    if (localNotProtein==-1)
                                    {
                                        alreadyHighlightWord.add(toHiglightWord);
                                    }
                                    break outerloop;
                                }
                            }
                            /**
                             * protein size is more than one word
                             */
                            else {
                                   ArrayList <String>  pluWord2=new ArrayList();
                                for (int k=0;k<wordArrayList.size();k++)
                                {
                                    if (j+k+1<word.length)
                                    {
                                        String wordToComparewith2="";
                                        ArrayList <String> isSpecialCase2=this.ContainSpecialCase(word[j+k+1]);
                                     
                                        pluWord2.add(word[j+k+1].toUpperCase());
                                        /**
                                         * for differentiation between added word and already existing ones
                                         */
                                        if (!(k+1==wordArrayList.size()&&wordArrayList.get(k).equals("ADDED")))
                                        {
                                            /**
                                             * repeat the above checks
                                             */
                                            if (SpecialCase)
                                            {
                                                if (isSpecialCase2.get(0).equals("1"))
                                                {
                                                    wordToComparewith2=word[j+k+1];
                                                    
                                                }
                                                else if (isSpecialCase2.get(0).equals("2"))
                                                {
                                                    wordToComparewith2= isSpecialCase2.get(1);;
                                                    
                                                }
                                                else if (isSpecialCase2.get(0).equals("3"))
                                                {
                                                    wordToComparewith2= isSpecialCase2.get(1);
                                                    pluWord2.add(isSpecialCase2.get(2));
                                                    
                                                }
                                                
                                                pluWord2.add(wordToComparewith2.toUpperCase());
                                            }
                                            
                                            /**
                                             * as above
                                             */
                                            String modifiedWordj2=word[j+k+1];
                                            while (StringUtils.endsWithAny(modifiedWordj2, new String [] {".",",",")","]"}))
                                            {
                                                modifiedWordj2=modifiedWordj2.substring(0, modifiedWordj2.length()-1);
                                            }
                                            while  (StringUtils.startsWithAny(modifiedWordj2, new String [] {".",",","(","["}))
                                            {
                                                modifiedWordj2=modifiedWordj2.substring(1, modifiedWordj2.length());
                                            }
                                            
                                            if(!modifiedWordj2.equals(word[j+k+1]))
                                            {
                                                pluWord2.add(modifiedWordj2.toUpperCase());
                                            }
                                            
                                            
                                            
                                            if (SpecialCase)
                                            {
                                                pluWord2.add((wordToComparewith2+"s").toUpperCase());
                                                if (word[j+k+1].endsWith("s"))
                                                {
                                                    pluWord2.add(word[j+k+1].substring(0, word[j+k+1].length()-1).toUpperCase());
                                                }
                                            }
                                            if (!pluWord2.contains(wordArrayList.get(k)))
                                            {
                                                match=false;
                                            }
                                            else {
                                                allprotein=allprotein+" "+word[j+k+1];
                                                addition=addition+word[j+k+1].length();
                                            }
                                        }
                                        else
                                        {
                                            added=true;
                                        }
                                    }
                                    else {
                                        match=false;
                                    }
                                    
                                }
                                if (match)
                                {    if (!alreadyHighlightWord.contains(allprotein))
                                {
                                    HighlightPainter usedhiglighter= myHighlightPainter  ;
                                    if (added)
                                    {
                                        usedhiglighter=myaddedHighlightPainter;
                                        j=j+wordArrayList.size()-1;
                                    }
                                    else {
                                        j=j+wordArrayList.size();
                                    }
                                    //  hilite.addHighlight(pos+1, pos+(1+ word[j].length()+addition), myHighlightPainter);
                                    int localNotProtein=0;
                                    while ((pos = text.indexOf(allprotein, pos)) >= 0) {
                                        
                                        localNotProtein=excel.findRow(workspace+"\\LOCALNonProtein.xlsx", PMID, Date, allprotein.toUpperCase());
                                        if (localNotProtein==-1)
                                        {
                                            // Create highlighter using private painter and apply around pattern
                                          //  hilite.addHighlight(pos, pos + allprotein.length(), usedhiglighter);
                                            
                                            //     hilite.addHighlight(pos+1, pos+1 + word[j].length(), myHighlightPainter);
                                              if (addToResult)
                                            {
                                            excel.AddProteinRecordToResults(PMID,Date,allprotein.toUpperCase(),Type,pluWord2.toString(),SectionName,0,0,0);
                                            }
                                        }
                                        // Create highlighter using private painter and apply around pattern
                                        pos += word[j].length()+1+addition;
                                    }
                                    
                                    if (localNotProtein==-1)
                                    {
                                        alreadyHighlightWord.add(allprotein);
                                    }
                                    break outerloop;
                                }
                                }
                                else {
                                    pos += word[j].length()+1;
                                }
                            }
                        }
                        
                    }
                }
                
                
            }
            
        } catch (BadLocationException e) {
            System.out.println(e);
        }
        return alreadyHighlightWord;
    }
    
     
            public  ArrayList <String> highlight_version2_suggestion(String PMID,String Date,JTextComponent textComp, Multimap <String,ArrayList<String>> pattern,
            HighlightPainter myHighlightPainter,HighlightPainter myaddedHighlightPainter,String Type,boolean SpecialCase,String workspace, 
            ExcelReader excel, boolean addToResult)
    {
      
            
        // First remove all old highlights
        //  ExcelReader excel=new ExcelReader();
        //  removeHighlights(textComp);
        ArrayList <String> alreadyHighlightWord=new ArrayList(); // to not pass over the same word again
        
        try {
            Highlighter hilite = textComp.getHighlighter();
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());
            
            /**
             * word i an arrary of all words in text splited when space is found
             */
            String [] word = StringUtils.split(text);//text.split("\\s+");
            
            for (int j=0; j<word.length;j++) {
                
                /**
                 * Made for debugging perpuses
                 */
                if (word[j].equals("surgical"))
                {
                    log.debug("find it");
                }
                
                String wordToComparewith="";
                
                ArrayList <String>  pluWord=new ArrayList();
                pluWord.add(word[j]);
                /**
                 * to see if the word is composed of two words separated with special characters
                 */
                if (SpecialCase)
                {
                    ArrayList <String> isSpecialCase=this.ContainSpecialCase(word[j]);
                    if (isSpecialCase.get(0).equals("1"))
                    {
                        wordToComparewith=word[j];
                        
                    }
                    else if (isSpecialCase.get(0).equals("2"))
                    {
                        wordToComparewith= isSpecialCase.get(1);
                        
                    }
                    else if (isSpecialCase.get(0).equals("3"))
                    {
                        wordToComparewith= isSpecialCase.get(1);
                        pluWord.add(isSpecialCase.get(2));
                        
                    }
                    
                    
                    pluWord.add(wordToComparewith);
                }
                String modifiedWordj=word[j];
                /**
                 * to see if the word end or begin with some special character to be ignore
                 */
                while (StringUtils.endsWithAny(modifiedWordj, new String [] {".",",",")","]"}))
                {
                    modifiedWordj=modifiedWordj.substring(0, modifiedWordj.length()-1);
                }
                while  (StringUtils.startsWithAny(modifiedWordj, new String [] {".",",","(","["}))
                {
                    modifiedWordj=modifiedWordj.substring(1, modifiedWordj.length());
                }
                
                if(!modifiedWordj.equals(word[j]))
                {
                    pluWord.add(modifiedWordj);
                }
                
                
                /**
                 * add the plural word case
                 * special case for protein named nos
                 */
                if (SpecialCase)
                {
                    if (!wordToComparewith.toLowerCase().equals("no"))
                    {
                        pluWord.add(wordToComparewith+"s");
                    }
                    if (wordToComparewith.endsWith("s"))
                    {
                        pluWord.add(wordToComparewith.substring(0, wordToComparewith.length()-1));
                    }
                }
                /**
                 * pass through all words in the pluword array
                 */
                outerloop:
                for (int L=0 ;L<pluWord.size();L++)
                {
                    boolean added =false;
                    if (pattern.containsKey(pluWord.get(L).toUpperCase()))
                    {
                        /*
                        * get colletion of protein that begin with that word and arrange them from longer to smaller words
                        */
                        Collection<ArrayList<String>> wordArrayLists = pattern.get(pluWord.get(L).toUpperCase());
                        
                        //Sorting wordarraylist from higher size to lower one.
                        List< ArrayList<String> > list = new ArrayList< ArrayList<String> >( wordArrayLists );
                        //      List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
                        Comparator<ArrayList<String>> comparator = new Comparator<ArrayList<String>>() {
                            public int compare(ArrayList<String> c1, ArrayList<String> c2) {
                                return c2.size() - c1.size(); // use your logic
                            }
                        };
                        
                        Collections.sort(list, comparator); // use the comparator as much as u want
                        //System.out.println(list);
                         float score=0;
                       //  float score2=(float) 0.0;
                   //     for(int l=0;l<wordArrayLists.size();l++)
                      //  {
                      //       score= score+Float.parseFloat(list.get(l).get(0));
                       //     score2=score2+Float.parseFloat(list.get(l).get(1));
                      //  }
//                        int freq=0;
//                        try{
//                                freq=disco_wiki.frequency(word[j]);
//                        }
//                        catch(Exception e)
//                        {
//                        }
//                        if (freq < 10000)
//                        {   
                     //   if (score>=10000)
                   //     {
                        //     System.out.println(pluWord.get(L));
                      //  System.out.println("score of "+pluWord.get(L)+" = "+score);
                     //   System.out.println("second order score of "+pluWord.get(L)+" = "+score2);
                            int pos = 0;
                            /**
                             * if protein size is only one word
                             */
                            
                                // check next word
                                String toHiglightWord=word[j];
                                int localNotProtein=0;
                                if (!alreadyHighlightWord.contains(toHiglightWord))
                                {
                                    while ((pos = text.indexOf(toHiglightWord, pos)) >= 0) {
                                        // Create highlighter using private painter and apply around pattern
                                        if (toHiglightWord.toUpperCase().equals("MR"))
                                        {
                                            boolean containMRI=false;
                                            for (int p=0;p<word.length;p++)
                                            {
                                                if (word[p].toUpperCase().equals("MRI") || word[p].toUpperCase().equals("IMAGING"))
                                                {
                                                    containMRI=true;
                                                }
                                            }
                                            if (containMRI)
                                            {
                                                break outerloop;
                                            }
                                        }
                                        localNotProtein=excel.findRow(workspace+"\\LOCALNonProtein.xlsx", PMID, Date, toHiglightWord.toUpperCase());
                                        if (localNotProtein==-1)
                                        {
                                            // Create highlighter using private painter and apply around pattern
                                            
                                            hilite.addHighlight(pos, pos + toHiglightWord.length(), myHighlightPainter);
                                            
                                            //     hilite.addHighlight(pos+1, pos+1 + word[j].length(), myHighlightPainter);
                                            if (addToResult)
                                            {
                                            excel.AddProteinRecordToResults_1word(PMID,Date,StringUtils.removeEnd(StringUtils.removeEnd(toHiglightWord.toUpperCase(),","),"."),Type,pluWord.get(L));
                                       
                                            }
                                        }
                                        pos += toHiglightWord.length()+1;
                                    }
                                    if (localNotProtein==-1)
                                    {
                                       
                                        alreadyHighlightWord.add(toHiglightWord);
                                        }
                                        
                                    break outerloop;
                                }
                            
                            
                        
                   // }
                    //}
                    }
                }
                
                
            }
            
        } catch (BadLocationException e) {
            System.out.println(e);
        } 
        return alreadyHighlightWord;
    }
            
             public  ArrayList <String> highlight_version2_3words(String PMID,String Date,JTextComponent textComp, Multimap <String,ArrayList<String>> pattern,
            HighlightPainter myHighlightPainter,HighlightPainter myaddedHighlightPainter,String Type,boolean SpecialCase,String workspace, 
            ExcelReader excel, boolean addToResult)
    {
        // First remove all old highlights
        //  ExcelReader excel=new ExcelReader();
        //  removeHighlights(textComp);
        ArrayList <String> alreadyHighlightWord=new ArrayList(); // to not pass over the same word again
        
        try {
            
            Highlighter hilite = textComp.getHighlighter();
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());
            
            /**
             * word i an arrary of all words in text splited when space is found
             */
            String [] word = StringUtils.split(text);//text.split("\\s+");
            
            for (int j=0; j<word.length;j++) {
                
                /**
                 * Made for debugging perpuses
                 */
                if (word[j].equals("surgical"))
                {
                    log.debug("find it");
                }
                
                String wordToComparewith="";
                
                ArrayList <String>  pluWord=new ArrayList();
                pluWord.add(word[j]);
                /**
                 * to see if the word is composed of two words separated with special characters
                 */
                if (SpecialCase)
                {
                    ArrayList <String> isSpecialCase=this.ContainSpecialCase(word[j]);
                    if (isSpecialCase.get(0).equals("1"))
                    {
                        wordToComparewith=word[j];
                        
                    }
                    else if (isSpecialCase.get(0).equals("2"))
                    {
                        wordToComparewith= isSpecialCase.get(1);
                        
                    }
                    else if (isSpecialCase.get(0).equals("3"))
                    {
                        wordToComparewith= isSpecialCase.get(1);
                        pluWord.add(isSpecialCase.get(2));
                        
                    }
                    
                    
                    pluWord.add(wordToComparewith);
                }
                String modifiedWordj=word[j];
                /**
                 * to see if the word end or begin with some special character to be ignore
                 */
                while (StringUtils.endsWithAny(modifiedWordj, new String [] {".",",",")","]"}))
                {
                    modifiedWordj=modifiedWordj.substring(0, modifiedWordj.length()-1);
                }
                while  (StringUtils.startsWithAny(modifiedWordj, new String [] {".",",","(","["}))
                {
                    modifiedWordj=modifiedWordj.substring(1, modifiedWordj.length());
                }
                
                if(!modifiedWordj.equals(word[j]))
                {
                    pluWord.add(modifiedWordj);
                }
                
                
                /**
                 * add the plural word case
                 * special case for protein named nos
                 */
                if (SpecialCase)
                {
                    if (!wordToComparewith.toLowerCase().equals("no"))
                    {
                        pluWord.add(wordToComparewith+"s");
                    }
                    if (wordToComparewith.endsWith("s"))
                    {
                        pluWord.add(wordToComparewith.substring(0, wordToComparewith.length()-1));
                    }
                }
                /**
                 * pass through all words in the pluword array
                 */
                outerloop:
                for (int L=0 ;L<pluWord.size();L++)
                {
                    boolean added =false;
                    if (pattern.containsKey(pluWord.get(L).toUpperCase()))
                    {
                        /*
                        * get colletion of protein that begin with that word and arrange them from longer to smaller words
                        */
                        Collection<ArrayList<String>> wordArrayLists = pattern.get(pluWord.get(L).toUpperCase());
                        
                        //Sorting wordarraylist from higher size to lower one.
                        List< ArrayList<String> > list = new ArrayList< ArrayList<String> >( wordArrayLists );
                        //      List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
                        Comparator<ArrayList<String>> comparator = new Comparator<ArrayList<String>>() {
                            public int compare(ArrayList<String> c1, ArrayList<String> c2) {
                                return c2.size() - c1.size(); // use your logic
                            }
                        };
                        
                        Collections.sort(list, comparator); // use the comparator as much as u want
                        //System.out.println(list);
                        
                        for (ArrayList<String> wordArrayList : list) {
                            String allprotein=word[j];
                            boolean match=true;
                            int addition=0;
                            int pos = 0;
                            /**
                             * if protein size is only one word
                             */
                            if (wordArrayList.size()==0)
                            {
                                // check next word
                                String toHiglightWord=word[j];
                                int localNotProtein=0;
                                if (!alreadyHighlightWord.contains(toHiglightWord))
                                {
                                    while ((pos = text.indexOf(toHiglightWord, pos)) >= 0) {
                                        // Create highlighter using private painter and apply around pattern
                                        if (toHiglightWord.toUpperCase().equals("MR"))
                                        {
                                            boolean containMRI=false;
                                            for (int p=0;p<word.length;p++)
                                            {
                                                if (word[p].toUpperCase().equals("MRI") || word[p].toUpperCase().equals("IMAGING"))
                                                {
                                                    containMRI=true;
                                                }
                                            }
                                            if (containMRI)
                                            {
                                                break outerloop;
                                            }
                                        }
                                        localNotProtein=excel.findRow(workspace+"\\LOCALNonProtein.xlsx", PMID, Date, toHiglightWord.toUpperCase());
                                        if (localNotProtein==-1)
                                        {
                                            // Create highlighter using private painter and apply around pattern
                                            
                                            hilite.addHighlight(pos, pos + toHiglightWord.length(), myHighlightPainter);
                                            
                                            //     hilite.addHighlight(pos+1, pos+1 + word[j].length(), myHighlightPainter);
                                            if (addToResult)
                                            {
                                            excel.AddProteinRecordToResults_3words(PMID,Date,StringUtils.removeEnd(StringUtils.removeEnd(toHiglightWord.toUpperCase(),","),"."),Type,pluWord.get(L));
                                            }
                                        }
                                        pos += toHiglightWord.length()+1;
                                    }
                                    if (localNotProtein==-1)
                                    {
                                        alreadyHighlightWord.add(toHiglightWord);
                                    }
                                    break outerloop;
                                }
                            }
                            /**
                             * protein size is more than one word
                             */
                            else {
                                  String  originalProteinName=pluWord.get(L);
                                for (int k=0;k<wordArrayList.size();k++)
                                {
                                    if (j+k+1<word.length)
                                    {
                                          ArrayList <String>  pluWord2=new ArrayList();
                                        String wordToComparewith2="";
                                        ArrayList <String> isSpecialCase2=this.ContainSpecialCase(word[j+k+1]);
                                     
                                        pluWord2.add(word[j+k+1].toUpperCase());
                                        /**
                                         * for differentiation between added word and already existing ones
                                         */
                                        if (!(k+1==wordArrayList.size()&&wordArrayList.get(k).equals("ADDED")))
                                        {
                                            /**
                                             * repeat the above checks
                                             */
                                            if (SpecialCase)
                                            {
                                                if (isSpecialCase2.get(0).equals("1"))
                                                {
                                                    wordToComparewith2=word[j+k+1];
                                                    
                                                }
                                                else if (isSpecialCase2.get(0).equals("2"))
                                                {
                                                    wordToComparewith2= isSpecialCase2.get(1);;
                                                    
                                                }
                                                else if (isSpecialCase2.get(0).equals("3"))
                                                {
                                                    wordToComparewith2= isSpecialCase2.get(1);
                                                    pluWord2.add(isSpecialCase2.get(2));
                                                    
                                                }
                                                
                                                pluWord2.add(wordToComparewith2.toUpperCase());
                                            }
                                            
                                            /**
                                             * as above
                                             */
                                            String modifiedWordj2=word[j+k+1];
                                            while (StringUtils.endsWithAny(modifiedWordj2, new String [] {".",",",")","]"}))
                                            {
                                                modifiedWordj2=modifiedWordj2.substring(0, modifiedWordj2.length()-1);
                                            }
                                            while  (StringUtils.startsWithAny(modifiedWordj2, new String [] {".",",","(","["}))
                                            {
                                                modifiedWordj2=modifiedWordj2.substring(1, modifiedWordj2.length());
                                            }
                                            
                                            if(!modifiedWordj2.equals(word[j+k+1]))
                                            {
                                                pluWord2.add(modifiedWordj2.toUpperCase());
                                            }
                                            
                                            
                                            
                                            if (SpecialCase)
                                            {
                                                pluWord2.add((wordToComparewith2+"s").toUpperCase());
                                                if (word[j+k+1].endsWith("s"))
                                                {
                                                    pluWord2.add(word[j+k+1].substring(0, word[j+k+1].length()-1).toUpperCase());
                                                }
                                            }
                                            if (!pluWord2.contains(wordArrayList.get(k)))
                                            {
                                                match=false;
                                            }
                                            else {
                                                allprotein=allprotein+" "+word[j+k+1];
                                                addition=addition+word[j+k+1].length();
                                                originalProteinName=originalProteinName+" "+wordArrayList.get(k);
                                            }
                                        }
                                        else
                                        {
                                            added=true;
                                        }
                                    }
                                    else {
                                        match=false;
                                    }
                                    
                                }
                                if (match)
                                {    if (!alreadyHighlightWord.contains(allprotein))
                                {
                                    HighlightPainter usedhiglighter= myHighlightPainter  ;
                                    if (added)
                                    {
                                        usedhiglighter=myaddedHighlightPainter;
                                        j=j+wordArrayList.size()-1;
                                    }
                                    else {
                                        j=j+wordArrayList.size();
                                    }
                                    //  hilite.addHighlight(pos+1, pos+(1+ word[j].length()+addition), myHighlightPainter);
                                    int localNotProtein=0;
                                    while ((pos = text.indexOf(allprotein, pos)) >= 0) {
                                        
                                        localNotProtein=excel.findRow(workspace+"\\LOCALNonProtein.xlsx", PMID, Date, allprotein.toUpperCase());
                                        if (localNotProtein==-1)
                                        {
                                            // Create highlighter using private painter and apply around pattern
                                            hilite.addHighlight(pos, pos + allprotein.length(), usedhiglighter);
                                            
                                            //     hilite.addHighlight(pos+1, pos+1 + word[j].length(), myHighlightPainter);
                                              if (addToResult)
                                            {
                                            excel.AddProteinRecordToResults_3words(PMID,Date,StringUtils.removeEnd(StringUtils.removeEnd(allprotein.toUpperCase(),","),"."),Type,originalProteinName);
                                            }
                                        }
                                        // Create highlighter using private painter and apply around pattern
                                        pos += word[j].length()+1+addition;
                                    }
                                    
                                    if (localNotProtein==-1)
                                    {
                                        alreadyHighlightWord.add(allprotein);
                                    }
                                    break outerloop;
                                }
                                }
                                else {
                                    pos += word[j].length()+1;
                                }
                            }
                        }
                        
                    }
                }
                
                
            }
            
        } catch (BadLocationException e) {
            System.out.println(e);
        }
        return alreadyHighlightWord;
    }
    
            
             public  ArrayList <String> highlight_version2_2words(String PMID,String Date,JTextComponent textComp, Multimap <String,ArrayList<String>> pattern,
            HighlightPainter myHighlightPainter,HighlightPainter myaddedHighlightPainter,String Type,boolean SpecialCase,String workspace, 
            ExcelReader excel, boolean addToResult)
    {
        // First remove all old highlights
        //  ExcelReader excel=new ExcelReader();
        //  removeHighlights(textComp);
        ArrayList <String> alreadyHighlightWord=new ArrayList(); // to not pass over the same word again
        
        try {
            
            Highlighter hilite = textComp.getHighlighter();
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());
            
            /**
             * word i an arrary of all words in text splited when space is found
             */
            String [] word = StringUtils.split(text);//text.split("\\s+");
            
            for (int j=0; j<word.length;j++) {
                
                /**
                 * Made for debugging perpuses
                 */
                if (word[j].equals("surgical"))
                {
                    log.debug("find it");
                }
                
                String wordToComparewith="";
                
                ArrayList <String>  pluWord=new ArrayList();
                pluWord.add(word[j]);
                /**
                 * to see if the word is composed of two words separated with special characters
                 */
                if (SpecialCase)
                {
                    ArrayList <String> isSpecialCase=this.ContainSpecialCase(word[j]);
                    if (isSpecialCase.get(0).equals("1"))
                    {
                        wordToComparewith=word[j];
                        
                    }
                    else if (isSpecialCase.get(0).equals("2"))
                    {
                        wordToComparewith= isSpecialCase.get(1);
                        
                    }
                    else if (isSpecialCase.get(0).equals("3"))
                    {
                        wordToComparewith= isSpecialCase.get(1);
                        pluWord.add(isSpecialCase.get(2));
                        
                    }
                    
                    
                    pluWord.add(wordToComparewith);
                }
                String modifiedWordj=word[j];
                /**
                 * to see if the word end or begin with some special character to be ignore
                 */
                while (StringUtils.endsWithAny(modifiedWordj, new String [] {".",",",")","]"}))
                {
                    modifiedWordj=modifiedWordj.substring(0, modifiedWordj.length()-1);
                }
                while  (StringUtils.startsWithAny(modifiedWordj, new String [] {".",",","(","["}))
                {
                    modifiedWordj=modifiedWordj.substring(1, modifiedWordj.length());
                }
                
                if(!modifiedWordj.equals(word[j]))
                {
                    pluWord.add(modifiedWordj);
                }
                
                
                /**
                 * add the plural word case
                 * special case for protein named nos
                 */
                if (SpecialCase)
                {
                    if (!wordToComparewith.toLowerCase().equals("no"))
                    {
                        pluWord.add(wordToComparewith+"s");
                    }
                    if (wordToComparewith.endsWith("s"))
                    {
                        pluWord.add(wordToComparewith.substring(0, wordToComparewith.length()-1));
                    }
                }
                /**
                 * pass through all words in the pluword array
                 */
                outerloop:
                for (int L=0 ;L<pluWord.size();L++)
                {
                    boolean added =false;
                    if (pattern.containsKey(pluWord.get(L).toUpperCase()))
                    {
                        /*
                        * get colletion of protein that begin with that word and arrange them from longer to smaller words
                        */
                        Collection<ArrayList<String>> wordArrayLists = pattern.get(pluWord.get(L).toUpperCase());
                        
                        //Sorting wordarraylist from higher size to lower one.
                        List< ArrayList<String> > list = new ArrayList< ArrayList<String> >( wordArrayLists );
                        //      List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
                        Comparator<ArrayList<String>> comparator = new Comparator<ArrayList<String>>() {
                            public int compare(ArrayList<String> c1, ArrayList<String> c2) {
                                return c2.size() - c1.size(); // use your logic
                            }
                        };
                        
                        Collections.sort(list, comparator); // use the comparator as much as u want
                        //System.out.println(list);
                        
                        for (ArrayList<String> wordArrayList : list) {
                            String allprotein=word[j];
                            boolean match=true;
                            int addition=0;
                            int pos = 0;
                            /**
                             * if protein size is only one word
                             */
                            if (wordArrayList.size()==0)
                            {
                                // check next word
                                String toHiglightWord=word[j];
                                int localNotProtein=0;
                                if (!alreadyHighlightWord.contains(toHiglightWord))
                                {
                                    while ((pos = text.indexOf(toHiglightWord, pos)) >= 0) {
                                        // Create highlighter using private painter and apply around pattern
                                        if (toHiglightWord.toUpperCase().equals("MR"))
                                        {
                                            boolean containMRI=false;
                                            for (int p=0;p<word.length;p++)
                                            {
                                                if (word[p].toUpperCase().equals("MRI") || word[p].toUpperCase().equals("IMAGING"))
                                                {
                                                    containMRI=true;
                                                }
                                            }
                                            if (containMRI)
                                            {
                                                break outerloop;
                                            }
                                        }
                                        localNotProtein=excel.findRow(workspace+"\\LOCALNonProtein.xlsx", PMID, Date, toHiglightWord.toUpperCase());
                                        if (localNotProtein==-1)
                                        {
                                            // Create highlighter using private painter and apply around pattern
                                            
                                            hilite.addHighlight(pos, pos + toHiglightWord.length(), myHighlightPainter);
                                            
                                            //     hilite.addHighlight(pos+1, pos+1 + word[j].length(), myHighlightPainter);
                                            if (addToResult)
                                            {
                                            excel.AddProteinRecordToResults_2words(PMID,Date,StringUtils.removeEnd(StringUtils.removeEnd(toHiglightWord.toUpperCase(),","),"."),Type,pluWord.get(L));
                                            }
                                        }
                                        pos += toHiglightWord.length()+1;
                                    }
                                    if (localNotProtein==-1)
                                    {
                                        alreadyHighlightWord.add(toHiglightWord);
                                    }
                                    break outerloop;
                                }
                            }
                            /**
                             * protein size is more than one word
                             */
                            else {
                                  String  originalProteinName=pluWord.get(L);
                                for (int k=0;k<wordArrayList.size();k++)
                                {
                                    if (j+k+1<word.length)
                                    {
                                          ArrayList <String>  pluWord2=new ArrayList();
                                        String wordToComparewith2="";
                                        ArrayList <String> isSpecialCase2=this.ContainSpecialCase(word[j+k+1]);
                                     
                                        pluWord2.add(word[j+k+1].toUpperCase());
                                        /**
                                         * for differentiation between added word and already existing ones
                                         */
                                        if (!(k+1==wordArrayList.size()&&wordArrayList.get(k).equals("ADDED")))
                                        {
                                            /**
                                             * repeat the above checks
                                             */
                                            if (SpecialCase)
                                            {
                                                if (isSpecialCase2.get(0).equals("1"))
                                                {
                                                    wordToComparewith2=word[j+k+1];
                                                    
                                                }
                                                else if (isSpecialCase2.get(0).equals("2"))
                                                {
                                                    wordToComparewith2= isSpecialCase2.get(1);;
                                                    
                                                }
                                                else if (isSpecialCase2.get(0).equals("3"))
                                                {
                                                    wordToComparewith2= isSpecialCase2.get(1);
                                                    pluWord2.add(isSpecialCase2.get(2));
                                                    
                                                }
                                                
                                                pluWord2.add(wordToComparewith2.toUpperCase());
                                            }
                                            
                                            /**
                                             * as above
                                             */
                                            String modifiedWordj2=word[j+k+1];
                                            while (StringUtils.endsWithAny(modifiedWordj2, new String [] {".",",",")","]"}))
                                            {
                                                modifiedWordj2=modifiedWordj2.substring(0, modifiedWordj2.length()-1);
                                            }
                                            while  (StringUtils.startsWithAny(modifiedWordj2, new String [] {".",",","(","["}))
                                            {
                                                modifiedWordj2=modifiedWordj2.substring(1, modifiedWordj2.length());
                                            }
                                            
                                            if(!modifiedWordj2.equals(word[j+k+1]))
                                            {
                                                pluWord2.add(modifiedWordj2.toUpperCase());
                                            }
                                            
                                            
                                            
                                            if (SpecialCase)
                                            {
                                                pluWord2.add((wordToComparewith2+"s").toUpperCase());
                                                if (word[j+k+1].endsWith("s"))
                                                {
                                                    pluWord2.add(word[j+k+1].substring(0, word[j+k+1].length()-1).toUpperCase());
                                                }
                                            }
                                            if (!pluWord2.contains(wordArrayList.get(k)))
                                            {
                                                match=false;
                                            }
                                            else {
                                                allprotein=allprotein+" "+word[j+k+1];
                                                addition=addition+word[j+k+1].length();
                                                originalProteinName=originalProteinName+" "+wordArrayList.get(k);
                                            }
                                        }
                                        else
                                        {
                                            added=true;
                                        }
                                    }
                                    else {
                                        match=false;
                                    }
                                    
                                }
                                if (match)
                                {    if (!alreadyHighlightWord.contains(allprotein))
                                {
                                    HighlightPainter usedhiglighter= myHighlightPainter  ;
                                    if (added)
                                    {
                                        usedhiglighter=myaddedHighlightPainter;
                                        j=j+wordArrayList.size()-1;
                                    }
                                    else {
                                        j=j+wordArrayList.size();
                                    }
                                    //  hilite.addHighlight(pos+1, pos+(1+ word[j].length()+addition), myHighlightPainter);
                                    int localNotProtein=0;
                                    while ((pos = text.indexOf(allprotein, pos)) >= 0) {
                                        
                                        localNotProtein=excel.findRow(workspace+"\\LOCALNonProtein.xlsx", PMID, Date, allprotein.toUpperCase());
                                        if (localNotProtein==-1)
                                        {
                                            // Create highlighter using private painter and apply around pattern
                                            hilite.addHighlight(pos, pos + allprotein.length(), usedhiglighter);
                                            
                                            //     hilite.addHighlight(pos+1, pos+1 + word[j].length(), myHighlightPainter);
                                              if (addToResult)
                                            {
                                            excel.AddProteinRecordToResults_2words(PMID,Date,StringUtils.removeEnd(StringUtils.removeEnd(allprotein.toUpperCase(),","),"."),Type,originalProteinName);
                                            }
                                        }
                                        // Create highlighter using private painter and apply around pattern
                                        pos += word[j].length()+1+addition;
                                    }
                                    
                                    if (localNotProtein==-1)
                                    {
                                        alreadyHighlightWord.add(allprotein);
                                    }
                                    break outerloop;
                                }
                                }
                                else {
                                    pos += word[j].length()+1;
                                }
                            }
                        }
                        
                    }
                }
                
                
            }
            
        } catch (BadLocationException e) {
            System.out.println(e);
        }
        return alreadyHighlightWord;
    }
    
            
            
             public  ArrayList <String> highlight_version2_1word(String PMID,String Date,JTextComponent textComp, Multimap <String,ArrayList<String>> pattern,
            HighlightPainter myHighlightPainter,HighlightPainter myaddedHighlightPainter,String Type,boolean SpecialCase,String workspace, 
            ExcelReader excel, boolean addToResult)
    {
        // First remove all old highlights
        //  ExcelReader excel=new ExcelReader();
        //  removeHighlights(textComp);
        ArrayList <String> alreadyHighlightWord=new ArrayList(); // to not pass over the same word again
        
        try {
            
            Highlighter hilite = textComp.getHighlighter();
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());
            
            /**
             * word i an arrary of all words in text splited when space is found
             */
            String [] word = StringUtils.split(text);//text.split("\\s+");
            
            for (int j=0; j<word.length;j++) {
                
                /**
                 * Made for debugging perpuses
                 */
                if (word[j].equals("surgical"))
                {
                    log.debug("find it");
                }
                
                String wordToComparewith="";
                
                ArrayList <String>  pluWord=new ArrayList();
                pluWord.add(word[j]);
                /**
                 * to see if the word is composed of two words separated with special characters
                 */
                if (SpecialCase)
                {
                    ArrayList <String> isSpecialCase=this.ContainSpecialCase(word[j]);
                    if (isSpecialCase.get(0).equals("1"))
                    {
                        wordToComparewith=word[j];
                        
                    }
                    else if (isSpecialCase.get(0).equals("2"))
                    {
                        wordToComparewith= isSpecialCase.get(1);
                        
                    }
                    else if (isSpecialCase.get(0).equals("3"))
                    {
                        wordToComparewith= isSpecialCase.get(1);
                        pluWord.add(isSpecialCase.get(2));
                        
                    }
                    
                    
                    pluWord.add(wordToComparewith);
                }
                String modifiedWordj=word[j];
                /**
                 * to see if the word end or begin with some special character to be ignore
                 */
                while (StringUtils.endsWithAny(modifiedWordj, new String [] {".",",",")","]"}))
                {
                    modifiedWordj=modifiedWordj.substring(0, modifiedWordj.length()-1);
                }
                while  (StringUtils.startsWithAny(modifiedWordj, new String [] {".",",","(","["}))
                {
                    modifiedWordj=modifiedWordj.substring(1, modifiedWordj.length());
                }
                
                if(!modifiedWordj.equals(word[j]))
                {
                    pluWord.add(modifiedWordj);
                }
                
                
                /**
                 * add the plural word case
                 * special case for protein named nos
                 */
                if (SpecialCase)
                {
                    if (!wordToComparewith.toLowerCase().equals("no"))
                    {
                        pluWord.add(wordToComparewith+"s");
                    }
                    if (wordToComparewith.endsWith("s"))
                    {
                        pluWord.add(wordToComparewith.substring(0, wordToComparewith.length()-1));
                    }
                }
                /**
                 * pass through all words in the pluword array
                 */
                outerloop:
                for (int L=0 ;L<pluWord.size();L++)
                {
                    boolean added =false;
                    if (pattern.containsKey(pluWord.get(L).toUpperCase()))
                    {
                        /*
                        * get colletion of protein that begin with that word and arrange them from longer to smaller words
                        */
                        Collection<ArrayList<String>> wordArrayLists = pattern.get(pluWord.get(L).toUpperCase());
                        
                        //Sorting wordarraylist from higher size to lower one.
                        List< ArrayList<String> > list = new ArrayList< ArrayList<String> >( wordArrayLists );
                        //      List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
                        Comparator<ArrayList<String>> comparator = new Comparator<ArrayList<String>>() {
                            public int compare(ArrayList<String> c1, ArrayList<String> c2) {
                                return c2.size() - c1.size(); // use your logic
                            }
                        };
                        
                        Collections.sort(list, comparator); // use the comparator as much as u want
                        //System.out.println(list);
                        
                        for (ArrayList<String> wordArrayList : list) {
                            String allprotein=word[j];
                            boolean match=true;
                            int addition=0;
                            int pos = 0;
                            /**
                             * if protein size is only one word
                             */
                            if (wordArrayList.size()==0)
                            {
                                // check next word
                                String toHiglightWord=word[j];
                                int localNotProtein=0;
                                if (!alreadyHighlightWord.contains(toHiglightWord))
                                {
                                    while ((pos = text.indexOf(toHiglightWord, pos)) >= 0) {
                                        // Create highlighter using private painter and apply around pattern
                                        if (toHiglightWord.toUpperCase().equals("MR"))
                                        {
                                            boolean containMRI=false;
                                            for (int p=0;p<word.length;p++)
                                            {
                                                if (word[p].toUpperCase().equals("MRI") || word[p].toUpperCase().equals("IMAGING"))
                                                {
                                                    containMRI=true;
                                                }
                                            }
                                            if (containMRI)
                                            {
                                                break outerloop;
                                            }
                                        }
                                        localNotProtein=excel.findRow(workspace+"\\LOCALNonProtein.xlsx", PMID, Date, toHiglightWord.toUpperCase());
                                        if (localNotProtein==-1)
                                        {
                                            // Create highlighter using private painter and apply around pattern
                                            
                                            hilite.addHighlight(pos, pos + toHiglightWord.length(), myHighlightPainter);
                                            
                                            //     hilite.addHighlight(pos+1, pos+1 + word[j].length(), myHighlightPainter);
                                            if (addToResult)
                                            {
                                            excel.AddProteinRecordToResults_1word(PMID,Date,StringUtils.removeEnd(StringUtils.removeEnd(toHiglightWord.toUpperCase(),","),"."),Type,pluWord.get(L));
                                            }
                                        }
                                        pos += toHiglightWord.length()+1;
                                    }
                                    if (localNotProtein==-1)
                                    {
                                        alreadyHighlightWord.add(toHiglightWord);
                                    }
                                    break outerloop;
                                }
                            }
                            /**
                             * protein size is more than one word
                             */
                            else {
                                  String  originalProteinName=pluWord.get(L);
                                for (int k=0;k<wordArrayList.size();k++)
                                {
                                    if (j+k+1<word.length)
                                    {
                                          ArrayList <String>  pluWord2=new ArrayList();
                                        String wordToComparewith2="";
                                        ArrayList <String> isSpecialCase2=this.ContainSpecialCase(word[j+k+1]);
                                     
                                        pluWord2.add(word[j+k+1].toUpperCase());
                                        /**
                                         * for differentiation between added word and already existing ones
                                         */
                                        if (!(k+1==wordArrayList.size()&&wordArrayList.get(k).equals("ADDED")))
                                        {
                                            /**
                                             * repeat the above checks
                                             */
                                            if (SpecialCase)
                                            {
                                                if (isSpecialCase2.get(0).equals("1"))
                                                {
                                                    wordToComparewith2=word[j+k+1];
                                                    
                                                }
                                                else if (isSpecialCase2.get(0).equals("2"))
                                                {
                                                    wordToComparewith2= isSpecialCase2.get(1);;
                                                    
                                                }
                                                else if (isSpecialCase2.get(0).equals("3"))
                                                {
                                                    wordToComparewith2= isSpecialCase2.get(1);
                                                    pluWord2.add(isSpecialCase2.get(2));
                                                    
                                                }
                                                
                                                pluWord2.add(wordToComparewith2.toUpperCase());
                                            }
                                            
                                            /**
                                             * as above
                                             */
                                            String modifiedWordj2=word[j+k+1];
                                            while (StringUtils.endsWithAny(modifiedWordj2, new String [] {".",",",")","]"}))
                                            {
                                                modifiedWordj2=modifiedWordj2.substring(0, modifiedWordj2.length()-1);
                                            }
                                            while  (StringUtils.startsWithAny(modifiedWordj2, new String [] {".",",","(","["}))
                                            {
                                                modifiedWordj2=modifiedWordj2.substring(1, modifiedWordj2.length());
                                            }
                                            
                                            if(!modifiedWordj2.equals(word[j+k+1]))
                                            {
                                                pluWord2.add(modifiedWordj2.toUpperCase());
                                            }
                                            
                                            
                                            
                                            if (SpecialCase)
                                            {
                                                pluWord2.add((wordToComparewith2+"s").toUpperCase());
                                                if (word[j+k+1].endsWith("s"))
                                                {
                                                    pluWord2.add(word[j+k+1].substring(0, word[j+k+1].length()-1).toUpperCase());
                                                }
                                            }
                                            if (!pluWord2.contains(wordArrayList.get(k)))
                                            {
                                                match=false;
                                            }
                                            else {
                                                allprotein=allprotein+" "+word[j+k+1];
                                                addition=addition+word[j+k+1].length();
                                                originalProteinName=originalProteinName+" "+wordArrayList.get(k);
                                            }
                                        }
                                        else
                                        {
                                            added=true;
                                        }
                                    }
                                    else {
                                        match=false;
                                    }
                                    
                                }
                                if (match)
                                {    if (!alreadyHighlightWord.contains(allprotein))
                                {
                                    HighlightPainter usedhiglighter= myHighlightPainter  ;
                                    if (added)
                                    {
                                        usedhiglighter=myaddedHighlightPainter;
                                        j=j+wordArrayList.size()-1;
                                    }
                                    else {
                                        j=j+wordArrayList.size();
                                    }
                                    //  hilite.addHighlight(pos+1, pos+(1+ word[j].length()+addition), myHighlightPainter);
                                    int localNotProtein=0;
                                    while ((pos = text.indexOf(allprotein, pos)) >= 0) {
                                        
                                        localNotProtein=excel.findRow(workspace+"\\LOCALNonProtein.xlsx", PMID, Date, allprotein.toUpperCase());
                                        if (localNotProtein==-1)
                                        {
                                            // Create highlighter using private painter and apply around pattern
                                            hilite.addHighlight(pos, pos + allprotein.length(), usedhiglighter);
                                            
                                            //     hilite.addHighlight(pos+1, pos+1 + word[j].length(), myHighlightPainter);
                                              if (addToResult)
                                            {
                                            excel.AddProteinRecordToResults_1word(PMID,Date,StringUtils.removeEnd(StringUtils.removeEnd(allprotein.toUpperCase(),","),"."),Type,originalProteinName);
                                            }
                                        }
                                        // Create highlighter using private painter and apply around pattern
                                        pos += word[j].length()+1+addition;
                                    }
                                    
                                    if (localNotProtein==-1)
                                    {
                                        alreadyHighlightWord.add(allprotein);
                                    }
                                    break outerloop;
                                }
                                }
                                else {
                                    pos += word[j].length()+1;
                                }
                            }
                        }
                        
                    }
                }
                
                
            }
            
        } catch (BadLocationException e) {
            System.out.println(e);
        }
        return alreadyHighlightWord;
    }
    
            
            
    /**
     *
     * * it is the mainly use highlight function
     * @param PMID
     * @param Date
     * @param textComp the text are in the jframe
     * @param pattern the hash table of the protein labels
     * @param myHighlightPainter the highlight used
     * @param myaddedHighlightPainter  the highlight used for added protein
     * @param Type the type of highlight ( protein,antibiotic , ....)
     * @param SpecialCase to tell the function if we want to consider special cases or no
     * @param workspace to know the workspace
     * @param excel
     * @return
     */
    
    public  ArrayList <String> highlight_version2(String PMID,String Date,JTextComponent textComp, Multimap <String,ArrayList<String>> pattern,
            HighlightPainter myHighlightPainter,HighlightPainter myaddedHighlightPainter,String Type,boolean SpecialCase,String workspace, 
            ExcelReader excel, boolean addToResult,String SectionName)
    {
        int countWords=0;
        int countSentence=1;
        int countWordInSentence=1;
        // First remove all old highlights
        //  ExcelReader excel=new ExcelReader();
        //  removeHighlights(textComp);
      if ( excel.findNotAnAbstractRow(workspace+"\\exludedAbstracts.xlsx", PMID) ==-1)
      {
        ArrayList <String> alreadyHighlightWord=new ArrayList(); // to not pass over the same word again
        
        try {
            
            Highlighter hilite = textComp.getHighlighter();
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());
            
            /**
             * word i an arrary of all words in text splited when space is found
             */
            String [] word = StringUtils.split(text);//text.split("\\s+");
            
            for (int j=0; j<word.length;j++) {
                  boolean FirstTime=true;
               countWords++;
               countWordInSentence++;
              
                /**
                 * Made for debugging perpuses
                 */
                if (word[j].equals("surgical"))
                {
                    log.debug("find it");
                }
                
                String wordToComparewith="";
                
                ArrayList <String>  pluWord=new ArrayList();
                pluWord.add(word[j]);
                /**
                 * to see if the word is composed of two words separated with special characters
                 */
                if (SpecialCase)
                {
                    ArrayList <String> isSpecialCase=this.ContainSpecialCase(word[j]);
                    if (isSpecialCase.get(0).equals("1"))
                    {
                        wordToComparewith=word[j];
                        
                    }
                    else if (isSpecialCase.get(0).equals("2"))
                    {
                        wordToComparewith= isSpecialCase.get(1);
                        
                    }
                    else if (isSpecialCase.get(0).equals("3"))
                    {
                        wordToComparewith= isSpecialCase.get(1);
                        pluWord.add(isSpecialCase.get(2));
                        
                    }
                    
                    
                    pluWord.add(wordToComparewith);
                }
                String modifiedWordj=word[j];
                /**
                 * to see if the word end or begin with some special character to be ignore
                 */
                while (StringUtils.endsWithAny(modifiedWordj, new String [] {".",",",")","]"}))
                {
                    modifiedWordj=modifiedWordj.substring(0, modifiedWordj.length()-1);
                }
                while  (StringUtils.startsWithAny(modifiedWordj, new String [] {".",",","(","["}))
                {
                    modifiedWordj=modifiedWordj.substring(1, modifiedWordj.length());
                }
                
                if(!modifiedWordj.equals(word[j]))
                {
                    pluWord.add(modifiedWordj);
                }
                
                
                /**
                 * add the plural word case
                 * special case for protein named nos
                 */
                if (SpecialCase)
                {
                    if (!wordToComparewith.toLowerCase().equals("no"))
                    {
                        pluWord.add(wordToComparewith+"s");
                    }
                    if (wordToComparewith.endsWith("s"))
                    {
                        pluWord.add(wordToComparewith.substring(0, wordToComparewith.length()-1));
                    }
                }
                /**
                 * pass through all words in the pluword array
                 */
                outerloop:
                for (int L=0 ;L<pluWord.size();L++)
                {
                     
                    boolean added =false;
                    if (pattern.containsKey(pluWord.get(L).toUpperCase()))
                    {
                        /*
                        * get colletion of protein that begin with that word and arrange them from longer to smaller words
                        */
                        Collection<ArrayList<String>> wordArrayLists = pattern.get(pluWord.get(L).toUpperCase());
                        
                        //Sorting wordarraylist from higher size to lower one.
                        List< ArrayList<String> > list = new ArrayList< ArrayList<String> >( wordArrayLists );
                        //      List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
                        Comparator<ArrayList<String>> comparator = new Comparator<ArrayList<String>>() {
                            public int compare(ArrayList<String> c1, ArrayList<String> c2) {
                                return c2.size() - c1.size(); // use your logic
                            }
                        };
                        
                        Collections.sort(list, comparator); // use the comparator as much as u want
                        //System.out.println(list);
                        
                        for (ArrayList<String> wordArrayList : list) {
                          
                            String allprotein=word[j];
                            boolean match=true;
                            int addition=0;
                            int pos = 0;
                            /**
                             * if protein size is only one word
                             */
                            if (wordArrayList.size()==0)
                            {
                                // check next word
                                String toHiglightWord=word[j];
                                int localNotProtein=0;
                                if (!alreadyHighlightWord.contains(toHiglightWord))
                                {
                                     
                                    while ((pos = text.indexOf(toHiglightWord, pos)) >= 0) {
                                      
                                        // Create highlighter using private painter and apply around pattern
                                        if (toHiglightWord.toUpperCase().equals("MR"))
                                        {
                                            boolean containMRI=false;
                                            for (int p=0;p<word.length;p++)
                                            {
                                                if (word[p].toUpperCase().equals("MRI") || word[p].toUpperCase().equals("IMAGING"))
                                                {
                                                    containMRI=true;
                                                }
                                            }
                                            if (containMRI)
                                            {
                                                break outerloop;
                                            }
                                        }
                                        localNotProtein=excel.findRow(workspace+"\\LOCALNonProtein.xlsx", PMID, Date, toHiglightWord.toUpperCase());
                                        if (localNotProtein==-1)
                                        {
                                            // Create highlighter using private painter and apply around pattern
                                            
                                            hilite.addHighlight(pos, pos + toHiglightWord.length(), myHighlightPainter);
                                            
                                            //     hilite.addHighlight(pos+1, pos+1 + word[j].length(), myHighlightPainter);
                                            if (addToResult && FirstTime)
                                            {
                                            excel.AddProteinRecordToResults(PMID,Date,StringUtils.removeEnd(StringUtils.removeEnd(toHiglightWord.toUpperCase(),","),"."),Type,pluWord.get(L),SectionName,countWords,
        countSentence,countWordInSentence);
                                            FirstTime=false;
                                            }
                                        }
                                        pos += toHiglightWord.length()+1;
                                    }
                                    if (localNotProtein==-1)
                                    {
                                        alreadyHighlightWord.add(toHiglightWord);
                                    }
                                    break outerloop;
                                }
                                else{
                                    if (FirstTime)
                                    {
                                 excel.AddProteinRecordToResults(PMID,Date,StringUtils.removeEnd(StringUtils.removeEnd(toHiglightWord.toUpperCase(),","),"."),Type,pluWord.get(L),SectionName,countWords,
        countSentence,countWordInSentence);
                                    FirstTime=false;
                                    }
                                }
                            }
                            /**
                             * protein size is more than one word
                             */
                            else {
                                  String  originalProteinName=pluWord.get(L);
                                for (int k=0;k<wordArrayList.size();k++)
                                {
                                    if (j+k+1<word.length)
                                    {
                                          ArrayList <String>  pluWord2=new ArrayList();
                                        String wordToComparewith2="";
                                        ArrayList <String> isSpecialCase2=this.ContainSpecialCase(word[j+k+1]);
                                     
                                        pluWord2.add(word[j+k+1].toUpperCase());
                                        /**
                                         * for differentiation between added word and already existing ones
                                         */
                                        if (!(k+1==wordArrayList.size()&&wordArrayList.get(k).equals("ADDED")))
                                        {
                                            /**
                                             * repeat the above checks
                                             */
                                            if (SpecialCase)
                                            {
                                                if (isSpecialCase2.get(0).equals("1"))
                                                {
                                                    wordToComparewith2=word[j+k+1];
                                                    
                                                }
                                                else if (isSpecialCase2.get(0).equals("2"))
                                                {
                                                    wordToComparewith2= isSpecialCase2.get(1);;
                                                    
                                                }
                                                else if (isSpecialCase2.get(0).equals("3"))
                                                {
                                                    wordToComparewith2= isSpecialCase2.get(1);
                                                    pluWord2.add(isSpecialCase2.get(2));
                                                    
                                                }
                                                
                                                pluWord2.add(wordToComparewith2.toUpperCase());
                                            }
                                            
                                            /**
                                             * as above
                                             */
                                            String modifiedWordj2=word[j+k+1];
                                            while (StringUtils.endsWithAny(modifiedWordj2, new String [] {".",",",")","]"}))
                                            {
                                                modifiedWordj2=modifiedWordj2.substring(0, modifiedWordj2.length()-1);
                                            }
                                            while  (StringUtils.startsWithAny(modifiedWordj2, new String [] {".",",","(","["}))
                                            {
                                                modifiedWordj2=modifiedWordj2.substring(1, modifiedWordj2.length());
                                            }
                                            
                                            if(!modifiedWordj2.equals(word[j+k+1]))
                                            {
                                                pluWord2.add(modifiedWordj2.toUpperCase());
                                            }
                                            
                                            
                                            
                                            if (SpecialCase)
                                            {
                                                pluWord2.add((wordToComparewith2+"s").toUpperCase());
                                                if (word[j+k+1].endsWith("s"))
                                                {
                                                    pluWord2.add(word[j+k+1].substring(0, word[j+k+1].length()-1).toUpperCase());
                                                }
                                            }
                                            if (!pluWord2.contains(wordArrayList.get(k)))
                                            {
                                                match=false;
                                            }
                                            else {
                                                allprotein=allprotein+" "+word[j+k+1];
                                                addition=addition+word[j+k+1].length();
                                                originalProteinName=originalProteinName+" "+wordArrayList.get(k);
                                            }
                                        }
                                        else
                                        {
                                            added=true;
                                        }
                                    }
                                    else {
                                        match=false;
                                    }
                                    
                                }
                                if (match)
                                {    
                                    if (!alreadyHighlightWord.contains(allprotein))
                                {
                                    HighlightPainter usedhiglighter= myHighlightPainter  ;
                                    if (added)
                                    {
                                        usedhiglighter=myaddedHighlightPainter;
                                        j=j+wordArrayList.size()-1;
                                    }
                                    else {
                                        j=j+wordArrayList.size();
                                    }
                                    //  hilite.addHighlight(pos+1, pos+(1+ word[j].length()+addition), myHighlightPainter);
                                    int localNotProtein=0;
                                    //   boolean FirstTime=true;
                                    while ((pos = text.indexOf(allprotein, pos)) >= 0) {
                                     
                                        localNotProtein=excel.findRow(workspace+"\\LOCALNonProtein.xlsx", PMID, Date, allprotein.toUpperCase());
                                        if (localNotProtein==-1)
                                        {
                                            // Create highlighter using private painter and apply around pattern
                                            hilite.addHighlight(pos, pos + allprotein.length(), usedhiglighter);
                                            
                                            //     hilite.addHighlight(pos+1, pos+1 + word[j].length(), myHighlightPainter);
                                              if (addToResult && FirstTime)
                                            {
                                            excel.AddProteinRecordToResults(PMID,Date,StringUtils.removeEnd(StringUtils.removeEnd(allprotein.toUpperCase(),","),"."),Type,originalProteinName,SectionName,countWords,
        countSentence,countWordInSentence);
                                            FirstTime=false;
                                            }
                                        }
                                        // Create highlighter using private painter and apply around pattern
                                        pos += word[j].length()+1+addition;
                                    }
                                    
                                    if (localNotProtein==-1)
                                    {
                                        alreadyHighlightWord.add(allprotein);
                                    }
                                    break outerloop;
                                }
                                    else {
                                        if (FirstTime)
                                        {
                                      excel.AddProteinRecordToResults(PMID,Date,StringUtils.removeEnd(StringUtils.removeEnd(allprotein.toUpperCase(),","),"."),Type,originalProteinName,SectionName,countWords,
        countSentence,countWordInSentence);
                                        FirstTime=false;
                                        }
                                    }
                                }
                                else {
                                    pos += word[j].length()+1;
                                }
                            }
                        }
                        
                    }
                }
                
               if (StringUtils.endsWith(word[j], ".")&& !word[j].equals("i.e."))
               {
               countSentence++;
               countWordInSentence=0;
               }  
            }
            
        } catch (BadLocationException e) {
            System.out.println(e);
        }
        return alreadyHighlightWord;
    }
      else {
      return null;
      }
    }
    
    /**
     * highlight non protein with a special color to be noticed when removing a protein from database.
     * @param textComp
     * @param pattern
     * @param myHighlightPainter
     */
    public void highlightNonProtein(JTextComponent textComp, String pattern,HighlightPainter myHighlightPainter) {
        // First remove all old highlights
        //  removeHighlights(textComp);
        
        try {
            Highlighter hilite = textComp.getHighlighter();
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());
            int pos = 0;
            
            // Search for pattern
            while ((pos = text.indexOf(pattern, pos)) >= 0) {
                // Create highlighter using private painter and apply around pattern
                hilite.addHighlight(pos, pos + pattern.length(), myHighlightPainter);
                pos += pattern.length();
            }
        } catch (BadLocationException e) {
            System.out.println(e);
        }
    }
    
    /**
     * remove highlight from a text component, used to clear our highlight area.
     * @param textComp
     */
    public void removeHighlights(JTextComponent textComp) {
        Highlighter hilite = textComp.getHighlighter();
        Highlighter.Highlight[] hilites = hilite.getHighlights();
        
        for (int i = 0; i < hilites.length; i++) {
            if (hilites[i].getPainter() instanceof HighlightPainter) {
                hilite.removeHighlight(hilites[i]);
            }
        }
    }
    
    /**
     *
     * @param jTextArea
     * @param turnLightOff
     * @param PMID
     * @param Date
     * @param Type
     * @param workspace
     */
    public void removeHighlights(JTextComponent jTextArea, String turnLightOff,String PMID,String Date,String Type,String workspace) {
        try {
            ExcelReader excel=new ExcelReader();
            Highlighter hilite = jTextArea.getHighlighter();
            
            Highlighter.Highlight[] hilites = hilite.getHighlights();
            Document doc = jTextArea.getDocument();
            String text = doc.getText(0, doc.getLength());
            for (int i = 0; i < hilites.length; i++) {
                String highlightedword=text.substring( hilites[i].getStartOffset(), hilites[i].getEndOffset());
                if (highlightedword.equals(turnLightOff))
                {
                    int wordLenght = hilites[i].getEndOffset() - hilites[i].getStartOffset();
                    
                    if(wordLenght == turnLightOff.length()){
                        
                        if (hilites[i].getPainter() instanceof HighlightPainter) {
                            
                            hilite.removeHighlight(hilites[i]);
                            int localNotProtein;
                            localNotProtein=excel.findRow(workspace+"\\LOCALNonProtein.xlsx", PMID, Date, turnLightOff);
                            if (localNotProtein==-1)
                            {
                                excel.NotProteinRecord(workspace+"\\LOCALNonProtein.xlsx",PMID,Date,turnLightOff.toUpperCase(),Type);
                            }
                            int inresult;
                            inresult=excel.findRow(workspace+"\\results.xlsx", PMID, Date, turnLightOff);
                            if (inresult!=-1)
                            {
                                excel.RemoveProteinRecordFromResults(PMID,Date,turnLightOff);
                            }
                            
                        }
                        
                    }
                }
            }
        } catch (BadLocationException ex) {
            Logger.getLogger(HighlightPainter.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    /**
     * this function is used for special cases words where they end with (,-,[, .....
     * @param word
     * @return
     */
    private  ArrayList<String> ContainSpecialCase(String word) {
        ArrayList<String> retunValue=new ArrayList();
        retunValue.add(0, "1"); //normal case wher only aplhabetic
        
        if (StringUtils.isAlphanumeric(word))
        {
            
            if (StringUtils.isAlpha(word))
            {
                retunValue.add(0, "1");//normal case wher only aplhabetic
            }
            else {
                if (StringUtils.endsWithAny(word, new String[] {"0","1", "2", "3","4","5","6","7","8","9"}))
                {
                    retunValue.add(0, "2"); //Not special case we have numbers at the end
                    String [] words= StringUtils.splitByCharacterType(word);
                    retunValue.add(1, words[0]);
                    
                    
                    
                    
                }
            }
            
        }
        else if (StringUtils.contains(word, "'") && !word.startsWith("'"))
        {
            String [] words= StringUtils.split(word, "'", 2);// split the word to two words befor the "'" and after it if we have many "'" it will not work
            if (words[0]!=null)
            {
                retunValue.add(0, "2");// apostrop case
                retunValue.add(1, words[0]);
            }
            
        }
        else if (StringUtils.contains(word, "*"))
        {
            String [] words= StringUtils.split(word, "*", 2);// split the word to two words befor the "*" and after it if we have many "*" it will not work
            if (words.length>0)
            {
                retunValue.add(0, "2");// apostrop case
                retunValue.add(1, words[0]);
            }
        }
        else if (StringUtils.contains(word, "-"))
        {
            if (StringUtils.contains(word.toLowerCase(), "c-"))
            {
                String [] words= StringUtils.splitByWholeSeparator(word.toLowerCase(), "c-", 2);// split the word to two words befor the "c-" and after it if we have many "c-" it will not work
                retunValue.add(0, "2");// apostrop case
                retunValue.add(1, words[0]);
            }
            else if (StringUtils.contains(word.toLowerCase(), "p-"))
            {
                String [] words= StringUtils.splitByWholeSeparator(word.toLowerCase(), "p-", 2);// split the word to two words befor the "p-" and after it if we have many "p-" it will not work
                retunValue.add(0, "2");// apostrop case
                retunValue.add(1, words[0]);
            }
            else {   String [] words= StringUtils.split(word, "-", 2);// split the word to two words befor the "*" and after it if we have many "*" it will not work
            if (words.length>0)
            {
                retunValue.add(0, "2");// apostrop case
                retunValue.add(1, words[0]);
            }
            }
        }
        else if (StringUtils.contains(word, "/"))
        {
            String [] words= StringUtils.split(word, "/", 2);// split the word to two words befor the "*" and after it if we have many "*" it will not work
            if (words.length>0)
            {
                
                retunValue.add(0, "2");// apostrop case
                retunValue.add(1, words[0]);
            }
        }
        else if (StringUtils.contains(word, "\\") && !word.equals("\\"))
        {
            try {
            String [] words= StringUtils.split(word, "\\", 2);// split the word to two words befor the "*" and after it if we have many "*" it will not work
            retunValue.add(0, "2");// apostrop case
            retunValue.add(1, words[0]);
            } catch (Exception ex)
            {
            System.out.println(" exception in word "+word+"  "+ex);
            }
        }
        else if (StringUtils.contains(word, "("))
        {
            try {
                String [] words= StringUtils.split(word, "(", 2);// split the word to two words befor the "*" and after it if we have many "*" it will not work
                if (words.length>0)
                {
                    if (words[0]!=null)
                    {
                        retunValue.add(0, "3");// apostrop case
                        retunValue.add(1, words[0]);
                        String wordInsideParen= StringUtils.removeEnd(words[0], ")");
                        retunValue.add(2, wordInsideParen);
                    }
                }
            } catch(Exception ex)
            {
                System.out.println(word);
            }
        }
        else if (StringUtils.contains(word, "["))
        {
            String [] words= StringUtils.split(word, "[", 2);// split the word to two words befor the "*" and after it if we have many "*" it will not work
            if (words.length>0)
            {
                retunValue.add(0, "2");// apostrop case
                retunValue.add(1, words[0]);
            }
        }
        
        return retunValue;
    }
    // </editor-fold>
}
