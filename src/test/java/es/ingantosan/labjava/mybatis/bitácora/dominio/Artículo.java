package es.ingantosan.labjava.mybatis.bitácora.dominio;

import java.util.Date;

public class Artículo {

    private int id;
//      private int bit_id;
    private Bitácora bitácora;
    private Autor autor;
    private String asunto;
    private String cuerpo;
    private Date publicado;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitácora getBitácora() {
        return bitácora;
    }

    public void setBitácora(Bitácora bitácora) {
        this.bitácora = bitácora;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public Date getPublicado() {
        return publicado;
    }

    public void setPublicado(Date publicado) {
        this.publicado = publicado;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }
}
