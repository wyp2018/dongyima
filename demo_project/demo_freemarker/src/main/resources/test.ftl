<html>
<head>
    <meta charset="utf-8">
    <title>Freemarker入门小DEMO </title>
</head>
<body>

<#include 'head.ftl'>

<#--我只是一个注释，我不会有任何输出  -->
    <#assign  userName="root" />
    <#assign  info={"mobile":"15237227777","address":"北京市朝阳区"} >
    <#assign age=18 />

    ${name},你好.${message}



    <hr/>
    获取自定义的变量 :${userName}
    对象的值:${info.mobile}>>>${info.address}
<hr/>

<#if age &gt;= 18>
    你已通过实名认证
    <#else>
    你未通过实名认证
</#if>



<hr/>

循环遍历:<br />
<ul>
    <#list goodsList as l>
    <li>${l_index}>>>${l.name}>>>>>${l.price}</li>
    </#list>

</ul>


<hr/>

函数:<br/>
获取集合的长度:${goodsList?size}

<hr/>
<#--//转换json格式 ?eval-->
<#--//获取长得 ?size-->
<#assign text="{'bank':'工商银行','account':'1888888888888888888'}" />
开户行:${text?eval.bank}   账号:${text?eval.account}<br />

当前日期:${today?date}<br />
当前时间:${today?time}<br />
当前日期+时间:${today?datetime}<br />
日期格式化:${today?string('yyyy年MM月dd日 HH:mm:ss')}

<br>
余额:${money?c}
<br>
空置处理:<br/>
<#if aaa??>
    aaa存在
    <#else >
    aaa变量不存在
</#if><br>
判断空值另一种形式:<br>
${vvv!'vvv不存在'}


</body>
</html>
