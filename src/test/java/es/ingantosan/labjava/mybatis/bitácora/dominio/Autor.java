package es.ingantosan.labjava.mybatis.bitácora.dominio;

import java.util.List;

public class Autor {

    private int id;
    private String nombre;
    private String usuario;
    private String contraseña;
    private String correo;
    private String bio;
    private List<Bitácora> bitácoras;
    private List<Artículo> artículos;

    public Autor() {
    }

    public Autor(int id) {
        this.id = id;
    }
    
    public Autor(int id, String nombre, String usuario, String contraseña, String correo, String bio) {
        this.id = id;
        this.nombre = nombre;
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.correo = correo;
        this.bio = bio;
        
    }

    public List<Bitácora> getBitácoras() {
        return bitácoras;
    }

    public void setBitácoras(List<Bitácora> bitácoras) {
        this.bitácoras = bitácoras;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<Artículo> getArtículos() {
        return artículos;
    }

    public void setArtículos(List<Artículo> artículos) {
        this.artículos = artículos;
    }

}
