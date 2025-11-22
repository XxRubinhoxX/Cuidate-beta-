"""
Servicio de Gestión de Usuarios - Registro, login y persistencia
"""
import json
import os
from typing import Dict, Optional, List
from models.usuario import Usuario
from models.paciente import Paciente
from models.medico import Medico


class GestionUsuarios:
    """Servicio para gestionar usuarios del sistema"""
    
    def __init__(self, archivo_datos: str = "data/usuarios.json"):
        self.archivo_datos = archivo_datos
        self.usuarios: Dict[str, Usuario] = {}
        self._asegurar_directorio()
        self.cargar_usuarios()
    
    def _asegurar_directorio(self):
        """Crea el directorio data si no existe"""
        directorio = os.path.dirname(self.archivo_datos)
        if directorio and not os.path.exists(directorio):
            os.makedirs(directorio)
    
    def cargar_usuarios(self):
        """Carga usuarios desde el archivo JSON"""
        if os.path.exists(self.archivo_datos):
            try:
                with open(self.archivo_datos, 'r', encoding='utf-8') as f:
                    datos = json.load(f)
                    for id_usuario, data in datos.items():
                        if data['tipo'] == 'paciente':
                            self.usuarios[id_usuario] = Paciente.from_dict(data)
                        elif data['tipo'] == 'medico':
                            self.usuarios[id_usuario] = Medico.from_dict(data)
            except Exception as e:
                print(f"Error al cargar usuarios: {e}")
                self.usuarios = {}
        else:
            self._crear_usuarios_ejemplo()
    
    def _crear_usuarios_ejemplo(self):
        """Crea usuarios de ejemplo para pruebas"""
        # Crear un médico de ejemplo
        medico = Medico(
            id_usuario="MED001",
            nombre="Carlos",
            apellido="Gaitan",
            cedula="1234567890",
            correo="carlos.gaitan@cuidate.com",
            contrasena="medico123",
            especialidad="Medicina General",
            registro_medico="RM-2024-001",
            anos_experiencia=5
        )
        self.usuarios[medico.id_usuario] = medico
        
        # Crear un paciente de ejemplo
        paciente = Paciente(
            id_usuario="PAC001",
            nombre="lana",
            apellido="ruedas",
            cedula="0987654321",
            correo="lana.ruedas@email.com",
            contrasena="paciente123",
            edad=30,
            genero="Femenino",
            direccion="Calle 123",
            telefono="3001234567",
            grupo_sanguineo="O+"
        )
        self.usuarios[paciente.id_usuario] = paciente
        
        self.guardar_usuarios()
    
    def guardar_usuarios(self):
        """Guarda usuarios en el archivo JSON"""
        try:
            datos = {id_u: u.to_dict() for id_u, u in self.usuarios.items()}
            with open(self.archivo_datos, 'w', encoding='utf-8') as f:
                json.dump(datos, f, indent=4, ensure_ascii=False)
        except Exception as e:
            print(f"Error al guardar usuarios: {e}")
    
    def registrar_paciente(self, nombre: str, apellido: str, cedula: str,
                          correo: str, contrasena: str, edad: int = 0,
                          genero: str = "", telefono: str = "") -> Optional[Paciente]:
        """Registra un nuevo paciente"""
        if self.buscar_por_cedula(cedula):
            return None
        
        id_usuario = f"PAC{len([u for u in self.usuarios.values() if u.tipo == 'paciente']) + 1:03d}"
        paciente = Paciente(
            id_usuario=id_usuario,
            nombre=nombre,
            apellido=apellido,
            cedula=cedula,
            correo=correo,
            contrasena=contrasena,
            edad=edad,
            genero=genero,
            telefono=telefono
        )
        self.usuarios[id_usuario] = paciente
        self.guardar_usuarios()
        return paciente
    
    def registrar_medico(self, nombre: str, apellido: str, cedula: str,
                        correo: str, contrasena: str, especialidad: str = "",
                        registro_medico: str = "") -> Optional[Medico]:
        """Registra un nuevo médico"""
        if self.buscar_por_cedula(cedula):
            return None
        
        id_usuario = f"MED{len([u for u in self.usuarios.values() if u.tipo == 'medico']) + 1:03d}"
        medico = Medico(
            id_usuario=id_usuario,
            nombre=nombre,
            apellido=apellido,
            cedula=cedula,
            correo=correo,
            contrasena=contrasena,
            especialidad=especialidad,
            registro_medico=registro_medico
        )
        self.usuarios[id_usuario] = medico
        self.guardar_usuarios()
        return medico
    
    def iniciar_sesion(self, cedula: str, contrasena: str) -> Optional[Usuario]:
        """Inicia sesión y retorna el usuario si es válido"""
        usuario = self.buscar_por_cedula(cedula)
        if usuario and usuario.verificar_contrasena(contrasena):
            return usuario
        return None
    
    def buscar_por_cedula(self, cedula: str) -> Optional[Usuario]:
        """Busca un usuario por su cédula"""
        for usuario in self.usuarios.values():
            if usuario.cedula == cedula:
                return usuario
        return None
    
    def buscar_por_id(self, id_usuario: str) -> Optional[Usuario]:
        """Busca un usuario por su ID"""
        return self.usuarios.get(id_usuario)
    
    def obtener_medicos(self) -> List[Medico]:
        """Obtiene la lista de todos los médicos"""
        return [u for u in self.usuarios.values() if isinstance(u, Medico)]
    
    def obtener_pacientes(self) -> List[Paciente]:
        """Obtiene la lista de todos los pacientes"""
        return [u for u in self.usuarios.values() if isinstance(u, Paciente)]
    
    def actualizar_usuario(self, usuario: Usuario):
        """Actualiza un usuario en el sistema"""
        if usuario.id_usuario in self.usuarios:
            self.usuarios[usuario.id_usuario] = usuario
            self.guardar_usuarios()