<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String basePath = request.getScheme() + "://" + request.getServerName() + ":" +request.getServerPort() + request.getContextPath() + "/"; %>
<!--_________________________https__________________________localhost______________________8080_______________________crm-->
<!DOCTYPE html>
<html>
<head>
    <base href=<%=basePath%>>
    <meta charset="UTF-8">
    <!--jquery-->
    <script type="text/javascript" src="jquery-3.6.0.js"></script>
    <script type="text/javascript" src="jquery-1.11.1-min.js"></script>
    <!--统计图插件-->
    <script src="jquery/echarts.js"></script>

<script type="text/javascript">

    $(function () {
        showChart();

    })

    function showChart() {
        $.ajax({
            url:"work/transaction/tranEcharts.sv",
            type:"get",
            dataType:"json",
            success:function (data){
                var myChart = echarts.init(document.getElementById('main'));
                option = {
                    title: {
                        text: '交易统计图'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: '{a} <br/>{b} : {c}%'
                    },
                    toolbox: {
                        feature: {
                            dataView: { readOnly: false },
                            restore: {},
                            saveAsImage: {}
                        }
                    },
                    legend: {
                        data: ['Show', 'Click', 'Visit', 'Inquiry', 'Order']
                    },
                    series: [
                        {
                            name: 'Funnel',
                            type: 'funnel',
                            left: '10%',
                            top: 60,
                            bottom: 60,
                            width: '80%',
                            min: 0,
                            max: data.total,
                            minSize: '0%',
                            maxSize: '100%',
                            sort: 'descending',
                            gap: 2,
                            label: {
                                show: true,
                                position: 'inside'
                            },
                            labelLine: {
                                length: 10,
                                lineStyle: {
                                    width: 1,
                                    type: 'solid'
                                }
                            },
                            itemStyle: {
                                borderColor: '#fff',
                                borderWidth: 1
                            },
                            emphasis: {
                                label: {
                                    fontSize: 20
                                }
                            },
                            data: data.list

                            /*[
                                { value: 60, name: 'Visit' },
                                { value: 40, name: 'Inquiry' },
                                { value: 20, name: 'Order' },
                                { value: 80, name: 'Click' },
                                { value: 100, name: 'Show' }
                            ]*/
                        }
                    ]
                };
                myChart.setOption(option);
            }
        })
    }

</script>

</head>
<body>

<div id="main" style="width: 600px;height:400px;"></div>

</body>
</html>
