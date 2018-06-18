package nfc.controller;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import nfc.messages.BaseResponse;
import nfc.messages.OrderMessage;
import nfc.messages.OrderStatusMessage;
import nfc.messages.request.OrderStatusRequest;
import nfc.messages.base.BasePacket;
import nfc.messages.base.PaymentRequestPacket;
import nfc.messages.filters.BillRequestFilter;
import nfc.messages.filters.StatisticRequestFilter;
import nfc.messages.request.PayRequest;
import nfc.messages.request.PaymentCancel;

import nfc.model.Code;
import nfc.model.Group;
import nfc.model.Order;
import nfc.model.OrderDetail;
import nfc.model.ViewModel.OrderView;
import nfc.model.ViewModel.PosDetailView;
import nfc.model.ViewModel.SupplierView;
import nfc.model.Filter;
import nfc.model.ViewModel.GridView;
import nfc.service.ICodeService;
import nfc.service.IOrderService;
import nfc.service.IPosService;
import nfc.serviceImpl.Security.JwtTokenUtil;
import nfc.serviceImpl.common.PushNotification;
import nfc.serviceImpl.common.SpeedPayInformation;
import nfc.serviceImpl.common.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import nfc.serviceImpl.integration.OrderGateway;
import nfc.serviceImpl.integration.OrderStatusGateway;
import nfc.serviceImpl.payment.PaymentFactory;
import nfc.socket.DataQueue;
import org.json.simple.JSONObject;


@RestController
//@Configuration
//@ComponentScan
//@EnableIntegration
public class OrderManagementController {
    
    @Value("Authorization")
    private String tokenHeader;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    OrderGateway orderGateway;
    @Autowired
    OrderStatusGateway orderProcessGateway;
    
    @Autowired
    IOrderService orderDAO;
    @Autowired
    IPosService posDAO;
    @Autowired
    private ICodeService codeDAO;
//    @Autowired 
//    private SocketIntegrationService integrationService;    
//    public OrderManagementController(){
//        new DataQueue();
//    }
    
//    @Bean
//    public ServerWebSocketContainer serverWebSocketContainer() {
//            return new ServerWebSocketContainer("/notify").setAllowedOrigins("*").withSockJs();
//            //return new ServerWebSocketContainer.SockJsServiceOptions().setHeartbeatTime(60_000)
//    }
//    
//    @Bean(name = "webSocketFlow.input")
//    MessageChannel sendToStore() {
//        return new DirectChannel();
//    }
//    
//    @Bean
//    public ServerWebSocketContainer serverWebSocketContainerCustomer() {
//            return new ServerWebSocketContainer("/customer").setAllowedOrigins("*").withSockJs();
//            //return new ServerWebSocketContainer.SockJsServiceOptions().setHeartbeatTime(60_000)
//    }
//        
//    @Bean(name = "webSocketFlowCustomer.input")
//    MessageChannel sendToCustomer() {
//        return new DirectChannel();
//    }
//    
//    MessageChannel prepareSendToStore;
//    @Bean
//    MessageHandler webSocketOutboundAdapter() {
//        return new WebSocketOutboundMessageHandler(serverWebSocketContainer());
//    }
//    
//    @Bean
//    MessageHandler webSocketOutboundAdapterCustomer() {
//        return new WebSocketOutboundMessageHandler(serverWebSocketContainerCustomer());
//    }
//    
//    @Bean
//    IntegrationFlow webSocketFlow() {
//        return f -> {
//            Function<Message , Object> splitter = m -> serverWebSocketContainer()
//                    .getSessions()
//                    .keySet()
//                    .stream()
//                    .map(s -> MessageBuilder.fromMessage(m)
//                            .setHeader(SimpMessageHeaderAccessor.SESSION_ID_HEADER, s)
//                            .build())
//                    .collect(Collectors.toList());
//            f.split( Message.class, splitter)
//                    .channel(c -> c.executor(Executors.newCachedThreadPool()))
//                    .handle(webSocketOutboundAdapter());
//        };
//    }
//    
//    @Bean
//    IntegrationFlow webSocketFlowCustomer() {
//        return f -> {
//            Function<Message , Object> splitter = m -> serverWebSocketContainerCustomer()
//                    .getSessions()
//                    .keySet()
//                    .stream()
//                   .map(s -> MessageBuilder.fromMessage(m)
//                             .setHeader(SimpMessageHeaderAccessor.SESSION_ID_HEADER, s)
//                            .build())
//                    .collect(Collectors.toList());
//            f.split( Message.class, splitter)
//                    .channel(c -> c.executor(Executors.newCachedThreadPool()))
//                    .handle(webSocketOutboundAdapterCustomer());
//        };
//    }
    
    
    @RequestMapping(value="order/pos",method=RequestMethod.GET)
	public List<OrderView> getListOrderPosView(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        List<OrderView>  lstOrderView = orderDAO.getListOrderViewForPos(username);
        return lstOrderView;
    }
        
