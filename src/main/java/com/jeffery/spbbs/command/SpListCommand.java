package com.jeffery.spbbs.command;

import java.util.ArrayList;

import org.springframework.ui.Model;

import com.jeffery.spbbs.dao.SpDAO;
import com.jeffery.spbbs.dto.SpDTO;

public class SpListCommand implements SpCommand {

	@Override
	public void execute(Model model) {
		
		SpDAO dao = new SpDAO();
		ArrayList<SpDTO> dtos = dao.list();
		
		model.addAttribute("list", dtos);
		
	}

}
