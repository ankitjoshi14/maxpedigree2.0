import React from 'react';
import styles from './ControlledTabs.css';
import Pedigree from './Pedigree';
import Pca from './pca';
import Cultivar from './Cultivar';
import HCluster from './HCluster';
import Lightbox from "./lightbox";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import { showLightbox, downloadFile } from "./pedigreeaction";
import cx from 'classnames';


class ControlledTabs extends React.Component {
  constructor(props) {

    super();
    //  console.log("controlled tab height", window.innerHeight)
    this.state = {
      tabdim: {
        width: window.innerWidth-5, // need to figure out why -5 is needed to fit screen size
        height: window.innerHeight - 55
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
      <div className={styles.controlledtab} style={this.state.controlledTabdim}>
      <div className={styles.Tab} style={this.state.navBardim}>
        <nav className= "navbar navbar-dark navbar-expand-sm bg-dark" role = "navigation">
          <ul className = {cx("nav", "navbar-nav", "navbar-left", styles.navCustom)} >
          <li role="presention" className="tablinks active nav-item" id="dataTab" onClick={(e) => this.openTab('data')}><a class="nav-link">Home</a></li>
          <li role="presentation" className="tablinks nav-item" id ="pedigreeTab" onClick={(e) => this.openTab('pedigree')}><a class="nav-link">Pedigree</a></li>
          <li role="presentation"  className="tablinks nav-item" id="pcaTab" onClick={(e) => this.openTab('pca')}><a  class="nav-link">Principal Components</a></li>
          <li role="presentation" className="tablinks nav-item"  id="hcTab" onClick={(e) => this.openTab('hc')}><a class="nav-link">Hierarchical Cluster</a></li>
          </ul>
          <ul class="navbar-nav ml-auto">
            <li class="nav-item">
            <button type = "button" onClick={() => this.props.showLightbox(true)} className ="btn btn-danger navbar-btn">Analyze Data</button>
            </li>
          </ul>
        </nav>
      </div>
      <Lightbox openTabfromParent={this.openTab} />
      <div id="data" className={cx(styles.tabcontent , "tabcontent")}>
        <Cultivar dim={this.state.tabdim} openTabfromParent={this.openTab} />
      </div>
     <div id="pedigree" className={cx(styles.tabcontent,  "tabcontent")}>
        <Pedigree dim={this.state.tabdim} />
      </div>   
      <div id="pca" className={cx(styles.tabcontent,  "tabcontent")}>
        <Pca dim={this.state.tabdim} />
      </div>
      <div id="hc" className={cx(styles.tabcontent,  "tabcontent")}> 
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
