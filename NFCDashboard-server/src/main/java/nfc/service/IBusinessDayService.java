/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.service;

import java.util.List;
import nfc.model.BusinessDay;

/**
 *
 * @author Admin
 */
public interface IBusinessDayService {
    List<BusinessDay> getListBusinessDayBySupId(int suppl_id );
    boolean insertBusinessDay(BusinessDay businessDay);
    BusinessDay getBusinessDaybyId(int businessDays_id);
    boolean updateBusinessDay(BusinessDay businessDay);
    boolean deleteBusinessDay(int businessDays_id);
}
