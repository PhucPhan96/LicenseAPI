package nfc.service;
import java.util.List;
import nfc.messages.filters.BillRequestFilter;
import nfc.messages.filters.StatisticRequestFilter;
import nfc.model.Filter;

import nfc.model.Order;
import nfc.model.OrderDetail;
import nfc.model.PaymentOrderHistory;
import nfc.model.ViewModel.DeliveryInformation;
import nfc.model.ViewModel.OrderView;
import nfc.model.ViewModel.VATReport;
import nfc.model.ViewModel.VATReportInformation;

public interface IOrderService {
	boolean insertOrderView(OrderView orderView);
	boolean updateOrderView(OrderView orderView);
	boolean deleteOrderView(String orderId);
	List<OrderView> getListOrderViewForPos(String username);
	List<OrderDetail> getListOrderDetail(String orderId);
	List<Order> getListOrder(int supplierId);
	Order getOrder(String orderId);
	List<OrderView> getListOrderViewSearch(String dateFrom, String dateTo);
	String getOrderCount(int supplierId);
        //Order getLastOrder();
        boolean savePaymentOrderHistory(PaymentOrderHistory paymentOrderHistory);
        boolean updateOrderStatus(String orderId, String status);
        PaymentOrderHistory getPaymentOrderHistory(String orderId);
        //List<Order> getListOrderAllStoreOfUser(String userId);
        List<Order> fGetListOrderByFilter(Filter filter);
        List<Order> fGetListOrderByFilterWithPhoneAndAddress(Filter filter);
        List<Order> fGetListOrderByFilterWithPhone(Filter filter);
        List<Order> fGetListOrderByFilterWithAddress(Filter filter);
        List<Order> getListOrderOfStatisticRequest(StatisticRequestFilter filter);
        List<Order> getListOrderOfBill(BillRequestFilter filter);
        VATReport getVATReport(BillRequestFilter filter);
        List<VATReportInformation> getListVATReportFromOrder(BillRequestFilter filter);
        DeliveryInformation getDeliveryInformation(String orderId);
        Order getOrderFromPaymentKey(String paymentKey);
}
