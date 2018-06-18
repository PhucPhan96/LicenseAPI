/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.service;

import java.util.List;
import nfc.model.KeySingle;

/**
 *
 * @author PhucP
 */
public interface IKeySingle {
    public List<KeySingle> fGetAllKeySingle();
    public boolean GenerateKeySingle();
//    public boolean UpdateKeySingle(String key_single, String main, String cpu);
    public boolean FindByKey(String key_single, String main, String cpu);
    public List<KeySingle> GetFilterKeyActive();
    public KeySingle SearchKey(String key);
    public boolean GenerateKeySingleByNumber(int num);
    public boolean ResetKey(String key);
    public boolean Adjourn(String key, int num);
}
