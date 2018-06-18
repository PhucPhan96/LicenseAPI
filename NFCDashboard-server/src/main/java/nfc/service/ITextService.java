/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.service;

import java.util.List;
import nfc.model.Text;
import nfc.model.ViewModel.GridView;

/**
 *
 * @author Admin
 */
public interface ITextService {
    List<Text> getAllText();
    List<Text> getListText();
    List<Text> getListTextByType(String text_type);
    GridView getListTextGrid(GridView gridData);
    Text getTextbyId(int text_id);
    boolean updateText(Text text);
    boolean insertText(Text text);
    boolean deleteText(int text_id);
    List<Text> getListTextbyTextInput(String textInput, String text_type);
    List<Text> getListNews();
}
