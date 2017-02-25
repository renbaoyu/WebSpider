package com.renby.spider.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.renby.spider.entity.SpiderTask;

@Repository
public interface SpiderTaskRepository extends JpaRepository<SpiderTask,Long>{
	public List<SpiderTask> findByNameLike(String name, Pageable pageable);
}
