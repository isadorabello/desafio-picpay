package io.github.isadorabello.desafio_picpay.service;

import io.github.isadorabello.desafio_picpay.infrastructure.client.AutorizacaoClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AutorizacaoService {

    private final AutorizacaoClient client;

    public boolean validarTransferencia(){
        if(Objects.equals(client.validarAutorizacao().data().authorization(), "true")){
            return true;
        }
        return false;
    }
}
