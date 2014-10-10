package es.ingantosan.labjava;

import java.util.List;
import java.util.ArrayList;
import java.time.chrono.IsoChronology;
import java.time.LocalDate;

public class Persona {

    public enum Sexo {
        VARÓN, MUJER
    }
  
    String nombre; 
    LocalDate nacimiento;
    Sexo sexo;
    String correoE;
  
    Persona(String nombre, LocalDate nacimiento, Sexo sexo, String correoE) {
        this.nombre = nombre;
        this.nacimiento = nacimiento;
        this.sexo = sexo;
        this.correoE = correoE;
    }  

    public int getEdad() {
        return nacimiento
            .until(IsoChronology.INSTANCE.dateNow())
            .getYears();
    }

    public void imprimir() {
      System.out.println(nombre + ", " + this.getEdad());
    }
    
    public Sexo getSexo() {
        return sexo;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getCorreoE() {
        return correoE;
    }
    
    public LocalDate getNacimiento() {
        return nacimiento;
    }
    
    public static int compararPorEdad(Persona a, Persona b) {
        return a.nacimiento.compareTo(b.nacimiento);
    }
    
    public boolean isJubilado() {
        return getEdad() >= 65;
    }
    
    @Override
    public String toString() {
        return new StringBuffer("[Persona: ")
                .append(nombre).append(", ")
                .append(getEdad())
                .append("]").toString();
    }
    
    public static List<Persona> crearListado() {
        
        List<Persona> listado = new ArrayList<>();
        listado.add(new Persona("Antonio", 
                IsoChronology.INSTANCE.date(1945, 7, 10),
                Persona.Sexo.VARÓN, "antonio@correoelectronico.es"));
        listado.add(new Persona("Jesús",
                IsoChronology.INSTANCE.date(1981, 9, 25),
                Persona.Sexo.VARÓN, "jesus@correoelectronico.es"));
        listado.add(new Persona("José",
                IsoChronology.INSTANCE.date(2001, 1, 7),
                Persona.Sexo.VARÓN, "jose@correoelectronico.es"));
        listado.add(new Persona("María",
                IsoChronology.INSTANCE.date(1991, 11, 30),
                Persona.Sexo.MUJER, "maria@correoelectronico.es"));
        
        return listado;
    }
    
}
