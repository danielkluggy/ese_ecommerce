package br.udesc.ese_ecommerce.model;

public enum StatusPedido {
	AGUARDANDO_PAGAMENTO{
		@Override
		public String pagar(PedidoModel pedidoModel) {
			pedidoModel.setStatus(AGUARDANDO_ENVIO);
			return "Pagamento confirmado, pedido está aguardando envio!";
		}

		@Override
		public String enviar(PedidoModel pedidoModel) {
			return "Erro: Pedido aguardando pagamento!";
		}

		@Override
		public String finalizar(PedidoModel pedidoModel) {
			return "Erro: Pedido aguardando pagamento!";
		}
	},
	AGUARDANDO_ENVIO{
		@Override
		public String pagar(PedidoModel pedidoModel) {
			return "Erro: Pedido aguardando envio!";
		}

		@Override
		public String enviar(PedidoModel pedidoModel) {
			pedidoModel.setStatus(PEDIDO_ENVIADO);
			return "Pedido enviado, aguardando entrega!";
		}

		@Override
		public String finalizar(PedidoModel pedidoModel) {
			return "Erro: Pedido aguardando envio!";
		}
	},
	PEDIDO_ENVIADO{
		@Override
		public String pagar(PedidoModel pedidoModel) {
			return "Erro: Pedido aguardando entrega!";
		}

		@Override
		public String enviar(PedidoModel pedidoModel) {
			return "Erro: Pedido aguardando entrega!";
		}

		@Override
		public String finalizar(PedidoModel pedidoModel) {
			pedidoModel.setStatus(PEDIDO_FINALIZADO);
			return "Pedido entregue e finalizado!";
		}
	},
	PEDIDO_FINALIZADO{
		@Override
		public String pagar(PedidoModel pedidoModel) {
			return "Erro: Pedido finalizado!";
		}

		@Override
		public String enviar(PedidoModel pedidoModel) {
			return "Erro: Pedido finalizado!";
		}

		@Override
		public String finalizar(PedidoModel pedidoModel) {
			return "Erro: Pedido finalizado!";
		}
	};

	public String pagar(PedidoModel pedidoModel) {
		return "";
	}

	public String enviar(PedidoModel pedidoModel) {
		return "";
	}

	public String finalizar(PedidoModel pedidoModel) {
		return "";
	}
}
