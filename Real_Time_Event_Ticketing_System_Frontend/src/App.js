import React, { useState } from 'react';
import Home from './components/Home';

/**
 * App component that manages the configuration state and passes it to the Home component.
 *
 * @component
 * @example
 * return (
 *   <App />
 * )
 */
function App() {
  const [configuration, setConfiguration] = useState({
    totalTickets: 0,
    releaseRate: 0,
    buyingRate: 0,
    maxCapacity: 0,
  });

  return (
    <Home
      configuration={configuration}
      /**
       * Function to update the configuration state.
       * 
       * @param {Object} newConfig - The new configuration object.
       */
      onUpdateConfiguration={(newConfig) => setConfiguration(newConfig)}
    />
  );
}

export default App;