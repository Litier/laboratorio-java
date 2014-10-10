package es.ingantosan.labjava.java8;

import es.ingantosan.labjava.Persona;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Ejemplos de uso de expresiones lambdas. Enfoque progresivo para ilutrar la
 * ventaja sintáctica, empezando por la contraposición con clases anónimas
 * convencionales, siguiendo por el ahorro declarativo de interfaces que ofrecen 
 * las interfaces funcionales en el paquete java.util.function, y terminando 
 * por la ejemplificación del potencial expresion haciendo uso de agregados y 
 * reducciones en colecciones. 
 * 
 * @author antonio
 */
public class Lambdas {
    
    /**
     * Llama imprimir() sobre las Personas que de un lista cumplen lo 
     * especificado por la implementacion de Criterio que se da.
     */
    public static void imprimirPorCriterio(List<Persona> listado, 
                                        Criterio<Persona> criterio) {
        for (Persona p : listado) {
            if (criterio.probar(p))
                p.imprimir();
        }       
    }
    
    /**
     * Llama imprimir() sobre las Personas que de un lista cumplen lo 
     * especificado por el Predicate que se da. Predicate hace innecesaria
     * la definicion y existencia de Criterio.
     */
    public static void imprimirConPredicate(List<Persona> listado, 
                                            Predicate<Persona> criterio) {
        for (Persona p : listado) {
            if (criterio.test(p))
                p.imprimir();
        }
    }
    
    /**
     * Aplica a Persona la funcionalidad expresada por Consumer sobre la lista 
     * de Personas que cumplen lo espeficiado por Predicate. Haciendo uso de 
     * las interfaces de java.util.function evitamos la declaracion de 
     * interfaces y clases ad-hoc innecesarias, y ademas podemos programar de
     * forma generica, por ejemplo, evitando declarar un metodo por cada 
     * posible convinacion de predicado y accion operativa.
     */
    public static void procesarPersonas(List<Persona> listado, 
                                        Predicate<Persona> predicado, 
                                        Consumer<Persona> proceso) {
        for (Persona p : listado) {
            if (predicado.test(p))
                proceso.accept(p);
        }
    }
    
    /**
     * Versión sobrecargada de procesarPersonas() que aplica la acción operativa
     * a un dato obtenido de Persona mediante una Function.
     */
    public static void procesarPersonas(List<Persona> listado, 
                                        Predicate<Persona> predicado, 
                                        Function<Persona, String> función, 
                                        Consumer<String> proceso) {
        for (Persona p : listado) {
            if (predicado.test(p)) {
                String res = función.apply(p);
                proceso.accept(res);
            }
                
        }
    }
    
    /**
     * Método genérido para procesar un conjunto de elementos, filtrando según
     * un predicado, y aplicando una acción determinada sobre un valor extraido 
     * u obtenido a partir de los elementos filtrados. 
     * 
     * 
     * @param <X> tipo de los elementos a iterar
     * @param <Y> tipo obtenido de aplicar a los X seleccionados una función
     * @param elementos conjunto de X a iterar
     * @param predicado criterio para filtrar los X
     * @param función obtiene un Y a partir de un X filtrado
     * @param proceso opera sobre un Y obtenido a partir del X filtrado. 
     */
    public static <X, Y> void procesar(Iterable<X> elementos,
                                        Predicate<X> predicado, 
                                        Function<X, Y> función, 
                                        Consumer<Y> proceso) {
        for (X p : elementos) {
            if (predicado.test(p)) {
                Y res = función.apply(p);
                proceso.accept(res);
            }
                
        }
    }
    
    /**
     * Utilidad para transferir los elementos de una coleccion de cualquier tipo
     * a otra coleccion generada del modo especificado
     * 
     * @param <T> tipo de los elementos coleccion
     * @param <U> tipo de la coleccion origen
     * @param <V> tipo de la coleccion generada
     * @param colFuente coleccion de Ts origen
     * @param factoriaColDestino factoria para producir el V devuelto
     * @return un V con los mismos elementos que hay en colFuente
     */
    public static <T, U extends Collection<T>, V extends Collection<T>>
            V transferir(U colFuente, Supplier<V> factoriaColDestino) {

        V resultado = factoriaColDestino.get();
        for (T t : colFuente) {
            resultado.add(t);
        }
        return resultado;
    }    
    
