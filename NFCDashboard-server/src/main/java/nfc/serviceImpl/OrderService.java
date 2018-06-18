package nfc.serviceImpl;

import java.io.Serializable;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.ObjectCodec;
import nfc.messages.filters.BillRequestFilter;
import nfc.messages.filters.StatisticRequestFilter;

import nfc.model.Category;
import nfc.model.Customer;
import nfc.model.Order;
import nfc.model.OrderDetail;
import nfc.model.PaymentOrderHistory;
import nfc.model.Supplier;
import nfc.model.SupplierAddress;
import nfc.model.SupplierCategories;
import nfc.model.SupplierUser;
import nfc.model.SupplierWork;
import nfc.model.User;
import nfc.model.Filter;
import nfc.model.UserAddress;
import nfc.model.ViewModel.DeliveryInformation;
import nfc.model.ViewModel.OrderView;
import nfc.model.ViewModel.SupplierAddressView;
import nfc.model.ViewModel.UserAddressView;
import nfc.model.ViewModel.VATReport;
import nfc.model.ViewModel.VATReportInformation;
import nfc.service.IOrderService;
import nfc.service.ISupplierService;
import nfc.service.IUserService;
import nfc.serviceImpl.common.Utils;
import org.hibernate.transform.Transformers;
import org.springframework.util.StringUtils;

public class OrderService implements IOrderService {

    @Autowired
    private IUserService userDAO;
    @Autowired
    private ISupplierService supplierDAO;

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private void insertOrderDetail(List<OrderDetail> lstOrderDetail, String orderIdDesc, Session session) {
        for (OrderDetail orderDetail : lstOrderDetail) {
            orderDetail.setOrder_id(orderIdDesc);
            session.save(orderDetail);
        }
    }

    public boolean insertOrderView(OrderView orderView) {
        
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try
        {
            String orderIdDesc = generateOrderId(session);
            orderView.getOrder().setOrder_id(orderIdDesc);
            setupCustomerInformationForOrder(session, orderView);
            session.save(orderView.getOrder());
            insertOrderDetail(orderView.getLstOrderDetail(), orderIdDesc, session);
            trans.commit();
            return true;
        } catch (Exception ex) {
            System.out.println("Error " + ex.getMessage());
            trans.rollback();
            return false;
        }
    }

    private void setupCustomerInformationForOrder(Session session, OrderView orderView) {
        if (StringUtils.isEmpty(orderView.getOrder().getUser_id())) {
            insertCustomer(session, orderView);
            orderView.getOrder().setCustomer_id(orderView.getCustomer().getCustomer_id());
        }
    }
    
    private void insertCustomer(Session session, OrderView orderView){
        Customer cus = getCustomerExist(session, orderView.getCustomer());
        if(cus == null){
            session.save(orderView.getCustomer());
        }
        else{
            orderView.setCustomer(cus);
        }
    }
    
    private Customer getCustomerExist(Session session, Customer customer){
        Criteria criteria = session.createCriteria(Customer.class);
        criteria.add(Restrictions.eq("customer_phone", customer.getCustomer_phone()));
        criteria.add(Restrictions.eq("customer_email", customer.getCustomer_email()));
        criteria.add(Restrictions.eq("customer_address", customer.getCustomer_address()));
        criteria.add(Restrictions.eq("customer_name", customer.getCustomer_name()));
        Customer cus = (Customer)criteria.uniqueResult();
        return cus;
    }

