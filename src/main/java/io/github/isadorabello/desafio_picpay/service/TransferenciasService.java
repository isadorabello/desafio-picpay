package io.github.isadorabello.desafio_picpay.service;

import io.github.isadorabello.desafio_picpay.controller.TransacaoDTO;
import io.github.isadorabello.desafio_picpay.infrastructure.entity.TipoUsuario;
import io.github.isadorabello.desafio_picpay.infrastructure.entity.Usuario;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferenciasService {

    private final UsuarioService service;
    private final AutorizacaoService authService;

    @Transactional
    public void transferenciaValores(TransacaoDTO dto){
        Usuario payer = service.buscarUsuario(dto.payer());
        Usuario payee = service.buscarUsuario(dto.payee());

        // validar se o pagador é um lojista (caso seja, transferência não é autorizada)
        validarPagador(payer);
        // validar se o pagador é tem saldo suficente para fazer a transferência
        validarSaldo(payer, dto.value());
        // a partir de um serviço autorizador externo (mock fornceido), validar se a transferência foi autorizada
        validarTransferencia();
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
            if(!authService.validarTransferencia()){
                throw new IllegalArgumentException("Transação não autorizada - API");
            }
        }catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }


}
