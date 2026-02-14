import React, { useState, useEffect } from 'react';
import './App.css';

/**
 * Interfaz de Checkout de Despacho.
 * Replica la experiencia de usuario de seleccion de slots de Walmart.
 * * @author Mauricio Gomez
 * @version 1.2
 */
function App() {
  // --- 1. PRIMERO DEFINIMOS LA LOGICA DE DIAS ---
  const days = Array.from({ length: 7 }, (_, i) => {
    const d = new Date();
    d.setDate(d.getDate() + i);
    
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    
    const dateStr = `${year}-${month}-${day}`; 
    
    return {
      label: i === 0 ? 'Hoy' : d.toLocaleDateString('es-ES', { weekday: 'short' }).replace('.', ''),
      date: dateStr,
      display: `${day}/${month}`
    };
  });

  // --- 2. AHORA SI PODEMOS USAR 'days' EN LOS ESTADOS ---
  const [windows, setWindows] = useState([]);
  const [selectedDay, setSelectedDay] = useState(days[0].date);
  const [selectedWindowId, setSelectedWindowId] = useState(null);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState({ text: '', type: '' });

  const API_URL = "http://localhost:8080/api/v1/dispatch";
  const currentZone = "zone-1";

  const fetchWindows = async () => {
    try {
      const response = await fetch(`${API_URL}/windows`);
      const data = await response.json();
      setWindows(data);
      setLoading(false);
    } catch (error) {
      console.error("Error:", error);
      setLoading(false);
    }
  };

  /**
 * Cambia el dia seleccionado y limpia el estado de la seleccion previa.
 * * @param {string} dateStr Fecha en formato YYYY-MM-DD
 */
const handleDayChange = (dateStr) => {
  setSelectedDay(dateStr);
  setSelectedWindowId(null); // Limpia la seleccion al cambiar de dia
  setMessage({ text: '', type: '' }); // Limpia mensajes de error/exito previos
};

/**
 * Determina si el boton de reserva debe estar deshabilitado.
 * Verifica si hay una ventana seleccionada y si tiene capacidad.
 * * @return {boolean}
 */
const isReserveDisabled = () => {
  if (!selectedWindowId) return true;
  const selectedWin = windows.find(w => w.id === selectedWindowId);
  return !selectedWin || selectedWin.capacityByZone[currentZone] <= 0;
};
  useEffect(() => { fetchWindows(); }, []);

  const handleReserve = async () => {
    if (!selectedWindowId) return;
    try {
      const response = await fetch(`${API_URL}/reserve/${selectedWindowId}?zoneId=${currentZone}`, {
        method: 'POST'
      });
      const resultText = await response.text();
      if (response.ok) {
        setMessage({ text: "¡Reserva confirmada!", type: 'success' });
        fetchWindows();
      } else {
        const errorData = JSON.parse(resultText);
        setMessage({ text: errorData.message, type: 'error' });
      }
    } catch (error) {
      setMessage({ text: "Error de red", type: 'error' });
    }
  };

  /**
 * Verifica si un día específico tiene al menos un horario con capacidad disponible.
 * * @param {string} dateStr Fecha en formato YYYY-MM-DD
 * @return {boolean} true si el día está totalmente agotado
 */
const isDaySoldOut = (dateStr) => {
  // Filtramos todas las ventanas que pertenecen a ese día
  const dayWindows = windows.filter(w => w.date === dateStr);
  
  // Si no hay ventanas para ese día, no lo marcamos como agotado (puede ser que aún no cargan)
  if (dayWindows.length === 0) return false;

  // El día está agotado si TODAS sus ventanas tienen capacidad 0 en la zona actual
  return dayWindows.every(w => w.capacityByZone[currentZone] <= 0);
};

  return (
    <div className="checkout-container">
      <button className="back-button">← Volver</button>

      <div className="delivery-toggle">
        <button className="toggle-btn">Retiro Pickup</button>
        <button className="toggle-btn active">Despacho a domicilio</button>
      </div>

      <div className="customer-info">
        <div className="info-text">
          <strong>Mauricio Gomez</strong>
          <p>Avenida Central 123, Macul, Santiago</p>
        </div>
        <button className="change-btn">Cambiar</button>
      </div>

      <section className="date-selection">
  <h3>Elige tu despacho a domicilio: fecha y hora</h3>
  <div className="days-row">
    {days.map(day => {
      const soldOut = isDaySoldOut(day.date); // Verificamos si el día está agotado
      
      return (
        <div 
          key={day.date} 
          className={`day-item 
            ${selectedDay === day.date ? 'selected' : ''} 
            ${soldOut ? 'day-disabled' : ''}`
          }
          onClick={() => !soldOut && handleDayChange(day.date)} // Si está agotado, no se puede hacer clic
        >
          <span className="day-label">{day.label}</span>
          <div className="day-circle">
            {soldOut ? 'X' : day.display}
          </div>
          {soldOut && <span className="status-label">Agotado</span>}
        </div>
      );
    })}
  </div>
</section>

    <section className="time-slots">
      {loading ? <p>Cargando...</p> : (
        windows.filter(w => w.date === selectedDay).map(win => (
          <div key={win.id} className={`slot-item ${win.capacityByZone[currentZone] <= 0 ? 'slot-disabled' : ''}`}>
            <label>
              <input 
                type="radio" 
                name="slot" 
                checked={selectedWindowId === win.id}
                onChange={() => {
                  setSelectedWindowId(win.id);
                  setMessage({ text: '', type: '' }); // Limpia error al elegir otro horario
                }}
                disabled={win.capacityByZone[currentZone] <= 0}
              />
              <span className="slot-time">{win.start} - {win.end}</span>
            </label>
            <span className="slot-price">
              {win.capacityByZone[currentZone] > 0 ? '$2.990' : 'Agotado'}
            </span>
          </div>
        ))
      )}
    </section>

    {message.text && <div className={`alert ${message.type}`}>{message.text}</div>}

    <button 
      className="reserve-final-btn" 
      onClick={handleReserve}
      // USAMOS LA NUEVA FUNCION DE VALIDACION ACA:
      disabled={isReserveDisabled()}
    >
      {selectedWindowId && isReserveDisabled() ? 'Sin Cupos' : 'Reservar Horario'}
    </button>
    </div>
  );
}

export default App;