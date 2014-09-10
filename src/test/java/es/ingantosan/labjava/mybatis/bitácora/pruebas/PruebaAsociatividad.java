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

import es.ingantosan.labjava.mybatis.bitácora.persistencia.Asociatividad;
import es.ingantosan.labjava.mybatis.bitácora.dominio.Autor;
import es.ingantosan.labjava.mybatis.bitácora.dominio.Bitácora;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Probamos aquí diversos aspectos de la asociatividad, centrándonos en el uso
 * del atributo 'column' en diversas circunstancias: association, collection, 
 * mapas anidados, consultas anidadas.
 * 
 * CONCLUSIONES AQUÍ: http://code.google.com/p/juntando-lineas/wiki/Asociatividad
 * 
 * REFERENCIAS:
 *  - Comentarios en el xml con los mapas.
 *  - http://code.google.com/p/mybatis/issues/detail?id=522
 *  - http://groups.google.com/group/mybatis-user/browse_thread/thread/84928c7964767761
 * 
 * PENDIENTES:
 * - Caso: varios parámetros para la consulta anidada...
 * - Probar clave compuesta
 * - Probar USO_INCORRECTO_ColecciónAnidadaEnMapaSinID en una consulta que que no traiga 'nombre' repetido
 * - Probar asociación sin column y sin parámetro en la anidada.
 *
 * @author Antonio Sánchez
 */
public class PruebaAsociatividad extends PruebaBase {
    Asociatividad asoc;

    @Test
    public void entidadSimpleSinAsociaciones() {
        SqlSession sesión = null;
        Autor autor = null;

        try {
            sesión = factoría.openSession();
            asoc = sesión.getMapper(Asociatividad.class);
            autor = asoc.autorPorId(2);
        } finally {
            sesión.close();
        }

        assertNotNull(autor);
        assertEquals(ID_AUTOR_ANTONIO, autor.getId());
        assertEquals(NOMBRE_AUTOR_ANTONIO, autor.getNombre());
        assertNull(autor.getBitácoras());
    }

    /*
     * ***************************************************************************************
     * COLECCIÓN EN MAPA
     * ***************************************************************************************
     */
    
    /**
     * Vemos que en en colección por mapa el uso de 'column' es irrelevante y tanto su inclusión
     * como el valor que se le de no afectan al resultado pues 'column' aquí es ignorada.
     */
    @Test
    public void colecciónAnidadaEnMapa() {
        SqlSession sesión = null;

        try {
            sesión = factoría.openSession();
            asoc = sesión.getMapper(Asociatividad.class);

            correctoAutoresConArtículos(asoc.autoresConArtículosPorMapaAnidado());
            
            //column es ignorada por completo en colección por mapa, no afecta el resultado
            correctoAutoresConArtículos(asoc.USO_INDEBIDO_autoresConArtículosPorMapaAnidadoColumnaInexistente());
            correctoAutoresConArtículos(asoc.USO_INDEBIDO_autoresConArtículosPorMapaAnidadoColumnaIncorrecta());

        } finally {
            sesión.close();
        }
    }

    /**
     * Traemos una colección pero no definimos ID en el mapa externo y además la 
     * consulta viene con valores repetidos así que MyBatis no sabe como resolver
     * la 'clave primaria'
     */
    @Test
    public void USO_INCORRECTO_ColecciónAnidadaEnMapaSinID() {
        SqlSession sesión = null;
        List<Autor> autores = null;

        try {
            sesión = factoría.openSession();
            asoc = sesión.getMapper(Asociatividad.class);
            autores = asoc.USO_INCORRECTO_AutoresConArtículosMapaSinID();

            assertNotNull(autores);
            assertFalse(autores.isEmpty());
            assertFalse(TOTAL_AUTORES == autores.size());

        } finally {
            sesión.close();
        }

    }

    /*
     * ***************************************************************************************
     * COLECCIÓN EN CONSULTA
     * ***************************************************************************************
     */
    
    /**
     * Mapa con colección que utiliza column y con consulta anidada que utiliza parámetro.
     */
    @Test
    public void colecciónConColumnYConsultaAnidadaConArgumento() {
        SqlSession sesión = null;
        Bitácora bitácora = null;

        try {
            sesión = factoría.openSession();
            asoc = sesión.getMapper(Asociatividad.class);
            bitácora = asoc.bitácoraConArtículosEnConsultaAnidada(ID_BITÁCORA_ESPERANZA);

            assertNotNull(bitácora);
            assertEquals(ID_BITÁCORA_ESPERANZA, bitácora.getId());
            assertNotNull(bitácora.getArtículos());
            assertEquals(TOTAL_ARTÍCULOS_BITÁCORA_ESPERANZA, bitácora.getArtículos().size());
        } finally {
            sesión.close();
        }
    }

    
    /**
     * Mapa con colección que utiliza column y con consulta anidada que utiliza parámetro.
     * 
     * Este mapa demuestra el valor que debe pasarse a column.
     */
    @Test
    public void valorDeColumnEnColecciónConConsultaAnidada() {
        SqlSession sesión = null;
        List<Bitácora> bitácoras = null;
        Bitácora bitácora = null;

        try {
            sesión = factoría.openSession();
            asoc = sesión.getMapper(Asociatividad.class);
            bitácoras = asoc.bitácorasConArtículosEnConsultaAnidada();

            assertNotNull(bitácoras);
            assertEquals(TOTAL_BITÁCORAS, bitácoras.size());
            bitácora = bitácoras.get(ID_BITÁCORA_ESPERANZA - 1);
            assertEquals(ID_BITÁCORA_ESPERANZA, bitácora.getId());
            assertNotNull(bitácora.getArtículos());
            assertEquals(TOTAL_ARTÍCULOS_BITÁCORA_ESPERANZA, bitácora.getArtículos().size());
        } finally {
            sesión.close();
        }
    }

