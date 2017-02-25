package com.renby.spider.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.renby.spider.entity.SpiderExplan;
import com.renby.spider.entity.SpiderTask;

@Repository
public interface SpiderExplanRepository extends JpaRepository<SpiderExplan,Long>{
	public List<SpiderExplan> findByNameLike(String name, Pageable pageable);
}
