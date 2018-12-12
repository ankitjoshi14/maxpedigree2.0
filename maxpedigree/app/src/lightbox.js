import React from 'react';
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import { showLightbox, pca, hcluster, downloadFile } from "./pedigreeaction";
import "./lightbox.css";



class Lightbox extends React.Component {
    constructor(props) {
        super(props);
        console.log("constructor", props.selectedCultivars);
        this.state = {
            shouldShow: true,
            value: props.selectedCultivars
        };


        this.handleChange = this.handleChange.bind(this);
        // Don't call this.setState() here!
        this.oncancel = this.oncancel.bind(this);
        this.onsubmit = this.onsubmit.bind(this);
        this.refresh = this.refresh.bind(this);

    };


    refresh() {
        this.setState({ value: this.props.selectedCultivars.map(item => item).join('\n') });
    }

    oncancel() {
        this.setState((state, props) => {
            return { shouldShow: false }
        });


    }
    onsubmit(event, value) {
        console.log("submit", value);
        // prperty name must matach class prpertys

        let postData = this.state.value.split("\n");
        switch (value) {
            case "pca":
                this.props.pca(postData);
                break;
            case "hc":
                this.props.hcluster(postData);
                break;
            case "cultivars":
                this.props.downloadFile(postData, "cultivar");
                break;
            case "snp":
                this.props.downloadFile(postData, "snp");
                break;
            default:
                break;
        }

        if (!((value == "cultivars") || (value == "snp")))
            this.props.openTabfromParent(value);
        this.props.show(false);

    }

    handleChange(event) {
        this.setState({ value: event.target.value });
        console.log("textbox values", event.target.value)
        let k = event.target.value.split("\n");
        // k.splice(k.length-1 , k.length);
        console.log(k[k.length - 1]);
    }


    componentDidUpdate(prevProps, prevState) {
        //  console.log("componet did update", this.state.value)
        if ((this.props.selectedCultivars !== prevProps.selectedCultivars)) {
            this.setState({ value: this.props.selectedCultivars.map(item => item).join('\n') });
        }
    }


    componentDidMount() {
        //  this.props.dispatch({
        //    type: "getall"
        // })

    }



    static AgetDerivedStateFromProps(nextProps, prevState) {
        // Store prevId in state so we can compare when props change.
        // Clear out previously-loaded data (so we don't render stale stuff).
        console.log("derived state")
        console.log("next prop", nextProps.selectedCultivars)
        console.log("prev state", prevState.value)
        if ((nextProps.selectedCultivars !== prevState.value)) {

            return Object.assign({}, prevState, {
                value: nextProps.selectedCultivars
            });

        }

        // No state update necessary
        return null;
    }

    render() {
        console.log("render lightbox ", this.props.showit)
        return (
            <div><div id="target" className={this.props.showit ? 'overlay' : 'hidden  overlay'} ></div>
                <div className={this.props.showit ? 'moddal' : 'hidden moddal'}>
                    <h2>
                        ANALYZE
                    </h2>
                     <div>                    <form className="uploadform">
                     <div className="form-row">
                        <div className="form-group">
                        <label for="comment">Selected Cultivars:</label>
                            <textarea className="form-control" rows="10" id="comment" onChange={this.handleChange} value={this.state.value} ></textarea>
                        </div>
                        </div>
                        <div className="form-row">
                            <div className="form-group col-md-12">

                                <button type="button" className="btn btn-primary btn-block .btn-lg " onClick={(e) => this.onsubmit(e, "pca")} >PCA</button>
                            </div>
                            <div className="form-group col-md-12">
                                <button type="button" className="btn  btn-info btn-block .btn-lg " onClick={(e) => this.onsubmit(e, "hc")} >HCluster</button>
                            </div>
                        </div>
                     
                        <div className="form-row">
                            <div className="form-group col-md-6" >
                                <button type="button" className="btn btn-primary btn-block" onClick={() => this.refresh()}>
                                    <span className="glyphicon glyphicon-refresh"></span> Refresh</button>

                            </div>
                            <div className=" form-group col-md-6" >
                                <button type="button" className="btn btn-block btn-danger" onClick={() => this.props.show(false)}>cancel</button>
                            </div>
                        </div>
                    </form>
                    </div>

                </div>
            </div>

        );
    }
}


function mapDispatchToProps(dispatch) {
    //  console.log("mapDispatchToProps lighbox");
    return bindActionCreators({ show: showLightbox, pca: pca, hcluster: hcluster, downloadFile: downloadFile }, dispatch)
};


function mapStateToProps(state) {
    //  console.log("mapstatetoprops light");
    //   console.log(state.selectedCultivars);
    return {
        selectedCultivars: state.selectedCultivars,
        showit: state.showLightbox
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Lightbox);
