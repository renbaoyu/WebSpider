package com.renby.spider.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.renby.spider.entity.RunResult;
import com.renby.spider.entity.RunResultPage;
import com.renby.spider.repository.RunResultPageRepository;
import com.renby.spider.repository.RunResultRepository;

@RestController
@RequestMapping(RunResultController.BASE_URL)
public class RunResultController extends AbstractController {
	public static final String BASE_URL = "/spider/runresult";
	@Autowired
	private RunResultRepository resultRepository;
	@Autowired
	private RunResultPageRepository resultPageRepository;

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
		List<RunResult> results = null;
		if (StringUtils.isEmpty(s)) {
			PageImpl<RunResult> resultData = (PageImpl<RunResult>) resultRepository
					.findAll(new PageRequest(page, pageSize));
			results = resultData.getContent();
		} else {
			results = resultRepository.findByNameLike(s, new PageRequest(page, pageSize));
		}
		ModelMap model = new ModelMap();
		model.addAttribute("runresultlist", results);
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
		RunResult result = resultRepository.findOne(id);
		List<RunResultPage> resultPage = resultPageRepository.findByResult(result);
		
		ModelMap model = new ModelMap();
		model.addAttribute("result", result);
		model.addAttribute("resultPagelist", resultPage);
		return new ModelAndView(getViewPage(), model);
	}

	@Override
	public String getBasePage() {
		return BASE_URL;
	}
}
