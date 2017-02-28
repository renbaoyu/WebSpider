package com.renby.spider.entity;

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
	@JsonBackReference
	private Task task;
	private Date startTime;
	private Date runTimeStart;
	private Date runTimeEnd;
	private Date cycleTime;
	private int number;
	private ExplanStatus explanStatus;
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

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getRunTimeStart() {
		return runTimeStart;
	}

	public void setRunTimeStart(Date runTimeStart) {
		this.runTimeStart = runTimeStart;
	}

	public Date getRunTimeEnd() {
		return runTimeEnd;
	}

	public void setRunTimeEnd(Date runTimeEnd) {
		this.runTimeEnd = runTimeEnd;
	}

	public Date getCycleTime() {
		return cycleTime;
	}

	public void setCycleTime(Date cycleTime) {
		this.cycleTime = cycleTime;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
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

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

}
