package com.autobots.automanager.modelos;

import java.util.Date;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.enumeracoes.TipoDocumento;
import com.autobots.automanager.enumeracoes.TipoVeiculo;

@Component
public class VerificadorNulo {
	public boolean verificar(String dado) {
		boolean nulo = true;
		if (!(dado == null)) {
			if (!dado.isBlank()) {
				nulo = false;
			}
		}
		return nulo;
	}

	public boolean verificar(TipoDocumento tipoDocumento) {
		boolean nulo = true;
		if (!(tipoDocumento == null)) {
			nulo = false;
		}
		return nulo;
	}

	public boolean verificar(TipoVeiculo tipoVeiculo) {
		boolean nulo = true;
		if (!(tipoVeiculo == null)) {
			nulo = false;
		}
		return nulo;
	}

	public boolean verificar(Endereco endereco) {
		boolean nulo = true;
		if (!(endereco == null)) {
			nulo = false;
		}
		return nulo;
	}

	public boolean verificar(Set<?> tipo) {
		boolean nulo = true;
		if (!(tipo == null)) {
			nulo = false;
		}
		return nulo;
	}

	public boolean verificar(Usuario usuario) {
		boolean nulo = true;
		if (!(usuario == null)) {
			nulo = false;
		}
		return nulo;
	}

	public boolean verificar(Date cadastro) {
		boolean nulo = true;
		if (!(cadastro == null)) {
			nulo = false;
		}
		return nulo;
	}
}
