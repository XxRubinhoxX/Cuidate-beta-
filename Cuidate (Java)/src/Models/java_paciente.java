package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase para pacientes del sistema
 */
public class Paciente extends Usuario {
    private int edad;
    private String genero;
    private String direccion;
    private String telefono;
    private String grupoSanguineo;
    private List<String> historialConsultas;
    
    public Paciente(String idUsuario, String nombre, String apellido,
                   String cedula, String correo, String contrasena,
                   int edad, String genero, String direccion,
                   String telefono, String grupoSanguineo) {
        super(idUsuario, nombre, apellido, cedula, correo, contrasena, "paciente");
        this.edad = edad;
        this.genero = genero;
        this.direccion = direccion;
        this.telefono = telefono;
        this.grupoSanguineo = grupoSanguineo;
        this.historialConsultas = new ArrayList<>();
    }
    
    // Getters
    public int getEdad() { return edad; }
    public String getGenero() { return genero; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getGrupoSanguineo() { return grupoSanguineo; }
    public List<String> getHistorialConsultas() { return historialConsultas; }
    
    // Setters
    public void setEdad(int edad) { this.edad = edad; }
    public void setGenero(String genero) { this.genero = genero; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setGrupoSanguineo(String grupoSanguineo) { this.grupoSanguineo = grupoSanguineo; }
    public void setHistorialConsultas(List<String> historialConsultas) { 
        this.historialConsultas = historialConsultas; 
    }
    
    /**
     * Agrega una consulta al historial
     */
    public void agregarConsulta(String idConsulta) {
        if (!historialConsultas.contains(idConsulta)) {
            historialConsultas.add(idConsulta);
        }
    }
    
    /**
     * Actualiza datos médicos del paciente
     */
    public void actualizarDatosMedicos(Integer edad, String genero, 
                                      String direccion, String telefono,
                                      String grupoSanguineo) {
        if (edad != null) {
            this.edad = edad;
        }
        if (genero != null && !genero.isEmpty()) {
            this.genero = genero;
        }
        if (direccion != null && !direccion.isEmpty()) {
            this.direccion = direccion;
        }
        if (telefono != null && !telefono.isEmpty()) {
            this.telefono = telefono;
        }
        if (grupoSanguineo != null && !grupoSanguineo.isEmpty()) {
            this.grupoSanguineo = grupoSanguineo;
        }
    }
}
