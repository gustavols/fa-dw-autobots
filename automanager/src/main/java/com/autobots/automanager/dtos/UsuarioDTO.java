package com.autobots.automanager.dtos;

import java.util.List;

import com.autobots.automanager.entidades.CredencialUsuarioSenha;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Email;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.enumeracoes.PerfilUsuario;

import lombok.Data;

@Data
public class UsuarioDTO {
	String razaoSocial;
	Usuario usuario;
	Endereco endereco;
	List<Telefone> telefones;
	List<Documento> documentos;
	List<Email> emails;
	CredencialUsuarioSenha credencial;
	PerfilUsuario perfilUsuario;
	List<Mercadoria> mercadorias;
}
