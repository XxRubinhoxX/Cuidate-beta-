"""
Módulo de Consulta - Gestión de consultas médicas
"""
from typing import Dict, Any
from datetime import datetime


class Consulta:
    """Clase para manejar consultas médicas"""
    
    def __init__(self, id_consulta: str, id_paciente: str, id_medico: str,
                 motivo: str, estado: str = 'pendiente'):
        self.id_consulta = id_consulta
        self.id_paciente = id_paciente
        self.id_medico = id_medico
        self.motivo = motivo
        self.estado = estado  # 'pendiente', 'en_proceso', 'completada', 'cancelada'
        self.fecha_solicitud = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        self.fecha_atencion = None
        self.diagnostico = ""
        self.tratamiento = ""
        self.observaciones = ""
    
    def to_dict(self) -> Dict[str, Any]:
        """Convierte la consulta a diccionario"""
        return {
            'id_consulta': self.id_consulta,
            'id_paciente': self.id_paciente,
            'id_medico': self.id_medico,
            'motivo': self.motivo,
            'estado': self.estado,
            'fecha_solicitud': self.fecha_solicitud,
            'fecha_atencion': self.fecha_atencion,
            'diagnostico': self.diagnostico,
            'tratamiento': self.tratamiento,
            'observaciones': self.observaciones
        }
    
    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> 'Consulta':
        """Crea una consulta desde un diccionario"""
        consulta = cls(
            id_consulta=data['id_consulta'],
            id_paciente=data['id_paciente'],
            id_medico=data['id_medico'],
            motivo=data['motivo'],
            estado=data.get('estado', 'pendiente')
        )
        consulta.fecha_solicitud = data.get('fecha_solicitud', consulta.fecha_solicitud)
        consulta.fecha_atencion = data.get('fecha_atencion')
        consulta.diagnostico = data.get('diagnostico', '')
        consulta.tratamiento = data.get('tratamiento', '')
        consulta.observaciones = data.get('observaciones', '')
        return consulta
    
    def actualizar_estado(self, nuevo_estado: str):
        """Actualiza el estado de la consulta"""
        estados_validos = ['pendiente', 'en_proceso', 'completada', 'cancelada']
        if nuevo_estado in estados_validos:
            self.estado = nuevo_estado
            if nuevo_estado == 'en_proceso' and not self.fecha_atencion:
                self.fecha_atencion = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    
    def registrar_diagnostico(self, diagnostico: str, tratamiento: str = "",
                            observaciones: str = ""):
        """Registra el diagnóstico de la consulta"""
        self.diagnostico = diagnostico
        self.tratamiento = tratamiento
        self.observaciones = observaciones
        self.actualizar_estado('completada')
    
    def cancelar_consulta(self, motivo_cancelacion: str = ""):
        """Cancela la consulta"""
        self.actualizar_estado('cancelada')
        if motivo_cancelacion:
            self.observaciones = f"Cancelada: {motivo_cancelacion}"
    
    def es_pendiente(self) -> bool:
        """Verifica si la consulta está pendiente"""
        return self.estado == 'pendiente'
    
    def es_completada(self) -> bool:
        """Verifica si la consulta está completada"""
        return self.estado == 'completada'