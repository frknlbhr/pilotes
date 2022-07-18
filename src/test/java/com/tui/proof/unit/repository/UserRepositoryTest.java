package com.tui.proof.unit.repository;

import com.tui.proof.model.User;
import com.tui.proof.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author filbahar
 * @created 18.07.2022
 */

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    @Test
    public void should_find_no_users_if_repository_empty() {
        Iterable users = repository.findAll();
        assertThat(users).isEmpty();
    }

    @Test
    public void should_store_a_user() {
        User user = repository.save(new User("ali", "ali@gmail.com", "test1234"));
        assertThat(user).hasFieldOrPropertyWithValue("username", "ali");
        assertThat(user).hasFieldOrPropertyWithValue("email", "ali@gmail.com");
        Assertions.assertNotNull(user.getId());
    }

    @Test
    public void should_exist_by_username_if_already_saved_before() {
        User user = entityManager.persist(new User("ali", "ali@gmail.com", "test1234"));
        Boolean existsByUsername = repository.existsByUsername("ali");
        Assertions.assertTrue(existsByUsername);
    }

    @Test
    public void should_not_exist_by_username() {
        Boolean existsByUsername = repository.existsByUsername("veli");
        Assertions.assertFalse(existsByUsername);
    }
}
