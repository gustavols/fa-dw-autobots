package com.autobots.automanager.dtos;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class UsuarioVendaDTO {
	@Id
	private Long id;
	private String nome;
	private String nomeSocial;
}
