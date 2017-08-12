package br.com.caelum.leilao.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.Matchers.*;




import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AvaliadorTest {

	private Avaliador leiloeiro;
	private Usuario joao;
	private Usuario jose;
	private Usuario maria;

	@Before
	public void criaAvaliador() {
		this.leiloeiro = new Avaliador();
		this.joao = new Usuario("João");
		this.jose = new Usuario("José");
		this.maria = new Usuario("Maria");
	}

	@Test
	public void deveEntenderLancesEmOrdemCrescente() {

		Leilao leilao = new Leilao("Playstation 3 Novo");

		leilao.propoe(new Lance(joao, 250.0));
		leilao.propoe(new Lance(jose, 300.0));
		leilao.propoe(new Lance(maria, 400.0));

		// parte 2: acao
		leiloeiro.avalia(leilao);

		// parte 3: validacao
		
		//assertEquals(400.0, leiloeiro.getMaiorLance(), 0.00001);
		//assertEquals(250.0, leiloeiro.getMenorLance(), 0.00001);
		
		// Usando hamcrest ao invés do Assert...
		assertThat(leiloeiro.getMenorLance(), equalTo(250.0));
        assertThat(leiloeiro.getMaiorLance(), equalTo(400.0));
		
	}

	@Test
	public void deveEntenderLeilaoComApenasUmLance() {
		Leilao leilao = new Leilao("Playstation 3 Novo");

		leilao.propoe(new Lance(joao, 1000.0));

		leiloeiro.avalia(leilao);

		assertEquals(1000.0, leiloeiro.getMaiorLance(), 0.00001);
		assertEquals(1000.0, leiloeiro.getMenorLance(), 0.00001);
	}

	@Test
	public void deveEncontrarOsTresMaioresLances() {

		Leilao leilao = new CriadorDeLeilaoBuilder().para("Playstation 3 Novo").lance(joao, 100.0).lance(maria, 200.0)
				.lance(joao, 300.0).lance(maria, 400.0).constroi();

		leiloeiro.avalia(leilao);

		List<Lance> maiores = leiloeiro.getTresMaiores();

//		assertEquals(3, maiores.size());
//		assertEquals(400.0, maiores.get(0).getValor(), 0.00001);
//		assertEquals(300.0, maiores.get(1).getValor(), 0.00001);
//		assertEquals(200.0, maiores.get(2).getValor(), 0.00001);
		
		// Usando hamcrest ao invés do Assert...
		assertThat(maiores, hasItems(
                new Lance(maria, 400), 
                new Lance(joao, 300),
                new Lance(maria, 200)
        ));		
	}

	// Testando uma exceção com JUnit
	@Test(expected = RuntimeException.class)
	public void naoDeveAvaliarLeiloesSemNenhumLanceDado() {
		Leilao leilao = new CriadorDeLeilaoBuilder().para("Playstation 3 Novo").constroi();

		leiloeiro.avalia(leilao);
	}

}
