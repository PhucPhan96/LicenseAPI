/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.service;

import java.util.List;
import nfc.model.KeyTrial;

/**
 *
 * @author PhucP
 */
public interface IKeyTrial {
    public boolean CheckTrial(String main, String cpu);
    public List<KeyTrial> fGetAllKeyTrial();
}
