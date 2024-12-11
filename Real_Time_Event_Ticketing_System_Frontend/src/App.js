import React, { useState } from 'react';
import Home from './components/Home';

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
      onUpdateConfiguration={(newConfig) => setConfiguration(newConfig)}
    />
  );
}

export default App;
