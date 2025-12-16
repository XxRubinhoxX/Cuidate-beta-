package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase base para todos los usuarios del sistema
 */
public class Usuario {
    protected String idUsuario;
    protected String nombre;
    protected String apellido;
    protected String cedula;
    protected String correo;
    protected String contrasena;
    protected String tipo;
    protected String fechaRegistro;
    
    private static final DateTimeFormatter formatter = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public Usuario(String idUsuario, String nombre, String apellido, 
                   String cedula, String correo, String contrasena, String tipo) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.correo = correo;
        this.contrasena = contrasena;
        this.tipo = tipo;
        this.fechaRegistro = LocalDateTime.now().format(formatter);
    }
    
    // Getters
    public String getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getCedula() { return cedula; }
    public String getCorreo() { return correo; }
    public String getContrasena() { return contrasena; }
    public String getTipo() { return tipo; }
    public String getFechaRegistro() { return fechaRegistro; }
    
    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    
    /**
     * Actualiza los datos del usuario
     */
    public void actualizarDatos(String nombre, String apellido, 
                               String correo, String contrasena) {
        if (nombre != null && !nombre.isEmpty()) {
            this.nombre = nombre;
        }
        if (apellido != null && !apellido.isEmpty()) {
            this.apellido = apellido;
        }
        if (correo != null && !correo.isEmpty()) {
            this.correo = correo;
        }
        if (contrasena != null && !contrasena.isEmpty()) {
            this.contrasena = contrasena;
        }
    }
    
    /**
     * Verifica si la contraseña es correcta
     */
    public boolean verificarContrasena(String contrasena) {
        return this.contrasena.equals(contrasena);
    }
    
    /**
     * Retorna el nombre completo del usuario
     */
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}
