package com.renby.spider.controller;

import java.util.List;

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

import com.renby.spider.entity.SpiderTask;
import com.renby.spider.entity.SpiderTaskPageRule;
import com.renby.spider.repository.SpiderTaskPageRuleRepository;
import com.renby.spider.repository.SpiderTaskRepository;
import com.renby.spider.service.ISpiderExcuteService;

@RestController
@RequestMapping(SpiderTaskController.BASE_URL)
public class SpiderTaskController {
	public static final String DEFAULT_PAGE = "0";
	public static final String DEFAULT_PAGE_SIZE = "20";
	public static final String BASE_URL = "/spider/task";
	@Autowired
	private SpiderTaskRepository taskRepository;
	@Autowired
	private SpiderTaskPageRuleRepository taskPageRepository;
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
			@RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(value = "pagesize", required = false, defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
		List<SpiderTask> tasks = null;
		if (StringUtils.isEmpty(s)) {
			PageImpl<SpiderTask> pageData = (PageImpl<SpiderTask>) taskRepository
					.findAll(new PageRequest(page, pageSize));
			tasks = pageData.getContent();
		} else {
			tasks = taskRepository.findByNameLike(s, new PageRequest(page, pageSize));
		}
		ModelMap model = new ModelMap();
		model.addAttribute("tasklist", tasks);
		return new ModelAndView(BASE_URL + "/list", model);
	}

	/**
	 * 详情
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("view/{id}")
	public ModelAndView view(@PathVariable("id") long id) {
		SpiderTask task = taskRepository.findOne(id);
		ModelMap model = getModel(task);
		return new ModelAndView(BASE_URL + "/view", model);
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
		return new ModelAndView(BASE_URL + "/edit", model);
	}

	/**
	 * 新增提交
	 * 
	 * @param newTask
	 * @return
	 */
	@RequestMapping(value = "new", method = RequestMethod.POST)
	public ModelAndView create(SpiderTask newTask) {
		taskRepository.save(newTask);
		return list(null, Integer.valueOf(DEFAULT_PAGE), Integer.valueOf(DEFAULT_PAGE_SIZE));
	}

	/**
	 * 修改页面
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public ModelAndView updatePage(@PathVariable("id") long id) {
		SpiderTask task = taskRepository.findOne(id);
		ModelMap model = getModel(task);
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
	public ModelAndView update(SpiderTask modifierdTask) {
		SpiderTask saved = taskRepository.save(modifierdTask);
		ModelMap model = getModel(saved);
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
		taskRepository.delete(id);
		return list(null, Integer.valueOf(DEFAULT_PAGE), Integer.valueOf(DEFAULT_PAGE_SIZE));
	}

	/**
	 * 执行抓取任务
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("run/{id}")
	public ModelAndView run(@PathVariable("id") Long id) {
		excuteService.runTask(id);
		return list(null, Integer.valueOf(DEFAULT_PAGE), Integer.valueOf(DEFAULT_PAGE_SIZE));
	}

	/**
	 * 爬取任务查看界面
	 * 
	 * @param page
	 * @return
	 */
	private ModelMap getModel(SpiderTask task) {
		ModelMap model = new ModelMap();
		if (task != null) {
			List<SpiderTaskPageRule> taskPageList = taskPageRepository.findByTask(task);
			model.addAttribute("task", task);
			model.addAttribute("taskPagelist", taskPageList);
		} else {
			model.addAttribute("task", new SpiderTask());
			model.addAttribute("taskPagelist", new SpiderTaskPageRule());

		}
		return model;
	}
}
