package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class LocacaoServiceTest {

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

    /*********
     * TDD
     *********/
    @Test
    public void devePagar75PctNoFilme3(){
        // cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes  = Arrays.asList(new Filme("Filme 1",2,4.0),
                                            new Filme("Filme 2", 2, 4.0),
                                            new Filme("Filme 3", 2, 4.0));

        // acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        // verificacao
        //4+4+3=11
        assertThat(resultado.getValor(), is(11.0));
    }

    @Test
    public void devePagar50PctNoFilme4(){
        // cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes  = Arrays.asList(new Filme("Filme 1",2,4.0),
                new Filme("Filme 2", 2, 4.0),
                new Filme("Filme 3", 2, 4.0),
                new Filme("Filme 3", 2, 4.0));

        // acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        // verificacao
        //4+4+3+2=13
        assertThat(resultado.getValor(), is(13.0));
    }

    @Test
    public void devePagar25PctNoFilme5(){
        // cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes  = Arrays.asList(new Filme("Filme 1",2,4.0),
                new Filme("Filme 2", 2, 4.0),
                new Filme("Filme 3", 2, 4.0),
                new Filme("Filme 3", 2, 4.0),
                new Filme("Filme 3", 2, 4.0));

        // acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        // verificacao
        //4+4+3+2+1=14
        assertThat(resultado.getValor(), is(14.0));
    }

    @Test
    public void devePagar0NoFilme6(){
        // cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes  = Arrays.asList(new Filme("Filme 1",2,4.0),
                new Filme("Filme 2", 2, 4.0),
                new Filme("Filme 3", 2, 4.0),
                new Filme("Filme 3", 2, 4.0),
                new Filme("Filme 3", 2, 4.0));

        // acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        // verificacao
        //4+4+3+2+1=14
        assertThat(resultado.getValor(), is(14.0));
    }

}








