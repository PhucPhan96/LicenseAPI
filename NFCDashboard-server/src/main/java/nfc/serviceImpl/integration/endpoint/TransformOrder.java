package nfc.serviceImpl.integration.endpoint;

import nfc.model.Order;
import nfc.model.ViewModel.OrderView;
import nfc.serviceImpl.common.Utils;

public class TransformOrder {
	public String tranOrderToJson(OrderView orderView)
	{
		System.out.println("Vao transform order");
		return Utils.convertObjectToJsonString(orderView);
	}
}
