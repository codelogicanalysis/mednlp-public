/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package annotationtest;

import EECE.These.Annotation.LoadingFrame;
import EECE.These.Annotation.SAMNAFrame;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author msabra
 */

public class Test2 {
    /**
     * this is the main class that open a ZSA frame
     * @param args 
     */
     public static void main(String[] args) {
         try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }
         String filepath ="";
       //  JLabel label = new JLabel("Choose your workspace");
        //  UIManager.put("FileChooser.openDialogTitleText", "Choose your workspace");
          final JFileChooser fc = new JFileChooser();
          fc.setDialogTitle("Choose your workspace");
          fc.setCurrentDirectory(new java.io.File("."));
           fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
             filepath = fc.getSelectedFile().getAbsolutePath();
            //   abstracts=
        }
         LoadingFrame ZSAframe=new LoadingFrame("SAMNA",filepath);
       //  ZSAframe.RunLoadingFrame(filepath);
     }
}
