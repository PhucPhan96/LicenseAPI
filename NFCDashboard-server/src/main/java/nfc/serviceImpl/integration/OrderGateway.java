package nfc.serviceImpl.integration;

import org.springframework.integration.annotation.Gateway;

import nfc.model.ViewModel.OrderView;

public interface OrderGateway {
    @Gateway(requestChannel="orders")
    void sendOrder(OrderView request);
    public OrderView receive();
}
