/*
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
var showControllersOnly = false;
var seriesFilter = "";
var filtersOnlySampleSeries = true;

/*
 * Add header in statistics table to group metrics by category
 * format
 *
 */
function summaryTableHeader(header) {
    var newRow = header.insertRow(-1);
    newRow.className = "tablesorter-no-sort";
    var cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 1;
    cell.innerHTML = "Requests";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 3;
    cell.innerHTML = "Executions";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 7;
    cell.innerHTML = "Response Times (ms)";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 1;
    cell.innerHTML = "Throughput";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 2;
    cell.innerHTML = "Network (KB/sec)";
    newRow.appendChild(cell);
}

/*
 * Populates the table identified by id parameter with the specified data and
 * format
 *
 */
function createTable(table, info, formatter, defaultSorts, seriesIndex, headerCreator) {
    var tableRef = table[0];

    // Create header and populate it with data.titles array
    var header = tableRef.createTHead();

    // Call callback is available
    if(headerCreator) {
        headerCreator(header);
    }

    var newRow = header.insertRow(-1);
    for (var index = 0; index < info.titles.length; index++) {
        var cell = document.createElement('th');
        cell.innerHTML = info.titles[index];
        newRow.appendChild(cell);
    }

    var tBody;

    // Create overall body if defined
    if(info.overall){
        tBody = document.createElement('tbody');
        tBody.className = "tablesorter-no-sort";
        tableRef.appendChild(tBody);
        var newRow = tBody.insertRow(-1);
        var data = info.overall.data;
        for(var index=0;index < data.length; index++){
            var cell = newRow.insertCell(-1);
            cell.innerHTML = formatter ? formatter(index, data[index]): data[index];
        }
    }

    // Create regular body
    tBody = document.createElement('tbody');
    tableRef.appendChild(tBody);

    var regexp;
    if(seriesFilter) {
        regexp = new RegExp(seriesFilter, 'i');
    }
    // Populate body with data.items array
    for(var index=0; index < info.items.length; index++){
        var item = info.items[index];
        if((!regexp || filtersOnlySampleSeries && !info.supportsControllersDiscrimination || regexp.test(item.data[seriesIndex]))
                &&
                (!showControllersOnly || !info.supportsControllersDiscrimination || item.isController)){
            if(item.data.length > 0) {
                var newRow = tBody.insertRow(-1);
                for(var col=0; col < item.data.length; col++){
                    var cell = newRow.insertCell(-1);
                    cell.innerHTML = formatter ? formatter(col, item.data[col]) : item.data[col];
                }
            }
        }
    }

    // Add support of columns sort
    table.tablesorter({sortList : defaultSorts});
}

