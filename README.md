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
