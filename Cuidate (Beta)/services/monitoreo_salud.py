"""
Servicio de Monitoreo de Salud - Registro de signos vitales
"""
import json
import os
import random
from typing import Dict, List, Optional
from models.registro_salud import RegistroSalud


class MonitoreoSalud:
    """Servicio para monitorear la salud de los pacientes"""
    
    def __init__(self, archivo_datos: str = "data/registros.json"):
        self.archivo_datos = archivo_datos
        self.registros: Dict[str, RegistroSalud] = {}
        self._asegurar_directorio()
        self.cargar_registros()
    
    def _asegurar_directorio(self):
        """Crea el directorio data si no existe"""
        directorio = os.path.dirname(self.archivo_datos)
        if directorio and not os.path.exists(directorio):
            os.makedirs(directorio)
    
    def cargar_registros(self):
        """Carga registros desde el archivo JSON"""
        if os.path.exists(self.archivo_datos):
            try:
                with open(self.archivo_datos, 'r', encoding='utf-8') as f:
                    datos = json.load(f)
                    for id_registro, data in datos.items():
                        self.registros[id_registro] = RegistroSalud.from_dict(data)
            except Exception as e:
                print(f"Error al cargar registros: {e}")
                self.registros = {}
    
    def guardar_registros(self):
        """Guarda registros en el archivo JSON"""
        try:
            datos = {id_r: r.to_dict() for id_r, r in self.registros.items()}
            with open(self.archivo_datos, 'w', encoding='utf-8') as f:
                json.dump(datos, f, indent=4, ensure_ascii=False)
        except Exception as e:
            print(f"Error al guardar registros: {e}")
    
    def crear_registro_aleatorio(self, id_paciente: str) -> RegistroSalud:
        """Crea un registro de salud con valores aleatorios simulados"""
        id_registro = f"REG{len(self.registros) + 1:05d}"
        registro = RegistroSalud.generar_aleatorio(id_registro, id_paciente)
        self.registros[id_registro] = registro
        self.guardar_registros()
        return registro
    
    def crear_registro_manual(self, id_paciente: str, presion_sistolica: int,
                            presion_diastolica: int, frecuencia_cardiaca: int,
                            temperatura: float, saturacion_oxigeno: int) -> RegistroSalud:
        """Crea un registro de salud con valores manuales"""
        id_registro = f"REG{len(self.registros) + 1:05d}"
        registro = RegistroSalud(
            id_registro=id_registro,
            id_paciente=id_paciente,
            presion_sistolica=presion_sistolica,
            presion_diastolica=presion_diastolica,
            frecuencia_cardiaca=frecuencia_cardiaca,
            temperatura=temperatura,
            saturacion_oxigeno=saturacion_oxigeno
        )
        self.registros[id_registro] = registro
        self.guardar_registros()
        return registro
    
    def obtener_registro(self, id_registro: str) -> Optional[RegistroSalud]:
        """Obtiene un registro por su ID"""
        return self.registros.get(id_registro)
    
    def obtener_registros_paciente(self, id_paciente: str) -> List[RegistroSalud]:
        """Obtiene todos los registros de un paciente"""
        return [r for r in self.registros.values() 
                if r.id_paciente == id_paciente]
    
    def obtener_ultimo_registro(self, id_paciente: str) -> Optional[RegistroSalud]:
        """Obtiene el último registro de un paciente"""
        registros = self.obtener_registros_paciente(id_paciente)
        if registros:
            return max(registros, key=lambda r: r.fecha_registro)
        return None
    
    def generar_consejos_salud(self) -> List[str]:
        """Genera consejos de salud aleatorios"""
        consejos = [
            "Bebe al menos 8 vasos de agua al día para mantenerte hidratado.",
            "Realiza al menos 30 minutos de ejercicio moderado diariamente.",
            "Duerme entre 7 y 8 horas cada noche para una recuperación óptima.",
            "Consume 5 porciones de frutas y verduras al día.",
            "Reduce el consumo de alimentos procesados y azúcares refinados.",
            "Practica técnicas de relajación como meditación o yoga.",
            "Mantén una postura correcta al sentarte y al caminar.",
            "Lava tus manos frecuentemente para prevenir enfermedades.",
            "Limita el tiempo frente a pantallas, especialmente antes de dormir.",
            "Realiza chequeos médicos preventivos regularmente.",
            "Evita el consumo excesivo de alcohol y tabaco.",
            "Mantén un peso saludable según tu edad y estatura.",
            "Cuida tu salud mental: habla con alguien si te sientes abrumado.",
            "Protege tu piel del sol usando protector solar.",
            "Mantén una rutina de higiene bucal adecuada."
        ]
        return random.sample(consejos, 5)
    
    def analizar_tendencias(self, id_paciente: str) -> Dict[str, str]:
        """Analiza las tendencias de salud de un paciente"""
        registros = self.obtener_registros_paciente(id_paciente)
        if len(registros) < 2:
            return {'mensaje': 'No hay suficientes registros para analizar tendencias'}
        
        # Ordenar por fecha
        registros_ordenados = sorted(registros, 
                                    key=lambda r: r.fecha_registro, 
                                    reverse=True)
        
        ultimo = registros_ordenados[0]
        anterior = registros_ordenados[1]
        
        analisis = {}
        
        # Analizar presión arterial
        if ultimo.presion_sistolica > anterior.presion_sistolica:
            analisis['presion'] = 'Aumentó'
        elif ultimo.presion_sistolica < anterior.presion_sistolica:
            analisis['presion'] = 'Disminuyó'
        else:
            analisis['presion'] = 'Se mantiene estable'
        
        # Analizar frecuencia cardíaca
        if ultimo.frecuencia_cardiaca > anterior.frecuencia_cardiaca:
            analisis['frecuencia'] = 'Aumentó'
        elif ultimo.frecuencia_cardiaca < anterior.frecuencia_cardiaca:
            analisis['frecuencia'] = 'Disminuyó'
        else:
            analisis['frecuencia'] = 'Se mantiene estable'
        
        # Analizar temperatura
        if ultimo.temperatura > anterior.temperatura:
            analisis['temperatura'] = 'Aumentó'
        elif ultimo.temperatura < anterior.temperatura:
            analisis['temperatura'] = 'Disminuyó'
        else:
            analisis['temperatura'] = 'Se mantiene estable'
        
        return analisis