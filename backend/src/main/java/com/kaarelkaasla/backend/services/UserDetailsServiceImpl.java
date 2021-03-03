package com.kaarelkaasla.backend.services;

import com.kaarelkaasla.backend.entities.User;
import com.kaarelkaasla.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return UserDetailsImpl.build(user);
    }

    @Transactional
    public void deleteRoles(Integer id) {
        entityManager.createNativeQuery("delete from user_roles u where u.user_id = ?")
                .setParameter(1, id)
                .executeUpdate();
    }

    @Transactional
    public void addRoles(Integer id, List<Integer> roles) {
        for (Integer role : roles) {
            entityManager.createNativeQuery("insert into user_roles (user_id, role_id) values (?,?)")
                    .setParameter(1, id)
                    .setParameter(2, role)
                    .executeUpdate();
        }
    }

    @Transactional
    @Modifying
    public void setEnabled(Integer id, Boolean enabled) {
        entityManager.createNativeQuery("update users set is_enabled = ? where id = ?")
                .setParameter(1, enabled)
                .setParameter(2, id)
                .executeUpdate();
    }


}
