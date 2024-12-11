import React, { useState } from 'react';
import DefaultConfig from './DefaultConfig';
import ConfigForm from './ConfigForm';
import './Modal.css';

/**
 * ConfigPopup component that displays a modal for configuration settings.
 *
 * @component
 * @param {Object} props - The component props.
 * @param {Object} props.currentConfig - The current configuration object.
 * @param {Function} props.onClose - Function to close the popup.
 * @param {Function} props.onSave - Function to save the new configuration.
 * @example
 * const currentConfig = { totalTickets: 100, releaseRate: 10, buyingRate: 5, maxCapacity: 50 };
 * const onClose = () => { ... };
 * const onSave = (newConfig) => { ... };
 * return (
 *   <ConfigPopup currentConfig={currentConfig} onClose={onClose} onSave={onSave} />
 * )
 */
function ConfigPopup({ currentConfig, onClose, onSave }) {
  const [isCreating, setIsCreating] = useState(false);

  /**
   * Handles loading the default configuration.
   *
   * @param {Object} defaultConfig - The default configuration object.
   */
  const handleDefaultConfig = (defaultConfig) => {
    onSave(defaultConfig);
    onClose();
  };

  return (
    <div className="modal">
      {!isCreating ? (
        <div className="modal-content">
          <h3>Configuration Settings</h3>
          <p>
            Click on Create to create a new configuration or Load to load from
            previous configuration.
          </p>
          <div className="modal-buttons">
            <button className="btn btn-primary" onClick={() => setIsCreating(true)}>
              Create
            </button>
            <DefaultConfig onLoad={handleDefaultConfig} />
          </div>
          <button className="btn btn-secondary" onClick={onClose}>
            Close
          </button>
        </div>
      ) : (
        <ConfigForm
          initialConfig={currentConfig}
          onSave={(newConfig) => {
            onSave(newConfig);
            onClose();
          }}
          onCancel={onClose}
        />
      )}
    </div>
  );
}

export default ConfigPopup;