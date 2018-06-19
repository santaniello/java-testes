package br.ce.wcaquino.entidades;

import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class Locacao {
	@Getter @Setter
	private Usuario usuario;
	@Getter @Setter
	private List<Filme> filmes;
	@Getter @Setter
	private Date dataLocacao;
	@Getter @Setter
	private Date dataRetorno;

	public Double getValor() {
//		if (filmes != null){
//			filmes.forEach(f -> {
//				this.valor = this.valor + f.getPrecoLocacao();
//			});
//		}
		return calculaDesconto(this.filmes);

	}

	private Double calculaDesconto(List<Filme> filmes){
		double valor = 0D;
		for(int x =0; x < filmes.size();x++){
			if(x == 2){
				valor = valor + (((filmes.get(x).getPrecoLocacao() * 75) / 100));
			}else if (x == 3){
				valor = valor + (((filmes.get(x).getPrecoLocacao() * 50) / 100));
			}
			else if (x == 4){
				valor = valor + (((filmes.get(x).getPrecoLocacao() * 25) / 100));
			}else if (x == 5){
				valor = valor + (((filmes.get(x).getPrecoLocacao() * 100) / 100));
			}else{
				valor = valor + filmes.get(x).getPrecoLocacao();
			}
		}
		return valor;
	}

}