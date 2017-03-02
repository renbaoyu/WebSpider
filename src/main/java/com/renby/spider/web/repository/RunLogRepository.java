package com.renby.spider.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.renby.spider.web.entity.RunLog;

@Repository
public interface RunLogRepository extends JpaRepository<RunLog,Long>{
}
