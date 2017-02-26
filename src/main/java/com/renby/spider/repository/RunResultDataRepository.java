package com.renby.spider.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.renby.spider.entity.RunResultData;

@Repository
public interface RunResultDataRepository extends JpaRepository<RunResultData,Long>{
	public List<RunResultData> findByNameLike(String name, Pageable pageable);
}
