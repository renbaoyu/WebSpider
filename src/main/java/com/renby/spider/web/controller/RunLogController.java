package com.renby.spider.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.renby.spider.web.entity.RunLog;
import com.renby.spider.web.repository.RunLogRepository;

@RestController
@RequestMapping(RunLogController.BASE_URL)
public class RunLogController extends AbstractController{
	public static final String BASE_URL = "/spider/runlog";
	@Autowired
	private RunLogRepository runLogRepository;

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
		PageImpl<RunLog> logData = (PageImpl<RunLog>) runLogRepository.findAll(new PageRequest(page, pageSize));
		List<RunLog> logs = logData.getContent();
		ModelMap model = new ModelMap();
		setPagination(model, logData, getListPage(), s, pageSize, page);
		model.addAttribute("runloglist", logs);
		return new ModelAndView(getListPage(), model);
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
		runLogRepository.delete(id);
		response.sendRedirect(getListPage());
		return null;
	}

	/**
	 * 清空提交
	 * 
	 * @param id
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("deleteall")
	public ModelAndView deleteAll(HttpServletResponse response) throws IOException {
		runLogRepository.deleteAll();
		response.sendRedirect(getListPage());
		return null;
	}

	@Override
	public String getBasePage() {
		return BASE_URL;
	}
}
