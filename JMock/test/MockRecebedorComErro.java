
public class MockRecebedorComErro implements RecebedorMensagem {

	@Override
	public void receberMensagem(String msg) {
		throw new RuntimeException();

	}

}
