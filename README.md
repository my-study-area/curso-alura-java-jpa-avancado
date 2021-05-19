# curso-alura-java-jpa-avancado
Curso de Java e JPA: Consultas avançadas, performance e modelos complexos

## Módulo 01 - Mais relacionamentos

### Aula 01.01 - Apresentação
Conteúdo:
- Relacionamentos many-to-many e bidirecionais
- Mais consultas (agregação, named queries, select new)
- Lazy, Eager e Join Fetch
- Criteria API
- Outros tópicos (herança, chave composta, etc.)

### Aula 01.02 - Projeto inicial do treinamento
- [Projeto inicial](./projeto_inicial)

### Aula 01.03 - Mapeando novas entidades
![Tabela de pedidos e cliente](./tabela_pedidos_cliente.png)

### Aula 01.04 - Relacionamentos many-to-many
![Tabela itens de pedidos](./tabela_itens_pedido.png)

### Aula 01.05 - Relacionamentos bidirecionais
- Ao criar um relacionamento muitos para muitos não podemos esquecer de anotar com `@OneToMany(mappedBy = "pedido")`, no lado  inverso, para impedir a criação de uma tabela desnecessária.

### Aula 01.06 - Parâmetro mappedBy
Por qual motivo o parâmetro mappedBy deve ser utilizado na anotação `@OneToMany`?  
`R:`Para indicar que se trata de um relacionamento bidirecional. Sem esse parâmetro a JPA considera se tratar de outro relacionamento, assumindo que existe outra tabela de join

### Aula 01.07 - Teste do relacionamento bidirecional
- Utilizamos a propriedade `cascade = CascadeType.ALL` num relacionamento para salvarmos em cascata, por exemplo, ao salvar no banco de dados o `Pedido` já é salvo automaticamente o `ItemDoPedido`. Ex:
```java
@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
```

### Aula 01.08 - Parâmetro cascade
Qual a utilidade do parâmetro cascade?  
`R:` Propagar as operações realizadas de uma entidade em seu relacionamento. O cascade cria um efeito cascata nas operações realizadas em uma entidade.

### Aula 01.09 - Faça como eu fiz

### Aula 01.10 - O que aprendemos?
- A mapear novas entidades na aplicação conforme a modelagem do banco de dados;
- A mapear um relacionamento com cardinalidade muitos-para-muitos;
- A mapear um relacionamento bidirecional;
- Como persistir entidades que possuam relacionamentos bidirecionais.

## Módulo 02 - Consultas avançadas

### Aula 02.01 - Projeto da aula anterior

### Aula 02.02 - Consultas com funções de agregação
- Exemplo de consulta utilizando a função `SUM`:
```java
String jpql = "SELECT SUM(p.valorTotal) FROM Pedido p";
BigDecimal total = this.em.createQuery(jpql, BigDecimal.class)
    .getSingleResult();
System.out.println(total);
```

### Aula 02.03 - Consultas para relatórios
![Imagem com exemplo de um relatório de vendas](./relatorio_vendas.png)
- Exemplo de consulta, gerando relatório de pedidos, utilizando um Array de Objetos:
```java
String jpql = "SELECT "
        + "produto.nome, "
        + "SUM(item.quantidade), "
        + "MAX(pedido.data) "
      + "FROM Pedido pedido "
      + "JOIN pedido.itens item "
      + "JOIN item.produto produto "
      + "GROUP BY produto.nome "
      + "ORDER BY item.quantidade DESC";
 Object[] relatorio = this.em
    .createQuery(jpql, Object[].class)
    .getResultList();
```

