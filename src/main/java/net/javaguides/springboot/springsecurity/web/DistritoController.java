package net.javaguides.springboot.springsecurity.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.javaguides.springboot.springsecurity.model.DistritoPrecio;
import net.javaguides.springboot.springsecurity.repository.DistritoPrecioRepository;
import net.javaguides.springboot.springsecurity.service.DistritoPrecioService;



@Controller
@RequestMapping(path = "/distritoprecio")
public class DistritoController {

	@Autowired
	private DistritoPrecioService distritoPrecioService;
	@Autowired
	private DistritoPrecioRepository distritoPrecioRepository;
	
	@GetMapping("/")
	public String viewHomePage(Model model) {
		return findPaginated(1, "nombreDistrito", "asc", model);		
	}
	
	
	
	@GetMapping("/showNewDistritoPrecioForm")
	public String showNewEmpresaaForm(Model model) {
		// create model attribute to bind form data
		 
		DistritoPrecio personaa = new DistritoPrecio();
		model.addAttribute("personaa", personaa);
		return "new_DistritoPrecio";
	}
	
	@PostMapping("/saveDistritoPrecio")
	public String saveEmpresaa(@ModelAttribute("personaa") DistritoPrecio personaa,BindingResult bindingResult,RedirectAttributes redirectAttrs) {
		// save employee to database

		  if (bindingResult.hasErrors()) {
	            return "showNewDistritoPrecioForm";
	        }
	        if (distritoPrecioRepository.findFirstByNombreDistrito(personaa.getNombreDistrito()) != null) {
	            redirectAttrs
	                    .addFlashAttribute("mensaje", "Ya existe un DistritoPrecio con ese código")
	                    .addFlashAttribute("clase", "warning");
	            return "redirect:showNewDistritoPrecioForm";
	        }
	        distritoPrecioRepository.save(personaa);
	        redirectAttrs
	                .addFlashAttribute("mensaje", "Agregado correctamente")
	                .addFlashAttribute("clase", "success");
	        return "redirect:/distritoprecio/";

	    }
	


	
	@PostMapping(value = "/editar/{id}")
    public String actualizarDistritoPrecio(@ModelAttribute @Valid DistritoPrecio distritoPrecio, BindingResult bindingResult, RedirectAttributes redirectAttrs) {

        System.out.print(distritoPrecio.getId());
		if (bindingResult.hasErrors()) {
            if (distritoPrecio.getId() != 0L) {
                return "update_DistritoPrecio";
            }
            return "redirect:/distritoprecio/";
        }
        DistritoPrecio posibleDistritoPrecioExistente = distritoPrecioRepository.findFirstByNombreDistrito(distritoPrecio.getNombreDistrito());

        if (posibleDistritoPrecioExistente != null && !(posibleDistritoPrecioExistente.getId()==distritoPrecio.getId())) {
            redirectAttrs
                    .addFlashAttribute("mensaje", "Ya existe un producto con ese código")
                    .addFlashAttribute("clase", "warning");
            return "redirect:/distritoprecio/showNewDistritoPrecioForm";
        }
        distritoPrecioRepository.save(distritoPrecio);
        redirectAttrs
                .addFlashAttribute("mensaje", "Editado correctamente")
                .addFlashAttribute("clase", "success");
        return "redirect:/distritoprecio/";
    }
	
	 @GetMapping(value = "/editar/{id}")
	    public String mostrarFormularioEditar(@PathVariable int id, Model model) {
	        model.addAttribute("DistritoPrecio", distritoPrecioRepository.findById((long) id));
	        return "update_DistritoPrecio";
	    }
	

	@GetMapping( "/deleteDistritoPrecio/{id}")
	public String deletePersonaa(@PathVariable (value = "id") long id) {
		this.distritoPrecioService.deleteDistritoPrecioById(id);
		return "redirect:/distritoprecio/";
	}
	
	@GetMapping("/pageDistritoPrecio/{pageNo}")
	public String findPaginated(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		int pageSize = 7000;
		
		Page<DistritoPrecio> page = distritoPrecioService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<DistritoPrecio> listPersonaa = page.getContent();
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		
		model.addAttribute("listPersonaas", listPersonaa);
		return "index_DistritoPrecio";
	}
}
