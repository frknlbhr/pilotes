package com.tui.proof.repository;

import com.tui.proof.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o " +
            "join fetch o.deliveryAddress da " +
            "join fetch o.client c " +
            "join fetch c.user u " +
            "where (:clientName is null or c.name like %:clientName%) " +
            "and (:clientSurname is null or c.surname like %:clientSurname%) " +
            "and (:clientUsername is null or u.username like %:clientUsername%) " +
            "and (:clientEmail is null or u.email like %:clientEmail%) " +
            "order by o.id desc")
    List<Order> filter(@Param("clientName") String clientName, @Param("clientSurname") String clientSurname, @Param("clientUsername") String clientUsername, @Param("clientEmail") String clientEmail);
}
