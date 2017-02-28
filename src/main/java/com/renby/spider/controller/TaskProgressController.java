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

import com.renby.spider.entity.TaskProgress;
import com.renby.spider.repository.TaskProgressRepository;

@RestController
@RequestMapping(TaskProgressController.BASE_URL)
public class TaskProgressController extends AbstractController {
	public static final String DEFAULT_PAGE = "0";
	public static final String DEFAULT_PAGE_SIZE = "20";
	public static final String BASE_URL = "/spider/taskprogress";
	@Autowired
	private TaskProgressRepository progressRepository;

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
		PageImpl<TaskProgress> logData = (PageImpl<TaskProgress>) progressRepository
				.findAll(new PageRequest(page, pageSize));
		List<TaskProgress> tasks = logData.getContent();
		ModelMap model = new ModelMap();
		model.addAttribute("taskprocessList", tasks);
		return new ModelAndView(getListPage(), model);
	}

	@Override
	public String getBasePage() {
		return BASE_URL;
	}
}