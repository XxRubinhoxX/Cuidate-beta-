"""
Módulo de Paciente - Hereda de Usuario
"""
from typing import Dict, Any, List
from models.usuario import Usuario


class Paciente(Usuario):
    """Clase para pacientes del sistema"""
    
    def __init__(self, id_usuario: str, nombre: str, apellido: str, 
                 cedula: str, correo: str, contrasena: str,
                 edad: int = 0, genero: str = "", direccion: str = "",
                 telefono: str = "", grupo_sanguineo: str = ""):
        super().__init__(id_usuario, nombre, apellido, cedula, 
                        correo, contrasena, 'paciente')
        self.edad = edad
        self.genero = genero
        self.direccion = direccion
        self.telefono = telefono
        self.grupo_sanguineo = grupo_sanguineo
        self.historial_consultas: List[str] = []
    
    def to_dict(self) -> Dict[str, Any]:
        """Convierte el paciente a diccionario"""
        data = super().to_dict()
        data.update({
            'edad': self.edad,
            'genero': self.genero,
            'direccion': self.direccion,
            'telefono': self.telefono,
            'grupo_sanguineo': self.grupo_sanguineo,
            'historial_consultas': self.historial_consultas
        })
        return data
    
    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> 'Paciente':
        """Crea un paciente desde un diccionario"""
        paciente = cls(
            id_usuario=data['id_usuario'],
            nombre=data['nombre'],
            apellido=data['apellido'],
            cedula=data['cedula'],
            correo=data['correo'],
            contrasena=data['contrasena'],
            edad=data.get('edad', 0),
            genero=data.get('genero', ''),
            direccion=data.get('direccion', ''),
            telefono=data.get('telefono', ''),
            grupo_sanguineo=data.get('grupo_sanguineo', '')
        )
        paciente.historial_consultas = data.get('historial_consultas', [])
        return paciente
    
    def agregar_consulta(self, id_consulta: str):
        """Agrega una consulta al historial"""
        if id_consulta not in self.historial_consultas:
            self.historial_consultas.append(id_consulta)
    
    def actualizar_datos_medicos(self, edad: int = None, genero: str = None,
                                 direccion: str = None, telefono: str = None,
                                 grupo_sanguineo: str = None):
        """Actualiza datos médicos del paciente"""
        if edad is not None:
            self.edad = edad
        if genero:
            self.genero = genero
        if direccion:
            self.direccion = direccion
        if telefono:
            self.telefono = telefono
        if grupo_sanguineo:
            self.grupo_sanguineo = grupo_sanguineo