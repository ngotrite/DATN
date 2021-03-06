/* 
 * Copyright YYYY Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
pageSetUp();
//Color legend
var color = ["#42A7FF", "#1269FF", "#C6FF7A", "#FFCF4D", "#FF9C45", "#FF482B"];
//Jvectormap
var map;
//HighChart
Highcharts.setOptions(Highcharts.theme);
Highcharts.setOptions({
    global: {
        useUTC: false
    }
});
//Interval for update revenue
var intervalRevenue;
//Websocket
var webSocket = new WebSocket("ws://" + document.location.host + "/" + window.location.pathname.split("/")[1] + "/prodspecSocket");
$(window).on("beforeunload", function () {
    webSocket.close();
    if (intervalRevenue !== null) {
        clearInterval(intervalRevenue);
    }
});
webSocket.onerror = function (event) {
    console.log(event);
};
webSocket.onopen = function () {
    console.log("CONNECTION IS OPENED !!!");
};
webSocket.onclose = function () {
    console.log("CONNECTION IS CLOSED");
    if (intervalRevenue !== null) {
        clearInterval(intervalRevenue);
    }
};
webSocket.onmessage = function (message) {
    var response = $.trim(message.data);
    if (isEmptyString(response)) {
        return;
    }
    var jsonResponse = $.parseJSON(response);
    if (jsonResponse.type === 1) {
        addNewPoint("revenueChart", jsonResponse);
    } else {
        var chartId;
        switch (parseInt(jsonResponse.service)) {
            case 1:
                chartId = "voiceChart";
                break;
            case 2:
                chartId = "dataChart";
                break;
            case 3:
                chartId = "smsChart";
                break;
            case 4:
                chartId = "mmsChart";
                break;
        }
        redrawChart(chartId, jsonResponse);
    }
};

$("a.btn").click(function () {
    $(this).closest("div.group-button").find("a").removeClass("btn-active");
    $(this).addClass("btn-active");
    $(this).blur();
});

getDateTimePicker();

function showFormProdSpecData(element) {
    //Reset interval update revenue
    if (intervalRevenue !== null) {
        clearInterval(intervalRevenue);
    }
    //Show data form
    var prodspecId = parseInt(element.value);
    if (prodspecId === 0) {
        $("#dataProdSpec").removeClass("form-show");
        $("#dataProdSpec").addClass("form-hidden");
    } else {
        $("#dataProdSpec").removeClass("form-hidden");
        $("#dataProdSpec").addClass("form-show");
    }
}

function drawAllCharts() {
    var prodspecId = parseInt($("#prodSpecId").text().trim());
    if (prodspecId === 0) {
        if (intervalRevenue !== null) {
            clearInterval(intervalRevenue);
        }
        return;
    }
    $("#voiceStat").empty();
    $("#dataStat").empty();
    $("#smsStat").empty();
    $("#mmsStat").empty();
    $("#voiceStat").circliful();
    $("#dataStat").circliful();
    $("#smsStat").circliful();
    $("#mmsStat").circliful();
    //Style growth rate
    $(".stat-rate span").each(function () {
        var text = $(this).text().trim();
        if (/[-]/g.test(text)) {
            text = text.substr(1);
            $(this).find("span").text(text);
            $(this).find("i").removeClass("fa-caret-up");
            $(this).find("i").addClass("fa-caret-down");
        } else if (text === "0%") {
            $(this).find("i").removeClass("fa-caret-up");
            $(this).find("i").addClass("fa-sort");
        }
    });
    //Query data by date time
    $(".btn-chart").click(function () {
        //Change style button
        $(this).closest("div.btn-group").find("button").removeClass("btn-active");
        $(this).addClass("btn-active");
        $(this).blur();
        //Get date time
        var optionTime = parseInt($(this).attr("option"));
        var serviceTab = $(this).closest("div.tab-pane").attr("id");
        var startTime = getDateTimeWithOption("#" + serviceTab + "-start", optionTime);
        var endTime = getDateTimeWithOption("#" + serviceTab + "-end", optionTime);
        webSocket.send(JSON.stringify({
            type: 0,
            prodspec: prodspecId,
            service: $(this).attr("service"),
            option: optionTime,
            start: startTime,
            end: endTime
        }));
    });
    $("#search-voice").click(function () {
        requestChart(1, 'option-voice', 's2', prodspecId);
    });
    $("#search-sms").click(function () {
        requestChart(3, 'option-voice', 's3', prodspecId);
    });
    $("#search-data").click(function () {
        requestChart(2, 'option-voice', 's4', prodspecId);
    });
    $("#search-mms").click(function () {
        requestChart(4, 'option-voice', 's5', prodspecId);
    });
    //Scroll
    $('html, body').animate({
        scrollTop: $('#dataProdSpec').offset().top
    }, 3000);
    drawRevenue("revenueChart");
    drawService("voiceChart", "Voice Service Growth");
    drawService("dataChart", "Data Service Growth");
    drawService("smsChart", "SMS Service Growth");
    drawService("mmsChart", "MMS Service Growth");
    drawPercentService("percentServiceChart");
    //Calendar
    getDateTimePicker();
    $("input[type='checkbox']#start_interval").click(function () {
        if ($(this).prop("checked")) {
            intervalRevenue = setInterval(function () {
                webSocket.send(JSON.stringify({
                    type: 1,
                    service: 0,
                    prodspec: $("#prodSpecId").text().trim()
                }));
            }, 300000);
        } else {
            if (intervalRevenue !== null) {
                clearInterval(intervalRevenue);
            }
        }
    });
    //Jvectormap
    $("#map").empty();
    map = new jvm.Map({
        map: "vietnam_map_en",
        container: $("#map"),
        zoomAnimate: true,
        regionsSelectable: true,
        backgroundColor: "#4c4f53",
        regionStyle: {
            initial: {
                fill: "white"
            }
        },
        series: {
            regions: [{
                    values: {},
                    normalizeFunction: "polynomial"
                }, {
                    values: {},
                    normalizeFunction: "polynomial"
                }]
        },
        onRegionTipShow: function (e, el, code) {
            var mapObject = $("#map").vectorMap("get", "mapObject");
            el.html(el.html() + ' (' + code.toString() + ')<br/>Value : ' + mapObject.series.regions[0].values[code]);
        }
    });
    $("#typeMap").on("change", function () {
        fillMap(parseInt(this.value));
    });
    fillMap(1);
}

function requestChart(service, option, idServiceTab, prodspecId) {
    var optionTime = parseInt($("#" + option).find('.btn-active').attr("option"));
    var startTime = $("#" + idServiceTab + "-start").find('.view').val().trim();
    var endTime = $("#" + idServiceTab + "-end").find('.view').val().trim();
    webSocket.send(JSON.stringify({
        type: 0,
        prodspec: prodspecId,
        service: service,
        option: optionTime,
        start: startTime,
        end: endTime
    }));
}
//Config highChart
function configOneChart() {
    var highChart = {
        exporting: {
            enabled: false
        },
        credits: {
            enabled: false
        },
        title: {
            text: ""
        },
        tooltip: {
            xDateFormat: "%d-%m-%Y"
        },
        xAxis: [{
                type: "datetime",
                title: {
                    text: "Time"
                },
                labels: {
                    formatter: function () {
                        return Highcharts.dateFormat("%d-%m", this.value);
                    }
                },
                categories: []
            }],
        yAxis: [{
                min: 0,
                gridLineWidth: 1,
                title: {
                    text: "Volume",
                    x: -15
                },
                labels: {
                    style: {
                        fontWeight: "bold"
                    },
                    align: "left",
                    format: "{value}"
                }
            }],
        legend: {
            verticalAlign: "top",
            layout: "vertical",
            align: "left",
            floating: true,
            x: 100,
            y: 20
        },
        series: [{
                name: "Volume",
                type: "spline",
                data: []
            }]
    };
    return highChart;
}

function configTwoChart() {
    var highChart = {
        exporting: {
            enabled: false
        },
        credits: {
            enabled: false
        },
        title: {
            text: "",
            floating: true,
            align: 'center'
        },
        tooltip: {
            shared: true,
            crosshairs: true,
            xDateFormat: "%d-%m-%Y"
        },
        xAxis: {
            type: "datetime",
            title: {
                text: "Time"
            },
            labels: {
                formatter: function () {
                    return Highcharts.dateFormat("%d-%m", this.value);
                }
            },
            categories: []
        },
        yAxis: [{
                min: 0,
                gridLineWidth: 1,
                title: {
                    text: "Volume",
                    x: -15
                },
                labels: {
                    style: {
                        fontWeight: "bold"
                    },
                    align: "left",
                    format: "{value}"
                }
            }, {
                min: 0,
                gridLineWidth: 0,
                title: {
                    text: "Revenue",
                    x: 15
                },
                labels: {
                    style: {
                        fontWeight: "bold"
                    },
                    align: "right",
                    format: "{value}"
                },
                opposite: true
            }],
        legend: {
            layout: "vertical",
            verticalAlign: "top",
            align: "left",
            y: -20,
            x: 100,
            floating: true
        },
        series: [{
                name: "Volume",
                type: "line",
                data: []
            }, {
                name: "Revenue",
                type: "spline",
                yAxis: 1,
                data: []
            }]
    };
    return highChart;
}

function configAreaChart() {
    var highChart = {
        chart: {
            height: 350,
            type: "area"
        },
        credits: {
            enabled: false
        },
        exporting: {
            enabled: false
        },
        title: {
            text: "Live Revenue"
        },
        legend: {
            verticalAlign: "top",
            align: "right",
            floating: true
        },
        plotOptions: {
            area: {
                fillColor: {
                    linearGradient: {
                        x1: 0,
                        y1: 0,
                        x2: 0,
                        y2: 1
                    },
                    stops: [
                        [0, Highcharts.getOptions().colors[0]],
                        [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get("rgba")]
                    ]
                },
                lineWidth: 1,
                marker: {
                    radius: 3
                }
            }
        },
        xAxis: {
            type: "datetime",
            dateTimeLabelFormats: {
                hour: "%H:%M"
            },
            labels: {
                style: {
                    fontWeight: "bold"
                }
            }
        },
        yAxis: {
            min: 0,
            floating: true,
            gridLineWidth: 1,
            title: {
                text: "Revenue"
            },
            labels: {
                style: {
                    fontWeight: "bold"
                }
            }
        },
        series: [{
                name: "Revenue",
                data: []
            }]
    };
    return highChart;
}

function configPieChart() {
    var highChart = {
        title: {
            text: "Revenue Rate By Service (Today)"
        },
        exporting: {
            enabled: false
        },
        credits: {
            enabled: false
        },
        chart: {
            height: 250,
            plotBackgroundColor: null,
            plotShadow: false,
            type: "pie"
        },
        tooltip: {
            pointFormat: "{series.name}: <b>{point.percentage:.1f}%</b>"
        },
        legend: {
            enabled: true,
            layout: "vertical",
            align: "right",
            verticalAlign: "top",
            useHTML: true,
            borderWidth: 0,
            y: 65,
            x: -30,
            itemStyle: {
                paddingBottom: "10px"
            },
            labelFormatter: function () {
                return '<div style="width:120px; font-size: 15px;"><span style="float:left">' + this.name + '</span><span style="float:right">' + this.y + ' %</span></div>';
            }
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: "pointer",
                dataLabels: {
                    enabled: false
                },
                showInLegend: true
            }
        },
        series: [{
                name: "Service",
                innerSize: "50%",
                colorByPoint: true,
                data: []
            }]
    };
    return highChart;
}

function configComboChart() {
    var highChart = {
        exporting: {
            enabled: false
        },
        credits: {
            enabled: false
        },
        chart: {
            height: 350
        },
        title: {
            text: ""
        },
        xAxis: {
            labels: {
                style: {
                    fontWeight: "bold"
                }
            },
            categories: []
        },
        yAxis: [],
        series: []
    };
    return highChart;
}
//Send update request 
function requestUpdateChart(prodspecId) {
    var request = {
        type: 1,
        service: 0,
        prodspec: prodspecId
    };
    webSocket.send(JSON.stringify(request));
}
//Draw chart
function drawRevenue(chartId) {
    var response = getParseJSON("value-" + chartId);
    if (!response) {
        $("#" + chartId).empty();
        return;
    }
    var highChart = configAreaChart();
    highChart.series[0].data = convertToArray(response);
    $("#" + chartId).highcharts(highChart);
}

function drawPercentService(chartId) {
    var highchart = configPieChart();
    var response = getParseJSON("value-" + chartId);
    $("#" + chartId).empty();
    if (response) {
        highchart.series[0].data = response;
    }
    $("#" + chartId).highcharts(highchart);
}

function drawService(chartId, nameChart) {
    var legend;
    var name;
    var chart;
    var configChart = configTwoChart();
    $("#" + chartId).empty();
    var response = getParseJSON("value-" + chartId);
    configChart.title.text = nameChart;
    if (response) {
        if (response.time.length <= 6) {
            configChart.series[0].type = "column";
            configChart.series[1].type = "column";
        }
        configChart.xAxis.categories = response.time;
        configChart.series[0].data = response.volume;
        configChart.series[1].data = response.revenue;
    }
    $("#" + chartId).highcharts(configChart);
    chart = $("#" + chartId).highcharts();
    for (var i in chart.legend.allItems) {
        legend = chart.legend.allItems[i];
        name = legend.name;
        legend.legendItem.attr({text: name + " " + response.unit[i]});
    }
}
//Update chart
function redrawChart(chartId, response) {
    var formatDate;
    var legend;
    var name;
    var optionTime = parseInt(response.option);
    var chart = $("#" + chartId).highcharts();
    if (optionTime === 2) {
        formatDate = "%d-%m";
    } else if (optionTime === 4) {
        formatDate = "%m-%Y";
    } else {
        formatDate = "%Y";
    }
    chart.xAxis[0].update({
        labels: {
            formatter: function () {
                return Highcharts.dateFormat(formatDate, this.value);
            }
        }
    });
    chart.xAxis[0].setCategories(response.data.time, false, true);
    chart.series[0].setData(response.data.volume, false, true);
    chart.series[1].setData(response.data.revenue, false, true);
    if (response.data.time.length <= 6) {
        chart.series[0].update({
            type: "column"
        });
        chart.series[1].update({
            type: "column"
        });
    } else {
        chart.series[0].update({
            type: "line"
        });
        chart.series[1].update({
            type: "spline"
        });
    }
    for (var i in chart.legend.allItems) {
        legend = chart.legend.allItems[i];
        name = legend.name;
        legend.legendItem.attr({text: name + " " + response.data.unit[i]});
    }
}

function addNewPoint(chartId, response) {
    var highChart = $("#" + chartId).highcharts();
    var data = response.point;
    highChart.series[0].addPoint([parseInt(data[0]), parseInt(data[1])], true, true);
}

function updateChart(chartId, response) {
    var chart = $("#" + chartId).highcharts();
    var arrayData = $.parseJSON(response);
    arrayData = convertToArray(arrayData);
    chart.series[0].setData(arrayData, true, true);
}
//Jvectormap
function intitalMap() {
    $("#map").empty();
    map = new jvm.Map({
        map: "vietnam_map_en",
        container: $("#map"),
        zoomAnimate: true,
        regionsSelectable: false,
        backgroundColor: "#4c4f53",
        regionStyle: {
            initial: {
                fill: "white"
            }
        },
        series: {
            regions: [{
                    values: {},
                    normalizeFunction: "polynomial"
                }, {
                    values: {},
                    normalizeFunction: "polynomial"
                }]
        },
        onRegionTipShow: function (event, label, code) {
            var mapObject = $('#map').vectorMap('get', 'mapObject');
            label.html(label.html() + ' (' + code.toString() + ')<br/>Value : ' + mapObject.series.regions[0].values[code]);
        }
    });
}

function fillMap(mapType) {
    var json = getParseJSON("formLocation\\:value-typeMap");
    if (jQuery.isEmptyObject(json)) {
        console.log("JSON OF MAP IS EMPTY");
        return;
    }
    var data = json["data"][mapType - 1];
    //Show map legend
    var legend = json["legend"][mapType - 1];
    var unit = json["unit"][mapType - 1];
    createLegend(legend, unit);
    if (jQuery.isEmptyObject(data)) {
        console.log("DATA OF MAP IS EMPTY");
        return;
    }
    //Fill map data
    var mapObject = $("#map").vectorMap("get", "mapObject");
    mapObject.series.regions[0].setValues(data);
    setRangeLegend(mapObject, legend);
}

function createLegend(legend, unit) {
    $("#legend-map").empty();
    var mapLegend = '';
    var indexRange = 0;
    var sizeLegend = legend.length - 1;
    if (unit !== "") {
        unit = ' (' + unit + ')';
    }
    if (sizeLegend === 0) {
        mapLegend += '<li><span style="background:' + color[0] + ';"></span> <= ' + legend[0].toLocaleString() + '</li>';
        mapLegend += '<li><span style="background:' + color[1] + ';"></span> >= ' + (legend[0] + 1).toLocaleString() + '</li>';
    } else {
        for (var index = 0; index <= sizeLegend; index++) {
            mapLegend += '<li><span style="background:' + color[indexRange] + ';"></span>';
            if (index === 0) {
                mapLegend += ' <= ' + legend[index].toLocaleString() + unit + '</li>';
                indexRange++;
                mapLegend += '<li><span style="background:' + color[indexRange] + ';"></span>';
                mapLegend += (legend[index] + 1).toLocaleString() + ' - ' + legend[index + 1].toLocaleString();
            } else if (index === sizeLegend) {
                mapLegend += ' >= ' + (legend[index] + 1).toLocaleString();
            } else {
                mapLegend += (legend[index] + 1).toLocaleString() + ' - ' + legend[index + 1].toLocaleString();
            }
            mapLegend += unit + '</li>';
            indexRange++;
        }
    }
    $("#legend-map").append(mapLegend);
}

function setRangeLegend(mapObject, legend) {
    var range;
    var result = '{';
    var data = mapObject.series.regions[0].values;
    $.each(data, function (key, value) {
        range = getIndexRange(legend, parseInt(value));
        result += '"' + key + '\":"' + color[range] + '",';
    });
    result = result.slice(0, -1);
    result += '}';
    mapObject.series.regions[1].setValues($.parseJSON(result));
}

function getIndexRange(legend, value) {
    for (var i = 0; i < legend.length; i++) {
        if (value <= legend[i]) {
            return i;
        }
    }
    return legend.length;
}
//JSON
function getParseJSON(tagId) {
    var value = $("#" + tagId).text();
    value = $.trim(value);
    return isEmptyString(value) === true ? null : $.parseJSON(value);
}

function getDateTimeWithOption(id, optionTime) {
    var dateTime;
    switch (optionTime) {
        case 2 :
            optionTime = 0;
            break;
        case 4 :
            optionTime = 1;
            break;
        case 8 :
            optionTime = 2;
            break;
    }
    $(id).children().each(function (i) {
        if (i === optionTime) {
            $(this).removeClass("hide");
            $(this).addClass("view");
            dateTime = $(this).val().trim();
        } else {
            $(this).removeClass("view");
            $(this).addClass("hide");
        }
    });
    return dateTime;
}
//Convert
function getNumberWithCommas(number) {
    return number.toString().replace(/\d(?=(\d{3})+(?!\d))/g, ",");
}

function convertToArray(input) {
    var valueArray = [];
    for (var i in input) {
        valueArray.push([parseInt(input[i][0]), parseInt(input[i][1])]);
    }
    return valueArray;
}
//Check null
function isEmptyString(string) {
    string = $.trim(string);
    return (!string || (0 === string.length));
}
//Date time
function getDateTimePicker() {
    $('.dateTimePicker').datepicker({
        todayBtn: "linked",
        todayHighlight: true,
        format: "dd-mm-yyyy",
        autoclose: true
    }).on("show", function (event) {
        if (event.date) {
            $(this).data("stickyDate", event.date);
        } else {
            $(this).data("stickyDate", null);
        }
    }).on("hide", function (event) {
        var stickyDate = $(this).data("stickyDate");
        if (!event.date && stickyDate) {
            $(this).datepicker('setDate', stickyDate);
            $(this).data("stickyDate", null);
        }
    });

    $('.monthTimePicker').datepicker({
        format: "mm-yyyy",
        viewMode: "months",
        minViewMode: "months",
        autoclose: true
    }).on("show", function (event) {
        if (event.date) {
            $(this).data("stickyDate", event.date);
        } else {
            $(this).data("stickyDate", null);
        }
    }).on("hide", function (event) {
        var stickyDate = $(this).data("stickyDate");
        if (!event.date && stickyDate) {
            $(this).datepicker('setDate', stickyDate);
            $(this).data("stickyDate", null);
        }
    });

    $('.yearTimePicker').datepicker({
        format: "yyyy",
        viewMode: "years",
        minViewMode: "years",
        autoclose: true
    }).on("show", function (event) {
        if (event.date) {
            $(this).data("stickyDate", event.date);
        } else {
            $(this).data("stickyDate", null);
        }
    }).on("hide", function (event) {
        var stickyDate = $(this).data("stickyDate");
        if (!event.date && stickyDate) {
            $(this).datepicker('setDate', stickyDate);
            $(this).data("stickyDate", null);
        }
    });
}

function isDateFormat(dateString) {
    if (isEmptyString(dateString)) {
        return false;
    }
    var value = dateString.match(/^(\d{1,2})-(\d{1,2})-(\d{4})$/);
    var day = parseInt(value[1]);
    var month = parseInt(value[2]);
    var year = parseInt(value[3]);
    if (day < 1 || day > 31 || month < 1 || month > 12 || day > dayInMonth(month, year)) {
        return false;
    }
    return true;
}

function isMonthFomat(monthString) {
    if (isEmptyString(monthString)) {
        return false;
    }
    var value = monthString.match(/^(\d{1,2})-(\d{4})$/);
    var month = parseInt(value[1]);
    if (month < 1 || month > 12) {
        return false;
    }
    return true;
}

function isYearFomat(yearString) {
    if (isEmptyString(yearString)) {
        return false;
    }
    return /^(\d{4})$/.test(yearString);
}

function dayInMonth(month, year) {
    switch (month) {
        case 2 :
            return ((year % 4 === 0) && (year % 100)) || (year % 400 === 0) ? 29 : 28;
        case 4 :
        case 6 :
        case 9 :
        case 11 :
            return 30;
        default :
            return 31;
    }
}
//Change type map
function changeActiveButton(element) {
    var $this = $("#" + element);
    $this.closest("div.group-button").find("a").removeClass("btn-active");
    $this.addClass("btn-active");
    $this.blur();
}

function changeTypeOption(id, optionTime) {
    var dateTime;
    switch (optionTime) {
        case 2 :
            optionTime = 0;
            break;
        case 4 :
            optionTime = 1;
            break;
        case 8 :
            optionTime = 2;
            break;
    }
    $(id).children().each(function (i) {
        if (i === optionTime) {
            $(this).removeClass("hide");
            $(this).addClass("view");
            dateTime = $(this).val().trim();
        } else {
            $(this).removeClass("view");
            $(this).addClass("hide");
        }
    });
    return dateTime;
}

function changeDataMap(option) {
    changeTypeOption("#option-location", option);
    //Reload map
    intitalMap();
    fillMap(parseInt($("#typeMap").val()));
}