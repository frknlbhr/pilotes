package com.tui.proof.repository;

import com.tui.proof.model.Client;
import com.tui.proof.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByUser(User user);

}
