import java.util.ArrayList;
import java.util.List;

public class DistribuidorMensagens {

	private List<RecebedorMensagem> recebedores= new ArrayList<>();

	public void distribuirMensagem(String msg) throws SemRecebedoresException {
		if(recebedores.isEmpty()){
			throw new SemRecebedoresException("Não tem para quem mandar "
											 + " a mensagem: "+ msg);
		}
		for (RecebedorMensagem rm : recebedores) {
			try{
				rm.receberMensagem(msg);
			}catch(Exception e){}
		}

	}

	public void adicionaRecebedor(RecebedorMensagem recebedor) {
		 this.recebedores.add(recebedor);		
	}

	public void removerRecebedor(RecebedorMensagem recebedor) {
		this.recebedores.remove(recebedor);		
	}

}
