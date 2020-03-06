package ru.levap.cms.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.levap.cms.persistance.model.UserAuthority;

public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {
}
