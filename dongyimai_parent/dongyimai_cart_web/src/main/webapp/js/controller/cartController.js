 //控制层 
app.controller('cartController' ,function($scope,cartService,addressService,orderService){


	//根据广告分类id获取广告列表
	$scope.findCartList = function () {

		cartService.findCartList().success(function (response) {

			if(response){
				$scope.cartList = response;
			}
			$scope.totalValue = cartService.sum($scope.cartList);
		})
	}

	$scope.changeNum = function (skuId,num) {

		cartService.changeNum(skuId,num).success(function (response) {

			if(response.success){
				$scope.findCartList();
			}else {
				alert(response.message);
			}
		})
	}

	//进入结算页,获取收货列表

	$scope.findAddrList = function () {

		addressService.findAddrList().success(function (response) {

			if (response) {
				$scope.addrList = response;
				for (var i = 0; i < $scope.addrList.length; i++) {
					if ($scope.addrList[i].isDefault == 1) {
						$scope.selectAddr = $scope.addrList[i];
					}
				}
			} else {
				alert("失败");
			}
		})
	}

	$scope.changeAddr = function (addr) {

		$scope.selectAddr = addr;

	}

	//选择支付方式

	$scope.order={paymentType:'1'};

	$scope.selectPayType = function (type) {
		$scope.order.paymentType = type;
	}


	//提交订单
	$scope.submitOrder = function () {

		$scope.order.receiverAreaName = $scope.selectAddr.address;
		$scope.order.receiverMobile= $scope.selectAddr.mobile;
		$scope.order.receiver = $scope.selectAddr.contact;

		orderService.submitOrder($scope.order).success(function (response) {

			if(response.success){
				if($scope.order.paymentType == 1){
					location.href = 'pay.html';
				}else {
					location.href = 'paysuccess.html';
				}

			}else {
				alert(response.message);
			}
		})

	}





});	