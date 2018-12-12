import React from 'react';
import './HCluster.css';
import * as d3 from "d3v4";
import InfoBox from './Infobox';
import Attributebox from './AttributeBox';
import round from "math-round";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import { selectednodePedigree } from "./pedigreeaction";
//import { pedigreeData } from "./data";

class Pedigree extends React.Component {
    constructor(props) {
        super(props);
        let { width, height } = this.props.dim
        // Don't call this.setState() here!
        this.state = {
            name: "pedigree",
            tree: null,
            root: null,
            margin: null,
            svg: null,
            g: null,
            duration: 750,
            color: d3.scaleOrdinal(d3.schemeCategory10)
        };

        this.updatePedigree = this.updatePedigree.bind(this);
        this.click = this.click.bind(this);
        this.selected = this.selected.bind(this);
        this.calculateDim = this.calculateDim.bind(this);

    }

    componentDidMount() {

        this.treeinit();
    }

    componentDidUpdate() {
        this.treeinit();
    }

    treeinit() {
        this.svg = d3.select(this.refs.anchor);
         
        this.svg.selectAll("*").remove();
        let dim = this.calculateDim().vizdim;
        let width = dim.width;
        let height = dim.height;


        this.svg.attr("width", dim.width);
        this.svg.attr("height", dim.height);

        //  width = +svg.attr("width");
        // height = +svg.attr("height");
        this.g = this.svg.append("g").attr("transform", "translate(" + 150 + "," + 0 + ")").append("g");
        this.g.attr("width", dim.width);
        this.g.attr("height", dim.height);
        // height and width are opposite of dim as it 90 degree roteted.
        this.tree = d3.tree().size([height, width - 300]);
        // Collapse after the second level
        //root.children.forEach(collapse);
        if (this.props.pedigreeroot != null) {
            this.root = d3.hierarchy(this.props.pedigreeroot, function (d) { return d.childof; });

            //  this.tree(root);

            root.y0 = 0;
            root.x0 = dim.height / 2;
            this.updatePedigree(root, this.props.selectedAttributePedigree);
        }
    }

