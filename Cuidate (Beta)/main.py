"""
CUIDATE - Sistema de Asistencia Básica en Salud
Versión Beta - Terminal
"""
import os
import sys
import time
from typing import Optional

# Importar servicios
from services.gestion_usuarios import GestionUsuarios
from services.gestion_consultas import GestionConsultas
from services.monitoreo_salud import MonitoreoSalud

# Importar modelos
from models.usuario import Usuario
from models.paciente import Paciente
from models.medico import Medico


class SistemaCuidate:
    """Clase principal del sistema CUIDATE"""
    
    def __init__(self):
        self.gestion_usuarios = GestionUsuarios()
        self.gestion_consultas = GestionConsultas()
        self.monitoreo_salud = MonitoreoSalud()
        self.usuario_actual: Optional[Usuario] = None
    
    def limpiar_pantalla(self):
        """Limpia la pantalla de la terminal"""
        os.system('cls' if os.name == 'nt' else 'clear')
    
    def pausar(self):
        """Pausa la ejecución hasta que el usuario presione Enter"""
        input("\nPresiona Enter para continuar...")
    
    def mostrar_encabezado(self, titulo: str):
        """Muestra un encabezado decorado"""
        self.limpiar_pantalla()
        print("=" * 60)
        print(f"  {titulo.center(56)}")
        print("=" * 60)
        print()
    
    def validar_nombre(self, texto: str, campo: str) -> bool:
        """Valida que un nombre o apellido no contenga números"""
        if not texto:
            print(f"\n{campo} no puede estar vacío.")
            return False
        
        if any(char.isdigit() for char in texto):
            print(f"\n{campo} no puede contener números.")
            return False
        
        if len(texto) < 2:
            print(f"\n{campo} debe tener al menos 2 caracteres.")
            return False
        
        return True
    
    def validar_cedula(self, cedula: str) -> bool:
        """Valida que la cédula contenga solo números"""
        if not cedula:
            print("\nLa cédula no puede estar vacía.")
            return False
        
        if not cedula.isdigit():
            print("\nLa cédula debe contener solo números.")
            return False
        
        if len(cedula) < 6:
            print("\nLa cédula debe tener al menos 6 dígitos.")
            return False
        
        return True
    
    def validar_especialidad(self, especialidad: str) -> bool:
        """Valida que la especialidad no contenga solo números"""
        if not especialidad:
            print("\nLa especialidad no puede estar vacía.")
            return False
        
        if especialidad.isdigit():
            print("\nLa especialidad no puede contener solo números.")
            return False
        
        if len(especialidad) < 3:
            print("\nLa especialidad debe tener al menos 3 caracteres.")
            return False
        
        return True
    
    def mostrar_menu_principal(self):
        """Muestra el menú principal del sistema"""
        while True:
            self.mostrar_encabezado("SISTEMA CUIDATE - Asistencia Básica en Salud")
            print("1. Iniciar sesión")
            print("2. Registrarse")
            print("3. Consultar servicios")
            print("4. Salir")
            print()
            
            opcion = input("Selecciona una opción: ").strip()
            
            if opcion == '1':
                self.iniciar_sesion()
            elif opcion == '2':
                self.registrarse()
            elif opcion == '3':
                self.consultar_servicios()
            elif opcion == '4':
                print("\n¡Gracias por usar CUIDATE! Hasta pronto.")
                sys.exit(0)
            else:
                print("\nOpción inválida. Intenta de nuevo.")
                self.pausar()
    
    def iniciar_sesion(self):
        """Proceso de inicio de sesión"""
        self.mostrar_encabezado("Iniciar Sesión")
        
        print("Tipo de usuario:")
        print("1. Paciente")
        print("2. Médico")
        print()
        
        tipo_usuario = input("Selecciona el tipo de usuario: ").strip()
        
        if tipo_usuario not in ['1', '2']:
            print("\nTipo de usuario inválido.")
            self.pausar()
            return
        
        print()
        cedula = input("Cédula: ").strip()
        contrasena = input("Contraseña: ").strip()
        
        usuario = self.gestion_usuarios.iniciar_sesion(cedula, contrasena)
        
        if usuario:
            # Verificar que el tipo de usuario coincida con la selección
            if tipo_usuario == '1' and not isinstance(usuario, Paciente):
                print("\nError: Esta cédula pertenece a un médico, no a un paciente.")
                self.pausar()
                return
            elif tipo_usuario == '2' and not isinstance(usuario, Medico):
                print("\nError: Esta cédula pertenece a un paciente, no a un médico.")
                self.pausar()
                return
            
            self.usuario_actual = usuario
            print(f"\n¡Bienvenido/a, {usuario.get_nombre_completo()}!")
            time.sleep(1)
            
            if isinstance(usuario, Paciente):
                self.menu_paciente()
            elif isinstance(usuario, Medico):
                self.menu_medico()
        else:
            print("\nCédula o contraseña incorrectas.")
            self.pausar()
    
    def registrarse(self):
        """Proceso de registro de nuevo usuario"""
        self.mostrar_encabezado("Registro de Usuario")
        
        print("Tipo de usuario:")
        print("1. Paciente")
        print("2. Médico")
        print()
        
        tipo = input("Selecciona el tipo: ").strip()
        
        if tipo not in ['1', '2']:
            print("\nTipo inválido.")
            self.pausar()
            return
        
        print()
        
        # Validar nombre
        while True:
            nombre = input("Nombre: ").strip()
            if self.validar_nombre(nombre, "Nombre"):
                break
        
        # Validar apellido
        while True:
            apellido = input("Apellido: ").strip()
            if self.validar_nombre(apellido, "Apellido"):
                break
        
        # Validar cédula
        while True:
            cedula = input("Cédula: ").strip()
            if self.validar_cedula(cedula):
                # Verificar si la cédula ya existe
                if self.gestion_usuarios.buscar_por_cedula(cedula):
                    print("\nError: Esta cédula ya está registrada.")
                else:
                    break
        
        correo = input("Correo electrónico: ").strip()
        contrasena = input("Contraseña: ").strip()
        
        if tipo == '1':
            print()
            edad = int(input("Edad: ").strip() or "0")
            genero = input("Género (M/F/Otro): ").strip()
            telefono = input("Teléfono: ").strip()
            
            usuario = self.gestion_usuarios.registrar_paciente(
                nombre, apellido, cedula, correo, contrasena,
                edad, genero, telefono
            )
        else:
            print()
            # Validar especialidad
            while True:
                especialidad = input("Especialidad (ej: Cardiología, Pediatría, Medicina General): ").strip()
                if self.validar_especialidad(especialidad):
                    break
            
            registro_medico = input("Registro Médico (ej: RM-2024-001): ").strip()
            
            usuario = self.gestion_usuarios.registrar_medico(
                nombre, apellido, cedula, correo, contrasena,
                especialidad, registro_medico
            )
        
        if usuario:
            print(f"\n✓ Registro exitoso! Tu ID es: {usuario.id_usuario}")
        else:
            print("\nError: No se pudo completar el registro.")
        
        self.pausar()
    
    def consultar_servicios(self):
        """Muestra información sobre los servicios disponibles"""
        self.mostrar_encabezado("Servicios Disponibles")
        
        print("📋 CUIDATE ofrece los siguientes servicios:\n")
        print("✓ Monitoreo de signos vitales en tiempo real")
        print("✓ Consultas médicas en línea")
        print("✓ Historial médico digital")
        print("✓ Consejos de salud personalizados")
        print("✓ Seguimiento de tratamientos")
        print("✓ Alertas de salud preventivas")
        print("\n¡Regístrate para acceder a todos estos servicios!")
        
        self.pausar()
    
    def menu_paciente(self):
        """Menú principal para pacientes"""
        while True:
            self.mostrar_encabezado(f"Panel del Paciente - {self.usuario_actual.get_nombre_completo()}")
            
            print("1. Monitoreo de salud")
            print("2. Solicitar consulta en línea")
            print("3. Historial de consultas")
            print("4. Consejos de salud")
            print("5. Actualizar datos personales")
            print("6. Cerrar sesión")
            print()
            
            opcion = input("Selecciona una opción: ").strip()
            
            if opcion == '1':
                self.monitoreo_salud_paciente()
            elif opcion == '2':
                self.solicitar_consulta()
            elif opcion == '3':
                self.historial_consultas_paciente()
            elif opcion == '4':
                self.mostrar_consejos_salud()
            elif opcion == '5':
                self.actualizar_datos_paciente()
            elif opcion == '6':
                self.cerrar_sesion()
                break
            else:
                print("\nOpción inválida.")
                self.pausar()
    
    def monitoreo_salud_paciente(self):
        """Monitoreo de salud para pacientes"""
        self.mostrar_encabezado("Monitoreo de Salud")
        
        print("Generando lectura de signos vitales...\n")
        time.sleep(1)
        
        registro = self.monitoreo_salud.crear_registro_aleatorio(
            self.usuario_actual.id_usuario
        )
        
        print(f"📊 SIGNOS VITALES - {registro.fecha_registro}")
        print("-" * 60)
        print(f"Presión Arterial: {registro.presion_sistolica}/{registro.presion_diastolica} mmHg")
        print(f"Frecuencia Cardíaca: {registro.frecuencia_cardiaca} lpm")
        print(f"Temperatura: {registro.temperatura}°C")
        print(f"Saturación de Oxígeno: {registro.saturacion_oxigeno}%")
        print("-" * 60)
        print(f"\n📋 Evaluación: {registro.evaluar_estado()}")
        
        # Mostrar tendencias si hay registros anteriores
        tendencias = self.monitoreo_salud.analizar_tendencias(
            self.usuario_actual.id_usuario
        )
        
        if 'mensaje' not in tendencias:
            print("\n📈 Tendencias:")
            print(f"  • Presión arterial: {tendencias.get('presion', 'N/A')}")
            print(f"  • Frecuencia cardíaca: {tendencias.get('frecuencia', 'N/A')}")
            print(f"  • Temperatura: {tendencias.get('temperatura', 'N/A')}")
        
        self.pausar()
    
    def solicitar_consulta(self):
        """Solicitud de consulta médica"""
        self.mostrar_encabezado("Solicitar Consulta en Línea")
        
        # Mostrar médicos disponibles
        medicos = self.gestion_usuarios.obtener_medicos()
        
        if not medicos:
            print("No hay médicos disponibles en este momento.")
            self.pausar()
            return
        
        print("Médicos disponibles:\n")
        for i, medico in enumerate(medicos, 1):
            print(f"{i}. Dr(a). {medico.get_nombre_completo()}")
            print(f"   Especialidad: {medico.especialidad}")
            print(f"   Experiencia: {medico.anos_experiencia} años\n")
        
        try:
            seleccion = int(input("Selecciona un médico (número): ").strip())
            if 1 <= seleccion <= len(medicos):
                medico = medicos[seleccion - 1]
                print()
                motivo = input("Motivo de la consulta: ").strip()
                
                if motivo:
                    consulta = self.gestion_consultas.crear_consulta(
                        self.usuario_actual.id_usuario,
                        medico.id_usuario,
                        motivo
                    )
                    
                    # Actualizar historial del paciente
                    if isinstance(self.usuario_actual, Paciente):
                        self.usuario_actual.agregar_consulta(consulta.id_consulta)
                        self.gestion_usuarios.actualizar_usuario(self.usuario_actual)
                    
                    # Asignar paciente al médico
                    medico.asignar_paciente(self.usuario_actual.id_usuario)
                    self.gestion_usuarios.actualizar_usuario(medico)
                    
                    print(f"\n✓ Consulta creada exitosamente!")
                    print(f"ID de consulta: {consulta.id_consulta}")
                    print(f"Estado: {consulta.estado}")
                else:
                    print("\nDebe ingresar un motivo para la consulta.")
            else:
                print("\nSelección inválida.")
        except ValueError:
            print("\nEntrada inválida.")
        
        self.pausar()
    
    def historial_consultas_paciente(self):
        """Muestra el historial de consultas del paciente"""
        self.mostrar_encabezado("Historial de Consultas")
        
        consultas = self.gestion_consultas.obtener_consultas_paciente(
            self.usuario_actual.id_usuario
        )
        
        if not consultas:
            print("No tienes consultas registradas.")
        else:
            for consulta in consultas:
                medico = self.gestion_usuarios.buscar_por_id(consulta.id_medico)
                print(f"\n{'='*60}")
                print(f"ID: {consulta.id_consulta}")
                print(f"Médico: Dr(a). {medico.get_nombre_completo() if medico else 'N/A'}")
                print(f"Fecha: {consulta.fecha_solicitud}")
                print(f"Motivo: {consulta.motivo}")
                print(f"Estado: {consulta.estado}")
                
                if consulta.diagnostico:
                    print(f"\nDiagnóstico: {consulta.diagnostico}")
                if consulta.tratamiento:
                    print(f"Tratamiento: {consulta.tratamiento}")
                if consulta.observaciones:
                    print(f"Observaciones: {consulta.observaciones}")
        
        self.pausar()
    
    def mostrar_consejos_salud(self):
        """Muestra consejos de salud aleatorios"""
        self.mostrar_encabezado("Consejos de Salud")
        
        consejos = self.monitoreo_salud.generar_consejos_salud()
        
        print("💡 Consejos para mantener una vida saludable:\n")
        for i, consejo in enumerate(consejos, 1):
            print(f"{i}. {consejo}")
        
        self.pausar()
    
    def actualizar_datos_paciente(self):
        """Actualiza datos personales del paciente"""
        self.mostrar_encabezado("Actualizar Datos Personales")
        
        print("Deja en blanco para mantener el valor actual\n")
        
        # Validar nombre
        nombre_input = input(f"Nombre ({self.usuario_actual.nombre}): ").strip()
        if nombre_input:
            while not self.validar_nombre(nombre_input, "Nombre"):
                nombre_input = input(f"Nombre ({self.usuario_actual.nombre}): ").strip()
        nombre = nombre_input if nombre_input else None
        
        # Validar apellido
        apellido_input = input(f"Apellido ({self.usuario_actual.apellido}): ").strip()
        if apellido_input:
            while not self.validar_nombre(apellido_input, "Apellido"):
                apellido_input = input(f"Apellido ({self.usuario_actual.apellido}): ").strip()
        apellido = apellido_input if apellido_input else None
        
        correo = input(f"Correo ({self.usuario_actual.correo}): ").strip()
        
        if isinstance(self.usuario_actual, Paciente):
            telefono = input(f"Teléfono ({self.usuario_actual.telefono}): ").strip()
            
            # Validar teléfono si se ingresa uno nuevo
            if telefono:
                while not telefono.isdigit():
                    print("\nEl teléfono debe contener solo números.")
                    telefono = input(f"Teléfono ({self.usuario_actual.telefono}): ").strip()
                    if not telefono:  # Si deja en blanco, salir del bucle
                        break
            
            self.usuario_actual.actualizar_datos(
                nombre,
                apellido,
                correo or None
            )
            
            if telefono:
                self.usuario_actual.telefono = telefono
        else:
            self.usuario_actual.actualizar_datos(
                nombre,
                apellido,
                correo or None
            )
        
        self.gestion_usuarios.actualizar_usuario(self.usuario_actual)
        print("\n✓ Datos actualizados correctamente.")
        self.pausar()
    
    def menu_medico(self):
        """Menú principal para médicos"""
        while True:
            self.mostrar_encabezado(f"Panel del Médico - Dr(a). {self.usuario_actual.get_nombre_completo()}")
            
            print("1. Ver pacientes asignados")
            print("2. Consultas pendientes")
            print("3. Registrar diagnóstico")
            print("4. Historial atendido")
            print("5. Actualizar perfil")
            print("6. Cerrar sesión")
            print()
            
            opcion = input("Selecciona una opción: ").strip()
            
            if opcion == '1':
                self.ver_pacientes_asignados()
            elif opcion == '2':
                self.consultas_pendientes_medico()
            elif opcion == '3':
                self.registrar_diagnostico()
            elif opcion == '4':
                self.historial_atendido()
            elif opcion == '5':
                self.actualizar_perfil_medico()
            elif opcion == '6':
                self.cerrar_sesion()
                break
            else:
                print("\nOpción inválida.")
                self.pausar()
    
    def ver_pacientes_asignados(self):
        """Muestra los pacientes asignados al médico"""
        self.mostrar_encabezado("Pacientes Asignados")
        
        if isinstance(self.usuario_actual, Medico):
            if not self.usuario_actual.pacientes_asignados:
                print("No tienes pacientes asignados aún.")
            else:
                print(f"Total de pacientes: {len(self.usuario_actual.pacientes_asignados)}\n")
                
                for id_paciente in self.usuario_actual.pacientes_asignados:
                    paciente = self.gestion_usuarios.buscar_por_id(id_paciente)
                    if paciente:
                        print(f"\n{'='*60}")
                        print(f"ID: {paciente.id_usuario}")
                        print(f"Nombre: {paciente.get_nombre_completo()}")
                        if isinstance(paciente, Paciente):
                            print(f"Edad: {paciente.edad}")
                            print(f"Género: {paciente.genero}")
                            print(f"Teléfono: {paciente.telefono}")
                            print(f"Grupo Sanguíneo: {paciente.grupo_sanguineo}")
        
        self.pausar()
    
    def consultas_pendientes_medico(self):
        """Muestra las consultas pendientes del médico"""
        self.mostrar_encabezado("Consultas Pendientes")
        
        consultas = self.gestion_consultas.obtener_consultas_pendientes_medico(
            self.usuario_actual.id_usuario
        )
        
        if not consultas:
            print("No tienes consultas pendientes.")
        else:
            for consulta in consultas:
                paciente = self.gestion_usuarios.buscar_por_id(consulta.id_paciente)
                print(f"\n{'='*60}")
                print(f"ID: {consulta.id_consulta}")
                print(f"Paciente: {paciente.get_nombre_completo() if paciente else 'N/A'}")
                print(f"Fecha solicitud: {consulta.fecha_solicitud}")
                print(f"Motivo: {consulta.motivo}")
                print(f"Estado: {consulta.estado}")
        
        self.pausar()
    
    def registrar_diagnostico(self):
        """Registra el diagnóstico de una consulta"""
        self.mostrar_encabezado("Registrar Diagnóstico")
        
        id_consulta = input("ID de la consulta: ").strip()
        consulta = self.gestion_consultas.obtener_consulta(id_consulta)
        
        if not consulta:
            print("\nConsulta no encontrada.")
        elif consulta.id_medico != self.usuario_actual.id_usuario:
            print("\nEsta consulta no está asignada a ti.")
        elif consulta.es_completada():
            print("\nEsta consulta ya fue completada.")
        else:
            paciente = self.gestion_usuarios.buscar_por_id(consulta.id_paciente)
            print(f"\nPaciente: {paciente.get_nombre_completo() if paciente else 'N/A'}")
            print(f"Motivo: {consulta.motivo}\n")
            
            diagnostico = input("Diagnóstico: ").strip()
            tratamiento = input("Tratamiento: ").strip()
            observaciones = input("Observaciones: ").strip()
            
            if diagnostico:
                self.gestion_consultas.registrar_diagnostico(
                    id_consulta, diagnostico, tratamiento, observaciones
                )
                
                # Registrar en historial del médico
                if isinstance(self.usuario_actual, Medico):
                    self.usuario_actual.registrar_consulta_atendida(id_consulta)
                    self.gestion_usuarios.actualizar_usuario(self.usuario_actual)
                
                print("\n✓ Diagnóstico registrado exitosamente.")
            else:
                print("\nDebe ingresar un diagnóstico.")
        
        self.pausar()
    
    def historial_atendido(self):
        """Muestra el historial de consultas atendidas"""
        self.mostrar_encabezado("Historial de Consultas Atendidas")
        
        consultas = self.gestion_consultas.obtener_consultas_completadas_medico(
            self.usuario_actual.id_usuario
        )
        
        if not consultas:
            print("No has completado consultas aún.")
        else:
            estadisticas = self.gestion_consultas.obtener_estadisticas_medico(
                self.usuario_actual.id_usuario
            )
            
            print(f"📊 Estadísticas:")
            print(f"  Total de consultas: {estadisticas['total']}")
            print(f"  Completadas: {estadisticas['completadas']}")
            print(f"  Pendientes: {estadisticas['pendientes']}")
            print(f"  En proceso: {estadisticas['en_proceso']}")
            
            print(f"\n{'='*60}")
            print("Últimas consultas completadas:\n")
            
            for consulta in consultas[-5:]:  # Últimas 5
                paciente = self.gestion_usuarios.buscar_por_id(consulta.id_paciente)
                print(f"\n{'='*60}")
                print(f"ID: {consulta.id_consulta}")
                print(f"Paciente: {paciente.get_nombre_completo() if paciente else 'N/A'}")
                print(f"Fecha: {consulta.fecha_atencion}")
                print(f"Diagnóstico: {consulta.diagnostico}")
        
        self.pausar()
    
    def actualizar_perfil_medico(self):
        """Actualiza el perfil del médico"""
        self.mostrar_encabezado("Actualizar Perfil Profesional")
        
        print("Deja en blanco para mantener el valor actual\n")
        
        # Validar nombre
        nombre_input = input(f"Nombre ({self.usuario_actual.nombre}): ").strip()
        if nombre_input:
            while not self.validar_nombre(nombre_input, "Nombre"):
                nombre_input = input(f"Nombre ({self.usuario_actual.nombre}): ").strip()
        nombre = nombre_input if nombre_input else None
        
        # Validar apellido
        apellido_input = input(f"Apellido ({self.usuario_actual.apellido}): ").strip()
        if apellido_input:
            while not self.validar_nombre(apellido_input, "Apellido"):
                apellido_input = input(f"Apellido ({self.usuario_actual.apellido}): ").strip()
        apellido = apellido_input if apellido_input else None
        
        correo = input(f"Correo ({self.usuario_actual.correo}): ").strip()
        
        if isinstance(self.usuario_actual, Medico):
            especialidad_input = input(f"Especialidad ({self.usuario_actual.especialidad}): ").strip()
            if especialidad_input:
                while not self.validar_especialidad(especialidad_input):
                    especialidad_input = input(f"Especialidad ({self.usuario_actual.especialidad}): ").strip()
                    if not especialidad_input:  # Si deja en blanco, salir del bucle
                        break
            
            self.usuario_actual.actualizar_datos(
                nombre,
                apellido,
                correo or None
            )
            
            if especialidad_input:
                self.usuario_actual.especialidad = especialidad_input
        
        self.gestion_usuarios.actualizar_usuario(self.usuario_actual)
        print("\n✓ Perfil actualizado correctamente.")
        self.pausar()
    
    def cerrar_sesion(self):
        """Cierra la sesión actual"""
        print("\nCerrando sesión...")
        time.sleep(1)
        self.usuario_actual = None
    
    def ejecutar(self):
        """Inicia el sistema"""
        self.mostrar_menu_principal()


def main():
    """Función principal"""
    try:
        sistema = SistemaCuidate()
        sistema.ejecutar()
    except KeyboardInterrupt:
        print("\n\nPrograma interrumpido por el usuario.")
        sys.exit(0)
    except Exception as e:
        print(f"\nError inesperado: {e}")
        sys.exit(1)


if __name__ == "__main__":
    main()