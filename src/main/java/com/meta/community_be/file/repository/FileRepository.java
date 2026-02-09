package com.meta.community_be.file.repository;

import com.meta.community_be.file.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}