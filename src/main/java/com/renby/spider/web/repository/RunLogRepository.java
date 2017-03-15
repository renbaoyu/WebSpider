package com.renby.spider.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.renby.spider.web.entity.RunLog;

@Repository
@Transactional
public interface RunLogRepository extends JpaRepository<RunLog,Long>{
}
