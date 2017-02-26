package com.renby.spider.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.renby.spider.entity.RunResultPage;

@Repository
public interface RunResultPageRepository extends JpaRepository<RunResultPage,Long>{
	public List<RunResultPage> findByNameLike(String name, Pageable pageable);
}
