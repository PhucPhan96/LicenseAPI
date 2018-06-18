/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import nfc.model.SearchQA;
import nfc.model.Text;
import nfc.model.ViewModel.GridView;
import nfc.service.ITextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Admin
 */
@RestController
public class TextManagementController {

    @Autowired
    private ITextService textDAO;

    @RequestMapping(value = "texts/getListText", method = RequestMethod.GET)
    public List<Text> getListText() {
        List<Text> texts = textDAO.getListText();
        return texts;
    }
    
    @RequestMapping(value = "texts/grid", method = RequestMethod.POST)
    public GridView getListTextGrid(@RequestBody GridView gridData) {
        return textDAO.getListTextGrid(gridData);
    }
    
    

    @RequestMapping(value = "texts/getListTextByType/{text_type}", method = RequestMethod.GET)
    public List<Text> getListTextByType(@PathVariable("text_type") String text_type) {   
        List<Text> texts = textDAO.getListTextByType(text_type);
        return texts;
    }

    @RequestMapping(value = "texts/getTextbyId/{text_id}", method = RequestMethod.GET)
    public Text getTextbyId(@PathVariable("text_id") int text_id) {
        System.out.println("Vao getTextbyId ");
        Text texts = textDAO.getTextbyId(text_id);
        return texts;
    }

    @RequestMapping(value = "texts/edit", method = RequestMethod.PUT)
    public @ResponseBody String editText(@RequestBody Text text) {
        String data = textDAO.updateText(text) + "";
         System.out.println("Value updateText:");
        return "{\"result\":\"" + data + "\"}";
        
    }
    @RequestMapping(value = "texts/insert", method = RequestMethod.POST)
    public @ResponseBody String insertText(@RequestBody Text text) {
        System.out.println("Value insertText:");
        String data = textDAO.insertText(text) + "";         
        return "{\"result\":\"" + data + "\"}";
        
    } 
    @RequestMapping(value = "app/texts/insert", method = RequestMethod.POST)
    public @ResponseBody String insertTextApp(@RequestBody Text text) {
        System.out.println("Value insertText:");
        String data = textDAO.insertText(text) + "";         
        return "{\"result\":\"" + data + "\"}";
        
    } 
    @RequestMapping(value="texts/delete/{text_id}", method=RequestMethod.DELETE)
	public @ResponseBody String deleteGroup(@PathVariable("text_id") int text_id){
		String data = textDAO.deleteText(text_id)+"";
		return "{\"result\":\"" + data + "\"}";
	}
    @RequestMapping(value = "app/texts/getListTextByTypeApp/{text_type}", method = RequestMethod.GET)
    public List<Text> getListTextByTypeApp(@PathVariable("text_type") String text_type) {   
        List<Text> texts = textDAO.getListTextByType(text_type);
        return texts;
    }
    @RequestMapping(value = "app/texts/getListTextbyTextInput", method = RequestMethod.POST)
    public List<Text> getListTextbyTextInput(@RequestBody SearchQA search)  throws UnsupportedEncodingException {   
        List<Text> texts = textDAO.getListTextbyTextInput(search.getTextInput(),search.getText_type());
        return texts;
    }
    @RequestMapping(value = "app/texts/getListNews", method = RequestMethod.GET)
    public List<Text> getListNews() {
        List<Text> texts = textDAO.getListNews();
        return texts;
    }
    @RequestMapping(value = "app/texts/getNewsbyId/{text_id}", method = RequestMethod.GET)
    public Text getNewsbyId(@PathVariable("text_id") int text_id) {
        System.out.println("Vao getTextbyId ");
        Text texts = textDAO.getTextbyId(text_id);
        return texts;
    }
    @RequestMapping(value = "app/texts/getAllLaw/{text_type}", method = RequestMethod.GET)
    public List<Text> getAllLaw(@PathVariable("text_type") String text_type) {   
        List<Text> texts = textDAO.getListTextByType(text_type);
        System.out.println("Vao getAllLaw "+textDAO.getListTextByType(text_type));
        return texts;
    }
   }
