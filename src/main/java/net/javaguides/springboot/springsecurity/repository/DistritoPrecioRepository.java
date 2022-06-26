package net.javaguides.springboot.springsecurity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.javaguides.springboot.springsecurity.model.DistritoPrecio;

@Repository
public interface DistritoPrecioRepository extends JpaRepository<DistritoPrecio, Long>{
	DistritoPrecio findFirstByNombreDistrito(String nome);

	DistritoPrecio findById(long distritoPrecio);
}
