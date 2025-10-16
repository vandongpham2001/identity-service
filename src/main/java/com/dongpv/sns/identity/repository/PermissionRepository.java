package com.dongpv.sns.identity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dongpv.sns.identity.entity.PermissionEntity;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, String> {
    @Query(
            """
		SELECT p
		FROM PermissionEntity p
		WHERE COALESCE(:keyword, '') = ''
			OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
			OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
	""")
    Page<PermissionEntity> filter(@Param("keyword") String keyword, Pageable pageable);
}
