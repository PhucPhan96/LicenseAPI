/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl.report;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import nfc.model.Order;
import nfc.model.ViewModel.BillView;
import nfc.model.ViewModel.VATReportInformation;
import nfc.serviceImpl.common.Utils;
import org.springframework.util.StringUtils;

/**
 *
 * @author Admin
 */
public class VATPDFReport {
    
    private String FONT = "/fonts/h2gtrm.ttf";
    DecimalFormat decimalFormat = new DecimalFormat("#,###");
    
    private static class SingletonHelper{
        private static final VATPDFReport INSTANCE = new VATPDFReport(); 
    }
    
    public static VATPDFReport getInstance(){
        return SingletonHelper.INSTANCE;
    }
    
    public Document getVATPdfReport(List<VATReportInformation> vatReports, ByteArrayOutputStream byteArray) throws DocumentException, IOException{
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, byteArray);
        document.open();
        document.add(new Paragraph(""));
        //initAmountOrder(bill);
        
        BaseFont baseFont = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, true); 
        Font font  = new Font(baseFont); 
        font.setColor(Color.BLACK);
        font.setSize(8);
        getTableOrderDetail(vatReports, baseFont, document);
        document.close();
        return document;
    }
    
    public PdfPCell getCell(String text, int alignment, Font font, int padding, boolean boder) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(padding);
        if(alignment > -1){
            cell.setHorizontalAlignment(alignment);
        }
        if(!boder){
            cell.setBorder(PdfPCell.NO_BORDER);
        }
        return cell;
    }
    
    private void getTableOrderDetail(List<VATReportInformation> vatReports, BaseFont baseFont, Document document) throws DocumentException{
        Font font  = new Font(baseFont); 
        font.setColor(Color.BLACK);
        font.setSize(8);
        PdfPTable tableOrderDetail = new PdfPTable(6);
        tableOrderDetail.setWidthPercentage(100.0f);
        tableOrderDetail.setWidths(new float[]{2.5f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f});
        Font fontHearder  = new Font(baseFont); 
        fontHearder.setSize(8);
        fontHearder.setColor(Color.WHITE);
        PdfPCell headerCell = new PdfPCell();
        headerCell.setPadding(5);
        headerCell.setBackgroundColor(Color.GRAY);
        headerCell.setPhrase(new Phrase("거래일시", fontHearder));
        tableOrderDetail.addCell(headerCell);
        headerCell.setPhrase(new Phrase("배달주소", fontHearder));
        tableOrderDetail.addCell(headerCell);
        headerCell.setPhrase(new Phrase("주문금액", fontHearder));
        tableOrderDetail.addCell(headerCell);
        headerCell.setPhrase(new Phrase("곌제혱태", fontHearder));
        tableOrderDetail.addCell(headerCell);
        headerCell.setPhrase(new Phrase("사장님 자체할인", fontHearder));
        tableOrderDetail.addCell(headerCell);
        headerCell.setPhrase(new Phrase("본사 할인지헌금", fontHearder));
        tableOrderDetail.addCell(headerCell);
        
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        for(VATReportInformation vatReport : vatReports){
            totalAmount = totalAmount.add(vatReport.getProd_amt());
            tableOrderDetail.addCell(getCell(Utils.convertDateToString(vatReport.getOrder_date()), -1, font, 5, true));
            tableOrderDetail.addCell(getCell(vatReport.getSupplier_name(), -1, font, 5, true));
            if(!StringUtils.isEmpty(vatReport.getUser_address())){
                tableOrderDetail.addCell(getCell(vatReport.getUser_address(), -1, font, 5, true));
            }
            else{
                tableOrderDetail.addCell(getCell(vatReport.getCustomer_address(), -1, font, 5, true));
            }
            
            tableOrderDetail.addCell(getCell("온라인카드", -1, font, 5, true));
            tableOrderDetail.addCell(getCell(decimalFormat.format(vatReport.getProd_amt().subtract(vatReport.getDisc_amt())), -1, font, 5, true));
            tableOrderDetail.addCell(getCell(decimalFormat.format(vatReport.getDisc_amt()), -1, font, 5, true));
        }
        
//        font.setSize(12);
//        font.setStyle(Font.BOLD);
        Font boldFont  = new Font(baseFont); 
        boldFont.setColor(Color.BLACK);
        boldFont.setSize(12);
        boldFont.setStyle(Font.BOLD);
        Paragraph title = new Paragraph("Total money " + decimalFormat.format(totalAmount) + "헌", boldFont);
        title.setSpacingAfter(10);
        document.add(title);
        document.add(tableOrderDetail);
    }
}
