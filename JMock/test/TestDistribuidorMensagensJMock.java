import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class TestDistribuidorMensagensJMock {

	private DistribuidorMensagens dm;
	
	@Rule 
	public JUnitRuleMockery ctx = new JUnitRuleMockery();

	@Before
	public void iniciarDistribuidor(){
		dm = new DistribuidorMensagens();
	}

	@Test(expected = SemRecebedoresException.class)
	public void distribuidorSemRecebedores() throws SemRecebedoresException {
		dm.distribuirMensagem("teste");
	}
	
	@Test
	public void distribuidorComUmRecebedor() throws SemRecebedoresException {
		RecebedorMensagem mock = ctx.mock(RecebedorMensagem.class);
		dm.adicionaRecebedor(mock);		
		ctx.checking(new Expectations(){{
			oneOf(mock).receberMensagem("teste");			
		}});		
		dm.distribuirMensagem("teste");					
	}
	
	@Test
	public void distribuidorComVariosRecebedores() throws SemRecebedoresException {
		RecebedorMensagem m1 = ctx.mock(RecebedorMensagem.class,"mock1");
		RecebedorMensagem m2 = ctx.mock(RecebedorMensagem.class,"mock2");
		
		dm.adicionaRecebedor(m1);
		dm.adicionaRecebedor(m2);
		
		ctx.checking(new Expectations(){{
			oneOf(m1).receberMensagem("teste");
			oneOf(m2).receberMensagem("teste");
		}});
		
		dm.distribuirMensagem("teste");
		
	}
	
	@Test
	public void removerRecebedor() throws SemRecebedoresException {
		RecebedorMensagem m1 = ctx.mock(RecebedorMensagem.class,"mock1");
		RecebedorMensagem m2 = ctx.mock(RecebedorMensagem.class,"mock2");
		
		dm.adicionaRecebedor(m1);
		dm.adicionaRecebedor(m2);
		dm.removerRecebedor(m1);
		
		ctx.checking(new Expectations(){{
			never(m1).receberMensagem("teste");
			oneOf(m2).receberMensagem("teste");
		}});		
		dm.distribuirMensagem("teste");		
	}
	
	@Test
	public void erroNoRecebedor() throws SemRecebedoresException {
		RecebedorMensagem m1 = ctx.mock(RecebedorMensagem.class,"mock1");
		RecebedorMensagem m2 = ctx.mock(RecebedorMensagem.class,"mock2");
		RecebedorMensagem m3 = ctx.mock(RecebedorMensagem.class,"mock3");
		
		dm.adicionaRecebedor(m1);
		dm.adicionaRecebedor(m2);
		dm.adicionaRecebedor(m3);
		
		ctx.checking(new Expectations(){{
			oneOf(m1).receberMensagem("teste");
			oneOf(m2).receberMensagem("teste");
				will(throwException(new RuntimeException()));
			oneOf(m3).receberMensagem("teste");
		}});
		
		dm.distribuirMensagem("teste");
		
	}
	
	
}
