package com.tui.proof.unit.service;

import com.tui.proof.dto.OrderCreateRequest;
import com.tui.proof.dto.OrderDto;
import com.tui.proof.dto.OrderUpdateRequest;
import com.tui.proof.exception.OrderCreateException;
import com.tui.proof.exception.OrderUpdateException;
import com.tui.proof.model.Client;
import com.tui.proof.model.Order;
import com.tui.proof.repository.AddressRepository;
import com.tui.proof.repository.OrderRepository;
import com.tui.proof.service.ClientService;
import com.tui.proof.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private OrderService orderService;

    private OrderDto orderDto;
    private Order orderFromMockedRepo;
    private Client mockedClient;

    @BeforeEach
    public void setup() {
        orderFromMockedRepo = new Order();
        orderFromMockedRepo.setPilotes(5);
        orderFromMockedRepo.setId(1L);
        orderFromMockedRepo.setCreatedDate(LocalDateTime.of(2022, 1, 1, 1, 1, 1, 1));
        mockedClient = new Client();
        mockedClient.setId(8L);
        orderFromMockedRepo.setClient(mockedClient);
    }

    @Test
    public void test_createNewOrder_fail() {
        Mockito.when(clientService.getLoggedInClient()).thenReturn(null);
        Assertions.assertThrows(OrderCreateException.class, () -> orderService.createNewOrder(new OrderCreateRequest()));
    }

    @Test
    public void test_updateOrder_timeFail() {
        Mockito.when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(orderFromMockedRepo));
        Mockito.when(clientService.getLoggedInClient()).thenReturn(mockedClient);
        OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest();
        orderUpdateRequest.setOrderNumber(1L);
        Assertions.assertThrows(OrderUpdateException.class, () -> orderService.updateOrder(orderUpdateRequest));
    }

}
