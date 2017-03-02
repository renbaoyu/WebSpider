package com.renby.spider.web.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "spider_task_content")
public class TaskContentRule implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "pageid")
	private TaskPageRule page;
	private String name;
	private HtmlMatchRuleType ruleType = HtmlMatchRuleType.Normal;
	private String ruleExpression;
	private boolean emptyEnabled;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TaskPageRule getPage() {
		return page;
	}

	public void setPage(TaskPageRule page) {
		this.page = page;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HtmlMatchRuleType getRuleType() {
		return ruleType;
	}

	public void setRuleType(HtmlMatchRuleType ruleType) {
		this.ruleType = ruleType;
	}

	public String getRuleExpression() {
		return ruleExpression;
	}

	public void setRuleExpression(String ruleExpression) {
		this.ruleExpression = ruleExpression;
	}

	public boolean isEmptyEnabled() {
		return emptyEnabled;
	}

	public void setEmptyEnabled(boolean emptyEnabled) {
		this.emptyEnabled = emptyEnabled;
	}
}
