/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.controller;

import com.lowagie.text.DocumentException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import nfc.messages.filters.BillRequestFilter;
import nfc.messages.filters.StatisticRequestFilter;
import nfc.messages.request.DiscountRequest;
import nfc.model.Discount;
import nfc.model.ViewModel.BillView;
import nfc.model.ViewModel.VATReport;
import nfc.model.ViewModel.VATReportInformation;
import nfc.service.IOrderService;
import nfc.service.ISupplierService;
import nfc.serviceImpl.report.BillPDFView;
import nfc.serviceImpl.report.VATPDFReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Admin
 */
@RestController
public class ReportController {
    
    @Autowired
    IOrderService orderDAO;
    @Autowired
    private ISupplierService supplierDAO;
    
//    @RequestMapping(value="report/bill/{userId}/{from}/{to}", method=RequestMethod.GET)
//    public ModelAndView downloadBillPdf(@PathVariable("userId") String userId, @PathVariable("from") String from, @PathVariable("to") String to){
//        
//        BillView billView = new BillView();
//        BillRequestFilter filter = new BillRequestFilter();
//        filter.setDateFrom(from);
//        filter.setDateTo(to);
//        
//        billView.setBillRequest(filter);
//        billView.setOrders(orderDAO.getListOrderOfBill(filter));
//        return new ModelAndView("nfc.serviceImpl.report.BillPDFView", "bill", billView);
//    }
    
    @RequestMapping(value="report/bill/{userId}/{from}/{to}", method=RequestMethod.GET)
    public ResponseEntity<?> getPDF(@PathVariable("userId") String userId, @PathVariable("from") String from, @PathVariable("to") String to) throws IOException, DocumentException {
        BillView billView = new BillView();
        BillRequestFilter filter = new BillRequestFilter();
        filter.setUserId(userId);
        filter.setDateFrom(from);
        filter.setDateTo(to);
        billView.setBillRequest(filter);
        billView.setOrders(orderDAO.getListOrderOfBill(filter));
        billView.setBillSupplierInformation(supplierDAO.getBillSupplierInformation(userId));
        // generate the file
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        BillPDFView.getInstance().getPdfBillReport(billView, byteArray);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        String filename = "bill_report.pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(byteArray.toByteArray(), headers, HttpStatus.OK);
        return response;
    }
    
    @RequestMapping(value = "/report/vat", method = RequestMethod.POST)
    public VATReport getListOrderVATReport(@RequestBody BillRequestFilter filter) {
        return orderDAO.getVATReport(filter);
    }
    
    
    @RequestMapping(value="report/vat/{userId}/{from}/{to}", method=RequestMethod.GET)
    public ResponseEntity<?> getVATReportPdf(@PathVariable("userId") String userId, @PathVariable("from") String from, @PathVariable("to") String to) throws IOException, DocumentException {
        List<VATReportInformation> vatReports = new ArrayList<>();
        BillRequestFilter filter = new BillRequestFilter();
        filter.setUserId(userId);
        filter.setDateFrom(from);
        filter.setDateTo(to);
        vatReports = orderDAO.getListVATReportFromOrder(filter);
        // generate the file
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        VATPDFReport.getInstance().getVATPdfReport(vatReports, byteArray);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        String filename = "VAT_Report.pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(byteArray.toByteArray(), headers, HttpStatus.OK);
        return response;
    }
}
