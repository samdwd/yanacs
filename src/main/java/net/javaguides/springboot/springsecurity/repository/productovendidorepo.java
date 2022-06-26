package net.javaguides.springboot.springsecurity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.javaguides.springboot.springsecurity.model.ProductoVendido;

@Repository
public interface productovendidorepo extends JpaRepository<ProductoVendido, Integer>{
	
	 @Query("SELECT p FROM ProductoVendido p WHERE CONCAT(p.users.email, ' ',p.users.firstName, ' ', p.nombre, ' ', p.estado) LIKE %?1%")
		public List<ProductoVendido> search(String keyword);
}
