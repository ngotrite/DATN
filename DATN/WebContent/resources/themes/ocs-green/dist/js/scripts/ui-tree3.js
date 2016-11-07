var UITree = function () {

    var handleSample1 = function () {

        var parentReportMenu = function(node){
            if(node.id === 'rate-table'){
                return reportMenu(node)
            }
        };

        var reportMenu = function(node) {
            // access node as: node.id);
            if(node.id === 'rate-table'){
                // build your menu depending on node id
                return {
                    "1.Add New Rate Table": {
                        "icon": "fa fa-plus",
                        "separator_before": false,
                        "separator_after": false,
                        "label": "1.Add New Rate Table",
                        "action": function (obj) {
                            $('#create-rate-tbl').modal('show');
                        }
                    },
                    "2.Edit Rate Table": {
                        "icon": "fa fa-pencil",
                        "separator_before": false,
                        "separator_after": false,
                        "label": "2.Edit Rate Table",
                        "action": function (obj) {

                        }
                    },
                    "3.Check Dependencies": {
                        "icon": "fa fa-check",
                        "separator_before": false,
                        "separator_after": false,
                        "label": "3.Check Dependencies",
                        "action": function (obj) {

                        }
                    },
                    "4.Move Up": {
                        "icon": "fa fa-arrow-up",
                        "separator_before": false,
                        "separator_after": false,
                        "label": "4.Move Up",
                        "action": function (obj) {

                        }
                    },
                    "5.Move Down": {
                        "icon": "fa fa-arrow-down",
                        "separator_before": false,
                        "separator_after": false,
                        "label": "5.Move Down",
                        "action": function (obj) {

                        }
                    },
                    "6.Remove Rate Table": {
                        "icon": "fa fa-times",
                        "separator_before": false,
                        "separator_after": false,
                        "label": "6.Remove Rate Table",
                        "action": function (obj) {

                        }
                    }
                };
            }
            else {
               return false;
            }
        };

        $('#offer-tree').jstree({
            'plugins': ["types", "contextmenu", "search"],
            "contextmenu":{
                'select_node': false,
                "items": parentReportMenu
            },
            'core': {
                "themes" : {
                    "responsive": false
                },

                'data': [
                    {
                        "text": "Offer Templates",
                        "icon": "fa fa-th",
                        "a_attr": {
                            "href": "/ocs/theme-3/offer-template.html"
                        },
                        "state": {
                            "opened": true
                        },
                        "children": [
                            {
                                "text": "MI Templates",
                                "state": {
                                    "opened": true
                                },
                                "children": [
                                    {
                                        "text": "Deploying",
                                        "state": {
                                            "opened": true
                                        },
                                        "children": [
                                            {
                                                "text": "MI",
                                                "icon": "fa fa-trello",
                                                "a_attr": {
                                                    "href": "/ocs/theme-3/offer-template-detail.html"
                                                },
                                                "state": {
                                                    "opened": true
                                                },
                                                "children": [
                                                    {
                                                        "text": "MI_Ver_1.0",
                                                        "icon": "fa fa-code-fork",
                                                        "state": {
                                                            "opened": true
                                                        },
                                                        "children": [
                                                            {
                                                                "text": "Register/Purchase",
                                                                "icon": "fa fa-bookmark",
                                                                "a_attr": {
                                                                    "href": "/ocs/theme-3/offer-action-detail.html"
                                                                },
                                                                "state": {
                                                                    "opened": true
                                                                },
                                                                "children": [
                                                                    {
                                                                        "text": "MI Purchase Componence",
                                                                        "icon": "fa fa-th-list",
                                                                        "a_attr": {
                                                                            "href": "/ocs/theme-3/offer-pricecomponent-detail.html"
                                                                        },
                                                                        "state": {
                                                                            "opened": true
                                                                        },
                                                                        "children": [
                                                                            {
                                                                                "text": "MI Block",
                                                                                "icon": "fa fa-th-large",
                                                                                "a_attr": {
                                                                                    "href": "/ocs/theme-3/offer-block-detail.html"
                                                                                },
                                                                                "state": {
                                                                                    "opened": true
                                                                                },
                                                                                "children": [
                                                                                    {
                                                                                        "id": "rate-table",
                                                                                        "text": "Rate Table for Purchase",
                                                                                        "icon": "fa fa-line-chart",
                                                                                        "a_attr": {
                                                                                            "href": "/ocs/theme-3/offer-rate-table.html"
                                                                                        }
                                                                                    },
                                                                                    {
                                                                                        "text": "Rate Table 2",
                                                                                        "icon": "fa fa-line-chart",
                                                                                        "a_attr": {
                                                                                            "href": "/ocs/theme-3/offer-rate-table.html"
                                                                                        }
                                                                                    }
                                                                                ]
                                                                            }
                                                                        ]
                                                                    }
                                                                ]
                                                            }
                                                        ]
                                                    }
                                                ]
                                            }
                                        ]
                                    },
                                    {
                                        "text": "Not in Used",
                                        "state": {
                                            "opened": true
                                        },
                                        "children": [
                                            {
                                                "text": "MI_Old",
                                                "icon": "fa fa-trello",
                                                "state": {
                                                    "opened": true
                                                },
                                                "children": [
                                                    {
                                                        "text": "MI_Old_Ver_1.0",
                                                        "icon": "fa fa-code-fork",
                                                    }
                                                ]
                                            }
                                        ]
                                    }
                                ]
                            },
                            {
                                "text": "Voice Templates"
                            },
                            {
                                "text": "SMS Templates"
                            },
                            {
                                "text": "FTTH Templates"
                            }
                        ]
                    },
                    {
                        "text": "bla bla"
                    }
                ]
            },
            "types" : {
                "default" : {
                    "icon" : "fa fa-folder icon-state-warning icon-lg"
                },
                "file" : {
                    "icon" : "fa fa-file icon-state-warning icon-lg"
                }
            }
        });

        var to = false;
        $("#filter-offer-tree").keyup(function(){
            if(to) { clearTimeout(to); }
            to = setTimeout(function () {
                var v = $('#filter-offer-tree').val();
                $('#offer-tree').jstree(true).search(v);
            }, 250);
        });

        // handle link clicks in tree nodes(support target="_blank" as well)
        $('#offer-tree').on('select_node.jstree', function(e, data) {
            var url = '';
            var link = $('#' + data.selected).find('a');
            if (link.attr("href") != "#" && link.attr("href") != "javascript:;" && link.attr("href") != "") {
                if (link.attr("target") == "_blank") {
                    link.attr("href").target = "_blank";
                }
                //document.location.href = link.attr("href");
                url = link.attr("href");
                //return false;
            }
            if (url != '') {
                $("#holder-content").fadeOut('400', function(){
                    $(this).load(url, function(response){
                        //console.log(response);
                        $(this).fadeIn('400');
                    });
                });

            } else {
                $("#holder-content").html('');
            }
        });
    }

    var handleSample2 = function(){
        $("#normalizer-tree").jstree({
            'plugins': ["types", "search"],
            'core': {
                "themes" : {
                    "responsive": false
                },
                "data": [
                    {
                        "text": "Normalizers",
                        "icon": "fa fa-arrows",
                        "a_attr": {
                            "href": "/ocs/theme-3/normalizer-category.html"
                        },
                        "state": {
                            "opened": true
                        },
                        "children": [
                            {
                                "text": "Balance Range",
                                "state": {
                                    "opened": true
                                },
                                "children": [
                                    {
                                        "text": "BR Data Over 100Mb",
                                        "icon": "fa fa-arrow-circle-right",
                                        "a_attr": {
                                            "href": "/ocs/theme-3/normalizer-define.html"
                                        },
                                    }
                                ]
                            },
                            {
                                "text": "Call Type",
                                "state": {
                                    "opened": true
                                },
                                "children": [
                                    {
                                        "text": "Call Forwarding",
                                        "icon": "fa fa-arrow-circle-right"
                                    },
                                    {
                                        "text": "Local Call",
                                        "icon": "fa fa-arrow-circle-right"
                                    },
                                    {
                                        "text": "National Calls",
                                        "state": {
                                            "opened": true
                                        },
                                        "children": [
                                            {
                                                "text": "National Zone 1 Calls",
                                                "icon": "fa fa-arrow-circle-right"
                                            },
                                            {
                                                "text": "National Zone 2 Calls",
                                                "icon": "fa fa-arrow-circle-right"
                                            },
                                            {
                                                "text": "National Zone 3 Calls",
                                                "icon": "fa fa-arrow-circle-right"
                                            },
                                        ]
                                    }
                                ]
                            },
                            {
                                "text": "Date Match"
                            },
                            {
                                "text": "Device Type"
                            },
                            {
                                "text": "Group Members"
                            },
                            {
                                "text": "IPV4 Match"
                            },
                            {
                                "text": "IPV6 Match"
                            },
                            {
                                "text": "Phone Number Match"
                            },
                            {
                                "text": "Locations"
                            }
                        ]
                    }
                ]
            },
            'types': {
                "default" : {
                    "icon" : "fa fa-folder icon-state-warning icon-lg"
                },
                "file" : {
                    "icon" : "fa fa-file icon-state-warning icon-lg"
                }
            }
        });

        var to = false;
        $("#filter-normalizer-tree").keyup(function(){
            if(to) { clearTimeout(to); }
            to = setTimeout(function () {
                var v = $('#filter-normalizer-tree').val();
                $('#normalizer-tree').jstree(true).search(v);
            }, 250);
        });

        // handle link clicks in tree nodes(support target="_blank" as well)
        $('#normalizer-tree').on('select_node.jstree', function(e, data) {
            var url = '';
            var link = $('#' + data.selected).find('a');
            if (link.attr("href") != "#" && link.attr("href") != "javascript:;" && link.attr("href") != "") {
                if (link.attr("target") == "_blank") {
                    link.attr("href").target = "_blank";
                }
                //document.location.href = link.attr("href");
                url = link.attr("href");
                //return false;
            }
            if (url !== '') {
                $("#holder-container").fadeOut('400', function(){
                    $(this).load(url, function(response){
                        //console.log(response);
                        $(this).fadeIn('400');
                    });
                });
            } else {
                $("#holder-container").html('');
            }
        });
    }

    var handleSample3 = function () {
        $("#table-tree").jstree({
            'plugins': ["types", "search"],
            'core': {
                "themes" : {
                    "responsive": false
                },
                "data": [
                    {
                        "text": "Rate Table",
                        "icon": "fa fa-database",
                        "state": {
                            "opened": true
                        },
                        "children": [
                            {
                                "text": "Charging",
                                "state": {
                                    "opened": true
                                },
                                "children": [
                                    {
                                        "text": "Mobile",
                                        "state": {
                                            "opened": true
                                        },
                                        "children": [
                                            {
                                                "text": "Data",
                                                "state": {
                                                    "opened": true
                                                },
                                                "children": [
                                                    {
                                                        "text": "InternetData Charging RT",
                                                        "icon": "fa fa-exchange"
                                                    },
                                                    {
                                                        "text": "Youtube Charging RT",
                                                        "icon": "fa fa-exchange"
                                                    }
                                                ]
                                            },
                                            {
                                                "text": "Voice",
                                                "state": {
                                                    "opened": true
                                                },
                                                "children": [
                                                    {
                                                        "text": "Voice Charging RT",
                                                        "icon": "fa fa-exchange"
                                                    },
                                                    {
                                                        "text": "Local Call Charging RT",
                                                        "icon": "fa fa-exchange"
                                                    }
                                                ]
                                            },
                                            {
                                                "text": "Sharing",
                                                "state": {
                                                    "opened": true
                                                },
                                                "children": [
                                                    {
                                                        "text": "Data Share Charging RT",
                                                        "icon": "fa fa-exchange"
                                                    },
                                                    {
                                                        "text": "MMS Charging RT",
                                                        "icon": "fa fa-exchange"
                                                    }
                                                ]
                                            }
                                        ]
                                    },
                                    {
                                        "text": "FTTH",
                                        "state": {
                                            "opened": true
                                        },
                                        "children": [
                                            {
                                                "text": "FTTH Charging RT",
                                                "icon": "fa fa-exchange"
                                            }
                                        ]
                                    }
                                ]
                            },
                            {
                                "text": "Topup",
                                "state": {
                                    "opened": true
                                },
                                "children": [
                                    {
                                        "text": "From BCCS",
                                        "state": {
                                            "opened": true
                                        },
                                        "children": [
                                            {
                                                "text": "Register RT",
                                                "icon": "fa fa-exchange"
                                            },
                                            {
                                                "text": "Purchase Charging RT",
                                                "icon": "fa fa-exchange"
                                            }
                                        ]
                                    },
                                    {
                                        "text": "From BlankPlus",
                                        "state": {
                                            "opened": true
                                        },
                                        "children": [
                                            {
                                                "text": "BlankPlus Transation RT",
                                                "icon": "fa fa-exchange"
                                            }
                                        ]
                                    },
                                    {
                                        "text": "From Other OSC",
                                        "state": {
                                            "opened": true
                                        },
                                        "children": [
                                            {
                                                "text": "Money Transfer RT",
                                                "icon": "fa fa-exchange"
                                            }
                                        ]
                                    }
                                ]
                            },
                            {
                                "text": "Policies",
                                "state": {
                                    "opened": true
                                },
                                "children": [
                                    {
                                        "text": "Mobile Data Policy RT",
                                        "icon": "fa fa-exchange"
                                    },
                                    {
                                        "text": "FTTH Data Policy RT",
                                        "icon": "fa fa-exchange"
                                    }
                                ]
                            }
                        ]
                    }
                ]
            },
            'types': {
                "default" : {
                    "icon" : "fa fa-folder icon-state-warning icon-lg"
                },
                "file" : {
                    "icon" : "fa fa-file icon-state-warning icon-lg"
                }
            }
        });

        var to = false;
        $("#filter-table-tree").keyup(function(){
            if(to) { clearTimeout(to); }
            to = setTimeout(function () {
                var v = $('#filter-table-tree').val();
                $('#table-tree').jstree(true).search(v);
            }, 250);
        });
    }

    var contextualMenuSample = function() {

        $("#tree_3").jstree({
            "core" : {
                "themes" : {
                    "responsive": false
                }, 
                // so that create works
                "check_callback" : true,
                'data': [{
                        "text": "Parent Node",
                        "children": [{
                            "text": "Initially selected",
                            "state": {
                                "selected": true
                            }
                        }, {
                            "text": "Custom Icon",
                            "icon": "fa fa-warning icon-state-danger"
                        }, {
                            "text": "Initially open",
                            "icon" : "fa fa-folder icon-state-success",
                            "state": {
                                "opened": true
                            },
                            "children": [
                                {"text": "Another node", "icon" : "fa fa-file icon-state-warning"}
                            ]
                        }, {
                            "text": "Another Custom Icon",
                            "icon": "fa fa-warning icon-state-warning"
                        }, {
                            "text": "Disabled Node",
                            "icon": "fa fa-check icon-state-success",
                            "state": {
                                "disabled": true
                            }
                        }, {
                            "text": "Sub Nodes",
                            "icon": "fa fa-folder icon-state-danger",
                            "children": [
                                {"text": "Item 1", "icon" : "fa fa-file icon-state-warning"},
                                {"text": "Item 2", "icon" : "fa fa-file icon-state-success"},
                                {"text": "Item 3", "icon" : "fa fa-file icon-state-default"},
                                {"text": "Item 4", "icon" : "fa fa-file icon-state-danger"},
                                {"text": "Item 5", "icon" : "fa fa-file icon-state-info"}
                            ]
                        }]
                    },
                    "Another Node"
                ]
            },
            "types" : {
                "default" : {
                    "icon" : "fa fa-folder icon-state-warning icon-lg"
                },
                "file" : {
                    "icon" : "fa fa-file icon-state-warning icon-lg"
                }
            },
            "state" : { "key" : "demo2" },
            "plugins" : [ "contextmenu", "dnd", "state", "types" ]
        });
    
    }


    return {
        //main function to initiate the module
        init: function () {
            handleSample1();
            handleSample2();
            handleSample3();
        }

    };

}();