$(document).ready(function() {

    // Customize table sorter default options
    $.extend( $.tablesorter.defaults, {
        theme: 'blue',
        cssInfoBlock: "tablesorter-no-sort",
        widthFixed: true,
        widgets: ['zebra']
    });

    var data = {"OkPercent": 100.0, "KoPercent": 0.0};
    var dataset = [
        {
            "label" : "FAIL",
            "data" : data.KoPercent,
            "color" : "#FF6347"
        },
        {
            "label" : "PASS",
            "data" : data.OkPercent,
            "color" : "#9ACD32"
        }];
    $.plot($("#flot-requests-summary"), dataset, {
        series : {
            pie : {
                show : true,
                radius : 1,
                label : {
                    show : true,
                    radius : 3 / 4,
                    formatter : function(label, series) {
                        return '<div style="font-size:8pt;text-align:center;padding:2px;color:white;">'
                            + label
                            + '<br/>'
                            + Math.round10(series.percent, -2)
                            + '%</div>';
                    },
                    background : {
                        opacity : 0.5,
                        color : '#000'
                    }
                }
            }
        },
        legend : {
            show : true
        }
    });

    // Creates APDEX table
    createTable($("#apdexTable"), {"supportsControllersDiscrimination": true, "overall": {"data": [0.9385384615384615, 500, 1500, "Total"], "isController": false}, "titles": ["Apdex", "T (Toleration threshold)", "F (Frustration threshold)", "Label"], "items": [{"data": [0.986, 500, 1500, "11 Roles"], "isController": false}, {"data": [0.955, 500, 1500, "08 Wallet and transactions"], "isController": false}, {"data": [0.962, 500, 1500, "06 Books"], "isController": false}, {"data": [0.959, 500, 1500, "10 Finance overview"], "isController": false}, {"data": [0.799, 500, 1500, "01 Login"], "isController": false}, {"data": [0.947, 500, 1500, "04 Attendance projects"], "isController": false}, {"data": [0.955, 500, 1500, "05 Attendance records"], "isController": false}, {"data": [0.984, 500, 1500, "09 Merchant query"], "isController": false}, {"data": [0.949, 500, 1500, "12 Excel import page"], "isController": false}, {"data": [0.905, 500, 1500, "02 Personnel query"], "isController": false}, {"data": [0.915, 500, 1500, "13 Card page"], "isController": false}, {"data": [0.917, 500, 1500, "03 Gate records"], "isController": false}, {"data": [0.968, 500, 1500, "07 Borrowing records"], "isController": false}]}, function(index, item){
        switch(index){
            case 0:
                item = item.toFixed(3);
                break;
            case 1:
            case 2:
                item = formatDuration(item);
                break;
        }
        return item;
    }, [[0, 0]], 3);

    // Create statistics table
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 6500, 0, 0.0, 195.99584615384592, 1, 1841, 109.0, 560.0, 669.0, 1032.9799999999996, 1823.793490460157, 4393.024209104939, 492.9382803731762], "isController": false}, "titles": ["Label", "#Samples", "FAIL", "Error %", "Average", "Min", "Max", "Median", "90th pct", "95th pct", "99th pct", "Transactions/s", "Received", "Sent"], "items": [{"data": ["11 Roles", 500, 0, 0.0, 85.47600000000004, 1, 922, 4.0, 304.90000000000003, 433.5999999999999, 774.4200000000005, 241.196333815726, 243.08068017366136, 52.035282501205984], "isController": false}, {"data": ["08 Wallet and transactions", 500, 0, 0.0, 148.86000000000007, 1, 1236, 10.0, 465.90000000000003, 627.3999999999999, 1030.7500000000002, 233.53573096683795, 730.2552837459132, 51.979032870154136], "isController": false}, {"data": ["06 Books", 500, 0, 0.0, 142.5339999999999, 4, 1234, 12.5, 449.4000000000002, 603.95, 881.95, 226.03978300180833, 162.90757798372513, 71.50185776446654], "isController": false}, {"data": ["10 Finance overview", 500, 0, 0.0, 132.83799999999994, 1, 1386, 7.0, 458.7000000000001, 619.0, 1033.970000000001, 241.08003857280616, 83.10669298457088, 59.543944370781105], "isController": false}, {"data": ["01 Login", 500, 0, 0.0, 363.8360000000002, 85, 705, 246.0, 666.9000000000001, 682.0, 692.98, 693.4812760055479, 230.25745492371706, 179.4653692787795], "isController": false}, {"data": ["04 Attendance projects", 500, 0, 0.0, 211.8299999999998, 2, 1758, 146.0, 503.7000000000001, 745.2999999999998, 1112.8700000000001, 211.864406779661, 99.72523834745763, 67.63853945974577], "isController": false}, {"data": ["05 Attendance records", 500, 0, 0.0, 170.638, 2, 1274, 102.5, 470.00000000000034, 639.75, 1009.4300000000005, 215.3316106804479, 162.1295623385013, 69.58659695305771], "isController": false}, {"data": ["09 Merchant query", 500, 0, 0.0, 103.50800000000002, 1, 1052, 5.0, 327.0, 446.4499999999999, 855.99, 233.6448598130841, 423.70947721962614, 51.77515332943925], "isController": false}, {"data": ["12 Excel import page", 500, 0, 0.0, 265.81600000000003, 3, 943, 261.5, 505.80000000000007, 664.9, 826.99, 217.95989537925024, 1535.29758336966, 45.95803590889276], "isController": false}, {"data": ["02 Personnel query", 500, 0, 0.0, 266.7639999999999, 6, 1841, 121.0, 631.8000000000001, 806.6499999999999, 1326.8200000000002, 241.89646831156264, 1790.1283563134978, 75.57280327769713], "isController": false}, {"data": ["13 Card page", 500, 0, 0.0, 268.0560000000003, 6, 1607, 183.0, 614.9000000000001, 780.0499999999995, 1278.8400000000001, 213.4927412467976, 1221.1201030102477, 46.0585570559351], "isController": false}, {"data": ["03 Gate records", 500, 0, 0.0, 246.84599999999998, 2, 1727, 152.5, 645.100000000001, 885.1999999999998, 1241.8400000000001, 216.82567215958372, 240.7527238725065, 69.22244281222896], "isController": false}, {"data": ["07 Borrowing records", 500, 0, 0.0, 140.9440000000001, 2, 1222, 8.5, 452.0, 575.4499999999998, 910.8500000000001, 226.86025408348456, 333.42254140199634, 75.08454089156079], "isController": false}]}, function(index, item){
        switch(index){
            // Errors pct
            case 3:
                item = item.toFixed(2) + '%';
                break;
            // Mean
            case 4:
            // Mean
            case 7:
            // Median
            case 8:
            // Percentile 1
            case 9:
            // Percentile 2
            case 10:
            // Percentile 3
            case 11:
            // Throughput
            case 12:
            // Kbytes/s
            case 13:
            // Sent Kbytes/s
                item = item.toFixed(2);
                break;
        }
        return item;
    }, [[0, 0]], 0, summaryTableHeader);

    // Create error table
    createTable($("#errorsTable"), {"supportsControllersDiscrimination": false, "titles": ["Type of error", "Number of errors", "% in errors", "% in all samples"], "items": []}, function(index, item){
        switch(index){
            case 2:
            case 3:
                item = item.toFixed(2) + '%';
                break;
        }
        return item;
    }, [[1, 1]]);

        // Create top5 errors by sampler
    createTable($("#top5ErrorsBySamplerTable"), {"supportsControllersDiscrimination": false, "overall": {"data": ["Total", 6500, 0, "", "", "", "", "", "", "", "", "", ""], "isController": false}, "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"], "items": [{"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}]}, function(index, item){
        return item;
    }, [[0, 0]], 0);

});
