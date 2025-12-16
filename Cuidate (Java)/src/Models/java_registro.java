package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Clase para registros de salud y signos vitales
 */
public class RegistroSalud {
    private String idRegistro;
    private String idPaciente;
    private int presionSistolica;
    private int presionDiastolica;
    private int frecuenciaCardiaca;
    private double temperatura;
    private int saturacionOxigeno;
    private String fechaRegistro;
    private String observaciones;
    
    private static final DateTimeFormatter formatter = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public RegistroSalud(String idRegistro, String idPaciente,
                        int presionSistolica, int presionDiastolica,
                        int frecuenciaCardiaca, double temperatura,
                        int saturacionOxigeno) {
        this.idRegistro = idRegistro;
        this.idPaciente = idPaciente;
        this.presionSistolica = presionSistolica;
        this.presionDiastolica = presionDiastolica;
        this.frecuenciaCardiaca = frecuenciaCardiaca;
        this.temperatura = temperatura;
        this.saturacionOxigeno = saturacionOxigeno;
        this.fechaRegistro = LocalDateTime.now().format(formatter);
        this.observaciones = "";
    }
    
    // Getters
    public String getIdRegistro() { return idRegistro; }
    public String getIdPaciente() { return idPaciente; }
    public int getPresionSistolica() { return presionSistolica; }
    public int getPresionDiastolica() { return presionDiastolica; }
    public int getFrecuenciaCardiaca() { return frecuenciaCardiaca; }
    public double getTemperatura() { return temperatura; }
    public int getSaturacionOxigeno() { return saturacionOxigeno; }
    public String getFechaRegistro() { return fechaRegistro; }
    public String getObservaciones() { return observaciones; }
    
    // Setters
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    
    /**
     * Genera un registro de salud con valores aleatorios simulados
     */
    public static RegistroSalud generarAleatorio(String idRegistro, String idPaciente) {
        Random random = new Random();
        int presionSistolica = 110 + random.nextInt(31);
        int presionDiastolica = 70 + random.nextInt(21);
        int frecuenciaCardiaca = 60 + random.nextInt(41);
        double temperatura = 36.0 + (random.nextDouble() * 1.5);
        temperatura = Math.round(temperatura * 10.0) / 10.0;
        int saturacionOxigeno = 95 + random.nextInt(6);
        
        return new RegistroSalud(idRegistro, idPaciente, presionSistolica,
                                presionDiastolica, frecuenciaCardiaca,
                                temperatura, saturacionOxigeno);
    }
    
    /**
     * Evalúa el estado general basado en los signos vitales
     */
    public String evaluarEstado() {
        List<String> alertas = new ArrayList<>();
        
        if (presionSistolica > 140 || presionDiastolica > 90) {
            alertas.add("Presión arterial elevada");
        } else if (presionSistolica < 90 || presionDiastolica < 60) {
            alertas.add("Presión arterial baja");
        }
        
        if (frecuenciaCardiaca > 100) {
            alertas.add("Frecuencia cardíaca elevada");
        } else if (frecuenciaCardiaca < 60) {
            alertas.add("Frecuencia cardíaca baja");
        }
        
        if (temperatura > 37.5) {
            alertas.add("Temperatura elevada (fiebre)");
        } else if (temperatura < 36.0) {
            alertas.add("Temperatura baja (hipotermia)");
        }
        
        if (saturacionOxigeno < 95) {
            alertas.add("Saturación de oxígeno baja");
        }
        
        if (alertas.isEmpty()) {
            return "Normal - Todos los signos vitales en rango saludable";
        } else {
            return "Alerta: " + String.join(", ", alertas);
        }
    }
    
    /**
     * Agrega una observación al registro
     */
    public void agregarObservacion(String observacion) {
        this.observaciones = observacion;
    }
}
