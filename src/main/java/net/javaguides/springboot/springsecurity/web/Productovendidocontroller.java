package net.javaguides.springboot.springsecurity.web;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
import net.javaguides.springboot.springsecurity.service.ProductoVendidoService;

@Controller
public class Productovendidocontroller {

	@Autowired
	private ProductoVendidoService productoService;
	@Autowired
	private ProductosVendidosRepository productosVendidosRepository;
	@Autowired
	UserRepository userRepository;
	

	
	@Autowired
	ProfileRepository profileRepository;
	
	@GetMapping("/productovendido")
	public String viewHomePage(Model model) {
		return findPaginated(1, "id", "asc", model);		
	}

/////////////////////////////////////////
	@PostMapping("/saveProductoVendido")
	public String saveEmpresaa(@ModelAttribute("personaa") ProductoVendido personaa) throws UnsupportedEncodingException, MessagingException {
		// save employee to database

		productoService.saveProducto(personaa);
		//////////
		String name_cliente = personaa.getUsers().getFirstName();
		String nom_codigo = personaa.getCodigo();
		String nom_producto = personaa.getNombre();
		String email = personaa.getUsers().getEmail();
		
		Float precio = personaa.getPrecio();
		Float cantidad = personaa.getCantidad();
		Float tt_pedido = personaa.getTotal();
   
	        User u = userRepository.findByEmail(email);
		Profile resultado = profileRepository.findByUsers(u);
    		String phone_cliente = resultado.getPhone();

		Boolean nom_estado = personaa.getEstado();
		String estado;
		if(nom_estado == true) {
			estado="Recibido";
		}else{
			estado="Pendiente";
		}
		
		Integer num_pedido = personaa.getVenta().getId();
		sendEmail(email, name_cliente, phone_cliente, nom_codigo, nom_producto, precio, cantidad, tt_pedido, estado,num_pedido);
		
		for (User i : userRepository.findAll()) {
      	    for (Role role : i.getRoles()) {
      	    if(role.getName().equals("ROLE_ADMIN")) {
      	    	String emailadmin=i.getEmail();
      	    	sendEmail(emailadmin, name_cliente, phone_cliente, nom_codigo, nom_producto, precio, cantidad, tt_pedido, estado,num_pedido);
      			
}}}
  	 
		
/////////////////////////////////////////
	
		return "redirect:/productovendido";
	}
	
	@GetMapping("/showFormForProductovendidoUpdate/{id}")
	public String showFormForEmpresaUpdate(@PathVariable ( value = "id") long id, Model model) {
		// get employee from the service
		ProductoVendido personaa = productoService.getProductoById(id);
		// set employee as a model attribute to pre-populate the form
		model.addAttribute("personaa", personaa);
		productosVendidosRepository.save(personaa);
        
		return "update_productosvendidos";
	}
	
	
	
	
	@GetMapping("/pageProductovendido/{pageNo}")
	public String findPaginated(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		int pageSize = 5;
		
		Page<ProductoVendido> page = productoService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<ProductoVendido> listPersonaa = page.getContent();
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		
		model.addAttribute("listPersonaas", listPersonaa);
		return "index_productovendido";
	}
	
	@RequestMapping("/SearchProductosVendidos")
	public String viewHomePage(Model model, @Param("keyword") String keyword) {
		List<ProductoVendido> listProducts = productoService.listAll(keyword);
		model.addAttribute("listPersonaas", listProducts);
		model.addAttribute("keyword", keyword);
		
		return "index_productovendido";
	}
	
    @Autowired
	private JavaMailSender mailSender; 
    
    public void sendEmail(String recipientEmail, String name_cliente, String phone_cliente, String nom_codigo, String nom_producto, Float precio, Float cantidad, Float tt_pedido, String estado, Integer num_pedido)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();              
        MimeMessageHelper helper = new MimeMessageHelper(message);
        
        String paleBlueRows = "'paleBlueRows'";
        helper.setFrom("almacen.corxer@gmail.com", "Pedidos Corxer Contratistas Generales S.A.C.");
        helper.setTo(recipientEmail);
        
        String subject = "Estado de Pedido del Cliente:  "+name_cliente;
       
        String content =
        		"<div style=\"background-color:rgb(245,245,245); width:100%; padding:2%;\" >"
    	                + "<p  style=\"font-family:arial black;\">Hola "+name_cliente+" :</p>"
    	        		+ "<p>Su pedido ha sido recibido, nos comunicaremos con usted, por correo o al siguiente número:"
    	                
    	        		+"<a style=\"text-align: center; font-size:12px; font-family:arial black;\" >"+ phone_cliente +"</a>"
    	        	    +" . A continuación, se presentan los detalles del producto disponible:</p></br></br></br>"
    	                
    	                + "<div style=\"background-color:rgb(255,255,255); width:91%; padding:5%;\">"
    	                + "<div style=\" text-align: center; display:block; \">"
    	                + "<a style=\"text-align: center; font-size:25px; font-family:arial black;\" >Corxer</a><br>"
    	                + "<a style=\"text-align: center;\" >Contratistas Generales S.A.C.</a><br>"
    	                + "<a style=\"text-align: center;\" >20605413839</a><br>"
    	                + "<a style=\"text-align: center;\" >Cal. 1 Mza. B Lote. 10b - San Martin de Porres, Lima</a><br></div>"

    	                +"<p style=\"border-bottom-style:dashed; border-top-style:dashed; border-top: solid 0.5px black;\"> <p>"
      					+ "<div style=\"display:flex\">"
    	                + "<div style=\"top:-40px; margin-top:-20%; \" >"
    	    	        + "<a style=\"font-size:12px; font-family:arial black;\">Razon social / Nombres:</a><a> "+ name_cliente+"</a><br>"
    	    	    	
    	    	    	+ "<div style=\"\">"
    	    	        + "<a style=\"font-size:12px; font-family:arial black;\">Número de pedido:</a><a> "+ num_pedido+"</a>"
    	                +"</div>" 
    	                +"<p style=\"border-bottom-style:dashed; border-top-style:dashed; border-top: solid 0.5px black;\"> <p>"
    	                

	                + "<table style=\" width:90%;\" ><tr style=\" border: solid 1px #000000; background-color:rgb(240,240,240); color:rgb(51,193,255); \" >"+
	                  "<th>Cantidad</th>"+
	                  "<th style=\"\">Unidad Medida</th>"+
	                  "<th>Producto</th>"+
	                  "<th>Codigo</th>"+
	                  "<th>Precio</th>"+
	                  "<th>Total</th>"+
	                  "<th>Estado</th>"+
		                  "</tr>"+
	                  
                  "</tr>"
                  + "<tr>"
                  + "<td>"+cantidad+"</td>"
                  + "<td>metro cubico</td>"
                  + "<td>"+nom_producto+"</td>"
                  + "<td>"+nom_codigo+"</td>"
                  + "<td>"+precio+"</td>"
                  + "<td>"+tt_pedido+"</td>"
                  + "<td>"+estado+"</td>"

                  + "</tr>"+
                  "</table>";
        helper.setSubject(subject);
        helper.setText(content, true); 
        mailSender.send(message);	    }	
    
    


}
