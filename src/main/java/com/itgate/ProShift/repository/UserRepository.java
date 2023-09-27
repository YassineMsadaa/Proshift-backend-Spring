package com.itgate.ProShift.repository;

import com.itgate.ProShift.entity.Role;
import com.itgate.ProShift.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findUserByRoles(Role role);
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
    User findByVerificationCode(String code);
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    User findByEmail(String email);

    boolean existsByCin(String cin);
    User findByResetPasswordToken(String token);
    List<User> findUsersByDepartement(User.Departement departement);
    List<User> findUsersByEquipe_id(Long id);
    List<User> findUsersByRolesContainingAndEquipeIsNull(Role role);
    List<User> findUsersByRolesContainingAndEquipeIsNullOrEquipe_Id(Role role,Long id);
}
