/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl.payment;

import java.util.Map;
import java.util.logging.Logger;
import kr.co.danal.jsinbi.*;
import java.net.*;
import java.util.*;
import java.text.*; 
import java.io.*;
import javax.crypto.*;
import javax.crypto.spec.*; 
import java.security.*;
import javax.servlet.http.HttpServletRequest;
import kr.co.danal.rnd.TeleditClient;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Admin
 */
public class DanalFunction {
    private static final Logger log =  Logger.getLogger(DanalFunction.class.toString());
    
    private static class SingletonHelper{
        private static final DanalFunction INSTANCE = new DanalFunction(); 
    }
    
    public static DanalFunction getInstance(){
        return SingletonHelper.INSTANCE;
    }
    
    
    
    //common config
    private static final int DN_CONNECT_TIMEOUT = 5000;
    private static final int DN_TIMEOUT = 30000; //SO_Timeout setting
    private static final String ERC_NETWORK_ERROR = "-1";
    private static final String ERM_NETWORK = "Network Error";

    //wire transfer config
    private static final String DN_TX_URL_WIRETRANSFER = "https://tx-wiretransfer.danalpay.com/bank/";
    private String CPID = "A010002002"; //영업담당자에게 문의
    public static String CRYPTOKEY_WIRETRANSFER = "20ad459ab1ad2f6e541929d50d24765abb05850094a9629041bebb726814625d"; //영업담당자에게 문의
    public static String IV_WIRETRANSFER = "31363032313523404542616e6b456e63"; // 고정값. 수정하지 마시오.
    public String CHARSET = "EUC-KR";
    
    //Virtual account config
    private static final String DN_TX_URL_VIRTUAL_ACCOUNT = "https://tx-vaccount.danalpay.com/vaccount/";
    public static String CRYPTOKEY_VIRTUAL_ACCOUNT = "5e846c64f2db12266e6b658a8e5b5b42cc225419b3ee1fca88acbb181ddfdb52"; //영업담당자에게 문의
    public static String IV_VIRTUAL_ACCOUNT = "45b913a44d61353d20402a2518de592a"; //수정하지 마세요.
    private String CP_ACCOUNT_HOLDER_NAME = "계좌주명"; //CP계좌에 적힌 계좌주명

    //tele config
    final String Info = "";
    public final static String ID  = "A010002002 ";
    public final static String PWD = "bbbbb";
    public final static String AMOUNT = "301";
        
    //function use from wire transfer
    public Map CallDanalBank(Map REQ_DATA, boolean Debug) {
            System.err.println("Req " + REQ_DATA.toString());
            String REQ_STR = toEncrypt(data2str(REQ_DATA), IV_WIRETRANSFER, CRYPTOKEY_WIRETRANSFER);
            REQ_STR = "CPID=" + CPID + "&DATA=" + REQ_STR;
            System.err.println("Req " + REQ_STR);
            HttpClient hc = new HttpClient();
            hc.setConnectionTimeout(DN_CONNECT_TIMEOUT);
            hc.setTimeout(DN_TIMEOUT);

            int Result = hc.retrieve("POST", DN_TX_URL_WIRETRANSFER, REQ_STR, CHARSET, CHARSET);

            String RES_STR = "";
            if (Result == HttpClient.EOK && hc.getResponseCode() == 200) {
                    RES_STR = hc.getResponseBody();
            } else {
                    RES_STR = "RETURNCODE=" + ERC_NETWORK_ERROR + "&RETURNMSG="
                                    + ERM_NETWORK + "( " + Result + "/" + hc.getResponseCode()
                                    + " )";
            }

            if (Debug) {
                    System.out.println("ReqData[" + data2str(REQ_DATA) + "]");
                    System.out.println("Req[" + REQ_STR + "]");
                    System.out.println("Ret[" + Result + "/" + hc.getResponseCode() + "]");
                    System.out.println("Res[" + RES_STR + "]");
            }

            Map resMap = str2data(RES_STR);
            RES_STR = toDecrypt((String) resMap.get("DATA"), IV_WIRETRANSFER, CRYPTOKEY_WIRETRANSFER);
            return str2data(RES_STR);
    }
    
