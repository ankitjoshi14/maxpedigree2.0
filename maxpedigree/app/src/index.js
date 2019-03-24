import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import { createStore, applyMiddleware } from "redux";
import { Provider } from "react-redux";
import 'bootstrap/dist/css/bootstrap.min.css';
import thunk from "redux-thunk";

let initialState = {
  selectedCultivars: [],
  count: [],
  cultivarResponse: null,
  showLightbox: false,
  cultivarswithPC: [],
  selectednodePC: null,
  selectednodeHCluster: null,
  selectednodePedigree: null,
  selectedAttributePedigree: null,
  selectedAttributePC: null,
  selectedAttributeHCluster: null,
  pedigreeroot: null,
  hclusterroot : null,
  uploadfileresponse : null,
  seachCriteria : {}

}
function reducer(state = initialState, action) {
  console.log("action called " + action.type)
  switch (action.type) {
    case "select":
      return Object.assign({}, state, {
        selectedCultivars: state.selectedCultivars.concat(action.payload)
      });
      
      case "selectall":
      return Object.assign({}, state, {
        selectedCultivars: action.payload
      });
    case "deselect":
      console.log("index", state.selectedCultivars.indexOf(action.payload))
      let index = state.selectedCultivars.indexOf(action.payload);
      // making copy of selectedcultivars
      let newarray = state.selectedCultivars.splice(0);
      newarray.splice(index, 1)

      console.log(state.selectedCultivars)
      return Object.assign({}, state, {
        selectedCultivars: newarray
      });
      case "deselectall":
      alert("deselect called")
      return Object.assign({}, state, {
        selectedCultivars: []
      });
    case "getall":
      console.log("getall");
      return Object.assign({}, state, {
        cultivarResponse: action.payload
      });

      case "setSearchCriteria":
       
      return Object.assign({}, state, {
        seachCriteria: action.payload
      });

    case "pca":

      return Object.assign({}, state, {
        cultivarswithPC: action.payload
      });

      case "pedigree":
      return Object.assign({}, state, {
        pedigreeroot: action.payload
      });

      case "hcluster":
      return Object.assign({}, state, {
        hclusterroot: action.payload
      });

    case "selectednodePC":
      return Object.assign({}, state, {
        selectednodePC: action.payload
      });

    case "selectednodePedigree":
      return Object.assign({}, state, {
        selectednodePedigree: action.payload
      });

      case "selectednodeHCluster":
      return Object.assign({}, state, {
        selectednodeHCluster: action.payload
      });
      
    case "selectedAttributePC":
      return Object.assign({}, state, {
        selectedAttributePC: action.payload
      });

    case "selectedAttributePedigree":
      return Object.assign({}, state, {
        selectedAttributePedigree: action.payload
      });

      case "selectedAttributeHCluster":
      return Object.assign({}, state, {
        selectedAttributeHCluster: action.payload
      });
    case "lightbox":
      console.log("lightbox");
      return Object.assign({}, state, {
        showLightbox: action.payload
      });
      case "uploadfile":
      return Object.assign({}, state, {
        uploadfileresponse: action.payload
      });
    default:
      return state;
  }
}

let store = createStore(reducer, initialState, applyMiddleware(thunk));


let provider = <Provider store={store}><App /></Provider>;

ReactDOM.render(provider, document.getElementById('root'));

