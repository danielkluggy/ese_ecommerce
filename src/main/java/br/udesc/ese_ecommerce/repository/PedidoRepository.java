package br.udesc.ese_ecommerce.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.udesc.ese_ecommerce.model.PedidoModel;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoModel, UUID>{
	
	@Query(value = "SELECT * FROM pedido WHERE status=?", nativeQuery = true)
	public List<PedidoModel> findByStatus(String status);
	
	// @Query("SELECT p FROM PedidoModel p WHERE p.cliente.excludedAt IS NULL")
    // Page<PedidoModel> findAllFiltered(Pageable pageable);
	
	@Query(value = "SELECT * FROM pedido WHERE cliente_id=?", nativeQuery = true)
	public Page<PedidoModel> findAllByClienteIdAndClienteExcludedAtIsNull(UUID clienteId, Pageable pageable);
}
