package com.renby.spider.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/spider")
public class SpiderController {
	@RequestMapping()
	public ModelAndView home() {
		return new ModelAndView("/spider/welcome");
	}
}
