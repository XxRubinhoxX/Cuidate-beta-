package services;

import models.RegistroSalud;
import utils.JsonUtil;
import com.google.gson.*;
import java.util.*;

/**
 * Servicio para monitorear la salud de los pacientes
 */
public class MonitoreoSalud {
    private String archivoDatos;
    private Map<String, RegistroSalud> registros;
    
    public MonitoreoSalud(String archivoDatos) {
        this.archivoDatos = archivoDatos;
        this.registros = new HashMap<>();
        cargarRegistros();
    }
    
    public MonitoreoSalud() {
        this("data/registros.json");
    }
    
    /**
     * Carga registros desde el archivo JSON
     */
    public void cargarRegistros() {
        JsonObject datos = JsonUtil.leerJson(archivoDatos);
        
        for (String idRegistro : datos.keySet()) {
            JsonObject registroJson = datos.getAsJsonObject(idRegistro);
            RegistroSalud registro = jsonToRegistro(registroJson);
            registros.put(idRegistro, registro);
        }
    }
    
    /**
     * Convierte JSON a RegistroSalud
     */
    private RegistroSalud jsonToRegistro(JsonObject json) {
        RegistroSalud r = new RegistroSalud(
            json.get("idRegistro").getAsString(),
            json.get("idPaciente").getAsString(),
            json.get("presionSistolica").getAsInt(),
            json.get("presionDiastolica").getAsInt(),
            json.get("frecuenciaCardiaca").getAsInt(),
            json.get("temperatura").getAsDouble(),
            json.get("saturacionOxigeno").getAsInt()
        );
        
        r.setFechaRegistro(json.get("fechaRegistro").getAsString());
        if (json.has("observaciones")) {
            r.setObservaciones(json.get("observaciones").getAsString());
        }
        
        return r;
    }
    
    /**
     * Convierte RegistroSalud a JSON
     */
    private JsonObject registroToJson(RegistroSalud r) {
        JsonObject json = new JsonObject();
        json.addProperty("idRegistro", r.getIdRegistro());
        json.addProperty("idPaciente", r.getIdPaciente());
        json.addProperty("presionSistolica", r.getPresionSistolica());
        json.addProperty("presionDiastolica", r.getPresionDiastolica());
        json.addProperty("frecuenciaCardiaca", r.getFrecuenciaCardiaca());
        json.addProperty("temperatura", r.getTemperatura());
        json.addProperty("saturacionOxigeno", r.getSaturacionOxigeno());
        json.addProperty("fechaRegistro", r.getFechaRegistro());
        json.addProperty("observaciones", r.getObservaciones());
        return json;
    }
    
    /**
     * Guarda registros en el archivo JSON
     */
    public void guardarRegistros() {
        JsonObject datos = new JsonObject();
        for (Map.Entry<String, RegistroSalud> entry : registros.entrySet()) {
            datos.add(entry.getKey(), registroToJson(entry.getValue()));
        }
        JsonUtil.guardarJson(archivoDatos, datos);
    }
    
    /**
     * Crea un registro de salud con valores aleatorios simulados
     */
    public RegistroSalud crearRegistroAleatorio(String idPaciente) {
        String idRegistro = String.format("REG%05d", registros.size() + 1);
        RegistroSalud registro = RegistroSalud.generarAleatorio(idRegistro, idPaciente);
        registros.put(idRegistro, registro);
        guardarRegistros();
        return registro;
    }
    
    /**
     * Crea un registro de salud con valores manuales
     */
    public RegistroSalud crearRegistroManual(String idPaciente, int presionSistolica,
                                            int presionDiastolica, int frecuenciaCardiaca,
                                            double temperatura, int saturacionOxigeno) {
        String idRegistro = String.format("REG%05d", registros.size() + 1);
        RegistroSalud registro = new RegistroSalud(idRegistro, idPaciente,
                                                   presionSistolica, presionDiastolica,
                                                   frecuenciaCardiaca, temperatura,
                                                   saturacionOxigeno);
        registros.put(idRegistro, registro);
        guardarRegistros();
        return registro;
    }
    
    /**
     * Obtiene un registro por su ID
     */
    public RegistroSalud obtenerRegistro(String idRegistro) {
        return registros.get(idRegistro);
    }
    
