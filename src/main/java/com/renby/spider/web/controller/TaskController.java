package com.renby.spider.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.renby.spider.web.entity.Task;
import com.renby.spider.web.entity.TaskPageRule;
import com.renby.spider.web.repository.TaskContentRuleRepository;
import com.renby.spider.web.repository.TaskPageRuleRepository;
import com.renby.spider.web.repository.TaskRepository;
import com.renby.spider.web.service.ISpiderExcuteService;

@RestController
@RequestMapping(TaskController.BASE_URL)
public class TaskController extends AbstractController {
	public static final String BASE_URL = "/spider/task";
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private TaskPageRuleRepository taskPageRepository;
	@Autowired
	private TaskContentRuleRepository taskContentRepository;
	@Autowired
	private ISpiderExcuteService excuteService;

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
		List<Task> tasks = null;
		if (StringUtils.isEmpty(s)) {
			PageImpl<Task> pageData = (PageImpl<Task>) taskRepository
					.findAll(new PageRequest(page, pageSize));
			tasks = pageData.getContent();
		} else {
			tasks = taskRepository.findByNameLike(s, new PageRequest(page, pageSize));
		}
		ModelMap model = new ModelMap();
		model.addAttribute("tasklist", tasks);
		return new ModelAndView(getListPage(), model);
	}

	/**
	 * 详情
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("view/{id}")
	public ModelAndView viewPage(@PathVariable("id") long id) {
		Task task = taskRepository.findOne(id);
		ModelMap model = getModel(task);
		return new ModelAndView(getViewPage(), model);
	}

	/**
	 * 新增页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "new", method = RequestMethod.GET)
	public ModelAndView createPage() {
		ModelMap model = getModel(null);
		model.addAttribute("action", BASE_URL + "/new");
		return new ModelAndView(getEditPage(), model);
	}

	/**
	 * 新增提交
	 * 
	 * @param newTask
	 * @return
	 */
	@RequestMapping(value = "new", method = RequestMethod.POST)
	public ModelAndView create(Task newTask) {
		taskRepository.save(newTask);
		TaskPageRule startPage = new TaskPageRule();
		startPage.setTask(newTask);
		startPage.setStartPage(true);
		startPage.setName("任务首页");
		taskPageRepository.save(startPage);
		return list(null, Integer.valueOf(LIST_DEFAULT_PAGE), Integer.valueOf(LIST_DEFAULT_PAGE_SIZE));
	}

	/**
	 * 修改页面
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public ModelAndView updatePage(@PathVariable("id") long id) {
		Task task = taskRepository.findOne(id);
		ModelMap model = getModel(task);
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
	public ModelAndView update(Task modifierdTask) {
		Task saved = taskRepository.save(modifierdTask);
		ModelMap model = getModel(saved);
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
	public ModelAndView delete(@PathVariable("id") Long id) {
		Task task = taskRepository.findOne(id);
		List<TaskPageRule> pages = taskPageRepository.findByTask(task);
		taskContentRepository.deleteByPageIn(pages);
		taskPageRepository.deleteInBatch(pages);
		taskRepository.delete(task);
		return list(null, Integer.valueOf(LIST_DEFAULT_PAGE), Integer.valueOf(LIST_DEFAULT_PAGE_SIZE));
	}

	/**
	 * 执行监测任务
	 * 
	 * @param id
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("run/{id}")
	public ModelAndView run(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
		excuteService.runTask(id);
		response.sendRedirect(getListPage());
		return null;
	}

	/**
	 * 爬取任务查看界面
	 * 
	 * @param page
	 * @return
	 */
	private ModelMap getModel(Task task) {
		ModelMap model = new ModelMap();
		if (task != null) {
			List<TaskPageRule> taskPageList = taskPageRepository.findByTask(task);
			model.addAttribute("task", task);
			model.addAttribute("taskPagelist", taskPageList);
		} else {
			model.addAttribute("task", new Task());
			model.addAttribute("taskPagelist", new TaskPageRule());

		}
		return model;
	}

	@Override
	public String getBasePage() {
		return BASE_URL;
	}
}
