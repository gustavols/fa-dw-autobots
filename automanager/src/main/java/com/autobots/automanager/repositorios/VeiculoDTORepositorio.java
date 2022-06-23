package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.dtos.VeiculoDTO;

public interface VeiculoDTORepositorio extends JpaRepository<VeiculoDTO, Long>{
}