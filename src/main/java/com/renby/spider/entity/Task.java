package com.renby.spider.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "spider_task")
public class Task implements Serializable {
	enum FinishedMode {
		ALL_FINISHED("全部URL完成"), ANY_FINISHED("第一次完成");

		private String name;

		FinishedMode(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String startUrl;
	private FinishedMode finishedMode = FinishedMode.ANY_FINISHED;
	private String remarks;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartUrl() {
		return startUrl;
	}

	public void setStartUrl(String startUrl) {
		this.startUrl = startUrl;
	}

	public FinishedMode getFinishedMode() {
		return finishedMode;
	}

	public void setFinishedMode(FinishedMode finishedMode) {
		this.finishedMode = finishedMode;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
