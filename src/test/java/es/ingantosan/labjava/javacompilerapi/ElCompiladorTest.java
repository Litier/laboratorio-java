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

import es.ingantosan.labjava.javacompilerapi.ElCompilador;
import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.net.URL;
import java.io.File;
import java.io.FileWriter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Probaremos:
 *  1. Compilación estándar: desde y hacia el sistema de archivos.
 *  2. Compilación dinámica: código fuente en memoria y bytechodes hacia el sistema de archivos.
 *  3. Compilación en memoria: código fuente en memoria y bytecodes en memoria
 * 
 * En cada caso se probará también la ejecución dinámica.
 * 
 * También comprobamos como la compilación y ejecución funciona para clases internas.
 * 
 * @author Antonio Sánchez
 */
public class ElCompiladorTest {

    static final File DIR_PRUEBAS = new File("pruebas");
    static final File DIR_FUENTE = new File(DIR_PRUEBAS, "src");
    static final String NOMBRE_PAQUETE = "paquete";
    static final File DIR_CLASES = new File(DIR_PRUEBAS, "classes");
    
    static final String NOMBRE_CLASE = "Clase";
    static final String NOMBRE_COMPLETO_CLASE = NOMBRE_PAQUETE + '.' + NOMBRE_CLASE; // "paquete.Clase"
    static final String NOMBRE_CLASE_INTERNA = "Clase$Interna";
    static final String NOMBRE_COMPLETO_CLASE_INTERNA = NOMBRE_PAQUETE + '.' + NOMBRE_CLASE_INTERNA; // "paquete.Clase.Interna"
    static final String NOMBRE_MÉTODO = "método";
    static final String NOMBRE_MÉTODO_INTERNO = "interno";
    static final File RUTA_FUENTE = new File(DIR_FUENTE, NOMBRE_PAQUETE + File.separatorChar + NOMBRE_CLASE + ".java"); // "pruebas/src/paquete/Clase.java"
    static final File RUTA_CLASE = new File(DIR_CLASES, NOMBRE_PAQUETE + File.separatorChar + NOMBRE_CLASE + ".class"); // "pruebas/classes/paquete/Clase.class"
    static final File RUTA_CLASE_INTERNA = new File(DIR_CLASES, NOMBRE_PAQUETE + File.separatorChar + NOMBRE_CLASE_INTERNA + ".class"); // "pruebas/classes/paquete/Clase$Interna.class"
    static final String RESPUESTA = "ESTA ES LA RESPUESTA DEL MÉTODO.";
    static final String RESPUESTA_INTERNA = "ESTA ES LA RESPUESTA DEL MÉTODO INTERNO.";
    static final String CÓDIGO_FUENTE =
            "package paquete; \n"
            + "\n"
            + "public class Clase {\n"
            + "    public String método() {\n"
            + "        System.out.println(\"Ejecutando Clase.método()\");  \n"
            + "        return  \"" + RESPUESTA + "\"; \n"
            + "    }\n"
            + "\n"
            + "    public static class Interna {\n"
            + "        public String interno() {    \n"
            + "            System.out.println(\"Ejecutando Clase$Interna.interno()\"); \n"
            + "            return  \"" + RESPUESTA_INTERNA + "\"; \n"
            + "        }\n"
            + "    }\n"
            + "}\n";
    
    private ElCompilador compilador;

