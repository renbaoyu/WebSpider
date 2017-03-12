package com.renby.spider.web.controller;

import java.io.IOException;

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
import com.renby.spider.web.repository.TaskContentRuleRepository;
import com.renby.spider.web.service.ITaskService;

@RestController
@RequestMapping(TaskContentController.BASE_URL)
public class TaskContentController extends AbstractController {
	public static final String BASE_URL = "/spider/task_content";
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
	public ModelAndView createPage(@RequestParam("page") Long pageid) {
		ModelMap model = getModel(null);
		model.addAttribute("pageid", pageid);
		model.addAttribute("action", BASE_URL + "/new");
		return new ModelAndView(getEditPage(), model);
	}

	/**
	 * 新增提交
	 * 
	 * @param newTaskContent
	 * @return
	 */
	@RequestMapping(value = "new", method = RequestMethod.POST)
	public ModelAndView create(TaskContentRule newTaskContent, HttpServletResponse response)
			throws ServletException, IOException {
		TaskContentRule savedcontent = taskContentRepository.save(newTaskContent);
		response.sendRedirect("/spider/task_page/view/" + savedcontent.getPage().getId());
		return null;
	}

	/**
	 * 修改页面
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public ModelAndView updatePage(@PathVariable("id") long id) {
		TaskContentRule saved = taskContentRepository.findOne(id);
		ModelMap model = getModel(saved);
		model.addAttribute("pageid", saved.getPage().getId());
		model.addAttribute("action", getEditPage());
		return new ModelAndView(getEditPage(), model);
	}

	/**
	 * 修改提交
	 * 
	 * @param modifierdContent
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public ModelAndView update(TaskContentRule modifierdContent) {
		TaskContentRule saved = taskContentRepository.save(modifierdContent);
		ModelMap model = getModel(saved);
		model.addAttribute("pageid", saved.getPage().getId());
		model.addAttribute("action", getEditPage());
		return new ModelAndView(getEditPage(), model);
	}

	/**
	 * 删除提交
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("delete/{id}")
	public ModelAndView delete(@PathVariable("id") Long id, HttpServletResponse response)
			throws ServletException, IOException {
		TaskContentRule content = taskContentRepository.findOne(id);
		taskService.deleteTaskContent(id);
		response.sendRedirect("/spider/task_page/view/" + content.getPage().getId());
		return null;
	}

	/**
	 * 爬取任务查看界面
	 * 
	 * @param content
	 * @return
	 */
	private ModelMap getModel(TaskContentRule content) {
		ModelMap model = new ModelMap();
		if (content != null) {
			model.addAttribute("content", content);
		} else {
			model.addAttribute("content", new TaskContentRule());
		}
		return model;
	}

	@Override
	public String getBasePage() {
		return BASE_URL;
	}
}
