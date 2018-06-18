package nfc.model.ViewModel;

import java.util.ArrayList;
import java.util.List;

import nfc.model.Category;
import nfc.model.Product;

public class SupplierProductView {
	private List<ProductView> products = new ArrayList<ProductView>();
	private Category category = new Category();
	public List<ProductView> getProducts() {
		return products;
	}
	public void setProducts(List<ProductView> products) {
		this.products = products;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
}
