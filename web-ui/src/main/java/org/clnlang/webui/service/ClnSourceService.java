package org.clnlang.webui.service;

import jakarta.transaction.Transactional;
import org.clnlang.webui.model.ClnSourceEntity;
import org.clnlang.webui.repository.ClnSourceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * CRUD service for CLN source files persisted in H2.
 */
@Service
public class ClnSourceService {

    private final ClnSourceRepository repository;

    public ClnSourceService(ClnSourceRepository repository) {
        this.repository = repository;
    }

    public List<ClnSourceEntity> findAll() {
        return repository.findAll();
    }

    public Optional<ClnSourceEntity> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<ClnSourceEntity> findByPackageName(String packageName) {
        return repository.findByPackageName(packageName);
    }

    @Transactional
    public ClnSourceEntity save(String packageName, String source) {
        return repository.findByPackageName(packageName)
                .map(existing -> {
                    existing.setSource(source);
                    return repository.save(existing);
                })
                .orElseGet(() -> repository.save(new ClnSourceEntity(packageName, source)));
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public void deleteByPackageName(String packageName) {
        repository.deleteByPackageName(packageName);
    }
}
