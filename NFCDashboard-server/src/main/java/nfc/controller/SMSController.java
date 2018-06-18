package nfc.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import nfc.model.Mail;

import nfc.service.IMailService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class SMSController {
	public static final String ACCOUNT_SID = "ACb4fc4a37e7e7edd2396d1c8bfe766034";
	public static final String AUTH_TOKEN = "01a94a54d2c1a124b0a73d0dc7715754";
	@Autowired
	private IMailService mailDAO;
	public void sendSMS(){
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message message = Message
            .creator(new PhoneNumber("+84932362854"), new PhoneNumber("+12142544543"),
                "sms notification from NFC")
            .create();
             System.out.println(message.getSid()); 
	}
	@RequestMapping(value="sms/send",method=RequestMethod.GET)
	public String getListOrderPosView(){
		/*if(sendEmail())
			return "ok";
		else
			return "false";*/
		return sendEmail();
	}
        
        @RequestMapping(value = "mail/send/sysadmin", method = RequestMethod.POST)
        public @ResponseBody String fSendEmailToSysAdmin(@RequestBody Mail mail) {
            boolean data = mailDAO.sendSimpleMail(mail);
            System.out.println(data);
            return "{\"result\":\"" + data + "\"}";
        }
	public String sendEmail(){
            //return mailDAO.sendSimpleMail("kjncunn@gmail.com", "kimduy.mlcm@gmail.com", "hello", "check register from NFC");
            return mailDAO.sendSimpleMailStr("kjncunn@gmail.com", "kimduy.mlcm@gmail.com", "hello", "check register from NFC");
		
	}
}
