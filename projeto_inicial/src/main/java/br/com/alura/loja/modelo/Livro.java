package br.com.alura.loja.modelo;

import javax.persistence.Entity;

@Entity
public class Livro extends Produto {

	private String autor;
	private Integer numerDePaginas;
	
	public Livro() {
		
	}
	
	public Livro(String autor, Integer numeroDePaginas) {
		this.autor = autor;
		this.numerDePaginas = numeroDePaginas;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public Integer getNumerDePaginas() {
		return numerDePaginas;
	}

	public void setNumerDePaginas(Integer numerDePaginas) {
		this.numerDePaginas = numerDePaginas;
	}

}
