package com.renby.spider.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.renby.spider.entity.RunResult;

@Repository
public interface RunResultRepository extends JpaRepository<RunResult,Long>{
	public List<RunResult> findByNameLike(String name, Pageable pageable);
}
