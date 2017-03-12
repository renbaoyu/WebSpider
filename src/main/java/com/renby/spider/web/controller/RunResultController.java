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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.renby.spider.web.entity.RunResult;
import com.renby.spider.web.entity.RunResultPage;
import com.renby.spider.web.repository.RunResultPageRepository;
import com.renby.spider.web.repository.RunResultRepository;
import com.renby.spider.web.service.IRunResultService;

@RestController
@RequestMapping(RunResultController.BASE_URL)
public class RunResultController extends AbstractController {
	public static final String BASE_URL = "/spider/runresult";
	@Autowired
	private RunResultRepository resultRepository;
	@Autowired
	private RunResultPageRepository resultPageRepository;
	@Autowired
	private IRunResultService runResultService;

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
		ModelMap model = new ModelMap();
		if (StringUtils.isEmpty(s)) {
			PageImpl<RunResult> resultData = (PageImpl<RunResult>) resultRepository
					.findAll(new PageRequest(page, pageSize));
			setPagination(model, resultData, getListPage(), s, pageSize, page);
			results = resultData.getContent();
		} else {
			results = resultRepository.findByNameLike(s, new PageRequest(page, pageSize));
		}
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
	public ModelAndView viewPage(@PathVariable("id") long id, @RequestParam(value = "s", required = false) String s,
			@RequestParam(value = "page", required = false, defaultValue = LIST_DEFAULT_PAGE) int page,
			@RequestParam(value = "pagesize", required = false, defaultValue = LIST_DEFAULT_PAGE_SIZE) int pageSize) {
		RunResult result = resultRepository.findOne(id);
		PageImpl<RunResultPage> resultPage = resultPageRepository.findByResult(result, new PageRequest(page, pageSize));

		ModelMap model = new ModelMap();
		setPagination(model, resultPage, getViewPageUrl(id), s, pageSize, page);
		model.addAttribute("result", result);
		model.addAttribute("resultPagelist", resultPage.getContent());
		return new ModelAndView(getViewPage(), model);
	}

	/**
	 * 删除提交
	 * 
	 * @param id
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("delete/{id}")
	public ModelAndView delete(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
		runResultService.deleteRunResultById(id);
		response.sendRedirect(getListPage());
		return null;
	}

	@Override
	public String getBasePage() {
		return BASE_URL;
	}
}