    /**
     * Mapa con colección en consulta anidada que en column tiene un valor de columna 
     * inexistente en la consulta padre. 
     * 
     * No se carga la colección porque MB no ejecuta la consulta al no reconocer una
     * columna válida.
     */
    @Test
    public void USO_INCORRECTO_colecciónEnConsultaAnidadaConColumnInexistente() {
        SqlSession sesión = null;
        Bitácora bitácora = null;

        try {
            sesión = factoría.openSession();
            asoc = sesión.getMapper(Asociatividad.class);
            bitácora = asoc.USO_INCORRECTO_BitácorasConArtículosEnConsultaAnidadaConColumnInexistente(ID_BITÁCORA_ESPERANZA);

            assertNotNull(bitácora);
            assertNull(bitácora.getTodasEtiquetas());
        } finally {
            sesión.close();
        }
    }
    
    /**
     * Mapa con colección en consulta anidada que no define parámetro y que en column
     * tiene un valor de columna existente en la consulta padre. 
     * 
     * Se carga la colección porque MB ejecuta la consulta al reconocer una columna válida
     * y a pesar de que no haya paso de parámetros, así que cualquier columna vale para
     * ejecutar la consulta.
     * 
     * No parece una práctica muy ortodoxa.
     */
    @Test
    public void USO_EXTRAÑO_colecciónEnConsultaAnidadaSinParámetro() {
        SqlSession sesión = null;
        Bitácora bitácora = null;

        try {
            sesión = factoría.openSession();
            asoc = sesión.getMapper(Asociatividad.class);
            bitácora = asoc.USO_EXTRAÑO_BitácorasConArtículosEnConsultaAnidadaSinParámetro(ID_BITÁCORA_ESPERANZA);

            assertNotNull(bitácora);
            assertNotNull(bitácora.getTodasEtiquetas());
            assertEquals(TOTAL_ETIQUETAS, bitácora.getTodasEtiquetas().size());
        } finally {
            sesión.close();
        }
    }
    

    /*
     * ***************************************************************************************
     * ASOCIACIÓN EN MAPA
     * ***************************************************************************************
     */
    
    /**
     * Vemos que en en asociación por mapa el uso de 'column' es irrelevante y tanto su inclusión
     * como el valor que se le de no afectan al resultad pues 'column' aquí es ignorada.
     */
    @Test
    public void consultaConAsociaciónPorMapa() {
        SqlSession sesión = null;
        List<Bitácora> bitácoras = null;

        try {
            sesión = factoría.openSession();
            asoc = sesión.getMapper(Asociatividad.class);

            correctoBitácorasConAutores(asoc.bitácorasConAutorPorMapaAnidado());
            
            //column es ignorada por completo en asociación por mapa, no afecta el resultado
            correctoBitácorasConAutores(asoc.USO_INDEBIDO_BitácorasConAutorPorMapaAnidadoColumnaInexistente());
            correctoBitácorasConAutores(asoc.USO_INDEBIDO_BitácorasConAutorPorMapaAnidadoColumnaIncorrecta());

        } finally {
            sesión.close();
        }

    }

    /*
     * ***************************************************************************************
     * ASOCIACIÓN EN CONSULTA
     * ***************************************************************************************
     */
    
    /**
     * Asociación por consulta.
     */
    @Test
    public void consultaConAsociaciónPorConsulta() {
        SqlSession sesión = null;

        try {
            sesión = factoría.openSession();
            asoc = sesión.getMapper(Asociatividad.class);

            correctoBitácorasConAutores(asoc.bitácorasConAutorPorMapaAnidado());
        } finally {
            sesión.close();
        }
    }
           
    /**
     * No se carga una asociación por consulta si se le da un valor incorrecto a 'column' que no 
     * corresponde con la columna que pueda hacer la correspondencia entre las filas de las 
     * consultas externa y anidada. 
     */
    @Test
    public void USO_INCORRECTO_asociaciónPorConsultaAnidadaConValorIncorrectoEnColumn() {
        SqlSession sesión = null;
        Bitácora bitácora = null;

        try {
            sesión = factoría.openSession();
            asoc = sesión.getMapper(Asociatividad.class);
            bitácora = asoc.USO_INCORRECTO_BitácoraConAutorEnConsultaAnidadaConColumnIncorrectaEnAssociaton(ID_BITÁCORA_ESPERANZA);

            assertNotNull(bitácora);
            assertNull(bitácora.getAutor());
        } finally {
            sesión.close();
        }
    }    

    /*
     * ***************************************************************************************
     * UTIL
     * ***************************************************************************************
     */
    
    private void correctoAutoresConArtículos(List<Autor> autores) {
        assertNotNull(autores);
        assertFalse(autores.isEmpty());
        assertEquals(TOTAL_AUTORES, autores.size());

        assertEquals(ID_AUTOR_FELIPE, autores.get(0).getId());
        assertNotNull(autores.get(0).getArtículos());
        assertEquals(TOTAL_ARTÍCULOS_FELIPE, autores.get(0).getArtículos().size());
    }

    private void correctoBitácorasConAutores(List<Bitácora> bitácoras) {
        assertNotNull(bitácoras);
        assertFalse(bitácoras.isEmpty());
        assertEquals(TOTAL_BITÁCORAS, bitácoras.size());

        assertEquals(ID_BITÁCORA_FELIPE, bitácoras.get(0).getId());
        assertNotNull(bitácoras.get(0).getAutor());
        assertEquals(ID_AUTOR_FELIPE, bitácoras.get(0).getAutor().getId());
    }
}
