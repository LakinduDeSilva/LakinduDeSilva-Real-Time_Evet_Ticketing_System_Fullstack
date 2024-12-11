import React, { useState } from 'react';
import axios from 'axios';

function DefaultConfig({ onLoad }) {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  /**
     * Default configuration settings for the application.
     *
     * @module DefaultConfig
     */
  const loadConfiguration = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await axios.get('http://localhost:8080/api/config/load'); // Adjust this based on your backend
      const { data } = response;

    /**
     * Default configuration object.
     *
     * @type {Object}
     * @property {number} totalTickets - The total number of tickets available.
     * @property {number} releaseRate - The rate at which tickets are released.
     * @property {number} buyingRate - The rate at which tickets are bought.
     * @property {number} maxCapacity - The maximum capacity of the system.
     */
      const config = {
        totalTickets: data.totalTickets,
        releaseRate: data.ticketReleaseRate,
        buyingRate: data.customerRetrievalRate,
        maxCapacity: data.maxTicketCapacity,
      };

      console.log("Load Configuration Successfully")
      console.log(config)
      onLoad(config);

    } catch (err) {
    /**
     * Error message displayed when the configuration fails to load.
     *
     * @type {string}
     */
      console.error('Error loading configuration:', err);
      setError('Failed to load configuration. Please try again later.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <button className="btn btn-success" onClick={loadConfiguration} disabled={loading}>
        {loading ? 'Loading...' : 'Load '}
      </button>
      {error && <p style={{ color: 'red' }}>{error}</p>}
    </div>
  );
}

export default DefaultConfig;
