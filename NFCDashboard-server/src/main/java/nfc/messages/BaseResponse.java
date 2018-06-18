/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.messages;

/**
 *
 * @author Admin
 */
public class BaseResponse {
    public static final String OK = "OK";
    public static final String FAILED = "FAILED";
    public static final String NOT_REG = "NOT_REG";
    public static final String ILLEGAL_ARGU = "ILL_ARGU";
    public static final String WRONG_AMOUNT = "WRONG_AMOUNT";
    public static final String PROC_FAILED = "PROC_FAILED";
    public static final int RES_OK = 200;
    public static final int RES_FAILED = 401;
    public static final int RES_NOT_REG = 301;
    public static final int RES_ILLEGAL_ARGUMENT = 302;
    public static final int RES_WRONG_AMOUNT = 303;

    public String resultCode = "";

    public String errorMsg = "";
    
    public Object response_data;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Object getResponse_data() {
        return response_data;
    }

    public void setResponse_data(Object response_data) {
        this.response_data = response_data;
    }
    
    
}
