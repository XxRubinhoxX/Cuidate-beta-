package services;

import models.Consulta;
import utils.JsonUtil;
import com.google.gson.*;
import java.util.*;

/**
 * Servicio para gestionar consultas médicas
 */
public class GestionConsultas {
    private String archivoDatos;
    private Map<String, Consulta> consultas;
    
    public GestionConsultas(String archivoDatos) {
        this.archivoDatos = archivoDatos;
        this.consultas = new HashMap<>();
        cargarConsultas();
    }
    
    public GestionConsultas() {
        this("data/consultas.json");
    }
    
    /**
     * Carga consultas desde el archivo JSON
     */
    public void cargarConsultas() {
        JsonObject datos = JsonUtil.leerJson(archivoDatos);
        
        for (String idConsulta : datos.keySet()) {
            JsonObject consultaJson = datos.getAsJsonObject(idConsulta);
            Consulta consulta = jsonToConsulta(consultaJson);
            consultas.put(idConsulta, consulta);
        }
    }
    
    /**
     * Convierte JSON a Consulta
     */
    private Consulta jsonToConsulta(JsonObject json) {
        Consulta c = new Consulta(
            json.get("idConsulta").getAsString(),
            json.get("idPaciente").getAsString(),
            json.get("idMedico").getAsString(),
            json.get("motivo").getAsString(),
            json.has("estado") ? json.get("estado").getAsString() : "pendiente"
        );
        
        c.setFechaSolicitud(json.get("fechaSolicitud").getAsString());
        if (json.has("fechaAtencion") && !json.get("fechaAtencion").isJsonNull()) {
            c.setFechaAtencion(json.get("fechaAtencion").getAsString());
        }
        if (json.has("diagnostico")) {
            c.setDiagnostico(json.get("diagnostico").getAsString());
        }
        if (json.has("tratamiento")) {
            c.setTratamiento(json.get("tratamiento").getAsString());
        }
        if (json.has("observaciones")) {
            c.setObservaciones(json.get("observaciones").getAsString());
        }
        
        return c;
    }
    
    /**
     * Convierte Consulta a JSON
     */
    private JsonObject consultaToJson(Consulta c) {
        JsonObject json = new JsonObject();
        json.addProperty("idConsulta", c.getIdConsulta());
        json.addProperty("idPaciente", c.getIdPaciente());
        json.addProperty("idMedico", c.getIdMedico());
        json.addProperty("motivo", c.getMotivo());
        json.addProperty("estado", c.getEstado());
        json.addProperty("fechaSolicitud", c.getFechaSolicitud());
        json.addProperty("fechaAtencion", c.getFechaAtencion());
        json.addProperty("diagnostico", c.getDiagnostico());
        json.addProperty("tratamiento", c.getTratamiento());
        json.addProperty("observaciones", c.getObservaciones());
        return json;
    }
    
    /**
     * Guarda consultas en el archivo JSON
     */
    public void guardarConsultas() {
        JsonObject datos = new JsonObject();
        for (Map.Entry<String, Consulta> entry : consultas.entrySet()) {
            datos.add(entry.getKey(), consultaToJson(entry.getValue()));
        }
        JsonUtil.guardarJson(archivoDatos, datos);
    }
    
    /**
     * Crea una nueva consulta
     */
    public Consulta crearConsulta(String idPaciente, String idMedico, String motivo) {
        String idConsulta = String.format("CON%04d", consultas.size() + 1);
        Consulta consulta = new Consulta(idConsulta, idPaciente, idMedico, 
                                        motivo, "pendiente");
        consultas.put(idConsulta, consulta);
        guardarConsultas();
        return consulta;
    }
    
    /**
     * Obtiene una consulta por su ID
     */
    public Consulta obtenerConsulta(String idConsulta) {
        return consultas.get(idConsulta);
    }
    
    /**
     * Obtiene todas las consultas de un paciente
     */
    public List<Consulta> obtenerConsultasPaciente(String idPaciente) {
        List<Consulta> resultado = new ArrayList<>();
        for (Consulta c : consultas.values()) {
            if (c.getIdPaciente().equals(idPaciente)) {
                resultado.add(c);
            }
        }
        return resultado;
    }
    
    /**
     * Obtiene todas las consultas de un médico
     */
    public List<Consulta> obtenerConsultasMedico(String idMedico) {
        List<Consulta> resultado = new ArrayList<>();
        for (Consulta c : consultas.values()) {
            if (c.getIdMedico().equals(idMedico)) {
                resultado.add(c);
            }
        }
        return resultado;
    }
    
    /**
     * Obtiene consultas pendientes de un médico
     */
    public List<Consulta> obtenerConsultasPendientesMedico(String idMedico) {
        List<Consulta> resultado = new ArrayList<>();
        for (Consulta c : consultas.values()) {
            if (c.getIdMedico().equals(idMedico) && c.esPendiente()) {
                resultado.add(c);
            }
        }
        return resultado;
    }
    
    /**
     * Obtiene consultas completadas de un médico
     */
    public List<Consulta> obtenerConsultasCompletadasMedico(String idMedico) {
        List<Consulta> resultado = new ArrayList<>();
        for (Consulta c : consultas.values()) {
            if (c.getIdMedico().equals(idMedico) && c.esCompletada()) {
                resultado.add(c);
            }
        }
        return resultado;
    }
    
    /**
     * Actualiza una consulta en el sistema
     */
    public void actualizarConsulta(Consulta consulta) {
        if (consultas.containsKey(consulta.getIdConsulta())) {
            consultas.put(consulta.getIdConsulta(), consulta);
            guardarConsultas();
        }
    }
    
    /**
     * Registra el diagnóstico de una consulta
     */
    public boolean registrarDiagnostico(String idConsulta, String diagnostico,
                                       String tratamiento, String observaciones) {
        Consulta consulta = obtenerConsulta(idConsulta);
        if (consulta != null) {
            consulta.registrarDiagnostico(diagnostico, tratamiento, observaciones);
            actualizarConsulta(consulta);
            return true;
        }
        return false;
    }
    
    /**
     * Cancela una consulta
     */
    public boolean cancelarConsulta(String idConsulta, String motivo) {
        Consulta consulta = obtenerConsulta(idConsulta);
        if (consulta != null) {
            consulta.cancelarConsulta(motivo);
            actualizarConsulta(consulta);
            return true;
        }
        return false;
    }
    
    /**
     * Obtiene estadísticas de consultas de un médico
     */
    public Map<String, Integer> obtenerEstadisticasMedico(String idMedico) {
        List<Consulta> consultas = obtenerConsultasMedico(idMedico);
        Map<String, Integer> stats = new HashMap<>();
        
        stats.put("total", consultas.size());
        stats.put("pendientes", 0);
        stats.put("en_proceso", 0);
        stats.put("completadas", 0);
        stats.put("canceladas", 0);
        
        for (Consulta c : consultas) {
            String estado = c.getEstado();
            stats.put(estado, stats.getOrDefault(estado, 0) + 1);
        }
        
        return stats;
    }
}
