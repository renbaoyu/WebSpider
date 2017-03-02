package com.renby.spider.web.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.renby.spider.web.entity.Task;
import com.renby.spider.web.entity.TaskPageRule;

@Repository
@Transactional
public interface TaskPageRuleRepository extends JpaRepository<TaskPageRule,Long>{
	public List<TaskPageRule> findByNameLike(String name, Pageable pageable);
	public List<TaskPageRule> findByTask(Task task);
}
