/* Pagination Component 
-------------------------------------------------*/
import React from 'react';
import { getpedigreedata } from "./pedigreeaction";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import styles from "./Pagination.css";
import cx from 'classnames';
//const propTypes = {
//  items: React.PropTypes.array.isRequired,
//  onChangePage: React.PropTypes.func.isRequired,
// initialPage: React.PropTypes.number    
//}



class Pagination extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            pager: {},
            pageSize: 20
        };
    }

    componentDidMount() {
        console.log("pegination mounted");
        this.setPage(this.props.initialPage, this.state.pageSize);
        // this.setPage(this.props.initialPage);
        // set page if items array isn't empty
        if (this.props.pageOfItems && this.props.pageOfItems.length) {
            //       console.log("intial", this.props.initialPage);
            //  this.setPage(this.props.initialPage);
        }
    }

    componentDidUpdate(prevProps, prevState) {
        //     console.log("prevProps.pageOfItems", prevProps.pageOfItems);
        //      console.log("this.props.pageOfItems", this.props.pageOfItems)
        // reset page if items array has changed
        if ((this.props.totalItems !== prevProps.totalItems) || (this.props.seachCriteria !== prevProps.seachCriteria)) {
            //      console.log("reset page")
            this.setPage(this.state.initialPage);


        }
    }

    setPage(page) {
        var total = this.props.totalItems;
        var pager = this.state.pager;

        if (page < 1 || page > pager.totalPages) {
            console.log("retunr");
            return;
        }

        //this.props.getall((page-1) * 20, 20);
         
        let seachCriteria = this.props.seachCriteria;
        seachCriteria.start = page;
        seachCriteria.limit = this.state.pageSize;
         
        this.props.getCultivars(seachCriteria);
        // get new pager object for specified page
        
        pager = this.getPager(total, page, this.state.pageSize);
        //   console.log("current page", pager.currentPage);
        // get new page of items from items array
        //  var pageOfItems = items.slice(pager.startIndex, pager.endIndex + 1);

        // update state
        this.setState({ pager: pager });
         // console.log("pageofITems", pageOfItems)
        // call change page function in parent component
        // this.props.onChangePage(pageOfItems);
    }

    getPager(totalItems, currentPage, pageSize) {
        // default to first page
        currentPage = currentPage || 1;
        //   console.log("totalItems", totalItems);

        // default page size is 10
        pageSize = pageSize || 10;

        // calculate total pages
        var totalPages = Math.ceil(totalItems / pageSize);
        //   console.log("total page", totalPages);
        var startPage, endPage;
        if (totalPages <= 10) {
            // less than 10 total pages so show all
            startPage = 1;
            endPage = totalPages;
        } else {
            // more than 10 total pages so calculate start and end pages
            if (currentPage <= 6) {
                startPage = 1;
                endPage = 10;
            } else if (currentPage + 4 >= totalPages) {
                startPage = totalPages - 9;
                endPage = totalPages;
            } else {
                startPage = currentPage - 5;
                endPage = currentPage + 4;
            }
        }

        // calculate start and end item indexes
        var startIndex = (currentPage - 1) * pageSize;
        var endIndex = Math.min(startIndex + pageSize - 1, totalItems - 1);

        // create an array of pages to ng-repeat in the pager control
        var pages = [...Array((endPage + 1) - startPage).keys()].map(i => startPage + i);
        //   console.log("pages", pages)
        // return object with all pager properties required by the view
        return {
            totalItems: totalItems,
            currentPage: currentPage,
            pageSize: pageSize,
            totalPages: totalPages,
            startPage: startPage,
            endPage: endPage,
            startIndex: startIndex,
            endIndex: endIndex,
            pages: pages
        };
    }

    calculatedim() {
        const dim = this.props.dim;
        return{ 
            width: dim.width,
            height: dim.height,
        }
        

        

    }
    render() {
        console.log("pagination rendeer")
        var pager = this.state.pager;
        const dim = this.calculatedim();

        if (!pager.pages || pager.pages.length <= 1) {
            // don't display pager if there is only 1 page
            return null;
        }

        return (
            <div className={styles.paginationDiv} style={dim}>
                <ul className={cx( "pagination" , "justify-content-center")}>
                    <li className={pager.currentPage === 1 ? "disabled page-item" : "page-item"}>
                        <a className="page-link" onClick={() => this.setPage(1)}>First</a>
                    </li>
                    <li className={pager.currentPage === 1 ? "disabled page-item" : "page-item"}>
                        <a className="page-link" onClick={() => this.setPage(pager.currentPage - 1)}>Previous</a>
                    </li>
                    {pager.pages.map((page, index) =>
                        <li key={index} className={pager.currentPage === page ? "active page-item" : "page-item"}>
                            <a  className="page-link" onClick={() => this.setPage(page)}>{page}</a>
                        </li>
                    )}
                    <li className={pager.currentPage === pager.totalPages ? "disabled page-item" : "page-item"}>
                        <a className="page-link" onClick={() => this.setPage(pager.currentPage + 1)}>Next</a>
                    </li>
                    <li className={pager.currentPage === pager.totalPages ? "disabled page-item" : "page-item"}>
                        <a  className="page-link" onClick={() => this.setPage(pager.totalPages)}>Last</a>
                    </li>
                </ul>
            </div >
        );
    }
}

function mapDispatchToProps(dispatch) {
    //   console.log("mapDispatchToProps pagination")
    return bindActionCreators({ getCultivars: getpedigreedata }, dispatch)
};


function mapStateToProps(state) {
    //   console.log("mapstatetoprops pagination")
    //  console.log(state.cultivars)
    return {
        totalItems : state.cultivarResponse != null ? state.cultivarResponse.total : 1,
        seachCriteria : state.seachCriteria
    }
};
//Pagination.propTypes = propTypes;
//Pagination.defaultProps = defaultProps;

export default connect(mapStateToProps, mapDispatchToProps)(Pagination);