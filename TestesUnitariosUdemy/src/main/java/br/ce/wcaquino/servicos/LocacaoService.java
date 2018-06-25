package br.ce.wcaquino.servicos;

import br.ce.wcaquino.daos.LocacaoDao;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

public class LocacaoService {

	private LocacaoDao dao;
	private SpcService spcService;

//	public LocacaoService(LocacaoDao dao){
//		this.dao = dao;
//	}
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException,LocadoraException {

		if(usuario == null){
			throw new LocadoraException("Usuário vazio");
		}

		if(filmes == null){
			throw new LocadoraException("Filme vazio");
		}

		validaEstoque(filmes);

		if(spcService.possuiNegativacao(usuario)){
			throw new RuntimeException("Usuario negativado");
		}

		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		if(DataUtils.verificarDiaSemana(dataEntrega,Calendar.SUNDAY)){
			dataEntrega = adicionarDias(dataEntrega,1);
		}
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		dao.salvar(locacao);
		
		return locacao;
	}
	


	private void validaEstoque(List<Filme> filmes){
        filmes.forEach(f->{
            if(f.getEstoque() == 0){
                throw new FilmeSemEstoqueException("Filme sem estoque");
            }
        });
    }

    public void setLocacaoDao(LocacaoDao dao){
		this.dao = dao;
	}

	public void setSpcService(SpcService spc){
		this.spcService = spc;
	}

}