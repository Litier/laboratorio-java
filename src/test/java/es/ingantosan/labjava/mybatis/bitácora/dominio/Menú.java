package es.ingantosan.labjava.mybatis.bitácora.dominio;

import java.util.List;

public class Menú {    
    public int id;
    public String nombre;
    
    public Menú padre;
    public List<Menú> hijos;
        
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append('[').append(id).append(" ,").append(nombre).append(']').append("\t\tP:").append('[');
        
        if (padre != null)
            sb.append(padre.id).append(" ,").append(padre.nombre);
        sb.append("]\t\tH: {");
        if (hijos != null)
            for (Menú h : hijos)
                sb.append('[').append(h.id).append(" ,").append(h.nombre).append(']');            
        sb.append('}');        
        
        return sb.toString();    
    }
    
    
}
