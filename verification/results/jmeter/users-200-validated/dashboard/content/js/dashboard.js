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
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 2600, 0, 0.0, 3.2457692307692314, 0, 93, 3.0, 6.0, 7.0, 16.0, 262.75896917635174, 617.4076830154119, 71.03887695805963], "isController": false}, "titles": ["Label", "#Samples", "FAIL", "Error %", "Average", "Min", "Max", "Median", "90th pct", "95th pct", "99th pct", "Transactions/s", "Received", "Sent"], "items": [{"data": ["11 Roles", 200, 0, 0.0, 1.0349999999999997, 0, 13, 1.0, 1.0, 2.0, 6.0, 21.593608291945582, 16.174013273321098, 4.660339289570287], "isController": false}, {"data": ["08 Wallet and transactions", 200, 0, 0.0, 1.8599999999999997, 1, 18, 1.0, 2.0, 5.949999999999989, 13.0, 21.542438604049977, 67.36219571305472, 4.796558595433003], "isController": false}, {"data": ["06 Books", 200, 0, 0.0, 4.2349999999999985, 3, 20, 4.0, 5.0, 7.0, 18.950000000000045, 21.503064186646597, 15.497325556391786, 6.8037039028061495], "isController": false}, {"data": ["10 Finance overview", 200, 0, 0.0, 1.790000000000001, 1, 11, 1.0, 2.0, 4.0, 9.990000000000009, 21.570319240724764, 7.435862003882657, 5.32938551553063], "isController": false}, {"data": ["01 Login", 200, 0, 0.0, 6.740000000000003, 4, 40, 6.0, 7.0, 9.0, 39.86000000000013, 20.27163997567403, 6.7308179606730185, 5.246078704642206], "isController": false}, {"data": ["04 Attendance projects", 200, 0, 0.0, 2.895000000000002, 2, 19, 2.0, 3.9000000000000057, 8.949999999999989, 15.990000000000009, 21.461530207103767, 10.10200933576564, 6.85343786887005], "isController": false}, {"data": ["05 Attendance records", 200, 0, 0.0, 3.5950000000000015, 1, 93, 2.0, 4.0, 8.899999999999977, 48.950000000000045, 21.484584810398538, 16.17638172736062, 6.944724191642497], "isController": false}, {"data": ["09 Merchant query", 200, 0, 0.0, 1.4549999999999996, 0, 12, 1.0, 2.0, 4.949999999999989, 9.0, 21.55636990730761, 28.355888930804053, 4.7786093446863545], "isController": false}, {"data": ["12 Excel import page", 200, 0, 0.0, 3.9849999999999994, 3, 28, 3.0, 5.0, 8.949999999999989, 18.950000000000045, 21.591277124041888, 152.08777528878332, 4.554410018352586], "isController": false}, {"data": ["02 Personnel query", 200, 0, 0.0, 4.134999999999995, 2, 40, 3.0, 4.0, 10.899999999999977, 19.980000000000018, 21.429336762027216, 158.3627986713811, 6.696667738133504], "isController": false}, {"data": ["13 Card page", 200, 0, 0.0, 4.675000000000002, 3, 16, 4.0, 6.0, 9.0, 15.990000000000009, 21.59827213822894, 123.53621085313175, 4.661345842332613], "isController": false}, {"data": ["03 Gate records", 200, 0, 0.0, 2.9349999999999983, 2, 19, 2.0, 3.0, 6.949999999999989, 14.0, 21.443122118580465, 23.80940414924413, 6.847559504663879], "isController": false}, {"data": ["07 Borrowing records", 200, 0, 0.0, 2.8600000000000003, 1, 17, 2.0, 4.0, 8.0, 12.990000000000009, 21.51694459386767, 31.62402501344809, 7.123285368477676], "isController": false}]}, function(index, item){
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
    createTable($("#top5ErrorsBySamplerTable"), {"supportsControllersDiscrimination": false, "overall": {"data": ["Total", 2600, 0, "", "", "", "", "", "", "", "", "", ""], "isController": false}, "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"], "items": [{"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}]}, function(index, item){
        return item;
    }, [[0, 0]], 0);

});
