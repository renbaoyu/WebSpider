package com.renby.spider.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.renby.spider.entity.TaskContentRule;
import com.renby.spider.entity.TaskPageRule;

@Repository
public interface TaskContentRuleRepository extends JpaRepository<TaskContentRule,Long>{
	public List<TaskContentRule> findByNameLike(String name, Pageable pageable);
	public List<TaskContentRule> findByPage(TaskPageRule page);
}
