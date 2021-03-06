package com.renby.spider.web.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.renby.spider.web.entity.Explan;
import com.renby.spider.web.entity.Task;

@Repository
@Transactional
public interface ExplanRepository extends JpaRepository<Explan,Long>{
	public List<Explan> findByNameLike(String name, Pageable pageable);
	public void deleteByTask(Task task);
}
