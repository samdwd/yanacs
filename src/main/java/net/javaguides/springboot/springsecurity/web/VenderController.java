package net.javaguides.springboot.springsecurity.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import net.javaguides.springboot.springsecurity.model.DistritoPrecio;
import net.javaguides.springboot.springsecurity.model.ProductoParaVender;
import net.javaguides.springboot.springsecurity.model.ProductoVendido;
import net.javaguides.springboot.springsecurity.model.Profile;
import net.javaguides.springboot.springsecurity.model.Role;
import net.javaguides.springboot.springsecurity.model.User;
import net.javaguides.springboot.springsecurity.model.Venta;
import net.javaguides.springboot.springsecurity.model.poducto;
import net.javaguides.springboot.springsecurity.repository.DistritoPrecioRepository;
import net.javaguides.springboot.springsecurity.repository.ProductoRepository;
import net.javaguides.springboot.springsecurity.repository.ProductosVendidosRepository;
import net.javaguides.springboot.springsecurity.repository.ProfileRepository;
import net.javaguides.springboot.springsecurity.repository.UserRepository;
import net.javaguides.springboot.springsecurity.repository.VentasRepository;
import net.javaguides.springboot.springsecurity.service.ProductoVendidoService;
import net.javaguides.springboot.springsecurity.utilities.Utiles;

@Controller
@RequestMapping(path = "/vender")
public class VenderController {

	@Autowired
	private ProductoVendidoService productoService;
		@Autowired
    	private ProductoRepository productosRepository;
    	@Autowired
    	private DistritoPrecioRepository distritoPrecioRepository;
	    @Autowired
	    private VentasRepository ventasRepository;
	    @Autowired
	    private UserRepository userRepository;
	    @Autowired
	    private ProfileRepository profileRepository;
	    @Autowired
	    private ProductosVendidosRepository productosVendidosRepository;
		
	    
	    @GetMapping("/")
	    public String interfazVender(@RequestParam(defaultValue = "0") long selectseleccionado,Model model, HttpServletRequest request) {
	        model.addAttribute("producto", new poducto());
	        float total = 0;
	        ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request);
	        for (ProductoParaVender p: carrito) total += p.getTotal();
	        model.addAttribute("total", total);
	        model.addAttribute("items", productosRepository.findAll());

	        if(selectseleccionado==0) {
	        	model.addAttribute("distritoPrecio", distritoPrecioRepository.findAll());
	        	
	        }else {
	        	model.addAttribute("distritoPrecio", distritoPrecioRepository.findById(selectseleccionado));
	        }
	        
	        model.addAttribute("venta", new Venta(null,null,null,null,null));
	        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	    UserDetails userDetails = null;
    	    if (principal instanceof UserDetails) {userDetails = (UserDetails) principal;}
    	    
    	    String a = userDetails.getUsername();
    	    User u = userRepository.findByEmail(a);

    		Profile resultado = profileRepository.findByUsers(u);
    		String direccion_cliente = resultado.getDireccion();
    		String phone_cliente = resultado.getPhone();
    	    String name_cliente=u.getFirstName();
    	    String doc_cliente=u.getLastName();
    	    String correo_cliente=u.getEmail();
			String fechahora=Utiles.obtenerSoloFechaActual();

	        model.addAttribute("direccion_cliente", direccion_cliente);
	        model.addAttribute("phone_cliente", phone_cliente);
	        model.addAttribute("name_cliente", name_cliente);
	        model.addAttribute("doc_cliente", doc_cliente);
	        model.addAttribute("correo_cliente", correo_cliente);
	        model.addAttribute("fechahora", fechahora);
	        
