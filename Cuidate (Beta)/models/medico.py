"""
Módulo de Médico - Hereda de Usuario
"""
from typing import Dict, Any, List
from models.usuario import Usuario


class Medico(Usuario):
    """Clase para médicos del sistema"""
    
    def __init__(self, id_usuario: str, nombre: str, apellido: str,
                 cedula: str, correo: str, contrasena: str,
                 especialidad: str = "", registro_medico: str = "",
                 anos_experiencia: int = 0):
        super().__init__(id_usuario, nombre, apellido, cedula,
                        correo, contrasena, 'medico')
        self.especialidad = especialidad
        self.registro_medico = registro_medico
        self.anos_experiencia = anos_experiencia
        self.pacientes_asignados: List[str] = []
        self.consultas_atendidas: List[str] = []
    
    def to_dict(self) -> Dict[str, Any]:
        """Convierte el médico a diccionario"""
        data = super().to_dict()
        data.update({
            'especialidad': self.especialidad,
            'registro_medico': self.registro_medico,
            'anos_experiencia': self.anos_experiencia,
            'pacientes_asignados': self.pacientes_asignados,
            'consultas_atendidas': self.consultas_atendidas
        })
        return data
    
    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> 'Medico':
        """Crea un médico desde un diccionario"""
        medico = cls(
            id_usuario=data['id_usuario'],
            nombre=data['nombre'],
            apellido=data['apellido'],
            cedula=data['cedula'],
            correo=data['correo'],
            contrasena=data['contrasena'],
            especialidad=data.get('especialidad', ''),
            registro_medico=data.get('registro_medico', ''),
            anos_experiencia=data.get('anos_experiencia', 0)
        )
        medico.pacientes_asignados = data.get('pacientes_asignados', [])
        medico.consultas_atendidas = data.get('consultas_atendidas', [])
        return medico
    
    def asignar_paciente(self, id_paciente: str):
        """Asigna un paciente al médico"""
        if id_paciente not in self.pacientes_asignados:
            self.pacientes_asignados.append(id_paciente)
    
    def registrar_consulta_atendida(self, id_consulta: str):
        """Registra una consulta como atendida"""
        if id_consulta not in self.consultas_atendidas:
            self.consultas_atendidas.append(id_consulta)
    
    def actualizar_datos_profesionales(self, especialidad: str = None,
                                      registro_medico: str = None,
                                      anos_experiencia: int = None):
        """Actualiza datos profesionales del médico"""
        if especialidad:
            self.especialidad = especialidad
        if registro_medico:
            self.registro_medico = registro_medico
        if anos_experiencia is not None:
            self.anos_experiencia = anos_experiencia