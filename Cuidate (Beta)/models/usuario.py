
"""
Módulo de Usuario - Clase base para Paciente y Médico
"""
from typing import Dict, Any
from datetime import datetime


class Usuario:
    """Clase base para todos los usuarios del sistema"""
    
    def __init__(self, id_usuario: str, nombre: str, apellido: str, 
                 cedula: str, correo: str, contrasena: str, tipo: str):
        self.id_usuario = id_usuario
        self.nombre = nombre
        self.apellido = apellido
        self.cedula = cedula
        self.correo = correo
        self.contrasena = contrasena
        self.tipo = tipo  # 'paciente' o 'medico'
        self.fecha_registro = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    
    def to_dict(self) -> Dict[str, Any]:
        """Convierte el usuario a diccionario para JSON"""
        return {
            'id_usuario': self.id_usuario,
            'nombre': self.nombre,
            'apellido': self.apellido,
            'cedula': self.cedula,
            'correo': self.correo,
            'contrasena': self.contrasena,
            'tipo': self.tipo,
            'fecha_registro': self.fecha_registro
        }
    
    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> 'Usuario':
        """Crea un usuario desde un diccionario"""
        return cls(
            id_usuario=data['id_usuario'],
            nombre=data['nombre'],
            apellido=data['apellido'],
            cedula=data['cedula'],
            correo=data['correo'],
            contrasena=data['contrasena'],
            tipo=data['tipo']
        )
    
    def actualizar_datos(self, nombre: str = None, apellido: str = None, 
                        correo: str = None, contrasena: str = None):
        """Actualiza los datos del usuario"""
        if nombre:
            self.nombre = nombre
        if apellido:
            self.apellido = apellido
        if correo:
            self.correo = correo
        if contrasena:
            self.contrasena = contrasena
    
    def verificar_contrasena(self, contrasena: str) -> bool:
        """Verifica si la contraseña es correcta"""
        return self.contrasena == contrasena
    
    def get_nombre_completo(self) -> str:
        """Retorna el nombre completo del usuario"""
        return f"{self.nombre} {self.apellido}"