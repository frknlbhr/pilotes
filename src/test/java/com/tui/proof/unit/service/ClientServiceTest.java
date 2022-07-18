package com.tui.proof.unit.service;

import com.tui.proof.model.Client;
import com.tui.proof.model.User;
import com.tui.proof.repository.ClientRepository;
import com.tui.proof.service.ClientService;
import com.tui.proof.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private Client clientFromMockedRepo;
    private User userMocked;

    @BeforeEach
    public void setup() {
        clientFromMockedRepo = new Client();
        clientFromMockedRepo.setId(1L);
        clientFromMockedRepo.setName("ali");
        clientFromMockedRepo.setName("veli");
        userMocked = new User();
        userMocked.setId(1L);
        userMocked.setUsername("aliveli");
        userMocked.setEmail("aliveli@gmail.com");
        clientFromMockedRepo.setUser(userMocked);
    }

    @Test
    public void test_saveClient() {
        Mockito.when(clientRepository.save(Mockito.isA(Client.class))).thenReturn(clientFromMockedRepo);

        Client result = clientService.saveClient(new Client());
        Assertions.assertEquals(clientFromMockedRepo.getId(), result.getId());
        Assertions.assertEquals(clientFromMockedRepo.getName(), result.getName());
        Assertions.assertEquals(clientFromMockedRepo.getSurname(), result.getSurname());
        Mockito.verify(clientRepository, Mockito.times(1)).save(Mockito.isA(Client.class));
    }

    @Test
    public void test_createNewClient() {
        Mockito.when(clientRepository.save(Mockito.isA(Client.class))).thenReturn(clientFromMockedRepo);
        Client client = clientService.createNewClient(userMocked, "name", "surname", null);
        Assertions.assertEquals(clientFromMockedRepo.getId(), client.getId());
        Assertions.assertEquals(clientFromMockedRepo.getName(), client.getName());
        Mockito.verify(clientRepository, Mockito.times(1)).save(Mockito.isA(Client.class));
    }

    @Test
    public void test_findClientByUser_success() {
        Mockito.when(clientRepository.findByUser(Mockito.isA(User.class))).thenReturn(clientFromMockedRepo);

        Client result = clientService.findClientByUser(new User());
        Assertions.assertEquals(clientFromMockedRepo.getId(), result.getId());
        Assertions.assertEquals(clientFromMockedRepo.getUser().getUsername(), result.getUser().getUsername());
        Assertions.assertEquals(clientFromMockedRepo.getUser().getEmail(), result.getUser().getEmail());
        Mockito.verify(clientRepository, Mockito.times(1)).findByUser(Mockito.isA(User.class));
    }

    @Test
    public void test_getLoggedInClient_null() {
        Mockito.when(userService.getLoggedInUser()).thenReturn(null);
        Client client = clientService.getLoggedInClient();
        Assertions.assertNull(client);
        Mockito.verify(userService, Mockito.times(1)).getLoggedInUser();
    }

    @Test
    public void test_getLoggedInClient() {
        Mockito.when(userService.getLoggedInUser()).thenReturn(userMocked);
        Mockito.when(clientRepository.findByUser(Mockito.isA(User.class))).thenReturn(clientFromMockedRepo);
        Client client = clientService.getLoggedInClient();
        Assertions.assertNotNull(client);
        Assertions.assertNotNull(client.getUser());
        Assertions.assertEquals(clientFromMockedRepo.getId(), client.getId());
        Mockito.verify(userService, Mockito.times(1)).getLoggedInUser();
    }

}