    public static void main(String... args) {
        List<Persona> listado = Persona.crearListado();
        
        //imprimir personas mayores de edad, instanciando una clase definida 
        //que implementa la interface Criterio
        Lambdas.imprimirPorCriterio(listado, new CriterioMayorEdad());

        //imprimir personas mayores de edad, instanciando una clase anónima
        //que implementa la interface Criterio
        Lambdas.imprimirPorCriterio(listado, new Criterio<Persona>() {
            @Override
            public boolean probar(Persona p) {
                return p.getEdad() >= 18;
                        
            }
        });
        
        //imprimir mayores de edad, instanciando la interface Criterio 
        //mediante expresión lambda
        Lambdas.imprimirPorCriterio(listado, (Persona p) -> p.getEdad() >= 18);
        
        //imprimir los jubilados haciendo uso mediante lambda de las 
        //interfaces del paquete java.util.function, en este caso, Predicate, 
        //que hace innecesaria definir la interface Criterio
        Lambdas.imprimirConPredicate(listado, p -> p.getEdad() >= 65);
        
        //imprimir jubilados mediante código genérico que hace uso de las 
        //interfaces Predicate y Cosumer en java.util.function, seleccionando
        //primero los mayores de 65 (Predicate) e imprimiendo (Consumer) los 
        //seleccionados
        Lambdas.procesarPersonas(listado, 
                                p -> p.getEdad() >= 65, 
                                p -> p.imprimir());
        
        //imprimir los correos de los jubilados con lambdas de interfaces de
        //java.util.function, Predicate selecciona juubilados, Function obtiene
        //el correo-e de cada uno, y Consumer los imprime
        Lambdas.procesarPersonas(listado, 
                p -> p.getEdad() >= 65, 
                p -> p.getCorreoE(), 
                c -> System.out.println(c));
        
        Lambdas.procesar(listado, 
                p -> p.getEdad() >= 65, 
                p -> p.getCorreoE(), 
                c -> System.out.println(c));
        
        
        //imprimir los correos de los jubilados sin definir método alguno, 
        //haciendo uso de lambdas con el stream obtenido de la colección, el 
        //cual filtramos para obtener los jubilados, luego lo mapeamos para 
        //obtener los correos-e y finalmente los procesamos para imprimir los 
        //correos
        listado
            .stream()
            .filter(p -> p.getEdad() >= 65)
            .map(p -> p.getCorreoE())
            .forEach(c -> System.out.println(c));

        //imprimir los correos de los jubilados sin definir método alguno, 
        //haciendo uso de lambdas de referencias a métodos sobre el stream 
        //obtenido de la colección, el cual filtramos para obtener los 
        //jubilados, luego lo mapeamos para obtener los correos-e y finalmente 
        //los procesamos para imprimir los correos
        listado
            .stream()
            .filter(Persona::isJubilado)
            .map(p -> p.getCorreoE())
            .forEach(System.out::println);
        
        //notacion de referencia a metodos estaticos
        Persona[] personas = listado.toArray(new Persona[listado.size()]);
        Arrays.sort(personas, Persona::compararPorEdad);
        
        Arrays.sort(personas, new Comparator<Persona>() {
            @Override
            public int compare(Persona p1, Persona p2) {
                return Persona.compararPorEdad(p1, p2);
            }
        });
        
        //notacion de referencia a constructor
        Set<Persona> conjunto = Lambdas.transferir(listado, 
                                        () -> new HashSet<Persona>());
        conjunto = Lambdas.transferir(listado, HashSet::new);
        
    }
    
}

/**
 * Criterio genérico para discriminar elemenos de una colección. Interfaces de 
 * este tipo son innecesarias al poder ser sustituidas por las interfaces del
 * paquete java.util.funcion. En este caso, Predicate sustituría a Criterio.
 */
interface Criterio<T> {
    boolean probar(T t);
}

/**
 * Implementación de Criterio que selecciona Personas mayores de edad.
 */
class CriterioMayorEdad implements Criterio<Persona> {

    @Override
    public boolean probar(Persona p) {
        return p.getEdad() >= 18;
    }
}