    public boolean updateOrderView(OrderView orderView) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            session.update(orderView.getOrder());
            for (OrderDetail orderDetail : orderView.getLstOrderDetail()) {
                session.update(orderDetail);
            }
            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        }
    }

    private void deleteReferenceOfSupplier(Session session, String orderId, String table) {
        String deleteQuery = "delete from " + table + " where order_id = '" + orderId + "'";
        Query query = session.createSQLQuery(deleteQuery);
        query.executeUpdate();
    }

    public boolean deleteOrderView(String orderId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            deleteReferenceOfSupplier(session, orderId, "fg_order_details");
            deleteReferenceOfSupplier(session, orderId, "fg_orders");
            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        }
    }

    public List<OrderView> getListOrderViewForPos(String username) {
        // TODO Auto-generated method stub

        List<OrderView> lstOrderForPos = new ArrayList<OrderView>();
        User user = userDAO.findUserByUserName(username);
        List<SupplierUser> lstSupplierUser = supplierDAO.getListSupplierUser(username);
        int supplierId = 0;
        if (lstSupplierUser.size() > 0) {
            supplierId = lstSupplierUser.get(0).getSuppl_id();
        }

        List<Order> orders = getListOrderCurrentDay(supplierId);
        for (Order order : orders) {
            OrderView orderView = new OrderView();
            orderView.setOrder(order);
            orderView.setLstOrderDetail(getListOrderDetail(order.getOrder_id()));
            User cusUser = userDAO.getUser(order.getUser_id());
            //orderView.setCustomer_name(cusUser.getFirst_name() + " " + cusUser.getMiddle_name() + " " + cusUser.getLast_name());
            lstOrderForPos.add(orderView);
        }
        return lstOrderForPos;
    }

    public List<Order> getListOrder(int supplierId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(Order.class);
        criteria.add(Restrictions.eq("suppl_id", supplierId));
        List<Order> orders = (List<Order>) criteria.list();
        trans.commit();
        return orders;
    }

    public String getOrderCount(int supplierId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(Order.class);
        criteria.add(Restrictions.eq("suppl_id", supplierId));
        criteria.setProjection(Projections.rowCount());
        List rowCount = criteria.list();
        String orderCount = rowCount.get(0).toString();
        trans.commit();
        return orderCount;
    }

    private List<Order> getListOrderCurrentDay(int supplierId) {
        java.util.Date date = new java.util.Date();
        Date dateSql = new Date(date.getYear(), date.getMonth(), date.getDate());
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(Order.class);
        criteria.add(Restrictions.eq("suppl_id", supplierId));
        criteria.add(Restrictions.ge("order_date", dateSql));
        List<Order> orders = (List<Order>) criteria.list();
        trans.commit();
        return orders;
    }

    private List<Order> getListOrderSearch(String dateFrom, String dateTo) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date dateF = null;
        try {
            dateF = formatter.parse(dateFrom);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Date dateSqlFrom = new Date(dateF.getYear(), dateF.getMonth(), dateF.getDate());
        java.util.Date dateT = null;
        try {
            dateT = formatter.parse(dateTo);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Date dateSqlTo = new Date(dateT.getYear(), dateT.getMonth(), dateT.getDate());
        System.out.println(dateSqlFrom);
        System.out.println(dateSqlTo);
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(Order.class);
        criteria.add(Restrictions.between("order_date", dateSqlFrom, dateSqlTo));
        List<Order> orders = (List<Order>) criteria.list();
        trans.commit();
        return orders;
    }

    public List<OrderView> getListOrderViewSearch(String dateFrom, String dateTo) {
        List<OrderView> lstOrderForPos = new ArrayList<OrderView>();
        List<Order> orders = getListOrderSearch(dateFrom, dateTo);
        for (Order order : orders) {
            OrderView orderView = new OrderView();
            orderView.setOrder(order);
            orderView.setLstOrderDetail(getListOrderDetail(order.getOrder_id()));
            User cusUser = userDAO.getUser(order.getUser_id());
            //orderView.setCustomer_name(cusUser.getFirst_name() + " " + cusUser.getMiddle_name() + " " + cusUser.getLast_name());
            lstOrderForPos.add(orderView);
        }
        return lstOrderForPos;
    }

    public List<OrderDetail> getListOrderDetail(String orderId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(OrderDetail.class);
        criteria.add(Restrictions.eq("order_id", orderId));
        List<OrderDetail> orderDetail = (List<OrderDetail>) criteria.list();
        trans.commit();
        return orderDetail;
    }

    public Order getOrder(String orderId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Order order = new Order();
        try {
            Criteria criteria = session.createCriteria(Order.class);
            criteria.add(Restrictions.eq("order_id", orderId));
            order = (Order) criteria.uniqueResult();
            trans.commit();
        } catch (Exception ex) {
            trans.rollback();
        }
        return order;
    }

    /**
     * Lucas - Get List Order From SupplierID By Filter date, status (All
     * Information)
     *
     */
    public List<Order> fGetListOrderByFilter(Filter filter) {
        List<Order> lstOrder = new ArrayList<Order>();
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        System.out.println(filter.getStatus());
        for (String status : filter.getStatus()) {
            System.out.println(status);
            for (int supplId : filter.getSupplierId()) {
                System.out.println(supplId);
                String sqlQuery = "SELECT * FROM 82wafoodgo.fg_orders where suppl_id = '" + supplId + "' and order_status = '" + status
                        + "' and order_date >= '" + filter.getFromDate() + "' and order_date <= '" + filter.getToDate() + "';";
                System.out.println(sqlQuery);
                List<Order> lstOrderTemp = new ArrayList<Order>();
                try {
                    Query query = session.createSQLQuery(sqlQuery).addEntity(Order.class);;
                    lstOrderTemp = (List<Order>) query.list();
                    for (Order order : lstOrderTemp) {
                    lstOrder.add(order);
                }
                } catch (Exception ex) {
                    System.out.println("Loi Ne");
                    System.out.println(ex);
                }
            }
        }
        trans.commit();
        return lstOrder;
    }
    public List<Order> fGetListOrderByFilterWithAddress(Filter filter) {
        List<Order> lstOrderTemp = new ArrayList<Order>();
        List<Order> lstOrder = new ArrayList<Order>();
        List<UserAddress> lstUserAddress = new ArrayList<UserAddress>();
        lstUserAddress = userDAO.getListUserByAddress(filter.getAddress());
        lstOrderTemp = fGetListOrderByFilter(filter);
        for (UserAddress userAdd: lstUserAddress) {
            for (Order order: lstOrderTemp) {
                if (userAdd.getUser_id().equalsIgnoreCase(order.getUser_id())) {
                    lstOrder.add(order);
                }
            }
        }
        return lstOrder;
    }    
    
    public List<Order> fGetListOrderByFilterWithPhone(Filter filter) {
        List<Order> lstOrderTemp = new ArrayList<Order>();
        List<Order> lstOrder = new ArrayList<Order>();
        List<User> lstUser = new ArrayList<User>();
        lstUser = userDAO.getListUserByLikePhone(filter.getPhone_num());
        lstOrderTemp = fGetListOrderByFilter(filter);
        for (User user: lstUser) {
            for (Order order: lstOrderTemp) {
                if (user.getUser_id().equalsIgnoreCase(order.getUser_id())) {
                    lstOrder.add(order);
                }
            }
        }
        return lstOrder;
    }
    public List<Order> fGetListOrderByFilterWithPhoneAndAddress(Filter filter) {
        List<Order> lstOrderTemp = new ArrayList<Order>();
        List<Order> lstOrder = new ArrayList<Order>();
        List<User> lstUser = new ArrayList<User>();
        lstUser = userDAO.getListUserByLikePhoneAndAddress(filter.getPhone_num(), filter.getAddress());
        lstOrderTemp = fGetListOrderByFilter(filter);
        for (User user: lstUser) {
            for (Order order: lstOrderTemp) {
                if (user.getUser_id().equalsIgnoreCase(order.getUser_id())) {
                    lstOrder.add(order);
                }
            }
        }
        return lstOrder;
    }
    
    public Order getLastOrder(Session session){
        //Session session = this.sessionFactory.getCurrentSession();
        //Transaction trans = session.beginTransaction();
        Order order = new Order();
        try{
            Query query = session.createSQLQuery("SELECT * FROM 82wafoodgo.fg_orders order by order_id desc limit 1")
                          .addEntity(Order.class);
            order = (Order) query.uniqueResult();
            //trans.commit();
        }
        catch(Exception ex){
            //trans.rollback();
            System.err.println("Error last order " + ex.getMessage());
        }
        return order;
    }
    
    public String generateOrderId(Session session){
        long orderId = -1;
        SimpleDateFormat df = new SimpleDateFormat("yyMMdd");
        String orderIdGenerated = df.format(new java.util.Date());
        Order lastOrder = getLastOrder(session);
        if(lastOrder != null && lastOrder.getOrder_id().substring(0, 6).equals(orderIdGenerated)){
            System.err.println(lastOrder.getOrder_id().substring(0, 6));
            System.err.println(orderIdGenerated);
            orderId = Long.parseLong(lastOrder.getOrder_id().substring(6));
        }
        else{
            orderId = 0;
        }
        orderId = orderId + 1;
        if (orderId < 10) {
            orderIdGenerated = orderIdGenerated + "000000" + orderId;
        } else if (orderId < 100) {
            orderIdGenerated = orderIdGenerated + "00000" + orderId;
        } else if (orderId < 1000) {
            orderIdGenerated = orderIdGenerated + "0000" + orderId;
        } else if (orderId < 10000) {
            orderIdGenerated = orderIdGenerated + "000" + orderId;
        } else if (orderId < 100000) {
            orderIdGenerated = orderIdGenerated + "00" + orderId;
        } else if (orderId < 1000000) {
            orderIdGenerated = orderIdGenerated + "0" + orderId;
        } else {
            orderIdGenerated = orderIdGenerated + orderId;
        }
        return orderIdGenerated;
    }

    public boolean savePaymentOrderHistory(PaymentOrderHistory paymentOrderHistory) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            session.save(paymentOrderHistory);
            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        }
    }

    public boolean updateOrderStatus(String orderId, String status) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            String deleteQuery = "update fg_orders set order_status = '" + status + "' where order_id = '" + orderId + "'";
            Query query = session.createSQLQuery(deleteQuery);
            query.executeUpdate();
            trans.commit();
            return true;
        } catch (Exception ex) {
            trans.rollback();
            return false;
        }
    }

    public PaymentOrderHistory getPaymentOrderHistory(String orderId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        PaymentOrderHistory paymentOrderHistory = new PaymentOrderHistory();
        try {
            Criteria criteria = session.createCriteria(PaymentOrderHistory.class);
            criteria.add(Restrictions.eq("order_id", orderId));
            paymentOrderHistory = (PaymentOrderHistory) criteria.list();
            trans.commit();
        } catch (Exception ex) {
            trans.rollback();
        }
        return paymentOrderHistory;
    }

