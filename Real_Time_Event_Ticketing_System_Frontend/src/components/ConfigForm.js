import React, { useState } from 'react';
import axios from 'axios';

function ConfigForm({ initialConfig, onSave, onCancel }) {
  const [newConfig, setNewConfig] = useState(initialConfig);
  const [error, setError] = useState(null);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewConfig({ ...newConfig, [name]: parseInt(value, 10) });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:8080/api/config/save', {
        totalTickets: newConfig.totalTickets,
        ticketReleaseRate: newConfig.releaseRate,
        customerRetrievalRate: newConfig.buyingRate,
        maxTicketCapacity: newConfig.maxCapacity,
      });
      console.log('Save successful:', response.data);
      onSave(newConfig); // Callback to notify parent component
    } catch (err) {
      console.error('Error saving configuration:', err);
      setError('Failed to save configuration. Please try again later.');
    }
  };

  return (
    <div className="modal-content">
      <h3>Create New Configuration</h3>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Total Tickets:</label>
          <input
            type="number"
            name="totalTickets"
            value={newConfig.totalTickets}
            onChange={handleInputChange}
          />
        </div>
        <div className="form-group">
          <label>Release Rate:</label>
          <input
            type="number"
            name="releaseRate"
            value={newConfig.releaseRate}
            onChange={handleInputChange}
          />
        </div>
        <div className="form-group">
          <label>Buying Rate:</label>
          <input
            type="number"
            name="buyingRate"
            value={newConfig.buyingRate}
            onChange={handleInputChange}
          />
        </div>
        <div className="form-group">
          <label>Max Capacity:</label>
          <input
            type="number"
            name="maxCapacity"
            value={newConfig.maxCapacity}
            onChange={handleInputChange}
          />
        </div>
        <button type="submit" className="btn btn-primary">
          Save
        </button>
        <button type="button" className="btn btn-secondary" onClick={onCancel}>
          Cancel
        </button>
      </form>
      {error && <p style={{ color: 'red' }}>{error}</p>}
    </div>
  );
}

export default ConfigForm;
