/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EECE.These.Annotation;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author msabra
 */
public class ImageCanvas implements Serializable {
    transient ArrayList <BufferedImage> images;

    public ArrayList<BufferedImage> getImages() {
        return images;
    }

    public void setImages(ArrayList<BufferedImage> images) {
        this.images = images;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        
        out.defaultWriteObject();
        out.writeInt(images.size()); // how many images are serialized?
      
        for (BufferedImage eachImage : images) {
           // BufferedImage image = null; // your image
  ByteArrayOutputStream bufferStream = new ByteArrayOutputStream();
ImageIO.write(eachImage, "JPEG", bufferStream);
  byte[] bufferedBytes = bufferStream.toByteArray();
out.writeObject(bufferedBytes);

// Write bufferedBytes to ObjectOutputStream as Object, OR write bufferedBytes.length + bufferedBytes as raw bytes
           //ImageIO.write(eachImage, "jpg", out); // png is lossless
        }
      
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        final int imageCount = in.readInt();
        images = new ArrayList<BufferedImage>(imageCount);
     //    byte[] bufferedBytes = (byte[]) in.readObject(); // from ObjectInputStream
         
        for (int i=0; i<imageCount; i++) {
           
BufferedImage image = ImageIO.read(new ByteArrayInputStream((byte[]) in.readObject()));
            images.add(image);
        }
    }
}
