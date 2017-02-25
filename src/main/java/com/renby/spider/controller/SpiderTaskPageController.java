package com.renby.spider.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.renby.spider.entity.SpiderTask;
import com.renby.spider.entity.SpiderTaskContentRule;
import com.renby.spider.entity.SpiderTaskPageRule;
import com.renby.spider.repository.SpiderTaskContentRuleRepository;
import com.renby.spider.repository.SpiderTaskPageRuleRepository;
import com.renby.spider.repository.SpiderTaskRepository;

@RestController
@RequestMapping(SpiderTaskPageController.BASE_URL)
public class SpiderTaskPageController {
	public static final String DEFAULT_PAGE = "0";
	public static final String DEFAULT_PAGE_SIZE = "20";
	public static final String BASE_URL = "/spider/task_page";
	@Autowired
	private SpiderTaskRepository taskRepository;
	@Autowired
	private SpiderTaskPageRuleRepository taskPageRepository;
	@Autowired
	private SpiderTaskContentRuleRepository taskContentRepository;

	/**
	 * 新增页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "new", method = RequestMethod.GET)
	public ModelAndView createPage(@RequestParam("task") Long taskid) {
		ModelMap model = getModel(null);
		model.addAttribute("taskid", taskid);
		model.addAttribute("action", BASE_URL + "/new");
		return new ModelAndView(BASE_URL + "/edit", model);
	}

	/**
	 * 新增提交
	 * 
	 * @param newTaskPage
	 * @return
	 */
	@RequestMapping(value = "new", method = RequestMethod.POST)
	public ModelAndView create(SpiderTaskPageRule newTaskPage) {
		SpiderTaskPageRule savedPage = taskPageRepository.save(newTaskPage);
		return taskEditPage(savedPage);
	}

	/**
	 * 修改页面
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public ModelAndView updatePage(@PathVariable("id") long id) {
		SpiderTaskPageRule saved = taskPageRepository.findOne(id);
		ModelMap model = getModel(saved);
		model.addAttribute("taskid", saved.getTask().getId());
		model.addAttribute("action", BASE_URL + "/edit");
		return new ModelAndView(BASE_URL + "/edit", model);
	}

	/**
	 * 修改提交
	 * 
	 * @param modifierdTask
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public ModelAndView update(SpiderTaskPageRule modifierdTask) {
		SpiderTaskPageRule saved = taskPageRepository.save(modifierdTask);
		ModelMap model = getModel(saved);
		model.addAttribute("taskid", saved.getTask().getId());
		model.addAttribute("action", BASE_URL + "/edit");
		return new ModelAndView(BASE_URL + "/edit", model);
	}

	/**
	 * 删除提交
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("delete/{id}")
	public ModelAndView delete(@PathVariable("id") Long id) {
		SpiderTaskPageRule page = taskPageRepository.findOne(id);
		taskPageRepository.delete(id);
		return taskEditPage(page);
	}

	/**
	 * 爬取任务查看界面
	 * 
	 * @param page
	 * @return
	 */
	private ModelAndView taskEditPage(SpiderTaskPageRule page) {
		List<SpiderTaskPageRule> taskPageList = taskPageRepository.findByTask(page.getTask());
		ModelMap model = new ModelMap();
		SpiderTask task = taskRepository.findOne(page.getTask().getId());
		model.addAttribute("task", task);
		model.addAttribute("taskPagelist", taskPageList);
		return new ModelAndView(SpiderTaskController.BASE_URL + "/edit", model);
	}

	/**
	 * 爬取任务查看界面
	 * 
	 * @param page
	 * @return
	 */
	private ModelMap getModel(SpiderTaskPageRule page) {
		ModelMap model = new ModelMap();
		if (page != null) {
			List<SpiderTaskContentRule> taskContentList = taskContentRepository.findByPage(page);
			model.addAttribute("page", page);
			model.addAttribute("taskContentlist", taskContentList);
		} else {
			model.addAttribute("page", new SpiderTaskPageRule());
			model.addAttribute("taskContentlist", new SpiderTaskContentRule());

		}
		return model;
	}
}
