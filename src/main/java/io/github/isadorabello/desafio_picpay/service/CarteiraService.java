package io.github.isadorabello.desafio_picpay.service;

import io.github.isadorabello.desafio_picpay.infrastructure.entity.Carteira;
import io.github.isadorabello.desafio_picpay.infrastructure.entity.Usuario;
import io.github.isadorabello.desafio_picpay.infrastructure.exception.UserNotFound;
import io.github.isadorabello.desafio_picpay.infrastructure.repository.CarteiraRepository;
import io.github.isadorabello.desafio_picpay.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarteiraService {

    private final CarteiraRepository repository;

    public void salvar (Carteira carteira){
        repository.save(carteira);
    }
}
