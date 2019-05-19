package com.e.recolectar.modelo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Usuario implements Serializable {

    //region Atributos
    private String idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String dni;
    private String password;
    //endregion


    //region Metodos

    //Constructor de Persona Vacio
    public Usuario() {
    }

    //Constructor de Persona
    public Usuario(String p_id, String p_dni, String p_nombre, String p_apellido, String p_email, String p_password) {
        this.setIdUsuario(p_id);
        this.setUsuario(p_dni);
        this.setNombre(p_nombre);
        this.setApellido(p_apellido);
        this.setEmail(p_email);
        this.setPassword(p_password);
    }

    @Override
    public String toString() {
        return this.getNombre() + '\'' + this.getApellido();
    }

    //endregion

    //region Setters

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsuario(String dni) {
        this.dni = dni;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //endregion

    //region Getters

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getUsuario() {
        return dni;
    }

    public String getPassword() {
        return password;
    }

    //endregion


}