package br.ce.wcaquino.testesunitarios;

import br.ce.wcaquino.daos.LocacaoDao;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.servicos.LocacaoService;
import br.ce.wcaquino.servicos.SpcService;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.*;

import br.ce.wcaquino.entidades.Usuario;
import org.junit.rules.ErrorCollector;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class AssertTest {

	@Rule
	public ErrorCollector error = new ErrorCollector();

	private LocacaoService service;

	@Before
	public void init(){
		this.service = new LocacaoService();
		LocacaoDao dao = Mockito.mock(LocacaoDao.class);
		SpcService spcService = Mockito.mock(SpcService.class);
		service.setLocacaoDao(dao);
		service.setSpcService(spcService);
	}

	@After
	public void finish(){this.service = null;}

	@Test
	public void deveAlugarFilmeComSucesso() throws Exception {
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 1, 5.0);
		List<Filme> filmes = Arrays.asList(filme);

		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// Usando Assert do JUnit
		assertTrue(locacao.getValor() == 5.0);
		assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));

		// Usando AssertThat Junit + Hamcrest
        assertThat(locacao.getValor(), is(equalTo(5.0)));
        assertThat(locacao.getValor(), is(not(6.0)));
        assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));

		/*
		 *   Usando a Rule ErrorCollector do JUNIT
		 *   A vantagem dessa aboradem é que o Junit por default para no erro da primeira assertiva e não mostra
		 *   os error subsequentes... usando essa abordagem, ele executa todas as assertivas até o final e exibe os
		 *   erros de uma vez...
		 *
		 */
        error.checkThat(locacao.getValor(), is(equalTo(5.0)));
        error.checkThat(locacao.getValor(), is(not(6.0)));
        error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));


		//  Exemplos usando AssertJ
		//  assertThat(locacao.getValor()).isEqualTo(5.0);
		//  assertThat(locacao.getValor()).isNotEqualTo(6.0);
	}

	// Exemplos de Assertivas...

	@Test
	public void test(){
		Assert.assertTrue(true);
		Assert.assertFalse(false);

		Assert.assertEquals("Erro de comparacao", 1, 1);
		Assert.assertEquals(0.51234, 0.512, 0.001);
		Assert.assertEquals(Math.PI, 3.14, 0.01);

		int i = 5;
		Integer i2 = 5;
		Assert.assertEquals(Integer.valueOf(i), i2);
		Assert.assertEquals(i, i2.intValue());

		Assert.assertEquals("bola", "bola");
		Assert.assertNotEquals("bola", "casa");
		Assert.assertTrue("bola".equalsIgnoreCase("Bola"));
		Assert.assertTrue("bola".startsWith("bo"));

		Usuario u1 = new Usuario("Usuario 1");
		Usuario u2 = new Usuario("Usuario 1");
		Usuario u3 = null;

		Assert.assertEquals(u1, u2);

		Assert.assertSame(u2, u2);
		Assert.assertNotSame(u1, u2);

		Assert.assertNull(u3);
		Assert.assertNotNull(u2);
	}
}