package net.javaguides.springboot.springsecurity.service;

import java.util.List;

import org.springframework.data.domain.Page;

import net.javaguides.springboot.springsecurity.model.DistritoPrecio;


public interface DistritoPrecioService {

	List<DistritoPrecio> getAllDistritoPrecio();
	void saveDistritoPrecio(DistritoPrecio personaa);
	DistritoPrecio getDistritoPrecioById(long id);
	void deleteDistritoPrecioById(long id);
	Page<DistritoPrecio> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
	 List<DistritoPrecio> listaDistritoPrecio();
}
