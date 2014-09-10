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

import es.ingantosan.labjava.mybatis.bitácora.dominio.Artículo;
import es.ingantosan.labjava.mybatis.bitácora.persistencia.Parámetros;
import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Probamos las distintas formas de pasar parámetros a las consultas en el xml:
 * parameterType, parámetros en la interfaz, granos, ganos en interfaz, mapas.
 *
 * CONCLUSIONES:
 *
 *
 * PENDIENTES:
 * - Probar sobrecarga en métodos de interfaz.
 * - Granos con ognl.
 * - Errores en los tipos esperado y pasado (parameterType)
 * - Tipos extraños en interfaz.
 * - Nulos y notación #{property,javaType=int,jdbcType=NUMERIC}
 * - IN, OUT, INOUT
 * - STRUCT
 * - typeHandler
 * - Notación ${}
 * - Uso de @Param
 *
 * @author Antonio Sánchez
 */
public class PruebaParámetros extends PruebaBase {
    Parámetros param;
    Parámetros.Grano grano;
    
    @Before
    public void inicio() {
        grano = new Parámetros.Grano(ID_AUTOR_ANTONIO, CLAVE_MUNDO);
    }
    
    /*
     * ***************************************************************************************
     * PARÁMETROS SIMPLES POR INTERFAZ
     * ***************************************************************************************
     */    

    /**
     * Varios parámetros simples en la interfaz.
     */
    @Test
    public void parámetrosMúltiples() {
        SqlSession sesión = null;
        List<Artículo> artículos = null;

        try {
            sesión = factoría.openSession();
            param = sesión.getMapper(Parámetros.class);
            artículos = param.artículosPorAutorYTextoEnCuerpoPorInterfaz(ID_AUTOR_ANTONIO, CLAVE_MUNDO);
        } finally {
            sesión.close();
        }

        assertNotNull(artículos);
        assertEquals(OCURRENCIAS_CLAVE_MUNDO_ARTÍCULOS_ANTONIO, artículos.size());
    }

    /**
     * Un solo parámetro en la interfaz, que sería equivalente a usar parameterType.
     */
    @Test
    public void parámetroMúltipleÚnico() {
        SqlSession sesión = null;
        List<Artículo> artículos = null;

        try {
            sesión = factoría.openSession();
            param = sesión.getMapper(Parámetros.class);
            artículos = param.artículosPorAutorPorInterfaz(ID_AUTOR_ANTONIO);
        } finally {
            sesión.close();
        }

        assertNotNull(artículos);
        assertEquals(TOTAL_ARTÍCULOS_ANTONIO, artículos.size());
    }
    

    /*
     * ***************************************************************************************
     * PARÁMETROS POR GRANO
     * ***************************************************************************************
     */    

    /**
     * Un único grano en parameterType.
     */
    @Test
    public void parámetroGranoYParámetro() {
        SqlSession sesión = null;
        List<Artículo> artículos = null;

        try {
            sesión = factoría.openSession();
            param = sesión.getMapper(Parámetros.class);
            artículos = param.artículosPorAutorYTextoEnCuerpoPorGranoYParámetro(grano);
        } finally {
            sesión.close();
        }

        assertNotNull(artículos);
        assertEquals(OCURRENCIAS_CLAVE_MUNDO_ARTÍCULOS_ANTONIO, artículos.size());
    }

    /**
     * Un único grano pero sin parameterType.
     */
    @Test
    public void parámetroGranoSinParámetro() {
        SqlSession sesión = null;
        List<Artículo> artículos = null;

        try {
            sesión = factoría.openSession();
            param = sesión.getMapper(Parámetros.class);
            artículos = param.artículosPorAutorYTextoEnCuerpoPorGranoSinParámetro(grano);
        } finally {
            sesión.close();
        }

        assertNotNull(artículos);
        assertEquals(OCURRENCIAS_CLAVE_MUNDO_ARTÍCULOS_ANTONIO, artículos.size());
    }

    /**
     * Un solo grano en la interfaz.
     * Si solo hay un parámetro se pasa tal cual, si hay más de uno se pasan dentro de un hashmap. 
     * Por eso no acepta la notación 'param1'.
     */
    @Test(expected=org.apache.ibatis.exceptions.PersistenceException.class)
    public void parámetroGranoPorInterfaz() {
        SqlSession sesión = null;
        List<Artículo> artículos = null;

        try {
            sesión = factoría.openSession();
            param = sesión.getMapper(Parámetros.class);
            artículos = param.artículosPorAutorYTextoEnCuerpoPorGranoNotaciónParam(new Parámetros.Grano(ID_AUTOR_ANTONIO, CLAVE_MUNDO));
        } finally {
            sesión.close();
        }

//        assertNotNull(artículos);
//        assertEquals(OCURRENCIAS_CLAVE_MUNDO_ARTÍCULOS_ANTONIO, artículos.size());
    }

    /**
     * Dos granos en interfaz.
     * Si solo hay un parámetro se pasa tal cual, si hay más de uno se pasan dentro de un hashmap. 
     * Por eso aquí si es aceptada la notación 'param1'.
     */
    @Test
    public void parámetroGranosMúltiples() {
        SqlSession sesión = null;
        List<Artículo> artículos = null;

        try {
            sesión = factoría.openSession();
            param = sesión.getMapper(Parámetros.class);
            artículos = param.artículosPorAutorYTextoEnCuerpoPorMúltiplesGranos(new Parámetros.Grano(ID_AUTOR_ANTONIO, null),
                    new Parámetros.Grano(-1, CLAVE_MUNDO));
            assertNotNull(artículos);
            assertEquals(OCURRENCIAS_CLAVE_MUNDO_ARTÍCULOS_ANTONIO, artículos.size());

            //con el mismo grano
            Parámetros.Grano grano = new Parámetros.Grano(ID_AUTOR_ANTONIO, CLAVE_MUNDO);
            artículos = param.artículosPorAutorYTextoEnCuerpoPorMúltiplesGranos(grano, grano);

            assertNotNull(artículos);
            assertEquals(OCURRENCIAS_CLAVE_MUNDO_ARTÍCULOS_ANTONIO, artículos.size());


        } finally {
            sesión.close();
        }

    }
    

    /*
     * ***************************************************************************************
     * PARÁMETROS POR MAPA
     * ***************************************************************************************
     */
    
    @Test
    public void parámetrosPorMapa() {
        SqlSession sesión = null;
        List<Artículo> artículos = null;
        HashMap hashMap = new HashMap<String, Object>();

        try {
            sesión = factoría.openSession();
            param = sesión.getMapper(Parámetros.class);
            
            hashMap.put("id", ID_AUTOR_ANTONIO);
            hashMap.put("texto", CLAVE_MUNDO);
            artículos = param.artículosPorAutorYTextoEnCuerpoPorMapa(hashMap);
            
            assertNotNull(artículos);
            assertEquals(OCURRENCIAS_CLAVE_MUNDO_ARTÍCULOS_ANTONIO, artículos.size());

        } finally {
            sesión.close();
        }

    }
}
