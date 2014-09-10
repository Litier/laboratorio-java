/*
 * Este documento es parte del proyecto 'Juntando Líneas': 
 * https://code.google.com/p/juntando-lineas/
 * 
 * Copyright (C) 2011 Antonio Sánchez - Todos los derechos reservados
 * 
 * 'Juntando Líneas' es software libre: Ud. puede redistribuirlo y/o modificarlo 
 * bajo los términos de la Licencia Pública General de GNU tal como está publicada 
 * por la Fundación para el Software Libre, ya sea la versión 3 de la Licencia, o 
 * (a su elección) cualquier versión posterior.
 * 
 * 'Juntando Líneas' se distribuye con la esperanza de que sea útil, pero 
 * SIN GARANTÍA ALGUNA; ni siquiera la garantía implícita MERCANTIL o de 
 * APTITUD PARA UN PROPÓSITO DETERMINADO. Consulte los detalles de la 
 * Licencia Pública General GNU para obtener una información más detallada. 
 * 
 * Debería haber recibido una copia de la Licencia Pública General GNU junto a 
 * 'Juntando Líneas'. En caso contrario, consulte <http://www.gnu.org/licenses/>.
 * 
 * @author Antonio Sánchez
 */
package es.ingantosan.labjava.mybatis.bitácora.persistencia;

import es.ingantosan.labjava.mybatis.bitácora.dominio.Artículo;
import java.util.List;
import java.util.Map;

public interface Parámetros {
    
    public static class Grano {
        private int id;
        public String texto;
        
        public Grano(int id, String texto) {
            this.id = id;
            this.texto = texto;
        }
    }
    
    
    /*
     * ***************************************************************************************
     * PARÁMETROS SIMPLES POR INTERFAZ
     * ***************************************************************************************
     */    

    public List<Artículo> artículosPorAutorYTextoEnCuerpoPorInterfaz(int idAutor, String textoEnCuerpo);
    
    public List<Artículo> artículosPorAutorPorInterfaz(int idAutor);
    
    
    /*
     * ***************************************************************************************
     * PARÁMETROS POR GRANO
     * ***************************************************************************************
     */    
    
    public List<Artículo> artículosPorAutorYTextoEnCuerpoPorGranoYParámetro(Grano grano);
    
    public List<Artículo> artículosPorAutorYTextoEnCuerpoPorGranoSinParámetro(Grano grano);
    
    public List<Artículo> artículosPorAutorYTextoEnCuerpoPorGranoNotaciónParam(Grano grano);
    
    public List<Artículo> artículosPorAutorYTextoEnCuerpoPorMúltiplesGranos(Grano gId, Grano gTexto);
    
    
    /*
     * ***************************************************************************************
     * PARÁMETROS POR MAPA
     * ***************************************************************************************
     */    
    
    public List<Artículo> artículosPorAutorYTextoEnCuerpoPorMapa(Map mapa);

}
