package br.udesc.ese_ecommerce.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
@Table(name = "pedido")
public class PedidoModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID pedidoId;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtPedido;

	@Column(nullable = false)
	private UUID clienteId;

	@Column(nullable = false)
	private UUID enderecoId;

	@Column
	@Enumerated(EnumType.STRING)
	private StatusPedido status;

	@OneToMany(mappedBy = "pedidoId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<PedidoProdutoModel> produtosVendidos;
	
	public String pagar() {
		return status.pagar(this);
	}

	public String enviar() {
		return status.enviar(this);
	}

	public String finalizar() {
		return status.finalizar(this);
	}
}
