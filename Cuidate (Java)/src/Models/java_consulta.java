package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase para manejar consultas médicas
 */
public class Consulta {
    private String idConsulta;
    private String idPaciente;
    private String idMedico;
    private String motivo;
    private String estado;
    private String fechaSolicitud;
    private String fechaAtencion;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;
    
    private static final DateTimeFormatter formatter = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public Consulta(String idConsulta, String idPaciente, String idMedico,
                   String motivo, String estado) {
        this.idConsulta = idConsulta;
        this.idPaciente = idPaciente;
        this.idMedico = idMedico;
        this.motivo = motivo;
        this.estado = estado != null ? estado : "pendiente";
        this.fechaSolicitud = LocalDateTime.now().format(formatter);
        this.fechaAtencion = null;
        this.diagnostico = "";
        this.tratamiento = "";
        this.observaciones = "";
    }
    
    // Getters
    public String getIdConsulta() { return idConsulta; }
    public String getIdPaciente() { return idPaciente; }
    public String getIdMedico() { return idMedico; }
    public String getMotivo() { return motivo; }
    public String getEstado() { return estado; }
    public String getFechaSolicitud() { return fechaSolicitud; }
    public String getFechaAtencion() { return fechaAtencion; }
    public String getDiagnostico() { return diagnostico; }
    public String getTratamiento() { return tratamiento; }
    public String getObservaciones() { return observaciones; }
    
    // Setters
    public void setFechaSolicitud(String fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }
    public void setFechaAtencion(String fechaAtencion) { this.fechaAtencion = fechaAtencion; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    
    /**
     * Actualiza el estado de la consulta
     */
    public void actualizarEstado(String nuevoEstado) {
        String[] estadosValidos = {"pendiente", "en_proceso", "completada", "cancelada"};
        for (String e : estadosValidos) {
            if (e.equals(nuevoEstado)) {
                this.estado = nuevoEstado;
                if (nuevoEstado.equals("en_proceso") && this.fechaAtencion == null) {
                    this.fechaAtencion = LocalDateTime.now().format(formatter);
                }
                return;
            }
        }
    }
    
    /**
     * Registra el diagnóstico de la consulta
     */
    public void registrarDiagnostico(String diagnostico, String tratamiento, 
                                    String observaciones) {
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento != null ? tratamiento : "";
        this.observaciones = observaciones != null ? observaciones : "";
        actualizarEstado("completada");
    }
    
    /**
     * Cancela la consulta
     */
    public void cancelarConsulta(String motivoCancelacion) {
        actualizarEstado("cancelada");
        if (motivoCancelacion != null && !motivoCancelacion.isEmpty()) {
            this.observaciones = "Cancelada: " + motivoCancelacion;
        }
    }
    
    /**
     * Verifica si la consulta está pendiente
     */
    public boolean esPendiente() {
        return "pendiente".equals(this.estado);
    }
    
    /**
     * Verifica si la consulta está completada
     */
    public boolean esCompletada() {
        return "completada".equals(this.estado);
    }
}
