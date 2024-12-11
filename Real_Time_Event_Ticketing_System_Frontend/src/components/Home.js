import React, { useState, useEffect } from 'react';
import ConfigPopup from './ConfigPopup';
import './App.css';

/**
 * Home component that displays the ticket counter and manages the ticketing process.
 *
 * @component
 * @param {Object} props - The component props.
 * @param {Object} props.configuration - The configuration object.
 * @param {Function} props.onUpdateConfiguration - Function to update the configuration.
 * @example
 * const configuration = { totalTickets: 100, releaseRate: 10, buyingRate: 5, maxCapacity: 50 };
 * const onUpdateConfiguration = (newConfig) => { ... };
 * return (
 *   <Home configuration={configuration} onUpdateConfiguration={onUpdateConfiguration} />
 * )
 */
function Home({ configuration, onUpdateConfiguration }) {
  const [counter, setCounter] = useState(0);
  const [isRunning, setIsRunning] = useState(false);
  const [showPopup, setShowPopup] = useState(false);

  useEffect(() => {
    // Initialize WebSocket connection
    const socket = new WebSocket('ws://localhost:8080/ws/ticket'); // Update the URL to match your backend's WebSocket endpoint

    // Handle incoming messages
    socket.onmessage = (event) => {
      const message = event.data;

      if (!isNaN(message)) {
        // If the message is a number, update the counter
        setCounter(Number(message));
      } else {
        // Otherwise, print the message to the console
        console.log(message);
      }
    };

  }, []); // Empty dependency array ensures this effect runs only once

  /**
   * Function to start the ticketing process by sending a request to the backend.
   *
   * @async
   */
  const startTicketing = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/ticketing/start', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      const result = await response.text();
      console.log(result); // Display the backend response
      if (response.ok) {
        console.log('Ticketing process started successfully.');
        setIsRunning(true); // Disable Start button and enable Stop button
        setCounter(0);
      }
    } catch (error) {
      console.error('Error starting ticketing:', error);
    }
  };

  /**
   * Function to stop the ticketing process by sending a request to the backend.
   *
   * @async
   */
  const stopTicketing = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/ticketing/stop', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      const result = await response.text();
      console.log(result); // Display the backend response
      if (response.ok) {
        console.log('Ticketing process stopped successfully.');
        setIsRunning(false); // Enable Start button and disable Stop button
      } else {
        console.error('Error stopping ticketing: Backend returned a failure response');
      }
    } catch (error) {
      console.error('Error stopping ticketing:', error);
    }
  };

  return (
    <div className="App">
      <h1>Welcome to Real-Time Event Ticketing System</h1>
      <div className="counter-display">
        <h1>{counter}</h1>
      </div>
      <div className="controls">
        <div className="button-group">
          <button
            className="btn btn-primary"
            onClick={startTicketing}
            disabled={isRunning} // Start button disabled if running
          >
            Start
          </button>
          <button
            className="btn btn-danger"
            onClick={stopTicketing}
            disabled={!isRunning} // Stop button disabled if not running
          >
            Stop
          </button>
        </div>
        <button
          className="btn btn-success config-button"
          onClick={() => setShowPopup(true)}
        >
          Configuration
        </button>
      </div>
      <div className="configuration">
        <h3>Current Configuration</h3>
        <ul>
          <li>Total Tickets: {configuration.totalTickets}</li>
          <li>Release Rate : {configuration.releaseRate}</li>
          <li>Buying Rate  : {configuration.buyingRate}</li>
          <li>Max Capacity : {configuration.maxCapacity}</li>
        </ul>
      </div>
      {showPopup && (
        <ConfigPopup
          currentConfig={configuration}
          onClose={() => setShowPopup(false)}
          onSave={onUpdateConfiguration}
        />
      )}
    </div>
  );
}

export default Home;
