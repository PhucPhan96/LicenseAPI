/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl.report;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import static com.lowagie.text.ElementTags.FONT;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nfc.messages.filters.BillRequestFilter;
import nfc.model.Order;
import nfc.model.ViewModel.BillSupplierInformation;
import nfc.model.ViewModel.BillView;
import nfc.service.IOrderService;
import nfc.serviceImpl.common.Utils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.document.AbstractPdfView;

/**
 *
 * @author Admin
 */
public class BillPDFView  {
    private float chartWidth = 500;
    private float chartHeight = 250;
    DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private BigDecimal totalProductAmount = BigDecimal.ZERO;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private BigDecimal totalDiscountAmount = BigDecimal.ZERO;
    private BigDecimal totalDiscountCoupon = BigDecimal.ZERO;
    private BigDecimal totalOnlineFee = BigDecimal.ZERO;
    private BigDecimal totaTaxFee = BigDecimal.ZERO;
    private int totalNumberOrder = 0;
    private String FONT = "/fonts/h2gtrm.ttf";
    
    private static final int SUMMARY_PAGE = 1;
    private static final int CHART_PAGE = 2;
    private static final int TAX_DATE_INFO = 3;
    private static final int DATE_SEQUENCE = 4;
        
    private static class SingletonHelper{
        private static final BillPDFView INSTANCE = new BillPDFView(); 
    }
    
    public static BillPDFView getInstance(){
        return SingletonHelper.INSTANCE;
    }
    
//    protected void buildPdfDocument(Map<String, Object> map, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        
//        response.setHeader("Content-Disposition", "attachment; filename=\"bill_report.pdf\"");
//        
//        BillView bill = (BillView) map.get("bill");
//        Font font =FontFactory.getFont(FontFactory.TIMES_ROMAN,BaseFont.IDENTITY_H, BaseFont.EMBEDDED );
//        font.setSize(10);
//        font.setColor(Color.WHITE);
//        
//        PdfPCell cell = new PdfPCell();
//        cell.setBackgroundColor(Color.GRAY);
//        cell.setPadding(5);
//        
//        //table summary order
//        document.add(new Paragraph("* T?ng ti?n"));
//        //document.add(getTableSummaryOrder(bill, font,cell));
//        
//        //table detail order
//        document.add(new Paragraph("*????? (" +bill.getBillRequest().getDateFrom().replaceAll("-", ".") + "~" + bill.getBillRequest().getDateTo().replaceAll("-", ".") + ")"));
//        document.add(getTableOrderDetail(bill, font,cell));
//        
//    }
    
    
    public Document getPdfBillReport(BillView bill, ByteArrayOutputStream byteArray) throws DocumentException, IOException{
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, byteArray);
        document.open();
        
        initAmountOrder(bill);
        