    updatePedigree(source, selectedattribute) {
        let dim =  this.calculateDim().vizdim;
        let i = 0;
        let duration = 750;
        let tree = this.tree;
        let g = this.g;
        let svg = this.svg;
        let width = dim.width;
        let color = d3.scaleOrdinal(d3.schemeCategory10);





        function legendselection(d) {
            let value = selectedattribute;
            console.log("before legend", d);
            console.log("legend ", value);
            return color(selectionhepler(d, value));
            // switch (value) {
            //     case "state":
            //         return color(selectionhepler(value))
            //     case "mg":
            //         return color(d.data.maturityGroup != null ? d.data.maturityGroup.value : "N.A.")
            //     case "country":
            //         return color(d.data.country != null ? d.data.country.value : "N.A.")
            //     default:
            //         return "steelblue"

            // }
        }


        function selectionhepler(d, value) {

            switch (value) {
                case "State":
                    return d.data.state != null ? d.data.state : "N.A."
                case "MG":
                    return d.data.maturityGroup != null ? d.data.maturityGroup.value : "N.A."
                case "Country":
                    return d.data.country != null ? d.data.country.value : "N.A."
                case "Flower Color":
                    return d.data.flowerColor != null ? d.data.flowerColor.value : "N.A."
                case "Hilum Color":
                    return d.data.hilumColor != null ? d.data.hilumColor.value : "N.A."
                case "Pod Color":
                    return d.data.podColor != null ? d.data.podColor.value : "N.A."
                case "stemTermination":
                    return d.data.stemTermination != null ? d.data.stemTermination.value : "N.A."
                case "pubescenceColor":
                    return d.data.pubescenceColor != null ? d.data.pubescenceColor.value : "N.A."
                case "pubescenceDensity":
                    return d.data.pubescenceDensity != null ? d.data.pubescenceDensity.value : "N.A."
                case "pubescenceForm":
                    return d.data.pubescenceForm != null ? d.data.pubescenceForm.value : "N.A."
                case "seedcoatColor":
                    return d.data.seedcoatColor != null ? d.data.seedcoatColor.value : "N.A."
                case "seedcoatLuster":
                    return d.data.seedcoatLuster != null ? d.seedcoatLuster.value : "N.A."

                default:
                    return ""

            }
        }

        // Assigns the x and y position for the nodes
        let treeData = tree(this.root);

        // Compute the new tree layout.
        let nodes = treeData.descendants(),
            links = treeData.descendants().slice(1);

        console.log(nodes);
        // Normalize for fixed-depth.
        nodes.forEach(function (d) {
            d.y = d.depth * 210
        });

        //  nodes.forEach(function(d) {
        //     d.x = d.depth * 100
        // }); 
        // ****************** Nodes section ***************************
        console.log(nodes);
        let zoom = d3.zoom();
        this.svg.call(zoom.scaleExtent([-4, 4])
            // .wheelDelta(delta)
            .on("zoom", zoomed));


        //        function delta() {
        //          return -d3.event.deltaY * (d3.event.deltaMode ? 120 : 1) / 4000;
        //    }


        function zoomed() {
            g.attr("transform", d3.event.transform);
        }

        // Update the nodes...
        let node = g.selectAll('g.node')
            .data(nodes, function (d) {
                return d.id || (d.id = ++i);
            })

        // Enter any new modes at the parent's previous position.
        let nodeEnter = node.enter().append('g')
            .attr('class', 'node')
            .style('fill', "rgb(218, 34, 34)")
            .style('stroke', "steelblue")
            .style('stroke-width', "3px")
            .attr("transform", function (d) {
                // console.log("node ", d);
                return "translate(" + source.y0 + "," + source.x0 + ")";
            })
        //.on('click', click);

        // Add Circle for the nodes
        nodeEnter.append('circle')
            .attr('class', 'node')
            .attr('r', 1e-6)
            .style("fill", function (d) {
                return (d._children ? "lightsteelblue" : "#fff");
            }).on('click', this.click);

        // Add labels for the nodes
        // nodeEnter.append('text')
        //     .attr("dy", ".35em")
        //     .attr("x", function(d) {
        //         return d.children || d._children ? -13 : 13;
        //     })
        //     .attr("text-anchor", function(d) {
        //         return d.children || d._children ? "end" : "start";
        //     })
        //     .text(function(d) { return d.data.name + d.data.paretnt; });
        let selected = ["id"];
        nodeEnter.append("foreignObject")
            .attr("width", 170)
            .attr("height", 55)
            .attr("y", 0)
            .attr("x", function (d) {
                return d.children || d._children ? -182 : 12;
            })
            .style("font-size", "15px")
            .style("border", "solid")
            .style("border-color", "#ff000080")
            .style("border-radius", "5px")
            .style("border-width", "2px")
            // .style("background", "white")
            // .attr("text-anchor", function (d) {
            //     return d.children || d._children ? "end" : "start";
            // })
            .attr("value", function (d) {
                return d.data.cultivarId;
            }).on("mouseover", this.selected).on("mouseout", function (d) {
                d3.select(this).style("border-color", "#ff000080");
                d3.select(this.parentNode).select("circle").style("stroke-width", "3px")
            })
            .append("xhtml:div")
            .style("text-align", function (d) {
                return d.children || d._children ? "right" : "left";
            })

            .html(function (d) {
                var elementstoAppend = [];
                elementstoAppend[0] = (d.data.cultivarId ? d.data.cultivarId : "N.A.") + " | " + (d.data.cultivarName ? d.data.cultivarName.substring(0, 15) : "N.A.");
                elementstoAppend[1] = "</br>" + selectedattribute + " : " + selectionhepler(d, selectedattribute);
                for (var i = 1; i <= selected.length; i++) {

                    //   var row = "</br>" + selected[i - 1].substring(0, 15) + " : " + (d.data[selected[i - 1]] ? d.data[selected[i - 1]] : "N.A.");
                    // var row = "</br>" + (d.data[selected[i - 1]] ? d.data[selected[i - 1]] : "N.A.");
                    //  elementstoAppend[i] = "<tr>" + row + "</tr>";
                }
                return elementstoAppend.join('');
            });

        // UPDATE
        let nodeUpdate = nodeEnter.merge(node);

        // Transition to the proper position for the node
        nodeUpdate.transition()
            .duration(duration)
            .attr("transform", function (d) {
                return "translate(" + d.y + "," + d.x + ")";
            });

        // Update the node attributes and style
        nodeUpdate.select('circle.node')
            .attr('r', 10)
            .style("fill", function (d) {
                return d._children ? "lightsteelblue" : "#fff";
            })
            .attr('cursor', 'pointer');


        // Remove any exiting nodes
        let nodeExit = node.exit().transition()
            .duration(duration)
            .attr("transform", function (d) {
                return "translate(" + source.y + "," + source.x + ")";
            })
            .remove();

        // On exit reduce the node circles size to 0
        nodeExit.select('circle')
            .attr('r', 1e-6);

        // On exit reduce the opacity of text labels
        nodeExit.select('text')
            .style('fill-opacity', 1e-6);

        // ****************** links section ***************************

        // Update the links...
        let link = g.selectAll('path.link')
            //.style("stroke", function(d) { return (d.data.year === "female" ? "pink":"blue"); })

            .data(links, function (d) {
                return d.id;
            }).style("stroke", function (d) { return legendselection(d); }).style('stroke-width', "5px").style("opacity", 0.9);

        // Enter any new links at the parent's previous position.
        let linkEnter = link.enter().insert('path', "g")
            .attr("class", "link")
            //.style("stroke", function (d) {return (d.data.actedGender === "female" ? "#ec60db" : "blue");})

            .attr('d', function (d) {
                var o = {
                    x: source.x0,
                    y: source.y0
                }
                return diagonal(o, o)
            }).style("stroke", function (d) { return legendselection(d); }).style('stroke-width', "5px").style("opacity", 0.9);

        // UPDATE
        let linkUpdate = linkEnter.merge(link);

        // Transition back to the parent element position
        linkUpdate.transition()
            .duration(duration)
            .attr('d', function (d) {
                return diagonal(d, d.parent)
            });

        // Remove any exiting links
        link.exit().transition()
            .duration(duration)
            .attr('d', function (d) {
                var o = {
                    x: source.x,
                    y: source.y
                }
                return diagonal(o, o)
            })
            .remove();

        // Store the old positions for transition.
        nodes.forEach(function (d) {
            d.x0 = d.x;
            d.y0 = d.y;
        });

        // Creates a curved (diagonal) path from parent to the child nodes
        function diagonal(s, d) {

            let path = `M ${s.y} ${s.x}
                        C ${(s.y + d.y) / 2} ${s.x},
                          ${(s.y + d.y) / 2} ${d.x},
                          ${d.y} ${d.x}`

            return path
        }


        console.log("color domain pedigree", color.domain());
        var legend = svg.selectAll(".legend")
            .data(color.domain())
            .enter().append("g")
            .attr("class", "legend")
            .attr("transform", function (d, i) { return "translate(0," + ((i * 20) + 6) + ")"; });

        legend.append("rect")
            .attr("x", width - 22)
            .attr("width", 18)
            .attr("height", 18)
            .style("fill", color);

        legend.append("text")
            .attr("x", width - 23)
            .attr("y", 9)
            .attr("dy", ".35em")
            .style("text-anchor", "end")
            .text(function (d) { return d; });



    }
    // Toggle children on click
    click(d) {
        console.log("source" + d);
        if (d.children) {
            d._children = d.children;
            d.children = null;
        } else {
            d.children = d._children;
            d._children = null;
        }
        this.updatePedigree(d, this.props.selectedAttributePedigree);
    }


