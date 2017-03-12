package com.renby.spider.web.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.renby.spider.web.entity.RunResultData;
import com.renby.spider.web.entity.RunResultPage;

@Repository
@Transactional
public interface RunResultDataRepository extends JpaRepository<RunResultData,Long>{
	public List<RunResultData> findByNameLike(String name, Pageable pageable);
	public List<RunResultData> findByPage(RunResultPage page);
	public void deleteByPageIn(List<RunResultPage> pages);
}