        BaseFont baseFont = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, true); 
        Font font  = new Font(baseFont); 
        font.setColor(Color.BLACK);
        font.setSize(8);
        
        Font fontTile  = new Font(baseFont); 
        fontTile.setColor(Color.BLACK);
        fontTile.setSize(12);
        
        initPageInfomartion(document, bill, font);
        
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.GRAY);
        cell.setPadding(5);
        //table summary order
        
        Paragraph titleSummaryAmount = new Paragraph("* 정산금액", fontTile);
        titleSummaryAmount.setSpacingAfter(10);
        document.add(titleSummaryAmount);
        getTableSummaryOrder(font, cell, document);
        document.add(getLine());
        
        document.newPage();
        //add chart
        document.add(new Paragraph(getInformationDateTiltle(bill.getBillRequest(), CHART_PAGE) + "* 주문정보", fontTile));
        document.add(new Paragraph("주문수:  총 " + totalNumberOrder + "건 (주문수는 터치주문을 기준으로 하며 전화주문은 포함하지 않았습니다)", fontTile));
        document.add(generateBarChart(writer, bill));
        document.add(getLine());
        Paragraph titleTaxTable = new Paragraph("* 세금계산서 내옉", fontTile);
        titleTaxTable.setSpacingAfter(10);
        document.add(titleTaxTable);
        document.add(getTaxTable(bill, font));
        document.newPage();
         //table detail order
        Paragraph titleOrderDetailAmount = new Paragraph("*  상세거래내옉 (" +bill.getBillRequest().getDateFrom().replaceAll("-", ".") + "~" + bill.getBillRequest().getDateTo().replaceAll("-", ".") + ")", fontTile);
        titleOrderDetailAmount.setSpacingAfter(10);
        document.add(titleOrderDetailAmount);
        document.add(getTableOrderDetail(bill, font));
        
        
        //add total table
        Paragraph titleTotal = new Paragraph("*  거래내역합계 (" +bill.getBillRequest().getDateFrom().replaceAll("-", ".") + "~" + bill.getBillRequest().getDateTo().replaceAll("-", ".") + ")", fontTile);
        titleTotal.setSpacingAfter(10);
        titleTotal.setSpacingBefore(30);
        document.add(titleTotal);
        document.add(getTotalTable(bill, font));

        document.close();
        return document;
    }
    
    
    
    private PdfPTable getTaxTable(BillView bill, Font font) throws DocumentException, IOException{
        BaseFont baseFont = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, true); 
        Font fontHearder  = new Font(baseFont); 
        fontHearder.setColor(Color.WHITE);
        
        Font fontBold  = new Font(baseFont); 
        fontBold.setColor(Color.BLACK);
        fontBold.setStyle(Font.BOLD);
        
        PdfPTable taxTableOrderOuter = new PdfPTable(2);
        
        taxTableOrderOuter.setWidthPercentage(100.0f);
        taxTableOrderOuter.setWidths(new float[]{5.0f, 5.0f});
        PdfPCell cellTaxTable = new PdfPCell();
        cellTaxTable.setPaddingRight(10);
        cellTaxTable.setBorder(PdfPCell.NO_BORDER);
        
        PdfPCell cellAmountTable = new PdfPCell();
        cellAmountTable.setBorder(PdfPCell.NO_BORDER);
        cellAmountTable.setPaddingLeft(10);
        
        
        
        
        
        PdfPTable tableTax = new PdfPTable(2);
        tableTax.setWidthPercentage(100.0f);
        tableTax.setWidths(new float[]{4.0f, 6.0f});
        PdfPCell headerCell = new PdfPCell();
        headerCell.setPadding(5);
        headerCell.setBackgroundColor(Color.GRAY);
        headerCell.setPhrase(new Phrase("항목", fontHearder));
        tableTax.addCell(headerCell);
        headerCell.setPhrase(new Phrase("내용", fontHearder));
        tableTax.addCell(headerCell);
        tableTax.addCell(getCell("매입기간",-1, font, 5, true));
        tableTax.addCell(getCell(getInformationDateTiltle(bill.getBillRequest(), TAX_DATE_INFO),-1, font, 5, true));
        
        tableTax.addCell(getCell("작성일자",-1, font, 5, true));
        tableTax.addCell(getCell(getInformationDateTiltle(bill.getBillRequest(), DATE_SEQUENCE),-1, font, 5, true));
        tableTax.addCell(getCell("공급가액",-1, font, 5, true));
        tableTax.addCell(getCell(decimalFormat.format(totalOnlineFee)+" 헌",-1, font, 5, true));
        
        tableTax.addCell(getCell("부가세액",-1, font, 5, true));
        tableTax.addCell(getCell(decimalFormat.format(totaTaxFee)+" 헌",-1, font, 5, true));
        tableTax.addCell(getCell("공급받는자 등록번호",-1, font, 5, true));
        tableTax.addCell(getCell("",-1, font, 5, true));
        tableTax.addCell(getCell("공급자",-1, font, 5, true));
        tableTax.addCell(getCell("",-1, font, 5, true));
        tableTax.addCell(getCell("공급자 등록번호",-1, font, 5, true));
        tableTax.addCell(getCell("",-1, font, 5, true));
        
        cellTaxTable.addElement(tableTax);
        taxTableOrderOuter.addCell(cellTaxTable);
        taxTableOrderOuter.addCell(cellAmountTable);
        return taxTableOrderOuter;
    }
    
    private void initPageInfomartion(Document document, BillView bill, Font font) throws DocumentException{
        
        PdfPTable amountInformation = new PdfPTable(1);
        amountInformation.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
        amountInformation.setWidthPercentage(60);
        amountInformation.addCell(getCell("서곙범 사장님, " + getInformationDateTiltle(bill.getBillRequest(), SUMMARY_PAGE), PdfPCell.ALIGN_LEFT, font, 0, false));
        amountInformation.addCell(getCell("요기요 터치주문을 통해 총 " + totalNumberOrder + "건 의 주문을 전달해드렸습니다.", PdfPCell.ALIGN_LEFT, font, 0, false));
        amountInformation.addCell(getCell("총 주문금액은    " + totalProductAmount + "헌 입니다", PdfPCell.ALIGN_LEFT, font, 0, false));
        document.add(amountInformation);
        
        BillSupplierInformation billSupplierInfor = bill.getBillSupplierInformation();
        PdfPTable supplierInformation = new PdfPTable(1);
        supplierInformation.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);
        supplierInformation.setWidthPercentage(40);
        supplierInformation.addCell(getCell(billSupplierInfor.getAddress(), PdfPCell.ALIGN_RIGHT, font, 0, false));
        supplierInformation.addCell(getCell(billSupplierInfor.getSupplier_name() + " " + billSupplierInfor.getOwnername(), PdfPCell.ALIGN_RIGHT, font, 0, false));
        document.add(supplierInformation);
        
        
