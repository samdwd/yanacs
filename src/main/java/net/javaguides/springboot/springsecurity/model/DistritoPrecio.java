package net.javaguides.springboot.springsecurity.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class DistritoPrecio {
	 @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private long id;

	

		private String nombreDistrito;
	    
	    private Float distrito_precio;

	    @OneToMany(mappedBy = "distritoPrecio", cascade = CascadeType.ALL)
	    private Set<Venta> ventas;
	    
		public String getNombreDistrito() {
			return nombreDistrito;
		}

		public void setNombreDistrito(String nombreDistrito) {
			this.nombreDistrito = nombreDistrito;
		}

		public Float getDistrito_precio() {
			return distrito_precio;
		}

		public void setDistrito_precio(Float distrito_precio) {
			this.distrito_precio = distrito_precio;
		}

		public DistritoPrecio(@NotNull(message = "Debes especificar el nombre") @Size(min = 1, max = 50, message = "El nombre debe medir entre 1 y 50") String nombreDistrito,
				@NotNull(message = "Debes especificar el precio") @Min(value = 0, message = "El precio mínimo es 0") Float distrito_precio) {
			this.nombreDistrito = nombreDistrito;
			this.distrito_precio = distrito_precio;
		}
		
	    public DistritoPrecio(long id, String nombreDistrito, Float distrito_precio) {
			this.id = id;
			this.nombreDistrito = nombreDistrito;
			this.distrito_precio = distrito_precio;
		}
		public DistritoPrecio() {
			// TODO Auto-generated constructor stub
		}

		public long getId() {
			return id;
		}

	    public void setId(long id) {
	        this.id = id;
	    }

		public boolean isPresent() {
			// TODO Auto-generated method stub
			return false;
		}

		public DistritoPrecio get() {
			// TODO Auto-generated method stub
			return null;
		}

	    
	    
}
