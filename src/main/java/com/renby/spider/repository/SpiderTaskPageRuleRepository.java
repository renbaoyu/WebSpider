package com.renby.spider.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.renby.spider.entity.SpiderTask;
import com.renby.spider.entity.SpiderTaskPageRule;

@Repository
public interface SpiderTaskPageRuleRepository extends JpaRepository<SpiderTaskPageRule,Long>{
	public List<SpiderTaskPageRule> findByNameLike(String name, Pageable pageable);
	public List<SpiderTaskPageRule> findByTask(SpiderTask task);
}
