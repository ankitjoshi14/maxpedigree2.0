import React from 'react';
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import { setSearchCriteria, deselectcultivarall, selectcultivarall } from "./pedigreeaction";
import "./Searchbar.css"
import FileUploader from './FileUploader';
import FileDownloader from './FileDownloader';

class Searchbar extends React.Component {
    constructor(props) {

        super();
        //  console.log("controlled tab height", window.innerHeight)
        this.state = {
            tabdim: {
                width: window.innerWidth
            },
            controlledTabdim: {
                width: window.innerWidth,
                height: window.innerHeight

            },
            cultivar: "",
            showLightbox: false,
            searchCriteria: {},
            showSelectFilteredButton: false
        };

        this.handleCutivarChange = this.handleCutivarChange.bind(this);
        this.handleCountryChange = this.handleCountryChange.bind(this);
        this.handleMGChange = this.handleCutivarChange.bind(this);
        this.handleYearChange = this.handleYearChange.bind(this);
        this.handleSearch = this.handleSearch.bind(this);
        this.handlefiltered = this.handlefiltered.bind(this);
    }

    handleCutivarChange(event) {
        let inputValue = event.target.value;  // Cache the value of e.target.value
        let searchCriteria = Object.assign({}, this.state.searchCriteria);    //creating copy of object
        searchCriteria.cultivar = inputValue;                        //updating value
        this.setState((state, props) => {
            return { searchCriteria: searchCriteria }
        });
    }

    handleMGChange(event) {
        let inputValue = event.target.value;  // Cache the value of e.target.value
        let searchCriteria = Object.assign({}, this.state.searchCriteria);    //creating copy of object
        searchCriteria.maturityGroup = inputValue;                        //updating value
        this.setState((state, props) => {
            return { searchCriteria: searchCriteria }
        });
    }
    handleYearChange(event) {
        let inputValue = event.target.value;  // Cache the value of e.target.value
        let searchCriteria = Object.assign({}, this.state.searchCriteria);    //creating copy of object
        searchCriteria.year = inputValue;                        //updating value
        this.setState((state, props) => {
            return { searchCriteria: searchCriteria }
        });
    }
    handleCountryChange(event) {
        let inputValue = event.target.value;  // Cache the value of e.target.value
        let searchCriteria = Object.assign({}, this.state.searchCriteria);    //creating copy of object
        searchCriteria.country = inputValue;                        //updating value
        this.setState((state, props) => {
            return { searchCriteria: searchCriteria }
        });
    }

    handleSearch() {
        this.props.setSearchCriteria(this.state.searchCriteria);
    }

    handlefiltered() {
        const selectall = this.state.showSelectFilteredButton
        console.log("selectall", selectall);
        if (selectall) {
            this.props.selectcultivarall(this.state.searchCriteria);
        } else {
            this.props.deselectcultivarall();
        }
        this.setState((state, props) => {
            return { showSelectFilteredButton: !selectall }
        });
    }

    componentDidUpdate(prevProps, prevState) {
        // only update chart if the data has changed
        if (prevProps.selectedCultivars !== this.props.selectedCultivars) {
            if (this.props.selectedCultivars.length > 12) {
                alert("too many records");
                this.setState((state, props) => {
                    return { showSelectFilteredButton: true }
                });
                this.props.deselectcultivarall();


            }
        }
    }

    render() {
        console.log(window.innerWidth);
        return (
            <div id="searchbar" style={this.props.dim}>
                <form>
                    <div className="form-row">
                        <div className="form-group col-md-2">
                            <label htmlFor="cultivar" className="col-form-label">Cultivar</label>
                            <input className="form-control" type="text" id="cultivar" onChange={this.handleCutivarChange} value={this.state.searchCriteria.cultivar} placeholder="PI Number or Name or Male or Female" />

                        </div>
                        <div className="form-group col-md-1">
                            <label htmlFor="maturitygrp" className="col-form-label">Maturity group</label>
                            <input className="form-control" type="text" id="maturityGrp" onChange={this.handleMGChange} value={this.state.searchCriteria.maturityGroup} placeholder="Maturity Group" />
                        </div>
                        <div className="form-group col-md-1">
                            <label htmlFor="year" className="col-form-label">Year</label>
                            <input className="form-control" type="text" id="year" onChange={this.handleYearChange} value={this.state.searchCriteria.year} placeholder="year" />
                        </div>
                        <div className="form-group col-md-2">
                            <label htmlFor="origin" className="col-form-label">Origin</label>
                            <input className="form-control" type="text" id="origin" onChange={this.handleCountryChange} value={this.state.searchCriteria.country} placeholder="state or country" />
                        </div>
                        <div className="col-md-1" >
                            <button type="button" className="btn btn-primary searchbarbutton" onClick={this.handleSearch} id="search">Search </button>
                        </div>
                        <div className="col-md-1" >
                            <button type="button" className="btn btn-warning searchbarbutton" id="resetAll">Reset All</button>
                        </div>

                        <div className="col-md-2" >
                           <FileDownloader/>
                        </div>
                        <div className="col-md-2" >
                            <FileUploader />
                        </div>
                    </div>
                </form>
            </div>

        );
    }
}


function mapDispatchToProps(dispatch) {
    // console.log("mapDispatchToProps controlled tabs");
    return bindActionCreators({ setSearchCriteria: setSearchCriteria, selectcultivarall: selectcultivarall, deselectcultivarall: deselectcultivarall }, dispatch)
};


function mapStateToProps(state) {
    //  console.log("mapstatetoprops controlled tabs");
    //  console.log(state);
    return {
        selectedCultivars: state.selectedCultivars
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(Searchbar);