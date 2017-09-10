import org.junit.Before;
import org.junit.Test;

public class TestDistribuidorMensagens {
	
	private DistribuidorMensagens dm;
	
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
		MockRecebedorMensagem mock = new MockRecebedorMensagem();
		dm.adicionaRecebedor(mock);
		dm.distribuirMensagem("teste");
		mock.verificarRecebimento("teste");			
	}
	
	@Test
	public void distribuidorComVariosRecebedores() throws SemRecebedoresException {
		MockRecebedorMensagem mock1 = new MockRecebedorMensagem();
		MockRecebedorMensagem mock2 = new MockRecebedorMensagem();
		dm.adicionaRecebedor(mock1);
		dm.adicionaRecebedor(mock2);
		dm.distribuirMensagem("teste");
		mock1.verificarRecebimento("teste");
		mock2.verificarRecebimento("teste");
	}
	
	@Test
	public void removerRecebedor() throws SemRecebedoresException {
		MockRecebedorMensagem mock1 = new MockRecebedorMensagem();
		MockRecebedorMensagem mock2 = new MockRecebedorMensagem();
		dm.adicionaRecebedor(mock1);
		dm.adicionaRecebedor(mock2);
		dm.removerRecebedor(mock1);
		dm.distribuirMensagem("teste");
		mock1.verificarNaoRecebimento();
		mock2.verificarRecebimento("teste");
	}
	
	@Test
	public void erroNoRecebedor() throws SemRecebedoresException {
		MockRecebedorMensagem mock1 = new MockRecebedorMensagem();
		MockRecebedorMensagem mock2 = new MockRecebedorMensagem();
		MockRecebedorMensagem mock3 = new MockRecebedorMensagem();
		mock2.lancarExcecao();
		dm.adicionaRecebedor(mock1);
		dm.adicionaRecebedor(mock2);
		dm.adicionaRecebedor(mock3);
		dm.distribuirMensagem("teste");
		mock1.verificarRecebimento("teste");
		mock2.verificarRecebimento("teste");
		mock3.verificarRecebimento("teste");
	}

	/*
	 * Teste acima refatorado pois foi criada a classe MockRecebedorComErro...
	 * */
	
	@Test
	public void erroNoRecebedorRefatorado() throws SemRecebedoresException {
		MockRecebedorMensagem mock1 = new MockRecebedorMensagem();
		RecebedorMensagem mock2 = new MockRecebedorComErro();
		MockRecebedorMensagem mock3 = new MockRecebedorMensagem();
		dm.adicionaRecebedor(mock1);
		dm.adicionaRecebedor(mock2);
		dm.adicionaRecebedor(mock3);
		dm.distribuirMensagem("teste");
		mock1.verificarRecebimento("teste");
		mock3.verificarRecebimento("teste");
	}
	
	
}
