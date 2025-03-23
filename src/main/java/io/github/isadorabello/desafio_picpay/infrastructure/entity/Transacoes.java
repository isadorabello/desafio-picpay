package io.github.isadorabello.desafio_picpay.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Transacao")
@Table
public class Transacoes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private BigDecimal valor;

    @JoinColumn(name = "recebedor_id")
    @ManyToOne
    private Usuario recebedor;

    @JoinColumn(name = "pagador_id")
    @ManyToOne
    private Usuario pagador;

    private LocalDateTime horarioTransacao;

    @PrePersist
    void prePersist(){
        // sempre que uma transação for salva, será pré persistido a hora e data atual
        horarioTransacao = LocalDateTime.now();
    }

}
