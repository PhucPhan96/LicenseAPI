package nfc.model.ViewModel;

import nfc.model.OrderDetail;
import nfc.model.Product;

public class OrderDetailView {
	private OrderDetail orderDetail;
	private Product product;
	public OrderDetail getOrderDetail() {
		return orderDetail;
	}
	public void setOrderDetail(OrderDetail orderDetail) {
		this.orderDetail = orderDetail;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
}