    @RequestMapping(value="pos/detail/{id}",method=RequestMethod.GET)
    public PosDetailView getListOrderPosDetailView(@PathVariable("id") String orderId){
        PosDetailView  posDetailView = posDAO.getPosDetailView(orderId);
        return posDetailView;
    }
        
//    @RequestMapping(value="orders/stores/{userId}",method=RequestMethod.GET)
//    public List<Order> getListOrderOfStoresUser(@PathVariable("userId") String userId){
//        return orderDAO.getListOrderAllStoreOfUser(userId);
//    }
    
    @RequestMapping(value="orders/stores/user",method=RequestMethod.POST)
    public GridView getListOrderOfStoresUser(@RequestBody GridView gridData){
        return posDAO.getListOrderAllStoreOfUser(gridData);
    }
        
        
    @RequestMapping(value="order/search",method=RequestMethod.POST)
	public @ResponseBody List<OrderView> getListOrderSearch(@RequestBody Map<String,String> data){
    	List<OrderView>  lstOrderView = orderDAO.getListOrderViewSearch(data.get("dateFrom"), data.get("dateTo"));
    	return lstOrderView;
    }
        
    @RequestMapping(value="order/delete/{id}", method=RequestMethod.DELETE)
    public @ResponseBody String deleteRole(@PathVariable("id") String orderId){
        String data = orderDAO.deleteOrderView(orderId) + "";
        return "{\"result\":\"" + data + "\"}";
    }
    
    @RequestMapping(value="app/orderCount/{id}", method=RequestMethod.GET)
    public @ResponseBody String getOrderCount(@PathVariable("id") int supplierId){
        String data = orderDAO.getOrderCount(supplierId);
        return "{\"result\":\"" + data + "\"}";
    }
    
    
    @RequestMapping(value="/order/customer/test", method = RequestMethod.GET)
    public String sendTest() {
    	OrderView orderView = new OrderView();
        Order order = new Order();
        order.setSuppl_id(1111);
        orderView.setOrder(order);
        orderGateway.sendOrder(orderView);
        return orderGateway.receive().getOrder().getSuppl_id() + "";
    }
    //Lucas
    @RequestMapping(value = "app/order/getlistorderbyfilter", method = RequestMethod.POST)
    public List<Order> getListOrderByFilter(@RequestBody Filter filter) {
        List<Order> lstOrder = new ArrayList<Order>();
        if (filter.getAddress().equalsIgnoreCase("") && filter.getPhone_num().equalsIgnoreCase("")) {
            lstOrder = orderDAO.fGetListOrderByFilter(filter);
            return lstOrder;
        }
        else{
            if (filter.getAddress().equalsIgnoreCase("")) {
                lstOrder = orderDAO.fGetListOrderByFilterWithPhone(filter);
                return lstOrder;
            } else if (filter.getPhone_num().equalsIgnoreCase("")){
                lstOrder = orderDAO.fGetListOrderByFilterWithAddress(filter);
                return lstOrder;
            } else{
                lstOrder = orderDAO.fGetListOrderByFilterWithPhoneAndAddress(filter);
                return lstOrder;
            }
        }
    }
    
    
    @RequestMapping(value="/order/customer", method = RequestMethod.POST)
    public BaseResponse customerOrder(@RequestBody OrderView orderView) {
        BaseResponse baseResponse = new BaseResponse();
        orderGateway.sendOrder(orderView);
        Order orderReceive = orderGateway.receive().getOrder();
        baseResponse.setResponse_data(orderReceive);
        if(orderReceive.getOrder_status().equals(Utils.ORDER_FAILED)){
            baseResponse.resultCode = BaseResponse.FAILED;
        }
        else{
            baseResponse.resultCode = BaseResponse.OK;
        }
        return baseResponse;
    }
    
