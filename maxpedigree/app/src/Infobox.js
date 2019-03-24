import React from 'react';
import styles from './Infobox.css';
import { connect } from "react-redux";
import { bindActionCreators } from "redux";


class InfoBox extends React.Component {
    constructor(props) {
        super(props);

        this.renderswitch = this.renderswitch.bind(this);
        this.renderInfo = this.renderInfo.bind(this);

    }


    renderswitch() {
        switch (this.props.tab) {
            case "pedigree":
               return this.renderInfo(this.props.selectednodePedigree)
                
            case "pca":
              return this.renderInfo(this.props.selectednodePC)
               
            case "hc":
              return  this.renderInfo(this.props.selectednodeHCluster)
               
            default:
                return null;
        }
    }

    renderInfo(node) {
        if (node == null) {
            return null;
        }
        return (
            <ul className={styles.list}>
                <li>
                    ID : {node.cultivarId}
                </li>
                <li>
                    Name : {node.cultivarName}
                </li>
                <li>MG : {node.maturityGroup != null ? node.maturityGroup.value : "N.A."}</li>
                <li>Country : {node.country != null ? node.country.value : "N.A."}</li>
                <li>Flower Color : {node.flowerColor != null ? node.flowerColor.value : "N.A."}</li>
                <li>Hilum Color : {node.hilumColor != null ? node.hilumColor.value : "N.A."}</li>
                <li>Pod Color : {node.podColor != null ? node.podColor.value : "N.A."}</li>
                <li>PubescenceColor : {node.pubescenceColor != null ? node.pubescenceColor.value : "N.A."}</li>

            </ul>
        )
    }

    render() {

        return <div className={styles.infoBox} style={this.props.dim}>
            <h3> Cultivar Info</h3>
            <div id="cultivarInfo">
                {this.renderswitch()}
            </div>
        </div>


    }

}

function mapDispatchToProps(dispatch) {
    // console.log("mapDispatchToProps controlled tabs");
    return bindActionCreators({}, dispatch)
};


function mapStateToProps(state) {
   // console.log("mapstatetoprops infobox");
   // console.log(state.selectednodePC);
    return {
        selectednodePC: state.selectednodePC,
        selectednodePedigree: state.selectednodePedigree,
        selectednodeHCluster : state.selectednodeHCluster
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(InfoBox);
