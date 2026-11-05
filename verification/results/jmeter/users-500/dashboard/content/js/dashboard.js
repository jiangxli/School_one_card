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

    var data = {"OkPercent": 92.3076923076923, "KoPercent": 7.6923076923076925};
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
    createTable($("#apdexTable"), {"supportsControllersDiscrimination": true, "overall": {"data": [0.9230769230769231, 500, 1500, "Total"], "isController": false}, "titles": ["Apdex", "T (Toleration threshold)", "F (Frustration threshold)", "Label"], "items": [{"data": [1.0, 500, 1500, "11 Roles"], "isController": false}, {"data": [1.0, 500, 1500, "08 Wallet and transactions"], "isController": false}, {"data": [1.0, 500, 1500, "06 Books"], "isController": false}, {"data": [0.0, 500, 1500, "10 Finance overview"], "isController": false}, {"data": [1.0, 500, 1500, "01 Login"], "isController": false}, {"data": [1.0, 500, 1500, "04 Attendance projects"], "isController": false}, {"data": [1.0, 500, 1500, "05 Attendance records"], "isController": false}, {"data": [1.0, 500, 1500, "09 Merchant query"], "isController": false}, {"data": [1.0, 500, 1500, "12 Excel import page"], "isController": false}, {"data": [1.0, 500, 1500, "02 Personnel query"], "isController": false}, {"data": [1.0, 500, 1500, "13 Card page"], "isController": false}, {"data": [1.0, 500, 1500, "03 Gate records"], "isController": false}, {"data": [1.0, 500, 1500, "07 Borrowing records"], "isController": false}]}, function(index, item){
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
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 6500, 500, 7.6923076923076925, 8.686307692307683, 0, 455, 3.0, 9.0, 21.0, 175.97999999999956, 656.6983228935138, 1383.545760507173, 177.49363823499698], "isController": false}, "titles": ["Label", "#Samples", "FAIL", "Error %", "Average", "Min", "Max", "Median", "90th pct", "95th pct", "99th pct", "Transactions/s", "Received", "Sent"], "items": [{"data": ["11 Roles", 500, 0, 0.0, 3.455999999999998, 1, 154, 2.0, 5.0, 9.0, 58.950000000000045, 54.54347114650376, 31.90580001636304, 11.767114718555689], "isController": false}, {"data": ["08 Wallet and transactions", 500, 0, 0.0, 3.459999999999995, 1, 172, 2.0, 6.0, 11.0, 41.97000000000003, 54.17705060136526, 131.68620990898256, 12.058414711778092], "isController": false}, {"data": ["06 Books", 500, 0, 0.0, 9.273999999999996, 3, 209, 4.0, 13.0, 26.94999999999999, 132.7700000000002, 53.92579810181191, 38.86449121009491, 17.058035955025883], "isController": false}, {"data": ["10 Finance overview", 500, 500, 100.0, 1.5520000000000007, 0, 21, 1.0, 3.0, 6.0, 15.980000000000018, 54.460298442435466, 11.38135143230585, 13.451055508659188], "isController": false}, {"data": ["01 Login", 500, 0, 0.0, 9.539999999999994, 4, 275, 5.0, 11.0, 28.899999999999977, 95.98000000000002, 50.643168236604886, 16.815114453560216, 13.105898029980757], "isController": false}, {"data": ["04 Attendance projects", 500, 0, 0.0, 6.391999999999996, 1, 249, 2.0, 9.0, 18.94999999999999, 84.96000000000004, 53.72877713303245, 25.290303298946917, 17.15312197775629], "isController": false}, {"data": ["05 Attendance records", 500, 0, 0.0, 5.863999999999999, 1, 304, 3.0, 9.0, 15.0, 65.98000000000002, 53.86189809328881, 24.721769632661857, 17.40601940374879], "isController": false}, {"data": ["09 Merchant query", 500, 0, 0.0, 2.7199999999999998, 0, 71, 1.0, 4.0, 9.949999999999989, 49.87000000000012, 54.23581733376722, 71.3434042466645, 12.01853000596594], "isController": false}, {"data": ["12 Excel import page", 500, 0, 0.0, 29.240000000000006, 2, 341, 3.0, 98.80000000000007, 266.79999999999995, 305.99, 54.57323728443571, 384.4108989576511, 11.507065528814667], "isController": false}, {"data": ["02 Personnel query", 500, 0, 0.0, 6.723999999999996, 2, 220, 3.0, 10.900000000000034, 19.0, 78.84000000000015, 53.6135535063264, 338.38319952283933, 16.749837483915933], "isController": false}, {"data": ["13 Card page", 500, 0, 0.0, 22.309999999999988, 3, 455, 4.0, 61.0, 113.74999999999994, 298.97, 54.71656817684395, 300.56708593237033, 11.804458374370759], "isController": false}, {"data": ["03 Gate records", 500, 0, 0.0, 6.711999999999995, 1, 251, 2.0, 10.0, 20.94999999999999, 113.87000000000012, 53.642313056539, 46.88463885312735, 17.125517983585453], "isController": false}, {"data": ["07 Borrowing records", 500, 0, 0.0, 5.678, 1, 182, 3.0, 7.0, 12.0, 78.98000000000002, 54.01317921572864, 61.76702427892406, 17.876885397537], "isController": false}]}, function(index, item){
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
    createTable($("#errorsTable"), {"supportsControllersDiscrimination": false, "titles": ["Type of error", "Number of errors", "% in errors", "% in all samples"], "items": [{"data": ["400", 500, 100.0, 7.6923076923076925], "isController": false}]}, function(index, item){
        switch(index){
            case 2:
            case 3:
                item = item.toFixed(2) + '%';
                break;
        }
        return item;
    }, [[1, 1]]);

        // Create top5 errors by sampler
    createTable($("#top5ErrorsBySamplerTable"), {"supportsControllersDiscrimination": false, "overall": {"data": ["Total", 6500, 500, "400", 500, "", "", "", "", "", "", "", ""], "isController": false}, "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"], "items": [{"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": ["10 Finance overview", 500, 500, "400", 500, "", "", "", "", "", "", "", ""], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}]}, function(index, item){
        return item;
    }, [[0, 0]], 0);

});
