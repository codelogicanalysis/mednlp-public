/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EECE.These.Annotation;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 *
 * @author msabra
 */
public class AutoExtractedPapers implements Serializable {
    private ArrayList< ImageCanvas> allFiguresAbstracts;
    private  ArrayList<HashMap<String, String>> abstracts;

    public void setAllFiguresAbstracts(ArrayList<ImageCanvas> allFiguresAbstracts) {
        this.allFiguresAbstracts = allFiguresAbstracts;
    }

    public void setAbstracts(ArrayList<HashMap<String, String>> abstracts) {
        this.abstracts = abstracts;
    }

    public ArrayList< ImageCanvas> getAllFiguresAbstracts() {
        return allFiguresAbstracts;
    }

    public ArrayList<HashMap<String, String>> getAbstracts() {
        return abstracts;
    }
    
//     private void writeObject(ObjectOutputStream out2) throws IOException {
//        out2.defaultWriteObject();
//        out2.writeInt(allFiguresAbstracts.size()); // how many images are serialized?
//        for (ImageCanvas eachImageMap : allFiguresAbstracts) {
//            // for (BufferedImage eachImage : eachImageMap.get("allFigures")) {
//            out2.writeObject(eachImageMap); // png is lossless
//             }
//        
//    }
//
//    private void readObject(ObjectInputStream in2) throws IOException, ClassNotFoundException {
//        in2.defaultReadObject();
//        final int hashmapcount = in2.readInt();
//        allFiguresAbstracts = new  ArrayList<ImageCanvas>(hashmapcount);
//        for (int i=0; i<hashmapcount; i++) {
//         
//          //  HashMap<String, ArrayList<BufferedImage>> temp=(HashMap<String, ArrayList<BufferedImage>>)in.readObject();
//           allFiguresAbstracts.add(i, (ImageCanvas)in2.readObject());
////             for (int j=0; j<allFiguresAbstracts.get(i).size(); j++) {
////            allFiguresAbstracts.get(i).put(null, null);
////             } 
////             ArrayList<BufferedImage> temp2=temp.get("allFigures");
////          int imageCount = temp2.size();
////         ArrayList<BufferedImage> temp3 = new ArrayList<BufferedImage>(imageCount);
////               
////                
////             
////          //  allFiguresAbstracts.add(i, (HashMap<String, ArrayList<BufferedImage>>)in.readObject());
////             for (int j=0; j<imageCount; j++) {
////                  temp3.add(ImageIO.read(temp2.get(j)));
////                  
////                  
////              allFiguresAbstracts.add(i, temp);     images.add(ImageIO.read(in));
////                 ArrayList<BufferedImage> temp2= new  ArrayList<BufferedImage>();
////                 temp2
////            allFiguresAbstracts.get(i).put(null, null);
//             }
//        
//    }
    
    
}
