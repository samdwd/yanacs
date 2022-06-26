package net.javaguides.springboot.springsecurity.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.javaguides.springboot.springsecurity.model.ProductoVendido;
import net.javaguides.springboot.springsecurity.model.Profile;
import net.javaguides.springboot.springsecurity.model.Role;
import net.javaguides.springboot.springsecurity.model.User;
import net.javaguides.springboot.springsecurity.model.poducto;
import net.javaguides.springboot.springsecurity.repository.ProductosVendidosRepository;
import net.javaguides.springboot.springsecurity.repository.ProfileRepository;
import net.javaguides.springboot.springsecurity.repository.UserRepository;
import net.javaguides.springboot.springsecurity.repository.VentasRepository;


@Controller
@RequestMapping(path = "/perfil")
public class PerfilController {

	@Autowired
    VentasRepository ventasRepository;
	@Autowired
	 ProductosVendidosRepository productosVendidosRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProfileRepository profileRepository;

 @GetMapping("/")
 public String mostrarPerfil(Model model) { 
	   Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    UserDetails userDetails = null;
	    if (principal instanceof UserDetails) {
	      userDetails = (UserDetails) principal;
	    }			    
	    

      	Boolean pendiente = false;
      	Boolean activos = true;
	    String a = userDetails.getUsername();
      User u = userRepository.findByEmail(a);
  
      for (Role i : u.getRoles()) {
   	    String role = i.getName();
   	    if(role.equals("ROLE_USER")) {
   	     	 model.addAttribute("num_pedidos", productosVendidosRepository.countByUsers_id(u.getId()));
   	     	 model.addAttribute("num_pedidos_pendientes", productosVendidosRepository.countByEstadoAndUsers_id(pendiente, u.getId()));
          	   	 model.addAttribute("num_pedidos_realizados", productosVendidosRepository.countByEstadoAndUsers_id(activos, u.getId()));
         
   	    }
   	    else {

   	     model.addAttribute("num_pedidos", productosVendidosRepository.countByUsers_id(u.getId()));
	     	 model.addAttribute("num_pedidos_pendientes", productosVendidosRepository.countByEstadoAndUsers_id(pendiente, u.getId()));
      	   	 model.addAttribute("num_pedidos_realizados", productosVendidosRepository.countByEstadoAndUsers_id(activos, u.getId()));

   	    }}	

	  String phone = "";
	  String direccion = "";
	  Profile resultado = profileRepository.findByUsers(u);
	  if(resultado==null) {
		  phone = "";
		  direccion = "";
		  return "redirect:/perfil/new/";
	  }
	  else {
		  phone = resultado.getPhone();
		  direccion = resultado.getDireccion();
			}
	 
      model.addAttribute("nombres", u.getFirstName());
      model.addAttribute("ruc", u.getLastName());
      model.addAttribute("correo", u.getEmail());
      
      
      
      model.addAttribute("phone",phone);
      model.addAttribute("direccion",direccion);
	  return "ver_perfil";
 }
 
 

 @GetMapping("/new")
 public String newPerfil(Model model) { 
	   Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    UserDetails userDetails = null;
	    if (principal instanceof UserDetails) {
	      userDetails = (UserDetails) principal;}
	    
	    String a = userDetails.getUsername();
      User u = userRepository.findByEmail(a);

	  String phone = "";
	  String direccion = "";
	  Profile resultado = profileRepository.findByUsers(u);
	  if(resultado==null) {
		  phone = "";
		  direccion = "";
	  }
	  else {
		  phone = resultado.getPhone();
		  direccion = resultado.getDireccion();
			}
	  
      model.addAttribute("nombres", u.getFirstName());
      model.addAttribute("ruc", u.getLastName());
      model.addAttribute("correo", u.getEmail());
      model.addAttribute("phone",phone);
      model.addAttribute("direccion",direccion);
	  return "add_perfil";
 }
 
 
	
	@GetMapping("/profileSave/")
	public String profileSave(
			@RequestParam ( value = "direccion") String direccion,
			@RequestParam ( value = "phone") String phone,
			Model model) {
		System.out.print("entrando");
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		    UserDetails userDetails = null;
		    if (principal instanceof UserDetails) {
		      userDetails = (UserDetails) principal;}
		    
		    String a = userDetails.getUsername();
	      User u = userRepository.findByEmail(a);
	      Profile id = profileRepository.findByUsers(u);

		  Profile personaa = null;
	      Profile resultado = profileRepository.findByUsers(u);
		  if(resultado==null) {
			  personaa = new Profile(phone,direccion,u);
		  }
		  else { 
			  personaa = new Profile(id.getId(),phone,direccion,u);
		     }

			profileRepository.save(personaa);
		  
		  return "redirect:/";
	}




	
	
}
