package br.udesc.ese_ecommerce.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import br.udesc.ese_ecommerce.model.StatusPedido;
import lombok.Data;

@Data
public class PedidoRetornoDto {

    private UUID pedidoId;
	private Date dtPedido;
	private StatusPedido status;

    ClienteDto cliente;
    EnderecoDto endereco;
    List<ProdutosDto> produtos;
    Double valorFinal;

    @Data
    public static class ClienteDto {
        UUID clienteId;
        String nome;
        String cpf;
        Date dtExclusao;
    }

    @Data
    public static class EnderecoDto {
        UUID enderecoId;
        String logradouro;
        Integer numero;
        String complemento;
        String bairro;
        String cidade;
        String estado;
        String cep;
    }

    @Data
    public static class ProdutosDto {
        UUID produtoId;
        String nome;
        String descricao;
        Integer quantidadeVendida;
        Double valor;
    }
}
