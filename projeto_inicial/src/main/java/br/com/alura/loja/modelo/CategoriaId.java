package br.com.alura.loja.modelo;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class CategoriaId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String tipo;
	private String nome;

	public CategoriaId() {
		
	}

	public CategoriaId(String tipo, String nome) {
		this.tipo = tipo;
		this.nome = nome;
	}

	public String getTipo() {
		return tipo;
	}

	public String getNome() {
		return nome;
	}

}
