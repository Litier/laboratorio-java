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


import es.ingantosan.labjava.mybatis.bitácora.persistencia.Actualización;
import es.ingantosan.labjava.mybatis.bitácora.dominio.Autor;
import org.apache.ibatis.session.SqlSession;
import org.junit.*;
import static org.junit.Assert.*;

public class PruebaActualización extends PruebaBase {
    Actualización mod;

    @Test
    public void insertarConUseGeneratedKeys() {
        SqlSession sesión = null;
        Autor autor = null;
        
        autor = new Autor(-1);
        
        autor.setNombre("Nombre Apellido1 Apellido2");
        autor.setUsuario("usuario");
        autor.setContraseña("ñeña");
        
        try {            
            sesión = factoría.openSession();
            mod = sesión.getMapper(Actualización.class);
            mod.insertarConUseGeneratedKeys(autor);
        } finally {
            sesión.close();
        }

        assertFalse(-1 == autor.getId());
        assertEquals(INICIO_IDENTIDAD, autor.getId());
    }

}                                                                                                                                                                                                                                                                                                                                                                                   
