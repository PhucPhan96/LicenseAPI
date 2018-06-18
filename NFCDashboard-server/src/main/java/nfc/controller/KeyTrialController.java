/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.controller;

import java.util.List;
import nfc.model.KeyTrial;
import nfc.service.IKeyTrial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author PhucP
 */
@RestController
public class KeyTrialController {
    @Autowired
    public IKeyTrial keytrialDAO;
    
    @RequestMapping(value = "app/keytrial/getAll", method = RequestMethod.GET)
    public List<KeyTrial> fGetListKeyTrial() {
        List<KeyTrial> lstKeyTrial = keytrialDAO.fGetAllKeyTrial();
        return lstKeyTrial;
    }
    
    @RequestMapping(value = "app/keytrial/checkTrial", method = RequestMethod.POST)
    public @ResponseBody boolean CheckTrial(@RequestParam("main") String main,
                                            @RequestParam("cpu") String cpu) {
        boolean genKey = keytrialDAO.CheckTrial(main, cpu);
        return genKey;
    }
}
