package tp.models.validador;

import tp.models.misc.ExcepcionDefinidaPorUsuario;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Validador {

  public List<Patron> listadoPatronesSeguridad;

  private static Validador instancia = null;

  public static Validador getInstancia(){
    if(instancia == null){
      instancia = new Validador();
    }

    return instancia;
  }

  public Validador() {
    listadoPatronesSeguridad = new ArrayList<>();
    this.agregarPatronesSeguridad(new Patron(("((?=.*[0-9]).{8,64})")),new Patron(("((?=.*[a-z]).{8,64})")),new Patron(("((?=.*[A-Z]).{8,64})")),new Patron(("((?=\\S+$).{8,64})")),new Patron(("((?=.*[@%#$_/]).{8,64})")));
  }

  public void agregarPatronesSeguridad(Patron ... patrones) {
    Collections.addAll(listadoPatronesSeguridad,patrones);
  }

  public  boolean seCumplenPatrones(String unaContra) throws ExcepcionDefinidaPorUsuario {
    Boolean ret = false;

      ret = listadoPatronesSeguridad.stream().allMatch( p -> p.seVerificaEn(unaContra));

    return ret;
  }

  public boolean contraseniaTopPeores(String unaContra) throws ExcepcionDefinidaPorUsuario{
    Boolean ret = false;
    FileReader archivo;
    BufferedReader lector;
    try
    {
      archivo = new FileReader("./src/config/TopContraseñas.txt");
      lector = new BufferedReader(archivo);
      String cadena = lector.readLine();

      while(cadena != null)
      {
        if(cadena.equals(unaContra))
        {
          ret = true;
          throw new ExcepcionDefinidaPorUsuario("La contraseña ingresada esta entre el top de 10000 peores contraseñas. ");
        }

        cadena = lector.readLine();
      }
    }
    catch(IOException e)
    {
      System.out.println("Error: " + e.getMessage());
    }
    return ret;
  }
  public boolean esContraValida(String unaContra) throws ExcepcionDefinidaPorUsuario {
    return  !this.contraseniaTopPeores(unaContra) && this.seCumplenPatrones(unaContra);
  }
}