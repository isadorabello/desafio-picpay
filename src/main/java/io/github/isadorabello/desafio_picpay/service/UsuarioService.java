package io.github.isadorabello.desafio_picpay.service;

import io.github.isadorabello.desafio_picpay.infrastructure.entity.Usuario;
import io.github.isadorabello.desafio_picpay.infrastructure.exception.UserNotFound;
import io.github.isadorabello.desafio_picpay.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;

    public Usuario buscarUsuario (Long id){
        return repository.findById(id).orElseThrow(() -> new UserNotFound("Usuário não encontrado"));
    }
}
