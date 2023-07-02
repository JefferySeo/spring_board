package com.jeffery.spbbs.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeffery.spbbs.command.SpCommand;
import com.jeffery.spbbs.command.SpDetailCommand;
import com.jeffery.spbbs.command.SpListCommand;
import com.jeffery.spbbs.command.SpReplyCommand;
import com.jeffery.spbbs.command.SpReplyokCommand;
import com.jeffery.spbbs.command.SpUpdateCommand;
import com.jeffery.spbbs.command.SpWriteCommand;

@Controller
public class SpController {
	
	// 모든 command가 갖고있는 인터페이스 타입을 선언
	SpCommand command;
	
	@RequestMapping("/list")
	public String list(Model model) {
		System.out.println("list() 실행");
		
		command = new SpListCommand();
		command.execute(model);
		
		return "list";
	}
	
	@RequestMapping("/detail")
	public String detail(HttpServletRequest request, Model model) {
		System.out.println("detail() 실행");
		
		model.addAttribute("request", request);
		command = new SpDetailCommand();
		command.execute(model);
		
		return "detail";
	}
	
	@RequestMapping("/write")
	public String write(Model model) {
		System.out.println("write()");
		return "write";
	}
	
	@RequestMapping(value = "/writeok", method = RequestMethod.POST)
	public String writeok(HttpServletRequest request, Model model) {
		System.out.println("writeok");
		
		model.addAttribute("request", request);
		command = new SpWriteCommand();
		command.execute(model);
		
		return "redirect:list";
	}
	
	@RequestMapping("/reply")
	public String reply(HttpServletRequest request, Model model) {
		System.out.println("reply()");
		
		model.addAttribute("request", request);
		command = new SpReplyCommand();
		command.execute(model);
		
		return "reply";
	}
	
	@RequestMapping(value = "/replyok", method = RequestMethod.POST)
	public String replyok(HttpServletRequest request, Model model) {
		System.out.println("replyok()");
		
		model.addAttribute("request", request);
		command = new SpReplyokCommand();
		command.execute(model);
		
		return "redirect:list";
	}
	
	@RequestMapping("/update")
	   public String update(HttpServletRequest request, Model model) {
	      System.out.println("update()");
	      
	      model.addAttribute("request", request);
	      command = new SpUpdateCommand();
	      command.execute(model);
	      
	      return "update";
	   }
	
}
