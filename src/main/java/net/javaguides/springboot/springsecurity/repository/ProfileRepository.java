package net.javaguides.springboot.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.javaguides.springboot.springsecurity.model.Profile;
import net.javaguides.springboot.springsecurity.model.User;


@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

	Profile findByUsers(User u);
}
