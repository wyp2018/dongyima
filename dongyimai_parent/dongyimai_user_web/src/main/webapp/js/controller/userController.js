 //用户表控制层 
app.controller('userController' ,function($scope,userService){


	//保存
	$scope.save=function(){
		userService.save($scope.entity,$scope.entity.mycode).success(function (response) {

			if(response.success){
				location.href = "login.html";
			}else {
				alert(response.message);
			}
		})
	}

    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		userService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		userService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		userService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	

	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		userService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		userService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	//发送验证码
	$scope.sendCode = function(){
		userService.sendCode($scope.entity.phone).success(function (response) {

			if(response.success){

			}else {
				alert(response.message);
			}
		})
	}

	//验证登录
	$scope.checkLogin = function () {

		userService.findOne($scope.entity)
	}
    
});	