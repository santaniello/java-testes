package br.ce.wcaquino.testesunitarios;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.servicos.LocacaoService;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ExceptionsTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private LocacaoService service;

    @Before
    public void init(){
        this.service = new LocacaoService();
    }

    @After
    public void finish(){this.service = null;}


    /**********************************************
     *          Modo Elegante                     *
     *                                            *
     *********************************************/
    @Test(expected=FilmeSemEstoqueException.class)
    public void deveLancarExcecaoAoAlugarFilmeSemEstoque() throws Exception {
        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme = new Filme("Filme 1", 0, 5.0);
        List<Filme> filmes = Arrays.asList(filme);
        //acao
        Locacao  locacao = service.alugarFilme(usuario, filmes);
    }

    /**********************************************
     *          Modo Robusto                      *
     *                                            *
     *********************************************/
    @Test
    public void deveLancarExcecaoAoAlugarFilmeSemEstoque2()  {
        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme = new Filme("Filme 1", 0, 5.0);
        List<Filme> filmes = Arrays.asList(filme);
        //acao
        try {
            Locacao  locacao = service.alugarFilme(usuario, filmes);
            Assert.fail("Deveria ter lançado uma exceção");
        } catch (FilmeSemEstoqueException e) {
            assertThat(e.getMessage(), is("Filme sem estoque"));
        }
        // Repare que na forma robusta, mesmo ele lançando a exceção o método é executado até o fim...
        System.out.println("Foi até o fim...");
    }


    @Test
    public void deveLancarExcecaoAoAlugarFilmeSemUsuario(){
        //cenario
        Filme filme = new Filme("Filme 1", 2, 5.0);
        List<Filme> filmes = Arrays.asList(filme);
        //acao
        try {
            Locacao  locacao = service.alugarFilme(null, filmes);
            Assert.fail();
        }catch(LocadoraException e){
            Assert.assertThat(e.getMessage(), is("Usuário vazio"));
        }
    }

    /**********************************************
     *          Modo Novo                         *
     *                                            *
     *********************************************/
    @Test
    public void deveLancarExcecaoAoAlugarFilmeSemEstoque3() throws Exception {
        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme = new Filme("Filme 1", 0, 5.0);
        List<Filme> filmes = Arrays.asList(filme);

        // Nessa maneira a exceção deve ser declarada antes da execução da ação...
        exception.expect(FilmeSemEstoqueException.class);
        exception.expectMessage("Filme sem estoque");
        //acao
        Locacao  locacao = service.alugarFilme(usuario, filmes);
    }


    @Test
    public void deveLancarExcecaoAoAlugarSemFilme() throws LocadoraException{
        //cenario
        Usuario usuario = new Usuario("Usuario 1");

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        //acao
        Locacao  locacao = service.alugarFilme(usuario, null);

    }
}