### Aula 02.04 - Consultas com select new
- Exemplo de consulta, gerando relatório de pedidos, utilizando uma classe para visualização dos dados:
```java
// Exemplo de classe com os dados para visualização
public class RelatorioDeVendasVo {

	private String nomeProduto;
	private Long quantidadeVendida;
	private LocalDate dataUltimaVenda;

	public RelatorioDeVendasVo(String nomeProduto, Long quantidadeVendida,
          LocalDate dataUltimaVenda) {
		this.nomeProduto = nomeProduto;
		this.quantidadeVendida = quantidadeVendida;
		this.dataUltimaVenda = dataUltimaVenda;
	}

  //getters

	@Override
	public String toString() {
		return "RelatorioDeVendasVo [nomeProduto=" + nomeProduto + ", quantidadeVendida=" + quantidadeVendida
				+ ", dataUltimaVenda=" + dataUltimaVenda + "]";
	}
}

//Exemplo de método
public List<RelatorioDeVendasVo> relatorioPedidos() {
  String jpql = "SELECT "
          + "new br.com.alura.loja.vo.RelatorioDeVendasVo("
            + "produto.nome, "
            + "SUM(item.quantidade), "
            + "MAX(pedido.data) "
          + ")"
        + "FROM Pedido pedido "
        + "JOIN pedido.itens item "
        + "JOIN item.produto produto "
        + "GROUP BY produto.nome "
        + "ORDER BY item.quantidade DESC";
  return this.em.createQuery(jpql, RelatorioDeVendasVo.class).getResultList();
}
```
- No JPQL devemos utilizar o caminho completo do pacote com o nome da classe. Ex: `new br.com.alura.loja.vo.RelatorioDeVendasVo(param1, param2)`

### Aula 02.05 - Gerando relatórios
Considere a seguinte consulta JPQL:
```java
select 
    new br.com.alura.loja.dto.VendasPorDia(
        produto.nome,
        sum(pedido.valorTotal),
        pedido.data) 
from Pedido pedido 
join pedido.itens itens join itens.produto produto 
group by produto.nome, pedido.data
```
E a seguinte classe DTO:
```java
public class VendasPorDia {
    private String nomeDoProduto;
    private BigDecimal valorVendido;
    private LocalDate dataDasVendas;

    //getters e setter de todos os atributos
}
```
Por qual motivo a consulta anterior não vai funcionar corretamente?  
`R:` A classe DTO deve conter um construtor compatível com a consulta JPQL. A JPA cria instâncias da classe DTO via construtor que recebe parâmetros, conforme a consulta JPQL.

### Aula 02.06 - Utilizando Named Queries
- `Named Querie` é uma outra forma de realizarmos consultas usando JPQL, mas são criadas nas entidades de domínio da consulta. Por exemplo, na classe Produto:
- Utilizamos a anotação `@NamedQuery`
```java
@Entity
@Table(name = "produtos")
@NamedQuery(name = "Produto.produtosPorNomeDaCategoria", query = "SELECT p FROM Produto p WHERE p.categoria.nome = :nome")
public class Produto {
    // restante do código
}
```
- Para utilizar uma Named Querie devemos utilizar o método `createNamedQuery` conforme exemplo abaixo:
```java
public List<Produto> buscarPorNomeDaCategoria(String nome) {
  return em.createNamedQuery("Produto.produtosPorNomeDaCategoria", Produto.class)
      .setParameter("nome", nome)
      .getResultList();
}
```
- Para identificarmos a origem da named querie é comum nomearmos com o nome da classe, ponto e o nome da querie (`NomeDaClasse.nomeDaQuerie`), como no exemplo acima.

### Aula 02.07 - Vantagens das named queries
Qual das seguintes alternativas é uma das vantagens de se utilizar named queries?  
`R:` Deixar as consultas de uma entidade mais próximas da classe dela. As named queries são declaradas na classe da entidade.

### Aula 02.08 - Faça como eu fiz

### Aula 02.09 - O que aprendemos?
- A realizar consultas utilizando funções de agregação, tais como `MIN`, `MAX`, `AVG` E `SUM`;
- Como escrever consultas de relatórios utilizando `JPQL`;
- Como utilizar o recurso select new em consultas `JPQL`;
- A realizar consultas utilizando named queries.

## Módulo 03 - Performance de consultas

### Aula 03.01 - Projeto da aula anterior

