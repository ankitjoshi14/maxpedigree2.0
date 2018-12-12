import React from 'react';
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import { uploadfile } from "./pedigreeaction";
import "./Fileuploader.css";



class FileUploader extends React.Component {
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


        this.handleselectedFile = this.handleselectedFile.bind(this);
        this.handleFileType = this.handleFileType.bind(this);
        // Don't call this.setState() here!
        this.cancel = this.cancel.bind(this);
        this.handleUpload = this.handleUpload.bind(this);
        this.handleUploadType = this.handleUploadType.bind(this);
        this.show = this.show.bind(this);
        this.createmessage = this.createmessage.bind(this);
    };

    componhhentDidUpdate(prevProps, prevState) {
        if (this.props.responseobj !== prevProps.responseobj) {
            let response = this.createmessage();
            this.setState((state, props) => {
                return { message: response.message }
            });
        }

    }


    createmessage() {
        let response = {
            message: "",
            uploaded: "",
            valid: "",
            validationClass:""
        }
        const invalidValueFor = 'Invalid values for: '
        const exclamation = " !!";
        const nl = "\n";

        if (this.props.responseobj != null) {
            response.uploaded = this.props.responseobj.uploaded ? "TRUE" : "FALSE";
            response.uploadedClass = this.props.responseobj.uploaded ? "text-success" : "text-danger";
            response.valid = this.props.responseobj.success ? "SUCCESS" : "FAILED";
            response.validationClass = this.props.responseobj.success ? "text-success" : "text-danger";
            let objects = this.props.responseobj.validtionresponses;
           // response.message = response.message.concat(this.props.responseobj.message,`${nl}`);
            if (objects != null) {
                for (var i = 0; i < objects.length; i++) {
                    let validtionresponse = objects[i];

                    if (!validtionresponse.valid) {
                        response.message = response.message.concat(`${invalidValueFor}`, validtionresponse.cultivarId, `${exclamation}`, `${nl}`, validtionresponse.message, `${nl}`);
                    }
                }
            }

        }

        console.log("create called")
        return response;
    }



    cancel() {
        this.setState((state, props) => {
            return { show: false }
        });


    }







    handleselectedFile(event) {
        this.setState({
            selectedFile: event.target.files[0],
            loaded: 0,
        })
    }


    handleUpload() {
        this.props.uploadfile(this.state.selectedFile, this.state.fileType, this.state.uploadType);

    }





    componentDidMount() {
        //  this.props.dispatch({
        //    type: "getall"
        // })

    }

    handleFileType(event) {
        this.setState({
            fileType: event.target.value
        })

        console.log(this.state.Type)
    }
    handleUploadType(event) {
        this.setState({
            uploadType: event.target.value
        })

    }

    show() {
        this.setState({
            show: true
        })
    }

    handleChange(event) {
        this.setState({ value: event.target.value });
    }


    render() {
        console.log("fileloder render before create message");
        let response = this.createmessage();
        let uploaded = response.uploaded;
        let validation = response.valid;
        let validationClass = response.validationClass;
        let uploadedClass = response.uploadedClass;
        console.log("fileloder render called");
        return (

            <div>

                <button type="button" className="btn btn-primary Left searchbarbutton" onClick={this.show} >Import Data</button>
                <div className={this.state.show ? 'overlayer' : 'hidden  overlayer'} ></div>
                <div className={this.state.show ? 'modaluploader' : 'hidden modaluploader'}>
                   
                <h2>
                        IMPORT
                    </h2>
                    <form className="uploadform">
                        <div className="form-group">
                            <label class="form-check-label" for="exampleRadios2">Upload</label>
                            <div><input type="file" name="" id="" onChange={this.handleselectedFile} /></div>
                        </div>
                        <div className="form-group">
                            <label class="form-check-label" for="exampleRadios2">  File Type</label>
                            <div className="form-row">
                                <div className="form-group col-md-5" >
                                    <input onClick={this.handleFileType} type="radio" name="optradio" value="snp" />SNP

                            </div>
                                <div className=" form-group col-md-7" >
                                    <input onClick={this.handleFileType} type="radio" name="optradio" value="cultivar" />Cultivar
                            </div>
                            </div>
                        </div>
                        <div className="form-group">
                            <label class="form-check-label" for="exampleRadios2">Upload Type</label>
                            <div className="form-row">
                                <div className="form-group col-md-5" >
                                    <input onClick={this.handleUploadType} type="radio" name="uradio" value="false" />Validate

                            </div>
                                <div className=" form-group col-md-7" >
                                    <input onClick={this.handleUploadType} type="radio" name="uradio" value="true" />Validate and Upload
                            </div>
                            </div>
                        </div>



                        <div className="form-group">
                            <div className="form-row">
                                <div className="form-group col-md-6" >
                                    <button type="button" className="btn btn-primary btn-block" onClick={this.handleUpload} >upload</button>
                                </div><div className=" form-group col-md-6" >
                                    <button type="button" className="btn btn-danger btn-block" onClick={this.cancel}>cancel</button>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="form-check-label" for="exampleRadios2">Upload Status:</label>
                            <div className="form-row">
                                <div className="form-group col-md-5" >
                                Validation : <span className={validationClass}>{validation}</span>

                                </div>
                                <div className=" form-group col-md-7" >
                                    Uploaded : <span className={uploadedClass}>{uploaded}</span> 
                                </div>
                            </div>
                            <textarea className="form-control" rows="9" id="comment" value={response.message} readOnly ></textarea>
                        </div>

                    </form>
                </div>
            </div>

        );
    }
}


function mapDispatchToProps(dispatch) {
    //  console.log("mapDispatchToProps lighbox");
    return bindActionCreators({ uploadfile: uploadfile }, dispatch)
};


function mapStateToProps(state) {

    return {
        responseobj: state.uploadfileresponse
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(FileUploader);
