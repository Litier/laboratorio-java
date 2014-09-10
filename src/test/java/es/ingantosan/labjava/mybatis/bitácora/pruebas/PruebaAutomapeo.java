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

import es.ingantosan.labjava.mybatis.bitácora.persistencia.Automapeo;
import es.ingantosan.labjava.mybatis.bitácora.dominio.Bitácora;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;

/**
 * Probamos el comportamiento de la configuración autoMappingBehavior. 
 * 
 * El valor de esta configuración afecta al modo en que se cargan los mapas,
 * según estén definidos los mismos.
 * 
 * Probaremos múltiples escenarios: asociaciones/colecciones anidados en mapa o en consulta; 
 * la ausencia de mapa mapeando directamente con ResultType; el uso de notación OGNL. Todo 
 * combinado con los distintos tipos de automapeo.
 *
 * En definitiva estas son las conclusiones: 
 *
 * AutoMappingBehavior.NONE: No realiza ningún mapeo automáticamente, todos los
 * mapeos deben ser explícitos.
 *
 * AutoMappingBehavior.PARTIAL: Automáticamente mapea solo propiedades simples
 * siempre y cuando el mapa no tenga ninguna ningun otro mapa anidado, en cuyo
 * caso se comporta como NONE y no realiza ningún mapeo automáticamente, solo
 * los explícitos. Si en cambio estamos utilizando notación OGNL entonces PARTIAL 
 * no se comporta exáctamente igual con anidamiento explícito en mapa, pues 
 * en este caso SI carga automáticamente las propiedades simples.
 *
 * AutoMappingBehavior.FULL: Automáticamente mapea tanto propiedades simples
 * como anidadas. En cualquier caso la asociación o colección anidada debe estar 
 * explícitamente definida en el mapa.
 * 
 * Lo anterior se cumple tanto en mapas como en consultas anidados. 
 *
 * PENDIENTES: 
 * - Probar anidaciones y consultas complejas, con varios niveles y que manejen mas datos.
 *
 * @author Antonio Sánchez
 */
public class PruebaAutomapeo extends PruebaBase {

    Automapeo conf;

    @Before
    public void setUp() throws Exception {
        factoría.getConfiguration().setAutoMappingBehavior(AutoMappingBehavior.PARTIAL);
    }
    
    
    /* ****************************************************************************************
     * Asociaciones en mapa
     * ****************************************************************************************/

    /**
     * Con FULL se aplica automapeo tanto en propiedades simples como las
     * anidadas por medio de asociaciones.
     */
    @Test
    public void mapeaAutomaticamente_PropiedadesSimplesYAnidadas_ConAutoMapeoFULL() {
        SqlSession sesión = null;
        Bitácora bitácora = null;

        try {
            factoría.getConfiguration().setAutoMappingBehavior(AutoMappingBehavior.FULL);
            sesión = factoría.openSession();
            conf = sesión.getMapper(Automapeo.class);
            bitácora = conf.bitácoraConAutorAsociado(ID_BITÁCORA_FELIPE);

            assertNotNull(bitácora);
            assertEquals(ID_BITÁCORA_FELIPE, bitácora.getId());
            assertEquals(TÍTULO_BITÁCORA_FELIPE, bitácora.getTítulo()); //automapea
            assertNotNull(bitácora.getAutor());
            assertEquals(ID_AUTOR_FELIPE, bitácora.getAutor().getId());
            assertEquals(NOMBRE_AUTOR_FELIPE, bitácora.getAutor().getNombre()); //automapea
        } finally {
            sesión.close();
        }
    }

