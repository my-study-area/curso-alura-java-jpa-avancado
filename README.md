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
