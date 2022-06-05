<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% String basePath = request.getScheme() + "://" + request.getServerName() + ":" +request.getServerPort() + request.getContextPath() + "/"; %>
<!--_________________________https__________________________localhost______________________8080_______________________crm-->
<!DOCTYPE html>
<html>
<head>
	<base href=<%=basePath%>>
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>


<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <!--分页插件-->
    <!--分页样式-->
    <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>
    <link href="jquery/bs_pagination/jquery.bs_pagination.min.css" type="text/css" rel="stylesheet"/>
<script type="text/javascript">
	$(function(){
		$("#isCreateTransaction").click(function(){
			if(this.checked){
				$("#create-transaction2").show(200);
			}else{
				$("#create-transaction2").hide(200);
			}
		});
		//搜索活动的图标被单击，打开搜索窗口，加载活动列表
		$("#searchBtn").click(function () {
			loadActivityList2(1,10);
		})
		//搜索框回车键被按下
		$("#searchName").keydown(function (key) {
			if (key.keyCode==13){

				loadActivityList2(1,5,name);
				return false;
			}
		})
        //关联活动的确定按钮被单击
        $("#qdBtn").on("click",function () {
            if ($(":radio:checked").length==1){
                write();
            }else {
                alert("请选择一个市场活动源！");
            }
        })
        //转换按钮被单击
        $("#zhuanHuan").on("click",function () {
            zhuanHuan();
        })
	});

    //把线索转换成客户和联系人
    function zhuanHuan() {
        if ($("#isCreateTransaction").prop("checked")){
            //如果要创建交易,用表单的方式，能发post请求
			$("#flag").val("yes"); //yes是一个标记，在controller判断是否创建交易
            $("#form1").submit();
			$("#flag").val("no");
        }else {
            //不创建交易
            window.location.href="work/clue/zhuanHuan.sv?clueId=${clue.id}";
        }
    }
    //把活动源填进文本框
    function write() {
        var id = $(":radio:checked").val();
        $("#activityId").val(id);
        var name = $("#"+id).html();
        $("#activity").val(name);
        $("#searchActivityModal").modal("hide");
    }
	//封装的打开窗口，加载活动函数
	function loadActivityList2(pageNo,pageSize) {
		$.ajax({
			url:"work/clue/getAllActivityList.sv",
			type:"get",
			dataType:"json",
			data: {
				"pageNo":pageNo,
				"pageSize":pageSize,
				"activityName":$("#searchName").val()
			},
			success:function (data){
				var html = "";
				$.each(data.list,function (i,n) {
					html += '<tr>';
					html += '<td><input type="radio" value="'+n.id+'" name="activity"/></td>';
					html += '<td id="'+n.id+'">'+n.name+'</td>';
					html += '<td>'+n.startDate+'</td>';
					html += '<td>'+n.endDate+'</td>';
					html += '<td>'+n.owner+'</td>';
					html += '</tr>';
				})
				$("#tbody").html(html);
				var pageTotal = data.total%pageSize===0?data.total/pageSize:Math.floor(data.total/pageSize)+1;
				$("#pageDiv2").bs_pagination({
					currentPage:pageNo, //第几页
					rowsPerPage:pageSize, //每页几条
					maxRowsPerPage: 10, //每页最多几条
					totalPages:pageTotal, //总页数
					totalRows:data.total, //总条数
					visiblePageLinks: 5,  //显示几个卡片，如123456
					showGoToPage: true,  //是否显示去多少页
					showRowsPerPage: true,  //是否显示？
					showRowsInfo: true,  //是否显示？
					showRowsDefaultInfo: true,  //是否显示？
					//页面变化，再次调用加载页面函数，从数据库拿数据
					onChangePage:function (event,data){
						loadActivityList2(data.currentPage,data.rowsPerPage);
					}
				})
				$("#searchActivityModal").modal("show");
			}
		})
	}
</script>

</head>
<body>
	
	<!-- 搜索市场活动的模态窗口 -->
	<div class="modal fade" id="searchActivityModal" role="dialog" >
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">搜索市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" style="width: 300px;" id="searchName" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
								<td></td>
							</tr>
						</thead>
						<tbody id="tbody">
							<%--<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="qdBtn">确定</button>
                </div>
				<!--分页-->
				<div style="height: 50px; position: relative;top: 30px;">
					<div id="pageDiv2"></div>
				</div>

			</div>
		</div>
	</div>

	<div id="title" class="page-header" style="position: relative; left: 20px;">
		<h4>转换线索 <small>${clue.fullname}${clue.appellation}-${clue.company}</small></h4>
	</div>
	<div id="create-customer" style="position: relative; left: 40px; height: 35px;">
		新建客户：${clue.company}
	</div>
	<div id="create-contact" style="position: relative; left: 40px; height: 35px;">
		新建联系人：${clue.fullname}${clue.appellation}
	</div>
	<div id="create-transaction1" style="position: relative; left: 40px; height: 35px; top: 25px;">
		<input type="checkbox" id="isCreateTransaction"/>
		为客户创建交易
	</div>
	<div id="create-transaction2" style="position: relative; left: 40px; top: 20px; width: 80%; background-color: #F7F7F7; display: none;" >
	
		<form id="form1" method="post" action="work/clue/zhuanHuan.sv">
			<input type="hidden" name="clueId" value="${clue.id}">
            <input type="hidden" id="activityId" name="activityId">
			<input type="hidden" id="flag" name="flag">
		  <div class="form-group" style="width: 400px; position: relative; left: 20px;">
		    <label for="amountOfMoney">金额</label>
		    <input type="text" class="form-control" id="amountOfMoney" name="money">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="tradeName">交易名称</label>
		    <input type="text" class="form-control" id="tradeName" value="动力节点-" name="name">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="expectedClosingDate">预计成交日期</label>
		    <input type="text" class="form-control" id="expectedClosingDate" name="expectedDate">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="stage">阶段</label>
		    <select id="stage"  class="form-control" name="stage">
				<c:forEach items="${stageList}" var="s">
					<option value="${s.value}">${s.text}</option>
				</c:forEach>
		    </select>
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="activity">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" id="searchBtn" style="text-decoration: none;"><span class="glyphicon glyphicon-search"></span></a></label>
		    <input type="text" class="form-control" id="activity" name="source" placeholder="点击上面搜索" readonly>
		  </div>
		</form>
		
	</div>
	
	<div id="owner" style="position: relative; left: 40px; height: 35px; top: 50px;">
		记录的所有者：<br>
		<b>${clue.owner}</b>
	</div>
	<div id="operation" style="position: relative; left: 40px; height: 35px; top: 100px;">
		<input class="btn btn-primary" type="button" id="zhuanHuan" value="转换">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input class="btn btn-default" type="button" value="取消">
	</div>
</body>
</html>