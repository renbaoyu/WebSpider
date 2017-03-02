package com.renby.spider.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.renby.spider.web.entity.TaskProgress;

@Repository
public interface TaskProgressRepository extends JpaRepository<TaskProgress,Long>{
}
