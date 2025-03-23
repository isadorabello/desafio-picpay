package io.github.isadorabello.desafio_picpay.service;

import io.github.isadorabello.desafio_picpay.infrastructure.client.NotificacaoClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final NotificacaoClient client;

    public void enviarNotificacao(){
        client.enviarNotificacao();
    }
}
