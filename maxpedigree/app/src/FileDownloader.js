import React from 'react';
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import { downloadFile } from "./pedigreeaction";
import "./FileDownloader.css";



class FileDowndloader extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            show: false,
            selectedFile: null,
            status: null,
            fileType: "snp",
            uploadType: true,
            value: "",
            message: ""

        };



        // Don't call this.setState() here!
        this.cancel = this.cancel.bind(this);
        this.refresh = this.refresh.bind(this);

        this.show = this.show.bind(this);

    };

    cancel() {
        this.setState((state, props) => {
            return { show: false }
        });


    }
    componentDidMount() {
        //  this.props.dispatch({
        //    type: "getall"
        // })

    }

    refresh() {
        this.setState({ value: this.props.selectedCultivars.map(item => item).join('\n') });
    }

    show() {
        this.setState({
            show: true
        })
    }

    handleChange(event) {
        let inputValue = event.target.value;
        this.setState({ value: inputValue });
        console.log("textbox values", inputValue)
        let k = inputValue.split("\n");
        // k.splice(k.length-1 , k.length);
        console.log(k[k.length - 1]);
    }

    onsubmit(event, value) {
        console.log("submit", value);
        let postData = this.state.value.split("\n");
        switch (value) {
            case "cultivars":
                this.props.downloadFile(postData, "cultivar");
                break;
            case "snp":
                this.props.downloadFile(postData, "snp");
                break;
            default:
                break;
        }
    }

    componentDidUpdate(prevProps, prevState) {
        //  console.log("componet did update", this.state.value)
        if ((this.props.selectedCultivars !== prevProps.selectedCultivars)) {
            this.setState({ value: this.props.selectedCultivars.map(item => item).join('\n') });
        }
    }


    render() {

        return (

            <div>

                <button type="button" className="btn btn-primary Left searchbarbutton" onClick={this.show} >Export Data</button>
                <div className={this.state.show ? 'overlayer' : 'hidden  overlayer'} ></div>
                <div className={this.state.show ? 'modalloader' : 'hidden modalloader'}>
                    <h2>Export</h2>
                    <div>
                        <form className="uploadform">
                            <div className="form-row">
                                <div className="form-group">
                                    <label for="comment">Selected Cultivars:</label>
                                    <textarea className="form-control" rows="10" id="comment" onChange={this.handleChange} value={this.state.value} ></textarea>
                                </div>
                            </div>
                            <div className="form-row">
                                <div className="form-group col-md-12">
                                <button type="button" className="btn btn-info btn-block" onClick={(e) => this.onsubmit(e, "cultivars")} >Download Cultivars</button>
                                  
                                </div>
                                <div className="form-group col-md-12">
                                <button type="button" className="btn btn-info btn-block" onClick={(e) => this.onsubmit(e, "snp")} >Download SNP</button>
                                </div>
                            </div>
                            
                            <div className="form-row">
                                <div className="form-group col-md-6" >
                                    <button type="button" className="btn btn-primary btn-block" onClick={() => this.refresh()}>
                                        <span className="glyphicon glyphicon-refresh"></span> Refresh</button>

                                </div>
                                <div className=" form-group col-md-6" >
                                <button type="button" className="btn btn-danger btn-block" onClick={this.cancel}>cancel</button>
                                </div>
                            </div>

                        </form>
                    </div>
                </div>
            </div >

        );
    }
}


function mapDispatchToProps(dispatch) {
    //  console.log("mapDispatchToProps lighbox");
    return bindActionCreators({ downloadFile: downloadFile }, dispatch)
};


function mapStateToProps(state) {

    return {
        selectedCultivars: state.selectedCultivars
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(FileDowndloader);