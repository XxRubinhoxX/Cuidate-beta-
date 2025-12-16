package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase para médicos del sistema
 */
public class Medico extends Usuario {
    private String especialidad;
    private String registroMedico;
    private int anosExperiencia;
    private List<String> pacientesAsignados;
    private List<String> consultasAtendidas;
    
    public Medico(String idUsuario, String nombre, String apellido,
                 String cedula, String correo, String contrasena,
                 String especialidad, String registroMedico, int anosExperiencia) {
        super(idUsuario, nombre, apellido, cedula, correo, contrasena, "medico");
        this.especialidad = especialidad;
        this.registroMedico = registroMedico;
        this.anosExperiencia = anosExperiencia;
        this.pacientesAsignados = new ArrayList<>();
        this.consultasAtendidas = new ArrayList<>();
    }
    
    // Getters
    public String getEspecialidad() { return especialidad; }
    public String getRegistroMedico() { return registroMedico; }
    public int getAnosExperiencia() { return anosExperiencia; }
    public List<String> getPacientesAsignados() { return pacientesAsignados; }
    public List<String> getConsultasAtendidas() { return consultasAtendidas; }
    
    // Setters
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public void setRegistroMedico(String registroMedico) { this.registroMedico = registroMedico; }
    public void setAnosExperiencia(int anosExperiencia) { this.anosExperiencia = anosExperiencia; }
    public void setPacientesAsignados(List<String> pacientesAsignados) { 
        this.pacientesAsignados = pacientesAsignados; 
    }
    public void setConsultasAtendidas(List<String> consultasAtendidas) { 
        this.consultasAtendidas = consultasAtendidas; 
    }
    
    /**
     * Asigna un paciente al médico
     */
    public void asignarPaciente(String idPaciente) {
        if (!pacientesAsignados.contains(idPaciente)) {
            pacientesAsignados.add(idPaciente);
        }
    }
    
    /**
     * Registra una consulta como atendida
     */
    public void registrarConsultaAtendida(String idConsulta) {
        if (!consultasAtendidas.contains(idConsulta)) {
            consultasAtendidas.add(idConsulta);
        }
    }
    
    /**
     * Actualiza datos profesionales del médico
     */
    public void actualizarDatosProfesionales(String especialidad, 
                                            String registroMedico,
                                            Integer anosExperiencia) {
        if (especialidad != null && !especialidad.isEmpty()) {
            this.especialidad = especialidad;
        }
        if (registroMedico != null && !registroMedico.isEmpty()) {
            this.registroMedico = registroMedico;
        }
        if (anosExperiencia != null) {
            this.anosExperiencia = anosExperiencia;
        }
    }
}
