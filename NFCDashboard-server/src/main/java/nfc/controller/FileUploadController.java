package nfc.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.util.Calendar;
import java.util.Random;
import java.util.UUID;
import javax.imageio.ImageIO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import nfc.model.AttachFile;
import nfc.service.IFileService;
import nfc.serviceImpl.common.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;


@RestController
public class FileUploadController {
    @Autowired
    private IFileService fileDAO;
    private static final int BUFFER_SIZE = 10 * 1024 * 1024;
    @ResponseBody
    @RequestMapping(value="/file/upload", method=RequestMethod.POST)
    public AttachFile upload(@RequestParam MultipartFile file, HttpServletRequest request, HttpSession session){
        AttachFile attachFile = null;
        try
        {
            System.out.println(file.getOriginalFilename().split("\\.")[1]);
            String[] lstFileName = file.getOriginalFilename().split("\\.");
            System.out.println(lstFileName[lstFileName.length-1]);
            String name = UUID.randomUUID().toString()+ "." + lstFileName[lstFileName.length-1]; //file.getOriginalFilename();//request.getParameter("name");
            /*Integer chunk = 0, chunks = 0;
            if(null != request.getParameter("chunk") && !request.getParameter("chunk").equals(""))
            {
                    chunk = Integer.valueOf(request.getParameter("chunk"));
            }
            if(null != request.getParameter("chunks") && !request.getParameter("chunks").equals(""))
            {
                    chunks = Integer.valueOf(request.getParameter("chunks"));
            }*/
            String relativePath = Utils.uploadUrl;
            String realPath = session.getServletContext().getRealPath("");
            File folder = new File(realPath + relativePath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File destFile = new File(folder, name);
            if (destFile.exists()) {  //chunk == 0 && 
                destFile.delete();  
                destFile = new File(folder, name);
            }
            appendFile(file.getInputStream(), destFile);  
            attachFile = insertImage(name, (int) file.getSize(), file.getOriginalFilename());
        /*if (chunk == chunks - 1) {  
            System.out.println("finish");
            insertImage(name);
        }
        else 
        {
                System.out.println("In processing "+(chunks-1-chunk));
        }*/
        }
        catch(Exception ex)
        {
            System.out.println("Error " + ex.getMessage());
        }
        return attachFile;
        //return Utils.convertObjectToJsonString(attachFile);// "response from controller";
    }
    //Lucas
    @ResponseBody
    @RequestMapping(value="/app/file/upload", method=RequestMethod.POST)
    public AttachFile uploadForApp(@RequestBody String base64IMG, HttpServletRequest request, HttpSession session){
        AttachFile attachFile = null;
        BASE64Decoder decoder = new BASE64Decoder();
        try{
            byte[] decodedBytes = decoder.decodeBuffer(base64IMG);
            String nameTemp = getSaltString();
            String uploadFile = nameTemp + ".jpg";
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(decodedBytes));
            File outputfile = new File(uploadFile);
            // write the image
            ImageIO.write(image, "jpg", outputfile);
            InputStream targetStream = new FileInputStream(outputfile);
            String name = outputfile.getName().toString();
            String relativePath = Utils.uploadUrl;
            String realPath = session.getServletContext().getRealPath("");
            File folder = new File(realPath + relativePath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File destFile = new File(folder, name);
            if (destFile.exists()) {  //chunk == 0 && 
                destFile.delete();  
                destFile = new File(folder, name);
            }
            appendFile(targetStream, destFile);  
            attachFile = insertImage(name, (int) outputfile.length(), name);
            return attachFile;
        } catch(Exception e){
            return attachFile;
        }

    }
    
    private void appendFile(InputStream in, File destFile)
    {
        OutputStream out = null;
        try {
            if (destFile.exists()) {
                out = new BufferedOutputStream(new FileOutputStream(destFile, true), BUFFER_SIZE); 
            } else {
                out = new BufferedOutputStream(new FileOutputStream(destFile),BUFFER_SIZE);
            }
            in = new BufferedInputStream(in, BUFFER_SIZE);

            int len = 0;
            byte[] buffer = new byte[BUFFER_SIZE];			
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            System.out.print("Error" + e.getMessage());
        } finally {		
            try {
                if (null != in) {
                    in.close();
                }
                if(null != out){
                    out.close();
                }
            } catch (IOException e) {
                System.out.print("Error" + e.getMessage());
            }
        }
    }
	
    private AttachFile insertImage(String name, int size, String orgName){
        System.out.println("Name " + name);
        AttachFile file = new AttachFile();
        file.setApp_id(Utils.appId);
        file.setFile_name(name);
        file.setCreated_date(new Date(Calendar.getInstance().getTime().getTime()).toString());
        file.setFile_path(Utils.uploadUrl + name);
        file.setFile_org_name(orgName);
        file.setFile_size(size);

        //img.setImageUrl("/uploads/images/"+ name );
        //fileUploadDAO.saveFile(img);
        fileDAO.saveFile(file);
        return file;
    }
    
    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 50) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
}
