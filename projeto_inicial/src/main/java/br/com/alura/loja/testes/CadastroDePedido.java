package br.com.alura.loja.testes;

import java.math.BigDecimal;

import javax.persistence.EntityManager;

import br.com.alura.loja.dao.CategoriaDao;
import br.com.alura.loja.dao.ClienteDao;
import br.com.alura.loja.dao.PedidoDao;
import br.com.alura.loja.dao.ProdutoDao;
import br.com.alura.loja.modelo.Categoria;
import br.com.alura.loja.modelo.Cliente;
import br.com.alura.loja.modelo.ItemPedido;
import br.com.alura.loja.modelo.Pedido;
import br.com.alura.loja.modelo.Produto;
import br.com.alura.loja.util.JPAUtil;

public class CadastroDePedido {

	public static void main(String[] args) {
		
		cadastrarProduto();
		EntityManager em = JPAUtil.getEntityManager();
		ClienteDao clienteDao = new ClienteDao(em);
		ProdutoDao produtoDao = new ProdutoDao(em);
		PedidoDao pedidoDao = new PedidoDao(em);
		Cliente cliente = clienteDao.buscarPorId(1l);
		Produto produto = produtoDao.buscarPorId(1l);
		Pedido pedido = new Pedido(cliente);
		ItemPedido itemPedido = new ItemPedido(2, pedido, produto);
		pedido.adicionaItem(itemPedido);
		
		em.getTransaction().begin();
		pedidoDao.cadastrar(pedido);
		em.getTransaction().commit();
		
		BigDecimal totalVendido = pedidoDao.valorTotalVendido();
		System.out.println("VALOR_TOTAL = " + totalVendido);
	}

	private static void cadastrarProduto() {
		Categoria celulares = new Categoria("CELULARES");
		Produto celular = new Produto("Xiaomi Redmi", "Muito legal", new BigDecimal("800"), celulares );
		Cliente cliente = new Cliente("Carlos", "999.999.999-99");
		
		EntityManager em = JPAUtil.getEntityManager();
		ProdutoDao produtoDao = new ProdutoDao(em);
		CategoriaDao categoriaDao = new CategoriaDao(em);
		ClienteDao clienteDao = new ClienteDao(em);
		
		em.getTransaction().begin();
		
		categoriaDao.cadastrar(celulares);
		produtoDao.cadastrar(celular);
		clienteDao.cadastrar(cliente);
		
		em.getTransaction().commit();
		em.close();
	}
}
