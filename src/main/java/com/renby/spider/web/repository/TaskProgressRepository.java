package com.renby.spider.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.renby.spider.web.entity.Task;
import com.renby.spider.web.entity.TaskProgress;

@Repository
@Transactional
public interface TaskProgressRepository extends JpaRepository<TaskProgress,Long>{
	public void deleteByTask(Task task);
}
