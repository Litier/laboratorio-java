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

import es.ingantosan.labjava.mybatis.bitácora.dominio.Autor;
import es.ingantosan.labjava.mybatis.bitácora.dominio.Bitácora;
import java.util.List;

public interface Asociatividad {

    /**
     * Un autor sin asociaciones.
     */
    public Autor autorPorId(int id);
    
    
    /*
     * ***************************************************************************************
     * COLECCIÓN EN MAPA
     * ***************************************************************************************
     */    
    
    /**
     * Todos los autores con sus respectivos artículos cargados utilizando una colección por mapa.
     */
    public List<Autor> autoresConArtículosPorMapaAnidado();

    /**
     * Todos los autores con sus respectivos artículos cargados utilizando una colección por mapa
     * y especificando una columna inexistente que será ignorada por MyBatis.
     */
    public List<Autor> USO_INDEBIDO_autoresConArtículosPorMapaAnidadoColumnaInexistente();
    
    /**
     * Todos los autores con sus respectivos artículos cargados utilizando una colección por mapa
     * y especificando una columna que no es clave ajena y que será ignorada por MyBatis.
     */    
    public List<Autor> USO_INDEBIDO_autoresConArtículosPorMapaAnidadoColumnaIncorrecta();
    
    /**
     * Intenta traer todos los autores con sus respectivos artículos cargados,
     * pero fallará porque el mapa no utiliza ID.
     */
    public List<Autor> USO_INCORRECTO_AutoresConArtículosMapaSinID();
    
    
    /*
     * ***************************************************************************************
     * COLECCIÓN EN CONSULTA
     * ***************************************************************************************
     */    
    
    /**
     * Bitácora con todos sus artículos cargados mediante consulta anidada.
     */
    public Bitácora bitácoraConArtículosEnConsultaAnidada(int idBitácora);
    
    /**
     * Todas las bitácoras con sus artículos usando colección con consulta anidada.
     */
    public List<Bitácora> bitácorasConArtículosEnConsultaAnidada();
    
    /**
     * Bitácora con todas las etiquetas en BBDD. 
     */
    public Bitácora USO_INCORRECTO_BitácorasConArtículosEnConsultaAnidadaConColumnInexistente(int idBitácora);
    
    /**
     * Bitácora con todas las etiquetas en BBDD. 
     */
    public Bitácora USO_EXTRAÑO_BitácorasConArtículosEnConsultaAnidadaSinParámetro(int idBitácora);
    
    

    /*
     * ***************************************************************************************
     * ASOCIACIÓN EN MAPA
     * ***************************************************************************************
     */        

    /**
     * Todas las bitácoras con sus respectivos autores utilizando una asociación por mapa.
     */    
    public List<Bitácora> bitácorasConAutorPorMapaAnidado();
    
    /**
     * Todas las bitácoras con sus respectivos autores utilizando una asociación por mapa
     * y especificando una columna inexistente que será ignorada por MyBatis.
     */    
    public List<Bitácora> USO_INDEBIDO_BitácorasConAutorPorMapaAnidadoColumnaInexistente();
    
    /**
     * Todas las bitácoras con sus respectivos autores utilizando una asociación por mapa
     * y especificando una columna que no es clave ajena y que será ignorada por MyBatis.
     */        
    public List<Bitácora> USO_INDEBIDO_BitácorasConAutorPorMapaAnidadoColumnaIncorrecta();
    
    
    /*
     * ***************************************************************************************
     * ASOCIACIÓN EN CONSULTA
     * ***************************************************************************************
     */        
    
    /**
     * Todas las bitácoras con sus respectivos autores utilizando asociación por consulta.
     */    
    public List<Bitácora> bitácorasConAutorEnConsultaAnidada();

    
    /**
     * Bitácora con su autor utilizando un mapa con asociación por consulta donde se 
     * le da a 'column' un valor que no sirve para hacer la corresonpondencia.
     */
    public Bitácora USO_INCORRECTO_BitácoraConAutorEnConsultaAnidadaConColumnIncorrectaEnAssociaton(int idBitácora);
    
}
