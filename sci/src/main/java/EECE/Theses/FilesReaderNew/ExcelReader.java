/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package EECE.Theses.FilesReaderNew;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import de.linguatools.disco.DISCO;
import de.linguatools.disco.ReturnDataBN;
import de.linguatools.disco.ReturnDataCol;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author msabra
 */
public class ExcelReader implements java.io.Serializable{
    FileInputStream file;
    XSSFWorkbook workbook;
    XSSFSheet sheet;
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(getClass());
    // <editor-fold defaultstate="collapsed" desc="Methodes">
    
    public   ArrayList <Multimap <String,ArrayList<String>>> GetAllProteinAndSimilarWords(String
            excelPath)
    {
        ArrayList <Multimap <String,ArrayList<String>>> returnString= new ArrayList <Multimap<String,ArrayList<String>>>();
        Multimap <String,ArrayList<String>> Proteins = HashMultimap.create();
        Multimap <String,ArrayList<String>> SimilarWords = HashMultimap.create();
        Multimap <String,ArrayList<String>> SimilarWords_2 = HashMultimap.create();
        Multimap <String,ArrayList<String>> SimilarWords_3 = HashMultimap.create();
        long startTime = System.currentTimeMillis(); 
        ArrayList <String> Ci_Str= new  ArrayList <String>();
        /**
         * collocation threshold
         */
        float t_c=(float) 25.0;
        int count_t_c= 100000;
        /**
         * Similarity threshold
         */
        float t_s=(float) 200;
        int count_t_s= 200000;
        
        File serializedfile = new File(excelPath+"\\"+t_s+"_"+count_t_s+"_"+t_c+"_"+count_t_c+".ser");
        if(serializedfile.exists() && !serializedfile.isDirectory()) {
            try
            {
                FileInputStream fileIn = new FileInputStream(excelPath+"\\"+t_s+"_"+count_t_s+"_"+t_c+"_"+count_t_c+".ser");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                returnString = (  ArrayList <Multimap <String,ArrayList<String>>>) in.readObject();
                in.close();
                fileIn.close();
                return returnString;
            }catch(IOException | ClassNotFoundException i)
            {
                System.out.println(i);
            }
        }
        try
        {
            DISCO disco = new DISCO(excelPath+"\\en-PubMedOA-20070501", false);
            DISCO disco_wiki = new DISCO(excelPath+"\\enwiki-20130403-sim-lemma-mwl-lc", false);
            FileInputStream file = new FileInputStream(new File(excelPath+"\\formoh-labels_2.xlsx"));
            
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
                
                /**
                 * for each protein in the protein database take its similarity and collocation
                 */
                
                if (splited.length==1)
                {
                    ReturnDataBN Si= new ReturnDataBN();
                    ReturnDataCol[] Ci=null;
                    try{
                        
                        Si=disco.similarWords(StringUtils.remove(splited[0], '-'));
                        
                    } catch (Exception e){
                        System.out.println("failed to compute similarity of  "+StringUtils.remove(splited[0], '-')+e);
                        // SimilarWord_2.add(1,"0.0");
                    }
                    try{
                        Ci= disco.collocations(StringUtils.remove(splited[0], '-'));
                        for (int f=1;f<Ci.length;f++)
                        {
                            if (Ci[f].value>t_c && f<count_t_c)
                            {
                                Ci_Str.add(Ci[f].word);
                            }
                        }
                    } catch (Exception e){
                        log.debug("failed to compute collacation of  "+StringUtils.remove(splited[0], '-')+e);
                        // SimilarWord_2.add(1,"0.0");
                    }
                    if (Si!=null)
                    {
                        for(int k = 1; k < Si.words.length; k++){
                            if (Float.parseFloat(Si.values[k])>t_s && k<count_t_s)
                            {
                                if (Ci_Str.contains(Si.words[k]))
                                {
                                    log.debug("weirf similar word and coalocation word are the same for "+proteinCell.getStringCellValue()+" \n"+Si.words[k]);
                                }
                                ArrayList<String> SimilarWord = new ArrayList<String>();
                                
                                SimilarWord.add(0,Si.values[k]);
                                try{
                                    float s2 = disco.secondOrderSimilarity(proteinCell.getStringCellValue(), Si.words[k]);
                                    SimilarWord.add(1,String.valueOf(s2));
                                } catch (Exception e){
                                    log.debug("failed to compute similarity between "+proteinCell.getStringCellValue()+
                                            " and " +Si.words[k]+" exception " +e );
                                    SimilarWord.add(1,"0.0");
                                }
                                SimilarWords.put(Si.words[k].toUpperCase(),SimilarWord );
                                
                                
                                ReturnDataCol[] collocationOfSi=null;
                                try{
                                    collocationOfSi = disco.collocations(Si.words[k]);
                                    // and print the first 20 to stdout
                                } catch (Exception e){
                                    log.debug("failed to compute collacation of  "+Si.words[k]+e);
                                    // SimilarWord_2.add(1,"0.0");
                                }
                                if (collocationOfSi!=null)
                                {
                                    // System.out.println("Collocations:");
                                    for(int i = 1; i < collocationOfSi.length; i++){
                                        if (collocationOfSi[i].value>t_c && i<count_t_c)
                                        {
                                            if (splited.length==1 && Ci_Str.contains(collocationOfSi[i].word))
                                            {
                                                //  System.out.println(" found a common coallocation" +collocationOfSi[i].word);
                                            }
                                            else {
                                                ArrayList<String> SimilarWord_2 = new ArrayList<String>();
                                                SimilarWord_2.add(collocationOfSi[i].word);
                                                //   System.out.println("\t"+collocationResult[i].word+"\t"+
                                                //    collocationResult[i].value);
                                                SimilarWords_2.put(Si.words[k].toUpperCase(),SimilarWord_2 );
                                                
                                                ReturnDataCol[] collocationOfSi2=null;
                                                try{
                                                    collocationOfSi2 = disco.collocations(collocationOfSi[i].word);
                                                    // and print the first 20 to stdout
                                                } catch (Exception e){
                                                    log.debug("failed to compute collacation of  "+Si.words[k]+e);
                                                    // SimilarWord_2.add(1,"0.0");
                                                }
                                                if (collocationOfSi2!=null)
                                                {
                                                    // System.out.println("Collocations:");
                                                    for(int a = 1; a < collocationOfSi2.length; a++){
                                                        if (collocationOfSi2[a].value>t_c && a<count_t_c)
                                                        {
                                                            if (splited.length==1 && Ci_Str.contains(collocationOfSi2[a].word))
                                                            {
                                                                //  System.out.println(" found a common coallocation" +collocationOfSi[i].word);
                                                            }
                                                            else {
                                                                ArrayList<String> SimilarWord_3 = new ArrayList<String>();
                                                                SimilarWord_3.add(collocationOfSi[i].word);
                                                                SimilarWord_3.add(collocationOfSi2[a].word);
                                                                //   System.out.println("\t"+collocationResult[i].word+"\t"+
                                                                //    collocationResult[i].value);
                                                                SimilarWords_3.put(Si.words[k].toUpperCase(),SimilarWord_3 );
                                                            }
                                                        }
                                                        else {break;}
                                                    }
                                                    
                                                }                                               
                                            }
                                        }
                                        else {break;}
                                    }
                                    
                                }
                            }
                            else {
                                break;
                            }
                        }
                        
                    }
                    else {
                        log.debug("Si is null for "+proteinCell.getStringCellValue());
                    }
                }
                else {
                    HashMap<String,String> Si= new  HashMap<String,String>();
                    
                    ArrayList <ReturnDataBN> SiList= new ArrayList <ReturnDataBN>();
                    ArrayList <ReturnDataCol[]> CiList=new ArrayList <ReturnDataCol[]>();
                    ArrayList<  ArrayList <String>> Ci_StrList= new  ArrayList< ArrayList <String>>();
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
                    for (int l=1;l<CiList.size();l++)
                    {
                        for (int f=0;f<CiList.get(l).length;f++)
                        {
                            if (CiList.get(l)[f].value>t_c && f<count_t_c)
                            {
                                Ci_StrList.get(l).add(CiList.get(l)[f].word);
                            }
                            
                            else {break;}
                        }
                    }
                    for (int k=0;k<Ci_StrList.get(0).size();k++)
                    {
                        boolean intersection=true;
                        int a=0;
                        while (a<Ci_StrList.size())
                        {
                            a++;
                            if (Ci_StrList.get(a).contains(Ci_StrList.get(0).get(k)))
                            {
                                
                            }
                            else {
                                intersection=false;
                            }
                        }
                        if (intersection)
                        {
                            Ci_Str.add(Ci_StrList.get(0).get(k));
                        }
                    }
                    /**
                     * INTERSECTION OF SIMULARITY
                     */
                    
                    for (int l=1;l<SiList.size();l++)
                    {
                        for (int f=0;f<SiList.get(l).words.length;f++)
                        {
                            if (Float.parseFloat(SiList.get(l).values[f])>t_s && f<count_t_s)
                            {
                                Si_StrList.get(l).put(SiList.get(l).words[f],SiList.get(l).values[f]);
                            }
                            
                            else {break;}
                        }
                    }
                    for (Map.Entry<String, String> entry : Si_StrList.get(0).entrySet())
                    {
                        String lowestValue="0.0";
                        boolean intersection=true;
                        int a=0;
                        while (a<Si_StrList.size())
                        {
                            a++;
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
                        if (intersection)
                        {
                            Si.put(entry.getKey(),lowestValue);
                        }
                    }
                    
                    
                    
                    if ( Si.size()>0)
                        
                    {
                        
                        
                        
                        
                        for (Map.Entry<String, String> entry : Si.entrySet())
                        {
                            
                            if (Ci_Str.contains(entry.getKey()))
                            {
                                log.debug("weirf similar word and coalocation word are the same for "+proteinCell.getStringCellValue()+" \n"+entry.getKey());
                            }
                            ArrayList<String> SimilarWord = new ArrayList<String>();
                            
                            SimilarWord.add(0,entry.getValue());
                            
                            SimilarWords.put(entry.getKey().toUpperCase(),SimilarWord );
                            
                            
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
                                        if (Ci_Str.contains(collocationOfSi[i].word))
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
                                                        if (splited.length==1 && Ci_Str.contains(collocationOfSi2[a].word))
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
                                                    else {break;}
                                                }
                                                
                                            }
                                            
                                            
                                            
                                            
                                        }
                                        
                                    }
                                    
                                }
                            }
                            else {
                                break;
                            }
                            
                            
                            //System.out.println("\t"+simResult.words[k]+"\t"+simResult.values[k]);
                            
                        }
                        
                    }
                    else {
                        log.debug("Si is null for "+proteinCell.getStringCellValue());
                    }
                    
                }
                
                
                
                // here eberu Si will be Si[j] j is the number of words in each protein
                
                
            }
            file.close();
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            System.out.println("loading proteins and similar words in "+elapsedTime+" ms.");
            for(Iterator<Map.Entry<String, ArrayList<String>>> it = SimilarWords.entries().iterator
        (); it.hasNext(); ) {
                Map.Entry<String, ArrayList<String>> entry = it.next();
                if(Proteins.containsKey(entry.getKey())) {
                    Collection<ArrayList<String>> wordArrayLists = Proteins.get(entry.getKey().toUpperCase());
                    List< ArrayList<String> > list = new ArrayList< ArrayList<String> >( wordArrayLists );
                    for (int h=0;h<list.size();h++)
                    {
                        if (list.get(h).size()==0)
                        {
                            it.remove();
                            break;
                        }
                        else  if (list.get(h).size()==1 && list.get(h).get(0).equals("ADDED"))
                        {
                            it.remove();
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
                                    it.remove();
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
                                    it.remove();
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
            fileOut = new FileOutputStream(excelPath+"\\"+t_s+"_"+count_t_s+"_"+t_c+"_"+count_t_c
                    +".ser");
            out = new ObjectOutputStream(fileOut);
            System.out.println("creating a serialization");
            out.writeObject(returnString);
            System.out.println("finish creating a serialization");
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in "+t_s+"_"+count_t_s+"_"+t_c+"_"+count_t_c
                    +".ser");
            
        }
        catch (Exception e)
        {
            
            e.printStackTrace();
        }
        return returnString;
    }
    
    
    
    /**
     *
     * @param excelPath
     * @return
     * return all protein in the protein excel file as a hash map of string to array list of strings
     * the key is the first word of the protein
     * it can be used for any case protein or something else
     */
    public   Multimap <String,ArrayList<String>> GetAllProtein(String excelPath)
    {
        
        Multimap <String,ArrayList<String>> returnString = HashMultimap.create();
        int i =0;
        try
        {
            FileInputStream file = new FileInputStream(new File(excelPath));
            
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            
            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
            
            
            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext())
            {
                ArrayList<String> wordArrayList = new ArrayList<String>();
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();
                Cell proteinCell = row.getCell(0);
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
                returnString.put(splited[0].toUpperCase(),wordArrayList);
                
                i++;
            }
            file.close();
        }
        catch (Exception e)
        {
            
            e.printStackTrace();
        }/*
        * FileOutputStream fileOut;
        * ObjectOutputStream out = null;
        * try {
        * fileOut = new FileOutputStream("tmp/proteinlabels.ser");
        * out = new ObjectOutputStream(fileOut);
        * out.writeObject(returnString);
        * out.close();
        * fileOut.close();
        * System.out.printf("Serialized data is saved in /tmp/employee.ser");
        * }    catch (IOException ex) {
        * Logger.getLogger(ExcelReader.class.getName()).log(Level.SEVERE, null, ex);
        * } finally {
        * try {
        * out.close();
        * } catch (IOException ex) {
        * Logger.getLogger(ExcelReader.class.getName()).log(Level.SEVERE, null, ex);
        * }
        * }*/
        return returnString;
    }
    
    /**
     *
     * @param excelPath
     * @return
     * get all labels from an excel file,
     * return the first and second column separated by '/', in an array list of string
     */
    public   ArrayList <String> GetAllLables(String excelPath)
    {
        ArrayList <String> returnString = new  ArrayList <String>();
        int i =0;
        try
        {
            FileInputStream file = new FileInputStream(new File(excelPath));
            
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            
            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
            
            
            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext())
            {
                //   ArrayList<String> wordArrayList = new ArrayList<String>();
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();
                Cell LablCell = row.getCell(0);
                // proteinCell.getStringCellValue().split("\\s+");
                
                Cell AddedproteinCell = row.getCell(1);
                
                returnString.add(LablCell.getStringCellValue
        ()+"/"+AddedproteinCell.getStringCellValue());
                
                i++;
            }
            file.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return returnString;
    }
    
    /**
     *
     * @param excelPath
     * @param Protein
     * @param Color
     * add label to an excel file, label as first column and color as second column
     */
    public void AddLableToList(String excelPath,String Protein,String Color)
    {
        try{
            FileInputStream file = new FileInputStream(new File(excelPath));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            
            XSSFSheet sheet = workbook.getSheetAt(0);
            
            int listnumber=    sheet.getPhysicalNumberOfRows();
            Row row = sheet.createRow(listnumber);
            row.createCell(0).setCellValue(Protein);
            row.createCell(1).setCellValue(Color);
            //   row.createCell(2).setCellValue("ADDED");
            //row.createCell(2).setCellValue(Protein);
            
            file.close();
            
            FileOutputStream outFile =new FileOutputStream(new File(excelPath));
            workbook.write(outFile);
            outFile.close();
            
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    /**
     *
     * @param excelPath
     * @param Protein
     *  when adding a protein in the abstract we add it to the excel file of protein
     */
    
    public void AddProteinToList(String excelPath,String Protein)
    {
        
        try{
            FileInputStream file = new FileInputStream(new File(excelPath));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            
            XSSFSheet sheet = workbook.getSheetAt(0);
            
            int listnumber=    sheet.getPhysicalNumberOfRows();
            Row row = sheet.createRow(listnumber);
            row.createCell(0).setCellValue(Protein);
            row.createCell(1).setCellValue("TRUE");
            row.createCell(2).setCellValue("ADDED");
            //row.createCell(2).setCellValue(Protein);
            
            file.close();
            
            FileOutputStream outFile =new FileOutputStream(new File(excelPath));
            workbook.write(outFile);
            outFile.close();
            
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * open a excel file one times
     * @param excelPath
     */
    public void open(String excelPath) {
        try{
            file = new FileInputStream(new File(excelPath));
            workbook = new XSSFWorkbook(file);
            
            sheet = workbook.getSheetAt(0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * used to add a protein to an excel file ( result or local non protein, or labels ecel ..)
     * @param excelPath
     * @param PMID
     * @param Date
     * @param Protein
     * @param Label
     */
    
    
    public void NotProteinRecord(String excelPath,String PMID,String Date,String Protein,String Label)
    {
        try{
            FileInputStream file = new FileInputStream(new File(excelPath));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowindex=  sheet.getLastRowNum()+1;
            
            
            Row row = sheet.createRow(rowindex);
            row.createCell(0).setCellValue(PMID);
            row.createCell(1).setCellValue(Date);
            row.createCell(2).setCellValue(Protein);
            
            row.createCell(3).setCellValue(Label);
            
            file.close();
            
            FileOutputStream outFile =new FileOutputStream(new File(excelPath));
            workbook.write(outFile);
            outFile.close();
            
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * used to add a protein to a already opened result file without closing it
     * @param excelPath
     * @param PMID
     * @param Date
     * @param Protein
     */
    
    public void AddProteinRecordToResults(String PMID,String Date,String Protein,String Label,String orgineLable,String SectionName, int countWords,int countSentence,int countWordInSentence)
    {       
        int  rowindex=  sheet.getLastRowNum()+1;
        try {
            Row row = sheet.createRow(rowindex);
            row.createCell(0).setCellValue(PMID);
            row.createCell(1).setCellValue(Date);
            row.createCell(2).setCellValue(Protein);
            row.createCell(3).setCellValue(Label);
            row.createCell(4).setCellValue(orgineLable);
            row.createCell(5).setCellValue(SectionName);
            row.createCell(6).setCellValue(countWords);
            row.createCell(7).setCellValue(countSentence);
            row.createCell(8).setCellValue(countWordInSentence);
            
            
                   
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
     public void AddProteinRecordToResults_1word(String PMID,String Date,String Protein,String Label,String orgineLable)
    {
        
        int  rowindex=  sheet.getLastRowNum()+1;
        try {
            Row row = sheet.createRow(rowindex);
            row.createCell(0).setCellValue(PMID);
            row.createCell(1).setCellValue(Date);
            row.createCell(2).setCellValue(Protein);
            row.createCell(3).setCellValue(Label);
               row.createCell(4).setCellValue(orgineLable);
                row.createCell(5).setCellValue("1 word suggestion");
            
            
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
      public void AddProteinRecordToResults_2words(String PMID,String Date,String Protein,String Label,String orgineLable)
    {
        
        int  rowindex=  sheet.getLastRowNum()+1;
        try {
            Row row = sheet.createRow(rowindex);
            row.createCell(0).setCellValue(PMID);
            row.createCell(1).setCellValue(Date);
            row.createCell(2).setCellValue(Protein);
            row.createCell(3).setCellValue(Label);
               row.createCell(4).setCellValue(orgineLable);
                 row.createCell(5).setCellValue("2 word suggestion");
            
            
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
       public void AddProteinRecordToResults_3words(String PMID,String Date,String Protein,String Label,String orgineLable)
    {
        
        int  rowindex=  sheet.getLastRowNum()+1;
        try {
            Row row = sheet.createRow(rowindex);
            row.createCell(0).setCellValue(PMID);
            row.createCell(1).setCellValue(Date);
            row.createCell(2).setCellValue(Protein);
            row.createCell(3).setCellValue(Label);
               row.createCell(4).setCellValue(orgineLable);
              row.createCell(5).setCellValue("3 word suggestion");
            
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * close a excel file one times
     * @param excelPath
     */
    public void close(String excelPath) {
        
        try{
            
            file.close();
            FileOutputStream outFile =new FileOutputStream(new File(excelPath));
            workbook.write(outFile);
            outFile.close();
            
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     *
     * @param excelPath
     * @param PMID
     * @param Date
     * @param Protein
     */
    public void RemoveProteinRecordFromResults(String PMID,String Date,String Protein)
    {
        int rowtodelete=findRow(sheet,PMID,Date,Protein);
        if (rowtodelete!=-1)
        {
            removeRow(sheet,rowtodelete);
            /*
            Row row = null;
            row= sheet.getRow(rowtodelete);
            sheet.removeRow(row);//.removeRowBreak(rowtodelete);
            */
        }
    }
    
     public void RemoveProteinRecordFromFile(String excelPath,String PMID,String Date,String Protein)
    {
        try{
            FileInputStream file = new FileInputStream(new File(excelPath));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowtodelete=findRow(sheet,PMID,Date,Protein);
            if (rowtodelete!=-1)
            {
                removeRow(sheet,rowtodelete);
                /*
                Row row = null;
                row= sheet.getRow(rowtodelete);
                sheet.removeRow(row);//.removeRowBreak(rowtodelete);
                */
            }
            
            file.close();
            
            FileOutputStream outFile =new FileOutputStream(new File(excelPath));
            workbook.write(outFile);
            outFile.close();
            
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     *
     * @param excelPath
     * @param Protein
     */
    public void RemoveProteinFromDatabase(String excelPath,String Protein)
    {
        try{
            FileInputStream file = new FileInputStream(new File(excelPath));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            
            XSSFSheet sheet = workbook.getSheetAt(0);
            int repeat=1;
            while (repeat==1)
            {
                int rowtodelete=findRow(sheet,Protein);
                if (rowtodelete!=-1)
                {
                    removeRow(sheet,rowtodelete);
                    /*
                    Row row = null;
                    row= sheet.getRow(rowtodelete);
                    sheet.removeRow(row);//.removeRowBreak(rowtodelete);
                    
                    */
                }
                else {
                    repeat=2 ;
                }
                
                file.close();
                
                FileOutputStream outFile =new FileOutputStream(new File(excelPath));
                workbook.write(outFile);
                outFile.close();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public String RemoveLabelFromDatabase(String excelPath,String Protein)
    {
        String colorToReturn="";
        try{
            FileInputStream file = new FileInputStream(new File(excelPath));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            
            XSSFSheet sheet = workbook.getSheetAt(0);
            int repeat=1;
            while (repeat==1)
            {
                int rowtodelete=findRow(sheet,Protein);
                if (rowtodelete!=-1)
                {
                    colorToReturn=sheet.getRow(rowtodelete).getCell(1).getStringCellValue();
                    removeRow(sheet,rowtodelete);
                    /*
                    Row row = null;
                    row= sheet.getRow(rowtodelete);
                    sheet.removeRow(row);//.removeRowBreak(rowtodelete);
                    
                    */
                }
                else {
                    repeat=2 ;
                }
                
                file.close();
                
                FileOutputStream outFile =new FileOutputStream(new File(excelPath));
                workbook.write(outFile);
                outFile.close();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return colorToReturn;
    }
    
    
    /**
     *
     * @param excelPath
     * @param Protein
     */
    public void RemoveProteinFromResults(String Protein)
    {
        int repeat=1;
        while (repeat==1)
        {
            int rowtodelete=findRowInResult(sheet,Protein);
            if (rowtodelete!=-1)
            {
                removeRow(sheet,rowtodelete);
                /*  Row row = null;
                row= sheet.getRow(rowtodelete);
                sheet.removeRow(row);//.removeRowBreak(rowtodelete);
                */
            }
            else {
                repeat=2 ;
            }
            
            //    file.close();
            
            //   FileOutputStream outFile =new FileOutputStream(new File(excelPath));
            //   workbook.write(outFile);
            //    outFile.close();
        }
    }
    
     public void RemovePMIDFromResults(String excelPath,String PMID)
    {
        int repeat=1;
        while (repeat==1)
        {
            int rowtodelete=findPMIDRowInResult(sheet,PMID);
            if (rowtodelete!=-1)
            {
                removeRow(sheet,rowtodelete);
                /*  Row row = null;
                row= sheet.getRow(rowtodelete);
                sheet.removeRow(row);//.removeRowBreak(rowtodelete);
                */
            }
            else {
                repeat=2 ;
            }
            
            // file.close();
            
            //   FileOutputStream outFile =new FileOutputStream(new File(excelPath));
            //  workbook.write(outFile);
            //  outFile.close();
        }
    }
    
    /**
     *
     * @param sheet
     * @param PMID
     * @param Date
     * @param Protein
     * @return
     */
    
    private  int findRow(XSSFSheet sheet, String PMID,String Date,String Protein) {
        
        for (Row row : sheet) {
            if (row.getCell(0).getStringCellValue().equals(PMID)&&row.getCell(1).getStringCellValue
        ().equals(Date)&&row.getCell(2).getStringCellValue().equals(Protein.toUpperCase()))
            {
                return row.getRowNum();
                
            }
            
            
            
        }
        return -1;
    }
    
    /**
     *
     * @param sheet
     * @param Protein
     * @return
     */
    
    private  int findRow(XSSFSheet sheet, String Protein) {
        for (Row row : sheet) {
            if (row.getCell(0).getStringCellValue().toUpperCase().equals(Protein.toUpperCase()))
            {
                return row.getRowNum();
            }
        }
        return -1;
    }
    
      /**
     *
     * @param sheet
     * @param Protein
     * @return
     */
    
    private  int findPMIDRowInResult(XSSFSheet sheet, String PMID) {
        for (Row row : sheet) {
            if (row.getCell(0).getStringCellValue().equals(PMID))
            {
                return row.getRowNum();
            }
        }
        return -1;
    }
    
    
    /**
     *
     * @param sheet
     * @param Protein
     * @return
     */
    
    private  int findRowInResult(XSSFSheet sheet, String Protein) {
        for (Row row : sheet) {
            if (row.getCell(2).getStringCellValue().equals(Protein))
            {
                return row.getRowNum();
            }
        }
        return -1;
    }
    
    /**
     *
     * @param excelPath
     * @param PMID
     * @param Date
     * @param Protein
     * @return row number if it exist -1 if the row does not exist
     */
    
    public int findRow(String excelPath, String PMID,String Date,String Protein) {
        FileInputStream file;
        try {
            file = new FileInputStream(new File(excelPath));
            
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getCell(0).getStringCellValue().equals(PMID)&&row.getCell
        (1).getStringCellValue().equals(Date)&&row.getCell(2).getStringCellValue().equals(Protein.toUpperCase
        ()))
                {
                    return row.getRowNum();//row exsiste
                    
                }
            }
        }  catch (IOException ex) {
            Logger.getLogger(ExcelReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1; //row do not exsiste
    }
    
    
    
      public int findNotAnAbstractRow(String excelPath, String PMID) {
        FileInputStream file;
        try {
            file = new FileInputStream(new File(excelPath));
            
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getCell(0).getStringCellValue().equals(PMID))
                {
                    return row.getRowNum();//row exsiste
                    
                }
            }
        }  catch (IOException ex) {
            Logger.getLogger(ExcelReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1; //row do not exsiste
    }
    
    /**
     * Remove a row by its index and shift cells
     * @param sheet a Excel sheet
     * @param rowIndex a 0 based index of removing row
     */
    private  void removeRow(XSSFSheet sheet, int rowIndex) {
        int lastRowNum=sheet.getLastRowNum();
        if(rowIndex>=0&&rowIndex<lastRowNum){
            sheet.shiftRows(rowIndex+1,lastRowNum, -1);
        }
        if(rowIndex==lastRowNum){
            XSSFRow removingRow=sheet.getRow(rowIndex);
            if(removingRow!=null){
                sheet.removeRow(removingRow);
            }
        }
    }
    // </editor-fold>
    
}
