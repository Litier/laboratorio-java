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

import es.ingantosan.labjava.mybatis.bitácora.dominio.Bitácora;

/**
 * Pruebas de la configuraciòn autoMappingBehavior.
 * 
 * @author Antonio Sánchez
 */
public interface Automapeo {
	
        /**
         * Bitácora con autor asociado por mapa
         */
	public Bitácora bitácoraConAutorAsociado(int id);

        /**
         * Bitácora sin asociación en el mapa
         */        
        public Bitácora bitácoraSinAutorAsociado(int id);

        /**
         * Bitácora con autor con notación ognl en los result
         */        
        public Bitácora getBitácoraConAutorPorOgnlEnMapa(int id);
        
        /**
         * Bitácora con autor con notación ognl en el sql y asignado directamente a resulttype
         */
        public Bitácora getBitácoraConAutorPorOgnlEnSQL(int id);
        
        /**
         * Bitácora sin autor asignado diréctamente a resultType
         */
        public Bitácora bitácoraSinMapeo(int id);
        
        /**
         * Bitácora con artículos asociados en mapa anidado
         */
        public Bitácora bitácoraConArtículosEnMapaAnidado(int id);
        
        /**
         * Bitácora con artículos asociados en consulta anidada
         */
        public Bitácora bitácoraConArtículosEnConsultaAnidada(int id);
        
        
        /**
         * Biácora con autor asociado por consulta anidada
         */
        public Bitácora bitácoraConAutorEnConsultaAnidada(int id);
        

}
