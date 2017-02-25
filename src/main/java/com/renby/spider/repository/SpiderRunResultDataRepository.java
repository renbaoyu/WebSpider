package com.renby.spider.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.renby.spider.entity.SpiderRunResultData;

@Repository
public interface SpiderRunResultDataRepository extends JpaRepository<SpiderRunResultData,Long>{
	public List<SpiderRunResultData> findByNameLike(String name, Pageable pageable);
}
