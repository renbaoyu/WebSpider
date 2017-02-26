package com.renby.spider.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.renby.spider.entity.SpiderRunLog;
import com.renby.spider.repository.SpiderRunLogRepository;
import com.renby.spider.repository.SpiderTaskPageRuleRepository;
import com.renby.spider.repository.SpiderTaskRepository;

@RestController
@RequestMapping(RunLogController.BASE_URL)
public class RunLogController {
	public static final String DEFAULT_PAGE = "0";
	public static final String DEFAULT_PAGE_SIZE = "20";
	public static final String BASE_URL = "/spider/runlog";
	@Autowired
	private SpiderTaskRepository taskRepository;
	@Autowired
	private SpiderTaskPageRuleRepository taskPageRepository;
	@Autowired
	private SpiderRunLogRepository runLogRepository;

	/**
	 * 列表
	 * 
	 * @param s
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@RequestMapping({ "", "list" })
	public ModelAndView list(@RequestParam(value = "s", required = false) String s,
			@RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(value = "pagesize", required = false, defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
		PageImpl<SpiderRunLog> logData = (PageImpl<SpiderRunLog>) runLogRepository.findAll(new PageRequest(page, pageSize));
		List<SpiderRunLog> logs = logData.getContent();
		ModelMap model = new ModelMap();
		model.addAttribute("runloglist", logs);
		return new ModelAndView(BASE_URL + "/list", model);
	}
}