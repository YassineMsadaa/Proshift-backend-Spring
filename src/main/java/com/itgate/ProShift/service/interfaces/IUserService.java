package com.itgate.ProShift.service.interfaces;

import com.itgate.ProShift.entity.ERole;
import com.itgate.ProShift.entity.Role;
import com.itgate.ProShift.entity.User;

import java.util.List;

public interface IUserService {
    List<User> findAllUser();
    List<User> findUserByRole(ERole role);
    User blockUser(Long idUser);
    User unBlockUser(Long idUser);
    User updateUser (User user);
    User findUserbyId(Long idUser);
    public User checkin(Long idUser);
    public User checkOut(Long idUser);
    void removeUser(Long idUser);
    boolean changePassword(String verificationCode, String newPassword);
    boolean changePasswordByUser(Long id , String password , String newPassword);
    User findByUsername(String username);
    List<User> findUsersByDepartement(User.Departement departement);
    User updateEmployee(User user);
    List<User> findUsersByEquipe_id(Long id);
    List<User> findUsersByRolesContainingAndEquipeIsNull(String role);

    List<User> findUsersByRolesContainingAndEquipeIsNullOrEquipe_Id(Role role,Long id);
    User assignUserToEquipe(User updatedUser, Long id);
    User unAssignUserFromEquipe(User updatedUser);

    List<User> assignUsersToEquipe(List<User> users,Long equipeid);
    public List<User> unassignUsersFromEquipe(List<User> users);
}
