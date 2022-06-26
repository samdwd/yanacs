package net.javaguides.springboot.springsecurity.service;

import java.util.List;

import org.springframework.data.domain.Page;

import net.javaguides.springboot.springsecurity.model.Empresa;

public interface EmpesaService {
	List<Empresa> getAllEmpresa();
	void saveEmpresa(Empresa personaa);
	Empresa getEmpresaId(long id);
	void deleteEmpresaById(long id);
	Page<Empresa> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
}
