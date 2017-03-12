package com.renby.spider.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.renby.spider.web.entity.TaskContentRule;
import com.renby.spider.web.entity.TaskPageRule;
import com.renby.spider.web.repository.TaskContentRuleRepository;
import com.renby.spider.web.repository.TaskPageRuleRepository;
import com.renby.spider.web.service.ITaskService;

@RestController
@RequestMapping(TaskPageController.BASE_URL)
public class TaskPageController extends AbstractController {
	public static final String BASE_URL = "/spider/task_page";
	@Autowired
	private TaskPageRuleRepository taskPageRepository;
	@Autowired
	private TaskContentRuleRepository taskContentRepository;
	@Autowired
	private ITaskService taskService;

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
		return new ModelAndView(getEditPage(), model);
	}

	/**
	 * 新增提交
	 * 
	 * @param newTaskPage
	 * @return
	 */
	@RequestMapping(value = "new", method = RequestMethod.POST)
	public ModelAndView create(TaskPageRule newTaskPage, HttpServletResponse response) throws ServletException, IOException {
		TaskPageRule savedPage = taskPageRepository.save(newTaskPage);
		response.sendRedirect("/spider/task/view/" + savedPage.getTask().getId());
		return null;
	}

	/**
	 * 修改页面
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public ModelAndView viewPage(@PathVariable("id") long id) {
		TaskPageRule saved = taskPageRepository.findOne(id);
		ModelMap model = getModel(saved);
		model.addAttribute("action", getViewPage());
		return new ModelAndView(getViewPage(), model);
	}

	/**
	 * 修改页面
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public ModelAndView updatePage(@PathVariable("id") long id) {
		TaskPageRule saved = taskPageRepository.findOne(id);
		ModelMap model = getModel(saved);
		model.addAttribute("taskid", saved.getTask().getId());
		model.addAttribute("action", getEditPage());
		return new ModelAndView(getEditPage(), model);
	}

	/**
	 * 修改提交
	 * 
	 * @param modifierdTask
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public ModelAndView update(TaskPageRule modifierdTask) {
		TaskPageRule saved = taskPageRepository.save(modifierdTask);
		ModelMap model = getModel(saved);
		model.addAttribute("taskid", saved.getTask().getId());
		model.addAttribute("action", getEditPage());
		return new ModelAndView(getEditPage(), model);
	}

	/**
	 * 删除提交
	 * 
	 * @param id
	 * @return
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@RequestMapping("delete/{id}")
	public ModelAndView delete(@PathVariable("id") Long id, HttpServletResponse response) throws ServletException, IOException {
		TaskPageRule page = taskPageRepository.findOne(id);
		taskService.deleteTaskPage(id);
		response.sendRedirect("/spider/task/view/" + page.getTask().getId());
		return null;
	}

	/**
	 * 爬取任务查看界面
	 * 
	 * @param page
	 * @return
	 */
	private ModelMap getModel(TaskPageRule page) {
		ModelMap model = new ModelMap();
		if (page != null) {
			List<TaskContentRule> taskContentList = taskContentRepository.findByPage(page);
			model.addAttribute("page", page);
			model.addAttribute("taskContentlist", taskContentList);
		} else {
			model.addAttribute("page", new TaskPageRule());
			model.addAttribute("taskContentlist", new TaskContentRule());

		}
		return model;
	}

	@Override
	public String getBasePage() {
		return BASE_URL;
	}
}
