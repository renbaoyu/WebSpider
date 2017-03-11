package com.renby.spider.web.controller;

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

import com.renby.spider.web.entity.Explan;
import com.renby.spider.web.entity.Task;
import com.renby.spider.web.repository.ExplanRepository;

@RestController
@RequestMapping(ExplanController.BASE_URL)
public class ExplanController extends AbstractController {
	public static final String BASE_URL = "/spider/explan";
	@Autowired
	private ExplanRepository explanRepository;

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
		List<Explan> explans = null;
		ModelMap model = new ModelMap();
		if (StringUtils.isEmpty(s)) {
			PageImpl<Explan> explanData = (PageImpl<Explan>) explanRepository
					.findAll(new PageRequest(page, pageSize));
			setPagination(model, explanData, getListPage(), s, pageSize, page);
			explans = explanData.getContent();
		} else {
			explans = explanRepository.findByNameLike(s, new PageRequest(page, pageSize));
		}
		model.addAttribute("explanlist", explans);
		return new ModelAndView(getListPage(), model);
	}

	/**
	 * 新增页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "new", method = RequestMethod.GET)
	public ModelAndView createPage() {
		ModelMap model = new ModelMap();
		Explan explan = new Explan();
		Task task = new Task();
		task.setId((long) 1);
		explan.setTask(task);
		model.addAttribute("explan", explan);
		model.addAttribute("action", BASE_URL + "/new");
		return new ModelAndView(getEditPage(), model);
	}

	/**
	 * 新增提交
	 * 
	 * @param newExplan
	 * @return
	 */
	@RequestMapping(value = "new", method = RequestMethod.POST)
	public ModelAndView create(Explan newExplan) {
		explanRepository.save(newExplan);
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
		Explan explan = explanRepository.findOne(id);
		ModelMap model = new ModelMap();
		model.addAttribute("explan", explan);
		model.addAttribute("action", getEditPage());
		return new ModelAndView(getEditPage(), model);
	}

	/**
	 * 修改提交
	 * 
	 * @param modifierdExplan
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public ModelAndView update(Explan modifierdExplan) {
		Explan saved = explanRepository.save(modifierdExplan);
		ModelMap model = new ModelMap();
		model.addAttribute("explan", saved);
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
		explanRepository.delete(id);
		return list(null, Integer.valueOf(LIST_DEFAULT_PAGE), Integer.valueOf(LIST_DEFAULT_PAGE_SIZE));
	}

	@Override
	public String getBasePage() {
		return BASE_URL;
	}
}
