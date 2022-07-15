package com.tui.proof.service;

import com.tui.proof.model.Client;
import com.tui.proof.model.User;
import com.tui.proof.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;
    private final UserService userService;

    public ClientService(ClientRepository clientRepository, UserService userService) {
        this.clientRepository = clientRepository;
        this.userService = userService;
    }

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    public Client createNewClient(User user, String name, String surname, String phone) {
        Client client = new Client();
        client.setUser(user);
        client.setName(name);
        client.setSurname(surname);
        client.setPhone(phone);
        return saveClient(client);
    }

    public Client findClientByUser(User user) {
        return clientRepository.findByUser(user);
    }

    public Client getLoggedInClient() {
        User user = userService.getLoggedInUser();
        if (user != null) {
            return findClientByUser(user);
        }
        return null;
    }

}
