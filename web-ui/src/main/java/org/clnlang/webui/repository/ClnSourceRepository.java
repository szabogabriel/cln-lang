package org.clnlang.webui.repository;

import org.clnlang.webui.model.ClnSourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClnSourceRepository extends JpaRepository<ClnSourceEntity, Long> {
    Optional<ClnSourceEntity> findByPackageName(String packageName);
    boolean existsByPackageName(String packageName);
    void deleteByPackageName(String packageName);
}
