package com.tui.proof.unit.repository;

import com.tui.proof.model.Address;
import com.tui.proof.model.Client;
import com.tui.proof.model.Order;
import com.tui.proof.model.User;
import com.tui.proof.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author filbahar
 * @created 18.07.2022
 */

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository repository;

    @Test
    public void should_find_no_orders_if_repository_empty() {
        Iterable users = repository.findAll();
        assertThat(users).isEmpty();
    }

    private User user;
    private User user2;
    private Client client;
    private Client client2;
    private Address address;
    private Address address2;

    @BeforeEach
    public void setup() {
        user = new User("user1", "user1@gmail.com", "test1234");
        user2 = new User("user2", "user2@hotmail.com", "asdf4321");
        client = new Client();
        client.setUser(user);
        client.setName("name1");
        client.setSurname("surname1");
        client2 = new Client();
        client2.setUser(user2);
        client2.setName("name2");
        client2.setSurname("surname2");
        address = new Address();
        address.setStreet("asffgf");
        address.setCity("ankara");
        address.setCountry("TR");
        address2 = new Address();
        address2.setStreet("qweeqr");
        address2.setCity("ankara");
        address2.setCountry("TR");
    }

    @Test
    public void should_store_an_order() {
        user = entityManager.persist(user);
        client = entityManager.persist(client);
        address = entityManager.persist(address);
        Order order = new Order();
        order.setClient(client);
        order.setDeliveryAddress(address);
        order.setPilotes(5);
        order.setOrderTotal(order.getPilotes() * 1.33d);
        Order persistedOrder = repository.save(order);
        assertThat(persistedOrder).hasFieldOrPropertyWithValue("pilotes", 5);
        assertThat(persistedOrder).hasFieldOrPropertyWithValue("client", client);
        assertThat(persistedOrder).hasFieldOrPropertyWithValue("orderTotal", 5 * 1.33d);
        Assertions.assertNotNull(persistedOrder.getId());
    }

    @Test
    public void should_filter_by_username() {
        user = entityManager.persist(user);
        client = entityManager.persist(client);
        address = entityManager.persist(address);
        Order order = new Order();
        order.setClient(client);
        order.setDeliveryAddress(address);
        order.setPilotes(5);
        order.setOrderTotal(order.getPilotes() * 1.33d);
        order = entityManager.persist(order);

        user2 = entityManager.persist(user2);
        client2 = entityManager.persist(client2);
        address2 = entityManager.persist(address2);
        Order order2 = new Order();
        order2.setClient(client2);
        order2.setDeliveryAddress(address2);
        order2.setPilotes(10);
        order2.setOrderTotal(order2.getPilotes() * 1.33d);
        order2 = entityManager.persist(order2);

        List<Order> filteredOrders = repository.filter(null, null, "er2", null);
        Assertions.assertNotNull(filteredOrders);
        Assertions.assertEquals(1, filteredOrders.size());
        Assertions.assertEquals(order2.getId(), filteredOrders.get(0).getId());
        Assertions.assertEquals(order2.getPilotes(), filteredOrders.get(0).getPilotes());
    }

}
