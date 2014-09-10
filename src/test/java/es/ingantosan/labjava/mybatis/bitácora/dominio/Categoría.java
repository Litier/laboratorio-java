package es.ingantosan.labjava.mybatis.bitácora.dominio;

import java.util.List;

public class Categoría {    
    private int id;
    private String nombre;
    
    public List<Categoría> padres;
    public List<Categoría> hijos;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append('[').append(id).append(',').append(nombre).append(']');
        sb.append("\t\t\tP: {");
        if (padres != null)
            for (Categoría p : padres)
                sb.append('[').append(p.id).append(',').append(p.nombre).append(']');            
        sb.append(" }\t\tH: {");
        if (hijos != null)
            for (Categoría h : hijos)
                sb.append('[').append(h.id).append(',').append(h.nombre).append(']');            
        sb.append('}');        
        
        return sb.toString();    
    }
    
    
}