    selected(d) {
        console.log("selected hover node", d);
        this.props.selectednodePedigree(d.data);
    }

    calculateDim() {
        let dim = this.props.dim;
        const infodim = {
            width: 295,
            height: dim.height - 5,
        }
        const divseperater = {
            height: 5
        }

        return {
            svgwidth: dim.width,
            svgheight: dim.height,
            vizdim: {
                width: dim.width - 300,
                height: dim.height-10

            },
            infodim: infodim,
            infobox: {
                width: infodim.width - 20,
                height: ((infodim.height - divseperater.height) / 2)
            },
            attributebox: {
                width: infodim.width - 20,
                height: ((infodim.height - divseperater.height) / 2)
            },
            divseperater: divseperater
        }
    }

    render() {
        const dim = this.calculateDim();
        return <div>
            <div className="vizSection" style={dim.vizdim} >
                <svg ref="anchor" />
            </div>
            <div className="infoSection" style={dim.infodim}>
                <InfoBox tab={this.state.name} dim={dim.infobox} />
                <div style={dim.divseperater} />
                <Attributebox tab={this.state.name} dim={dim.attributebox} />
            </div>
        </div>;
    }
}


function mapDispatchToProps(dispatch) {
    // console.log("mapDispatchToProps controlled tabs");
    return bindActionCreators({
        selectednodePedigree: selectednodePedigree
    }, dispatch)
};


function mapStateToProps(state) {
    //  console.log("mapstatetoprops pedigree");
    //   console.log(state.selectedAttributePedigree);
    return {
        selectedAttributePedigree: state.selectedAttributePedigree,
        pedigreeroot: state.pedigreeroot
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(Pedigree);
