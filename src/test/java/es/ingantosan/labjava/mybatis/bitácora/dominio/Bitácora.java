package es.ingantosan.labjava.mybatis.bitácora.dominio;

import java.util.List;

public class Bitácora {

    protected int id;
    protected String título;
    private Autor autor;
    private List<Artículo> artículos;
    private List<Etiqueta> todasEtiquetas;

    public Bitácora() {
    }

    public Bitácora(int id, String título, Autor autor) {
        this();
        this.id = id;
        this.título = título;
        this.autor = autor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getTítulo() {
        return título;
    }

    public void setTítulo(String título) {
        this.título = título;
    }

    public List<Artículo> getArtículos() {
        return artículos;
    }

    public void setArtículos(List<Artículo> artículos) {
        this.artículos = artículos;
    }

    public List<Etiqueta> getTodasEtiquetas() {
        return todasEtiquetas;
    }

    public void setTodasEtiquetas(List<Etiqueta> todasEtiquetas) {
        this.todasEtiquetas = todasEtiquetas;
    }
}
