package EECE.These.Annotation;

/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

/**
 *
 * @author msabra
 */
public class ACTIONS {
    /* enum ACTIONS {
    ADD(1,"Addition to a table",""),ADDL(2,"Addition of a new label",""), REMOVELOCALLY(3,"removing protein locally",""), REMOVE(4,"removing from the table","");
    int value;
    String fullName;
    String releatedWord;
    ACTIONS(int value,String fullName,String releatedWord) {
    this.value = value;
    this.fullName=fullName;
    this.releatedWord=releatedWord;
    }
    
    };   */
    
    int value;
    String fullName;
    String releatedWord;
    String label;
    int labelindex;
    public  ACTIONS(int value,String fullName,String releatedWord,String label, int labelindex) {
        this.value = value;
        this.fullName=fullName;
        this.releatedWord=releatedWord;
        this.label=label;
        this.labelindex=labelindex;
    }
}