    /**
     * Con PARTIAL (NONE también) no hay mapeo automático si existe una
     * asociación en el mapa.
     */
    @Test
    public void noMapeaAutomáticamente_Nada_EnMapaConAsociación_ConAutoMapeoPARTIALoNONE() {
        SqlSession sesión = null;
        Bitácora bitácora = null;

        try {
            factoría.getConfiguration().setAutoMappingBehavior(AutoMappingBehavior.PARTIAL);
            sesión = factoría.openSession();
            conf = sesión.getMapper(Automapeo.class);
            bitácora = conf.bitácoraConAutorAsociado(ID_BITÁCORA_FELIPE);

            assertNotNull(bitácora);
            assertEquals(ID_BITÁCORA_FELIPE, bitácora.getId()); 
            assertNull(bitácora.getTítulo()); //no automapea
            assertNotNull(bitácora.getAutor());
            assertEquals(ID_AUTOR_FELIPE, bitácora.getAutor().getId());
            assertNull(bitácora.getAutor().getNombre()); //no automapea

        } finally {
            sesión.close();
        }

        try {
            factoría.getConfiguration().setAutoMappingBehavior(AutoMappingBehavior.NONE);
            sesión = factoría.openSession();
            conf = sesión.getMapper(Automapeo.class);
            bitácora = conf.bitácoraConAutorAsociado(ID_BITÁCORA_FELIPE);

            assertNotNull(bitácora);
            assertEquals(ID_BITÁCORA_FELIPE, bitácora.getId()); 
            assertNull(bitácora.getTítulo()); //no automapea
            assertNotNull(bitácora.getAutor());
            assertEquals(ID_AUTOR_FELIPE, bitácora.getAutor().getId());
            assertNull(bitácora.getAutor().getNombre()); //no automapea

        } finally {
            sesión.close();
        }

    }

    /**
     * Con PARTIAL (FULL también) hay mapeo automático de las propiedades simples si 
     * NO existe unaasociación en el mapa. Si existiera una asociación FULL mapearía
     * automáticamente, pero PARTIAL no lo hará. No se crea en absoluto la asociación,
     * ésta debe ser explícita en el mapa. 
     * 
     */
    @Test
    public void mapeaAutomaticamente_PropiedadesSimples_EnMapaSinAsociación_ConAutoMapeoPARTIALoFULL() {
        SqlSession sesión = null;
        Bitácora bitácora = null;

        try {
            factoría.getConfiguration().setAutoMappingBehavior(AutoMappingBehavior.PARTIAL);
            sesión = factoría.openSession();
            conf = sesión.getMapper(Automapeo.class);
            bitácora = conf.bitácoraSinAutorAsociado(ID_BITÁCORA_FELIPE);

            assertNotNull(bitácora);
            assertEquals(ID_BITÁCORA_FELIPE, bitácora.getId());
            assertEquals(TÍTULO_BITÁCORA_FELIPE, bitácora.getTítulo()); //automapea
            assertNull(bitácora.getAutor()); //no automapea propiedad anidada            
        } finally {
            sesión.close();
        }

        try {
            factoría.getConfiguration().setAutoMappingBehavior(AutoMappingBehavior.FULL);
            sesión = factoría.openSession();
            conf = sesión.getMapper(Automapeo.class);
            bitácora = conf.bitácoraSinAutorAsociado(ID_BITÁCORA_FELIPE);

            assertNotNull(bitácora);
            assertEquals(ID_BITÁCORA_FELIPE, bitácora.getId());
            assertEquals(TÍTULO_BITÁCORA_FELIPE, bitácora.getTítulo()); //automapea
            assertNull(bitácora.getAutor()); //no automapea propiedad anidada            
        } finally {
            sesión.close();
        }
    }

    /**
     * Con NONE no hay automapeo en mapa sin asociación. En otra prueba vemos
     * que tampoco hay automapeo con NONE y mapa con asociación
     */
    @Test
    public void noMapeaAutomáticamente_Nada_EnMapaSinAsociación_ConAutoMapeoNONE() {
        SqlSession sesión = null;
        Bitácora bitácora = null;

        try {
            factoría.getConfiguration().setAutoMappingBehavior(AutoMappingBehavior.NONE);
            sesión = factoría.openSession();
            conf = sesión.getMapper(Automapeo.class);
            bitácora = conf.bitácoraSinAutorAsociado(ID_BITÁCORA_FELIPE);
        } finally {
            sesión.close();
        }

        assertNotNull(bitácora);
        assertEquals(ID_BITÁCORA_FELIPE, bitácora.getId());
        assertNull(bitácora.getTítulo()); //no automapea propiedad simple
        assertNull(bitácora.getAutor()); //no automapea propiedad anidada
    }

    
    /* ****************************************************************************************
     * Colección anidada en mapa
     * ****************************************************************************************/

