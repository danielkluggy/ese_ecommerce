package br.udesc.ese_ecommerce.service;

import java.util.Optional;
import java.util.UUID;

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

import br.udesc.ese_ecommerce.dto.PedidoProdutoDto;
import br.udesc.ese_ecommerce.model.PedidoModel;
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

	public boolean validarEndereco(UUID enderecoId) {
		String url = "http://esecustomers:8083/endereco/" + enderecoId.toString();
		try {
            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.GET, null, Void.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (RestClientException e) {
            return false;
        }
	}

	// public boolean validarProdutos(List<PedidoProdutoDto> pedidoProdutoDto) {
	public boolean validarProdutos(PedidoProdutoDto produtosVendidos) {
		String url = "http://esestock:8082/produto/vender";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<PedidoProdutoDto> requestEntity = new HttpEntity<>(produtosVendidos, headers);

		try {
            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (RestClientException e) {
            return false;
        }

	}
	
}
