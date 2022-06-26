package net.javaguides.springboot.springsecurity.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.javaguides.springboot.springsecurity.model.Empresa;
import net.javaguides.springboot.springsecurity.service.EmpesaService;

@Controller
public class EmpresaController {
	
	@Autowired
	private EmpesaService empresaService;

	@GetMapping("/Empresa")
	public String viewHomePage(Model model) {
		return findPaginated(1, "nombres", "asc", model);		
	}

	@GetMapping("/showNewEmpresaForm")
	public String showNewEmpresaaForm(Model model) {
		// create model attribute to bind form data
		 
		Empresa personaa = new Empresa();
		model.addAttribute("personaa", personaa);
		return "new_Empresa";
	}
	

	@PostMapping("/saveEmpresa")
	public String saveEmpresaa(@ModelAttribute("personaa") Empresa personaa) {
		// save employee to database

		empresaService.saveEmpresa(personaa);
		return "redirect:/Empresa";
	}
	@GetMapping("/showFormForEmpresaUpdate/{id}")
	public String showFormForEmpresaUpdate(@PathVariable ( value = "id") long id, Model model) {
		
		
		// get employee from the service
		Empresa personaa = empresaService.getEmpresaId(id);
		
		// set employee as a model attribute to pre-populate the form
		model.addAttribute("personaa", personaa);
		return "update_Empresa";
	}
	@GetMapping("/deleteEmpresa/{id}")
	public String deletePersonaa(@PathVariable (value = "id") long id) {
		
		// call delete employee method 
		this.empresaService.deleteEmpresaById(id);
		return "redirect:/Empresa";
	}
	@GetMapping("/pageEmpresa/{pageNo}")
	public String findPaginated(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		int pageSize = 5000;
		
		Page<Empresa> page = empresaService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Empresa> listPersonaa = page.getContent();
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		
		model.addAttribute("listPersonaas", listPersonaa);
		return "index_empresa";
	}
}
