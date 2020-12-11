app.controller('searchController', function ($scope,$location,searchService) {
    //搜索
    $scope.search = function () {
        searchService.search($scope.searchMap).success(
            function (response) {
                $scope.resultMap = response;//搜索返回的结果
                //创建页码
                createPage();
            }
        );
    }

    createPage = function () {
        var firstPage = 1;
        var lastPage = $scope.resultMap.totalPage;
        $scope.pageTag = [];

        $scope.firstDot = true;  //前面有点
        $scope.lastDot = true;  //后边有点


        if ($scope.resultMap.totalPage > 5) {
            if ($scope.searchMap.pageNo <= 3) {
                lastPage = 5;
                $scope.firstDot = false;
            } else if ($scope.searchMap.pageNo >= $scope.resultMap.totalPage - 2) {
                firstPage = $scope.resultMap.totalPage - 4;
                $scope.lastDot = false;
            } else {
                firstPage = $scope.searchMap.pageNo - 2;
                lastPage = $scope.searchMap.pageNo + 2;

            }
        } else {
            $scope.firstDot = false;
            $scope.lastDot = false;
        }
        for (var i = firstPage; i <= lastPage; i++) {
            $scope.pageTag.push(i);
        }
    }

    //改变当前页
    $scope.changePage = function (page) {

        page = parseInt(page);

        if (page < 1 || page > $scope.resultMap.totalPage) {
            return;
        }
        $scope.searchMap.pageNo = page;
        $scope.search();

    }

    //数据
    $scope.searchMap = {
        'keywords': '',
        'category': '',
        'brand': '',
        'spec': {},
        'price': '',
        'pageNo': 1,
        'pageSize': 20
        ,'sortField':''
        ,'sortVal':''
    };




    //搜索对象
    //添加搜索条件
    $scope.addSearch = function (key, val) {

        if (key == "category" || key == "brand" || key == "price") {
            $scope.searchMap[key] = val;
        } else {
            $scope.searchMap.spec[key] = val;
        }

        $scope.search();
    }

    //移除搜索条件
    $scope.removeSearch = function (key) {

        if (key == "category" || key == "brand" || key == "price") {
            $scope.searchMap[key] = '';
        } else {
            //delete  可以删除对象中的键
            delete $scope.searchMap.spec[key];
        }

        $scope.search();
    }

    //添加排序方法
    $scope.addSort = function (sortF,sortV) {
        $scope.searchMap.sortField = sortF;
        $scope.searchMap.sortVal = sortV;

        $scope.search();

    }

    //进入页面加载关键字
    $scope.loadKw = function () {

        //接受传过来的参数
        var kw = $location.search()['kw'];
        if(kw == null){
            return;
        }
        $scope.searchMap.keywords = kw;
        $scope.search();
    }

});
