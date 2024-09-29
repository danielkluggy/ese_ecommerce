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

@Entity
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
	private Integer quantidade;

    @Column(nullable = false)
	private Integer valor;

	public PedidoProdutoModel() {
	}

	public UUID getPedidoProdutoId() {
		return pedidoProdutoId;
	}

	public void setPedidoProdutoId(UUID pedidoProdutoId) {
		this.pedidoProdutoId = pedidoProdutoId;
	}

	public PedidoModel getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(PedidoModel pedidoId) {
        this.pedidoId = pedidoId;
    }

	public UUID getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(UUID produtoId) {
        this.produtoId = produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }


	
}
