package tp.models.validador;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Patron {

    public String patron;

    public Patron(String unPatron) {
        this.patron = unPatron;
    }
    public boolean seVerificaEn(String unaContra) {
        Pattern patronAVerificar = Pattern.compile(patron);
        Matcher matcher = patronAVerificar.matcher(unaContra);
        return matcher.find();
    }
}
