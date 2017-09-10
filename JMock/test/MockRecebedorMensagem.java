import org.junit.Assert;

public class MockRecebedorMensagem implements RecebedorMensagem {

	private String recebida;
	private boolean erro = false;
	
	public void receberMensagem(String msg) {
		this.recebida = msg;
		if(erro){
			throw new RuntimeException();
		}
	}

	public void verificarRecebimento(String esperada) {
		Assert.assertEquals(esperada, recebida);
	}

	public void verificarNaoRecebimento() {
		Assert.assertNull(recebida);

	}

	public void lancarExcecao() {
       erro = true;
	}

}
