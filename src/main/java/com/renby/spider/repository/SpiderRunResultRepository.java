package com.renby.spider.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.renby.spider.entity.SpiderRunResult;

@Repository
public interface SpiderRunResultRepository extends JpaRepository<SpiderRunResult,Long>{
	public List<SpiderRunResult> findByNameLike(String name, Pageable pageable);
}
