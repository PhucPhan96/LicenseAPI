package nfc.service;

import java.util.List;

import nfc.model.Category;
import nfc.model.ViewModel.CategoryView;
import nfc.model.ViewModel.SupplierProductView;

public interface ICategoryService {
	List<Category> getListCategory(String username);
	List<Category> getListCategoryFilterType(String type);
	boolean insertCategory(Category cate);
	boolean updateCategory(Category cate);
	boolean deleteCategory(String cateID);
	Category getCategory(String cateID);
	List<CategoryView> getListCategoryView(String type);
	List<SupplierProductView> getListProductOfCategory(int supplierId);
        List<Category> getListCategoryOfSupplier(int supplierId);
        List<Category> addListCategory(List<Category> categories);
	
}
