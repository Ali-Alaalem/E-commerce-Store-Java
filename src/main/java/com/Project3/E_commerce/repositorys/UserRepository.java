package com.Project3.E_commerce.repositorys;


import com.Project3.E_commerce.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmail(String userEmailAddress);
    User findUserByEmail(String userEmailAddress);
}
