package br.udesc.ese_ecommerce.dto;

import java.util.UUID;

public class PedidoDto {
	
	private UUID clienteId;
	private UUID enderecoId;
    private PedidoProdutoDto produtosVendidos;

	public UUID getClienteId() {
        return clienteId;
    }

    public void setClienteId(UUID clienteId) {
        this.clienteId = clienteId;
    }
	
	public UUID getEnderecoId() {
		return enderecoId;
	}
	public void setEnderecoId(UUID enderecoId) {
		this.enderecoId = enderecoId;
	}

    public PedidoProdutoDto getProdutosVendidos() {
        return produtosVendidos;
    }

    public void setProdutosVendidos(PedidoProdutoDto produtosVendidos) {
        this.produtosVendidos = produtosVendidos;
    }



}
