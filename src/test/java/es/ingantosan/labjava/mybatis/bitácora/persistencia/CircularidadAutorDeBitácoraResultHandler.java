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
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

public class CircularidadAutorDeBitácoraResultHandler implements ResultHandler {
    Autor a;

    public void handleResult(ResultContext context) {
         a = (Autor) context.getResultObject();
         
         if (a != null && a.getBitácoras() != null)
             for (Bitácora b : a.getBitácoras())
                 b.setAutor(a);
    }
    
    public Autor getAutor() {
        return a;
    }
    
}
