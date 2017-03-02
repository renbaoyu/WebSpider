package com.renby.spider.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.renby.spider.web.entity.RunResult;
import com.renby.spider.web.entity.RunResultPage;

@Repository
public interface RunResultPageRepository extends JpaRepository<RunResultPage,Long>{
	public List<RunResultPage> findByResult(RunResult result);
}
