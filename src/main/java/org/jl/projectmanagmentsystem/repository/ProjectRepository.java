package org.jl.projectmanagmentsystem.repository;

import org.jl.projectmanagmentsystem.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}