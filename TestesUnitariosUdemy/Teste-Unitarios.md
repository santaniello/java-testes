# Teste Unitários




Um teste é dividido em 3 partes:

  - Cenário;
  - Ação;
  - Verificação;

**Exemplo:**
 
```java

@Test
public void deveAlugarFilmeComSucesso() throws Exception {
     //cenário
     Usuario usuario = new Usuario("Usuario 1");
     Filme filme = new Filme("Filme 1", 1, 5.0);
     List<Filme> filmes = Arrays.asList(filme);

     //acão
     Locacao locacao = service.alugarFilme(usuario, filmes);
     
     //verificação
     assertTrue(locacao.getValor() == 5.0);
     assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));
}
```

# Asserts (verificações)

Os asserts servem para verificar o resultado dos nossos teste. Podemos usar diversos tipos de asserts tais como:

- JUnit;
- AssertJ;
- JUnit + Hamcrest;

**Exemplo de um método de teste que usa Asserts do JUnit:**

```java
@Test
public void deveAlugarFilmeComSucesso() throws Exception {
    //cenario
    Usuario usuario = new Usuario("Usuario 1");
    Filme filme = new Filme("Filme 1", 1, 5.0);
    List<Filme> filmes = Arrays.asList(filme);

    //acao
    Locacao locacao = service.alugarFilme(usuario, filmes);

    //verificacao

    // Usando Assert do JUnit
    assertTrue(locacao.getValor() == 5.0);
    assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));
}    
```

**Exemplo usando asserts do JUnit e do Hamcrest:**

```java
@Test
public void deveAlugarFilmeComSucesso() throws Exception {
    //cenario
    Usuario usuario = new Usuario("Usuario 1");
    Filme filme = new Filme("Filme 1", 1, 5.0);
    List<Filme> filmes = Arrays.asList(filme);

    //acao
    Locacao locacao = service.alugarFilme(usuario, filmes);

    //verificacao
    // Usando AssertThat Junit + Hamcrest
    assertThat(locacao.getValor(), is(equalTo(5.0)));
    assertThat(locacao.getValor(), is(not(6.0)));
    assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
}    
```

Para saber todos os matchers disponíveis pelo hamcrest, acesse o link:

[Hamcrest Matchers](http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/Matchers.html)

**Exemplo usando asserts do AssertJ:**

```java
@Test
public void deveAlugarFilmeComSucesso() throws Exception {
    //cenario
    Usuario usuario = new Usuario("Usuario 1");
    Filme filme = new Filme("Filme 1", 1, 5.0);
    List<Filme> filmes = Arrays.asList(filme);

    //acao
    Locacao locacao = service.alugarFilme(usuario, filmes);

    //verificacao
    //Exemplos usando AssertJ
    assertThat(locacao.getValor()).isEqualTo(5.0);
    assertThat(locacao.getValor()).isNotEqualTo(6.0);
}    
```

Por default, quando temos diversos asserts em um mesmo método de teste o JUnit para no erro da primeira assertiva e não continua o teste até consertarmos o erro da mesma, porém, existe uma maneira de executar todas as verificações de um teste mesmo que uma assertiva acabe falhando no meio do caminho.

Para executar todas as assertivas utilizamos a @Rule ErrorCollector que permite executarmos o teste até o final mesmo que uma assertiva falhar no meio do caminho.

**Exemplo:**

```java
public class LocacaoServiceTest {
    
    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Test
    public void deveAlugarFilmeComSucesso() throws Exception {
        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme = new Filme("Filme 1", 1, 5.0);
        List<Filme> filmes = Arrays.asList(filme);

        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificacao
        error.checkThat(locacao.getValor(), is(equalTo(5.0)));
        error.checkThat(locacao.getValor(), is(not(6.0)));
        error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        error.checkThat(isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
    }
}
```

# Testando Exceções

Para testar exceções, existem 3 maneiras conhecidas:

- Elegante;
- Robusta;
- Nova;
 
## Utilizando o modo elegante:

Para usarmos o modo elegante, basta colocar dentro da anotação @Test em cima do método o seguinte atributo:  @Test(expected=SuaException.class)

**Exemplo:**

```java
@Test(expected=FilmeSemEstoqueException.class)
public void deveLancarExcecaoAoAlugarFilmeSemEstoque() throws Exception {
    //cenario
    Usuario usuario = new Usuario("Usuario 1");
    Filme filme = new Filme("Filme 1", 0, 5.0);
    List<Filme> filmes = Arrays.asList(filme);
        
    //acao que vai lançar a exceção...
    Locacao  locacao = service.alugarFilme(usuario, filmes);
}
```

