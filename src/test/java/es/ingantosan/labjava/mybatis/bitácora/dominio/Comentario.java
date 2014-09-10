package es.ingantosan.labjava.mybatis.bitácora.dominio;

import java.util.Date;

public class Comentario {
    
    private int id;
//    private int art_id;
//    private int aut_id;
    private Artículo artículo;
    private String contenido = "";
    private Date publicado = new Date();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Artículo getArtículo() {
        return artículo;
    }

    public void setArtículo(Artículo artículo) {
        this.artículo = artículo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Date getPublicado() {
        return publicado;
    }

    public void setPublicado(Date publicado) {
        this.publicado = publicado;
    }
}
