//登录服务层
app.service('uploadService',function($http){

    //上传图片
    this.uploadFile = function () {

        var form = new FormData();
        form.append("file",file.files[0]);

        return $http({

            method:'POST',
            url:"../upload.do",
            data: form,

            headers: {'Content-Type':undefined},

            transformRequest: angular.identity
        });
    }


});
