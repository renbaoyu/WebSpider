package com.renby.spider.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.renby.spider.web.entity.RunResultData;
import com.renby.spider.web.entity.RunResultPage;
import com.renby.spider.web.repository.RunResultDataRepository;
import com.renby.spider.web.repository.RunResultPageRepository;

@RestController
@RequestMapping(RunResultPageController.BASE_URL)
public class RunResultPageController extends AbstractController {
	public static final String BASE_URL = "/spider/runresult_page";
	@Autowired
	private RunResultPageRepository resultPageRepository;
	@Autowired
	private RunResultDataRepository resultDataRepository;

	/**
	 * 详情
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("view/{id}")
	public ModelAndView viewPage(@PathVariable("id") long id) {
		RunResultPage page = resultPageRepository.findOne(id);
		List<RunResultData> pageData = resultDataRepository.findByPage(page);
		
		ModelMap model = new ModelMap();
		model.addAttribute("page", page);
		model.addAttribute("pageDatalist", pageData);
		return new ModelAndView(getViewPage(), model);
	}

	@Override
	public String getBasePage() {
		return BASE_URL;
	}
}
