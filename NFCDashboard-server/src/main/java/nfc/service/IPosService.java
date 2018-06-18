package nfc.service;

import nfc.model.ViewModel.GridView;
import nfc.model.ViewModel.PosDetailView;

public interface IPosService {
    PosDetailView getPosDetailView(String orderId);
    GridView getListOrderAllStoreOfUser(GridView gridData);
}
