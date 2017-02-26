package com.renby.spider.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.renby.spider.entity.Explan;
import com.renby.spider.entity.Task;

@Repository
public interface ExplanRepository extends JpaRepository<Explan,Long>{
	public List<Explan> findByNameLike(String name, Pageable pageable);
}
