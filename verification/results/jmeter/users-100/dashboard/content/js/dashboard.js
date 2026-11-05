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
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 1300, 100, 7.6923076923076925, 4.680769230769225, 0, 44, 4.0, 7.0, 12.950000000000045, 25.99000000000001, 131.96629783778297, 278.02935615673533, 35.67808788447873], "isController": false}, "titles": ["Label", "#Samples", "FAIL", "Error %", "Average", "Min", "Max", "Median", "90th pct", "95th pct", "99th pct", "Transactions/s", "Received", "Sent"], "items": [{"data": ["11 Roles", 100, 0, 0.0, 2.6199999999999997, 1, 16, 2.0, 3.9000000000000057, 5.949999999999989, 15.969999999999985, 10.932546190007653, 6.39511246856893, 2.3594655351481357], "isController": false}, {"data": ["08 Wallet and transactions", 100, 0, 0.0, 2.8600000000000003, 1, 20, 2.0, 3.9000000000000057, 7.949999999999989, 19.97999999999999, 10.903936321011885, 26.503806155272056, 2.4278295714753027], "isController": false}, {"data": ["06 Books", 100, 0, 0.0, 5.840000000000001, 3, 26, 5.0, 8.0, 13.899999999999977, 26.0, 10.880208900010881, 7.841400554890654, 3.4425660972690673], "isController": false}, {"data": ["10 Finance overview", 100, 100, 100.0, 1.4900000000000009, 0, 6, 1.0, 2.9000000000000057, 4.0, 5.989999999999995, 10.919414719371042, 2.2819870604935577, 2.6978632070321034], "isController": false}, {"data": ["01 Login", 100, 0, 0.0, 8.399999999999993, 6, 44, 7.0, 9.0, 13.849999999999966, 43.89999999999995, 10.19056353816366, 3.3835855497809026, 2.6372063843880564], "isController": false}, {"data": ["04 Attendance projects", 100, 0, 0.0, 4.710000000000001, 2, 30, 3.0, 6.900000000000006, 16.0, 29.929999999999964, 10.828370330265294, 5.096947753113157, 3.457887791012453], "isController": false}, {"data": ["05 Attendance records", 100, 0, 0.0, 4.939999999999999, 2, 31, 3.0, 6.900000000000006, 23.94999999999999, 30.97999999999999, 10.851871947911015, 4.980839663591969, 3.5077828269126425], "isController": false}, {"data": ["09 Merchant query", 100, 0, 0.0, 2.379999999999999, 1, 22, 2.0, 3.0, 4.0, 21.969999999999985, 10.912265386294195, 14.354317847010039, 2.4190275807507637], "isController": false}, {"data": ["12 Excel import page", 100, 0, 0.0, 5.800000000000002, 3, 21, 5.0, 6.900000000000006, 17.0, 20.989999999999995, 10.944511327569224, 77.09253926343439, 2.308607858159133], "isController": false}, {"data": ["02 Personnel query", 100, 0, 0.0, 6.0600000000000005, 3, 33, 5.0, 8.800000000000011, 18.0, 32.889999999999944, 10.793308148947652, 68.12221735024285, 3.3729087965461413], "isController": false}, {"data": ["13 Card page", 100, 0, 0.0, 7.32, 4, 30, 6.0, 8.0, 24.64999999999992, 30.0, 10.95890410958904, 60.199058219178085, 2.365154109589041], "isController": false}, {"data": ["03 Gate records", 100, 0, 0.0, 4.570000000000001, 2, 22, 3.0, 6.0, 16.849999999999966, 21.989999999999995, 10.811979673478213, 9.449923640393555, 3.452653665261109], "isController": false}, {"data": ["07 Borrowing records", 100, 0, 0.0, 3.859999999999999, 2, 23, 3.0, 6.0, 7.949999999999989, 22.849999999999923, 10.894432944765224, 12.4583798616407, 3.6066530940189563], "isController": false}]}, function(index, item){
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
    createTable($("#errorsTable"), {"supportsControllersDiscrimination": false, "titles": ["Type of error", "Number of errors", "% in errors", "% in all samples"], "items": [{"data": ["400", 100, 100.0, 7.6923076923076925], "isController": false}]}, function(index, item){
        switch(index){
            case 2:
            case 3:
                item = item.toFixed(2) + '%';
                break;
        }
        return item;
    }, [[1, 1]]);

        // Create top5 errors by sampler
    createTable($("#top5ErrorsBySamplerTable"), {"supportsControllersDiscrimination": false, "overall": {"data": ["Total", 1300, 100, "400", 100, "", "", "", "", "", "", "", ""], "isController": false}, "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"], "items": [{"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": ["10 Finance overview", 100, 100, "400", 100, "", "", "", "", "", "", "", ""], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}]}, function(index, item){
        return item;
    }, [[0, 0]], 0);

});
