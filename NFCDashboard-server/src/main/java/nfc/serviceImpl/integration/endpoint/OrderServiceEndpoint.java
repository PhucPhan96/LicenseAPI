package nfc.serviceImpl.integration.endpoint;

import java.math.BigDecimal;
import java.util.Date;
import nfc.messages.OrderStatusCustomerMessage;
import nfc.messages.OrderStatusMessage;
import nfc.messages.request.OrderStatusRequest;
import nfc.messages.request.PaymentCancel;
import nfc.messages.base.BasePacket;
import nfc.messages.base.DeliveryRequestPacket;
import nfc.messages.request.Delivery82WaRequest;
import nfc.model.Discount;
import nfc.model.Order;
import nfc.model.PaymentOrderHistory;
import nfc.model.ViewModel.DeliveryInformation;
import org.springframework.beans.factory.annotation.Autowired;

import nfc.model.ViewModel.OrderView;
import nfc.service.IDiscountService;
import nfc.service.IOrderService;
import nfc.service.IUserService;
import nfc.serviceImpl.common.PushNotification;
import nfc.serviceImpl.common.SpeedPayInformation;
import nfc.serviceImpl.common.Utils;
import nfc.serviceImpl.delivery.DeliveryFactory;
import nfc.serviceImpl.payment.PaymentFactory;
import nfc.socket.CustomerDataQueue;
import nfc.socket.DataQueue;
import org.json.simple.JSONObject;

public class OrderServiceEndpoint {
	@Autowired
	private IOrderService orderDAO;
//        @Autowired
//        private IUserService userDAO;
        @Autowired
        private IDiscountService discountDAO;
        
	public OrderView saveOrder(OrderView orderView)
	{
            setupOderView(orderView);
            if(!orderDAO.insertOrderView(orderView)){
                orderView.getOrder().setOrder_status(Utils.ORDER_FAILED);
            }
            return orderView;
	}
        
        private void setupOderView(OrderView orderView){
            Order order  = orderView.getOrder();
            setDiscountOrder(order);
            setOnlineFeeOrder(order);
            setTaxFee(order);
            setCouponFee(order);
            setDeliveryFee(order);
            calculatorOrderAmount(order);
            setDateForOrder(order);
        }
        
        private void setDiscountOrder(Order order){
            Discount discount = discountDAO.getDiscountOfStore(order.getSuppl_id());
            System.err.println(discount);
            BigDecimal valueDiscount = BigDecimal.ZERO;
            if(discount != null){
                if(discount.getDiscount_value().contains("%")){
                    valueDiscount = order.getProd_amt().multiply(BigDecimal.valueOf(Integer.parseInt(discount.getDiscount_value().substring(0, discount.getDiscount_value().lastIndexOf("%")))))
                                                        .divide(BigDecimal.valueOf(100));
                }
                else {
                    valueDiscount = order.getProd_amt().subtract(BigDecimal.valueOf(Integer.parseInt(discount.getDiscount_value())));
                }
                
            }
            order.setDisc_amt(valueDiscount);
        }
        
        private void setOnlineFeeOrder(Order order){
            order.setOnline_fee(order.getProd_amt().multiply(BigDecimal.valueOf(3)).divide(BigDecimal.valueOf(100)));
        }
        
        private void setTaxFee(Order order){
            order.setTax_amt(order.getOnline_fee().multiply(BigDecimal.valueOf(10)).divide(BigDecimal.valueOf(100)));
        }
        
        private void setCouponFee(Order order){
            order.setDiscount_coupon(BigDecimal.ZERO);
        }
        
        private void setDeliveryFee(Order order){
            order.setDeliver_amt(BigDecimal.ZERO);
        }
        
        private void calculatorOrderAmount(Order order){
            order.setOrder_amt(order.getProd_amt().subtract(order.getOnline_fee())
                                                  .subtract(order.getTax_amt())
                                                  .subtract(order.getDeliver_amt())
                                                  .subtract(order.getDisc_amt())
                                                  .add(order.getDiscount_coupon()));
        }
        
        private void setDateForOrder(Order order){
            order.setOrder_date(new Date());
            order.setDeliver_date(new Date());
            order.setRequired_date(new Date());
        }
        
        
	public OrderView updateOrder(OrderView orderView){
            //orderDAO.updateOrderView(orderView);
            return orderView;
	}
        
        public OrderStatusRequest updateStatusOrder(OrderStatusRequest orderStatusRequest){
            if(!orderStatusRequest.getStatus().equals(Utils.ORDER_CANCEL)){
                if(!orderDAO.updateOrderStatus(orderStatusRequest.getOrderId(), orderStatusRequest.getStatus())){
                    orderStatusRequest.setStatus(Utils.ORDER_FAILED);
                }
            }
            return orderStatusRequest;
        }
        
        public OrderStatusRequest orderComplete(OrderStatusRequest orderStatusRequest){
            //send data to delivery system
            sendOrderToDeliverySystem(orderStatusRequest);
            sendOrderStatusToStore(orderStatusRequest, BasePacket.PacketType.COMPLETE_ORDER);
            pushNotificationToCustomer(orderStatusRequest);
            return orderStatusRequest;
        }
        
