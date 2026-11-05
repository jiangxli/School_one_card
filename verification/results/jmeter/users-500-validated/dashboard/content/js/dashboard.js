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
    createTable($("#apdexTable"), {"supportsControllersDiscrimination": true, "overall": {"data": [1.0, 500, 1500, "Total"], "isController": false}, "titles": ["Apdex", "T (Toleration threshold)", "F (Frustration threshold)", "Label"], "items": [{"data": [1.0, 500, 1500, "11 Roles"], "isController": false}, {"data": [1.0, 500, 1500, "08 Wallet and transactions"], "isController": false}, {"data": [1.0, 500, 1500, "06 Books"], "isController": false}, {"data": [1.0, 500, 1500, "10 Finance overview"], "isController": false}, {"data": [1.0, 500, 1500, "01 Login"], "isController": false}, {"data": [1.0, 500, 1500, "04 Attendance projects"], "isController": false}, {"data": [1.0, 500, 1500, "05 Attendance records"], "isController": false}, {"data": [1.0, 500, 1500, "09 Merchant query"], "isController": false}, {"data": [1.0, 500, 1500, "12 Excel import page"], "isController": false}, {"data": [1.0, 500, 1500, "02 Personnel query"], "isController": false}, {"data": [1.0, 500, 1500, "13 Card page"], "isController": false}, {"data": [1.0, 500, 1500, "03 Gate records"], "isController": false}, {"data": [1.0, 500, 1500, "07 Borrowing records"], "isController": false}]}, function(index, item){
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
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 6500, 0, 0.0, 3.6201538461538485, 0, 74, 2.0, 5.0, 9.0, 36.0, 656.6319830285887, 1549.0437796747146, 177.4259868421053], "isController": false}, "titles": ["Label", "#Samples", "FAIL", "Error %", "Average", "Min", "Max", "Median", "90th pct", "95th pct", "99th pct", "Transactions/s", "Received", "Sent"], "items": [{"data": ["11 Roles", 500, 0, 0.0, 1.3860000000000003, 0, 33, 1.0, 1.0, 4.949999999999989, 13.990000000000009, 54.042369217466494, 46.495436797449194, 11.654574686554257], "isController": false}, {"data": ["08 Wallet and transactions", 500, 0, 0.0, 2.2819999999999996, 0, 50, 1.0, 2.0, 5.949999999999989, 30.980000000000018, 53.85029617662897, 168.3873519116855, 11.981270193861066], "isController": false}, {"data": ["06 Books", 500, 0, 0.0, 4.724, 3, 57, 3.0, 6.0, 11.0, 37.99000000000001, 53.69992482010525, 38.70170363011492, 16.98218169369563], "isController": false}, {"data": ["10 Finance overview", 500, 0, 0.0, 2.1759999999999953, 1, 48, 1.0, 2.0, 9.949999999999989, 19.99000000000001, 53.93743257820928, 18.593665722761596, 13.317489212513484], "isController": false}, {"data": ["01 Login", 500, 0, 0.0, 6.170000000000002, 4, 63, 5.0, 7.0, 8.0, 44.0, 50.643168236604886, 16.815114453560216, 13.105898029980757], "isController": false}, {"data": ["04 Attendance projects", 500, 0, 0.0, 3.342, 1, 54, 2.0, 3.900000000000034, 11.949999999999989, 33.97000000000003, 53.63080553469913, 25.244187761450174, 17.1174447602703], "isController": false}, {"data": ["05 Attendance records", 500, 0, 0.0, 3.1380000000000017, 1, 67, 2.0, 4.0, 7.0, 36.90000000000009, 53.665342921541274, 40.4062298754964, 17.338098368573576], "isController": false}, {"data": ["09 Merchant query", 500, 0, 0.0, 1.7160000000000009, 0, 52, 1.0, 2.0, 5.0, 15.980000000000018, 53.88511693070374, 70.88208252505657, 11.93639535510292], "isController": false}, {"data": ["12 Excel import page", 500, 0, 0.0, 5.749999999999997, 2, 74, 3.0, 6.0, 30.94999999999999, 45.99000000000001, 54.21229534858506, 381.86844370053126, 11.426511845386532], "isController": false}, {"data": ["02 Personnel query", 500, 0, 0.0, 4.001999999999998, 2, 42, 3.0, 5.0, 9.949999999999989, 29.0, 53.54465624330692, 396.25137208181627, 16.723920405868494], "isController": false}, {"data": ["13 Card page", 500, 0, 0.0, 5.701999999999997, 3, 53, 4.0, 6.0, 21.94999999999999, 44.99000000000001, 54.265248534838285, 310.3823834653788, 11.70264000434122], "isController": false}, {"data": ["03 Gate records", 500, 0, 0.0, 3.446000000000002, 1, 73, 2.0, 3.0, 12.949999999999989, 31.980000000000018, 53.60205831903945, 59.517129207761585, 17.10826945754717], "isController": false}, {"data": ["07 Borrowing records", 500, 0, 0.0, 3.2280000000000024, 1, 64, 2.0, 4.0, 7.0, 32.98000000000002, 53.78078950198989, 79.04305488329568, 17.79555905130687], "isController": false}]}, function(index, item){
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
