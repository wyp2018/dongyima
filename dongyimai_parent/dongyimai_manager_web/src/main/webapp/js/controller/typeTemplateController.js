//type_template控制层 
app.controller('typeTemplateController' ,function($scope, $controller, typeTemplateService){	
	
	// 继承
	$controller("baseController", {
		$scope : $scope
	});
	
	// 保存
	$scope.save = function() {
		typeTemplateService.save($scope.entity).success(function(response) {
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
		typeTemplateService.findOne(id).success(
			function(response){
				$scope.entity= response;
				$scope.entity.brandIds = JSON.parse($scope.entity.brandIds) ;
				$scope.entity.specIds=  JSON.parse($scope.entity.specIds);//转换规格列表
				$scope.entity.customAttributeItems= JSON.parse($scope.entity.customAttributeItems);//转换扩展属性

			}
		);				
	}
	
	//批量删除 
	$scope.dele = function(){			
		//获取选中的复选框			
		typeTemplateService.dele($scope.selectIds).success(
			function(response){
				if(response.success){
					$scope.reloadList();
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	// 定义搜索对象 
	$scope.searchEntity = {};
	// 搜索
	$scope.search = function(page,size){			
		typeTemplateService.search(page,size,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;
			}			
		);
	}

	//新增扩展属性
	$scope.addRow = function () {
		$scope.entity.customAttributeItems.push({});
	}

	$scope.deleRow = function (idx) {
		$scope.entity.customAttributeItems.splice(idx,1);
	}
	//测试品牌下拉
	$scope.brandList = {"data":[{"id":1,"text":"苹果"},{"id":2,"text":"香蕉"},{"id":3,"text":"西瓜"}]};
	$scope.specList  = {"data":[{"id":1,"text":"大桥"},{"id":2,"text":"小乔"},{"id":3,"text":"沙雕"}]};

});	
