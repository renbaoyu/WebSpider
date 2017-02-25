package com.renby.spider.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.renby.spider.entity.SpiderRunResultPage;

@Repository
public interface SpiderRunResultPageRepository extends JpaRepository<SpiderRunResultPage,Long>{
	public List<SpiderRunResultPage> findByNameLike(String name, Pageable pageable);
}
