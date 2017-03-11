package com.renby.spider.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.renby.spider.web.entity.TaskProgress;
import com.renby.spider.web.repository.TaskProgressRepository;

@RestController
@RequestMapping(TaskProgressController.BASE_URL)
public class TaskProgressController extends AbstractController {
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
			@RequestParam(value = "page", required = false, defaultValue = LIST_DEFAULT_PAGE) int page,
			@RequestParam(value = "pagesize", required = false, defaultValue = LIST_DEFAULT_PAGE_SIZE) int pageSize) {
		PageImpl<TaskProgress> processData = (PageImpl<TaskProgress>) progressRepository
				.findAll(new PageRequest(page, pageSize));
		List<TaskProgress> tasks = processData.getContent();
		ModelMap model = new ModelMap();
		setPagination(model, processData, getListPage(), s, pageSize, page);
		model.addAttribute("taskprocessList", tasks);
		return new ModelAndView(getListPage(), model);
	}

	/**
	 * 删除提交
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("delete/{id}")
	public ModelAndView delete(@PathVariable("id") Long id) {
		progressRepository.delete(id);
		return list(null, Integer.valueOf(LIST_DEFAULT_PAGE), Integer.valueOf(LIST_DEFAULT_PAGE_SIZE));
	}

	@Override
	public String getBasePage() {
		return BASE_URL;
	}
}
