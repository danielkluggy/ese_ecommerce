package br.udesc.ese_ecommerce.dto;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PedidoDto {
	private UUID clienteId;
	private UUID enderecoId;

	@NotEmpty(message = "Lista de itens não pode ser vazia")
    private List<ProdutoVendidoDto> produtosVendidos;

    @Data
    public static class ProdutoVendidoDto {

        @NotBlank(message = "Necessário informar um UUID")
        private UUID produtoId;

        @Digits(integer = 10, fraction = 0, message = "O valor deve ser um número inteiro com no máximo 10 dígitos")
        private int quantidadeVendida;
    }
}
