import React, { Component } from 'react';
import './App.css';

import ControlledTabs from './ControlledTabs';

class App extends Component {
  constructor(props){
    super(props);
    this.state = {
    
    }
    console.log(" app height", window.innerHeight)
  }
  render() {
    return (
      <div className="App">
          <ControlledTabs/>
      </div>
    );
  }
}

export default App;
