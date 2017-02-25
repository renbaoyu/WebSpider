package com.renby.spider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.renby.spider.entity.SpiderRunLog;

@Repository
public interface SpiderRunLogRepository extends JpaRepository<SpiderRunLog,Long>{
}
