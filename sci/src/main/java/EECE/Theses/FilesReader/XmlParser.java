/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EECE.Theses.FilesReader;

import EECE.These.Annotation.AutoExtractedPapers;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author msabra
 */
public class XmlParser {
    
      // <editor-fold defaultstate="collapsed" desc="Methodes">
    
    /**
     *
     * @param XmlFilePath the path of the XML file
     * @return an array list of hasp map in each hasp map we can find the title, abstract, PMID, date.
     * it create a serialized object of this array list of hasp map in the same file directory
     */
    
    public ArrayList <HashMap<String, String>> GetXmlAbstracts (String XmlFilePath)
    {
        ArrayList<HashMap<String, String>> abstracts = new ArrayList();
        File abstractsfile = new File(XmlFilePath);
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        try {
            //   builder = factory.newDocumentBuilder();
            //  factory.setValidating(false);
            factory.setValidating(false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            DocumentBuilder dBuilder = factory.newDocumentBuilder();
          
            Document document = dBuilder.parse(abstractsfile);
            document.getDocumentElement().normalize();
            NodeList nodes = document.getElementsByTagName("PubmedArticle");
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    HashMap recorde = new HashMap(4);
                    Element element = (Element) node;
                    recorde.put("PMID", getValue("PMID", element));
                    recorde.put("DATE", getValue("DateCompleted", element));
                    recorde.put("Abstract", getValue("Abstract", element));
                    recorde.put("Title", getValue("ArticleTitle", element));
                    //recorde.put("Date", getValue("DateCompleted", element));
                    abstracts.add(recorde);
                }
                
            }
            
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XmlParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(XmlParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XmlParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("finish parsing xml");
        FileOutputStream fileOut;
        ObjectOutputStream out = null;
        try {
            if (abstracts.size()>1)
            {
            fileOut = new FileOutputStream(XmlFilePath+".ser");
            out = new ObjectOutputStream(fileOut);
            System.out.println("creating a serialization");
            out.writeObject(abstracts);
            System.out.println("finish creating a serialization");
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in "+XmlFilePath+".ser");
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
     *
     * @param tag
     * @param element
     * @return
     */
    
    private static String getValue(String tag, Element element) {
        try {
            if (tag.equals("DateCompleted"))
            {
                
                NodeList nodes  =  element.getElementsByTagName("DateCompleted");
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node node = nodes.item(i);
                    
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element2 = (Element) node;
                        return getValue("Day", element2)+"/"+getValue("Month", element2)+"/"+getValue("Year", element2);
                    }
                    
                }
                return "";
            }
            else if (tag.equals("Abstract"))
            {
                String abstracts="";
                abstracts= element.getElementsByTagName(tag).item(0).getTextContent();//getChildNodes();
                return abstracts; //
            }
            else {
                NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
                
                Node node = (Node) nodes.item(0);
                return node.getNodeValue();
            }
        }
        catch (Exception ex)
        {
            return "";
        }
    }
    
    /**
     *
     * @param SerializedFilePath path of the serialized object file
     * @return an array list of hasp map in each hasp map we can find the title, abstract, PMID, date.
     */
    
    public AutoExtractedPapers GetXmlAbstractsserialized (String SerializedFilePath)
    {
        ArrayList<HashMap<String, String>> abstracts = new ArrayList();
        AutoExtractedPapers autoExtractedPapers= new AutoExtractedPapers();
        try
        {
            FileInputStream fileIn = new FileInputStream(SerializedFilePath);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            autoExtractedPapers = ( AutoExtractedPapers) in.readObject();
            in.close();
            fileIn.close();
        }catch(IOException | ClassNotFoundException | ClassCastException i)
        {
             try
        {
            FileInputStream fileIn = new FileInputStream(SerializedFilePath);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            abstracts = (  ArrayList<HashMap<String, String>>) in.readObject();
            in.close();
            fileIn.close();
            autoExtractedPapers.setAbstracts(abstracts);
            autoExtractedPapers.setAllFiguresAbstracts(null);
            }
             catch(IOException | ClassNotFoundException e)
        {
        System.out.println(e);
        }
            
        }
        
        
        return autoExtractedPapers;
    }
    
    
    // </editor-fold>

}
