package io.github.isadorabello.desafio_picpay.service;

import io.github.isadorabello.desafio_picpay.controller.TransacaoDTO;
import io.github.isadorabello.desafio_picpay.infrastructure.entity.TipoTransferencia;
import io.github.isadorabello.desafio_picpay.infrastructure.entity.TipoUsuario;
import io.github.isadorabello.desafio_picpay.infrastructure.entity.Transacoes;
import io.github.isadorabello.desafio_picpay.infrastructure.entity.Usuario;
import io.github.isadorabello.desafio_picpay.infrastructure.exception.BadRequestException;
import io.github.isadorabello.desafio_picpay.infrastructure.repository.TransacaoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final UsuarioService usuarioService;
    private final AutorizacaoService autorizacaoService;
    private final CarteiraService carteiraService;
    private final TransacaoRepository repository;
    private final NotificacaoService notificacaoService;

    @Transactional
    public void transferenciaValores(TransacaoDTO dto){
        Usuario payer = usuarioService.buscarUsuario(dto.payer());
        Usuario payee = usuarioService.buscarUsuario(dto.payee());

        // validar se o pagador é um lojista (caso seja, transferência não é autorizada)
        validarPagador(payer);
        // validar se o pagador é tem saldo suficente para fazer a transferência
        validarSaldo(payer, dto.value());
        // a partir de um serviço autorizador externo (mock fornceido), validar se a transferência foi autorizada
        validarTransferencia();

        // atualizando a carteira do pagador
        atualizarSaldo(payer, dto.value(), TipoTransferencia.PAGADOR);
        // atualizando a carteira do recebedor
        atualizarSaldo(payee, dto.value(),  TipoTransferencia.RECEBEDOR);

        Transacoes transacoes = Transacoes.builder()
                .valor(dto.value())
                .pagador(payer)
                .recebedor(payee)
                .build();

        repository.save(transacoes);
        enviarNotificacao();
    }

    private void validarPagador(Usuario user){
        try {
            if(user.getTipoUsuario().equals(TipoUsuario.LOJISTA)){
                throw new IllegalArgumentException("Transação não autorizada para esse tipo de usuário");
            }
        }catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private void validarSaldo(Usuario user, BigDecimal valor){
        try {
            if(user.getCarteira().getSaldo().compareTo(valor) < 0){
                throw new IllegalArgumentException("Transação não autorizada - saldo insuficiente");
            }
        }catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private void validarTransferencia(){
        try {
            if(!autorizacaoService.validarTransferencia()){
                throw new IllegalArgumentException("Transação não autorizada - API");
            }
        }catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private void atualizarSaldo(Usuario usuario, BigDecimal valor, TipoTransferencia tipo){
        if(tipo.equals(TipoTransferencia.PAGADOR)){
            usuario.getCarteira().setSaldo(usuario.getCarteira().getSaldo().subtract(valor));
        }else{
            usuario.getCarteira().setSaldo(usuario.getCarteira().getSaldo().add(valor));
        }
        carteiraService.salvar(usuario.getCarteira());
    }

    private void enviarNotificacao(){
        try {
           notificacaoService.enviarNotificacao();
        }catch (HttpClientErrorException e){
            throw new BadRequestException("Erro ao enviar notificação");
        }
    }


}
