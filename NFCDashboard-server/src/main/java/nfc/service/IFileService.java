package nfc.service;

import java.util.List;

import org.hibernate.Session;

import nfc.model.AttachFile;

public interface IFileService {
	boolean saveFile(AttachFile file);
	AttachFile getAttachFile(int fileId);
	AttachFile getAttachFileWithSession(int fileId,Session session);
        boolean deleteFile(String url);
        boolean deleteAttachFile(int fileId);
}