//    public List<Order> getListOrderAllStoreOfUser(String userId) {
//        Session session = this.sessionFactory.getCurrentSession();
//        Transaction trans = session.beginTransaction();
//        List<Order> orders = new ArrayList<>();
//        try {
//            Query query = session.createSQLQuery("select o.*, s.supplier_name from fg_orders o inner join fg_supplier_users su on o.suppl_id = su.suppl_id inner join fg_suppliers s on s.suppl_id = su.suppl_id where su.user_id='" + userId + "' and order_date >= current_date() order by o.order_date desc")
//                    .setResultTransformer(Transformers.aliasToBean(Order.class));
//            orders = (List<Order>) query.list();
//            trans.commit();
//        } catch (Exception ex) {
//            System.err.println("error " + ex.getMessage());
//            trans.rollback();
//        }
//        return orders;
//    }

    /*Lucas -  get list all order by supplierID
     */
    public List<OrderView> getListOrderBySupplierID(String username) {
        List<OrderView> lstOrderView = new ArrayList<OrderView>();

        return lstOrderView;
    }
    
    public List<Order> getListOrderOfStatisticRequest(StatisticRequestFilter filter){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<Order> orders = new ArrayList<>();
        try{
            Query query = session.createSQLQuery("select * from fg_orders where find_in_set(suppl_id,'" + filter.getStoreIds() + "') and order_date >= '" + Utils.convertDateToString(filter.getDateFrom())+ "' and order_date <= '" + Utils.convertDateToString(filter.getDateTo())+ "' and (order_status = 'CANCEL' or order_status = 'COMPLETE') order by order_date")
                            .setResultTransformer(Transformers.aliasToBean(Order.class));
            orders = (List<Order>) query.list();
            trans.commit();
        }
        catch(Exception ex){
            System.err.println("error " + ex.getMessage());
            trans.rollback();
        }
        return orders;
    }
    
    
    public List<Order> getListOrderOfBill(BillRequestFilter filter){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<Order> orders = new ArrayList<>();
        try{
            Query query = session.createSQLQuery("select o.*, s.supplier_name from fg_orders o inner join fg_supplier_users su on o.suppl_id = su.suppl_id inner join fg_suppliers s on s.suppl_id = su.suppl_id where su.user_id = '" + filter.getUserId() + "' and order_date >= '" + filter.getDateFrom()+ "' and order_date <= '" + filter.getDateTo()+ "' and order_status = 'COMPLETE'")
                            .setResultTransformer(Transformers.aliasToBean(Order.class));
            orders = (List<Order>) query.list();
            trans.commit();
        }
        catch(Exception ex){
            System.err.println("error " + ex.getMessage());
            trans.rollback();
        }
        return orders;
    }
    
    public VATReport getVATReport(BillRequestFilter filter){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        VATReport report = new VATReport();
        try{
            Query query = session.createSQLQuery("select IFNULL(sum(o.prod_amt), 0) as prod_amt, IFNULL(sum(o.tax_amt), 0) as tax_amt from fg_orders o inner join fg_supplier_users su on o.suppl_id = su.suppl_id where su.user_id = '" + filter.getUserId() + "' and order_date >= '" + filter.getDateFrom()+ "' and order_date <= '" + filter.getDateTo()+ "' and order_status = 'COMPLETE'")
                            .setResultTransformer(Transformers.aliasToBean(VATReport.class));
            report = (VATReport) query.uniqueResult();
            trans.commit();
        }
        catch(Exception ex){
            System.err.println("error " + ex.getMessage());
            trans.rollback();
        }
        return report;
    }
    
    public List<VATReportInformation> getListVATReportFromOrder(BillRequestFilter filter){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<VATReportInformation> vatReports = new ArrayList<>();
        try{
            Query query = session.createSQLQuery("select o.order_date, o.prod_amt, o.disc_amt, s.supplier_name, (select a.address from fg_user_address ud join fg_address a on ud.addr_id = a.addr_id where ud.user_id = o.user_id and ud.is_deliver = 1 limit 1) as  user_address, (select c.customer_address from fg_customers c where c.customer_id = o.customer_id) as customer_address from fg_orders o inner join fg_supplier_users su on o.suppl_id = su.suppl_id inner join fg_suppliers s on s.suppl_id = su.suppl_id where su.user_id = '" + filter.getUserId() + "' and order_date >= '" + filter.getDateFrom()+ "' and order_date <= '" + filter.getDateTo()+ "' and order_status = 'COMPLETE'")
                            .setResultTransformer(Transformers.aliasToBean(VATReportInformation.class));
            vatReports = (List<VATReportInformation>) query.list();
            trans.commit();
        }
        catch(Exception ex){
            System.err.println("error " + ex.getMessage());
            trans.rollback();
        }
        return vatReports;
    }
    
    public DeliveryInformation getDeliveryInformation(String orderId){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        DeliveryInformation deliveryInfor = new DeliveryInformation();
        try {
            Query query = session.createSQLQuery("CALL SP_GetDeliveryInformation(:orderId)")
                    .setParameter("orderId", orderId)
                    .setResultTransformer(Transformers.aliasToBean(DeliveryInformation.class));
            deliveryInfor = (DeliveryInformation) query.uniqueResult();
            trans.commit();
        } catch (Exception ex) {
            System.err.println("error " + ex.getMessage());
            trans.rollback();
        }
        return deliveryInfor;
    }
    
    public Order getOrderFromPaymentKey(String paymentKey){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Order order = new Order();
        try{
            Query query = session.createSQLQuery("select o.* from fg_orders o join fg_payment_order_history p on o.order_id = p.order_id where p.payment_unique_number = '" + paymentKey + "';")
                          .addEntity(Order.class);
            order = (Order) query.uniqueResult();
            trans.commit();
        }
        catch(Exception ex){
            trans.rollback();
            System.err.println("Error last order " + ex.getMessage());
        }
        return order;
    }
}
