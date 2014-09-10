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

import es.ingantosan.labjava.mybatis.bitácora.persistencia.Parámetros;
import org.junit.*;

/**
 * Probamos aquí... 
 * 
 * CONCLUSIONES:
 * 
 * 
 * PENDIENTES:
 * - colección y asociación SIN Column
 * - (resultType | resultMap)
 *
 * @author Antonio Sánchez
 */
//public class PruebaCargaXML extends PruebaBase {
public class PruebaCargaXML  {
    Parámetros param;

    /**
     * No debería permitirlo...
     */
    @Test
    @Ignore
    public void colecciónSINColumn() {
        
//        SqlSession sesión = null;
//        List<Artículo> artículos = null;
//
//        try {
//            sesión = factoría.openSession();
//            param = sesión.getMapper(Parámetros.class);
//            artículos = param.artículosPorAutorYTextoEnCuerpo(ID_AUTOR_ANTONIO, "mundo");
//        } finally {
//            sesión.close();
//        }
//
//        assertNotNull(artículos);
//        assertEquals(2, artículos.size());
    }

    /*
     * ***************************************************************************************
     * 
     * ***************************************************************************************
     */
    
}
