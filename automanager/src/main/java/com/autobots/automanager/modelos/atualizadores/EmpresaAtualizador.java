package com.autobots.automanager.modelos.atualizadores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.modelos.VerificadorNulo;

@Component
public class EmpresaAtualizador {
	
	@Autowired
	VerificadorNulo verificador;
	
	public void atualizar(Empresa empresa, Empresa atualizacao) {
		
		if (atualizacao != null) {
			 if (!verificador.verificar(atualizacao.getRazaoSocial())) {
			        empresa.setRazaoSocial(atualizacao.getRazaoSocial());
			      }

			      if (!verificador.verificar(atualizacao.getNomeFantasia())) {
			        empresa.setNomeFantasia(atualizacao.getNomeFantasia());
			      }

			      if (!verificador.verificar(atualizacao.getEndereco())) {
			        empresa.setEndereco(atualizacao.getEndereco());
			      }

			      if (!verificador.verificar(atualizacao.getCadastro())) {
			        empresa.setCadastro(atualizacao.getCadastro());
			      }
			}
		
	}
}
