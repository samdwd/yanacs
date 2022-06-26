package net.javaguides.springboot.springsecurity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.javaguides.springboot.springsecurity.model.poducto;

@Repository
public interface ProductoRepository extends JpaRepository<poducto, Long>{
	poducto findFirstByNombre(String nombre);
	
	
	 @Query("SELECT p FROM poducto p WHERE CONCAT(p.codigo, ' ', p.nombre, ' ', p.existencia, ' ',p.precio) LIKE %?1%")
		public List<poducto> search(String keyword);
}
