package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.dtos.UsuarioVendaDTO;

public interface UsuarioVendaDTORepositorio extends JpaRepository<UsuarioVendaDTO, Long>{
}