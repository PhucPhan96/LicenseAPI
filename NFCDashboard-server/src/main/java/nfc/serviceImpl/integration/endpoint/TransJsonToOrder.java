package nfc.serviceImpl.integration.endpoint;

import java.util.ArrayList;
import java.util.List;

import nfc.model.Order;
import nfc.model.OrderDetail;
import nfc.serviceImpl.common.Utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class TransJsonToOrder {
	public Order tranJsonToOrder(String data)
	{
		System.out.println("Data " + data);
		//Order order = new Order();
		JSONObject orderJsonObj = Utils.convertStringToJsonObject(data);
		Order order = (Order) Utils.convertJsonObjectToClass(orderJsonObj, "nfc.app.order.Order", new String[]{"orderDetails"});
		if(orderJsonObj.containsKey("orderDetails"))
		{
			List<OrderDetail> lstOrderDetail = new ArrayList<OrderDetail>();
			JSONArray arrOrderDetail = (JSONArray) orderJsonObj.get("orderDetails");
			for(int i=0;i<arrOrderDetail.size();i++)
			{
				JSONObject jsonOrderDetail = (JSONObject) arrOrderDetail.get(i);
				OrderDetail orderDetail = (OrderDetail) Utils.convertJsonObjectToClass(jsonOrderDetail, "nfc.app.order.OrderDetail", new String[]{});
				lstOrderDetail.add(orderDetail);
			}
			//order.setOrderDetails(lstOrderDetail);	
		}
		return order;
	}
	public String tranOrderToJson(Order order)
	{
		return "aaaaaa";
	}
}