    @RequestMapping(value="/order/update/request", method = RequestMethod.POST)
    public BaseResponse send(@RequestBody OrderStatusRequest orderStatusRequest) {
        BaseResponse baseResponse = new BaseResponse();
        orderProcessGateway.sendOrderMessage(orderStatusRequest);
        if(orderProcessGateway.receive().getStatus().equals(Utils.ORDER_FAILED)){
            baseResponse.resultCode = BaseResponse.FAILED;
        }
        else{
            baseResponse.resultCode = BaseResponse.OK;
        }
        return baseResponse;
    }
    
//    @RequestMapping(value="/order/payment/test", method = RequestMethod.GET)
//    public String paymentTest() {
//    	PayRequest speedPayRequest = new PayRequest();
//        speedPayRequest.setAmt("1000");
//        speedPayRequest.setCard_no("5562456078580705");
//        //speedPayRequest.setCard_serial("6267");
//        speedPayRequest.setCard_ymd("20170817");
//        speedPayRequest.setSell_nm("sadsad");
//        speedPayRequest.setProduct_nm("aaaa");
//        speedPayRequest.setBuyer_email("aa@gmail.com");
//        speedPayRequest.setBuyer_nm("chongsongyong");
//        speedPayRequest.setBuyer_phone_no("01023134519");
//        JSONObject resultPayment = PaymentFactory.getPaymentApi("SPEEDPAY").payment(speedPayRequest);
//        return resultPayment.toJSONString();
//    }
    
    @RequestMapping(value="/order/send/store/test", method = RequestMethod.GET)
    public String sendOrderToStoreTest() {
        String data = "10%";
        return data.substring(0, data.lastIndexOf("%"));
//        Order order = new Order();
//        order.setSupplier_name("Store Example");
//        order.setOrder_amt(BigDecimal.valueOf(1000));
//        order.setOrder_date(new java.util.Date());
//        order.setOrder_id("2017081140000001");
//        order.setOrder_status(Utils.ORDER_PAID);
//        order.setProd_amt(BigDecimal.valueOf(900));
//        order.setTax_amt(BigDecimal.valueOf(12));
//        order.setDisc_amt(BigDecimal.valueOf(11));
//    	OrderMessage orderMessage = new OrderMessage("e56f5a26-2272-410d-9713-4e4a54093d88");
//        //orderMessage.setCustomer_name("Nguyen Van A");
//        orderMessage.setOrder(order);
//        DataQueue.getInstance().addDataQueue(orderMessage);
//        return "Okie";
    }
    
    @RequestMapping(value="/order/send/status/test", method = RequestMethod.GET)
    public String sendOrderStatusToStoreTest() {
        OrderStatusMessage orderStatusMessage = new OrderStatusMessage("e56f5a26-2272-410d-9713-4e4a54093d88", BasePacket.PacketType.COMPLETE_ORDER);
        orderStatusMessage.setStoreId(35);
        orderStatusMessage.setOrderId("2017081140000001");
        orderStatusMessage.setStatus(Utils.ORDER_COMPLETE);
        DataQueue.getInstance().addDataQueue(orderStatusMessage);
        DataQueue.getInstance().addDataQueue(orderStatusMessage);
        return "Okie";
    }
    
    
    @RequestMapping(value="/order/send/notification", method = RequestMethod.GET)
    public void sendNotificationTest() {
        //PushNotification.getInstance().pushNotification("cKCaijPhT4k:APA91bEVcvLFglLFextfO3R-CWXvbzWyZTAX2ZBvdCGGn6UUEzQaNlb4RGzdaag1QlQUDWmFUlkTBDoI9KSWupu9eP3xXcVPLz6rGKKDhjaZsMlpIgxSPPlGLyZ0qIshU8V47rnO4nwE", "Hello", "from nfc");
        PushNotification.getInstance().pushNotification("cHH12tE-6d8:APA91bFcrtBEuMJHqfu_0ajZ2A8vI7P-LjxR7hjjXw8n50mTvLsS_LSUUREPWKOdK8ecxonXfsAoaAxycw8FGzEpJarlVUTyqszjlnsnoS2dNAY1p8dbNaK60nl2ChZhDqJPc3h1npL6", "Hello", "from nfc");
        
        
    }
    
    @RequestMapping(value = "/order/statistic", method = RequestMethod.POST)
    public List<Order> getListOrderByFilter(@RequestBody StatisticRequestFilter filter) {
        List<Order> lstOrder = new ArrayList<Order>();
        lstOrder = orderDAO.getListOrderOfStatisticRequest(filter);
        return lstOrder;
    }
    
    
    
    @RequestMapping(value = "/order/lgpay", method = RequestMethod.GET)
    public String lgPaymentTest() {
        LinkedHashMap<String, String> paymentRequest = new LinkedHashMap<>();
        paymentRequest.put("LGD_PAYKEY", "sdafsdaf3423k2345234");
        return PaymentFactory.getPaymentApi("LGPAYNOW").payment(paymentRequest, "1709200000010") + "";
    }
    
}