    public ElCompiladorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        //creamos código fuente en el sistema de archivos
        RUTA_FUENTE.getParentFile().mkdirs();
        FileWriter fw = new FileWriter(RUTA_FUENTE);
        fw.write(CÓDIGO_FUENTE);
        fw.close();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        limpiarDir(DIR_PRUEBAS); //limpiamos lo creado en el sistema de archivos
    }

    static boolean limpiarDir(File dir) {
        if (dir.isDirectory()) {
            String[] hijos = dir.list();
            for (int i = 0; i < hijos.length; i++) {
                if (!limpiarDir(new File(dir, hijos[i]))) 
                    return false;
            }
        }

        return dir.delete();
    }

    @Before
    public void setUp() throws MalformedURLException {
        compilador = new ElCompilador();
        DIR_CLASES.mkdirs();
    }

    @After
    public void tearDown() {
        limpiarDir(DIR_CLASES); //nos aseguramos de que no haya ningún .class
    }

    /**
     * Del sistema de archivos al sistema de archivos
     */
    @Test
    public void testCompilarEstático() throws Exception {
        System.out.println("PROBANDO testCompilarEstático() - Del sistema de archivos al sistema de archivos");
        
        //nos aseguramos que aún NO existen los .class
        assertFalse(RUTA_CLASE.exists()); 
        assertFalse(RUTA_CLASE_INTERNA.exists()); 

        //con esto decimos que queremos bytecodes en sta. archivos
        compilador.setClassOutputLocation(DIR_CLASES); 
        //manejamos opciones de compilación, en este caso dónde debe buscar fuentes
        compilador.setOpciones("-sourcepath", DIR_FUENTE.getPath());
        boolean resultado = compilador.compilar(RUTA_FUENTE);
        
        assertTrue(resultado);
        //se han creado el .class en el lugar esperado
        assertTrue(RUTA_CLASE.exists()); 
        assertTrue(RUTA_CLASE_INTERNA.exists()); 

        //probamos la ejecución dinámica
        ClassLoader cargador = new URLClassLoader(new URL[] {DIR_CLASES.toURI().toURL()});
        testEjecutar(cargador);
        
        System.out.println("------------------ FIN DE PRUEBA testCompilarEstático()");
        System.out.println();

    }

    /**
     * De memoria al sistema de archivos
     */
    @Test
    public void testCompilarDinámico() throws Exception {
        System.out.println("PROBANDO testCompilarDinámico() - De memoria al sistema de archivos");
        
        //nos aseguramos que aún NO existen los .class
        assertFalse(RUTA_CLASE.exists()); 
        assertFalse(RUTA_CLASE_INTERNA.exists()); 
        
        //con esto decimos que queremos bytecodes en sta. archivos
        compilador.setClassOutputLocation(DIR_CLASES);
        compilador.compilar(NOMBRE_COMPLETO_CLASE, CÓDIGO_FUENTE);
        boolean resultado = compilador.compilar(RUTA_FUENTE);
        
        assertTrue(resultado);
        //se han creado el .class en el lugar esperado
        assertTrue(RUTA_CLASE.exists()); 
        assertTrue(RUTA_CLASE_INTERNA.exists()); 
        
        //probamos la ejecución dinámica
        testEjecutar(new URLClassLoader(new URL[] {DIR_CLASES.toURI().toURL()}));
        
        System.out.println("------------------ FIN DE PRUEBA testCompilarDinámico()");
        System.out.println();
    }

    /**
     * De memoria a memoria
     */    
    @Test
    public void testCompilarMemoria() throws Exception {
        System.out.println("PROBANDO testCompilarMemoria() - De memoria a memoria");
        
        boolean resultado = compilador.compilar(RUTA_FUENTE);
        
        assertTrue(resultado);
        assertFalse(RUTA_CLASE.exists()); //esta vez NO se ha creado el .class sino que queda en memoria        
        
        //probamos la ejecución dinámica
        try {
            testEjecutar(new URLClassLoader(new URL[] {DIR_CLASES.toURI().toURL()}));
            fail("No se ha producido la ClassNotFoundException esperada.");
        } catch (ClassNotFoundException ex) {
            //no hacer nada
        }
        
        //probamos la ejecución dinámica
        testEjecutar(compilador.getCargadorMemoria());
        
        System.out.println("------------------ FIN DE PRUEBA testCompilarMemoria()");
        System.out.println();
    }
    
    
    /**
     * Probar la ejecución dinámica
     */
    private void testEjecutar(ClassLoader cargador) throws Exception {
        Class clase = null;
        Object respuesta = null;
        
        //ejecución dinámica de clase principal
        clase = cargador.loadClass(NOMBRE_COMPLETO_CLASE);
        respuesta = clase.getMethod(NOMBRE_MÉTODO).invoke(clase.newInstance());
        
        assertTrue(respuesta instanceof String);
        assertTrue(RESPUESTA.equals((String) respuesta));
        
        //ejecución dinámica de clase interna
        clase = cargador.loadClass(NOMBRE_COMPLETO_CLASE_INTERNA);
        respuesta = clase.getMethod(NOMBRE_MÉTODO_INTERNO).invoke(clase.newInstance());
        
        assertTrue(respuesta instanceof String);
        assertTrue(RESPUESTA_INTERNA.equals((String) respuesta));
    }
}
