import React, { useState } from 'react';
import DefaultConfig from './DefaultConfig';
import ConfigForm from './ConfigForm';
import './Modal.css';

function ConfigPopup({ currentConfig, onClose, onSave }) {
  const [isCreating, setIsCreating] = useState(false);

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
