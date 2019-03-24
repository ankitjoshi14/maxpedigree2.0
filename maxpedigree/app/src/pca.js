import React from 'react';
import styles from'./HCluster.css';
import './pca.css';
import * as d3 from "d3v4";
import InfoBox from './Infobox';
import Attributebox from './AttributeBox';
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import { selectednodePC } from "./pedigreeaction";


class Pca extends React.Component {
    constructor(props) {
        super(props);
      
        // Don't call this.setState() here!
        this.state = {
            name: "pca",
            selectedcultivar: null,
            color: d3.scaleOrdinal(d3.schemeCategory10)
        };

        this.selected = this.selected.bind(this);
        this.calculateDim = this.calculateDim.bind(this);
        this.togglesize = this.togglesize.bind(this);
        //   this.legendselection = this.legendselection.bind(this);

    }


    componentDidMount() {

    }


    componentDidUpdate() {
        if (this.props.cultivarswithPC.length > 0) { // console.log("",this.props.dim.width);



            this.createscatter(this.props.selectedAttributePC);
        }
    }




    selected(d) {
        this.props.selectednodePC(d);

    }


    togglesize(d) {
        let node = d3.select("[cultivarid='" + d.cultivarId + "']");
        console.log("node", node.attr('r'))
        let currentradius = parseInt(node.attr('r'), 10);
        if (currentradius === 10) {
            console.log("inside")
            node.transition()
                .attr('r', 18);
            //d3.select(this).attr('selected', true)

        } else {
            node.transition()
                .attr('r', 10);
        }
    }

