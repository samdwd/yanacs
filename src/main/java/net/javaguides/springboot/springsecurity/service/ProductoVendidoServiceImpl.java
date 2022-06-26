package net.javaguides.springboot.springsecurity.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import net.javaguides.springboot.springsecurity.model.ProductoVendido;
import net.javaguides.springboot.springsecurity.model.Venta;
import net.javaguides.springboot.springsecurity.repository.VentasRepository;
import net.javaguides.springboot.springsecurity.repository.productovendidorepo;
import net.javaguides.springboot.springsecurity.repository.ventasRepo;

@Service

public class ProductoVendidoServiceImpl implements ProductoVendidoService {

	@Autowired
	private productovendidorepo daoProd;
	
	@Autowired
	private ventasRepo ventaRepo;
	
	@Override
	public void saveProducto(ProductoVendido personaa) {
		this.daoProd.save(personaa);		
	}

	@Override
	public ProductoVendido getProductoById(long id) {
		Optional<ProductoVendido> optional = daoProd.findById((int) id);
		ProductoVendido producto = null;
		if (optional.isPresent()) {
			producto = optional.get();
		} else {
			throw new RuntimeException(" producto no encontrado para id :: " + id);
		}
		return producto;
	}

	@Override
	public void deleteProductoById(long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Page<ProductoVendido> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return this.daoProd.findAll(pageable);	}

	@Override
	public List<ProductoVendido> getAllProducto() {
		return  daoProd.findAll();		

	}

	@Override
	public List<ProductoVendido> listAll(String keyword) {
		if (keyword != null) {
			return daoProd.search(keyword);
		}
		return daoProd.findAll();
	}

	@Override
	public List<Venta> listAllVenta(String keyword) {
		if (keyword != null) {
			return ventaRepo.search(keyword);
		}
		return ventaRepo.findAll();
	}


}
