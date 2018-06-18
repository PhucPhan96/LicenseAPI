/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.controller;

import java.util.List;
import nfc.model.KeySingle;
import nfc.model.User;
import nfc.service.IKeySingle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
public class KeySingleController {
    @Autowired
    public IKeySingle keysingleDAO;
    
//    Get All Key
    
    @RequestMapping(value = "app/keysingle/getAll", method = RequestMethod.GET)
    public List<KeySingle> fGetListKeySingle() {
        List<KeySingle> lstKeySingle = keysingleDAO.fGetAllKeySingle();
        return lstKeySingle;
    }
    
//    Get All Key Actived
    
    @RequestMapping(value = "app/keysingle/getFilterKeyActive", method = RequestMethod.GET)
    public List<KeySingle> GetFilterKeyActive() {
        List<KeySingle> lstKeySingle = keysingleDAO.GetFilterKeyActive();
        return lstKeySingle;
    }
    
//    Gen Key(500)
    
    @RequestMapping(value = "app/keysingle/genKey", method = RequestMethod.POST)
    public boolean GenerateKeySingle() {
        boolean genKey = keysingleDAO.GenerateKeySingle();
        return genKey;
    }
    
//    Gen Key by Quantity
    
    @RequestMapping(value = "app/keysingle/genKeyByNum", method = RequestMethod.POST)
    public @ResponseBody boolean GenerateKeySingleByNumber(@RequestParam("num") int num) {
        boolean genKey = keysingleDAO.GenerateKeySingleByNumber(num);
        return genKey;
    }
    
//    Check Key

    @RequestMapping(value = "app/keysingle/findByKey", method = RequestMethod.POST  )
    public @ResponseBody boolean FindByKey (@RequestParam("key_single") String key_single,
                                                         @RequestParam("ex_main") String main,
                                                         @RequestParam("ex_cpu") String cpu) {
        boolean ks = keysingleDAO.FindByKey(key_single, main, cpu);
        return ks;
    }
    
//    Search Key
    
    @RequestMapping(value = "app/keysingle/searchKey", method = RequestMethod.POST  )
    public @ResponseBody KeySingle SearchKey (@RequestParam("key_single") String key) {
        KeySingle ks = keysingleDAO.SearchKey(key);
        return ks;
    }
    
//    Reset Key
    
    @RequestMapping(value = "app/keysingle/resetKey", method = RequestMethod.POST  )
    public @ResponseBody boolean ResetKey (@RequestParam("key_single") String key_single) {
        boolean ks = keysingleDAO.ResetKey(key_single);
        return ks;
    }
    
//    Adjourn key
    
    @RequestMapping(value = "app/keysingle/adjourn", method = RequestMethod.POST  )
    public @ResponseBody boolean Adjourn (@RequestParam("key_single") String key_single,
                                          @RequestParam("num") int num) {
        boolean ks = keysingleDAO.Adjourn(key_single, num);
        return ks;
    }
}
