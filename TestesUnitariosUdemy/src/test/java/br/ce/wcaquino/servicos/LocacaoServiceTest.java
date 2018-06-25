package br.ce.wcaquino.servicos;

import br.ce.wcaquino.daos.LocacaoDao;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
        LocacaoDao dao = Mockito.mock(LocacaoDao.class);
        SpcService spcService = Mockito.mock(SpcService.class);
        service.setLocacaoDao(dao);
        service.setSpcService(spcService);
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








