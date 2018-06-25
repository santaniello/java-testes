package br.ce.wcaquino.suites;

import br.ce.wcaquino.servicos.LocacaoServiceTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import static org.junit.runners.Suite.*;

@RunWith(Suite.class)
@SuiteClasses({
        LocacaoServiceTest.class
})
public class SuiteExecucao {

    /*
    * Os métodos before e after serão executados antes e depois de toda a bateria de testes...
    *
    * */

    @BeforeClass
    public static void before(){
        System.out.println("before");
    }

    @AfterClass
    public static void after(){
        System.out.println("after");
    }
}
