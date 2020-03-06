package ru.levap.cms.persistance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.levap.cms.persistance.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByLogin(String login);
}