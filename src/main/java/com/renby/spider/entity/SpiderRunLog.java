package com.renby.spider.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "spider_run_log")
public class SpiderRunLog implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "taskid")
	@JsonBackReference
	private SpiderTask task;
	private SpiderExplan explan;
	private String url;
	private String contentCharset;
	private String contentType;
	private int stateCode;
	@Column(columnDefinition = "MEDIUMBLOB")
	private byte[] content;
	private Date finishedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SpiderTask getTask() {
		return task;
	}

	public void setTask(SpiderTask task) {
		this.task = task;
	}

	public SpiderExplan getExplan() {
		return explan;
	}

	public void setExplan(SpiderExplan explan) {
		this.explan = explan;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public int getStateCode() {
		return stateCode;
	}

	public void setStateCode(int stateCode) {
		this.stateCode = stateCode;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public Date getFinishedDate() {
		return finishedDate;
	}

	public void setFinishedDate(Date finishedDate) {
		this.finishedDate = finishedDate;
	}

	public String getContentCharset() {
		return contentCharset;
	}

	public void setContentCharset(String contentCharset) {
		this.contentCharset = contentCharset;
	}

}
