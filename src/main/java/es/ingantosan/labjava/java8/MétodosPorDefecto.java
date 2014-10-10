package es.ingantosan.labjava.java8;

/**
 * ESTE CÓDIGO AUN ESTÁ EN DESARROLLO.
 * 
 * @author Antonio Sánchez
 */
public class MétodosPorDefecto {
    interface A {
        void metodoA();
        default void conflicto() {
            System.out.println("A.conflicto()");
        }
    }
    
    interface B {
        void metodoB();
        default void conflicto() {
            System.out.println("B.conflicto()");
        }
    }
    
    public static class C implements A, B {

        @Override
        public void metodoA() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void metodoB() {
            throw new UnsupportedOperationException();
        }
        
//        @Override
//        public void conflicto() {
//            throw new UnsupportedOperationException(); 
//        }

        @Override
        public void conflicto() {
            System.out.println("C.conflicto()");
            A.super.conflicto();
        }
        
        
    }
    
    public static void main(String... args) {
        C c = new C();
        
        c.conflicto();
    
    }
    
}
