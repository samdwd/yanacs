package net.javaguides.springboot.springsecurity.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import net.javaguides.springboot.springsecurity.model.DistritoPrecio;
import net.javaguides.springboot.springsecurity.repository.DistritoPrecioRepository;

@Service
public class DistritoPrecioServiceImpl implements DistritoPrecioService{

	@Autowired
	private DistritoPrecioRepository daoProd;
	
	@Override
	public List<DistritoPrecio> getAllDistritoPrecio() {
		return  daoProd.findAll();		
	}

	@Override
	public void saveDistritoPrecio(DistritoPrecio personaa) {
		this.daoProd.save(personaa);		
	}

	@Override
	public DistritoPrecio getDistritoPrecioById(long id) {
		DistritoPrecio optional = daoProd.findById(id);
		DistritoPrecio producto = null;
		if (optional.isPresent()) {
			producto = optional.get();
		} else {
			throw new RuntimeException(" producto no encontrado para id :: " + id);
		}
		return producto;
	}

	@Override
	public void deleteDistritoPrecioById(long id) {
		this.daoProd.deleteById(id);
		
	}

	@Override
	public Page<DistritoPrecio> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return this.daoProd.findAll(pageable);
	}

	@Override
	public List<DistritoPrecio> listaDistritoPrecio() {
		return daoProd.findAll();
	}
	
	

}