    /**
     * Obtiene todos los registros de un paciente
     */
    public List<RegistroSalud> obtenerRegistrosPaciente(String idPaciente) {
        List<RegistroSalud> resultado = new ArrayList<>();
        for (RegistroSalud r : registros.values()) {
            if (r.getIdPaciente().equals(idPaciente)) {
                resultado.add(r);
            }
        }
        return resultado;
    }
    
    /**
     * Obtiene el último registro de un paciente
     */
    public RegistroSalud obtenerUltimoRegistro(String idPaciente) {
        List<RegistroSalud> regs = obtenerRegistrosPaciente(idPaciente);
        if (regs.isEmpty()) {
            return null;
        }
        
        RegistroSalud ultimo = regs.get(0);
        for (RegistroSalud r : regs) {
            if (r.getFechaRegistro().compareTo(ultimo.getFechaRegistro()) > 0) {
                ultimo = r;
            }
        }
        return ultimo;
    }
    
    /**
     * Genera consejos de salud aleatorios
     */
    public List<String> generarConsej osSalud() {
        List<String> todosConsejos = Arrays.asList(
            "Bebe al menos 8 vasos de agua al día para mantenerte hidratado.",
            "Realiza al menos 30 minutos de ejercicio moderado diariamente.",
            "Duerme entre 7 y 8 horas cada noche para una recuperación óptima.",
            "Consume 5 porciones de frutas y verduras al día.",
            "Reduce el consumo de alimentos procesados y azúcares refinados.",
            "Practica técnicas de relajación como meditación o yoga.",
            "Mantén una postura correcta al sentarte y al caminar.",
            "Lava tus manos frecuentemente para prevenir enfermedades.",
            "Limita el tiempo frente a pantallas, especialmente antes de dormir.",
            "Realiza chequeos médicos preventivos regularmente.",
            "Evita el consumo excesivo de alcohol y tabaco.",
            "Mantén un peso saludable según tu edad y estatura.",
            "Cuida tu salud mental: habla con alguien si te sientes abrumado.",
            "Protege tu piel del sol usando protector solar.",
            "Mantén una rutina de higiene bucal adecuada."
        );
        
        Collections.shuffle(todosConsejos);
        return todosConsejos.subList(0, Math.min(5, todosConsejos.size()));
    }
    
    /**
     * Analiza las tendencias de salud de un paciente
     */
    public Map<String, String> analizarTendencias(String idPaciente) {
        List<RegistroSalud> regs = obtenerRegistrosPaciente(idPaciente);
        Map<String, String> analisis = new HashMap<>();
        
        if (regs.size() < 2) {
            analisis.put("mensaje", "No hay suficientes registros para analizar tendencias");
            return analisis;
        }
        
        // Ordenar por fecha (del más reciente al más antiguo)
        regs.sort((r1, r2) -> r2.getFechaRegistro().compareTo(r1.getFechaRegistro()));
        
        RegistroSalud ultimo = regs.get(0);
        RegistroSalud anterior = regs.get(1);
        
        // Analizar presión arterial
        if (ultimo.getPresionSistolica() > anterior.getPresionSistolica()) {
            analisis.put("presion", "Aumentó");
        } else if (ultimo.getPresionSistolica() < anterior.getPresionSistolica()) {
            analisis.put("presion", "Disminuyó");
        } else {
            analisis.put("presion", "Se mantiene estable");
        }
        
        // Analizar frecuencia cardíaca
        if (ultimo.getFrecuenciaCardiaca() > anterior.getFrecuenciaCardiaca()) {
            analisis.put("frecuencia", "Aumentó");
        } else if (ultimo.getFrecuenciaCardiaca() < anterior.getFrecuenciaCardiaca()) {
            analisis.put("frecuencia", "Disminuyó");
        } else {
            analisis.put("frecuencia", "Se mantiene estable");
        }
        
        // Analizar temperatura
        if (ultimo.getTemperatura() > anterior.getTemperatura()) {
            analisis.put("temperatura", "Aumentó");
        } else if (ultimo.getTemperatura() < anterior.getTemperatura()) {
            analisis.put("temperatura", "Disminuyó");
        } else {
            analisis.put("temperatura", "Se mantiene estable");
        }
        
        return analisis;
    }
}