    /**
     * Con PARTIAL (NO FULL) se aplica automapeo tanto en propiedades simples como las
     * anidadas por medio de colecciones.
     */
    @Test
    public void mapeaAutomaticamente_PropiedadesSimplesYAnidadasEnColección_ConAutoMapeoFULL() {
        SqlSession sesión = null;
        Bitácora bitácora = null;

        try {
            factoría.getConfiguration().setAutoMappingBehavior(AutoMappingBehavior.FULL);
            sesión = factoría.openSession();
            conf = sesión.getMapper(Automapeo.class);
            bitácora = conf.bitácoraConArtículosEnMapaAnidado(ID_BITÁCORA_ESPERANZA);

            assertNotNull(bitácora);
            assertEquals(ID_BITÁCORA_ESPERANZA, bitácora.getId());
            assertEquals(TÍTULO_BITÁCORA_ESPERANZA, bitácora.getTítulo()); //automapea
            assertNotNull(bitácora.getArtículos());
            assertTrue(bitácora.getArtículos().size() > 0);
            assertNotNull(bitácora.getArtículos().get(0).getAsunto()); //automapea
            assertTrue(bitácora.getArtículos().get(0).getAsunto().length() > 0);
        } finally {
            sesión.close();
        }
    }
    
    /**
     * Con PARTIAL (también NONE) se aplica automapeo tanto en propiedades simples como las
     * anidadas por medio de colecciones.
     */
    @Test
    public void noMapeaAutomaticamente_NadaEnMapaConColección_ConAutoMapeoPARTIALoNONE() {
        SqlSession sesión = null;
        Bitácora bitácora = null;

        try {
            factoría.getConfiguration().setAutoMappingBehavior(AutoMappingBehavior.PARTIAL);
            sesión = factoría.openSession();
            conf = sesión.getMapper(Automapeo.class);
            bitácora = conf.bitácoraConArtículosEnMapaAnidado(ID_BITÁCORA_ESPERANZA);

            assertNotNull(bitácora);
            assertNull(bitácora.getTítulo()); //no  automapea
            assertNotNull(bitácora.getArtículos());
            assertTrue(bitácora.getArtículos().size() > 0);
            assertNull(bitácora.getArtículos().get(0).getAsunto()); //no automapea            
        } finally {
            sesión.close();
        }
        
        try {
            factoría.getConfiguration().setAutoMappingBehavior(AutoMappingBehavior.NONE);
            sesión = factoría.openSession();
            conf = sesión.getMapper(Automapeo.class);
            bitácora = conf.bitácoraConArtículosEnMapaAnidado(ID_BITÁCORA_ESPERANZA);

            assertNotNull(bitácora);
            assertNull(bitácora.getTítulo()); //no  automapea
            assertNotNull(bitácora.getArtículos());
            assertTrue(bitácora.getArtículos().size() > 0);
            assertNull(bitácora.getArtículos().get(0).getAsunto()); //no automapea            
        } finally {
            sesión.close();
        }
    }

    
    /* ****************************************************************************************
     * Notación OGNL
     * ****************************************************************************************/
    
    /**
     * Con PARTIAL (FULL también) en un mapa sin asociación pero con propiedades
     * ognl hay automapeo pero SOLO de las propiedades simples, no del objeto
     * anidado con ognl. 
     * 
     * Igual que si el ognl estuviera en los alias del SQL.
     * 
     * Aquí PARTIAL no se comporta exáctamente igual que si
     * tuviera asociación anidada. Aquí SI carga automáticamente las propiedades
     * simples, pero en el caso de haber asociación no cargaría las propiedades
     * simples.
     */
    @Test
    public void mapeaAutomaticamente_SOLOPropiedadesSimples_EnMapaSinAsociaciónYConPropiedadesOgnl_ConAutoMapeoPARTIALoFULL() {
        SqlSession sesión = null;
        Bitácora bitácora = null;

        try {
            factoría.getConfiguration().setAutoMappingBehavior(AutoMappingBehavior.PARTIAL);
            sesión = factoría.openSession();
            conf = sesión.getMapper(Automapeo.class);
            bitácora = conf.getBitácoraConAutorPorOgnlEnMapa(ID_BITÁCORA_FELIPE);

            assertNotNull(bitácora);
            assertEquals(ID_BITÁCORA_FELIPE, bitácora.getId());
            assertEquals(TÍTULO_BITÁCORA_FELIPE, bitácora.getTítulo());
            assertNotNull(bitácora.getAutor());
            assertEquals(ID_AUTOR_FELIPE, bitácora.getAutor().getId());
            assertEquals(NOMBRE_AUTOR_FELIPE, bitácora.getAutor().getNombre());
            assertNull(bitácora.getAutor().getUsuario());

        } finally {
            sesión.close();
        }

        try {
            factoría.getConfiguration().setAutoMappingBehavior(AutoMappingBehavior.FULL);
            sesión = factoría.openSession();
            conf = sesión.getMapper(Automapeo.class);
            bitácora = conf.getBitácoraConAutorPorOgnlEnMapa(ID_BITÁCORA_FELIPE);

            assertNotNull(bitácora);
            assertEquals(ID_BITÁCORA_FELIPE, bitácora.getId());
            assertEquals(TÍTULO_BITÁCORA_FELIPE, bitácora.getTítulo());
            assertNotNull(bitácora.getAutor());
            assertEquals(ID_AUTOR_FELIPE, bitácora.getAutor().getId());
            assertEquals(NOMBRE_AUTOR_FELIPE, bitácora.getAutor().getNombre());
            assertNull(bitácora.getAutor().getUsuario());

        } finally {
            sesión.close();
        }
    }
    
