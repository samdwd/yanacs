package net.javaguides.springboot.springsecurity.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.javaguides.springboot.springsecurity.model.Role;
import net.javaguides.springboot.springsecurity.model.User;
import net.javaguides.springboot.springsecurity.model.Venta;
import net.javaguides.springboot.springsecurity.model.poducto;
import net.javaguides.springboot.springsecurity.repository.ProductosVendidosRepository;
import net.javaguides.springboot.springsecurity.repository.UserRepository;
import net.javaguides.springboot.springsecurity.repository.VentasRepository;
import net.javaguides.springboot.springsecurity.service.ProductoVendidoService;


@Controller
@RequestMapping(path = "/ventas")
public class VentasController {

	@Autowired
	private ProductoVendidoService productoService;
	@Autowired
    VentasRepository ventasRepository;
	@Autowired
	 ProductosVendidosRepository productosVendidosRepository;

    @Autowired
    private UserRepository userRepository;

 @GetMapping("/")
 public String mostrarVentas(Model model) { 
	 
	   Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    UserDetails userDetails = null;
	    if (principal instanceof UserDetails) {
	      userDetails = (UserDetails) principal;
	    }			    
	    String a = userDetails.getUsername();
       User u = userRepository.findByEmail(a);
       for (Role i : u.getRoles()) {
    	    String role = i.getName();
    	    if(role.equals("ROLE_USER")) {
    	    	model.addAttribute("ventas", ventasRepository.findByUsers_id(u.getId()));
    	    }
    	    else {
    	    	model.addAttribute("ventas", ventasRepository.findAll());}
    	}
     return "ver_ventas";
 }
 
	@RequestMapping("/SearchVenta/")
	public String viewHomePage(Model model, @Param("keyword") String keyword) {
		List<Venta> listProducts = productoService.listAllVenta(keyword);
		model.addAttribute("producto", listProducts);
		model.addAttribute("keyword", keyword);
		
		return "ver_ventas";
	}
	
	
}
