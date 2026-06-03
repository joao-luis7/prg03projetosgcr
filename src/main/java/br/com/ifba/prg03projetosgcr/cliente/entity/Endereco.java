package br.com.ifba.prg03projetosgcr.cliente.entity;

import br.com.ifba.prg03projetosgcr.infrastructure.entity.PersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 *
 * @author joaol
 */
@Entity
@Table(name = "endereco")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Endereco extends PersistenceEntity{
    
    @Column(nullable = false)
    private String bairro;
    
    @Column(nullable = false)
    private String rua;
    
    private String numero;
    
    @Column(name = "ponto_referencia")
    private String pontoReferencia;
}
