package nfc.serviceImpl;

import nfc.model.Code;
import nfc.model.Mail;
import nfc.service.ICodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import nfc.service.IMailService;
import nfc.serviceImpl.common.Utils;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class MailService implements IMailService{
    @Autowired
    private ICodeService codeDAO;

    @Autowired
    private JavaMailSenderImpl mailSender;
	
    public boolean sendSimpleMail(String from, String to, String subject, String msg){
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(msg);
            mailSender.send(message);
            return true;
        }
        catch(Exception ex)
        {
            System.out.println("mail exception " + ex.getMessage());
            return false;
        }
    }
        
    private Code getCodeUserEmail(){
        return codeDAO.getCode("0013", "0001");
    }

    private void setMailSender(Code code){
        mailSender.setUsername(code.getCode_name());
        mailSender.setPassword(code.getCode_desc());
    }
        
    public boolean sendSimpleMail(Mail mail){
        try{
          
            Code code = getCodeUserEmail();
            setMailSender(code);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(code.getCode_name());
            message.setSubject(mail.getTitle());
            message.setText("From mail: " + mail.getFromAdd()
                            +"\n\n" + mail.getContent());
            mailSender.send(message);
            return true;
        }
        catch(Exception ex)
        {
            System.out.println("mail exception " + ex.getMessage());
            return false;
        }
    }
    
        
    public String sendSimpleMailStr(String from, String to, String subject, String msg){
        try{

            mailSender.setUsername(from);
            System.out.println(mailSender);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(msg);
            mailSender.send(message);
            return "ok";
        }
        catch(Exception ex)
        {
            return ex.getMessage();
        }
    }
     public boolean sendMailFromAdmin(Mail mail){
        try{
          
            Code code = getCodeUserEmail();
            setMailSender(code);           
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("kjncunn@gmail.com");
            message.setTo(mail.getToAdd());
            message.setSubject(mail.getTitle());
            message.setText("From mail: " + mail.getFromAdd()
                            +"\n\n"+ "New your password: " + mail.getContent());
            mailSender.send(message);
             System.out.println("Noi dung mail " + message);
            return true;
        }
        catch(Exception ex)
        {
            System.out.println("mail exception " + ex.getMessage());
            return false;
        }
    }
}
