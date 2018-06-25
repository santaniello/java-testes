package br.ce.wcaquino.matchers;

import br.ce.wcaquino.utils.DataUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/*
* Date é o parâmetro inicial que será comparado
* Exemplo:
*
* assertThat(retorno.getDataRetorno(), caiEm(Calendar.Monday))
*
* O Date equivale ao retorno.getDataRetorno()
*
* */
public class DiaSemanaMatcher extends TypeSafeMatcher<Date> {

    private Integer diaSemana;

    /*
     * diaSemana é o parâmetro Final que será comparado
     * Exemplo:
     *
     * assertThat(retorno.getDataRetorno(), caiEm(Calendar.Monday))
     *
     * O diaSemana equivale ao caiEm(Calendar.Monday)
     *
     * */
    public DiaSemanaMatcher(Integer diaSemana){
        this.diaSemana = diaSemana;
    }

    /*
    * Método aonde será realizada a comparação...
    * */
    @Override
    protected boolean matchesSafely(Date date) {
        return DataUtils.verificarDiaSemana(date,diaSemana);
    }

    /*
     * Método aonde customizamos a mensagem de erro...
     * */
    @Override
    public void describeTo(Description description) {
        Calendar data = Calendar.getInstance();
        data.set(Calendar.DAY_OF_WEEK, diaSemana);
        String dataExtenso = data.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("pt","BR"));
        description.appendText(dataExtenso);
    }
}
