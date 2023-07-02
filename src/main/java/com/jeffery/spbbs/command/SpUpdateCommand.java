package com.jeffery.spbbs.command;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.jeffery.spbbs.dao.SpDAO;
import com.jeffery.spbbs.dto.SpDTO;

public class SpUpdateCommand implements SpCommand {

	@Override
	public void execute(Model model) {
		// TODO Auto-generated method stub

	      Map<String, Object> map = model.asMap();
	      HttpServletRequest request =(HttpServletRequest) map.get("request");
	      String num = request.getParameter("num");
	      
	      SpDAO dao = new SpDAO();
	      SpDTO dto = dao.update(num);
	      
	      model.addAttribute("update", dto);

	}

}
