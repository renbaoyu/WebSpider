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

@Entity
@Table(name = "spider_explan")
public class Explan implements Serializable {
	public enum ExplanStatus {
		NOT_START("未开始"), RUNNING("进行中"), INTERRUPTED("已中断"), SUSPEND("已暂停"), FINISHED("已完成");

		private String name;

		ExplanStatus(String name) {
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
	@ManyToOne
	@JoinColumn(name = "taskid")
	private Task task;
	private Date startTime;
	private int intervals;
	private int repeatNumbers = 1;
	private int finishedNumber;
	private ExplanStatus explanStatus = ExplanStatus.NOT_START;
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
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public int getRepeatNumbers() {
		return repeatNumbers;
	}
	public void setRepeatNumbers(int repeatNumbers) {
		this.repeatNumbers = repeatNumbers;
	}
	public int getFinishedNumber() {
		return finishedNumber;
	}
	public void setFinishedNumber(int finishedNumber) {
		this.finishedNumber = finishedNumber;
	}
	public ExplanStatus getExplanStatus() {
		return explanStatus;
	}
	public void setExplanStatus(ExplanStatus explanStatus) {
		this.explanStatus = explanStatus;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public int getIntervals() {
		return intervals;
	}
	public void setIntervals(int intervals) {
		this.intervals = intervals;
	}

}
