package com.autobots.automanager.dtos;

import java.util.List;

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Telefone;

import lombok.Data;

@Data
public class EmpresaDTO {
	Empresa empresa;
	Endereco endereco;
	List<Telefone> telefones;
}
