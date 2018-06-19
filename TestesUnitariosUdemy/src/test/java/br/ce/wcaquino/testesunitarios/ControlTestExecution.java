package br.ce.wcaquino.testesunitarios;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.servicos.LocacaoService;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.*;
import org.junit.rules.ErrorCollector;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ControlTestExecution {

    @Rule
    public ErrorCollector error = new ErrorCollector();

    private LocacaoService service;

    /*
    * Before e After executam antes de cada teste.
    *
    * BeforeClass e o AfterClass executam uma única vez no inicio e na finalização da classe de teste...
    * OBS: eles devem ser estáticos...
    *
    * */

    @Before
    public void init(){
        this.service = new LocacaoService();
    }

    @After
    public void finish(){this.service = null;}

    @Test
    @Ignore // Ignora o teste
    public void naoDeveDevolverFilmeNoDomingo(){
        // cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes  = Arrays.asList(new Filme("Filme 1",2,4.0));

        //acao
        Locacao retorno = service.alugarFilme(usuario, filmes);

        //verificacao
        boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
        Assert.assertTrue(ehSegunda);
    }

    @Test
    public void deveAlugarFilmeComSucesso() throws Exception {
        // Assume, serve para controlarmos dinâmicamente a execução de nosos teste baseado em uma dterminada condição...
        // no exemplo abaixo, verificamos se o dia da semana não  é sabádo...
        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(),Calendar.SATURDAY));
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme = new Filme("Filme 1", 1, 5.0);
        List<Filme> filmes = Arrays.asList(filme);

        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        // Usando Assert do JUnit
        assertTrue(locacao.getValor() == 5.0);
        assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));
        assertTrue(isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
    }


    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado(){
        // Assume, serve para controlarmos dinâmicamente a execução de nosos teste baseado em uma dterminada condição...
        // no exemplo abaixo, verificamos se o dia da semana é sabádo...
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(),Calendar.SATURDAY));

        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes  = Arrays.asList(new Filme("Filme 1",2,4.0),
                new Filme("Filme 2", 2, 4.0),
                new Filme("Filme 3", 2, 4.0),
                new Filme("Filme 3", 2, 4.0),
                new Filme("Filme 3", 2, 4.0));

        // acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        boolean ehSegunda = DataUtils.verificarDiaSemana(resultado.getDataRetorno(),Calendar.MONDAY);
        Assert.assertTrue(ehSegunda);

    }
}








