"""
Módulo de Registro de Salud - Signos vitales y monitoreo
"""
from typing import Dict, Any
from datetime import datetime
import random


class RegistroSalud:
    """Clase para registros de salud y signos vitales"""
    
    def __init__(self, id_registro: str, id_paciente: str,
                 presion_sistolica: int = 0, presion_diastolica: int = 0,
                 frecuencia_cardiaca: int = 0, temperatura: float = 0.0,
                 saturacion_oxigeno: int = 0):
        self.id_registro = id_registro
        self.id_paciente = id_paciente
        self.presion_sistolica = presion_sistolica
        self.presion_diastolica = presion_diastolica
        self.frecuencia_cardiaca = frecuencia_cardiaca
        self.temperatura = temperatura
        self.saturacion_oxigeno = saturacion_oxigeno
        self.fecha_registro = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        self.observaciones = ""
    
    def to_dict(self) -> Dict[str, Any]:
        """Convierte el registro a diccionario"""
        return {
            'id_registro': self.id_registro,
            'id_paciente': self.id_paciente,
            'presion_sistolica': self.presion_sistolica,
            'presion_diastolica': self.presion_diastolica,
            'frecuencia_cardiaca': self.frecuencia_cardiaca,
            'temperatura': self.temperatura,
            'saturacion_oxigeno': self.saturacion_oxigeno,
            'fecha_registro': self.fecha_registro,
            'observaciones': self.observaciones
        }
    
    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> 'RegistroSalud':
        """Crea un registro desde un diccionario"""
        registro = cls(
            id_registro=data['id_registro'],
            id_paciente=data['id_paciente'],
            presion_sistolica=data.get('presion_sistolica', 0),
            presion_diastolica=data.get('presion_diastolica', 0),
            frecuencia_cardiaca=data.get('frecuencia_cardiaca', 0),
            temperatura=data.get('temperatura', 0.0),
            saturacion_oxigeno=data.get('saturacion_oxigeno', 0)
        )
        registro.fecha_registro = data.get('fecha_registro', registro.fecha_registro)
        registro.observaciones = data.get('observaciones', '')
        return registro
    
    @classmethod
    def generar_aleatorio(cls, id_registro: str, id_paciente: str) -> 'RegistroSalud':
        """Genera un registro de salud con valores aleatorios simulados"""
        return cls(
            id_registro=id_registro,
            id_paciente=id_paciente,
            presion_sistolica=random.randint(110, 140),
            presion_diastolica=random.randint(70, 90),
            frecuencia_cardiaca=random.randint(60, 100),
            temperatura=round(random.uniform(36.0, 37.5), 1),
            saturacion_oxigeno=random.randint(95, 100)
        )
    
    def evaluar_estado(self) -> str:
        """Evalúa el estado general basado en los signos vitales"""
        alertas = []
        
        if self.presion_sistolica > 140 or self.presion_diastolica > 90:
            alertas.append("Presión arterial elevada")
        elif self.presion_sistolica < 90 or self.presion_diastolica < 60:
            alertas.append("Presión arterial baja")
        
        if self.frecuencia_cardiaca > 100:
            alertas.append("Frecuencia cardíaca elevada")
        elif self.frecuencia_cardiaca < 60:
            alertas.append("Frecuencia cardíaca baja")
        
        if self.temperatura > 37.5:
            alertas.append("Temperatura elevada (fiebre)")
        elif self.temperatura < 36.0:
            alertas.append("Temperatura baja (hipotermia)")
        
        if self.saturacion_oxigeno < 95:
            alertas.append("Saturación de oxígeno baja")
        
        if not alertas:
            return "Normal - Todos los signos vitales en rango saludable"
        else:
            return "Alerta: " + ", ".join(alertas)
    
    def agregar_observacion(self, observacion: str):
        """Agrega una observación al registro"""
        self.observaciones = observacion