**OBS: Para usarmos essa maneira, cada exception em nosso sistema presisará ter uma classe própria por exemplo: UsuarioInvalidoException pois do contrário não conseguiremos capturar uma exceção genérica e validar se a mesma esta ocorrendo pelo motivo correto.**

**Essa abordagem é indicada quando apenas a exceção importa para você.**

**A desvantagem é que você não tem muito controle sobre a exceção que vai ser lançado pois você não poderá verificar se a mensagem da exceção está correta por exemplo e muitas outras coisas...**

## Utilizando o modo robusto

Para utilizar o modo robusto, usaremos uma assertiva do próprio JUnit que ficará responsável por verificar se foi lançada uma exceção no nosso teste ou não.

```java
@Test
// Repare que a exceção não foi lançada e sim tratada no próprio método.
public void deveLancarExcecaoAoAlugarFilmeSemEstoque2()  {
    //cenario
    Usuario usuario = new Usuario("Usuario 1");
    Filme filme = new Filme("Filme 1", 0, 5.0);
    List<Filme> filmes = Arrays.asList(filme);

    //acao
    try {
        Locacao  locacao = service.alugarFilme(usuario, filmes);
        // Assertiva que vai verificar se foi lançada a exceção.
        Assert.fail("Deveria ter lançado uma exceção");
    } catch (FilmeSemEstoqueException e) {
        assertThat(e.getMessage(), is("Filme sem estoque"));
    }
    // No modo robusto, o método é executado até o fim.
    System.out.println("Foi até o fim...");
}
```

**Essa abordagem é indicada sempre que você quiser ter um maior controle sobre o método de teste e da exceção a ser lançada.**

## Utilizando a forma nova

Para utilizar a maneira nova, precisaremos usar uma @Rule do JUnit.

**Exemplo:**

```java

public class LocacaoServiceTest {
   @Rule
   public ExpectedException exception = ExpectedException.none();

   public void deveLancarExcecaoAoAlugarFilmeSemEstoque3() throws Exception {
      //cenario
      Usuario usuario = new Usuario("Usuario 1");
      Filme filme = new Filme("Filme 1", 0, 5.0);
      List<Filme> filmes = Arrays.asList(filme);

      // A exceção deve ser declarada antes da execução da ação...
      exception.expect(FilmeSemEstoqueException.class);
      exception.expectMessage("Filme sem estoque");
      
      //acao
      Locacao  locacao = service.alugarFilme(usuario, filmes);
    } 
}
```

**Essa abordagem nos da um maior controle sobre nossa exceçãoe atende a maioria dos casos quando precisarmos testar uma exception.**

# Métodos de inicialiação e finalização de uma classe de teste.

O Junit nos fornece alguns métodos de inicialização e finalização de uma classe de teste, são eles:

**@Before, @After, @BeforeClass, @AfterClass**

@Before - É executado antes de cada método de teste.
@After  - É executado após cada método de teste.
@BeforeClass - É executado somente na inicialização da classe de teste.
@AfterClass  - É executado somente na finalização da classe de teste.

**Exemplo:**

```java
public class LocacaoServiceTest {
    @Before
    public void init(){this.service = new LocacaoService();}

    @After
    public void finish(){this.service = null;}

    @BeforeClass
    public static void beforeClass(){
        System.out.println("Before Class");
    }

    @AfterClass
    public static void afterClass(){
        System.out.println("After Class");
    }
}    
```

**OBS: Os métodos @BeforeClass e @AfterClass devem ser estáticos.**

# Controlando execução de testes.

Muitas vezes, queremos controlar a execução de um teste em específico pois o mesmo deve rodar em um dia da semana em específico ou deve ter uma série de condições para que o mesmo seja executado com sucesso. Nestes casos, possuimos 2 maneiras que podemos usar para controlar a execução dos nossos métodos: usando @Ignore e Assumes.

## Utilizando @Ignore

O @Ignore é equivalente ao ato de comentar um método.

**Exemplo:**

```java
@Test
@Ignore // ignore o teste
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
```

Repare que enquanto o método possuir a anotação @Ignore, o mesmo não será executado...

## Usando Assumes

Quando queremos controlar a execução de um método dinâmicamente, usamos assumes que funcionam como as assertivas do JUnit.

**Exemplo:**

Vamos supor que o método abaixo deve ser executado apenas no sabado:

```java
@Test
public void deveDevolverNaSegundaAoAlugarNoSabado(){
    // Verificamos se o dia da semana é sabado e se sim, ele executa o restante do método senão, ele ignora o restante da execução...
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
```
 