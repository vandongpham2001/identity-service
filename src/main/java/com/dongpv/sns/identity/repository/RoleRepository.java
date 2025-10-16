package com.dongpv.sns.identity.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dongpv.sns.identity.entity.RoleEntity;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, String> {
    Optional<RoleEntity> findByName(String name);

    @Query(
            """
		SELECT r
		FROM RoleEntity r
		WHERE COALESCE(:keyword, '') = ''
			OR LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
			OR LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
	""")
    Page<RoleEntity> filter(@Param("keyword") String keyword, Pageable pageable);
}