### Aula 03.02 - Entendendo Lazy e Eager
- Relacionamentos `@ManyToOne` e `@OneToOne` carregam os dados de outras entidades mesmo que não estejam sendo utilizados. Este comportamento é chamada de `EAGER` e é o padrão no Hibernate. Veja o log abaixo
```sql
select
    pedido0_.id as id1_3_0_,
    pedido0_.cliente_id as cliente_4_3_0_,
    pedido0_.data as data2_3_0_,
    pedido0_.valor_total as valor_to3_3_0_,
    cliente1_.id as id1_1_1_,
    cliente1_.cpf as cpf2_1_1_,
    cliente1_.nome as nome3_1_1_ 
from
    pedidos pedido0_ 
left outer join
    clientes cliente1_ 
        on pedido0_.cliente_id=cliente1_.id 
where
    pedido0_.id=?
```
- Para tornar um relacionamento `EAGER` em `LAZY` utilizamos a seguinte anotação: `@ManyToOne(fetch = FetchType.LAZY)`

- No relacionamento `@ManyToMany` e `@OneToMany` o carregamento dos dados é `LAZY`, por padrão, ou seja, somente são carregados os dados quando são utilizados.

### Aula 03.03 - Consultas com Join Fetch
 - A exception `org.hibernate.LazyInitializationException` é lançada quando tentamos acessar um dado que pertence a um relacionamento `LAZY` que utilizado após fechar a conexão do EntityManager. Ex:
 ```java
 // Exemplo que lança org.hibernate.LazyInitializationException
public static void main(String[] args) {
    popularBancoDeDados();
    EntityManager em = JPAUtil.getEntityManager();
    
    Pedido pedido = em.find(Pedido.class, 1l);
    System.out.println(pedido.getData());
    em.close();
    System.out.println(pedido.getCliente().getNome());
}
 ```
 - A solução quanto a exception `LazyInitializationException` é a utilização de query planejada, ou seja, na consulta já trazer a Entidade com o seu relacionamento. Ex:
 ```java
public class PedidoDao {

    private EntityManager em;

    // outros métodos

    public Pedido buscarPedidoComCliente(Long id) {
      String jpql = "SELECT p FROM Pedido p JOIN FETCH p.cliente WHERE p.id = :id";
      return this.em.createQuery(jpql, Pedido.class)
          .setParameter("id", id)
          .getSingleResult();
    }
}

//utilização
public static void main(String[] args) {
    popularBancoDeDados();
    EntityManager em = JPAUtil.getEntityManager();
    PedidoDao pedidoDao = new PedidoDao(em);

    Pedido pedido = pedidoDao.buscarPedidoComCliente(1l);
    System.out.println(pedido.getData());
    System.out.println(pedido.getItens().size());
    em.close();
    System.out.println(pedido.getCliente().getNome());
}
 ```

### Aula 03.04 - Consultas com Lazy e Eager
Considere as seguintes entidades JPA:

```java
@Entity
@Table(name = “clientes”)
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @OneToOne(fetch = FetchType.EAGER)
    private Endereco endereco;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = “cliente”)
    private List<Telefone> telefones = new ArrayList<>();

}
```

```java
@Entity
@Table(name = “enderecos”)
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String logradouro;
    private String cep;
    private String cidade;
    private String uf;
    private String bairro;
    private String numero;

}
```

```java
@Entity
@Table(name = “telefones”)
public class Telefone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ddd;
    private String numero;
    @ManyToOne
    private Cliente cliente;

}
```

E o seguinte trecho de código:
```java
Cliente cliente = em.find(Cliente.class, 1l);
System.out.println(cliente.getNome());
```
Que tipo de consulta no banco de dados o trecho de código anterior vai gerar?  
`R:` Um select na tabela de clientes fazendo join apenas com a tabela de enderecos. O relacionamento com a entidade Endereco é do tipo eager, portanto a consulta vai gerar um join com a tabela de enderecos.

### Aula 03.05 - Vantagens do join fetch
Por qual motivo é interessante utilizar o recurso join fetch em uma consulta JPQL?  
`R:` Para evitar carregar relacionamentos em todas as consultas da aplicação. O join fetch permite escolher quais relacionamentos serão carregados em determinada consulta, ao invés de sempre os carregar
