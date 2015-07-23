/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EECE.Theses.FilesReaderNew;

import au.com.bytecode.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author msabra
 */
public class TxtReader {
    
    
    
    
    public  ArrayList<HashMap<String, String>> GetPaperAbstracts (String TxtFilePath)
    {
        ArrayList<HashMap<String, String>> abstracts = new ArrayList();
         StringBuilder Abstractsb = new StringBuilder();
         StringBuilder Titlesb = new StringBuilder();
             StringBuilder DocIdsb = new StringBuilder();
                StringBuilder Resultsb = new StringBuilder();
                  StringBuilder Figuresb = new StringBuilder();
                  File folder = new File(TxtFilePath);
                  for (final File fileEntry : folder.listFiles()) {
                      if (fileEntry.isDirectory() && fileEntry.getName().contains("_") && StringUtils.isNumeric(StringUtils.removeEnd(fileEntry.getName(), "_"))) {
                          try{
                              File folder2 = new File(fileEntry.getAbsolutePath());
                              Abstractsb = new StringBuilder();
                              Titlesb = new StringBuilder();
                              DocIdsb = new StringBuilder();
                              Resultsb = new StringBuilder();
                              Figuresb = new StringBuilder();
                              for (final File fileEntry2 : folder2.listFiles()) {
                                  
                                  if (fileEntry2.isDirectory() ) {
                                      FileInputStream Abstract = null;
                                      Abstract = new FileInputStream(fileEntry2.getAbsolutePath()+"\\abstract.txt");
                                      DataInputStream Abstractin = new DataInputStream(Abstract);
                                      BufferedReader Abstractbr = new BufferedReader(new InputStreamReader(Abstractin));
                                      
                                      String Abstractline = Abstractbr.readLine();
                                      
                                      while (Abstractline != null) {
                                          Abstractsb.append(Abstractline);
                                          Abstractsb.append("\n");
                                          Abstractline = Abstractbr.readLine();
                                      }
                                      FileInputStream Title = null;
                                      Title = new FileInputStream(fileEntry2.getAbsolutePath()+"\\title.txt");
                                      DataInputStream Titlein = new DataInputStream(Title);
                                      BufferedReader Titlebr = new BufferedReader(new InputStreamReader(Titlein));
                                      
                                      String Titleline = Titlebr.readLine();
                                      
                                      while (Titleline != null) {
                                          Titlesb.append(Titleline);
                                          Titlesb.append("\n");
                                          Titleline = Titlebr.readLine();
                                      }
                                      
                                      FileInputStream DocId = null;
                                      DocId = new FileInputStream(fileEntry2.getAbsolutePath()+"\\docid.txt");
                                      DataInputStream DocIdin = new DataInputStream(DocId);
                                      BufferedReader DocIdbr = new BufferedReader(new InputStreamReader(DocIdin));
                                      
                                      String DocIdline = DocIdbr.readLine();
                                      
                                      while (DocIdline != null) {
                                          DocIdsb.append(DocIdline);
                                          DocIdsb.append("\n");
                                          DocIdline = DocIdbr.readLine();
                                      }
                                      
                                      FileInputStream Result = null;
                                      Result = new FileInputStream(fileEntry2.getAbsolutePath()+"\\AllResultfile.txt");
                                      DataInputStream Resultin = new DataInputStream(Result);
                                      BufferedReader Resultbr = new BufferedReader(new InputStreamReader(Resultin));
                                      
                                      String Resultline = Resultbr.readLine();
                                      
                                      while (Resultline != null) {
                                          Resultsb.append(Resultline);
                                          Resultsb.append("\n");
                                          Resultline = Resultbr.readLine();
                                      }
                                      
                                  } else {
                                      if (fileEntry2.getName().contains("_p.txt"))
                                      {
                                          FileInputStream Figure = null;
                                          Figure = new FileInputStream(fileEntry2.getAbsolutePath());
                                          DataInputStream Figurein = new DataInputStream(Figure);
                                          BufferedReader Figurebr = new BufferedReader(new InputStreamReader(Figurein));
                                          
                                          String Figureline = Figurebr.readLine();
                                          
                                          while (Figureline != null) {
                                              Figuresb.append(Figureline);
                                              Figuresb.append("\n");
                                              Figureline = Figurebr.readLine();
                                          }
                                      }
                                  }
                                  
                                  
                              }
                          } catch (FileNotFoundException ex) {
                              Logger.getLogger(TxtReader.class.getName()).log(Level.SEVERE, null, ex);
                          } catch (IOException ex) {
                              Logger.getLogger(TxtReader.class.getName()).log(Level.SEVERE, null, ex);
                          }
                          
                      }
                      HashMap recorde = new HashMap();
                      
                      recorde.put("Abstract",Abstractsb.toString());
                      recorde.put("PMID",DocIdsb.toString());
                      recorde.put("DATE", "");
                      recorde.put("Result", Resultsb.toString());
                      recorde.put("Figures", Figuresb.toString());
                      recorde.put("Title",Titlesb.toString());
                      abstracts.add(recorde);
        }
         System.out.println("creating serialized");
        FileOutputStream fileOut;
        ObjectOutputStream out = null;
        try {
            if (abstracts.size()>1)
            {
            fileOut = new FileOutputStream(TxtFilePath+"\\SerializedObject.ser");
            out = new ObjectOutputStream(fileOut);
            System.out.println("creating a serialization");
            out.writeObject(abstracts);
            System.out.println("finish creating a serialization");
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in "+TxtFilePath+"\\SerializedObject.ser");
            }
        }    catch (IOException ex) {
            Logger.getLogger(ExcelReader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(ExcelReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return abstracts;
    }
    
    
    /**
     * parse a txt file and get the abstract and title from it
     * @param TxtFilePath
     * @return 
     */
    public  ArrayList<HashMap<String, String>> GetTxtAbstracts (String TxtFilePath)
    {
           ArrayList<HashMap<String, String>> abstracts = new ArrayList();
        try {
         
            FileInputStream fstream = null;
            fstream = new FileInputStream(TxtFilePath);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine=br.readLine();
               
              while (strLine != null)   {
                  
                   if (strLine.equals(""))
                   {
                       int i =1;//line number
                     HashMap recorde = new HashMap(4);
                        strLine = br.readLine();
                         while (!strLine.equals(""))
                         {
                 
             //       System.out.println (strLine);
                    if (strLine.startsWith("http://www"))
                    {
                          recorde.put("PMID",strLine);  
                           recorde.put("DATE", "");
                             strLine = br.readLine();
                    }
              /*     else  if (strLine.startsWith("AFFILIATIONS: "))
                    {
                        String affili="";
                        while (!strLine.startsWith("ABSTRACT: "))
                        {
                        affili=affili+strLine;
                         strLine = br.readLine();
                                }
                          recorde.put("Title",affili);  
                    }*/
                    else if (i==2)
                    {
                      recorde.put("Title",strLine);  
                         strLine = br.readLine();
                         i++;
                    }
                   else   if (strLine.startsWith("ABSTRACT: "))
                    {
                        String Avstract="";
                      
                        while ( !strLine.equals(""))
                        {
                        Avstract=Avstract+strLine;
                         strLine = br.readLine();
                         if(strLine==null)
                         {
                         break;
                         }
                                }
                          
                          recorde.put("Abstract",Avstract);  
                               abstracts.add(recorde);
                                if(strLine==null)
                         {
                         break;
                         }
                    
                    }
                   else 
                   {
                       strLine = br.readLine();
                       i++;
                   }
                   }
                   }
                   else{
                    strLine = br.readLine();
                   }
              }
         
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TxtReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TxtReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    
       return abstracts;
    }
    
    /**
     * parse a csv file and get the abstract and title from it
     * @param TxtFilePath
     * @return 
     */
     public  ArrayList<HashMap<String, String>> GetTxtAbstractsFromCSV (String CSVFilePath)
    {
           ArrayList<HashMap<String, String>> abstracts = new ArrayList();
   // String csvFile = TxtFilePath;
    try {
     CSVReader reader = new CSVReader(new FileReader(CSVFilePath));
      String [] nextLine;
      //int lineNumber = 0;
      while ((nextLine = reader.readNext()) != null) {
        HashMap recorde = new HashMap(4);
        recorde.put("PMID",nextLine[18]);
  recorde.put("DATE", nextLine[2]);
  recorde.put("Title",nextLine[1]);
  recorde.put("Abstract",nextLine[14]);  
       //   lineNumber++;
      // System.out.println("Line # " + lineNumber);

        // nextLine[] is an array of values from the line
      //  System.out.println(nextLine[4] + "etc...");
        abstracts.add(recorde);	
      }
	
		
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} 
                
	
 
	return abstracts;
  }

    
}
