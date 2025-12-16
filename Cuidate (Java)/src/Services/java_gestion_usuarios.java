package services;

import models.*;
import utils.JsonUtil;
import com.google.gson.*;
import java.util.*;

/**
 * Servicio para gestionar usuarios del sistema
 */
public class GestionUsuarios {
    private String archivoD atos;
    private Map<String, Usuario> usuarios;
    
    public GestionUsuarios(String archivoD atos) {
        this.archivoD atos = archivoD atos;
        this.usuarios = new HashMap<>();
        cargarUsuarios();
    }
    
    public GestionUsuarios() {
        this("data/usuarios.json");
    }
    
    /**
     * Carga usuarios desde el archivo JSON
     */
    public void cargarUsuarios() {
        JsonObject datos = JsonUtil.leerJson(archivoD atos);
        
        if (datos.size() == 0) {
            crearUsuariosEjemplo();
            return;
        }
        
        for (String idUsuario : datos.keySet()) {
            JsonObject userJson = datos.getAsJsonObject(idUsuario);
            String tipo = userJson.get("tipo").getAsString();
            
            if ("paciente".equals(tipo)) {
                Paciente paciente = jsonToPaciente(userJson);
                usuarios.put(idUsuario, paciente);
            } else if ("medico".equals(tipo)) {
                Medico medico = jsonToMedico(userJson);
                usuarios.put(idUsuario, medico);
            }
        }
    }
    
    /**
     * Convierte JSON a Paciente
     */
    private Paciente jsonToPaciente(JsonObject json) {
        Paciente p = new Paciente(
            json.get("idUsuario").getAsString(),
            json.get("nombre").getAsString(),
            json.get("apellido").getAsString(),
            json.get("cedula").getAsString(),
            json.get("correo").getAsString(),
            json.get("contrasena").getAsString(),
            json.has("edad") ? json.get("edad").getAsInt() : 0,
            json.has("genero") ? json.get("genero").getAsString() : "",
            json.has("direccion") ? json.get("direccion").getAsString() : "",
            json.has("telefono") ? json.get("telefono").getAsString() : "",
            json.has("grupoSanguineo") ? json.get("grupoSanguineo").getAsString() : ""
        );
        
        p.setFechaRegistro(json.get("fechaRegistro").getAsString());
        
        if (json.has("historialConsultas")) {
            JsonArray consultas = json.getAsJsonArray("historialConsultas");
            List<String> historial = new ArrayList<>();
            for (JsonElement e : consultas) {
                historial.add(e.getAsString());
            }
            p.setHistorialConsultas(historial);
        }
        
        return p;
    }
    
    /**
     * Convierte JSON a Medico
     */
    private Medico jsonToMedico(JsonObject json) {
        Medico m = new Medico(
            json.get("idUsuario").getAsString(),
            json.get("nombre").getAsString(),
            json.get("apellido").getAsString(),
            json.get("cedula").getAsString(),
            json.get("correo").getAsString(),
            json.get("contrasena").getAsString(),
            json.has("especialidad") ? json.get("especialidad").getAsString() : "",
            json.has("registroMedico") ? json.get("registroMedico").getAsString() : "",
            json.has("anosExperiencia") ? json.get("anosExperiencia").getAsInt() : 0
        );
        
        m.setFechaRegistro(json.get("fechaRegistro").getAsString());
        
        if (json.has("pacientesAsignados")) {
            JsonArray pacientes = json.getAsJsonArray("pacientesAsignados");
            List<String> asignados = new ArrayList<>();
            for (JsonElement e : pacientes) {
                asignados.add(e.getAsString());
            }
            m.setPacientesAsignados(asignados);
        }
        
        if (json.has("consultasAtendidas")) {
            JsonArray consultas = json.getAsJsonArray("consultasAtendidas");
            List<String> atendidas = new ArrayList<>();
            for (JsonElement e : consultas) {
                atendidas.add(e.getAsString());
            }
            m.setConsultasAtendidas(atendidas);
        }
        
        return m;
    }
    
    /**
     * Convierte Usuario a JSON
     */
    private JsonObject usuarioToJson(Usuario u) {
        JsonObject json = new JsonObject();
        json.addProperty("idUsuario", u.getIdUsuario());
        json.addProperty("nombre", u.getNombre());
        json.addProperty("apellido", u.getApellido());
        json.addProperty("cedula", u.getCedula());
        json.addProperty("correo", u.getCorreo());
        json.addProperty("contrasena", u.getContrasena());
        json.addProperty("tipo", u.getTipo());
        json.addProperty("fechaRegistro", u.getFechaRegistro());
        
        if (u instanceof Paciente) {
            Paciente p = (Paciente) u;
            json.addProperty("edad", p.getEdad());
            json.addProperty("genero", p.getGenero());
            json.addProperty("direccion", p.getDireccion());
            json.addProperty("telefono", p.getTelefono());
            json.addProperty("grupoSanguineo", p.getGrupoSanguineo());
            
            JsonArray historial = new JsonArray();
            for (String c : p.getHistorialConsultas()) {
                historial.add(c);
            }
            json.add("historialConsultas", historial);
        } else if (u instanceof Medico) {
            Medico m = (Medico) u;
            json.addProperty("especialidad", m.getEspecialidad());
            json.addProperty("registroMedico", m.getRegistroMedico());
            json.addProperty("anosExperiencia", m.getAnosExperiencia());
            
            JsonArray pacientes = new JsonArray();
            for (String p : m.getPacientesAsignados()) {
                pacientes.add(p);
            }
            json.add("pacientesAsignados", pacientes);
            
            JsonArray consultas = new JsonArray();
            for (String c : m.getConsultasAtendidas()) {
                consultas.add(c);
            }
            json.add("consultasAtendidas", consultas);
        }
        
        return json;
    }
    
