package br.udesc.ese_ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import br.udesc.ese_ecommerce.dto.PedidoDto;
import br.udesc.ese_ecommerce.dto.PedidoDto.ProdutoVendidoDto;
import br.udesc.ese_ecommerce.dto.PedidoRetornoDto;
import br.udesc.ese_ecommerce.dto.PedidoRetornoDto.ClienteDto;
import br.udesc.ese_ecommerce.dto.PedidoRetornoDto.EnderecoDto;
import br.udesc.ese_ecommerce.dto.PedidoRetornoDto.ProdutosDto;
import br.udesc.ese_ecommerce.model.PedidoModel;
import br.udesc.ese_ecommerce.model.PedidoProdutoModel;
import br.udesc.ese_ecommerce.repository.PedidoRepository;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repository;

	@Autowired
	private RestTemplate restTemplate;
	
	public Page<PedidoModel> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Optional<PedidoModel> findById(UUID id) {
		return repository.findById(id);
	}
	
	@Transactional
	public PedidoModel save(PedidoModel pedido) {
		return repository.save(pedido);
	}
	
	public void delete(PedidoModel pedido) {
		repository.delete(pedido);
	}
	
	public Page<PedidoModel> findByClienteId(UUID clienteId, Pageable pageable){
		return repository.findAllByClienteIdAndClienteExcludedAtIsNull(clienteId, pageable);
	}

	public boolean validarCliente(UUID clienteId) {
		String url = "http://esecustomers:8083/cliente/" + clienteId.toString();
		try {
            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.GET, null, Void.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (RestClientException e) {
            return false;
        }
	}

	public ClienteDto getCliente(UUID clienteId) {
		String url = "http://esecustomers:8083/cliente/" + clienteId.toString();
		ClienteDto clienteDto = restTemplate.getForObject(url, ClienteDto.class);
		return clienteDto;
	}

	public boolean validarEndereco(UUID enderecoId) {
		String url = "http://esecustomers:8083/endereco/" + enderecoId.toString();
		try {
            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.GET, null, Void.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (RestClientException e) {
            return false;
        }
	}

	public EnderecoDto getEndereco(UUID enderecoId) {
		String url = "http://esecustomers:8083/endereco/" + enderecoId.toString();
		EnderecoDto enderecoDto = restTemplate.getForObject(url, EnderecoDto.class);
		return enderecoDto;
	}

	public boolean validarProdutos(PedidoDto pedidoDto) {
		String url = "http://esestock:8082/produto/vender";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Object> requestEntity = new HttpEntity<>(pedidoDto, headers);

		try {
            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (RestClientException e) {
            return false;
        }

	}

	public List<PedidoProdutoModel> createListPedidoProdutoModel(PedidoDto pedidoDto, PedidoModel pedidoModel) {
		List<PedidoProdutoModel> produtosVendidos = pedidoDto.getProdutosVendidos().stream()
					.map(dto -> createPedidoProdutoModel(dto, pedidoModel)) // Mapeia cada ItemVendaDTO para ItemVenda
					.collect(Collectors.toList());
		return produtosVendidos;
	}

    private PedidoProdutoModel createPedidoProdutoModel(ProdutoVendidoDto produtoVendidoDto, PedidoModel pedidoModel) {
        PedidoProdutoModel pedidoProdutoModel = new PedidoProdutoModel();
		pedidoProdutoModel.setPedidoId(pedidoModel);
		pedidoProdutoModel.setProdutoId(produtoVendidoDto.getProdutoId());
		pedidoProdutoModel.setQuantidadeVendida(produtoVendidoDto.getQuantidadeVendida());
		pedidoProdutoModel.setValor(getProdutoValor(produtoVendidoDto.getProdutoId()));
        return pedidoProdutoModel;
    }

	public Double getProdutoValor(UUID produtoId) {
		String url = "http://esestock:8082/produto/";

        @SuppressWarnings("unchecked")
		Map<String, Object> response = restTemplate.getForObject(url + produtoId, Map.class);

        if (response != null && response.containsKey("preco")) {
            return (Double) response.get("preco");
        }

        return null;
	}

	public String getProdutoNome(UUID produtoId) {
		String url = "http://esestock:8082/produto/";

        @SuppressWarnings("unchecked")
		Map<String, Object> response = restTemplate.getForObject(url + produtoId, Map.class);

        if (response != null && response.containsKey("nome")) {
            return (String) response.get("nome");
        }

        return null;
	}

	public String getProdutoDescricao(UUID produtoId) {
		String url = "http://esestock:8082/produto/";

        @SuppressWarnings("unchecked")
		Map<String, Object> response = restTemplate.getForObject(url + produtoId, Map.class);

        if (response != null && response.containsKey("descricao")) {
            return (String) response.get("descricao");
        }

        return null;
	}

	public List<ProdutosDto> getProdutos(List<PedidoProdutoModel> produtosVendidos) {
		List<ProdutosDto> produtosDto = new ArrayList<>();

		for (PedidoProdutoModel produtoVendido : produtosVendidos) {
			ProdutosDto produtoDto = new ProdutosDto();
			produtoDto.setProdutoId(produtoVendido.getProdutoId());
			produtoDto.setNome(getProdutoNome(produtoVendido.getProdutoId()));
			produtoDto.setDescricao(getProdutoDescricao(produtoVendido.getProdutoId()));
			produtoDto.setQuantidadeVendida(produtoVendido.getQuantidadeVendida());
			produtoDto.setValor(produtoVendido.getValor());
			produtosDto.add(produtoDto);
		}
		return produtosDto;
	}

	public Double getValorFinal(List<ProdutosDto> produtosDto) {
		Double valor = 0.0;
		
		for (ProdutosDto produtoDto : produtosDto) {
			valor += produtoDto.getValor() * produtoDto.getQuantidadeVendida();
		}

		return valor;
	}

	public PedidoRetornoDto getRetornoDto(PedidoModel pedidoModel) {
		PedidoRetornoDto retorno = new PedidoRetornoDto();
		retorno.setPedidoId(pedidoModel.getPedidoId());
		retorno.setDtPedido(pedidoModel.getDtPedido());
		retorno.setStatus(pedidoModel.getStatus());
		
		ClienteDto clienteDto = getCliente(pedidoModel.getClienteId());
		retorno.setCliente(clienteDto);
		EnderecoDto enderecoDto = getEndereco(pedidoModel.getEnderecoId());
		retorno.setEndereco(enderecoDto);
		List<ProdutosDto> produtosDto = getProdutos(pedidoModel.getProdutosVendidos());
		retorno.setProdutos(produtosDto);
		retorno.setValorFinal(getValorFinal(produtosDto));

		return retorno;
	}
	
}
