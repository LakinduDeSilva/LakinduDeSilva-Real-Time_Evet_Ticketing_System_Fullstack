import React, { useState } from 'react';
import axios from 'axios';

function DefaultConfig({ onLoad }) {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const loadConfiguration = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await axios.get('http://localhost:8080/api/config/load'); // Adjust this based on your backend
      const { data } = response;

      // Parse response and create a configuration object
      const config = {
        totalTickets: data.totalTickets,
        releaseRate: data.ticketReleaseRate,
        buyingRate: data.customerRetrievalRate,
        maxCapacity: data.maxTicketCapacity,
      };
      console.log(config)
      // console.log("totalTickets:",data.totalTickets)
      // console.log("releaseRate:", data.ticketReleaseRate)
      // console.log("buyingRate:" ,data.customerRetrievalRate)
      // console.log("maxCapacity:", data.maxTicketCapacity)

      onLoad(config);
    } catch (err) {
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