    //function use from virtual account
    public Map CallVAccount(Map REQ_DATA, boolean Debug) {
        String REQ_STR = toEncrypt(data2str(REQ_DATA),IV_VIRTUAL_ACCOUNT, CRYPTOKEY_VIRTUAL_ACCOUNT);
        REQ_STR = "CPID=" + CPID + "&DATA=" + REQ_STR;

        HttpClient hc = new HttpClient();
        hc.setConnectionTimeout(DN_CONNECT_TIMEOUT);
        hc.setTimeout(DN_TIMEOUT);

        int Result = hc.retrieve("POST", DN_TX_URL_VIRTUAL_ACCOUNT, REQ_STR, CHARSET, CHARSET);

        String RES_STR = "";
        if (Result == HttpClient.EOK && hc.getResponseCode() == 200) {
                RES_STR = hc.getResponseBody();
        } else {
                RES_STR = "RETURNCODE=" + ERC_NETWORK_ERROR + "&RETURNMSG="
                                + ERM_NETWORK + "( " + Result + "/" + hc.getResponseCode()
                                + " )";
        }

        if (Debug) {
                System.out.println("ReqDATA[" + data2str(REQ_DATA) + "]");
                System.out.println("Req[" + REQ_STR + "]");
                System.out.println("Ret[" + Result + "/" + hc.getResponseCode()	+ "]");
                System.out.println("Res[" + RES_STR + "]");
        }

        Map resMap = str2data(RES_STR);
        if( resMap.containsKey("DATA") ){
                resMap = str2data( toDecrypt((String) resMap.get("DATA"),IV_VIRTUAL_ACCOUNT, CRYPTOKEY_VIRTUAL_ACCOUNT) );
        }
        return resMap;
    }
    
    
    public Map str2data(String str) {
            Map map = new HashMap();
            String[] st = str.split("&");

            for (int i = 0; i < st.length; i++) {
                    int index = st[i].indexOf('=');
                    if (index > 0)
                            map.put(st[i].substring(0, index), urlDecode(st[i].substring(index + 1)));
            }

            return map;
    }

    public String data2str(Map data) {
            Iterator i = data.keySet().iterator();
            StringBuffer sb = new StringBuffer();
            while (i.hasNext()) {
                    Object key = i.next();
                    Object value = data.get(key);

                    sb.append(key.toString());
                    sb.append('=');
                    sb.append(urlEncode(value.toString()));
                    sb.append('&');
            }

            if (sb.length() > 0) {
                    return sb.substring(0, sb.length() - 1);
            } else {
                    return "";
            }
    }

    public Map getReqMap(HttpServletRequest req) {
            Map reqMap = req.getParameterMap();
            Map retMap = new HashMap();

            Set entSet = reqMap.entrySet();
            Iterator it = entSet.iterator();
            while (it.hasNext()) {
                    Map.Entry et = (Map.Entry) it.next();
                    Object v = et.getValue();
                    if (v instanceof String) {
                            String tt = (String) v;
                            retMap.put(et.getKey(), tt);
                    } else if (v instanceof String[]) {
                            String[] tt = (String[]) v;
                            retMap.put(et.getKey(), tt[0]);
                    } else {
                            retMap.put(et.getKey(), v.toString());
                    }
            }
            return retMap;
    }

    /*
     *  urlEncode
     */
    public String urlEncode(Object obj) {
            if (obj == null) {
                    return null;
            }

            try {
                    return URLEncoder.encode(obj.toString(), "EUC-KR");
            } catch (Exception e) {
                    e.printStackTrace();
                    return obj.toString();
            }
    }

    /*
     *  urlDecode
     */
    public String urlDecode(Object obj) {
            if (obj == null) {
                    return null;
            }

            try {
                    return URLDecoder.decode(obj.toString(), "EUC-KR");
            } catch (Exception e) {
                    return obj.toString();
            }
    }

