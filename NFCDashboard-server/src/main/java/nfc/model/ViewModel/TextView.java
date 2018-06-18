/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.model.ViewModel;

import java.util.ArrayList;
import java.util.List;
import nfc.model.Text;

/**
 *
 * @author Admin
 */
public class TextView {
    private Text text = new Text();
    private List<TextFileView> images = new ArrayList<>();
    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public List<TextFileView> getImages() {
        return images;
    }

    public void setImages(List<TextFileView> images) {
        this.images = images;
    }

    

}
