package net.javaguides.springboot.springsecurity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.javaguides.springboot.springsecurity.model.Venta;

@Repository
public interface ventasRepo extends JpaRepository<Venta, Integer>{
	 @Query("SELECT p FROM Venta p WHERE CONCAT(p.users.firstName,' ',p.tipoDeEnvio) LIKE %?1%")
		public List<Venta> search(String keyword);
}