        public OrderStatusRequest orderCancel(OrderStatusRequest orderStatusRequest){
            if(cancelOrder(orderStatusRequest)){
                orderDAO.updateOrderStatus(orderStatusRequest.getOrderId(), Utils.ORDER_CANCEL);
                pushNotificationToCustomer(orderStatusRequest);
                //sendDataToCustomer(orderStatusRequest, BasePacket.PacketType.CANCEL_ORDER);
                sendOrderStatusToStore(orderStatusRequest, BasePacket.PacketType.CANCEL_ORDER);
            }
            else{
                orderStatusRequest.setStatus(Utils.ORDER_FAILED);
            }
            return orderStatusRequest;
            
        }
        
        public OrderStatusRequest orderRequestCancel(OrderStatusRequest orderStatusRequest){
            sendOrderStatusToStore(orderStatusRequest, BasePacket.PacketType.REQUEST_CANCEL_ORDER);
            return orderStatusRequest;
        }
        
        public OrderStatusRequest orderCooking(OrderStatusRequest orderStatusRequest){
            pushNotificationToCustomer(orderStatusRequest);
            //sendDataToCustomer(orderStatusRequest, BasePacket.PacketType.COOKING_ORDER);
            sendOrderStatusToStore(orderStatusRequest, BasePacket.PacketType.COOKING_ORDER);
            return orderStatusRequest;
        }
        
        
        private void sendOrderStatusToStore(OrderStatusRequest orderStatusRequest, BasePacket.PacketType type){
            OrderStatusMessage orderStatusMessage = new OrderStatusMessage(orderStatusRequest.getUserIdStore(), type);
            orderStatusMessage.setStoreId(orderStatusRequest.getStoreId());
            orderStatusMessage.setOrderId(orderStatusRequest.getOrderId());
            orderStatusMessage.setStatus(orderStatusRequest.getStatus());
            DataQueue.getInstance().addDataQueue(orderStatusMessage);
        }
        
        private void sendDataToCustomer(OrderStatusRequest orderStatusRequest, BasePacket.PacketType type){
            OrderStatusCustomerMessage orderStatusCustomerMessage = new OrderStatusCustomerMessage(orderStatusRequest.getUuid(), type);
            orderStatusCustomerMessage.setOrderId(orderStatusRequest.getOrderId());
            CustomerDataQueue.getInstance().addDataQueue(orderStatusCustomerMessage);
        }
        
        
        private void pushNotificationToCustomer(OrderStatusRequest orderStatusRequest){
            
            PushNotification.getInstance().pushNotification(orderStatusRequest.getUuid(), "Information", getMessageOfOrderStatus(orderStatusRequest.getStatus()));
        }
        
        private String getMessageOfOrderStatus(String status){
            String message = "";
            if(status == Utils.ORDER_CANCEL){
                return "Order cancel";
            }
            else if(status == Utils.ORDER_COMPLETE || status == Utils.ORDER_DELIVERY_FAILED){
                return "Order complete";
            }
            else if(status == Utils.ORDER_DELIVERY_SUCCESS){
                return "Order delivering";
            }
            else if(status == Utils.ORDER_COOKING){
                return "Order cooking";
            }
            else if(status == Utils.ORDER_FAILED){
                return "Order failed";
            }
            return message;
        }
        
        private boolean cancelOrder(OrderStatusRequest orderStatusRequest){
            try{
                PaymentOrderHistory paymentHistory = orderDAO.getPaymentOrderHistory(orderStatusRequest.getOrderId());
                PaymentCancel speedPayCancel = new PaymentCancel();
                speedPayCancel.setId(paymentHistory.getPayment_unique_number());
                return PaymentFactory.getPaymentApi(orderStatusRequest.getPayment_code()).cancel(speedPayCancel);
            }
            catch(Exception ex){
                return false;
            } 
        }
        
        
        private void sendOrderToDeliverySystem(OrderStatusRequest request){
            DeliveryInformation delivery = getDeliveryInformation(request.getOrderId());
            if(isStoreNonDelivery(delivery)){
                if(sendDelivery(delivery)){
                    request.setStatus(Utils.ORDER_DELIVERY_SUCCESS);
                }
                else{
                    request.setStatus(Utils.ORDER_DELIVERY_FAILED);
                }
            }
        }
        
        private boolean isStoreNonDelivery(DeliveryInformation delivery){
            if(delivery.getDelivery_id().equals("DELIVERIED"))
                return false;
            return true;
        }
        
        private DeliveryInformation getDeliveryInformation(String orderId){
            return orderDAO.getDeliveryInformation(orderId);
        }
        
        private boolean sendDelivery(DeliveryInformation delivery){
            JSONObject response = DeliveryFactory.getDeliveryApi(delivery.getDelivery_id()).order(initDeliveryRequest(delivery));
            return isSendSuccess(response);
        }
        
        private DeliveryRequestPacket initDeliveryRequest(DeliveryInformation delivery){
            DeliveryRequestPacket request = new DeliveryRequestPacket();
            request.setCust_tel(delivery.getDelivery_phone());
            request.setPrice(delivery.getOrder_amt().longValue());
            request.setReach_addr(delivery.getDelivery_addr());
            return request; 
        }
        
        private boolean isSendSuccess(JSONObject response){
            if(response.get("result").equals("OK"))
                return true;
            return false;
        }
}
