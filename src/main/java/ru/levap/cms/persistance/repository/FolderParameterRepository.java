package ru.levap.cms.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.levap.cms.persistance.model.FolderParameter;

public interface FolderParameterRepository extends JpaRepository<FolderParameter, Long> {
}