    /**
     * Con PARTIAL (FULL también) en un mapa sin asociación pero con propiedades
     * ognl en el alias de SQL hay automapeo pero SOLO de las propiedades simples, 
     * no del objeto anidado con ognl. 
     * 
     * Igual que si el ognl estuviera en la propiedades del mapa.
     * 
     * Aquí PARTIAL no se comporta exáctamente igual que si
     * tuviera asociación anidada. Aquí SI carga automáticamente las propiedades
     * simples, pero en el caso de haber asociación no cargaría las propiedades
     * simples.
     */
    @Test
    public void mapeaAutomaticamente_SOLOPropiedadesSimples_EnMapaSinAsociaciónYConOgnlEnSQL_ConAutoMapeoPARTIALoFULL() {
        SqlSession sesión = null;
        Bitácora bitácora = null;

        try {
            factoría.getConfiguration().setAutoMappingBehavior(AutoMappingBehavior.PARTIAL);
            sesión = factoría.openSession();
            conf = sesión.getMapper(Automapeo.class);
            bitácora = conf.getBitácoraConAutorPorOgnlEnSQL(ID_BITÁCORA_FELIPE);
            
            assertNotNull(bitácora);
            assertEquals(ID_BITÁCORA_FELIPE, bitácora.getId());
            assertEquals(TÍTULO_BITÁCORA_FELIPE, bitácora.getTítulo());
            assertNotNull(bitácora.getAutor());
            assertEquals(ID_AUTOR_FELIPE, bitácora.getAutor().getId());
            assertEquals(NOMBRE_AUTOR_FELIPE, bitácora.getAutor().getNombre());
            assertNull(bitácora.getAutor().getUsuario());            
        } finally {
            sesión.close();
        }

        try {
            factoría.getConfiguration().setAutoMappingBehavior(AutoMappingBehavior.FULL);
            sesión = factoría.openSession();
            conf = sesión.getMapper(Automapeo.class);
            bitácora = conf.getBitácoraConAutorPorOgnlEnSQL(ID_BITÁCORA_FELIPE);
            
            assertNotNull(bitácora);
            assertEquals(ID_BITÁCORA_FELIPE, bitácora.getId());
            assertEquals(TÍTULO_BITÁCORA_FELIPE, bitácora.getTítulo());
            assertNotNull(bitácora.getAutor());
            assertEquals(ID_AUTOR_FELIPE, bitácora.getAutor().getId());
            assertEquals(NOMBRE_AUTOR_FELIPE, bitácora.getAutor().getNombre());
            assertNull(bitácora.getAutor().getUsuario());            
        } finally {
            sesión.close();
        }

    }
    

