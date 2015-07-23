/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package annotationtest;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import de.linguatools.disco.DISCO;
import de.linguatools.disco.ReturnDataBN;
import de.linguatools.disco.ReturnDataCol;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.tartarus.snowball.ext.PorterStemmer;
/**
 *
 * @author msabra
 */
public class excelreader_vtest {
    FileInputStream file;
    XSSFWorkbook workbook;
    XSSFSheet sheet;
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(getClass());
    // <editor-fold defaultstate="collapsed" desc="Methodes">
       /**
     * 
     * @param excelPath the path of the excel file ( predefined database of terms)
     * @return and array list of multi map first multi map contain the proteins in the excel file
     * second multi map contain the 1 word suggestion
     * third multi map contain the 2 words suggestions
     * fourth multi map contain the 3 words suggestions
     */
    public   ArrayList <Multimap <String,ArrayList<String>>> GetAllProteinAndSimilarWords(String
            excelPath)
    {
        /**
         * initialization of each multimap
         * protein
         * suggestion of 1 words
         * suggestions of two words
         * suggestions of three words
         */
        PorterStemmer stemmer = new PorterStemmer();
        ArrayList <Multimap <String,ArrayList<String>>> returnString= new ArrayList <Multimap<String,ArrayList<String>>>();
        Multimap <String,ArrayList<String>> Proteins = HashMultimap.create();
        Multimap <String,ArrayList<String>> SimilarWords = HashMultimap.create();
        Multimap <String,ArrayList<String>> SimilarWords_2 = HashMultimap.create();
        Multimap <String,ArrayList<String>> SimilarWords_3 = HashMultimap.create();
        
        long startTime = System.currentTimeMillis();
        /**
         * collocation stop array list
         * Si are the similarity Map with similar word as key and value is the score of simulation
         * Si repeat to count how many time this key is repeated
         */
        HashMap<String,Float> Ci_Str= new HashMap<String,Float>();
        HashMap<String,Integer> Ci_Str_repeat= new  HashMap<String,Integer>();
        HashMap<String,String> Si= new  HashMap<String,String>();
         HashMap<String,Integer> Si_repeat= new  HashMap<String,Integer>();
        /**
         * collocation threshold
         */
        float t_c=(float) 3;
        int count_t_c= 100000;
        /**
         * Similarity threshold
         */
        float t_s=(float) 100;
        int count_t_s= 200000;
        /**
         * English threshold
         */
        int englishthreshold=200;
        int britshenglishthreshold=300;
          /**
           * delta and accepted score
           */
          float delta=(float) 1000;
          float delta2=(float) 10;
          float  global_similarity_score_threshold= 68445871;
        float  global_colocation_threshold= 1435699;
        /**
         * loading protein from the excel files and compute the collocation stop along with the similarity suggestions and score if serialized file do not exist
         */
          try{
            File serializedfile = new File(excelPath+"\\"+t_s+"_"+count_t_s+"_"+t_c+"_"+count_t_c+"_"+delta+"_"+global_similarity_score_threshold+"_"+global_colocation_threshold+".ser");
           
            if(serializedfile.exists() && !serializedfile.isDirectory()) {
                try
                {
                    FileInputStream fileIn = new FileInputStream(excelPath+"\\"+t_s+"_"+count_t_s+"_"+t_c+"_"+count_t_c+"_"+delta+"_"+global_similarity_score_threshold+"_"+global_colocation_threshold+".ser");
                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    returnString = (  ArrayList <Multimap <String,ArrayList<String>>>) in.readObject();
                    in.close();
                    fileIn.close();
                    
                FileInputStream file = new FileInputStream(new File(excelPath+"\\formoh-labels.xlsx"));
                
                //Create Workbook instance holding reference to .xlsx file
                XSSFWorkbook workbook = new XSSFWorkbook(file);
                
                //Get first/desired sheet from the workbook
                XSSFSheet sheet = workbook.getSheetAt(0);
                
                
                //Iterate through each rows one by one
                /**
                 * get the hash map of array list of protein
                 * the key of hash map is the first word of the protein
                 */
                Iterator<Row> rowIterator = sheet.iterator();
                while (rowIterator.hasNext())
                {
                    ArrayList<String> wordArrayList = new ArrayList<String>();
                    
                    Row row = rowIterator.next();
                    //For each row, iterate through all the columns
                    Iterator<Cell> cellIterator = row.cellIterator();
                    Cell proteinCell = row.getCell(0);
                    if (row.getRowNum()  ==0)
                    {
                        row = rowIterator.next();
                        cellIterator = row.cellIterator();
                        proteinCell = row.getCell(0);
                    }
                    if (row.getRowNum() % 100 ==0)
                    {
                        System.out.println(row.getRowNum());
                    }
                    String[] splited=null;
                    try {
                        
                        splited =StringUtils.split(proteinCell.getStringCellValue());//
                        proteinCell.getStringCellValue().split("\\s+");
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    for (int j=1;j<splited.length;j++){
                        wordArrayList.add(splited[j].toUpperCase());
                    }
                    Cell AddedproteinCell = row.getCell(2);
                    if (AddedproteinCell!=null)
                    {
                        if (AddedproteinCell.getStringCellValue().equals("ADDED"))
                        {
                            wordArrayList.add("ADDED");
                        }
                    }
                    Proteins.put(splited[0].toUpperCase(),wordArrayList);
                }
                returnString.remove(0);
                    returnString.add(0, Proteins);
                    
                    return returnString;
                }catch(IOException | ClassNotFoundException i)
                {
                    System.out.println(i);
                }
            }
                DISCO disco = new DISCO(excelPath+"\\en-PubMedOA-20070501", false);
              DISCO disco_wiki = new DISCO(excelPath+"\\enwiki-20130403-sim-lemma-mwl-lc", false);
                 DISCO disco_britsh = new DISCO(excelPath+"\\en-BNC-20080721", false);
            try
            {
             
                FileInputStream file = new FileInputStream(new File(excelPath+"\\formoh-labels.xlsx"));
                
                //Create Workbook instance holding reference to .xlsx file
                XSSFWorkbook workbook = new XSSFWorkbook(file);
                
                //Get first/desired sheet from the workbook
                XSSFSheet sheet = workbook.getSheetAt(0);
                
                
                //Iterate through each rows one by one
                /**
                 * get the hash map of array list of protein
                 * the key of hash map is the first word of the protein
                 */
                Iterator<Row> rowIterator = sheet.iterator();
                while (rowIterator.hasNext())
                {
                    ArrayList<String> wordArrayList = new ArrayList<String>();
                    
                    Row row = rowIterator.next();
                    //For each row, iterate through all the columns
                    Iterator<Cell> cellIterator = row.cellIterator();
                    Cell proteinCell = row.getCell(0);
                    if (row.getRowNum()  ==0)
                    {
                        row = rowIterator.next();
                        cellIterator = row.cellIterator();
                        proteinCell = row.getCell(0);
                    }
                    if (row.getRowNum() % 100 ==0)
                    {
                        System.out.println(row.getRowNum());
                    }
                    String[] splited=null;
                    try {
                        
                        splited =StringUtils.split(proteinCell.getStringCellValue());//
                        proteinCell.getStringCellValue().split("\\s+");
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    for (int j=1;j<splited.length;j++){
                        wordArrayList.add(splited[j].toUpperCase());
                    }
                    Cell AddedproteinCell = row.getCell(2);
                    if (AddedproteinCell!=null)
                    {
                        if (AddedproteinCell.getStringCellValue().equals("ADDED"))
                        {
                            wordArrayList.add("ADDED");
                        }
                    }
                    Proteins.put(splited[0].toUpperCase(),wordArrayList);
                    
                    
                    
                    
                    ArrayList <ReturnDataBN> SiList= new ArrayList <ReturnDataBN>();
                    ArrayList <ReturnDataCol[]> CiList=new ArrayList <ReturnDataCol[]>();
                    ArrayList<  HashMap <String,Float>> Ci_StrList= new  ArrayList< HashMap <String,Float>>();
                    ArrayList<  HashMap <String,String>> Si_StrList= new  ArrayList<  HashMap <String,String>>();
                    for (int d=0;d<splited.length;d++)
                    {
                        try{
                            
                            SiList.add(d,disco.similarWords(StringUtils.remove(splited[d], '-')));
                            
                        } catch (Exception e){
                            System.out.println("failed to compute similarity of  "+StringUtils.remove(splited[d], '-')+e);
                            // SimilarWord_2.add(1,"0.0");
                        }
                        try{
                            
                            CiList.add(disco.collocations(StringUtils.remove(splited[d], '-')));
                            
                        } catch (Exception e){
                            log.debug("failed to compute collacation of  "+StringUtils.remove(splited[d], '-')+e);
                            // SimilarWord_2.add(1,"0.0");
                        }
                    }
                    
                    /**
                     * INTERSECTION OF COLOCATIONS
                     */
                    for (int l=0;l<CiList.size();l++)
                    {
                        if (CiList.get(l)!=null)
                        {
                        for (int f=0;f<CiList.get(l).length;f++)
                        {
                            if (CiList.get(l)[f].value>t_c && f<count_t_c)
                            {
                                if (Ci_StrList.size()<=l)
                                {
                                      HashMap <String,Float> temp=new  HashMap <String,Float>();
                                    temp.put(CiList.get(l)[f].word,CiList.get(l)[f].value);
                                   
                                    Ci_StrList.add(Ci_StrList.size(), temp);
                             //   Ci_StrList.add(Ci_StrList.size(), new ArrayList<String> (Arrays.asList(CiList.get(l)[f].word)));
                                }
                                else {
                                       Ci_StrList.get(l).put(CiList.get(l)[f].word,CiList.get(l)[f].value);
                              //  Ci_StrList.get(l).add(CiList.get(l)[f].word);
                                }
                            }
                            
                            else {break;}
                        }
                        }
                    }
                      if (Ci_StrList.size()>0)
                    {
                    for (Map.Entry<String, Float> entry : Ci_StrList.get(0).entrySet())
                    {
                        Float lowestValue=entry.getValue();
                        boolean intersection=true;
                        int a=1;
                        while (a<Ci_StrList.size())
                        {
                            // skip if Si_StrList.get(a) is null
                            if (Ci_StrList.get(a)!=null)
                            {
                            if (Ci_StrList.get(a).containsKey(entry.getKey()))
                            {
                                if (entry.getValue()>Ci_StrList.get(a).get(entry.getKey()))
                                {
                                    lowestValue=Ci_StrList.get(a).get(entry.getKey());
                                }
                            }
                            else {
                                intersection=false;
                            }
                            }
                            a++;
                        }
                        if (intersection)
                        { int freq=0;
                    
                          int freq2=0;
                          int freq3=0;
                            try{
                             freq=disco_wiki.frequency(entry.getKey());
                            }catch(Exception e)
                            {
                            }
                             try{
                             freq2=disco_britsh.frequency(entry.getKey().toUpperCase());
                            }catch(Exception e)
                            {
                            }
                              try{
                             freq3=disco_britsh.frequency(entry.getKey().toLowerCase());
                            }catch(Exception e)
                            {
                            }
                           
                          if (freq < englishthreshold && freq2 < britshenglishthreshold && freq3 < britshenglishthreshold)
                          {
                            if (Ci_Str.containsKey(entry.getKey().toUpperCase()))
                            {
                               Float sumofscores=lowestValue*(delta+(Ci_Str_repeat.get(entry.getKey().toUpperCase())*delta2))+(Ci_Str.get(entry.getKey().toUpperCase()));
                                Ci_Str.put(entry.getKey().toUpperCase(),sumofscores);
                                 Ci_Str_repeat.put(entry.getKey().toUpperCase(),Ci_Str_repeat.get(entry.getKey().toUpperCase())+1);
                            }
                            else {
                                Ci_Str.put(entry.getKey().toUpperCase(),lowestValue);
                                 Ci_Str_repeat.put(entry.getKey().toUpperCase(),1);
                            }
                          }
                          else 
                          {
                               //   System.out.println("english"+entry.getKey());
                                  }
                          
                        }
                    }
                    }
                    /**
                     * INTERSECTION OF SIMULARITY
                     */
                    
                    for (int l=0;l<SiList.size();l++)
                    {
                       if (SiList.get(l)!=null)
                        {   
                        for (int f=1;f<SiList.get(l).words.length;f++)
                        {
                          
                            if (Float.parseFloat(SiList.get(l).values[f])>t_s && f<count_t_s)
                            {
                                if (Si_StrList.size()<=l)
                                {
                                    HashMap <String,String> temp=new  HashMap <String,String>();
                                    temp.put(SiList.get(l).words[f],SiList.get(l).values[f]);
                                   
                                    Si_StrList.add(Si_StrList.size(), temp);
                         //       Si_StrList.add(l,temp);
//new HashMap<String,String> (Arrays.asList(CiList.get(l)[f].word)));
                                }
                                else {
                                Si_StrList.get(l).put(SiList.get(l).words[f],SiList.get(l).values[f]);
                                }
                            }
                            
                            else {break;}
                        }
                        }
                    }
                    if (Si_StrList.size()>0)
                    {
                    for (Map.Entry<String, String> entry : Si_StrList.get(0).entrySet())
                    {
                        String lowestValue=entry.getValue();
                        boolean intersection=true;
                        int a=1;
                        while (a<Si_StrList.size())
                        {
                            // skip if Si_StrList.get(a) is null
                            if (Si_StrList.get(a)!=null)
                            {
                            if (Si_StrList.get(a).containsKey(entry.getKey()))
                            {
                                if (Float.parseFloat(entry.getValue())>Float.parseFloat(Si_StrList.get(a).get(entry.getKey())))
                                {
                                    lowestValue=Si_StrList.get(a).get(entry.getKey());
                                }
                            }
                            else {
                                intersection=false;
                            }
                            }
                            a++;
                        }
                        if (intersection)
                        { int freq=0;
                    
                          int freq2=0;
                          int freq3=0;
                            try{
                             freq=disco_wiki.frequency(entry.getKey());
                            }catch(Exception e)
                            {
                            }
                             try{
                             freq2=disco_britsh.frequency(entry.getKey().toUpperCase());
                            }catch(Exception e)
                            {
                            }
                              try{
                             freq3=disco_britsh.frequency(entry.getKey().toLowerCase());
                            }catch(Exception e)
                            {
                            }
                               String stemedword="";
                               stemmer.setCurrent(entry.getKey());
                               stemmer.stem();
                               stemedword= stemmer.getCurrent();
                          if (freq < englishthreshold && freq2 < britshenglishthreshold && freq3 < britshenglishthreshold && stemedword.equals(entry.getKey()))
                          {
                            if (Si.containsKey(entry.getKey().toUpperCase()))
                            {
                                Float sumofscores=(Float.parseFloat(lowestValue)*(delta+(Si_repeat.get(entry.getKey().toUpperCase())*delta2)))+(Float.parseFloat(Si.get(entry.getKey().toUpperCase())));
                                Si.put(entry.getKey().toUpperCase(),sumofscores.toString());
                                 Si_repeat.put(entry.getKey().toUpperCase(),Si_repeat.get(entry.getKey().toUpperCase())+1);
                            }
                            else {
                                Si.put(entry.getKey().toUpperCase(),lowestValue);
                                Si_repeat.put(entry.getKey().toUpperCase(),1);
                            }
                          }
                          else 
                          {
                               //   System.out.println("english"+entry.getKey());
                                  }
                          
                        }
                    }
                    }
                }
                file.close();
            }
            catch (Exception e)
            {
                
                e.printStackTrace();
            }
          System.out.println("***************************************");
          for (String key : Ci_Str.keySet()) {
                System.out.println(key + "\t" + Ci_Str.get(key));
          }
            System.out.println("***************************************");
            int count=0;
           
            for (Map.Entry<String, String> entry : Si.entrySet())
            {
                 try{
                if (count % 100 ==0)
                    {
                        System.out.println(count+" of "+ Si.size());
                    }
                if (Float.parseFloat(entry.getValue())>global_similarity_score_threshold)
                {
                ArrayList<String> SimilarWord = new ArrayList<String>();
                
                SimilarWord.add(0,entry.getValue());
                SimilarWord.add(1,Si_repeat.get(entry.getKey()).toString());
                SimilarWords.put(entry.getKey(),SimilarWord );
                
                
                ReturnDataCol[] collocationOfSi=null;
                try{
                    collocationOfSi = disco.collocations(entry.getKey());
                    // and print the first 20 to stdout
                } catch (Exception e){
                    log.debug("failed to compute collacation of  "+entry.getKey() +" "+e);
                    // SimilarWord_2.add(1,"0.0");
                }
                if (collocationOfSi!=null)
                {
                    // System.out.println("Collocations:");
                    for(int i = 1; i < collocationOfSi.length; i++){
                        if (collocationOfSi[i].value>t_c && i<count_t_c)
                        {
                           if (Ci_Str.containsKey(collocationOfSi[i].word) && Ci_Str.get(collocationOfSi[i].word) > global_colocation_threshold)
                            {
                                //  System.out.println(" found a common coallocation" +collocationOfSi[i].word);
                            }
                            else {
                                ArrayList<String> SimilarWord_2 = new ArrayList<String>();
                                SimilarWord_2.add(collocationOfSi[i].word);
                                //   System.out.println("\t"+collocationResult[i].word+"\t"+
                                //    collocationResult[i].value);
                                SimilarWords_2.put(entry.getKey().toUpperCase(),SimilarWord_2 );
                                
                                ReturnDataCol[] collocationOfSi2=null;
                                try{
                                    collocationOfSi2 = disco.collocations(collocationOfSi[i].word);
                                    // and print the first 20 to stdout
                                } catch (Exception e){
                                    log.debug("failed to compute collacation of  "+entry.getKey()+" "+e);
                                    // SimilarWord_2.add(1,"0.0");
                                }
                                if (collocationOfSi2!=null)
                                {
                                    // System.out.println("Collocations:");
                                    for(int a = 1; a < collocationOfSi2.length; a++){
                                        if (collocationOfSi2[a].value>t_c && a<count_t_c)
                                        {
                                            if ( Ci_Str.containsKey(collocationOfSi2[a].word) && Ci_Str.get(collocationOfSi[i].word) > global_colocation_threshold)
                                            {
                                                //  System.out.println(" found a common coallocation" +collocationOfSi[i].word);
                                            }
                                            else {
                                                ArrayList<String> SimilarWord_3 = new ArrayList<String>();
                                                SimilarWord_3.add(collocationOfSi[i].word);
                                                SimilarWord_3.add(collocationOfSi2[a].word);
                                                //   System.out.println("\t"+collocationResult[i].word+"\t"+
                                                //    collocationResult[i].value);
                                                SimilarWords_3.put(entry.getKey().toUpperCase(),SimilarWord_3 );
                                            }
                                        }
                                        else {
                                            break;
                                        }
                                    }
                                }
                           } // colocation stop
                            }
                else {
                    break;
                }
                        }
                        
                    }
            }
             count++;
              }
            catch (Exception e)
            {
                
                e.printStackTrace();
            }
            }
            
            
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            System.out.println("loading proteins and similar words in "+elapsedTime+" ms.");
            for(Iterator<Map.Entry<String, ArrayList<String>>> it = SimilarWords.entries().iterator(); it.hasNext(); ) {
                Map.Entry<String, ArrayList<String>> entry = it.next();
                if(Proteins.containsKey(entry.getKey())) {
                    Collection<ArrayList<String>> wordArrayLists = Proteins.get(entry.getKey().toUpperCase());
                    List< ArrayList<String> > list = new ArrayList< ArrayList<String> >( wordArrayLists );
                    for (int h=0;h<list.size();h++)
                    {
                        if (list.get(h).size()==0)
                        {
                          //  it.remove();
                            System.out.println(entry.getKey()+"\t"+Float.parseFloat(entry.getValue().get(0))+"\t"+Float.parseFloat(entry.getValue().get(1)));
                            break;
                        }
                        else  if (list.get(h).size()==1 && list.get(h).get(0).equals("ADDED"))
                        {
                          //  it.remove();
                              System.out.println(entry.getKey()+"\t"+Float.parseFloat(entry.getValue().get(0))+"\t"+Float.parseFloat(entry.getValue().get(1)));
                            break;
                        }
                    }
                    // it.remove();
                }
            }
            
            for(Iterator<Map.Entry<String, ArrayList<String>>> it = SimilarWords_2.entries
                ().iterator(); it.hasNext(); ) {
                Map.Entry<String, ArrayList<String>> entry = it.next();
                if(Proteins.containsKey(entry.getKey()) ) {
                    Collection<ArrayList<String>> wordArrayLists = Proteins.get(entry.getKey().toUpperCase());
                    List< ArrayList<String> > list = new ArrayList< ArrayList<String> >( wordArrayLists );
                    // outerloop:
                    for (int h=0;h<list.size();h++)
                    {
                        if (list.get(h).size()==1 )
                        {
                            Collection<ArrayList<String>> wordArrayLists2 = SimilarWords_2.get(entry.getKey
                ().toUpperCase());
                            List< ArrayList<String> > list2 = new ArrayList< ArrayList<String> >( wordArrayLists2 );
                            for (int h2=0;h2<list2.size();h2++)
                            {
                                if (list2.get(h2).get(0).equals(list.get(h).get(0)))
                                //    it.remove();
                                     System.out.println("2 words"+entry.getKey()+"\t"+Float.parseFloat(entry.getValue().get(0))+"\t"+Float.parseFloat(entry.getValue().get(1)));
                                break ;// outerloop;
                            }
                            
                        }
                        else if (list.get(h).size()==2 && list.get(h).get(1).equals("ADDED"))
                        {
                            Collection<ArrayList<String>> wordArrayLists2 = SimilarWords_2.get(entry.getKey
                ().toUpperCase());
                            List< ArrayList<String> > list2 = new ArrayList< ArrayList<String> >( wordArrayLists2 );
                            for (int h2=0;h2<list2.size();h2++)
                            {
                                if (list2.get(h2).get(0).equals(list.get(h).get(0)))
                                //    it.remove();
                                        System.out.println("2 words"+entry.getKey()+"\t"+Float.parseFloat(entry.getValue().get(0))+"\t"+Float.parseFloat(entry.getValue().get(1)));
                                break;
                            }
                        }
                    }
                    //  it.remove();
                }
            }
            long endTime2 = System.currentTimeMillis();
            long elapsedTime2 = endTime2 - endTime;
            System.out.println("removing dublicates in  "+elapsedTime2+" ms.");
            returnString.add(0, Proteins);
            returnString.add(1, SimilarWords);
            returnString.add(2, SimilarWords_2);
            returnString.add(3, SimilarWords_3);
            FileOutputStream fileOut;
            ObjectOutputStream out = null;
            fileOut = new FileOutputStream(excelPath+"\\"+t_s+"_"+count_t_s+"_"+t_c+"_"+count_t_c +"_"+delta+"_"+global_similarity_score_threshold+"_"+global_colocation_threshold+".ser");
            out = new ObjectOutputStream(fileOut);
            System.out.println("creating a serialization");
            out.writeObject(returnString);
            System.out.println("finish creating a serialization");
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in "+t_s+"_"+count_t_s+"_"+t_c+"_"+count_t_c+"_"+delta+"_"+global_similarity_score_threshold+"_"+global_colocation_threshold+".ser");
        }
        catch (Exception ex)
        {
            
        }
        return returnString;
    }
    
}
