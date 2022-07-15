package com.tui.proof.service;

import com.tui.proof.dto.*;
import com.tui.proof.exception.OrderCreateException;
import com.tui.proof.exception.OrderUpdateException;
import com.tui.proof.model.Address;
import com.tui.proof.model.Client;
import com.tui.proof.model.Order;
import com.tui.proof.repository.AddressRepository;
import com.tui.proof.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final ClientService clientService;

    public OrderService(OrderRepository orderRepository, AddressRepository addressRepository, ClientService clientService) {
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
        this.clientService = clientService;
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public OrderDto createNewOrder(OrderCreateRequest orderCreateRequest) {
        Client client = clientService.getLoggedInClient();
        if (client == null) {
            throw new OrderCreateException("Only clients can create order!");
        }
        Address address = createAddressFromDto(orderCreateRequest.getDeliveryAddress());
        address = addressRepository.save(address);
        Order order = new Order();
        order.setDeliveryAddress(address);
        order.setClient(client);
        order.setPilotes(orderCreateRequest.getPortion().getNumberOfMeatBalls());
        order.setOrderTotal(1.33d * order.getPilotes());
        return createDtoFromOrder(saveOrder(order));
    }

    public OrderDto updateOrder(OrderUpdateRequest orderUpdateRequest) {
        Order order = orderRepository.findById(orderUpdateRequest.getOrderNumber()).orElseThrow(() -> new EntityNotFoundException("Order not found!"));
        checkIfOrderUpdateable(order);
        if (orderUpdateRequest.getPortion() != null) {
            order.setPilotes(orderUpdateRequest.getPortion().getNumberOfMeatBalls());
            order.setOrderTotal(1.33d * order.getPilotes());
        }
        if (orderUpdateRequest.getDeliveryAddress() != null) {
            Address address = order.getDeliveryAddress();
            address.setStreet(orderUpdateRequest.getDeliveryAddress().getStreet());
            address.setPostcode(orderUpdateRequest.getDeliveryAddress().getPostcode());
            address.setCity(orderUpdateRequest.getDeliveryAddress().getCity());
            address.setCountry(orderUpdateRequest.getDeliveryAddress().getCountry());
        }
        return createDtoFromOrder(saveOrder(order));
    }

    public List<OrderFilterResponse> filterOrders(String name, String surname, String username, String email) {
        List<Order> orders = orderRepository.filter(name, surname, username, email);
        List<OrderFilterResponse> orderFilterResponses = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orders)) {
            orderFilterResponses = orders.stream().map(order -> new OrderFilterResponse(createDtoFromOrder(order), createDtoFromClient(order.getClient()))).collect(Collectors.toList());
        }
        return orderFilterResponses;
    }

    private void checkIfOrderUpdateable(Order order) {
        Client client = clientService.getLoggedInClient();
        if (!Objects.equals(client, order.getClient())) {
            throw new OrderUpdateException("You can only update your own orders!");
        }
        Duration duration = Duration.between(order.getCreatedDate(), LocalDateTime.now());
        if (duration.toMinutes() >= 5) {
            throw new OrderUpdateException("More than 5 minutes have passed since the order was created. Cannot be updated");
        }
    }

    private OrderDto createDtoFromOrder(Order order) {
        return OrderDto.builder()
                .orderNumber(order.getId())
                .orderTotal(order.getOrderTotal())
                .deliveryAddress(createDtoFromAddress(order.getDeliveryAddress()))
                .portion(PilotesPortionEnum.findByNumberOfMeatBalls(order.getPilotes()))
                .build();
    }

    private AddressDto createDtoFromAddress(Address address) {
        return AddressDto.builder()
                .street(address.getStreet())
                .postcode(address.getPostcode())
                .city(address.getCity())
                .country(address.getCountry())
                .build();
    }

    private Address createAddressFromDto(AddressDto addressDto) {
        Address address = new Address();
        address.setStreet(addressDto.getStreet());
        address.setPostcode(addressDto.getPostcode());
        address.setCity(addressDto.getCity());
        address.setCountry(addressDto.getCountry());
        return address;
    }

    private ClientDto createDtoFromClient(Client client) {
        return ClientDto.builder()
                .name(client.getName())
                .surname(client.getSurname())
                .username(client.getUser().getUsername())
                .email(client.getUser().getEmail())
                .build();
    }
}
