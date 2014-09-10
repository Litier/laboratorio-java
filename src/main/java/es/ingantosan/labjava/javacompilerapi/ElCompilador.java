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

package es.ingantosan.labjava.javacompilerapi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

/**
 * Fachada de Java Compiler API para compilar en memoria y en sistema de archivos.
 * 
 * Por defecto compila en memoria. Para compilar al sistema de archivos hay que 
 * establecer un directorio destino no nulo con el método 'setClassOutputLocation'.
 * 
 * Cada uno de los métodos de compilación devuelve el resultado de la compilación,
 * si ha sido exitoso o no. En caso negativo se pueden recoger los diagnósticos 
 * de la última compilación con el método 'getDiagnósticos'. Los diagnósticos serán 
 * siempre actualizados después de cada compilación, no persisten entre llamadas
 * a compilar.
 * 
 * Se pueden establecer opciones de compilación con el método 'setOpciones' antes
 * de la compilación. Si no se restablecen para cada compilación las opciones 
 * establecidas persisten entre compilaciones.
 * 
 * Mediante 'getCargadorMemoria' se obtiene el ClassLoader de las clases compiladas
 * en memoria con esta instancia. Este cargador va acumulando o recordando todas las
 * clases compiladas a lo largo de todas las compilaciones que se hayan realizado con esta 
 * instancia.
 * 
 * Para cargar clases compiladas en el sistema de archivos lo recomendable es obtener
 * un URLClassLoader con el destino especificado con 'setClassOutputLocation', y obtenerlo
 * después de cada compilación. Por ejemplo:
 * 
 *   File destino = new File("build/classes");
 *   JLCompilador comp = new JLCompilador();
 *   
 *   comp.setClassOutputLocation(destino);
 *   comp.compilar(...);
 * 
 *   ClassLoader cargador = new URLClassLoader(new URL[] {destino.toURI().toURL()})
 *   cargador.loadClass(...);
 */
public class ElCompilador {
    private JavaCompiler compilador;
    private ElJavaFileManager fileManager;
    private List<String> opciones;
    private DiagnosticCollector<JavaFileObject> diagnósticos;

    /**
     * Compilador que tomará como ClassLoader padre aquel con el que se invoca este constructor.
     */    
    public ElCompilador() {
        this(null);
    }

    /**
     * Compilador que utilizará el ClassLoader dado
     * 
     * @param cargadorPadre cargador padre para compilación en memoria; si es null se utiliza
     *   el ClassLoader desde el que se invoca este constructor.
     */        
    public ElCompilador(ClassLoader cargadorPadre) {
        //obtenemos el compilador con el que trabajará esta instancia
        compilador = ToolProvider.getSystemJavaCompiler();

        //nos basaremos en el fileManager estándar del compilador con los valores por defecto
        StandardJavaFileManager sfm = compilador.getStandardFileManager(null, null, null); 

        //creamos nuestro JavaFileManager con funcionalidad específica
        fileManager = new ElJavaFileManager(sfm, cargadorPadre);
    }

    /**
     * @return un cargador de clases que sabe cargar las clases compiladas
     */
    public ClassLoader getCargadorMemoria() {
        return fileManager.getClassLoader(null);
    }

    /**
     * Establece la ruta del sistema de archivos donde se crearán los .class. 
     * Esto indica, siempre que sea distinto a null, que la compilación NO es
     * en memoria. 
     * 
     * @param ruta
     * @throws IOException 
     */
    public void setClassOutputLocation(File ruta) throws IOException {
        fileManager.getFileManager().setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(ruta));
    }

    /**
     * @param opciones las opciones de compilación 
     */
    public void setOpciones(String... opciones) {
        this.opciones = Arrays.asList(opciones);
    }

    /**
     * Compila código existente en ficheros del del sistema de archivos.
     * 
     * @param ficherosJava los fuentes del sistema de archivos
     * @return si la compilación ha sido o no exitosa.
     */
    public boolean compilar(File... ficherosJava) {
        //construimos los JavaFileObject con el métod   o utililidad del StandardJavaFileManager subyacente
        Iterable<? extends JavaFileObject> s = fileManager.getFileManager().getJavaFileObjects(ficherosJava);
        return compilar(s);
    }

    /**
     * Compila el código fuente correspondiente al nombre de clase dado. 
     * 
     * Para la correcta compilación y posterior carga es indispensable que coincida
     * el nombre cualificado dado con el nombre de la clase del código fuente si esta
     * es public.
     * 
     * @param nombreCualificado el nombre completo de la clase
     * @param códigoFuente el código a compilar
     * @return si la compilación ha sido o no exitosa.
     */
    public boolean compilar(String nombreCualificado, CharSequence códigoFuente) {
        //creamos el correspondiente JavaFileObject para el código fuente dado
        JavaFileObject fuente = ElJavaFileManager.crearFuenteEnMemoria(nombreCualificado, códigoFuente);
        
        return compilar(Arrays.asList(fuente));
    }

    /**
     * Compila un conjunto de fuentes de una vez
     * 
     * @param clasesFuente mapa con los fuentes a compilar y sus respectivos nombres completos
     * @return si la compilación ha sido o no exitosa.
     */
    public boolean compilar(Map<String, CharSequence> clasesFuente) {
        List<JavaFileObject> fuentes = new ArrayList<JavaFileObject>(clasesFuente.size());

        for (Entry<String, CharSequence> par : clasesFuente.entrySet()) {
            //creamos el correspondiente JavaFileObject para el código fuente dado
            JavaFileObject fuente = ElJavaFileManager.crearFuenteEnMemoria(par.getKey(), par.getValue());

            fuentes.add(fuente);
        }

        return compilar(fuentes);
    }
    
    /**
     * Crea y ejecuta la tarea de compilación con todos los componente existentes.
     * 
     * @param udsComp unidades para compilar
     * @return si la compilación ha sido o no exitosa.
     */
    private boolean compilar(Iterable<? extends JavaFileObject> udsComp) {
        diagnósticos = new DiagnosticCollector<JavaFileObject>();

        CompilationTask tarea = compilador.getTask(null, fileManager, diagnósticos, opciones, null, udsComp);

        return tarea.call();
    }

    /**
     * @return los diagnósticos de la última compilación realizada.
     */
    public DiagnosticCollector<JavaFileObject> getDiagnósticos() {
        return diagnósticos;
    }

//    /**
//     * Liberar recursos y dejar en un estado de recién creado.
//     */
//    public void reiniciar() {
//        ClassLoader cargadorPadre = fileManager.getClassLoader(null).getParent();
//        StandardJavaFileManager sfm = compilador.getStandardFileManager(null, null, null); 
//
//        //liberamos recursos y cerramos filemanager
//        try {
//            fileManager.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }    
//
//        //creamos un nuevo fileManager limpio
//        fileManager = new JLJavaFileManager(sfm, cargadorPadre);
//    }
    
    @Override
    public void finalize() {
        //liberamos recursos y cerramos filemanager
        try {
            fileManager.close();
        } catch (IOException e) {
        }    
    }
}
