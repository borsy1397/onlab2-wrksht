package com.captainborsy.wrksht.repository;


import com.captainborsy.wrksht.model.Role;
import com.captainborsy.wrksht.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsername(String username);

    List<User> findAllByRole(Role role);

    List<User> findAllByIsDeleted(boolean deleted);

    Optional<User> findFirstByUsernameAndIdNot(String username, String id);
}
