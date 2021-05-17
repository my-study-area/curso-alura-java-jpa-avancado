package br.com.alura.loja.testes;

import java.math.BigDecimal;
import java.util.List;

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
import br.com.alura.loja.vo.RelatorioDeVendasVo;

public class CadastroDePedido {

	public static void main(String[] args) {
		
		cadastrarProduto();
		
		EntityManager em = JPAUtil.getEntityManager();
		ClienteDao clienteDao = new ClienteDao(em);
		ProdutoDao produtoDao = new ProdutoDao(em);
		PedidoDao pedidoDao = new PedidoDao(em);
		
		Cliente cliente = clienteDao.buscarPorId(1l);
		
		Produto produto = produtoDao.buscarPorId(1l);
		Produto produto2 = produtoDao.buscarPorId(2l);
		Produto produto3 = produtoDao.buscarPorId(3l);
		
		Pedido pedido = new Pedido(cliente);
		Pedido pedido2 = new Pedido(cliente);
		Pedido pedido3 = new Pedido(cliente);
		
		ItemPedido itemPedido = new ItemPedido(2, pedido, produto);
		ItemPedido itemPedido2 = new ItemPedido(1, pedido2, produto2);
		ItemPedido itemPedido3 = new ItemPedido(2, pedido3, produto3);
		
		pedido.adicionarItem(itemPedido);
		pedido2.adicionarItem(itemPedido2);
		pedido3.adicionarItem(itemPedido3);
		
		em.getTransaction().begin();
		pedidoDao.cadastrar(pedido);
		pedidoDao.cadastrar(pedido2);
		pedidoDao.cadastrar(pedido3);
		em.getTransaction().commit();
		
		BigDecimal totalVendido = pedidoDao.valorTotalVendido();
		System.out.println("VALOR_TOTAL = " + totalVendido);
		
		List<RelatorioDeVendasVo> relatorioPedidos = pedidoDao.relatorioPedidos();
		relatorioPedidos.forEach(System.out::println);
	}

	private static void cadastrarProduto() {
		Categoria celulares = new Categoria("CELULARES");
		Categoria videogames = new Categoria("VIDEOGAMES");
		Categoria informatica = new Categoria("INFORMATICA");
		
		Produto celular = new Produto("Xiaomi Redmi", "Muito legal", new BigDecimal("800"), celulares );
		Produto videogame = new Produto("PS5", "Playstation 5", new BigDecimal("4000"), videogames );
		Produto mackbook = new Produto("Mackbook", "Mackbook Pro", new BigDecimal("8000"), informatica );
		
		Cliente cliente = new Cliente("Carlos", "999.999.999-99");
		
		EntityManager em = JPAUtil.getEntityManager();
		ProdutoDao produtoDao = new ProdutoDao(em);
		CategoriaDao categoriaDao = new CategoriaDao(em);
		ClienteDao clienteDao = new ClienteDao(em);
		
		em.getTransaction().begin();
		
		categoriaDao.cadastrar(celulares);
		categoriaDao.cadastrar(videogames);
		categoriaDao.cadastrar(informatica);
		
		produtoDao.cadastrar(celular);
		produtoDao.cadastrar(videogame);
		produtoDao.cadastrar(mackbook);
		
		clienteDao.cadastrar(cliente);
		
		em.getTransaction().commit();
		em.close();
	}
}
