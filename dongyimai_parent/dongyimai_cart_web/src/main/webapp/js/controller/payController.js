//content控制层 
app.controller('payController', function ($scope, $location,payService) {

    // 进入页面，请求后台，获取二维码url
    $scope.createCode = function () {
        payService.createCode().success(function (response) {
            if(response) {
                // 订单号
                $scope.out_trade_no = response.out_trade_no;
                console.log(  $scope.out_trade_no);
                // 金额
                $scope.total_amount = response.total_amount;

                // 二维码url
                var qr = new QRious({
                    element: document.getElementById('mycode'),
                    size: 250,
                    level: 'H',
                    value: response.qrcode
                });

                // 查询支付状态
                queryPayStatus($scope.out_trade_no);
            }
        });
    }

    queryPayStatus = function (out_trade_no) {

        payService.queryPayStatus(out_trade_no).success(function (response) {
            if(response.success) {
                location.href = "paysuccess.html#?money=" + $scope.total_amount;
            } else {
                if(response.message = '二维码失效') {
                    $(".red").html("二维码已过期，刷新页面重新获取");
                } else {
                    alert(response.message);
                }
            }
        });

    }


    // 进入付款成功页面，获取支付金额
    $scope.getMoney = function() {
        $scope.totalPay = $location.search()['money'];
    }


});	
