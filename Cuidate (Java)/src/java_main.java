import models.*;
import services.*;
import java.util.*;

/**
 * CUIDATE - Sistema de Asistencia Básica en Salud
 * Versión Beta - Terminal (Java)
 */
public class Main {
    private GestionUsuarios gestionUsuarios;
    private GestionConsultas gestionConsultas;
    private MonitoreoSalud monitoreoSalud;
    private Usuario usuarioActual;
    private Scanner scanner;
    
    public Main() {
        this.gestionUsuarios = new GestionUsuarios();
        this.gestionConsultas = new GestionConsultas();
        this.monitoreoSalud = new MonitoreoSalud();
        this.usuarioActual = null;
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Limpia la pantalla de la terminal
     */
    private void limpiarPantalla() {
        try {
            String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Si falla, imprimir líneas en blanco
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
    
    /**
     * Pausa la ejecución hasta que el usuario presione Enter
     */
    private void pausar() {
        System.out.println("\nPresiona Enter para continuar...");
        scanner.nextLine();
    }
    
    /**
     * Muestra un encabezado decorado
     */
    private void mostrarEncabezado(String titulo) {
        limpiarPantalla();
        System.out.println("============================================================");
        System.out.println("  " + centrarTexto(titulo, 56));
        System.out.println("============================================================");
        System.out.println();
    }
    
    /**
     * Centra un texto en un ancho específico
     */
    private String centrarTexto(String texto, int ancho) {
        int espacios = (ancho - texto.length()) / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < espacios; i++) {
            sb.append(" ");
        }
        sb.append(texto);
        return sb.toString();
    }
    
    /**
     * Valida que un nombre o apellido no contenga números
     */
    private boolean validarNombre(String texto, String campo) {
        if (texto == null || texto.isEmpty()) {
            System.out.println("\n" + campo + " no puede estar vacío.");
            return false;
        }
        
        for (char c : texto.toCharArray()) {
            if (Character.isDigit(c)) {
                System.out.println("\n" + campo + " no puede contener números.");
                return false;
            }
        }
        
        if (texto.length() < 2) {
            System.out.println("\n" + campo + " debe tener al menos 2 caracteres.");
            return false;
        }
        
        return true;
    }
    
    /**
     * Valida que la cédula contenga solo números
     */
    private boolean validarCedula(String cedula) {
        if (cedula == null || cedula.isEmpty()) {
            System.out.println("\nLa cédula no puede estar vacía.");
            return false;
        }
        
        if (!cedula.matches("\\d+")) {
            System.out.println("\nLa cédula debe contener solo números.");
            return false;
        }
        
        if (cedula.length() < 6) {
            System.out.println("\nLa cédula debe tener al menos 6 dígitos.");
            return false;
        }
        
        return true;
    }
    
    /**
     * Valida que la especialidad no contenga solo números
     */
    private boolean validarEspecialidad(String especialidad) {
        if (especialidad == null || especialidad.isEmpty()) {
            System.out.println("\nLa especialidad no puede estar vacía.");
            return false;
        }
        
        if (especialidad.matches("\\d+")) {
            System.out.println("\nLa especialidad no puede contener solo números.");
            return false;
        }
        
        if (especialidad.length() < 3) {
            System.out.println("\nLa especialidad debe tener al menos 3 caracteres.");
            return false;
        }
        
        return true;
    }
    
    /**
     * Muestra el menú principal del sistema
     */
    private void mostrarMenuPrincipal() {
        while (true) {
            mostrarEncabezado("SISTEMA CUIDATE - Asistencia Básica en Salud");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Registrarse");
            System.out.println("3. Consultar servicios");
            System.out.println("4. Salir");
            System.out.println();
            
            System.out.print("Selecciona una opción: ");
            String opcion = scanner.nextLine().trim();
            
            switch (opcion) {
                case "1":
                    iniciarSesion();
                    break;
                case "2":
                    registrarse();
                    break;
                case "3":
                    consultarServicios();
                    break;
                case "4":
                    System.out.println("\n¡Gracias por usar CUIDATE! Hasta pronto.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("\nOpción inválida. Intenta de nuevo.");
                    pausar();
            }
        }
    }
    
    /**
     * Proceso de inicio de sesión
     */
    private void iniciarSesion() {
        mostrarEncabezado("Iniciar Sesión");
        
        System.out.println("Tipo de usuario:");
        System.out.println("1. Paciente");
        System.out.println("2. Médico");
        System.out.println();
        
        System.out.print("Selecciona el tipo de usuario: ");
        String tipoUsuario = scanner.nextLine().trim();
        
        if (!tipoUsuario.equals("1") && !tipoUsuario.equals("2")) {
            System.out.println("\nTipo de usuario inválido.");
            pausar();
            return;
        }
        
        System.out.println();
        System.out.print("Cédula: ");
        String cedula = scanner.nextLine().trim();
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine().trim();
        
        Usuario usuario = gestionUsuarios.iniciarSesion(cedula, contrasena);
        
        if (usuario != null) {
            // Verificar que el tipo de usuario coincida con la selección
            if (tipoUsuario.equals("1") && !(usuario instanceof Paciente)) {
                System.out.println("\nError: Esta cédula pertenece a un médico, no a un paciente.");
                pausar();
                return;
            } else if (tipoUsuario.equals("2") && !(usuario instanceof Medico)) {
                System.out.println("\nError: Esta cédula pertenece a un paciente, no a un médico.");
                pausar();
                return;
            }
            
            this.usuarioActual = usuario;
            System.out.println("\n¡Bienvenido/a, " + usuario.getNombreCompleto() + "!");
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            if (usuario instanceof Paciente) {
                menuPaciente();
            } else if (usuario instanceof Medico) {
                menuMedico();
            }
        } else {
            System.out.println("\nCédula o contraseña incorrectas.");
            pausar();
        }
    }
    
    /**
     * Proceso de registro de nuevo usuario
     */
    private void registrarse() {
        mostrarEncabezado("Registro de Usuario");
        
        System.out.println("Tipo de usuario:");
        System.out.println("1. Paciente");
        System.out.println("2. Médico");
        System.out.println();
        
        System.out.print("Selecciona el tipo: ");
        String tipo = scanner.nextLine().trim();
        
        if (!tipo.equals("1") && !tipo.equals("2")) {
            System.out.println("\nTipo inválido.");
            pausar();
            return;
        }
        
        System.out.println();
        
        // Validar nombre
        String nombre;
        while (true) {
            System.out.print("Nombre: ");
            nombre = scanner.nextLine().trim();
            if (validarNombre(nombre, "Nombre")) {
                break;
            }
        }
        
        // Validar apellido
        String apellido;
        while (true) {
            System.out.print("Apellido: ");
            apellido = scanner.nextLine().trim();
            if (validarNombre(apellido, "Apellido")) {
                break;
            }
        }
        
        // Validar cédula
        String cedula;
        while (true) {
            System.out.print("Cédula: ");
            cedula = scanner.nextLine().trim();
            if (validarCedula(cedula)) {
                if (gestionUsuarios.buscarPorCedula(cedula) != null) {
                    System.out.println("\nError: Esta cédula ya está registrada.");
                } else {
                    break;
                }
            }
        }
        
        System.out.print("Correo electrónico: ");
        String correo = scanner.nextLine().trim();
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine().trim();
        
        Usuario usuario = null;
        
        if (tipo.equals("1")) {
            System.out.println();
            System.out.print("Edad: ");
            int edad = 0;
            try {
                edad = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                edad = 0;
            }
            
            System.out.print("Género (M/F/Otro): ");
            String genero = scanner.nextLine().trim();
            System.out.print("Teléfono: ");
            String telefono = scanner.nextLine().trim();
            
            usuario = gestionUsuarios.registrarPaciente(nombre, apellido, cedula,
                                                       correo, contrasena, edad,
                                                       genero, telefono);
        } else {
            System.out.println();
            
            // Validar especialidad
            String especialidad;
            while (true) {
                System.out.print("Especialidad (ej: Cardiología, Pediatría, Medicina General): ");
                especialidad = scanner.nextLine().trim();
                if (validarEspecialidad(especialidad)) {
                    break;
                }
            }
            
            System.out.print("Registro Médico (ej: RM-2024-001): ");
            String registroMedico = scanner.nextLine().trim();
            
            usuario = gestionUsuarios.registrarMedico(nombre, apellido, cedula,
                                                     correo, contrasena, especialidad,
                                                     registroMedico);
        }
        
        if (usuario != null) {
            System.out.println("\n✓ Registro exitoso! Tu ID es: " + usuario.getIdUsuario());
        } else {
            System.out.println("\nError: No se pudo completar el registro.");
        }
        
        pausar();
    }
    
    /**
     * Muestra información sobre los servicios disponibles
     */
    private void consultarServicios() {
        mostrarEncabezado("Servicios Disponibles");
        
        System.out.println("📋 CUIDATE ofrece los siguientes servicios:\n");
        System.out.println("✓ Monitoreo de signos vitales en tiempo real");
        System.out.println("✓ Consultas médicas en línea");
        System.out.println("✓ Historial médico digital");
        System.out.println("✓ Consejos de salud personalizados");
        System.out.println("✓ Seguimiento de tratamientos");
        System.out.println("✓ Alertas de salud preventivas");
        System.out.println("\n¡Regístrate para acceder a todos estos servicios!");
        
        pausar();
    }
    
    /**
     * Menú principal para pacientes
     */
    private void menuPaciente() {
        while (true) {
            mostrarEncabezado("Panel del Paciente - " + usuarioActual.getNombreCompleto());
            
            System.out.println("1. Monitoreo de salud");
            System.out.println("2. Solicitar consulta en línea");
            System.out.println("3. Historial de consultas");
            System.out.println("4. Consejos de salud");
            System.out.println("5. Actualizar datos personales");
            System.out.println("6. Cerrar sesión");
            System.out.println();
            
            System.out.print("Selecciona una opción: ");
            String opcion = scanner.nextLine().trim();
            
            switch (opcion) {
                case "1":
                    monitoreoSaludPaciente();
                    break;
                case "2":
                    solicitarConsulta();
                    break;
                case "3":
                    historialConsultasPaciente();
                    break;
                case "4":
                    mostrarConsejosSalud();
                    break;
                case "5":
                    actualizarDatosPaciente();
                    break;
                case "6":
                    cerrarSesion();
                    return;
                default:
                    System.out.println("\nOpción inválida.");
                    pausar();
            }
        }
    }
    
    /**
     * Monitoreo de salud para pacientes
     */
    private void monitoreoSaludPaciente() {
        mostrarEncabezado("Monitoreo de Salud");
        
        System.out.println("Generando lectura de signos vitales...\n");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        RegistroSalud registro = monitoreoSalud.crearRegistroAleatorio(usuarioActual.getIdUsuario());
        
        System.out.println("📊 SIGNOS VITALES - " + registro.getFechaRegistro());
        System.out.println("------------------------------------------------------------");
        System.out.println("Presión Arterial: " + registro.getPresionSistolica() + "/" + 
                          registro.getPresionDiastolica() + " mmHg");
        System.out.println("Frecuencia Cardíaca: " + registro.getFrecuenciaCardiaca() + " lpm");
        System.out.println("Temperatura: " + registro.getTemperatura() + "°C");
        System.out.println("Saturación de Oxígeno: " + registro.getSaturacionOxigeno() + "%");
        System.out.println("------------------------------------------------------------");
        System.out.println("\n📋 Evaluación: " + registro.evaluarEstado());
        
        // Mostrar tendencias si hay registros anteriores
        Map<String, String> tendencias = monitoreoSalud.analizarTendencias(usuarioActual.getIdUsuario());
        
        if (!tendencias.containsKey("mensaje")) {
            System.out.println("\n📈 Tendencias:");
            System.out.println("  • Presión arterial: " + tendencias.getOrDefault("presion", "N/A"));
            System.out.println("  • Frecuencia cardíaca: " + tendencias.getOrDefault("frecuencia", "N/A"));
            System.out.println("  • Temperatura: " + tendencias.getOrDefault("temperatura", "N/A"));
        }
        
        pausar();
    }
    
    /**
     * Solicitud de consulta médica
     */
    private void solicitarConsulta() {
        mostrarEncabezado("Solicitar Consulta en Línea");
        
        List<Medico> medicos = gestionUsuarios.obtenerMedicos();
        
        if (medicos.isEmpty()) {
            System.out.println("No hay médicos disponibles en este momento.");
            pausar();
            return;
        }
        
        System.out.println("Médicos disponibles:\n");
        for (int i = 0; i < medicos.size(); i++) {
            Medico medico = medicos.get(i);
            System.out.println((i + 1) + ". Dr(a). " + medico.getNombreCompleto());
            System.out.println("   Especialidad: " + medico.getEspecialidad());
            System.out.println("   Experiencia: " + medico.getAnosExperiencia() + " años\n");
        }
        
        try {
            System.out.print("Selecciona un médico (número): ");
            int seleccion = Integer.parseInt(scanner.nextLine().trim());
            
            if (seleccion >= 1 && seleccion <= medicos.size()) {
                Medico medico = medicos.get(seleccion - 1);
                System.out.println();
                System.out.print("Motivo de la consulta: ");
                String motivo = scanner.nextLine().trim();
                
                if (!motivo.isEmpty()) {
                    Consulta consulta = gestionConsultas.crearConsulta(
                        usuarioActual.getIdUsuario(),
                        medico.getIdUsuario(),
                        motivo
                    );
                    
                    // Actualizar historial del paciente
                    if (usuarioActual instanceof Paciente) {
                        ((Paciente) usuarioActual).agregarConsulta(consulta.getIdConsulta());
                        gestionUsuarios.actualizarUsuario(usuarioActual);
                    }
                    
                    // Asignar paciente al médico
                    medico.asignarPaciente(usuarioActual.getIdUsuario());
                    gestionUsuarios.actualizarUsuario(medico);
                    
                    System.out.println("\n✓ Consulta creada exitosamente!");
                    System.out.println("ID de consulta: " + consulta.getIdConsulta());
                    System.out.println("Estado: " + consulta.getEstado());
                } else {
                    System.out.println("\nDebe ingresar un motivo para la consulta.");
                }
            } else {
                System.out.println("\nSelección inválida.");
            }
        } catch (NumberFormatException e) {
            System.out.println("\nEntrada inválida.");
        }
        
        pausar();
    }
    
    /**
     * Muestra el historial de consultas del paciente
     */
    private void historialConsultasPaciente() {
        mostrarEncabezado("Historial de Consultas");
        
        List<Consulta> consultas = gestionConsultas.obtenerConsultasPaciente(usuarioActual.getIdUsuario());
        
        if (consultas.isEmpty()) {
            System.out.println("No tienes consultas registradas.");
        } else {
            for (Consulta consulta : consultas) {
                Usuario medico = gestionUsuarios.buscarPorId(consulta.getIdMedico());
                System.out.println("\n============================================================");
                System.out.println("ID: " + consulta.getIdConsulta());
                System.out.println("Médico: Dr(a). " + (medico != null ? medico.getNombreCompleto() : "N/A"));
                System.out.println("Fecha: " + consulta.getFechaSolicitud());
                System.out.println("Motivo: " + consulta.getMotivo());
                System.out.println("Estado: " + consulta.getEstado());
                
                if (!consulta.getDiagnostico().isEmpty()) {
                    System.out.println("\nDiagnóstico: " + consulta.getDiagnostico());
                }
                if (!consulta.getTratamiento().isEmpty()) {
                    System.out.println("Tratamiento: " + consulta.getTratamiento());
                }
                if (!consulta.getObservaciones().isEmpty()) {
                    System.out.println("Observaciones: " + consulta.getObservaciones());
                }
            }
        }
        
        pausar();
    }
    
    /**
     * Muestra consejos de salud aleatorios
     */
    private void mostrarConsejosSalud() {
        mostrarEncabezado("Consejos de Salud");
        
        List<String> consejos = monitoreoSalud.generarConsejosSalud();
        
        System.out.println("💡 Consejos para mantener una vida saludable:\n");
        for (int i = 0; i < consejos.size(); i++) {
            System.out.println((i + 1) + ". " + consejos.get(i));
        }
        
        pausar();
    }
    
    /**
     * Actualiza datos personales del paciente
     */
    private void actualizarDatosPaciente() {
        mostrarEncabezado("Actualizar Datos Personales");
        
        System.out.println("Deja en blanco para mantener el valor actual\n");
        
        // Validar nombre
        System.out.print("Nombre (" + usuarioActual.getNombre() + "): ");
        String nombreInput = scanner.nextLine().trim();
        String nombre = null;
        if (!nombreInput.isEmpty()) {
            while (!validarNombre(nombreInput, "Nombre")) {
                System.out.print("Nombre (" + usuarioActual.getNombre() + "): ");
                nombreInput = scanner.nextLine().trim();
            }
            nombre = nombreInput;
        }
        
        // Validar apellido
        System.out.print("Apellido (" + usuarioActual.getApellido() + "): ");
        String apellidoInput = scanner.nextLine().trim();
        String apellido = null;
        if (!apellidoInput.isEmpty()) {
            while (!validarNombre(apellidoInput, "Apellido")) {
                System.out.print("Apellido (" + usuarioActual.getApellido() + "): ");
                apellidoInput = scanner.nextLine().trim();
            }
            apellido = apellidoInput;
        }
        
        System.out.print("Correo (" + usuarioActual.getCorreo() + "): ");
        String correo = scanner.nextLine().trim();
        
        if (usuarioActual instanceof Paciente) {
            Paciente paciente = (Paciente) usuarioActual;
            System.out.print("Teléfono (" + paciente.getTelefono() + "): ");
            String telefonoInput = scanner.nextLine().trim();
            
            if (!telefonoInput.isEmpty()) {
                while (!telefonoInput.matches("\\d+")) {
                    System.out.println("\nEl teléfono debe contener solo números.");
                    System.out.print("Teléfono (" + paciente.getTelefono() + "): ");
                    telefonoInput = scanner.nextLine().trim();
                    if (telefonoInput.isEmpty()) break;
                }
            }
            
            usuarioActual.actualizarDatos(nombre, apellido, 
                                         correo.isEmpty() ? null : correo, null);
            
            if (!telefonoInput.isEmpty()) {
                paciente.setTelefono(telefonoInput);
            }
        } else {
            usuarioActual.actualizarDatos(nombre, apellido,
                                         correo.isEmpty() ? null : correo, null);
        }
        
        gestionUsuarios.actualizarUsuario(usuarioActual);
        System.out.println("\n✓ Datos actualizados correctamente.");
        pausar();
    }
    
    /**
     * Menú principal para médicos
     */
    private void menuMedico() {
        while (true) {
            mostrarEncabezado("Panel del Médico - Dr(a). " + usuarioActual.getNombreCompleto());
            
            System.out.println("1. Ver pacientes asignados");
            System.out.println("2. Consultas pendientes");
            System.out.println("3. Registrar diagnóstico");
            System.out.println("4. Historial atendido");
            System.out.println("5. Actualizar perfil");
            System.out.println("6. Cerrar sesión");
            System.out.println();
            
            System.out.print("Selecciona una opción: ");
            String opcion = scanner.nextLine().trim();
            
            switch (opcion) {
                case "1":
                    verPacientesAsignados();
                    break;
                case "2":
                    consultasPendientesMedico();
                    break;
                case "3":
                    registrarDiagnostico();
                    break;
                case "4":
                    historialAtendido();
                    break;
                case "5":
                    actualizarPerfilMedico();
                    break;
                case "6":
                    cerrarSesion();
                    return;
                default:
                    System.out.println("\nOpción inválida.");
                    pausar();
            }
        }
    }
    
    /**
     * Muestra los pacientes asignados al médico
     */
    private void verPacientesAsignados() {
        mostrarEncabezado("Pacientes Asignados");
        
        if (usuarioActual instanceof Medico) {
            Medico medico = (Medico) usuarioActual;
            
            if (medico.getPacientesAsignados().isEmpty()) {
                System.out.println("No tienes pacientes asignados aún.");
            } else {
                System.out.println("Total de pacientes: " + medico.getPacientesAsignados().size() + "\n");
                
                for (String idPaciente : medico.getPacientesAsignados()) {
                    Usuario pacienteUsuario = gestionUsuarios.buscarPorId(idPaciente);
                    if (pacienteUsuario != null) {
                        System.out.println("\n============================================================");
                        System.out.println("ID: " + pacienteUsuario.getIdUsuario());
                        System.out.println("Nombre: " + pacienteUsuario.getNombreCompleto());
                        
                        if (pacienteUsuario instanceof Paciente) {
                            Paciente paciente = (Paciente) pacienteUsuario;
                            System.out.println("Edad: " + paciente.getEdad());
                            System.out.println("Género: " + paciente.getGenero());
                            System.out.println("Teléfono: " + paciente.getTelefono());
                            System.out.println("Grupo Sanguíneo: " + paciente.getGrupoSanguineo());
                        }
                    }
                }
            }
        }
        
        pausar();
    }
    
    /**
     * Muestra las consultas pendientes del médico
     */
    private void consultasPendientesMedico() {
        mostrarEncabezado("Consultas Pendientes");
        
        List<Consulta> consultas = gestionConsultas.obtenerConsultasPendientesMedico(
            usuarioActual.getIdUsuario()
        );
        
        if (consultas.isEmpty()) {
            System.out.println("No tienes consultas pendientes.");
        } else {
            for (Consulta consulta : consultas) {
                Usuario paciente = gestionUsuarios.buscarPorId(consulta.getIdPaciente());
                System.out.println("\n============================================================");
                System.out.println("ID: " + consulta.getIdConsulta());
                System.out.println("Paciente: " + (paciente != null ? paciente.getNombreCompleto() : "N/A"));
                System.out.println("Fecha solicitud: " + consulta.getFechaSolicitud());
                System.out.println("Motivo: " + consulta.getMotivo());
                System.out.println("Estado: " + consulta.getEstado());
            }
        }
        
        pausar();
    }
    
    /**
     * Registra el diagnóstico de una consulta
     */
    private void registrarDiagnostico() {
        mostrarEncabezado("Registrar Diagnóstico");
        
        System.out.print("ID de la consulta: ");
        String idConsulta = scanner.nextLine().trim();
        Consulta consulta = gestionConsultas.obtenerConsulta(idConsulta);
        
        if (consulta == null) {
            System.out.println("\nConsulta no encontrada.");
        } else if (!consulta.getIdMedico().equals(usuarioActual.getIdUsuario())) {
            System.out.println("\nEsta consulta no está asignada a ti.");
        } else if (consulta.esCompletada()) {
            System.out.println("\nEsta consulta ya fue completada.");
        } else {
            Usuario paciente = gestionUsuarios.buscarPorId(consulta.getIdPaciente());
            System.out.println("\nPaciente: " + (paciente != null ? paciente.getNombreCompleto() : "N/A"));
            System.out.println("Motivo: " + consulta.getMotivo() + "\n");
            
            System.out.print("Diagnóstico: ");
            String diagnostico = scanner.nextLine().trim();
            System.out.print("Tratamiento: ");
            String tratamiento = scanner.nextLine().trim();
            System.out.print("Observaciones: ");
            String observaciones = scanner.nextLine().trim();
            
            if (!diagnostico.isEmpty()) {
                gestionConsultas.registrarDiagnostico(idConsulta, diagnostico, 
                                                     tratamiento, observaciones);
                
                // Registrar en historial del médico
                if (usuarioActual instanceof Medico) {
                    ((Medico) usuarioActual).registrarConsultaAtendida(idConsulta);
                    gestionUsuarios.actualizarUsuario(usuarioActual);
                }
                
                System.out.println("\n✓ Diagnóstico registrado exitosamente.");
            } else {
                System.out.println("\nDebe ingresar un diagnóstico.");
            }
        }
        
        pausar();
    }
    
    /**
     * Muestra el historial de consultas atendidas
     */
    private void historialAtendido() {
        mostrarEncabezado("Historial de Consultas Atendidas");
        
        List<Consulta> consultas = gestionConsultas.obtenerConsultasCompletadasMedico(
            usuarioActual.getIdUsuario()
        );
        
        if (consultas.isEmpty()) {
            System.out.println("No has completado consultas aún.");
        } else {
            Map<String, Integer> estadisticas = gestionConsultas.obtenerEstadisticasMedico(
                usuarioActual.getIdUsuario()
            );
            
            System.out.println("📊 Estadísticas:");
            System.out.println("  Total de consultas: " + estadisticas.get("total"));
            System.out.println("  Completadas: " + estadisticas.getOrDefault("completadas", 0));
            System.out.println("  Pendientes: " + estadisticas.getOrDefault("pendientes", 0));
            System.out.println("  En proceso: " + estadisticas.getOrDefault("en_proceso", 0));
            
            System.out.println("\n============================================================");
            System.out.println("Últimas consultas completadas:\n");
            
            // Mostrar últimas 5
            int limite = Math.min(5, consultas.size());
            for (int i = consultas.size() - limite; i < consultas.size(); i++) {
                Consulta consulta = consultas.get(i);
                Usuario paciente = gestionUsuarios.buscarPorId(consulta.getIdPaciente());
                System.out.println("\n============================================================");
                System.out.println("ID: " + consulta.getIdConsulta());
                System.out.println("Paciente: " + (paciente != null ? paciente.getNombreCompleto() : "N/A"));
                System.out.println("Fecha: " + consulta.getFechaAtencion());
                System.out.println("Diagnóstico: " + consulta.getDiagnostico());
            }
        }
        
        pausar();
    }
    
    /**
     * Actualiza el perfil del médico
     */
    private void actualizarPerfilMedico() {
        mostrarEncabezado("Actualizar Perfil Profesional");
        
        System.out.println("Deja en blanco para mantener el valor actual\n");
        
        // Validar nombre
        System.out.print("Nombre (" + usuarioActual.getNombre() + "): ");
        String nombreInput = scanner.nextLine().trim();
        String nombre = null;
        if (!nombreInput.isEmpty()) {
            while (!validarNombre(nombreInput, "Nombre")) {
                System.out.print("Nombre (" + usuarioActual.getNombre() + "): ");
                nombreInput = scanner.nextLine().trim();
            }
            nombre = nombreInput;
        }
        
        // Validar apellido
        System.out.print("Apellido (" + usuarioActual.getApellido() + "): ");
        String apellidoInput = scanner.nextLine().trim();
        String apellido = null;
        if (!apellidoInput.isEmpty()) {
            while (!validarNombre(apellidoInput, "Apellido")) {
                System.out.print("Apellido (" + usuarioActual.getApellido() + "): ");
                apellidoInput = scanner.nextLine().trim();
            }
            apellido = apellidoInput;
        }
        
        System.out.print("Correo (" + usuarioActual.getCorreo() + "): ");
        String correo = scanner.nextLine().trim();
        
        if (usuarioActual instanceof Medico) {
            Medico medico = (Medico) usuarioActual;
            System.out.print("Especialidad (" + medico.getEspecialidad() + "): ");
            String especialidadInput = scanner.nextLine().trim();
            
            if (!especialidadInput.isEmpty()) {
                while (!validarEspecialidad(especialidadInput)) {
                    System.out.print("Especialidad (" + medico.getEspecialidad() + "): ");
                    especialidadInput = scanner.nextLine().trim();
                    if (especialidadInput.isEmpty()) break;
                }
            }
            
            usuarioActual.actualizarDatos(nombre, apellido,
                                         correo.isEmpty() ? null : correo, null);
            
            if (!especialidadInput.isEmpty()) {
                medico.setEspecialidad(especialidadInput);
            }
        }
        
        gestionUsuarios.actualizarUsuario(usuarioActual);
        System.out.println("\n✓ Perfil actualizado correctamente.");
        pausar();
    }
    
    /**
     * Cierra la sesión actual
     */
    private void cerrarSesion() {
        System.out.println("\nCerrando sesión...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        this.usuarioActual = null;
    }
    
    /**
     * Inicia el sistema
     */
    public void ejecutar() {
        mostrarMenuPrincipal();
    }
    
    /**
     * Función principal
     */
    public static void main(String[] args) {
        try {
            Main sistema = new Main();
            sistema.ejecutar();
        } catch (Exception e) {
            System.err.println("\nError inesperado: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}