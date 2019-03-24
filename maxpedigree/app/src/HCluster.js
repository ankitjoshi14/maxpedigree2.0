/*
 * A simple React component
 */
import React from 'react';
import styles from'./HCluster.css';
import * as d3 from "d3v4";
import InfoBox from './Infobox';
import Attributebox from './AttributeBox';
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import { selectednodeHCluster } from "./pedigreeaction";

class HCluster extends React.Component {
    constructor(props) {
        super(props);
       
        // Don't call this.setState() here!
        this.state = {
        };
    }


    componentDidMount() {

        // console.log("",this.props.dim.width);
        //  this.createcluster();
    }

    componentDidUpdate() {
        if (this.props.hclusterroot != null) { // console.log("",this.props.dim.width);
            this.createcluster();
        }

    }

    createcluster() {
        let dim = this.calculateDim().vizdim;
        let pcamargin = { top: 20, right: 150, bottom: 20, left: 30 };
        let width = dim.width;
        let height = dim.height - 20;
        var data = this.props.hclusterroot;
        console.log(data);
        let root = d3.hierarchy(data);
        // height and width are opposite of dim as it 90 degree roteted.
        var tree = d3.cluster().size([height - pcamargin.bottom, (width - pcamargin.right)]);

        tree(root)

        console.log(root);

        let cluster = d3.select(this.refs.anchor);
        cluster.selectAll("*").remove();
        cluster.attr("width", dim.width);
        cluster.attr("height", dim.height);


        // create a clipping region 
        cluster.append("defs").append("clipPath")
            .attr("id", "clip")
            .append("rect")
            .attr("width", width)
            .attr("height", height)

        // create scale objects
        let xScale = d3.scaleLinear()
            .domain([0, 1])
            .range([width - pcamargin.right, 0]);
        // create axis objects
        let xAxis = d3.axisBottom(xScale)
            .ticks(20, "s");

        // Draw Axis
        let gX = cluster.append('g')
            .attr('transform', 'translate(' + pcamargin.left + ',' + (height- pcamargin.bottom) + ')')
            .call(xAxis);

        let zoom = d3
            .zoom()
            .extent([[0, 0], [width, height]])
            .on("zoom", zoomed);

        cluster.append("rect")
            .attr("width", width)
            .attr("height", height)
            .style("fill", "none")
            .style("pointer-events", "all")
            .attr('transform', 'translate(' + pcamargin.left + ',' + pcamargin.top + ')')
            .call(zoom);

        // Draw Datapoints
        var points_g = cluster.append("g")
            .attr('transform', 'translate(' + (pcamargin.left) + ',' + (pcamargin.top) + ')')
            .attr("clip-path", "url(#clip)")
            .classed("points_g", true).append("g");

        let links = points_g.selectAll(".link")
            .data(root.descendants().slice(1))
            .enter()
            .append("path");



        links.each(function (d) {
            //       console.log("d.y before " +d.data.name +" "+ d.y + " height" + d.data.height );

            //d.y = d.depth * 120 + (d.children ? d.data.height : height - 160 );
            //d.y = (height/2) - d.data.height;
            return d.y = xScale(d.data.height);
            //    console.log("d.y after " + d.y);
        });

        links.attr("class", "link")
            .attr("d", function (d) {
                return "M" + d.y + "," + d.x
                    + "L" + (d.parent.y) + "," + (d.x)
                    + " " + (d.parent.y) + "," + d.parent.x;
            }).style('stroke-width', "2px").style("opacity", 0.9).style("fill","none").style("stroke", "#555");


        let nodes = points_g
            .selectAll(".node")
            .data(root.descendants())
            .enter()
            .append("g")
            .attr("class", function (d) {
                return "node" + (d.children ? " node--internal" : " node--leaf");
            })
            .attr("transform", function (d) {
                return (
                    "rotate( 0 " +
                    d.y +
                    "," +
                    d.x +
                    ")" +
                    " translate(" +
                    d.y +
                    "," +
                    d.x +
                    ")"
                );
            });

        nodes.append("circle").attr("r", 2.5);

        nodes.append("text")
            .attr("x", 10)
            .attr("y", 2)
            .style("font-size",function (d) {
                return d.children ? "10px": "15px";
            })
            .text(function (d) {
                return d.children ? d.data.height.toFixed(2) : d.data.nodeName;
            });
        //  width = +svg.attr("width");
        // height = +svg.attr("height");
        // var g = svg.append("g").attr("transform", "translate(40,0)");







        function zoomed() {
            console.log("zoomcalled");
            var new_xScale = d3.event.transform.rescaleX(xScale);
            //points_g.attr("transform", "translate(" + [pcamargin.left, pcamargin.top] + ") scale(" + new_xScale + ")");
            points_g.attr("transform", d3.event.transform);


            gX.call(xAxis.scale(new_xScale));
        }

















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
        return (<div>
            <div className={styles.vizSection} style={dim.vizdim} >
                <svg ref="anchor" />
            </div>
            <div className={styles.infoSection} style={dim.infodim}>
                <InfoBox tab={this.state.name} dim={dim.infobox} />
                <div style={dim.divseperater}/>
                <Attributebox tab={this.state.name} dim={dim.attributebox} />
            </div></div>);
    }
}


function mapDispatchToProps(dispatch) {
    // console.log("mapDispatchToProps controlled tabs");
    return bindActionCreators({ selectednodeHCluster: selectednodeHCluster }, dispatch)
};


function mapStateToProps(state) {
    //  console.log("mapstatetoprops controlled tabs");
    //  console.log(state);
    return {
        hclusterroot: state.hclusterroot,
        selectedAttributeHCluster: state.selectedAttributeHCluster
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(HCluster);


  /*
   * Render the above component into the div#app
   */
  //ReactDOM.render(<HCluster width={1000} height={600} />, document.getElementById("app"));
