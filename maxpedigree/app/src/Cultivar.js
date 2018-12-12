
import React from 'react';
import round from "math-round";
import { getpedigreedata, pedigree } from "./pedigreeaction";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import "./Cultivar.css";
import Checkbox from "./checkbox";
import Pagination from "./Pagination";
import Searchbar from './Searchbar';


class Cultivar extends React.Component {
    constructor(props) {
        super(props);
        let { width, height } = this.props.dim
        var exampleItems = [...Array(150).keys()].map(i => ({ id: (i + 1), name: 'Item ' + (i + 1) }));

        // Don't call this.setState() here!
        this.state = {
            svgwidth: width,
            svgheight: height,
            showSelectFilteredButton: false,
            exampleItems: exampleItems,
            pageOfItems: []

        };
      //  this.props.getall(1, 20);
        this.headers = this.headers.bind(this);
        this.tablebody = this.tablebody.bind(this);
        this.onChangePage = this.onChangePage.bind(this);

    }

    onChangePage(pageOfItems) {
        // update state with new page of items
        this.setState({ pageOfItems: pageOfItems });
    }

    componentDidMount() {
        //  this.props.dispatch({
        //    type: "getall"
        // })

    }

    showPedigree(cultivarid) {
        this.props.pedigree(cultivarid);
        this.props.openTabfromParent("pedigree")
    }

    headers(columnName) {

        return (
            <th key={columnName}>
                {columnName}
            </th>

        );

    }

    tablebody(row) {
        const listitems = row.map((col) => <td>{col}</td>);
        return (
            <tr>
                {listitems}
            </tr>
        );
    }

    calculateDim(){
        const dim = this.props.dim;
        return{
            searchbar: {
            width: dim.width,
            height: 70

        },
        pagination: {
            width: dim.width,
            height: 50
        },
        
        table:{
            width:dim.width,
            height:dim.height-120
        }
    }
          
    }
    render() {
              //  if (this.props.pageOfItems == null) {
          //  return null;
    //    }
        const dim = this.calculateDim();

        // var first = this.props.pageOfItems[0];
        // let keys = Object.keys(this.props.pageOfItems[0]);
        //   const rows = this.props.cultivars.map((row) => this.tablebody);
       // <button type="button" className={this.state.showSelectFilteredButton ? 'show btn btn-info ' : 'hide'} id="exportData" onClick={this.handlefiltered}>Select Filtered</button>
       // <button type="button" className={!this.state.showSelectFilteredButton ? 'show btn btn-info ' : 'hide'} id="exportData" onClick={this.handlefiltered}>DeSelect Filtered</button>
        return (
            
            <div>
                <Searchbar dim={dim.searchbar} />
                    <div id="tablebodyDiv" style={dim.table}>
                        <table className=" table table-hover ">
                        <thead id="tableHeader">
                            <tr>
                                <th>      </th>
                                <th> 
                            
                         </th>
                                <th>CultivarID</th>
                                <th>Cultivar name</th>
                                <th>MG</th>
                                <th>country</th>
                                <th>year</th>
                                <th>stem termination</th>
                                <th>pubescence color</th>
                                <th>pubescence form</th>
                                <th>pubescence density</th>
                            </tr>
                        </thead>
                            <tbody id="tableBody">
                                {this.props.pageOfItems === null ? "" :this.props.pageOfItems.map((item, index) =>
                                    <tr key={item.cultivarId} ><td><Checkbox id={item.cultivarId} /></td>
                                        <td><button type="button" className="btn btn-primary" onClick={(e) => this.showPedigree(item.cultivarId)}>show pedigree</button></td>
                                        <td>{item.cultivarId}</td>
                                        <td>{item.cultivarName}</td>
                                        <td>{item.maturityGroup != null ? item.maturityGroup.value : ""}</td>
                                        <td>{item.country != null ? item.country.value : ""}</td>
                                        <td>{item.year}</td>
                                        <td>{item.stemTermination !=null ? item.stemTermination.value : ""}</td>
                                        <td>{item.pubescenceColor != null ? item.pubescenceColor.value : ""}</td>
                                        <td>{item.pubescenceForm != null ? item.pubescenceForm.value : ""}</td>
                                        <td>{item.pubescenceDensity != null ? item.pubescenceDensity.value : ""}</td>

                                    </tr>)}
                            </tbody>
                        </table></div>
                
                <Pagination initialPage={1} dim ={dim.pagination}/>
            </div>

        );
    }
}



function mapDispatchToProps(dispatch) {
    //   console.log("mapDispatchToProps cultivar")
    return bindActionCreators({ getall: getpedigreedata, pedigree: pedigree }, dispatch)
};


function mapStateToProps(state) {
    //   console.log("mapstatetoprops cultivar")
    //   console.log(state.cultivars)
    return {
        pageOfItems: state.cultivarResponse !=null ? state.cultivarResponse.cultivars : []
    }
};


export default connect(mapStateToProps, mapDispatchToProps)(Cultivar);