package br.com.caelum.leilao.servico;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.infra.dao.RepositorioDeLeiloes;

public class EncerradorDeLeilaoTest {

	private RepositorioDeLeiloes daoFalso;
	private Carteiro carteiroFalso;

	@Before
	public void setUp() {
		// criando o mock!
		this.daoFalso = mock(RepositorioDeLeiloes.class);
		this.carteiroFalso = mock(Carteiro.class);
	}

	public void endUp() {
		this.daoFalso = null;
		this.carteiroFalso = null;
	}

	@Test
	public void deveEncerrarLeiloesQueComecaramUmaSemanaAtras() {
		Calendar antiga = Calendar.getInstance();
		antiga.set(1999, 1, 20);

		Leilao leilao1 = new CriadorDeLeilao().para("TV de plasma").naData(antiga).constroi();
		Leilao leilao2 = new CriadorDeLeilao().para("Geladeira").naData(antiga).constroi();

		List<Leilao> leiloesAntigos = Arrays.asList(leilao1, leilao2);

		// ensinando o mock a reagir da maneira que esperamos!
		when(daoFalso.correntes()).thenReturn(leiloesAntigos);

		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso, carteiroFalso);
		encerrador.encerra();

		assertTrue(leilao1.isEncerrado());
		assertTrue(leilao2.isEncerrado());
		assertEquals(2, encerrador.getTotalEncerrados());
	}

	@Test
	public void deveAtualizarLeiloesEncerrados() {

		Calendar antiga = Calendar.getInstance();
		antiga.set(1999, 1, 20);

		Leilao leilao1 = new CriadorDeLeilao().para("TV de plasma").naData(antiga).constroi();

		when(daoFalso.correntes()).thenReturn(Arrays.asList(leilao1));

		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso, carteiroFalso);
		encerrador.encerra();

		// verifica se o método atualiza da classe LeilaoDao foi chamado 1
		// vez...
		verify(daoFalso, times(1)).atualiza(leilao1);
	}

	@Test
	public void naoDeveEncerrarLeiloesQueComecaramMenosDeUmaSemanaAtras() {

		Calendar ontem = Calendar.getInstance();
		ontem.add(Calendar.DAY_OF_MONTH, -1);

		Leilao leilao1 = new CriadorDeLeilao().para("TV de plasma").naData(ontem).constroi();
		Leilao leilao2 = new CriadorDeLeilao().para("Geladeira").naData(ontem).constroi();

		when(daoFalso.correntes()).thenReturn(Arrays.asList(leilao1, leilao2));

		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso, carteiroFalso);
		encerrador.encerra();

		assertEquals(0, encerrador.getTotalEncerrados());
		assertFalse(leilao1.isEncerrado());
		assertFalse(leilao2.isEncerrado());

		// verifys aqui

		verify(daoFalso, never()).atualiza(leilao1);
		verify(daoFalso, never()).atualiza(leilao2);

		/*
		 * O método atLeastOnce() vai garantir que o método foi invocado no
		 * mínimo uma vez. Ou seja, se ele foi invocado 1, 2, 3 ou mais vezes, o
		 * teste passa. Se ele não for invocado, o teste vai falhar.
		 * 
		 * O método atLeast(numero) funciona de forma análoga ao método acima,
		 * com a diferença de que passamos para ele o número mínimo de
		 * invocações que um método deve ter.
		 * 
		 * Por fim, o método atMost(numero) nos garante que um método foi
		 * executado até no máximo N vezes. Ou seja, se o método tiver mais
		 * invocações do que o que foi passado para o atMost, o teste falha.
		 * 
		 * Veja que existem diversas maneiras diferentes para garantir a
		 * quantidade de invocações de um método! Você pode escolher a melhor e
		 * mais elegante para seu teste!
		 * 
		 */

	}

	@Test
	public void deveEnviarEmailAposPersistirLeilaoEncerrado() {
		Calendar antiga = Calendar.getInstance();
		antiga.set(1999, 1, 20);

		Leilao leilao1 = new CriadorDeLeilao().para("TV de plasma").naData(antiga).constroi();

		RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);

		when(daoFalso.correntes()).thenReturn(Arrays.asList(leilao1));

		Carteiro carteiroFalso = mock(Carteiro.class);
		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso, carteiroFalso);

		encerrador.encerra();

		/*
		 * garantindo que os métodos foram executados nessa ordem específica:
		 * primeiro o DAO, depois o envio do e-mail.
		 */
		InOrder inOrder = Mockito.inOrder(daoFalso, carteiroFalso);
		inOrder.verify(daoFalso, times(1)).atualiza(leilao1);
		inOrder.verify(carteiroFalso, times(1)).envia(leilao1);
	}

	@Test
	public void deveContinuarAExecucaoMesmoQuandoDaoFalha() {
		Calendar antiga = Calendar.getInstance();
		antiga.set(1999, 1, 20);

		Leilao leilao1 = new CriadorDeLeilao().para("TV de plasma").naData(antiga).constroi();
		Leilao leilao2 = new CriadorDeLeilao().para("Geladeira").naData(antiga).constroi();

		RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
		when(daoFalso.correntes()).thenReturn(Arrays.asList(leilao1, leilao2));

		// mockando a exceção!!!
		doThrow(new RuntimeException()).when(daoFalso).atualiza(leilao1);

		Carteiro carteiroFalso = mock(Carteiro.class);
		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso, carteiroFalso);

		encerrador.encerra();

		// testando se após o lançamento da exceção, o leilão 2 será atualizado!
		verify(daoFalso).atualiza(leilao2);
		verify(carteiroFalso).envia(leilao2);
	}

	@Test
	public void deveDesistirSeDaoFalhaPraSempre() {
		Calendar antiga = Calendar.getInstance();
		antiga.set(1999, 1, 20);

		Leilao leilao1 = new CriadorDeLeilao().para("TV de plasma").naData(antiga).constroi();
		Leilao leilao2 = new CriadorDeLeilao().para("Geladeira").naData(antiga).constroi();

		RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
		when(daoFalso.correntes()).thenReturn(Arrays.asList(leilao1, leilao2));

		Carteiro carteiroFalso = mock(Carteiro.class);
		doThrow(new RuntimeException()).when(daoFalso).atualiza(leilao1);
		doThrow(new RuntimeException()).when(daoFalso).atualiza(leilao2);

		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso, carteiroFalso);

		encerrador.encerra();
		// Precisamos garantir que o EnviadorDeEmail não enviará e-mail nem para o leilao1 nem para 
		// o leilao2
		
		verify(carteiroFalso, never()).envia(leilao1);
		verify(carteiroFalso, never()).envia(leilao2);
	}
	
	@Test
	public void deveDesistirSeDaoFalhaPraSempreRefatoradoComAny() {
		Calendar antiga = Calendar.getInstance();
		antiga.set(1999, 1, 20);

		Leilao leilao1 = new CriadorDeLeilao().para("TV de plasma").naData(antiga).constroi();
		Leilao leilao2 = new CriadorDeLeilao().para("Geladeira").naData(antiga).constroi();

		RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
		when(daoFalso.correntes()).thenReturn(Arrays.asList(leilao1, leilao2));

		Carteiro carteiroFalso = mock(Carteiro.class);
		// doThrow(new RuntimeException()).when(daoFalso).atualiza(leilao1);
		// doThrow(new RuntimeException()).when(daoFalso).atualiza(leilao2);
		
		// O any substitui as linhas acima pois aplica a exceção para qualquer objeto da classe Leilao...
		doThrow(new RuntimeException()).when(daoFalso).atualiza(any(Leilao.class));

		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso, carteiroFalso);

		encerrador.encerra();
		// Precisamos garantir que o EnviadorDeEmail não enviará e-mail nem para o leilao1 nem para 
		// o leilao2
		
		//verify(carteiroFalso, never()).envia(leilao1);
	    //verify(carteiroFalso, never()).envia(leilao2);
		
		// Substitui as linhas acima ...
		verify(carteiroFalso, never()).envia(any(Leilao.class));
		
	}

}
