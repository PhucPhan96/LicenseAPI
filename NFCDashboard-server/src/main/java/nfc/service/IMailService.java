package nfc.service;

import nfc.model.Mail;

public interface IMailService {
	boolean sendSimpleMail(String from, String to, String subject, String msg);
        boolean sendSimpleMail(Mail mail);
	String sendSimpleMailStr(String from, String to, String subject, String msg);
        boolean sendMailFromAdmin(Mail mail);
}
