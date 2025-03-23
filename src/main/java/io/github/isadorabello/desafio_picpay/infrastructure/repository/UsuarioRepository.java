package io.github.isadorabello.desafio_picpay.infrastructure.repository;

import io.github.isadorabello.desafio_picpay.infrastructure.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
