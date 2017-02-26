package com.renby.spider.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.renby.spider.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long>{
	public List<Task> findByNameLike(String name, Pageable pageable);
}