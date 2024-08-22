'use strict';

$(document).ready(function () {
    let allData = [];
    let chartBar = null;
    let initialYear;

    $.ajax({
        url: 'student-analysis',
        type: 'get',
        data: {},
        success: function (response) {
            if (response !== null) {
                allData = response;
                console.log("response, " ,response)
                let uniqueYears = [...new Set(response.map(item => item.year))];
                initialYear = uniqueYears[0];
                let dropdown = $('#yearDropdown');
                uniqueYears.forEach(year => {
                    dropdown.append(new Option(year, year));
                });
                updateChart(initialYear);
            }
        }
    });

    $('#yearDropdown').change(function () {
        let selectedYear = $(this).val();        
        updateChart(selectedYear);
    });

    function updateChart(selectedYear) {
        if ($('#bar').length > 0) {
            let filteredData = allData.filter(item => item.year == selectedYear);
            let classNames = [...new Set(filteredData.map(item => item.className))];
            let passedCounts = [];
            let notPassedCounts = [];

            classNames.forEach(className => {
                let passed = filteredData.filter(item => item.className == className && item.status == 'Pass').length;
                let notPassed = filteredData.filter(item => item.className == className && item.status == 'Not Pass').length;
                passedCounts.push(passed);
                notPassedCounts.push(notPassed);
            });

            var optionsBar = {
                chart: {
                    type: 'bar',
                    height: 350,
                    width: '100%',
                    stacked: true,
                    toolbar: {
                        show: true
                    },
                },
                plotOptions: {
                    bar: {
                        columnWidth: '45%',
                        distributed: true,
                    }
                },
                series: [{
                    name: "Passed",
                    data: passedCounts,
                    color: '#63b598' 
                }, {
                    name: "Not Passed",
                    data: notPassedCounts,
                    color: '#ee6352' 
                }],
                xaxis: {
                    categories: classNames,
                    labels: {
                        show: true,
                        rotate: -45,
                        style: {
                            fontSize: '12px',
                            fontWeight: 400,
                        }
                    },
                },
                yaxis: {
                    title: {
                        text: 'Number of Students',
                        style: {
                            fontSize: '14px'
                        }
                    },
                },
                legend: {
                    position: 'top',
                    horizontalAlign: 'center',
                    offsetY: 10,
                },                
                tooltip: {
                    y: {
                        formatter: function (val) {
                            return val;
                        }
                    }
                }
            };

            if (chartBar === null) {
                chartBar = new ApexCharts(document.querySelector('#bar'), optionsBar);
                chartBar.render();
            } else {
                chartBar.updateOptions(optionsBar);
                chartBar.updateSeries([{
                    data: passedCounts
                }, {
                    data: notPassedCounts
                }]);
            }
        }
    }
});
