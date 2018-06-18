/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.controller;

import java.util.List;
import nfc.model.BusinessDay;
import nfc.model.Text;
import nfc.service.IBusinessDayService;
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
public class BusinessDayManagementController {
    @Autowired
    private IBusinessDayService businessDayDAO;
    
    @RequestMapping(value = "businessDays/getListBusinessDayBySupId/{suppl_id}", method = RequestMethod.GET)
    public List<BusinessDay> getListBusinessDayBySupId(@PathVariable("suppl_id") int suppl_id) {
        System.out.println("Value getListBusinessDayBySupId:"+ businessDayDAO.getListBusinessDayBySupId(suppl_id));
        List<BusinessDay> businessDays = businessDayDAO.getListBusinessDayBySupId(suppl_id);
        return businessDays;
    }
    
    @RequestMapping(value = "businessDays/insertBusinessDay", method = RequestMethod.POST)
    public @ResponseBody String insertBusinessDay(@RequestBody BusinessDay businessDay) {
        System.out.println("Value insertBusinessDay:");
        String data = businessDayDAO.insertBusinessDay(businessDay) + "";         
        return "{\"result\":\"" + data + "\"}";
        
    }
    
    @RequestMapping(value = "businessDays/getBusinessDaybyId/{businessDays_id}", method = RequestMethod.GET)
    public BusinessDay getBusinessDaybyId(@PathVariable("businessDays_id") int businessDays_id) {
        System.out.println("Vao getBusinessDaybyId ");
        BusinessDay businessDay = businessDayDAO.getBusinessDaybyId(businessDays_id);
        return businessDay;
    }
    @RequestMapping(value = "businessDays/updateBusinessDay", method = RequestMethod.PUT)
    public @ResponseBody String updateBusinessDay(@RequestBody BusinessDay businessDay) {
        String data = businessDayDAO.updateBusinessDay(businessDay) + "";
         System.out.println("Value updateBusinessDay:");
        return "{\"result\":\"" + data + "\"}";
        
    }
    @RequestMapping(value="businessDays/deleteBusinessDay/{businessDays_id}", method=RequestMethod.DELETE)
	public @ResponseBody String deleteBusinessDay(@PathVariable("businessDays_id") int businessDays_id){
		String data = businessDayDAO.deleteBusinessDay(businessDays_id)+"";
                 System.out.println("Value deleteBusinessDay:");
		return "{\"result\":\"" + data + "\"}";
	}
   
    
}
