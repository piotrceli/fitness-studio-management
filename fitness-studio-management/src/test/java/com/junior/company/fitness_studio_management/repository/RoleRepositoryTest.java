package com.junior.company.fitness_studio_management.repository;

import com.junior.company.fitness_studio_management.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void shouldFindRoleByName_givenValidRoleName() {

        // given
        Role role = new Role(1L, "USER");
        roleRepository.save(role);

        // when
        Optional<Role> result = roleRepository.findByName(role.getName());

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(Optional.of(role));
    }

    @Test
    void shouldNotFindRoleByName_givenInvalidRoleName() {

        // given
        String roleName = "invalid";

        // when
        Optional<Role> result = roleRepository.findByName(roleName);

        // then
        assertThat(result).isEmpty();
    }

}