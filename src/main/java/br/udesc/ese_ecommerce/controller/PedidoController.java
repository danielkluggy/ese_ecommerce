package br.udesc.ese_ecommerce.controller;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.udesc.ese_ecommerce.dto.PedidoDto;
import br.udesc.ese_ecommerce.dto.PedidoRetornoDto;
import br.udesc.ese_ecommerce.exception.ObjectNotFoundException;
import br.udesc.ese_ecommerce.model.PedidoModel;
import br.udesc.ese_ecommerce.model.StatusPedido;
import br.udesc.ese_ecommerce.service.PedidoService;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/pedido")
public class PedidoController {
	
	@Autowired
	private PedidoService pedidoService;
	
	@PostMapping
	public ResponseEntity<Object> savePedido(@RequestBody @Valid PedidoDto pedidoDto) {
		PedidoModel pedidoModel = new PedidoModel();
		BeanUtils.copyProperties(pedidoDto, pedidoModel);
		pedidoModel.setDtPedido(new Date());
		pedidoModel.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);

		if (!pedidoService.validarCliente(pedidoModel.getClienteId()))
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getMessageClienteNotFound());
		}

		if (!pedidoService.validarEndereco(pedidoModel.getEnderecoId()))
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getMessageEnderecoNotFound());
		}

		if (!pedidoService.validarProdutos(pedidoDto))
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getMessageProdutosNotFound());
		}

		pedidoModel.setProdutosVendidos(pedidoService.createListPedidoProdutoModel(pedidoDto, pedidoModel));
		pedidoService.save(pedidoModel);

		PedidoRetornoDto retorno = pedidoService.getRetornoDto(pedidoModel);
		return ResponseEntity.status(HttpStatus.CREATED).body(retorno);
	}
	
	@GetMapping
	public ResponseEntity<Page<PedidoModel>> getAllPedido(
				@PageableDefault(page = 0, size = 10, direction = Sort.Direction.ASC) Pageable peageable) {
		return ResponseEntity.status(HttpStatus.OK).body(this.pedidoService.findAll(peageable));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> getOnePedido(@PathVariable(value = "id") UUID id) {
		Optional<PedidoModel> pedidoModelOptional = this.pedidoService.findById(id);
		if (!pedidoModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getMessagePedidoNotFound());
		}
		PedidoRetornoDto pedidoRetornoDto = pedidoService.getRetornoDto(pedidoModelOptional.get());
		
		return ResponseEntity.status(HttpStatus.OK).body(pedidoRetornoDto);
	}
	
	@GetMapping("/byCliente/{id}")
	public ResponseEntity<Object> getAllPedidosByCliente(@PathVariable(value = "id") UUID id, 
			@PageableDefault(page = 0, size = 10, direction = Sort.Direction.ASC) Pageable peageable)  {
		if (!pedidoService.validarCliente(id))
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getMessageClienteNotFound());
		}
		return ResponseEntity.status(HttpStatus.OK).body(pedidoService.findByClienteId(id, peageable));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deletePedido(@PathVariable(value = "id") UUID id) throws ObjectNotFoundException {
		Optional<PedidoModel> pedidoModelOptional = this.pedidoService.findById(id);
		if (!pedidoModelOptional.isPresent()) {
			throw new ObjectNotFoundException(getMessagePedidoNotFound());
		}
		this.pedidoService.delete(pedidoModelOptional.get());
		return ResponseEntity.status(HttpStatus.OK).body(getMessagePedidoDeleted());
	}
	
	@PostMapping("/{id}/pagar")
	public ResponseEntity<Object> pagar(@PathVariable(value = "id") UUID id) throws ObjectNotFoundException{
		Optional<PedidoModel> pedidoOptional = pedidoService.findById(id);
		if(!pedidoOptional.isPresent()) {
			throw new ObjectNotFoundException(getMessagePedidoNotFound());
		}
		PedidoModel pedido = pedidoOptional.get();
		String mensagem = pedido.pagar();
		pedidoService.save(pedido);
		return ResponseEntity.status(HttpStatus.OK).body(mensagem);
	}
	
	@PostMapping("/{id}/enviar")
	public ResponseEntity<Object> enviar(@PathVariable(value = "id") UUID id) throws ObjectNotFoundException{
		Optional<PedidoModel> pedidoOptional = pedidoService.findById(id);
		if(!pedidoOptional.isPresent()) {
			throw new ObjectNotFoundException(getMessagePedidoNotFound());
		}
		PedidoModel pedido = pedidoOptional.get();
		String mensagem = pedido.enviar();
		pedidoService.save(pedido);
		return ResponseEntity.status(HttpStatus.OK).body(mensagem);
	}

	@PostMapping("/{id}/finalizar")
	public ResponseEntity<Object> finalizar(@PathVariable(value = "id") UUID id) throws ObjectNotFoundException{
		Optional<PedidoModel> pedidoOptional = pedidoService.findById(id);
		if(!pedidoOptional.isPresent()) {
			throw new ObjectNotFoundException(getMessagePedidoNotFound());
		}
		PedidoModel pedido = pedidoOptional.get();
		String mensagem = pedido.finalizar();
		pedidoService.save(pedido);
		return ResponseEntity.status(HttpStatus.OK).body(mensagem);
	}
	
	protected String getMessagePedidoNotFound() {
		return "Pedido não encontrado!";
	}

	protected String getMessagePedidoDeleted() {
		return "Pedido deletado com sucesso!";
	}

	protected String getMessageClienteNotFound() {
		return "Cliente não encontrado!";
	}

	protected String getMessageEnderecoNotFound() {
		return "Endereço não encontrado!";
	}

	protected String getMessageProdutosNotFound() {
		return "Algum produto não encontrado ou sem estoque!";
	}
}
