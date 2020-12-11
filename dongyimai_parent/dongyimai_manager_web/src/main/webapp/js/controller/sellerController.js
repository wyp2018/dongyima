//seller控制层 
app.controller('sellerController' ,function($scope, $controller, sellerService){	
	
	// 继承
	$controller("baseController", {
		$scope : $scope
	});
	
	// 保存
	$scope.save = function() {
		sellerService.save($scope.entity).success(function(response) {
			if (response.success) {
				// 重新加载
				$scope.reloadList();
			} else {
				alert(response.message);
			}
		});
	}
	
	//查询实体 
	$scope.findOne = function(id){				
		sellerService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//批量删除 
	$scope.dele = function(){			
		//获取选中的复选框			
		sellerService.dele($scope.selectIds).success(
			function(response){
				if(response.success){
					$scope.reloadList();
					$scope.selectIds=[];
				}						
			}		
		);				
	}


	$scope.add=function(){
		sellerService.add( $scope.entity  ).success(
			function(response){
				if(response.success){
					location.href='shoplogin.html';
				}else{
					alert(response.message);
				}
			}
		);
	}

		// 定义搜索对象
	$scope.searchEntity = {};
	// 搜索
	$scope.search = function(page,size){			
		sellerService.search(page,size,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;
			}			
		);
	}

	//修改审核状态
	$scope.updateStatus = function (sellerID,status) {
		sellerService.updateStatus(sellerID,status).success(function (response) {

			if(response){
				alert(status);
				$scope.reloadList();
			}else {
				alert(response.message);
			}
		})

	}


	$scope.reloadList = function(){
		$scope.search( $scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
	}


	$scope.statusName = ["w未审核","审核通过","审核未通过","关闭"];

});
