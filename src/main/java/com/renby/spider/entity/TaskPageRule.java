package com.renby.spider.entity;

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
@Table(name = "spider_task_rule")
public class TaskPageRule implements Serializable {
	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "taskid")
	private Task task;
	private String name;
	private HtmlMatchRuleType urlRuleType = HtmlMatchRuleType.Normal;
	private String urlRuleExpression;
	private boolean startPage = false;
	private boolean target;

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

	public HtmlMatchRuleType getUrlRuleType() {
		return urlRuleType;
	}

	public void setUrlRuleType(HtmlMatchRuleType urlRuleType) {
		this.urlRuleType = urlRuleType;
	}

	public String getUrlRuleExpression() {
		return urlRuleExpression;
	}

	public void setUrlRuleExpression(String urlRuleExpression) {
		this.urlRuleExpression = urlRuleExpression;
	}

	public boolean isTarget() {
		return target;
	}

	public void setTarget(boolean target) {
		this.target = target;
	}

	public boolean isStartPage() {
		return startPage;
	}

	public void setStartPage(boolean startPage) {
		this.startPage = startPage;
	}
}
