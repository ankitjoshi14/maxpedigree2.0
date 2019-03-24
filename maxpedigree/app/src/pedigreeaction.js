
import axios from "axios";
import Searchbar from "./Searchbar";
import saveAs from 'file-saver';


  

export const uploadfile = (selectedFile, FileType, uploadType) => {

    const data = new FormData()
    data.append('file', selectedFile);
    data.append('name', FileType);
    data.append('upload', uploadType);
    const endpoint = "http://localhost:8085/maxpedigree/uploadfile"
    return (dispatch) => {
        axios.post(endpoint, data).then((res) => {
            console.log("ka", res.data);

            dispatch({
                type: "uploadfile",
                payload: res.data
            });
        })
            .catch((err) => {
                console.log("AXIOS ERROR: ", err);
            })
    }
};

export const downloadFile = (cutlivarids, fileType) => {

    const data = new FormData()
    data.append('ids', cutlivarids);
    data.append('filetype', fileType);

    let axiosConfig = {
        headers: {
            responseType: 'blob'
        }
    };
    const endpoint = "http://localhost:8085/maxpedigree/createfile"
   let fileName ="";
    return (dispatch) => {
        axios.post(endpoint, data).then((response) => {
            fileName = response.data;
           
            let downloadlink = "http://localhost:8085/maxpedigree/downloadfile/"
            downloadlink = downloadlink.concat(`${fileName}`);
            window.location.href= downloadlink;
            return (dispatch) => {
                dispatch({
                    type: "lightbox",
                payload: false
                });
            }
                            
        })
            .catch((err) => {
                console.log("AXIOS ERROR: ", err);
            })
            
    }
    

};


export const setSearchCriteria = (seachCriteria) => {
    return (dispatch) => {
        dispatch({
            type: "setSearchCriteria",
            payload: seachCriteria
        });

    }
};


export const getpedigreedata = (seachCriteria) => {

    console.log("getall action");
    // var exampleItems = [...Array(150).keys()].map(i => ({ id: (i + 1), name: 'Item ' + (i + 1) }));

    // let items  = cultivarData();
    // var endIndex = startIndex + limit;
    //  endIndex = (endIndex > items.length ? items.length : endIndex);
    //  items.map((item)=>{
    //     return item.selected = true;
    //  });
    //   console.log("start end " + startIndex +" "+endIndex)
    // var selected = items.slice(startIndex, endIndex );
    // selected.total = items.length;
    let start = seachCriteria.start;
    seachCriteria.start = ((start - 1) * seachCriteria.limit);
    var postData = seachCriteria;
    let axiosConfig = {
        headers: {
            "Access-Control-Allow-Origin": "*"
        }
    };


    return (dispatch) => {
        axios.post('http://localhost:8085/maxpedigree/cultivars', postData, axiosConfig)
            .then((res) => {
                console.log("inside")
                console.log(res);
                dispatch({
                    type: "getall",
                    payload: res.data
                });
            })
            .catch((err) => {
                console.log("AXIOS ERROR: ", err);
            })


    }
};


export const pedigree = (cultivarid) => {

    console.log("pedigree action called");
    // var exampleItems = [...Array(150).keys()].map(i => ({ id: (i + 1), name: 'Item ' + (i + 1) }));

    // let items  = cultivarData();
    // var endIndex = startIndex + limit;
    //  endIndex = (endIndex > items.length ? items.length : endIndex);
    //  items.map((item)=>{
    //     return item.selected = true;
    //  });
    //   console.log("start end " + startIndex +" "+endIndex)
    // var selected = items.slice(startIndex, endIndex );
    // selected.total = items.length;

    let axiosConfig = {
        headers: {
            "Access-Control-Allow-Origin": "*"
        },
        params: {
            id: cultivarid
        }
    };


    return (dispatch) => {
        axios.get('http://localhost:8085//maxpedigree/pedigree', axiosConfig)
            .then((res) => {
                console.log("inside")
                console.log(res);
                dispatch({
                    type: "pedigree",
                    payload: res.data
                });
            })
            .catch((err) => {
                console.log("AXIOS ERROR: ", err);
            })


    }
};


export const hcluster = (cutlivarids) => {
    let postData = cutlivarids;
    let axiosConfig = {
        headers: {
            "Access-Control-Allow-Origin": "*"
        }
    };

    return (dispatch) => {
        axios.post('http://localhost:8085//maxpedigree/hcluster', postData, axiosConfig)
            .then((res) => {
                dispatch({
                    type: "hcluster",
                    payload: res.data
                });
            })
            .catch((err) => {
                console.log("AXIOS ERROR in pca: ", err);
            })


    }
}

export const selectednodePC = (node) => {
    return (dispatch) => {
        dispatch({
            type: "selectednodePC",
            payload: node
        });
    }
}

export const selectednodePedigree = (node) => {
    return (dispatch) => {
        dispatch({
            type: "selectednodePedigree",
            payload: node
        });
    }
}

export const selectednodeHCluster = (node) => {
    return (dispatch) => {
        dispatch({
            type: "selectednodeHCluster",
            payload: node
        });
    }
}



export const selectedAttributePC = (attribute) => {
    return (dispatch) => {
        dispatch({
            type: "selectedAttributePC",
            payload: attribute
        });
    }
}


export const selectedAttributePedigree = (attribute) => {
    return (dispatch) => {
        dispatch({
            type: "selectedAttributePedigree",
            payload: attribute
        });
    }
}

export const pca = (cutlivarids) => {
    let postData = cutlivarids;
    let axiosConfig = {
        headers: {
            "Access-Control-Allow-Origin": "*"
        }
    };

    return (dispatch) => {
        axios.post('http://localhost:8085//maxpedigree/pca', postData, axiosConfig)
            .then((res) => {
                dispatch({
                    type: "pca",
                    payload: res.data
                });
            })
            .catch((err) => {
                console.log("AXIOS ERROR in pca: ", err);
            })


    }
}



export const selectcultivar = (id) => {
    return {
        type: "select",
        payload: id
    }
}

export const selectcultivarall = (searchCriteria) => {
    let postData = searchCriteria;

    return (dispatch) => {
        axios.post('http://localhost:8085//maxpedigree/cultivarids', postData)
            .then((res) => {
               
               
                    dispatch({
                        type: "selectall",
                        payload: res.data.cultivarids
                    });

               

            })
            .catch((err) => {
                console.log("AXIOS ERROR: ", err);
            })


    }

}

export const deselectcultivar = (id) => {
    return {
        type: "deselect",
        payload: id
    }
}

export const deselectcultivarall = () => {

    return {
        type: "deselectall",
        payload: null
    }
}


export const showLightbox = (show) => {
    return {
        type: "lightbox",
        payload: show
    }
}




