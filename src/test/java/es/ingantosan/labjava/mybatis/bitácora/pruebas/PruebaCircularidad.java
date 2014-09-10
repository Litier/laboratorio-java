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

package es.ingantosan.labjava.mybatis.bitácora.pruebas;


import es.ingantosan.labjava.mybatis.bitácora.persistencia.CircularidadAutorDeBitácoraResultHandler;
import es.ingantosan.labjava.mybatis.bitácora.dominio.Autor;
import es.ingantosan.labjava.mybatis.bitácora.dominio.Bitácora;
import es.ingantosan.labjava.mybatis.bitácora.dominio.Categoría;
import es.ingantosan.labjava.mybatis.bitácora.dominio.Menú;
import es.ingantosan.labjava.mybatis.bitácora.persistencia.Circularidad;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Pruebas sobre la circularidad de los objetos creados por MyBatis. Con circularidad entendemos
 * que un hijo que tenga referencia al padre utilize efectiamente esa misma referencia de la JVM.
 * 
 * PENDIENTE:
 *  - Por dominio no se puede lograr la circularidad porque MB primero pone la lista
 * vacía y una vez puesta le va agregando instancias, así que el único remedio sería
 * una lista observadoras a medida. Tiene sus inconvenientes...
 * - Demostrar que poner una asociación dentro de la colección realmente crea otra instancia diferente.
 * - Probar usando caché.
 * - Caso todas las bitácoras con todas las etiquetas: la referencia a la lista de etiquetas ¿sería la misma para
 * todas las bitácoras o se crearía una lista por cada bitácora? Idem. para las instancias de Etiqueta, ¿compartidas o 
 * distintas?
 * - Implementar http://code.google.com/p/mybatis/issues/detail?id=509
 * 
 * @author Antonio Sánchez
 */
public class PruebaCircularidad extends PruebaBase {
    Circularidad circ;

    @Test
    public void RENOMBRAR_SÍHayCircularidadEstándar() {
        SqlSession sesión = null;
        Autor autor = null;
        
        try {            
            sesión = factoría.openSession();
            circ = sesión.getMapper(Circularidad.class);
            autor = circ.getAutorConBitácoras(2);
        } finally {
            sesión.close();
        }
        
        assertNotNull(autor);
        assertEquals(ID_AUTOR_ANTONIO, autor.getId());
        assertNotNull(autor.getBitácoras());
        assertEquals(TOTAL_BITÁCORAS_ANTONIO, autor.getBitácoras().size());
        assertNotNull(autor.getBitácoras().get(0).getAutor());
        assertSame(autor, autor.getBitácoras().get(0).getAutor());
    }
    
    @Test
    public void menús() {
        SqlSession sesión = null;
        List<Menú> menús = null;
        
        try {            
            sesión = factoría.openSession();
            circ = sesión.getMapper(Circularidad.class);
            menús = circ.getMenús();
        } finally {
            sesión.close();
        }
        
        for (Menú c : menús ) 
            System.out.println(c);        
        
        assertNotNull(menús);
        assertEquals(3, menús.size());
        assertNull(menús.get(0).padre); 
        
        //FALLA: menú de primer nivel no tiene padre
//        assertNotNull(menús.get(1).padre); //FALLA
//        assertNotNull(menús.get(2).padre); //FALLA
        
        //PASA: MENÚS hojas SÍ TIENEN PADRE, rama a
        assertNotNull(menús.get(1).hijos.get(0).padre); //aa
        assertSame(menús.get(1), menús.get(1).hijos.get(0).padre);
        assertNotNull(menús.get(1).hijos.get(1).padre); //ab
        assertSame(menús.get(1), menús.get(1).hijos.get(1).padre);
        //PASA: MENÚS hojas SÍ TIENEN PADRE, rama b
        assertNotNull(menús.get(2).hijos.get(0).padre); //ba
        assertSame(menús.get(2), menús.get(2).hijos.get(0).padre);
        assertNotNull(menús.get(2).hijos.get(1).padre); //bb
        assertSame(menús.get(2), menús.get(2).hijos.get(1).padre);
        
        
    }
    
    @Test
    @Ignore
    public void categorías() {
        SqlSession sesión = null;
        List<Categoría> categorías = null;
        
        try {            
            sesión = factoría.openSession();
            circ = sesión.getMapper(Circularidad.class);
            categorías = circ.getCategorías();
        } finally {
            sesión.close();
        }
        
        assertNotNull(categorías);
        for (Categoría c : categorías ) 
            System.out.println(c);
        
    }        
    
    
    /**
     * Después de cada servicio de persistencia aplicar la circularidad.
     * 
     * No es muy elegante precisamente.
     */
    @Test
    @Ignore
    public void circularidadManual() {
        SqlSession sesión = null;
        Autor autor = null;
        
        try {            
            sesión = factoría.openSession();
            circ = sesión.getMapper(Circularidad.class);
            autor = circ.getAutorConBitácoras(2);
            
            if (autor != null && autor.getBitácoras() != null)
                for (Bitácora b : autor.getBitácoras())
                    b.setAutor(autor);
        } finally {
            sesión.close();
        }
        
        assertNotNull(autor);
        assertEquals(ID_AUTOR_ANTONIO, autor.getId());
        assertNotNull(autor.getBitácoras());
        assertEquals(TOTAL_BITÁCORAS_ANTONIO, autor.getBitácoras().size());
        assertNotNull(autor.getBitácoras().get(0).getAutor());
        assertSame(autor, autor.getBitácoras().get(0).getAutor());
    }
    
    
    
    /**
     * Llamamos con un ResultHandler que se encargará de resolver la circularidad. 
     * 
     * Resuelve el inconveniente de que la circularidad no sea resuelta en el propio 
     * servicio de la interfaz sino que haya que hacerlo manualmente, pero al coste
     * de perder el uso de la interface de mapeo y la resolución de nombres y tipos
     * en tiempo de compilación, además de tener que crear una clase extra por cada
     * resolución de circularidad, lo cual es muy frecuente normalmente. 
     *
     * Demasiado código y complicación para una cosa tan elemental.
     */
    @Test
    @Ignore
    public void circularidadConResultHandler() {
        SqlSession sesión = null;
        Autor autor = null;
        
        try {            
            CircularidadAutorDeBitácoraResultHandler rh = new CircularidadAutorDeBitácoraResultHandler();
            
            sesión = factoría.openSession();
            sesión.select("getAutorConBitácoras", new Integer(2), rh);
            autor = rh.getAutor();
            
        } finally {
            sesión.close();
        }
        
        assertNotNull(autor);
        assertEquals(ID_AUTOR_ANTONIO, autor.getId());
        assertNotNull(autor.getBitácoras());
        assertEquals(TOTAL_BITÁCORAS_ANTONIO, autor.getBitácoras().size());
        assertNotNull(autor.getBitácoras().get(0).getAutor());
        assertSame(autor, autor.getBitácoras().get(0).getAutor());
    }
}