    createscatter(selectedattribute) {

        let pcadata = this.props.cultivarswithPC;
        let pcamargin = { top: 20, right: 20, bottom: 20, left: 30 };
        // dimensions and margins
        let dim = this.calculateDim().vizdim;
        let width = dim.width - (pcamargin.left + pcamargin.right);
        let height = dim.height - (pcamargin.top + pcamargin.bottom);


        let color = d3.scaleOrdinal(d3.schemeCategory10);

        function legendselection(d) {
            let value = selectedattribute;
            console.log("before legend", d)
            console.log("legend ", value)
            return color(selectionhepler(d, value))
        }

        function selectionhepler(d, value) {

            switch (value) {
                case "State":
                    return d.state != null ? d.state : "N.A."
                case "MG":
                    return d.maturityGroup != null ? d.maturityGroup.value : "N.A."
                case "Country":
                    return d.country != null ? d.country.value : "N.A."
                case "Flower Color":
                    return d.flowerColor != null ? d.flowerColor.value : "N.A."
                case "Hilum Color":
                    return d.hilumColor != null ? d.hilumColor.value : "N.A."
                case "Pod Color":
                    return d.podColor != null ? d.podColor.value : "N.A."
                case "stemTermination":
                    return d.stemTermination != null ? d.stemTermination.value : "N.A."
                case "pubescenceColor":
                    return d.pubescenceColor != null ? d.pubescenceColor.value : "N.A."
                case "pubescenceDensity":
                    return d.pubescenceDensity != null ? d.pubescenceDensity.value : "N.A."
                case "pubescenceForm":
                    return d.pubescenceForm != null ? d.pubescenceForm.value : "N.A."
                case "seedcoatColor":
                    return d.seedcoatColor != null ? d.seedcoatColor.value : "N.A."
                case "seedcoatLuster":
                    return d.seedcoatLuster != null ? d.seedcoatLuster.value : "N.A."

                default:
                    return ""

            }
        }



        function rangecalculateD3(data) {
            let domain = {}
            domain.x = d3.extent(data, function (d) { return d.pc1; });
            domain.y = d3.extent(data, function (d) { return d.pc2; });
            let offset = 20;
            domain.x[0] = domain.x[0] > 0 ? (domain.x[0] + offset) : (domain.x[0] - offset);
            domain.x[1] = domain.x[1] > 0 ? (domain.x[1] + offset) : (domain.x[1] - offset);
            domain.y[0] = domain.y[0] > 0 ? (domain.y[0] + offset) : (domain.y[0] - offset);
            domain.y[1] = domain.y[1] > 0 ? (domain.y[1] + offset) : (domain.y[1] - offset);

            return domain;
        }




        let scatter = d3.select(this.refs.anchor);
        //remove old content
        scatter.selectAll("*").remove();
        scatter.attr("width", dim.width);
        scatter.attr("height", dim.height);

        // create a clipping region 
        scatter.append("defs").append("clipPath")
            .attr("id", "clip")
            .append("rect")
            .attr("width", width)
            .attr("height", height)


        // create scale objects

        let xScale = d3.scaleLinear()
            .domain(rangecalculateD3(pcadata).x).nice()
            .range([0, (width)]);
        let yScale = d3.scaleLinear().nice()
            .domain(rangecalculateD3(pcadata).y)
            .range([(height), 0]);
        // create axis objects
        let xAxis = d3.axisBottom(xScale)
            .ticks(20, "s");
        let yAxis = d3.axisLeft(yScale)
            .ticks(20, "s");
        // Draw Axis
        let gX = scatter.append('g')
            .attr('transform', 'translate(' + pcamargin.left + ',' + (dim.height - pcamargin.bottom) + ')')
            .call(xAxis);
        let gY = scatter.append('g')
            .attr('transform', 'translate(' + pcamargin.left + ',' + pcamargin.top + ')')
            .call(yAxis);


        let pcazoom = d3.zoom()
            .scaleExtent([-20, 20])
            .extent([[0, 0], [width, height]])
            .on("zoom", zoomfunction);

        scatter.append("rect")
            .attr("width", width)
            .attr("height", height)
            .style("fill", "none")
            .style("pointer-events", "all")
            .attr('transform', 'translate(' + (pcamargin.left) + ',' + pcamargin.top + ')')
            .call(pcazoom);


        // Draw Datapoints
        var points_g = scatter.append("g")
            .attr('transform', 'translate(' + (pcamargin.left) + ',' + (pcamargin.top) + ')')
            .attr("clip-path", "url(#clip)")
            .classed("points_g", true);

        //let color = d3.scaleOrdinal(d3.schemeCategory10);

        //data = genRandomData (n, max);
        var points = points_g.selectAll("circle").data(pcadata);
        points = points.enter().append("circle")
            .attr('cx', function (d) { return xScale(d.pc1) })
            .attr('cy', function (d) { return yScale(d.pc2) })
            .attr('r', 10)
            .attr('cultivarid', function (d) { return d.cultivarId })
            .style("fill", function (d) { return legendselection(d) })
            .on("mouseover", this.selected)
            .on("click", this.togglesize);


        function zoomfunction() {
            // create new scale ojects based on event
            var new_xScale = d3.event.transform.rescaleX(xScale);
            var new_yScale = d3.event.transform.rescaleY(yScale);
            // update axes
            gX.call(xAxis.scale(new_xScale));
            gY.call(yAxis.scale(new_yScale));
            points.data(pcadata)
                .attr('cx', function (d) { return new_xScale(d.pc1) })
                .attr('cy', function (d) { return new_yScale(d.pc2) });
        }



        console.log("color domain pca", color.domain());
        var legend = scatter.selectAll(".legend")
            .data(color.domain())
            .enter().append("g")
            .attr("class", "legend")
            .attr("transform", function (d, i) { return "translate(0," + ((i * 20) + 6) + ")"; });

        legend.append("rect")
            .attr("x", width - 2)
            .attr("width", 18)
            .attr("height", 18)
            .style("fill", color);

        legend.append("text")
            .attr("x", width - 3)
            .attr("y", 9)
            .attr("dy", ".35em")
            .style("text-anchor", "end")
            .text(function (d) { return d; });


        // scatter.append("text")
        //     .attr("id", "selected")
        //     .attr("x", 80)
        //     .attr("y", 20)
        //     .attr("dy", ".35em")
        //     .style("font-size", "30px")
        //     .style("text-anchor", "start")
        //     .text("CultivarID : ");


    }

    calculateDim(){
        let dim = this.props.dim;
        const infodim = {
            width: 295,
            height: dim.height - 5,
        }
        const divseperater ={
            height:5
        }

        return {
            svgwidth: dim.width ,
            svgheight: dim.height,
            vizdim: {
                width: dim.width - 300 ,
                height: dim.height-10

            },
            infodim: infodim ,
            infobox:{
                width: infodim.width - 20 ,
                height: ((infodim.height-divseperater.height)/2)
            },
            attributebox:{
                width: infodim.width - 20,
                height: ((infodim.height-divseperater.height)/2)
            },
            divseperater:divseperater
        }
    }

    render() {
        const dim = this.calculateDim();
        return <div>
            <div className={styles.vizSection} style={dim.vizdim} >
                <svg ref="anchor" />
            </div>
            <div className={styles.infoSection} style={dim.infodim}>
                <InfoBox tab={this.state.name} dim={dim.infobox} />
                <div style={dim.divseperater}/>
                <Attributebox tab={this.state.name} dim={dim.attributebox} />
            </div>
        </div>;
    }

}



function mapDispatchToProps(dispatch) {
    // console.log("mapDispatchToProps controlled tabs");
    return bindActionCreators({ selectednodePC: selectednodePC }, dispatch)
};


function mapStateToProps(state) {
    //  console.log("mapstatetoprops controlled tabs");
    //  console.log(state);
    return {
        cultivarswithPC: state.cultivarswithPC,
        selectedAttributePC: state.selectedAttributePC
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(Pca);


