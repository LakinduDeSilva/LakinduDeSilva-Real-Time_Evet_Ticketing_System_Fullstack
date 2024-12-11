import React, { useState } from 'react';
import axios from 'axios';

/**
 * ConfigForm component that allows users to create or update configuration settings.
 *
 * @component
 * @param {Object} props - The component props.
 * @param {Object} props.initialConfig - The initial configuration object.
 * @param {Function} props.onSave - Function to save the new configuration.
 * @param {Function} props.onCancel - Function to cancel the configuration process.
 * @example
 * const initialConfig = { totalTickets: 100, releaseRate: 10, buyingRate: 5, maxCapacity: 50 };
 * const onSave = (newConfig) => { ... };
 * const onCancel = () => { ... };
 * return (
 *   <ConfigForm initialConfig={initialConfig} onSave={onSave} onCancel={onCancel} />
 * )
 */
function ConfigForm({ initialConfig, onSave, onCancel }) {
  const [newConfig, setNewConfig] = useState(initialConfig);
  const [error, setError] = useState(null);

  /**
   * Handles input change events for the form fields.
   *
   * @param {Object} e - The event object.
   */
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewConfig({ ...newConfig, [name]: parseInt(value, 10) });
  };

  /**
   * Handles form submission to save the new configuration.
   *
   * @param {Object} e - The event object.
   * @async
   */
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:8080/api/config/save', {
        totalTickets: newConfig.totalTickets,
        ticketReleaseRate: newConfig.releaseRate,
        customerRetrievalRate: newConfig.buyingRate,
        maxTicketCapacity: newConfig.maxCapacity,
      });
      console.log(response.data);
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
        {error && <p className="error">{error}</p>}
        <div className="form-buttons">
          <button type="submit" className="btn btn-primary">Save</button>
          <button type="button" className="btn btn-secondary" onClick={onCancel}>Cancel</button>
        </div>
      </form>
    </div>
  );
}

export default ConfigForm;