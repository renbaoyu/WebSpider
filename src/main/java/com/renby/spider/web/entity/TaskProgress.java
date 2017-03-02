package com.renby.spider.web.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "spider_task_progress")
public class TaskProgress implements Serializable {
	public enum TaskStatus {
		NOTSTART("未开始"), RUNNING("正在执行中"), PAUSE("暂停中"), FINISHED("已完成");

		private String name;

		TaskStatus(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "taskid")
	@JsonBackReference
	private Task task;
	@ManyToOne
	@JoinColumn(name = "explanid")
	@JsonBackReference
	private Explan explan;
	private Date startTime;
	private TaskStatus status = TaskStatus.NOTSTART;
	private int processedPageCount;
	private Date finishedTime;
	private int cust;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Explan getExplan() {
		return explan;
	}

	public void setExplan(Explan explan) {
		this.explan = explan;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public Date getFinishedTime() {
		return finishedTime;
	}

	public void setFinishedTime(Date finishedTime) {
		this.finishedTime = finishedTime;
	}

	public int getCust() {
		return cust;
	}

	public void setCust(int cust) {
		this.cust = cust;
	}

	public int getProcessedPageCount() {
		return processedPageCount;
	}

	public void setProcessedPageCount(int processedPageCount) {
		this.processedPageCount = processedPageCount;
	}

}