    /**
     * Guarda usuarios en el archivo JSON
     */
    public void guardarUsuarios() {
        JsonObject datos = new JsonObject();
        for (Map.Entry<String, Usuario> entry : usuarios.entrySet()) {
            datos.add(entry.getKey(), usuarioToJson(entry.getValue()));
        }
        JsonUtil.guardarJson(archivoD atos, datos);
    }
    
    /**
     * Crea usuarios de ejemplo para pruebas
     */
    private void crearUsuariosEjemplo() {
        Medico medico = new Medico(
            "MED001", "Carlos", "Rodríguez", "1234567890",
            "carlos.rodriguez@cuidate.com", "medico123",
            "Medicina General", "RM-2024-001", 5
        );
        usuarios.put(medico.getIdUsuario(), medico);
        
        Paciente paciente = new Paciente(
            "PAC001", "María", "García", "0987654321",
            "maria.garcia@email.com", "paciente123",
            30, "Femenino", "Calle 123", "3001234567", "O+"
        );
        usuarios.put(paciente.getIdUsuario(), paciente);
        
        guardarUsuarios();
    }
    
    /**
     * Registra un nuevo paciente
     */
    public Paciente registrarPaciente(String nombre, String apellido, String cedula,
                                     String correo, String contrasena, int edad,
                                     String genero, String telefono) {
        if (buscarPorCedula(cedula) != null) {
            return null;
        }
        
        long countPacientes = usuarios.values().stream()
            .filter(u -> u.getTipo().equals("paciente"))
            .count();
        
        String idUsuario = String.format("PAC%03d", countPacientes + 1);
        
        Paciente paciente = new Paciente(idUsuario, nombre, apellido, cedula,
                                        correo, contrasena, edad, genero,
                                        "", telefono, "");
        usuarios.put(idUsuario, paciente);
        guardarUsuarios();
        return paciente;
    }
    
    /**
     * Registra un nuevo médico
     */
    public Medico registrarMedico(String nombre, String apellido, String cedula,
                                 String correo, String contrasena, String especialidad,
                                 String registroMedico) {
        if (buscarPorCedula(cedula) != null) {
            return null;
        }
        
        long countMedicos = usuarios.values().stream()
            .filter(u -> u.getTipo().equals("medico"))
            .count();
        
        String idUsuario = String.format("MED%03d", countMedicos + 1);
        
        Medico medico = new Medico(idUsuario, nombre, apellido, cedula,
                                  correo, contrasena, especialidad,
                                  registroMedico, 0);
        usuarios.put(idUsuario, medico);
        guardarUsuarios();
        return medico;
    }
    
    /**
     * Inicia sesión y retorna el usuario si es válido
     */
    public Usuario iniciarSesion(String cedula, String contrasena) {
        Usuario usuario = buscarPorCedula(cedula);
        if (usuario != null && usuario.verificarContrasena(contrasena)) {
            return usuario;
        }
        return null;
    }
    
    /**
     * Busca un usuario por su cédula
     */
    public Usuario buscarPorCedula(String cedula) {
        for (Usuario usuario : usuarios.values()) {
            if (usuario.getCedula().equals(cedula)) {
                return usuario;
            }
        }
        return null;
    }
    
    /**
     * Busca un usuario por su ID
     */
    public Usuario buscarPorId(String idUsuario) {
        return usuarios.get(idUsuario);
    }
    
    /**
     * Obtiene la lista de todos los médicos
     */
    public List<Medico> obtenerMedicos() {
        List<Medico> medicos = new ArrayList<>();
        for (Usuario u : usuarios.values()) {
            if (u instanceof Medico) {
                medicos.add((Medico) u);
            }
        }
        return medicos;
    }
    
    /**
     * Obtiene la lista de todos los pacientes
     */
    public List<Paciente> obtenerPacientes() {
        List<Paciente> pacientes = new ArrayList<>();
        for (Usuario u : usuarios.values()) {
            if (u instanceof Paciente) {
                pacientes.add((Paciente) u);
            }
        }
        return pacientes;
    }
    
    /**
     * Actualiza un usuario en el sistema
     */
    public void actualizarUsuario(Usuario usuario) {
        if (usuarios.containsKey(usuario.getIdUsuario())) {
            usuarios.put(usuario.getIdUsuario(), usuario);
            guardarUsuarios();
        }
    }
}
