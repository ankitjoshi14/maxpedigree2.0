import React from 'react';
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import { selectcultivar, deselectcultivar } from "./pedigreeaction";



class Checkbox extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            checked: false

        };

        // Don't call this.setState() here!
        this.onChangeAction = this.onChangeAction.bind(this);
    };

    onChangeAction(e) {
        this.setState((state, props) => {
            return { checked: !state.checked }
        });
        if(e.target.checked){
            this.props.select(this.props.id);
        }else{
            this.props.deselect(this.props.id);
        }
       
    }

    componentDidMount(){
        
        this.setState((state, props) => {
           return{ checked: this.props.selectedCultivars.includes(this.props.id)}

        });
    }

    componentDidUpdate(prevProps, prevState) {
        // only update chart if the data has changed
        if (prevProps.selectedCultivars !== this.props.selectedCultivars) {
            this.setState((state, props) => {
                return {checked : this.props.selectedCultivars.includes(this.props.id)}
    
            });
        }
      }


    render() {
        return (
            <input id={this.props.id}
                type="checkbox"
                checked={this.state.checked}
                onChange={(e)=>this.onChangeAction(e)} />
        );
    }


}


function mapDispatchToProps(dispatch) {
   // console.log("mapDispatchToProps checkbox");
    return bindActionCreators({ select: selectcultivar, deselect: deselectcultivar }, dispatch)
};


function mapStateToProps(state) {
   console.log("mapstatetoprops checkbox");
   // console.log(state);
    return {
        selectedCultivars: state.selectedCultivars
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Checkbox);

