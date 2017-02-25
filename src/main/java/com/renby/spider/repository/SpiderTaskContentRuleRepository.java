package com.renby.spider.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.renby.spider.entity.SpiderTaskContentRule;
import com.renby.spider.entity.SpiderTaskPageRule;

@Repository
public interface SpiderTaskContentRuleRepository extends JpaRepository<SpiderTaskContentRule,Long>{
	public List<SpiderTaskContentRule> findByNameLike(String name, Pageable pageable);
	public List<SpiderTaskContentRule> findByPage(SpiderTaskPageRule page);
}