    /**
     * Con NONE en un mapa sin asociación pero con propiedades ognl no hay
     * automapeo.
     */
    @Test
    public void noMapeaAutomáticamente_Nada_EnMapaSinAsociaciónYConPropiedadesOgnl_ConAutoMapeoNONE() {
        SqlSession sesión = null;
        Bitácora bitácora = null;

        try {
            factoría.getConfiguration().setAutoMappingBehavior(AutoMappingBehavior.NONE);
            sesión = factoría.openSession();
            conf = sesión.getMapper(Automapeo.class);
            bitácora = conf.getBitácoraConAutorPorOgnlEnMapa(ID_BITÁCORA_FELIPE);

            assertNotNull(bitácora);
            assertEquals(ID_BITÁCORA_FELIPE, bitácora.getId());
            assertNull(bitácora.getTítulo());
            assertNotNull(bitácora.getAutor());
            assertEquals(ID_AUTOR_FELIPE, bitácora.getAutor().getId());
            assertEquals(NOMBRE_AUTOR_FELIPE, bitácora.getAutor().getNombre());
            assertNull(bitácora.getAutor().getUsuario());

        } finally {
            sesión.close();
        }
    }
    
    
    /* ****************************************************************************************
     * NONE con ResultType.
     * ****************************************************************************************/

    /**
     * Con NONE y utilizando ResultType en lugar de un mapa vemos que ni siquiera se produce 
     * carga alguna y lo devuelto es Null.
     */
    @Test
    public void noCarga_NadaYQuedaNULL_UsandoResultType_ConAutoMapeoNONE() {
        SqlSession sesión = null;
        Bitácora bitácora = null;

        try {
            factoría.getConfiguration().setAutoMappingBehavior(AutoMappingBehavior.NONE);
            sesión = factoría.openSession();
            conf = sesión.getMapper(Automapeo.class);
            bitácora = conf.bitácoraSinMapeo(ID_BITÁCORA_FELIPE);
        } finally {
            sesión.close();
        }

        assertNull(bitácora);
    }
    
    
    /* ****************************************************************************************
     * Pruebas con mapas con CONSULTA ANIDADA. Estas pruebas las hacemos para completar
     * la batería y demostrar cómo funciona el automapeo con consultas anidadas. 
     * 
     * NOTA: Aquí no probamos FULL porque las consultas son sencillas y solo implican
     * una tabla a la vez, es decir, que no hay asociaciones ni colecciones anidadas.
     * ****************************************************************************************/
    
    /**
     * Mapea automaticamente propiedades simples en consulta anidada de colección.
     * 
     * Con FULL daría el mismo resultado. NONE lo haría fallar.
     */
    @Test
    public void mapeaAutomaticamente_PropiedadesSimples_EnConsultaAnidadaDeColección() {
        SqlSession sesión = null;
        Bitácora bitácora = null;

        try {
            factoría.getConfiguration().setAutoMappingBehavior(AutoMappingBehavior.PARTIAL);
            sesión = factoría.openSession();
            conf = sesión.getMapper(Automapeo.class);
            bitácora = conf.bitácoraConArtículosEnConsultaAnidada(ID_BITÁCORA_ESPERANZA);

            assertNotNull(bitácora);
            assertEquals(ID_BITÁCORA_ESPERANZA, bitácora.getId());
            assertEquals(TÍTULO_BITÁCORA_ESPERANZA, bitácora.getTítulo()); //automapea
            assertNotNull(bitácora.getArtículos());
            assertTrue(bitácora.getArtículos().size() > 0);
            assertNotNull(bitácora.getArtículos().get(0).getAsunto()); //automapea
            assertTrue(bitácora.getArtículos().get(0).getAsunto().length() > 0);
        } finally {
            sesión.close();
        }
    }
    
    /**
     * Mapea automaticamente propiedades simples en consulta anidada de asociación.
     * 
     * Con FULL daría el mismo resultado. NONE lo haría fallar.
     */
    @Test
    public void mapeaAutomaticamente_PropiedadesSimples_EnConsultaAnidadaDeAsociación() {
        SqlSession sesión = null;
        Bitácora bitácora = null;

        try {
            factoría.getConfiguration().setAutoMappingBehavior(AutoMappingBehavior.PARTIAL);
            sesión = factoría.openSession();
            conf = sesión.getMapper(Automapeo.class);
            bitácora = conf.bitácoraConAutorEnConsultaAnidada(ID_BITÁCORA_ESPERANZA);

            assertNotNull(bitácora);
            assertEquals(ID_BITÁCORA_ESPERANZA, bitácora.getId());
            assertEquals(TÍTULO_BITÁCORA_ESPERANZA, bitácora.getTítulo()); //automapea
            assertNotNull(bitácora.getAutor());
            assertNotNull(bitácora.getAutor().getNombre());//automapea
        } finally {
            sesión.close();
        }
    }
    
}
