<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>健身数据趋势图</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            padding: 0;
        }

        header {
            background-color: #4CAF50;
            color: white;
            padding: 15px;
            text-align: center;
        }

        h2 {
            margin-top: 20px;
            text-align: center;
            color: #333;
        }

        .chart-container {
            width: 80%;
            margin: 20px auto;
            padding: 20px;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        }

        .chart-container h3 {
            text-align: center;
            color: #333;
            font-size: 18px;
            margin-bottom: 20px;
        }

        .back-link {
            text-align: center;
            margin-top: 20px;
        }

        .back-link a {
            display: inline-block;
            background-color: #f0f0f0;
            color: #333;
            padding: 12px 20px;
            border-radius: 5px;
            text-align: center;
            font-size: 16px;
            border: 1px solid #ccc;
            text-decoration: none;
        }

        .back-link a:hover {
            background-color: #e0e0e0;
        }

        canvas {
            margin: 10px 0;
            border-radius: 8px;
        }
    </style>
</head>

<body>

<header>
    <h1>健身数据趋势图</h1>
</header>

<h2>查看您的健身数据趋势</h2>

<div class="chart-container">
    <h3>步数趋势</h3>
    <canvas id="stepsChart" width="400" height="200"></canvas>

    <h3>消耗热量趋势</h3>
    <canvas id="caloriesChart" width="400" height="200"></canvas>

    <h3>公里数趋势</h3>
    <canvas id="distanceChart" width="400" height="200"></canvas>
</div>

<div class="back-link">
    <!-- 添加了按钮样式 -->
    <a href="ActivityDataServlet">返回健身数据</a>
</div>

<script>
    // 从后端获取的 JSON 数据
    const trendData = <%= request.getAttribute("trendDataJson") %>;
    console.log(trendData); // 打印调试数据

    // 准备图表数据
    const labels = trendData.map(data => data.date); // 日期
    const steps = trendData.map(data => data.steps); // 步数
    const calories = trendData.map(data => data.calories); // 消耗热量
    const distance = trendData.map(data => data.distance); // 公里数

    // 渲染步数趋势图
    const stepsCtx = document.getElementById('stepsChart').getContext('2d');
    const stepsChart = new Chart(stepsCtx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: '步数',
                data: steps,
                borderColor: 'blue',
                fill: false,
                borderWidth: 2
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top'
                }
            },
            scales: {
                x: {
                    title: {
                        display: true,
                        text: '日期'
                    }
                },
                y: {
                    title: {
                        display: true,
                        text: '步数'
                    },
                    ticks: {
                        beginAtZero: true
                    }
                }
            }
        }
    });

    // 渲染消耗热量趋势图
    const caloriesCtx = document.getElementById('caloriesChart').getContext('2d');
    const caloriesChart = new Chart(caloriesCtx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: '消耗热量 (卡)',
                data: calories,
                borderColor: 'red',
                fill: false,
                borderWidth: 2
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top'
                }
            },
            scales: {
                x: {
                    title: {
                        display: true,
                        text: '日期'
                    }
                },
                y: {
                    title: {
                        display: true,
                        text: '热量 (卡)'
                    },
                    ticks: {
                        beginAtZero: true
                    }
                }
            }
        }
    });

    // 渲染公里数趋势图
    const distanceCtx = document.getElementById('distanceChart').getContext('2d');
    const distanceChart = new Chart(distanceCtx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: '距离 (公里)',
                data: distance,
                borderColor: 'green',
                fill: false,
                borderWidth: 2
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top'
                }
            },
            scales: {
                x: {
                    title: {
                        display: true,
                        text: '日期'
                    }
                },
                y: {
                    title: {
                        display: true,
                        text: '距离 (公里)'
                    },
                    ticks: {
                        beginAtZero: true
                    }
                }
            }
        }
    });
</script>

</body>
</html>
