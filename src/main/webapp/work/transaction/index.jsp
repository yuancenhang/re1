<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String basePath = request.getScheme() + "://" + request.getServerName() + ":" +request.getServerPort() + request.getContextPath() + "/"; %>
<!--_________________________https__________________________localhost______________________8080_______________________crm-->
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<base href=<%=basePath%>>
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <!--分页插件-->
    <!--分页样式-->
    <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>
    <link href="jquery/bs_pagination/jquery.bs_pagination.min.css" type="text/css" rel="stylesheet"/>

<script type="text/javascript">

	$(function(){
        //刷新交易列表
        loadTran(1,5);
        //查询按钮被单击，刷列表，$("#pageBody").bs_pagination("getOption","rowsPerPage")
		$("#searchBtn").click(function () {
            loadTran(1,5);
        })
		
	});

    //封装的刷新交易列表的方法
    function loadTran(pageNo,pageSize) {
        $.ajax({
            url:"work/transaction/loadTranList.sv",
            type:"get",
            dataType:"json",
            data:{
                "pageNo":pageNo,
                "pageSize":pageSize,
                "owner":$.trim($("#owner").val()),
                "name":$.trim($("#name").val()),
                "customerName":$.trim($("#customerName").val()), //联表处理
                "stage":$.trim($("#stage").val()),
                "type":$.trim($("#type").val()),
                "source":$.trim($("#source").val()),
                "contactsName":$.trim($("#contactsName").val()) //联表处理
            },
            success:function (data){
                var html = "";
                $.each(data.list,function (i,n) {
                    html += '<tr>';
                    html += '<td><input type="checkbox" id="'+n.id+'" /></td>';
                    html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'work/transaction/detail.sv?id='+n.id+'\';">'+n.name+'</a></td>';
                    html += '<td>'+n.customerId+'</td>'; //已经把ID处理成名字
                    html += '<td>'+n.stage+'</td>';
                    html += '<td>'+n.type+'</td>';
                    html += '<td>'+n.owner+'</td>';
                    html += '<td>'+n.source+'</td>';
                    html += '<td>'+n.contactsId+'</td>'; //已经把ID处理成名字
                    html += '</tr>';
                })
                $("#TranBody").html(html);
                var pageTotal = data.total%pageSize===0?data.total/pageSize:Math.floor(data.total/pageSize)+1;
                $("#pageBody").bs_pagination({
                    currentPage:pageNo, //第几页
                    rowsPerPage:pageSize, //每页几条
                    maxRowsPerPage: 20, //每页最多几条
                    totalPages:pageTotal, //总页数
                    totalRows:data.total, //总条数
                    visiblePageLinks: 5,  //显示几个卡片，如123456
                    showGoToPage: true,  //是否显示去多少页
                    showRowsPerPage: true,  //是否显示？
                    showRowsInfo: true,  //是否显示？
                    showRowsDefaultInfo: true,  //是否显示？
                    //页面变化，再次调用加载页面函数，从数据库拿数据
                    onChangePage:function (event,data){
                        loadTran(data.currentPage,data.rowsPerPage);
                    }
                })
            }
        })
    }
</script>
</head>
<body>

	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>交易列表</h3>
			</div>
		</div>
	</div>
	
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
	
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="owner">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">客户名称</div>
				      <input class="form-control" type="text" id="customerName">
				    </div>
				  </div>
				  
				  <br>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">阶段</div>
					  <select class="form-control" id="stage">
						  <option></option>
					  	<c:forEach items="${stageList}" var="s">
                            <option value="${s.value}">${s.text}</option>
                        </c:forEach>
					  </select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">类型</div>
					  <select class="form-control" id="type">
						  <option></option>
                          <c:forEach items="${typeList}" var="s">
                              <option value="${s.value}">${s.text}</option>
                          </c:forEach>
					  </select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">来源</div>
				      <select class="form-control" id="source">
						  <option></option>
                          <c:forEach items="${sourceList}" var="s">
                              <option value="${s.value}">${s.text}</option>
                          </c:forEach>
						</select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">联系人名称</div>
				      <input class="form-control" type="text" id="contactsName">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 10px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" onclick="window.location.href='work/transaction/loadData.sv';"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" onclick="window.location.href='work/transaction/edit.sv';"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" /></td>
							<td>名称</td>
							<td>客户名称</td>
							<td>阶段</td>
							<td>类型</td>
							<td>所有者</td>
							<td>来源</td>
							<td>联系人名称</td>
						</tr>
					</thead>
					<tbody id="TranBody">
						<%--<tr>
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">动力节点-交易01</a></td>
							<td>动力节点</td>
							<td>谈判/复审</td>
							<td>新业务</td>
							<td>zhangsan</td>
							<td>广告</td>
							<td>李四</td>
						</tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 20px;">
				<div id="pageBody"></div>
			</div>
			
		</div>
		
	</div>
</body>
</html>