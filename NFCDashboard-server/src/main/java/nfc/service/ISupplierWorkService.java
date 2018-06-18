package nfc.service;

import java.util.List;

import nfc.model.SupplierWork;

public interface ISupplierWorkService {
	List<SupplierWork> getListSupplierWork();
	boolean insertSupplierWork(SupplierWork supplierwork);
}
