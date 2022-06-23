package com.autobots.automanager.modelos.criadores;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.autobots.automanager.dtos.EmpresaDTO;
import com.autobots.automanager.entidades.Empresa;

@Component
public class CriadorEmpresa {
	public Empresa criar(EmpresaDTO empresaDTO) {
		
		empresaDTO.getEmpresa().setCadastro(new Date());
		empresaDTO.getEmpresa().setEndereco(empresaDTO.getEndereco());
		empresaDTO.getTelefones().forEach(telefone -> {
			empresaDTO.getEmpresa().getTelefones().add(telefone);
		});
		
		Empresa novaEmpresa = empresaDTO.getEmpresa();
		return novaEmpresa;
	}
}
