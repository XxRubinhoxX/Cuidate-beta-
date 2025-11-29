"""
Servicio de Gestión de Consultas - Crear y gestionar consultas médicas
"""
import json
import os
from typing import Dict, List, Optional
from models.consulta import Consulta


class GestionConsultas:
    """Servicio para gestionar consultas médicas"""
    
    def __init__(self, archivo_datos: str = "data/consultas.json"):
        self.archivo_datos = archivo_datos
        self.consultas: Dict[str, Consulta] = {}
        self._asegurar_directorio()
        self.cargar_consultas()
    
    def _asegurar_directorio(self):
        """Crea el directorio data si no existe"""
        directorio = os.path.dirname(self.archivo_datos)
        if directorio and not os.path.exists(directorio):
            os.makedirs(directorio)
    
    def cargar_consultas(self):
        """Carga consultas desde el archivo JSON"""
        if os.path.exists(self.archivo_datos):
            try:
                with open(self.archivo_datos, 'r', encoding='utf-8') as f:
                    datos = json.load(f)
                    for id_consulta, data in datos.items():
                        self.consultas[id_consulta] = Consulta.from_dict(data)
            except Exception as e:
                print(f"Error al cargar consultas: {e}")
                self.consultas = {}
    
    def guardar_consultas(self):
        """Guarda consultas en el archivo JSON"""
        try:
            datos = {id_c: c.to_dict() for id_c, c in self.consultas.items()}
            with open(self.archivo_datos, 'w', encoding='utf-8') as f:
                json.dump(datos, f, indent=4, ensure_ascii=False)
        except Exception as e:
            print(f"Error al guardar consultas: {e}")
    
    def crear_consulta(self, id_paciente: str, id_medico: str,
                      motivo: str) -> Consulta:
        """Crea una nueva consulta"""
        id_consulta = f"CON{len(self.consultas) + 1:04d}"
        consulta = Consulta(
            id_consulta=id_consulta,
            id_paciente=id_paciente,
            id_medico=id_medico,
            motivo=motivo
        )
        self.consultas[id_consulta] = consulta
        self.guardar_consultas()
        return consulta
    
    def obtener_consulta(self, id_consulta: str) -> Optional[Consulta]:
        """Obtiene una consulta por su ID"""
        return self.consultas.get(id_consulta)
    
    def obtener_consultas_paciente(self, id_paciente: str) -> List[Consulta]:
        """Obtiene todas las consultas de un paciente"""
        return [c for c in self.consultas.values() 
                if c.id_paciente == id_paciente]
    
    def obtener_consultas_medico(self, id_medico: str) -> List[Consulta]:
        """Obtiene todas las consultas de un médico"""
        return [c for c in self.consultas.values() 
                if c.id_medico == id_medico]
    
    def obtener_consultas_pendientes_medico(self, id_medico: str) -> List[Consulta]:
        """Obtiene consultas pendientes de un médico"""
        return [c for c in self.consultas.values() 
                if c.id_medico == id_medico and c.es_pendiente()]
    
    def obtener_consultas_completadas_medico(self, id_medico: str) -> List[Consulta]:
        """Obtiene consultas completadas de un médico"""
        return [c for c in self.consultas.values() 
                if c.id_medico == id_medico and c.es_completada()]
    
    def actualizar_consulta(self, consulta: Consulta):
        """Actualiza una consulta en el sistema"""
        if consulta.id_consulta in self.consultas:
            self.consultas[consulta.id_consulta] = consulta
            self.guardar_consultas()
    
    def registrar_diagnostico(self, id_consulta: str, diagnostico: str,
                            tratamiento: str = "", observaciones: str = "") -> bool:
        """Registra el diagnóstico de una consulta"""
        consulta = self.obtener_consulta(id_consulta)
        if consulta:
            consulta.registrar_diagnostico(diagnostico, tratamiento, observaciones)
            self.actualizar_consulta(consulta)
            return True
        return False
    
    def cancelar_consulta(self, id_consulta: str, motivo: str = "") -> bool:
        """Cancela una consulta"""
        consulta = self.obtener_consulta(id_consulta)
        if consulta:
            consulta.cancelar_consulta(motivo)
            self.actualizar_consulta(consulta)
            return True
        return False
    
    def obtener_estadisticas_medico(self, id_medico: str) -> Dict[str, int]:
        """Obtiene estadísticas de consultas de un médico"""
        consultas = self.obtener_consultas_medico(id_medico)
        return {
            'total': len(consultas),
            'pendientes': len([c for c in consultas if c.estado == 'pendiente']),
            'en_proceso': len([c for c in consultas if c.estado == 'en_proceso']),
            'completadas': len([c for c in consultas if c.estado == 'completada']),
            'canceladas': len([c for c in consultas if c.estado == 'cancelada'])
        }