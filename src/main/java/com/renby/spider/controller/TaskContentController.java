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
@RequestMapping(TaskContentController.BASE_URL)
public class TaskContentController {
	public static final String DEFAULT_PAGE = "0";
	public static final String DEFAULT_PAGE_SIZE = "20";
	public static final String BASE_URL = "/spider/task_content";
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
	public ModelAndView createPage(@RequestParam("page") Long pageid) {
		ModelMap model = getModel(null);
		model.addAttribute("pageid", pageid);
		model.addAttribute("action", BASE_URL + "/new");
		return new ModelAndView(BASE_URL + "/edit", model);
	}

	/**
	 * 新增提交
	 * 
	 * @param newTaskContent
	 * @return
	 */
	@RequestMapping(value = "new", method = RequestMethod.POST)
	public ModelAndView create(SpiderTaskContentRule newTaskContent) {
		SpiderTaskContentRule savedPage = taskContentRepository.save(newTaskContent);
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
		SpiderTaskContentRule saved = taskContentRepository.findOne(id);
		ModelMap model = getModel(saved);
		model.addAttribute("pageid", saved.getPage().getId());
		model.addAttribute("action", BASE_URL + "/edit");
		return new ModelAndView(BASE_URL + "/edit", model);
	}

	/**
	 * 修改提交
	 * 
	 * @param modifierdContent
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public ModelAndView update(SpiderTaskContentRule modifierdContent) {
		SpiderTaskContentRule saved = taskContentRepository.save(modifierdContent);
		ModelMap model = getModel(saved);
		model.addAttribute("pageid", saved.getPage().getId());
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
		SpiderTaskContentRule content = taskContentRepository.findOne(id);
		taskContentRepository.delete(id);
		return taskEditPage(content);
	}

	/**
	 * 爬取任务查看界面
	 * 
	 * @param content
	 * @return
	 */
	private ModelAndView taskEditPage(SpiderTaskContentRule content) {
		SpiderTaskPageRule taskPage = taskPageRepository.findOne(content.getPage().getId());
		List<SpiderTaskPageRule> taskPageList = taskPageRepository.findByTask(taskPage.getTask());
		ModelMap model = new ModelMap();
		SpiderTask task = taskRepository.findOne(taskPage.getTask().getId());
		model.addAttribute("task", task);
		model.addAttribute("taskPagelist", taskPageList);
		return new ModelAndView(TaskController.BASE_URL + "/edit", model);
	}

	/**
	 * 爬取任务查看界面
	 * 
	 * @param content
	 * @return
	 */
	private ModelMap getModel(SpiderTaskContentRule content) {
		ModelMap model = new ModelMap();
		if (content != null) {
			model.addAttribute("content", content);
		} else {
			model.addAttribute("content", new SpiderTaskContentRule());

		}
		return model;
	}
}
