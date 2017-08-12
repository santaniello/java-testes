package br.com.caelum.leilao.dominio;

public class CriadorDeLeilaoBuilder {
	private Leilao leilao;

	public CriadorDeLeilaoBuilder() { }

	public CriadorDeLeilaoBuilder para(String descricao) {
		this.leilao = new Leilao(descricao);
		return this;
	}

	public CriadorDeLeilaoBuilder lance(Usuario usuario, double valor) {
		leilao.propoe(new Lance(usuario, valor));
		return this;
	}

	public Leilao constroi() {
		return leilao;
	}
}