//        Paragraph titleDateAmount = new Paragraph("서곙범 사장님, " + getInformationDateTiltle(bill.getBillRequest()),font);
//        titleDateAmount.setSpacingAfter(5);
//        document.add(titleDateAmount);
//        Paragraph titleSumOrder = new Paragraph("요기요 터치주문을 통해 총 " + totalNumberOrder + "건 의 주문을 전달해드렸습니다.",font);
//        titleSumOrder.setSpacingAfter(5);
//        document.add(titleSumOrder);
//        Paragraph totalOrder = new Paragraph("총 주문금액은    " + totalAmount + "헌 입니다",font);
//        totalOrder.setSpacingAfter(10);
//        document.add(totalOrder);
        
        
//        BillSupplierInformation billSupplierInfor = bill.getBillSupplierInformation();
//        Paragraph billAddress = new Paragraph(billSupplierInfor.getAddress(),font);
//        titleDateAmount.setAlignment(Element.ALIGN_JUSTIFIED);
//        billAddress.setSpacingAfter(5);
//        document.add(billAddress);
//        
//        Paragraph supplierNameAndUser = new Paragraph(billSupplierInfor.getSupplier_name() + " " + billSupplierInfor.getOwnername(),font);
//        supplierNameAndUser.setSpacingAfter(50);
//        titleDateAmount.setAlignment(Element.ALIGN_JUSTIFIED);
//        document.add(supplierNameAndUser);
        document.add(getLine());
        
        
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
    
    private Paragraph getLine(){
        Paragraph line = new Paragraph("----------------------------------------------------------------------------------------------------------------------------------");
        line.setSpacingAfter(50);
        line.setSpacingBefore(30);
        return line;
    }
    
    private String getInformationDateTiltle(BillRequestFilter billRequest, int flag){
        String result = "";
        String[] dateFromSplit = billRequest.getDateFrom().split("-");
        String[] dateToSplit = billRequest.getDateTo().split("-");
        int dayFrom = Integer.parseInt(dateFromSplit[2]); 
        int dayTo = Integer.parseInt(dateToSplit[2]); 
        
        
        if(flag == SUMMARY_PAGE || flag == DATE_SEQUENCE){
            result = dateFromSplit[0] + "녠 " + dateFromSplit[1] + "헐 ";
        }
        else if (flag == CHART_PAGE || flag == TAX_DATE_INFO){
            result = dateFromSplit[1] + "헐 ";
        }
        
        if(dayTo < 8){
            result = result + "1기";
        }
        else if (dayTo < 15){
            result = result + "2기";
        }
        else if (dayTo < 22){
            result = result + "3기";
        }
        else{
            result = result + "4기";
        }
         
        if(flag == SUMMARY_PAGE){
            result = result + " (" + dayFrom + "일~" +  dayTo + "일)";
        }
        else if(flag ==  TAX_DATE_INFO){
            result = result + " (" + dateFromSplit[0] + "." +  dayFrom + "~" + dateFromSplit[0] + "." +  dayTo + ")";
        }
        
        return result;
    }
    
    
    private void initAmountOrder(BillView bill){
        totalProductAmount = BigDecimal.ZERO;
        totalAmount = BigDecimal.ZERO;
        totalDiscountAmount = BigDecimal.ZERO;
        totalDiscountCoupon = BigDecimal.ZERO;
        totalOnlineFee = BigDecimal.ZERO;
        totaTaxFee = BigDecimal.ZERO;
        totalNumberOrder = 0;
        totalNumberOrder = bill.getOrders().size();
        for(Order order : bill.getOrders()){
           totalProductAmount = totalProductAmount.add(order.getProd_amt());
           totalAmount = totalAmount.add(order.getOrder_amt());
           totalDiscountAmount = totalDiscountAmount.add(order.getDisc_amt());
           totalDiscountCoupon = totalDiscountCoupon.add(order.getDiscount_coupon());
           totalOnlineFee = totalOnlineFee.add(order.getOnline_fee());
           totaTaxFee = totaTaxFee.add(order.getTax_amt());
        }
    }
        
    
    private void getTableSummaryOrder(Font font, PdfPCell cell, Document document ) throws DocumentException, IOException{
        
        BaseFont baseFont = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, true); 
        Font fontHearder  = new Font(baseFont); 
        fontHearder.setColor(Color.WHITE);
        
        Font fontBold  = new Font(baseFont); 
        fontBold.setColor(Color.BLACK);
        fontBold.setStyle(Font.BOLD);
        
        PdfPTable tableSummaryOrderOuter = new PdfPTable(2);
        
        tableSummaryOrderOuter.setWidthPercentage(100.0f);
        tableSummaryOrderOuter.setWidths(new float[]{5.0f, 5.0f});
        PdfPCell cellSummaryTable = new PdfPCell();
        cellSummaryTable.setPaddingRight(10);
        cellSummaryTable.setBorder(PdfPCell.NO_BORDER);
        
        PdfPCell cellAmountTable = new PdfPCell();
        cellAmountTable.setBorder(PdfPCell.NO_BORDER);
        cellAmountTable.setPaddingLeft(10);
        //cellAmountTable.setHorizontalAlignment(PdfPCell.BOTTOM);
        
        
        
        PdfPTable tableSummaryOrder = new PdfPTable(2);
        tableSummaryOrder.setWidthPercentage(100.0f);
        tableSummaryOrder.setWidths(new float[]{6.0f, 4.0f});
        cell.setPhrase(new Phrase("항목", fontHearder));
        
        tableSummaryOrder.addCell(cell);
        cell.setPhrase(new Phrase("금액", fontHearder));
        tableSummaryOrder.addCell(cell);
        
        tableSummaryOrder.addCell(getCell("주문금액 사장님",-1, font, 5, true));
        tableSummaryOrder.addCell(getCell(decimalFormat.format(totalProductAmount)+" 헌",-1, font, 5, true));
        
        tableSummaryOrder.addCell(getCell("사장님 자할인",-1, font, 5, true));
        tableSummaryOrder.addCell(getCell(decimalFormat.format(totalDiscountAmount)+" 헌",-1, font, 5, true));
        
        tableSummaryOrder.addCell(getCell("본사 할인자헌금",-1, font, 5, true));
        tableSummaryOrder.addCell(getCell(decimalFormat.format(totalDiscountCoupon)+" 헌",-1, font, 5, true));
        //online fee
        tableSummaryOrder.addCell(getCell("외부결제 수수료",-1, font, 5, true));
        tableSummaryOrder.addCell(getCell(decimalFormat.format(totalOnlineFee)+" 헌",-1, font, 5, true));
        
        tableSummaryOrder.addCell(getCell("부가가치세",-1, font, 5, true));
        tableSummaryOrder.addCell(getCell(decimalFormat.format(totaTaxFee)+" 헌",-1, font, 5, true));
        tableSummaryOrder.addCell(getCell("기타 서비스 이용료",-1, font, 5, true));
        tableSummaryOrder.addCell(getCell("0 헌",-1, font, 5, true));
        tableSummaryOrder.addCell(getCell("누적 미납요금",-1, font, 5, true));
        tableSummaryOrder.addCell(getCell("0 헌",-1, font, 5, true));
        tableSummaryOrder.addCell(getCell("알뜰쇼팡 지하금",-1, font, 5, true));
        tableSummaryOrder.addCell(getCell("0 헌",-1, font, 5, true));
        
        
        tableSummaryOrder.addCell(getCell("합계",-1, fontBold, 5, true));
        tableSummaryOrder.addCell(getCell(decimalFormat.format(totalAmount) + " 헌",-1, fontBold, 5, true));
        tableSummaryOrder.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
        
        
        cellSummaryTable.addElement(tableSummaryOrder);
        tableSummaryOrderOuter.addCell(cellSummaryTable);
        cellAmountTable.addElement(generateInfoTotalAmount(document, totalAmount));
        tableSummaryOrderOuter.addCell(cellAmountTable);
        document.add(tableSummaryOrderOuter);
    }
    
    
    private PdfPTable generateInfoTotalAmount(Document document, BigDecimal totalAmount) throws DocumentException, IOException{
        BaseFont baseFont = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, true); 
        Font fontBold  = new Font(baseFont); 
        fontBold.setColor(Color.BLACK);
        fontBold.setStyle(Font.BOLD);
        fontBold.setSize(13);
        
        
        PdfPTable tableTotalAmount = new PdfPTable(2);
        tableTotalAmount.setWidthPercentage(100.0f);
        tableTotalAmount.setWidths(new float[]{5.0f, 5.0f});
        PdfPCell cellTitle = new PdfPCell(new Phrase("입금 받으실 금액", fontBold));
        cellTitle.disableBorderSide(Rectangle.BOTTOM);
        cellTitle.setColspan(2);
        cellTitle.setPaddingTop(10);
        tableTotalAmount.addCell(cellTitle);
        
        PdfPCell cellAmount = new PdfPCell(new Phrase(decimalFormat.format(totalAmount) + " 헌", fontBold));
        cellAmount.setColspan(2);
        cellAmount.setPaddingTop(10);
        cellAmount.setPaddingBottom(10);
        cellAmount.disableBorderSide(Rectangle.TOP);
        cellAmount.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tableTotalAmount.addCell(cellAmount);
        tableTotalAmount.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);
        
        return tableTotalAmount;
    }
    
    
    
    
    private PdfPTable getTableOrderDetail(BillView bill, Font font) throws DocumentException{
        PdfPTable tableOrderDetail = new PdfPTable(10);
        tableOrderDetail.setWidthPercentage(100.0f);
        tableOrderDetail.setWidths(new float[]{0.5f, 1.4f, 1.6f, 0.9f, 1f, 0.9f, 0.9f, 0.9f, 0.9f, 1f});
        //cell.setPhrase(new Phrase("No.", font));
        tableOrderDetail.addCell(getCell("No", -1, font, 5, true));
        //cell.setPhrase(new Phrase("거래일시", font));
        tableOrderDetail.addCell(getCell("거래일시", -1, font, 5, true));
        //cell.setPhrase(new Phrase("배달주소", font));
        tableOrderDetail.addCell(getCell("배달주소", -1, font, 5, true));
        //cell.setPhrase(new Phrase("주문금액", font));
        tableOrderDetail.addCell(getCell("주문금액", -1, font, 5, true));
        //cell.setPhrase(new Phrase("Payment Type", font));
        tableOrderDetail.addCell(getCell("곌제혱태", -1, font, 5, true));
        //cell.setPhrase(new Phrase("Store Discount", font));
        tableOrderDetail.addCell(getCell("사장님 자체할인", -1, font, 5, true));
        //cell.setPhrase(new Phrase("Event Discount", font));
        tableOrderDetail.addCell(getCell("본사 할인지헌금", -1, font, 5, true));
        //cell.setPhrase(new Phrase("Online Fee", font));
        tableOrderDetail.addCell(getCell("주문중계 이용료", -1, font, 5, true));
        //cell.setPhrase(new Phrase("Tax", font));
        tableOrderDetail.addCell(getCell("외부곌제 수수료", -1, font, 5, true));
        //cell.setPhrase(new Phrase("Total", font));
        tableOrderDetail.addCell(getCell("부가가지세", -1, font, 5, true));
        
        int indexOrderDetail = 1;
        for(Order order : bill.getOrders()){
            tableOrderDetail.addCell(getCell(String.valueOf(indexOrderDetail++), -1, font, 5, true));
            tableOrderDetail.addCell(getCell(Utils.convertDateToString(order.getOrder_date()), -1, font, 5, true));
            tableOrderDetail.addCell(getCell(order.getSupplier_name(), -1, font, 5, true));
            tableOrderDetail.addCell(getCell(decimalFormat.format(order.getProd_amt()), -1, font, 5, true));
            tableOrderDetail.addCell(getCell("온라인카드", -1, font, 5, true));
            tableOrderDetail.addCell(getCell(decimalFormat.format(order.getDisc_amt()), -1, font, 5, true));
            tableOrderDetail.addCell(getCell(decimalFormat.format(order.getDiscount_coupon()), -1, font, 5, true));
            tableOrderDetail.addCell(getCell(decimalFormat.format(order.getOnline_fee()), -1, font, 5, true));
            tableOrderDetail.addCell(getCell(decimalFormat.format(order.getTax_amt()), -1, font, 5, true));
            tableOrderDetail.addCell(getCell(decimalFormat.format(order.getOrder_amt()), -1, font, 5, true));
        }
        return tableOrderDetail;
    }
    
    private void setValueDefautForChart(HashMap<Integer, Integer> hmap, BillView bill, int dayFrom, int dayTo){
        for(int index = dayFrom; index<= dayTo ; index++ ){
            hmap.put(index, 0);
        }
    }
    
    private Image generateBarChart(PdfWriter writer, BillView bill) throws BadElementException {
        String[] dateFromSplit = bill.getBillRequest().getDateFrom().split("-");
        String[] dateToSplit = bill.getBillRequest().getDateTo().split("-");
        int dayFrom = Integer.parseInt(dateFromSplit[2]); 
        int dayTo = Integer.parseInt(dateToSplit[2]); 
        HashMap<Integer, Integer> valueChart = new HashMap<Integer, Integer>();
        setValueDefautForChart(valueChart, bill, dayFrom, dayTo);
        for(Order order: bill.getOrders()){
            valueChart.replace(order.getOrder_date().getDate(), (valueChart.get(order.getOrder_date().getDate()) + 1));
        }
        
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        for (int i = dayFrom; i <= dayTo; i++) {
            dataSet.setValue(valueChart.get(i), "Population", String.valueOf(i));
        }

        JFreeChart chart = ChartFactory.createBarChart(
                        "", "", "",
                        dataSet, PlotOrientation.VERTICAL, false, false, false);
        
        PdfContentByte contentByte = writer.getDirectContent();
        PdfTemplate template = contentByte.createTemplate(chartWidth, chartHeight);
        Graphics2D graphics2d = template.createGraphics(chartWidth, chartHeight,new DefaultFontMapper());
        Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, chartWidth, chartHeight);

        chart.draw(graphics2d, rectangle2d);
        graphics2d.dispose();
        
        Image chartImage = Image.getInstance(template);
        return chartImage;
        //contentByte.addTemplate(template, 0, 0);
    }
    
    
    private PdfPTable getTotalTable(BillView bill, Font font) throws DocumentException{
        PdfPTable tableTotal = new PdfPTable(8);
        tableTotal.setWidthPercentage(100.0f);
        tableTotal.setWidths(new float[]{3.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f});
        tableTotal.addCell(getCell("상호명", -1, font, 5, true));
        tableTotal.addCell(getCell("주문수", -1, font, 5, true));
        tableTotal.addCell(getCell("주문금액", -1, font, 5, true));
        tableTotal.addCell(getCell("사장님 자체할인", -1, font, 5, true));
        tableTotal.addCell(getCell("본사 할인지헌금", -1, font, 5, true));
        tableTotal.addCell(getCell("주문중계 이용료", -1, font, 5, true));
        tableTotal.addCell(getCell("외부곌제 수수료", -1, font, 5, true));
        tableTotal.addCell(getCell("부가가지세", -1, font, 5, true));
        
        tableTotal.addCell(getCell(bill.getBillSupplierInformation().getSupplier_name() + "-" + bill.getBillSupplierInformation().getOwnername(), PdfPCell.ALIGN_CENTER, font, 5, true));
        tableTotal.addCell(getCell(decimalFormat.format(totalNumberOrder), PdfPCell.ALIGN_CENTER, font, 5, true));
        tableTotal.addCell(getCell(decimalFormat.format(totalProductAmount), PdfPCell.ALIGN_CENTER, font, 5, true));
        tableTotal.addCell(getCell(decimalFormat.format(totalDiscountAmount), PdfPCell.ALIGN_CENTER, font, 5, true));
        tableTotal.addCell(getCell(decimalFormat.format(totalDiscountCoupon), PdfPCell.ALIGN_CENTER, font, 5, true));
        tableTotal.addCell(getCell(decimalFormat.format(totalOnlineFee), PdfPCell.ALIGN_CENTER, font, 5, true));
        tableTotal.addCell(getCell(decimalFormat.format(totaTaxFee), PdfPCell.ALIGN_CENTER, font, 5, true));
        tableTotal.addCell(getCell(decimalFormat.format(totalAmount), PdfPCell.ALIGN_CENTER, font, 5, true));
        
        tableTotal.addCell(getCellBorderTopAndBottom("합계", PdfPCell.ALIGN_CENTER, font, 5));
        tableTotal.addCell(getCellBorderTopAndBottom(decimalFormat.format(totalNumberOrder), PdfPCell.ALIGN_CENTER, font, 5));
        tableTotal.addCell(getCellBorderTopAndBottom(decimalFormat.format(totalProductAmount), PdfPCell.ALIGN_CENTER, font, 5));
        tableTotal.addCell(getCellBorderTopAndBottom(decimalFormat.format(totalDiscountAmount), PdfPCell.ALIGN_CENTER, font, 5));
        tableTotal.addCell(getCellBorderTopAndBottom(decimalFormat.format(totalDiscountCoupon), PdfPCell.ALIGN_CENTER, font, 5));
        tableTotal.addCell(getCellBorderTopAndBottom(decimalFormat.format(totalOnlineFee), PdfPCell.ALIGN_CENTER, font, 5));
        tableTotal.addCell(getCellBorderTopAndBottom(decimalFormat.format(totaTaxFee), PdfPCell.ALIGN_CENTER, font, 5));
        tableTotal.addCell(getCellBorderTopAndBottom(decimalFormat.format(totalAmount), PdfPCell.ALIGN_CENTER, font, 5));
        return tableTotal;
    }
    
    public PdfPCell getCellBorderTopAndBottom(String text, int alignment, Font font, int padding) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        if(alignment > -1){
            cell.setHorizontalAlignment(alignment);
        }
        cell.setPadding(padding);
        cell.setBorderWidthTop(1);
        cell.setBorderWidthBottom(1);
        cell.setBorderWidthLeft(PdfPCell.NO_BORDER);
        cell.setBorderWidthRight(PdfPCell.NO_BORDER);
        return cell;
    }
    
}
