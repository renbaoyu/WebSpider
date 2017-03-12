package com.renby.spider.web.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.renby.spider.web.entity.RunResult;
import com.renby.spider.web.entity.Task;

@Repository
@Transactional
public interface RunResultRepository extends JpaRepository<RunResult,Long>{
	public List<RunResult> findByNameLike(String name, Pageable pageable);
	public List<RunResult> findByTask(Task task);
}
