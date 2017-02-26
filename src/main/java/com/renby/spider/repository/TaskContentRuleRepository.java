package com.renby.spider.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.renby.spider.entity.TaskContentRule;
import com.renby.spider.entity.TaskPageRule;

@Repository
@Transactional
public interface TaskContentRuleRepository extends JpaRepository<TaskContentRule,Long>{
	public List<TaskContentRule> findByNameLike(String name, Pageable pageable);
	public List<TaskContentRule> findByPage(TaskPageRule page);
	public void deleteByPage(TaskPageRule page);
	public void deleteByPageIn(List<TaskPageRule> pages);
}
