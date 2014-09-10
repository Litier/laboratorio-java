package es.ingantosan.labjava.mybatis.bitácora.pruebas;

import java.io.IOException;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public abstract class PruebaBase {
    public final static int ID_AUTOR_FELIPE = 1;
    public final static String NOMBRE_AUTOR_FELIPE = "Felipe";
    public final static int ID_BITÁCORA_FELIPE = 1;
    public final static String TÍTULO_BITÁCORA_FELIPE = "Bitácora de Felipe";
    public final static int TOTAL_ARTÍCULOS_FELIPE = 2;
    
    public final static int ID_BITÁCORA_ESPERANZA = 3;
    public final static String TÍTULO_BITÁCORA_ESPERANZA = "La esperanza";
    public final static int TOTAL_ARTÍCULOS_BITÁCORA_ESPERANZA = 3;
    
    public final static int ID_AUTOR_ANTONIO = 2;
    public final static String NOMBRE_AUTOR_ANTONIO = "Antonio";
    public final static int TOTAL_BITÁCORAS_ANTONIO = 3;
    public final static int TOTAL_ARTÍCULOS_ANTONIO = 3;
    
    public final static int TOTAL_AUTORES = 6;
    public final static int TOTAL_BITÁCORAS = 7;
    public final static int TOTAL_ETIQUETAS = 6;
    public final static int TOTAL_CATEGORÍAS = 25;
    
    public final static int TOTAL_ARTÍCULOS_TODAS_BITÁCORAS = 7;
    
    public final static String CLAVE_MUNDO = "mundo";
    public final static int OCURRENCIAS_CLAVE_MUNDO_ARTÍCULOS_ANTONIO = 2;
    
    public final static int INICIO_IDENTIDAD = 1000;
    
    
    SqlSessionFactory factoría;

    public PruebaBase() {
        try {
            factoría = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("conf/bitácora-mybatis.xml"));
        } catch (IOException ex) {
            System.exit(1);
        }
    }

}
