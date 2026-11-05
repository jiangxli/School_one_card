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
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 1300, 0, 0.0, 3.0669230769230773, 0, 43, 3.0, 6.0, 7.0, 11.0, 132.03331302051595, 309.83989373857406, 35.67121165955718], "isController": false}, "titles": ["Label", "#Samples", "FAIL", "Error %", "Average", "Min", "Max", "Median", "90th pct", "95th pct", "99th pct", "Transactions/s", "Received", "Sent"], "items": [{"data": ["11 Roles", 100, 0, 0.0, 0.9199999999999998, 0, 3, 1.0, 1.9000000000000057, 2.9499999999999886, 3.0, 10.81665765278529, 7.711093834505139, 2.3322361749594376], "isController": false}, {"data": ["08 Wallet and transactions", 100, 0, 0.0, 1.4699999999999998, 1, 5, 1.0, 2.0, 3.0, 4.989999999999995, 10.8038029386344, 33.782985360847015, 2.4033186244057907], "isController": false}, {"data": ["06 Books", 100, 0, 0.0, 3.940000000000001, 3, 8, 4.0, 5.0, 7.0, 7.989999999999995, 10.787486515641856, 7.774575242718447, 3.4110158778317152], "isController": false}, {"data": ["10 Finance overview", 100, 0, 0.0, 1.9600000000000004, 1, 16, 1.0, 3.0, 5.0, 15.97999999999999, 10.81081081081081, 3.7267736486486487, 2.6688133445945947], "isController": false}, {"data": ["01 Login", 100, 0, 0.0, 7.190000000000001, 5, 43, 6.0, 7.0, 8.0, 42.89999999999995, 10.188487009679063, 3.3828960774325014, 2.636669001528273], "isController": false}, {"data": ["04 Attendance projects", 100, 0, 0.0, 2.770000000000001, 2, 15, 2.0, 3.0, 5.949999999999989, 14.969999999999985, 10.77354018530489, 5.071139032536092, 3.4381691378474466], "isController": false}, {"data": ["05 Attendance records", 100, 0, 0.0, 2.5200000000000005, 1, 7, 2.0, 3.0, 4.0, 7.0, 10.782833728703903, 8.11871562971749, 3.4832554385917622], "isController": false}, {"data": ["09 Merchant query", 100, 0, 0.0, 1.1399999999999997, 0, 4, 1.0, 2.0, 3.0, 4.0, 10.807305738679347, 14.21625081054793, 2.393543816870204], "isController": false}, {"data": ["12 Excel import page", 100, 0, 0.0, 3.8600000000000003, 2, 11, 3.0, 5.0, 9.949999999999989, 11.0, 10.817827780181739, 76.20018728364344, 2.2796670475443532], "isController": false}, {"data": ["02 Personnel query", 100, 0, 0.0, 4.200000000000001, 2, 23, 3.0, 4.900000000000006, 9.0, 22.95999999999998, 10.759629868732516, 79.4783596944265, 3.3601777692597374], "isController": false}, {"data": ["13 Card page", 100, 0, 0.0, 4.5, 3, 11, 4.0, 5.900000000000006, 8.949999999999989, 10.989999999999995, 10.828370330265294, 61.93531740660531, 2.334761606659448], "isController": false}, {"data": ["03 Gate records", 100, 0, 0.0, 2.7999999999999994, 2, 8, 3.0, 3.9000000000000057, 5.0, 8.0, 10.770059235325794, 11.958552100161551, 3.437058259289176], "isController": false}, {"data": ["07 Borrowing records", 100, 0, 0.0, 2.600000000000001, 2, 9, 2.0, 3.9000000000000057, 4.949999999999989, 8.989999999999995, 10.79447322970639, 15.864924033894647, 3.571347251457254], "isController": false}]}, function(index, item){
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
    createTable($("#top5ErrorsBySamplerTable"), {"supportsControllersDiscrimination": false, "overall": {"data": ["Total", 1300, 0, "", "", "", "", "", "", "", "", "", ""], "isController": false}, "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"], "items": [{"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}]}, function(index, item){
        return item;
    }, [[0, 0]], 0);

});
