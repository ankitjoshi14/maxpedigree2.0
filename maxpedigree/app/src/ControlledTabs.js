import React from 'react';
import './ControlledTabs.css';
//import HCluster from './HCluster';
import Pedigree from './Pedigree';
import Pca from './pca';
import Cultivar from './Cultivar';
import HCluster from './HCluster';
import Pagination from "./Pagination";
import Lightbox from "./lightbox";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import { showLightbox, downloadFile } from "./pedigreeaction";
import FileUploader from './FileUploader';


class ControlledTabs extends React.Component {
  constructor(props) {

    super();
    //  console.log("controlled tab height", window.innerHeight)
    this.state = {
      tabdim: {
        width: window.innerWidth,
        height: window.innerHeight - 60
      },
      controlledTabdim: {
        width: window.innerWidth,
        height: window.innerHeight

      },
      navBardim: {
        width: window.innerWidth,
        height: 55
      },
      showLightbox: false
    };




    this.openTab = this.openTab.bind(this);
    this.updateDimensions = this.updateDimensions.bind(this);
    this.openpopup = this.openpopup.bind(this);
    // Bind the handleSelect function already here (not in the render function)

  }

  /**
   * Calculate & Update state of new dimensions
   */
  updateDimensions() {
    if (window.innerWidth < 500) {
      this.setState({ width: 450, height: 102 });
    } else {
      let update_width = window.innerWidth - 100;
      let update_height = Math.round(update_width / 4.4);
      this.setState({ width: update_width, height: update_height });
    }
  }

  /**
   * Add event listener
   */
  componentDidMount() {
    this.openTab('data', null);
    //  this.updateDimensions();
    // window.addEventListener("resize", this.updateDimensions);
  }

  /**
   * Remove event listener
   */
  componentWillUnmount() {
    // window.removeEventListener("resize", this.updateDimensions);
  }



  openTab(tabName) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
      tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablinks");

    for (i = 0; i < tablinks.length; i++) {
      tablinks[i].className = tablinks[i].className.replace(" activetab", "");
    }
    document.getElementById(tabName).style.display = "block";
    document.getElementById(tabName).className += " active";
    document.getElementById(tabName + "Tab").className += " activetab";



  }

  openpopup(e) {
    console.log("openpopup");
    this.setState((state, props) => {
      return { showlighbox: true }
    });
  }

  render() {
    console.log("render controltabs");
    return (
      <div className="controlledtab" style={this.state.controlledTabdim}>

        <div className="Tab" style={this.state.navBardim}>
        
 
          <ul class="nav navbar-nav">
            <li role="presention" className="tablinks" id="dataTab" onClick={(e) => this.openTab('data')}><a>Home</a></li>
            <li role="presentation" className="tablinks" id ="pedigreeTab" onClick={(e) => this.openTab('pedigree')}><a >Pedigree</a></li>
            <li role="presentation"  className="tablinks" id="pcaTab" onClick={(e) => this.openTab('pca')}><a  >Principal Components</a></li>
            <li role="presentation" className="tablinks" id="hcTab" onClick={(e) => this.openTab('hc')}><a >Hierarchical Cluster</a></li>
          </ul>

          <ul class="nav navbar-nav navbar-right">
            <div className="form-group col-md-10">
              <button type="button" onClick={() => this.props.showLightbox(true)} class="btn btn-danger btn-lg">Analyze Data</button>

            </div>
            <div className="form-group col-md-3"></div>
          </ul>
        </div>

        <Lightbox openTabfromParent={this.openTab} />

        <div id="data" className="tabcontent">
          <Cultivar dim={this.state.tabdim} openTabfromParent={this.openTab} />

        </div>

        <div id="pedigree" className="tabcontent">
          <Pedigree dim={this.state.tabdim} />

        </div>

        <div id="pca" className="tabcontent">
          <Pca dim={this.state.tabdim} />
        </div>

        <div id="hc" className="tabcontent">

          <HCluster dim={this.state.tabdim} />
        </div>

      </div>
    );
  }

}

function mapDispatchToProps(dispatch) {
  // console.log("mapDispatchToProps controlled tabs");
  return bindActionCreators({ showLightbox: showLightbox, downloadFile: downloadFile }, dispatch)
};


function mapStateToProps(state) {
  //  console.log("mapstatetoprops controlled tabs");
  //  console.log(state);
  return {
  };
}

export default connect(mapStateToProps, mapDispatchToProps)(ControlledTabs);
