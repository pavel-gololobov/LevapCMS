package ru.levap.cms.persistance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.levap.cms.persistance.model.Folder;

public interface FolderRepository extends JpaRepository<Folder, Long> {
	Page<Folder> findByActiveAndPictureNotNull(Boolean active, Pageable pageable);
	Page<Folder> findByActiveAndParent(Boolean active, Folder parent, Pageable pageable);
	Page<Folder> findByParent(Folder parent, Pageable pageable);
	
	@Query("select p from Folder p where p.active = :active and p.parent = :parent and p.picture != null and p.createDate >= :createDate")
	Page<Folder> findByActiveAndParentAndPictureNotNullAndCreateDateMoreThen(Boolean active, Folder parent, Long createDate, Pageable pageable);
	
	@Query("select p from Folder p where p.active = :active and p.parent = :parent and p.picture != null and p.createDate <= :createDate")
	Page<Folder> findByActiveAndParentAndPictureNotNullAndCreateDateLessThen(Boolean active, Folder parent, Long createDate, Pageable pageable);
	
	@Query("select p from Folder p where p.active = :active and p.picture != null and p.createDate >= :createDate")
	Page<Folder> findByActiveAndPictureNotNullAndCreateDateMoreThen(Boolean active, Long createDate, Pageable pageable);
	
	@Query("select p from Folder p where p.active = :active and p.picture != null and p.createDate <= :createDate")
	Page<Folder> findByActiveAndPictureNotNullAndCreateDateLessThen(Boolean active, Long createDate, Pageable pageable);
}
