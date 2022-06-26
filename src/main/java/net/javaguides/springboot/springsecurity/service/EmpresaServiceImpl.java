package net.javaguides.springboot.springsecurity.service;

import java.util.List;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import net.javaguides.springboot.springsecurity.model.Empresa;
import net.javaguides.springboot.springsecurity.repository.EmpresaRepository;

@Service
public class EmpresaServiceImpl implements EmpesaService {

	@Autowired
	private EmpresaRepository daoEmpresa;
	
	@Override
	public List<Empresa> getAllEmpresa() {
		return  daoEmpresa.findAll();		
	}

	@Override
	public void saveEmpresa(Empresa personaa) {
		this.daoEmpresa.save(personaa);	
		
	}

	@Override
	public Empresa getEmpresaId(long id) {
		Optional<Empresa> optional = daoEmpresa.findById(id);
		Empresa empresa = null;
		if (optional.isPresent()) {
			empresa = optional.get();
		} else {
			throw new RuntimeException(" empresa no encontrado para id :: " + id);
		}
		return empresa;
	}

	@Override
	public void deleteEmpresaById(long id) {
		this.daoEmpresa.deleteById(id);		
		
	}

	@Override
	public Page<Empresa> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return this.daoEmpresa.findAll(pageable);
	}
	
	

}
