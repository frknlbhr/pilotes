package com.tui.proof.unit.service;

import com.tui.proof.dto.*;
import com.tui.proof.exception.OrderCreateException;
import com.tui.proof.exception.OrderUpdateException;
import com.tui.proof.model.Address;
import com.tui.proof.model.Client;
import com.tui.proof.model.Order;
import com.tui.proof.model.User;
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
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private List<Order> orderList;

    @BeforeEach
    public void setup() {
        orderFromMockedRepo = new Order();
        orderFromMockedRepo.setPilotes(5);
        orderFromMockedRepo.setId(1L);
        orderFromMockedRepo.setCreatedDate(LocalDateTime.of(2022, 1, 1, 1, 1, 1, 1));
        mockedClient = new Client();
        mockedClient.setId(8L);
        mockedClient.setUser(new User());
        orderFromMockedRepo.setClient(mockedClient);
        orderFromMockedRepo.setDeliveryAddress(new Address());

        orderList = new ArrayList<>();
        orderList.add(orderFromMockedRepo);
    }

    @Test
    public void test_createNewOrder_fail() {
        Mockito.when(clientService.getLoggedInClient()).thenReturn(null);
        Assertions.assertThrows(OrderCreateException.class, () -> orderService.createNewOrder(new OrderCreateRequest()));
    }

    @Test
    public void test_createNewOrder() {
        Mockito.when(clientService.getLoggedInClient()).thenReturn(mockedClient);
        Address mockedAddress = new Address();
        mockedAddress.setCountry("tr");
        mockedAddress.setCity("asdasf");
        mockedAddress.setStreet("asdasdfsd");
        Mockito.when(addressRepository.save(Mockito.isA(Address.class))).thenReturn(mockedAddress);
        Mockito.when(orderRepository.save(Mockito.isA(Order.class))).thenReturn(orderFromMockedRepo);
        AddressDto addressDto = AddressDto.builder().country("asd").city("qweqwr").street("asdaf").build();
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest.setPortion(PilotesPortionEnum.HALF);
        orderCreateRequest.setDeliveryAddress(addressDto);
        OrderDto result = orderService.createNewOrder(orderCreateRequest);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(orderFromMockedRepo.getId(), result.getOrderNumber());
        Assertions.assertEquals(orderFromMockedRepo.getPilotes(), result.getPortion().getNumberOfMeatBalls());
        Mockito.verify(clientService, Mockito.times(1)).getLoggedInClient();
        Mockito.verify(addressRepository, Mockito.times(1)).save(Mockito.isA(Address.class));
    }

    @Test
    public void test_updateOrder_timeFail() {
        Mockito.when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(orderFromMockedRepo));
        Mockito.when(clientService.getLoggedInClient()).thenReturn(mockedClient);
        OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest();
        orderUpdateRequest.setOrderNumber(1L);
        Assertions.assertThrows(OrderUpdateException.class, () -> orderService.updateOrder(orderUpdateRequest));
    }

    @Test
    public void test_filterOrders() {
        Mockito.when(orderRepository.filter(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(orderList);
        List<OrderFilterResponse> filterResponseList = orderService.filterOrders("test", "test", "test", "test");
        Assertions.assertFalse(StringUtils.isEmpty(filterResponseList));
        Assertions.assertEquals(filterResponseList.size(), orderList.size());
        Assertions.assertEquals(filterResponseList.get(0).getOrder().getOrderNumber(), orderList.get(0).getId());
        Assertions.assertEquals(filterResponseList.get(0).getOrder().getPortion().getNumberOfMeatBalls(), orderList.get(0).getPilotes());
        Mockito.verify(orderRepository, Mockito.times(1))
                .filter(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }
}
