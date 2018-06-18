package nfc.serviceImpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import nfc.model.Customer;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import nfc.model.Order;
import nfc.model.OrderDetail;
import nfc.model.PaymentOrderHistory;
import nfc.model.Text;
import nfc.model.User;
import nfc.model.UserAddress;
import nfc.model.ViewModel.GridView;
import nfc.model.ViewModel.OrderDetailView;
import nfc.model.ViewModel.OrderDetailViewModel;
import nfc.model.ViewModel.OrderView;
import nfc.model.ViewModel.PosDetailView;
import nfc.model.ViewModel.UserAddressView;
import nfc.service.IOrderService;
import nfc.service.IPosService;
import nfc.service.IProductService;
import nfc.service.ISupplierService;
import nfc.service.IUserService;
import nfc.serviceImpl.common.Utils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.util.StringUtils;

public class PosService implements IPosService{
    
	@Autowired
	private IOrderService orderDAO;
	@Autowired
	private IProductService productDAO;
	@Autowired
	private IUserService userDAO;
	@Autowired
	private ISupplierService supplierDAO;
        
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public PosDetailView getPosDetailView(String orderId) {
            PosDetailView posDetailView = new PosDetailView();
            Order order = orderDAO.getOrder(orderId);
            if(order != null){
                posDetailView.setOrder(order);
                Customer customer = new Customer();
                if(StringUtils.isEmpty(order.getUser_id())){
                    //get from customer
                    customer = getCustomer(order.getCustomer_id());
                }
                else{
                    User user = userDAO.getUser(order.getUser_id());
                    customer.setCustomer_name(getNameOfUser(user));
                    customer.setCustomer_phone(user.getPhone_no());
                    customer.setCustomer_email(user.getEmail());
                    customer.setCustomer_address(getAddressOfUser(user));

                }
                posDetailView.setCustomer(customer);
                posDetailView.setListOrderDetailView(getListDetailViewModel(order.getOrder_id()));
                posDetailView.setPayment_code(getPaymentCode(order.getOrder_id()));

            }
            return posDetailView;
	}
        
        private String getPaymentCode(String orderId){
           PaymentOrderHistory orderHistory = orderDAO.getPaymentOrderHistory(orderId);
           if(orderHistory != null){
               return orderHistory.getPayment_code();
           }
           else{
               return "";
           }
        }
        
    
    private String getAddressOfUser(User user){
        String address = "";
        for(UserAddressView userAddressView: user.getLstuserAddress()){
            if(userAddressView.isIs_deliver()){
                address = userAddressView.getAddressOfUser().getAddress();
                return address;
            }
            address = userAddressView.getAddressOfUser().getAddress();
        }
        return address;
    }
    
    private String getNameOfUser(User user){
        return (StringUtils.isEmpty(user.getFirst_name())==true ? "" : user.getFirst_name()) 
                + " " + (StringUtils.isEmpty(user.getMiddle_name())==true ? "" : user.getMiddle_name())
                + " " + (StringUtils.isEmpty(user.getLast_name())==true ? "" : user.getLast_name());
    }
    
    private Customer getCustomer(String customerId){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Customer customer = new Customer();
        try{
            Criteria criteria = session.createCriteria(Customer.class);
            criteria.add(Restrictions.eq("customer_id", customerId));
            customer = (Customer) criteria.uniqueResult();
            trans.commit();
        }
        catch(Exception ex){
            trans.rollback();
        }
        return customer;
    }
    
    private List<OrderDetailViewModel> getListDetailViewModel(String orderId){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<OrderDetailViewModel> listOrderDetail = new ArrayList<>();
        try{
            Query query = session.createSQLQuery("select od.*, p.prod_name from fg_order_details od inner join fg_products p on od.prod_id = p.prod_id where od.order_id = '" + orderId + "'")
                                  .setResultTransformer(Transformers.aliasToBean(OrderDetailViewModel.class));
            listOrderDetail = (List<OrderDetailViewModel>) query.list();
            trans.commit();
        }
        catch(Exception ex){
            System.err.println("error " + ex.getMessage());
            trans.rollback();
        }
        return listOrderDetail;
    }
    
    public GridView getListOrderAllStoreOfUser(GridView gridData){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try {
            StringBuilder filterBuild = Utils.generateGridFilterString(gridData);
            String filter = filterBuild.toString();
            LinkedHashMap<String, String> data = (LinkedHashMap<String, String>) gridData.getData();
            Query query = session.createSQLQuery("select o.*, s.supplier_name from fg_orders o inner join fg_supplier_users su on o.suppl_id = su.suppl_id inner join fg_suppliers s on s.suppl_id = su.suppl_id where su.user_id='" + data.get("userId") + "' and order_status='" + data.get("status") + "' and order_date >= current_date() order by o.order_date desc" + (filter.equals("")  ? "" :  (" where " + filter)) + " limit " + gridData.getPageSize() + " offset " + ((gridData.getPageIndex() - 1) * gridData.getPageSize()))
                    .setResultTransformer(Transformers.aliasToBean(Order.class));
            List<Order> orders = (List<Order>) query.list();
            
            gridData.setResponse(new ArrayList<>(convertTextToJson(orders)));
            
            long count = (long) session.createSQLQuery("select count(*) as count from fg_orders o inner join fg_supplier_users su on o.suppl_id = su.suppl_id inner join fg_suppliers s on s.suppl_id = su.suppl_id where su.user_id='" + data.get("userId") + "' and order_status='" + data.get("status") + "' and order_date >= current_date() order by o.order_date desc" + (filter.equals("")  ? "" :  (" where " + filter)))
                    .addScalar("count", LongType.INSTANCE)
                    .uniqueResult();
            gridData.setCount(count);
            trans.commit();
        } catch (Exception ex) {
            System.err.println("error" + ex.getMessage());
            trans.rollback();
        }
        return gridData;
    }
        
    private JSONArray convertTextToJson(List<Order> orders){
        JSONArray jsonArr = new JSONArray();
        for(Order order: orders){
            JSONObject object = new JSONObject();
            object.put("order_id", order.getOrder_id());
            object.put("supplier_name", order.getSupplier_name());
            object.put("prod_amt",order.getProd_amt());
            object.put("disc_amt", order.getDisc_amt());
            object.put("tax_amt", order.getTax_amt());
            object.put("order_amt", order.getOrder_amt());
            object.put("order_status", order.getOrder_status());
            object.put("order_date", Utils.convertDateToString(order.getOrder_date()));
            jsonArr.add(object);
        }
        return jsonArr;
    }
}
