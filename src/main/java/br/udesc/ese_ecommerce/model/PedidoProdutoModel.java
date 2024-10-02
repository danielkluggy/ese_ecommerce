package br.udesc.ese_ecommerce.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "pedidoProduto")
public class PedidoProdutoModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID pedidoProdutoId;

	@ManyToOne
    @JoinColumn(name = "pedido_id")
    private PedidoModel pedidoId;
	
	@Column(nullable = false)
	private UUID produtoId;

	@Column(nullable = false)
	private Integer quantidadeVendida;

    @Column(nullable = false)
	private Double valor;	
}
