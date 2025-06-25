package com.base.tesis_backend.repositories;

import com.base.tesis_backend.entities.AudioVisualContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AudioVisualContentRepository extends JpaRepository<AudioVisualContent, Long> {
}