    public String toEncrypt(String originalMsg, String IV, String CRYPTOKEY) {
            System.out.println("OrgMsg: " + originalMsg);
            String AESMode = "AES/CBC/PKCS5Padding";
            String SecetKeyAlgorithmString = "AES";

            IvParameterSpec ivspec = new IvParameterSpec(hexToByteArray(IV));
            SecretKey keySpec = new SecretKeySpec(hexToByteArray(CRYPTOKEY),SecetKeyAlgorithmString);
            try {
                    Cipher cipher = Cipher.getInstance(AESMode);
                    cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivspec);
                    byte[] encrypted = cipher.doFinal(originalMsg.getBytes());
                    return new String(Base64.encodeBase64(encrypted));
            } catch (Exception e) {
                    e.printStackTrace();
                    return "";
            }
    }

    public String toDecrypt(String originalMsg, String IV, String CRYPTOKEY) {
            System.out.println("To be decrypted msg: " + originalMsg);
            String AESMode = "AES/CBC/PKCS5Padding";
            String SecetKeyAlgorithmString = "AES";

            IvParameterSpec ivspec = new IvParameterSpec(hexToByteArray(IV));
            SecretKey keySpec = new SecretKeySpec(hexToByteArray(CRYPTOKEY),SecetKeyAlgorithmString);
            try {
                    Cipher cipher = Cipher.getInstance(AESMode);
                    cipher.init(Cipher.DECRYPT_MODE, keySpec, ivspec);
                    byte[] decrypted = cipher.doFinal(Base64.decodeBase64(originalMsg.getBytes()));
                    String retValue = new String(decrypted);
                    System.out.println(retValue);
                    return retValue;
            } catch (Exception e) {
                    e.printStackTrace();
                    return "";
            }
    }

    private byte[] hexToByteArray(String hex) {
            if (hex == null || hex.length() == 0) {
                    return null;
            }

            byte[] ba = new byte[hex.length() / 2];
            for (int i = 0; i < ba.length; i++) {
                    ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
            }
            return ba;
    }
    
    
    
    
    //tele pay
    public Map CallTeledit(Map TransR) {

		String Input = MakeParam( TransR );
		String Output = new TeleditClient( Info ).SClient( Input );

		return Parsor( Output,"\n" );
	}

	public Map CallTeleditCancel(Map TransR) {

		String Input = MakeParam( TransR );
		String Output = new TeleditClient( Info ).cancel( Input );

		return Parsor( Output,"\n" );
	}

	public Map Parsor(String output,String delimiter) {

		String[] step1 = null;
		String[] step2 = null;

		int stepc;

		Map retval = new HashMap();

		step1 = output.split(delimiter);

		for( stepc=0;stepc<step1.length;stepc++ )
		{
			step2 = step1[stepc].split( "=" );

			if( step2.length < 2 ) continue;

			retval.put( step2[0].trim(),step2[1].trim() );
		}

		return retval;
	}

	public String MakeFormInput(Map map,String[] arr) {

		StringBuffer ret = new StringBuffer();

		if( arr!=null )
		{
			for( int i=0;i<arr.length;i++ )
			{
				if( map.containsKey(arr[i]) )
				{
					map.remove( arr[i] );
				}
			}
		}

		Iterator i = map.keySet().iterator();

		while( i.hasNext() )
		{
			String key = (String)i.next();
			String value = (String)map.get(key);
			
			ret.append( "<input type=\"hidden\" name=\"" );
			ret.append( key );
			ret.append( "\" value=\"" );
			ret.append( value );
			ret.append( "\">" );
			ret.append( "\n" );
		}

		return ret.toString();
	}

	public String MakeFormInputHTTP(Map HTTPVAR,String arr) {

		StringBuffer ret = new StringBuffer();
		String key = "";
		String[] value = null;

		Iterator i = HTTPVAR.keySet().iterator();

		while( i.hasNext() )
		{
			key = (String)i.next();

			if( key.equals(arr) )
			{
				continue;
			}

			value = (String[])HTTPVAR.get(key);
			
			for( int j=0;j<value.length;j++ )
			{
				ret.append( "<input type=\"hidden\" name=\"" );
				ret.append( key );
				ret.append( "\" value=\"" );
				ret.append( toEuckr(value[j]) );
				ret.append( "\">" );
				ret.append( "\n" );
			}
		}

		return ret.toString();
	}

	public void MakeAddtionalInput(Map Trans,Map HTTPVAR,String[] Names) {

		for (int i=0;i<Names.length;i++ )
		{
			if( HTTPVAR.containsKey(Names[i]) )
			{
				String[] a = (String[])HTTPVAR.get( Names[i] );
				Trans.put( Names[i],a[0] );
			}
		}
	}

	public String MakeItemInfo(String ItemAmt,String ItemCode,String ItemName) {

		StringBuffer ItemInfo = new StringBuffer();

		ItemInfo.append( ItemCode.substring(0,1) );
		ItemInfo.append( "|" );
		ItemInfo.append( ItemAmt );
		ItemInfo.append( "|1|" );
		ItemInfo.append( ItemCode );
		ItemInfo.append( "|" );
		ItemInfo.append( ItemName );

		return ItemInfo.toString();
	}

	public String MakeParam(Map Trans) {

		Iterator i = Trans.keySet().iterator();
		StringBuffer sb = new StringBuffer();
	
		while( i.hasNext() )
		{
	    		Object key = i.next();
	    		Object value = Trans.get(key);
	    		sb.append( key.toString().trim()+"="+value.toString().trim()+";" );
		}

		if( sb.length() > 0 )
		{
			return sb.substring( 0,sb.length()-1 );
		}
		else
		{
	    		return "";
		}
     	}

	public String GetCIURL(String IsUseCI,String CIURL) {

		/*
		 * Default Danal CI
		 */
		String URL = "https://ui.teledit.com/Danal/Teledit/Web/images/customer_logo.gif";

		if( IsUseCI.equals("Y") && CIURL != null )
		{
			URL = CIURL;
		}

		return URL;
	}

	public String GetBgColor(String BgColor) {

		/*
		 * Default : Blue
		 */
		int Color = 0;
		int nBgColor = Integer.parseInt(BgColor);

		if( nBgColor > 0 && nBgColor < 11 )
		{
			Color = nBgColor;
		}

		if( Color >= 0 && Color <= 9 )
		{
			return "0" + Integer.toString(Color);
		}
		else
		{
			return Integer.toString(Color);
		}
	}

	public String Map2Str(Map Trans) {

		Iterator i = Trans.keySet().iterator();
		StringBuffer sb = new StringBuffer();

		while( i.hasNext() )
		{
			Object key = i.next();
			Object value = Trans.get(key);
			sb.append( key.toString().trim()+" = "+value.toString().trim()+"<BR>" );
		}

		return sb.toString();
	}

	public String toEuckr(String str) {

		if( str == null ) return "";

		try
		{
			return new String( str.getBytes("8859_1"),"euc-kr" );
		}
		catch(IOException e)
		{
			return "";
		}
	}
    //end tele pay
    
}