	        return "vender";}

	    
	    private void limpiarCarrito(HttpServletRequest request) {
	        this.guardarCarrito(new ArrayList<>(), request);}

	    
	    @GetMapping(value = "/limpiar")
	    public String cancelarVenta(HttpServletRequest request, RedirectAttributes redirectAttrs) {
	        this.limpiarCarrito(request);
	        redirectAttrs
	                .addFlashAttribute("mensaje", "Pedido cancelado")
	                .addFlashAttribute("clase", "info");
	        return "redirect:/vender/";}
	    
	    
	    @PostMapping(value = "/terminar")
	    public String terminarVenta( @RequestParam (value = "distritoPrecio") long distritoPrecio, @ModelAttribute("venta") Venta venta, BindingResult bindingResult, HttpServletRequest request, RedirectAttributes redirectAttrs) throws UnsupportedEncodingException, MessagingException {
	        ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request);
	        if (carrito == null || carrito.size() <= 0) {
	            return "redirect:/vender/"; }
			  if (bindingResult.hasErrors()) {
				   return "redirect:/vender/";}
		        
        	    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        	    UserDetails userDetails = null;
        	    if (principal instanceof UserDetails) {userDetails = (UserDetails) principal;}
        	    
        	    String a = userDetails.getUsername();
        	    User u = userRepository.findByEmail(a);

    	        DistritoPrecio mdistritoPrecio = distritoPrecioRepository.findById(distritoPrecio);
    	        String namedistrito= mdistritoPrecio.getNombreDistrito();
        		Profile resultado = profileRepository.findByUsers(u);
        		String direccion = "";
        	    String listaprodcs = "";
	    	    String name_cliente=u.getFirstName();
	    	    String doc_cliente=u.getLastName();
	    	    String correo_cliente=u.getEmail();
        	    Integer num_pedido = null;
        	    String tt_pedido_str = "";
        	    double tt_pedido = 0.0;
  			  	String fechahora=Utiles.obtenerFechaYHoraActual();
			  	Venta venta1 = new Venta(fechahora,venta.getFormaDePago(),venta.getTipoDeEnvio(), u, mdistritoPrecio);
				Venta v = ventasRepository.save(venta1);
		        String strformaDePago =venta.getFormaDePago();
		   	    String strtipoDeEnvio=venta.getTipoDeEnvio();
		   	    
		   	for (ProductoParaVender productoParaVender : carrito) {
	            poducto p = productosRepository.findById(productoParaVender.getId()).orElse(null);
	            if (p == null) continue; // Si es nulo o no existe, ignoramos el siguiente código con continue
	            // Le restamos existencia
	            p.restarExistencia(productoParaVender.getCantidad());
	            productosRepository.save(p);
	            
		        boolean estado = false ;         
	            ProductoVendido productoVendidos = null;
	            // Creamos un nuevo producto que será el que se guarda junto con la venta
	            ProductoVendido productoVendido = new ProductoVendido(productoParaVender.getCantidad(), productoParaVender.getPrecio(), productoParaVender.getNombre(), productoParaVender.getCodigo(), estado,v,u);
		        
	            // data para email
	        	listaprodcs =listaprodcs+ 
		                "<tr style=\"background-color:rgb(250,250,250);\">"+
		                "  <td style=\"width:10%\">"+productoParaVender.getCantidad()+"</td>"+
		                "  <td style=\"width:10%\">metro cubico</td>"+
    	                "  <td>"+productoParaVender.getNombre()+"</td>"+
    					"  <td>"+productoParaVender.getCodigo()+"</td>"+
		    	                "  <td>"+productoParaVender.getPrecio()+"</td>"+
		    	                "  <td>"+productoParaVender.getTotal().toString() +"</td>"+
		    	                "</tr>";    
	        	/////////////
	        	num_pedido =productoVendido.getVenta().getId();
	        	tt_pedido_str = productoVendido.getTotal().toString();
	        	double doble = Double.parseDouble(tt_pedido_str);
	        	tt_pedido = doble+ tt_pedido;
	            productosVendidosRepository.save(productoVendido);}
        	
		   	System.out.print(strtipoDeEnvio); 

		   	if (strtipoDeEnvio.equals("Envio a domicilio")) {
		   		direccion ="<p>El envio a domicilio se realizara en la siguiente dirección: <a style=\"font-family:arial black;\">"+resultado.getDireccion()+"</a></p>";
		   	};
		   	
		   	if (strtipoDeEnvio.equals("Recojo a local")) {
		   		direccion ="<p>El recojo a local se realizara en nuestro local ubicado en: <a style=\"font-family:arial black;\">"+"Cal. 1 Mza. B Lote. 10b Huertos del Naranjal, San Martin de Porres, Lima"+"</a></p>";
		   	};
		   	
		   	if (strtipoDeEnvio.equals("Cordinación")) {
		   		direccion ="";};
		   		
        	String email = correo_cliente; 
			sendEmail(email,listaprodcs,num_pedido,tt_pedido, name_cliente, direccion , doc_cliente, correo_cliente, strformaDePago, strtipoDeEnvio, fechahora,namedistrito);

			for (User i : userRepository.findAll()) {
	      	    for (Role role : i.getRoles()) {
	      	    if(role.getName().equals("ROLE_ADMIN")) {
	      	    	String emailadmin=i.getEmail();
	      	    	sendEmail(emailadmin,listaprodcs,num_pedido,tt_pedido, name_cliente,direccion, doc_cliente, correo_cliente, strformaDePago, strtipoDeEnvio, fechahora,namedistrito);}}}
	    	
	        this.limpiarCarrito(request);
	        redirectAttrs
	                .addFlashAttribute("mensaje", "Pedido realizado correctamente")
	                .addFlashAttribute("clase", "success");
	        return "redirect:/ventas/";}
	    

	    private ArrayList<ProductoParaVender> obtenerCarrito(HttpServletRequest request) {
	        ArrayList<ProductoParaVender> carrito = (ArrayList<ProductoParaVender>) request.getSession().getAttribute("carrito");
	        if (carrito == null) {
	            carrito = new ArrayList<>();}
	        return carrito;}

	    private void guardarCarrito(ArrayList<ProductoParaVender> carrito, HttpServletRequest request) {
	        request.getSession().setAttribute("carrito", carrito);}
	    
	       
	    @PostMapping(value = "/agregar")
	    public String agregarAlCarrit(@RequestParam ( value = "distritoPrecioRecibido") long distritoPrecio, @ModelAttribute poducto producto, HttpServletRequest request, RedirectAttributes redirectAttrs) {
	        ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request);
	        
	        poducto productoBuscadoPorCodigo = productosRepository.findFirstByNombre(producto.getNombre());
	        DistritoPrecio mdistritoPrecio = distritoPrecioRepository.findById(distritoPrecio);
	        float precioUno = productoBuscadoPorCodigo.getPrecio();
	        float precio1= precioUno+(precioUno * (mdistritoPrecio.getDistrito_precio()/100));
	        final String nameDistrito = mdistritoPrecio.getNombreDistrito();
	        productoBuscadoPorCodigo.setPrecio(precio1);
	        float cantida_add=producto.getExistencia();
	        
	        if (productoBuscadoPorCodigo.sinExistencia()) {
	            redirectAttrs
	                    .addFlashAttribute("mensaje", "El producto está agotado")
	                    .addFlashAttribute("clase", "warning");
		        return "redirect:/vender/?selectseleccionado="+distritoPrecio;}
	        
	        boolean encontrado = false;
	        for (ProductoParaVender productoParaVenderActual : carrito) {
	        	float cantidad_sumada = productoParaVenderActual.getCantidad()+producto.getExistencia();
        	
	        	if (cantidad_sumada-1>=productoBuscadoPorCodigo.getExistencia()) {
		            redirectAttrs
		                    .addFlashAttribute("mensaje", "La cantidad solicitada excede el producto")
		                    .addFlashAttribute("clase", "warning");
			        return "redirect:/vender/?selectseleccionado="+distritoPrecio;}
		        
	            if (productoParaVenderActual.getCodigo().equals(productoBuscadoPorCodigo.getCodigo())) {
	            	productoParaVenderActual.setCantidad(cantidad_sumada);
	    	        encontrado = true;
	                break;}}
	        
	        if (!encontrado) {
	            carrito.add(new ProductoParaVender(productoBuscadoPorCodigo.getNombre(), productoBuscadoPorCodigo.getCodigo(), productoBuscadoPorCodigo.getPrecio(), productoBuscadoPorCodigo.getExistencia(), productoBuscadoPorCodigo.getId(), cantida_add));
	          }
	        
	        this.guardarCarrito(carrito, request);
	        return "redirect:/vender/?selectseleccionado="+distritoPrecio;}
	    
	    
	    @PostMapping(value = "/quitar/{indice}")
	    public String quitarDelCarrito(@PathVariable int indice, HttpServletRequest request) {
	        ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request);
	        if (carrito != null && carrito.size() > 0 && carrito.get(indice) != null) {
	            carrito.remove(indice);
	            this.guardarCarrito(carrito, request);}
	        return "redirect:#";}
	    
	    
	    @Autowired
		private JavaMailSender mailSender; 
	    public void sendEmail(String recipientEmail, String listaprodcs, Integer num_pedido, 
	    		double tt_pedido, String name_cliente, String direccion, String doc_cliente, String correo_cliente, 
	    		String strformaDePago, String strtipoDeEnvio, String fechahora,String namedistrito)
	    
	            throws MessagingException, UnsupportedEncodingException {
	        MimeMessage message = mailSender.createMimeMessage();              
	        MimeMessageHelper helper = new MimeMessageHelper(message);
	        
	        String paleBlueRows = "'paleBlueRows'";
	        helper.setFrom("samucux1@gmail.com", "Pedidos Corxer Contratistas Generales S.A.C.");
	        helper.setTo(recipientEmail);
	        
	        String subject = "Tu pedido  "+name_cliente+" ha sido generado:";
	       
	        String content =
	        		"<div style=\"background-color:rgb(245,245,245); width:100%; padding:2%;\" >"
	                + "<p  style=\"font-family:arial black;\">Hola "+name_cliente+" :</p>"
	        		+ "<p>Tu pedido ha sido recibido y ahora esta siendo procesado. Los detalles del pedido se muestran debajo la referencia:</p></br></br></br>"
	                
	                + "<div style=\"background-color:rgb(255,255,255); width:91%; padding:5%;\">"
	                + "<div style=\" text-align: center; display:block; \">"
	                + "<a style=\"text-align: center; font-size:25px; font-family:arial black;\" >Corxer</a><br>"
	                + "<a style=\"text-align: center;\" >Contratistas Generales S.A.C.</a><br>"
	                + "<a style=\"text-align: center;\" >20605413839</a><br>"
	                + "<a style=\"text-align: center;\" >Cal. 1 Mza. B Lote. 10b - San Martin de Porres, Lima</a><br></div>"

	                +"<p style=\"border-bottom-style:dashed; border-top-style:dashed; border-top: solid 0.5px black;\"> <p>"
  					+ "<div style=\"display:flex\">"
	                + "<div style=\"top:-40px; margin-top:-20%; \" >"
	    	        + "<a style=\"font-size:12px; font-family:arial black;\">Correo:</a><a> "+ correo_cliente+"</a><br>"
	    	        + "<a style=\"font-size:12px; font-family:arial black;\">Razon social / Nombres:</a><a> "+ name_cliente+"</a><br>"
	    	    	+ "<a style=\"font-size:12px; font-family:arial black;\">Ruc / Dni:</a><a> "+ doc_cliente+"</a></div></div>"
	              
	    	    	+ "<div style=\"\">"
	    	        + "<a style=\"font-size:12px; font-family:arial black;\">Fecha y Hora:</a><a> "+ fechahora+"</a><br>"
	    	                + "<a style=\"font-size:12px; font-family:arial black;\">Número de pedido:</a><a> "+ num_pedido+"</a>"
	                + "<a style=\"font-size:12px; font-family:arial black;\">Tarifa de Distrito:</a><a> "+ namedistrito+"</a>"
	                +"</div>" 
	                +"<p style=\"border-bottom-style:dashed; border-top-style:dashed; border-top: solid 0.5px black;\"> <p>"
	                
	                + "<table style=\" width:90%;\" ><tr style=\" border: solid 1px #000000; background-color:rgb(240,240,240); color:rgb(51,193,255); \" >"+
	                  "<th>Cantidad</th>"+
	                  "<th style=\"\">Unidad Medida</th>"+
	                  "<th>Producto</th>"+
	                  "<th>Codigo</th>"+
	                  "<th>Precio</th>"+
	                  "<th>Total</th>"+
	                  "</tr>"+
	                listaprodcs+
	                  "</table>"+
	                  "<p style=\"background: rgb(240,240,240); color: #0c92ac; font-weight: bold; padding: 15px; border: 2px solid #abecf9; border-radius: 6px;\">El importe total del pedido es: S/."+
	                  tt_pedido+
	                  "</p> <br><br>" 
	                  + "<a style=\"text-align: center; font-size:15px; font-family:arial black;\" >Gracias por su pedido, conserve este comprobante en caso de reclamo o devolucion</a><br>"
	       	               
	                  + "</div>"
	                
	                  

	                +"<p style=\"border-bottom-style:dashed; border-top-style:dashed; border-top: solid 0.5px black;\"> <p>"
	                + "<p style=\"font-family:arial black;\">Metodo de pago escogido: "
	                + strformaDePago
	                + "</p>" 
	                + "<p style=\"font-family:arial black;\">Tipo de envio escogido: "
	    	        + strtipoDeEnvio
	    	        + "</p>"
	    	        + direccion
	    	        + "<p>Recuerda que puedes realizar tu pago directamente en nuestra cuenta bancaria a través de internet, depósitos en cajeros o ventanillas y en agentes disponibles. Por favor, usa tu identificador de pedido como referenciade pago. El pedido no se enviara hasta que recibamos importe en nuestra cuenta y la confirmación de su pago via e-mail.</p>"
	    	        + "<p>En caso de escoger opcion recojo a domicilio, una vez realizado el pago, enviar domicilio a destino, en caso de mencionar pagos tipo POS o Web tarjeta, envie la imagen adjunta al email.</p>"
	    	        + "<p>Tienes 72 horas para pagar, de lo contrario tu pedido sera cancelado </p>"
	    	        +"<p style=\"font-family:arial black;\">Número de cuenta: 3231231546488978</p>"
	    	        + "</div>";
	         
	        helper.setSubject(subject);
	        helper.setText(content, true); 
	        mailSender.send(message);	    }	    
	    
	


}
