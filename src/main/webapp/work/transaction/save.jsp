<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Iterator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String basePath = request.getScheme() + "://" + request.getServerName() + ":" +request.getServerPort() + request.getContextPath() + "/"; %>
<!--_________________________https__________________________localhost______________________8080_______________________crm-->
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	Map<String,String> map = (Map<String, String>) application.getAttribute("Stage");
	Set<String> set = map.keySet();
%>
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
	<!--自动补全插件-->
<script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>

	<!--分页插件-->
	<!--分页样式-->
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>
	<link href="jquery/bs_pagination/jquery.bs_pagination.min.css" type="text/css" rel="stylesheet"/>
</head>
<body>
	<script type="text/javascript">
		//这个json是阶段和可能性组成的关系
		var json = {
			<%
				Iterator<String> iterator = set.iterator();
				while (iterator.hasNext()){
				String key = iterator.next();
				String value = map.get(key);
			%>
				"<%=key%>":<%=value%>,
			<%
				}
			%>
		}
		//时间选择器
		$(".time").datetimepicker({
			minView:"month",
			language:'zh-CN',
			format:'yyyy-mm-dd',
			autoclose:true,
			todayBtn:true,
			pickerPosition:"bottom-left"
		})

		$(function () {
			//交易框文本被改变，模糊查询，补全
			getCustomerList();

			//阶段下拉框选择项改变,填入可能性
			$("#create-transactionStage").on("change",function () {
				var text = $("#create-transactionStage").val();
				var possibility = json[text];
				$("#create-possibility").val(possibility);
			})

			//图标被单击，打开活动搜索窗口，加载活动列表
			$("#openActivity").on("click",function () {
				loadActivityList3(1,5);
				$("#findMarketActivity").modal("show");
			})

			//图标被单击，打开联系人搜索窗口，加载联系人列表
			$("#openContacts").on("click",function () {
				loadContactsList(1,5);
				$("#findContacts").modal("show");
			})

		})



		//从后台查询满足条件的客户名字，customer，自动补全
		function getCustomerList() {
			$("#create-accountName").typeahead({
				source:function (query,process) {
					$.ajax({
						url:"work/transaction/getCustomerList.sv",
						type:"get",
						dataType:"json",
						data:{
							"name":query
						},
						success:function (data){
							process(data);
						}
					})
				},
				delay:1000
			})
		}
		//封装的刷新市场活动列表的方法,分页显示
		function loadContactsList(pageNo,pageSize){
			$.ajax({
				url:"work/transaction/getContactsListByName.sv",
				type:"post",
				dataType: "json",
				data:{
					"pageNo":pageNo,
					"pageSize":pageSize,
					"fullname":$("#searchContacts").val()
				},
				success:function (data){
					//查到的总条数total，activity的集合
					var html = "";
					$.each(data.list,function (i,n) {
						html += '<tr>';
						html += '<td><input type="radio" id="'+n.id+'" name="activity"/></td>';
						html += '<td>'+n.fullname+'</td>';
						html += '<td>'+n.email+'</td>';
						html += '<td>'+n.mphone+'</td>';
						html += '</tr>';
					})
					$("#ContactsBody").html(html);
					var pageTotal = data.total%pageSize===0?data.total/pageSize:Math.floor(data.total/pageSize)+1;
					$("#pageDiv2").bs_pagination({
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
							loadContactsList(data.currentPage,data.rowsPerPage);
						}
					})
				}
			})
		}
		//封装的刷新市场活动列表的方法,分页显示
		function loadActivityList3(pageNo,pageSize){
			$.ajax({
				url:"work/transaction/getActivityListByName.sv",
				type:"post",
				dataType: "json",
				data:{
					"pageNo":pageNo,
					"pageSize":pageSize,
					"name":$("#searchActivity").val()
				},
				success:function (data){
					//查到的总条数total，activity的集合
					var html = "";
					$.each(data.list,function (i,n) {
						html += '<tr>';
						html += '<td><input type="radio" name="activity" id="'+n.id+'"/></td>';
						html += '<td>'+n.name+'</td>';
						html += '<td>'+n.startDate+'</td>';
						html += '<td>'+n.endDate+'</td>';
						html += '<td>'+n.owner+'</td>';
						html += '</tr>';
					})
					$("#ActivityBody").html(html);
					var pageTotal = data.total%pageSize===0?data.total/pageSize:Math.floor(data.total/pageSize)+1;
					$("#pageDiv1").bs_pagination({
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
							loadActivityList3(data.currentPage,data.rowsPerPage);
						}
					})
				}
			})
		}
	</script>
	<!-- 查找市场活动 -->	
	<div class="modal fade" id="findMarketActivity" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" id="searchActivity" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable3" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
							</tr>
						</thead>
						<tbody id="ActivityBody">
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
				<div style="height: 50px; position: relative;top: 20px;">
					<div id="pageDiv1"></div>
				</div>
			</div>
		</div>
	</div>

	<!-- 查找联系人 -->	
	<div class="modal fade" id="findContacts" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找联系人</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" id="searchContacts" style="width: 300px;" placeholder="请输入联系人名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>邮箱</td>
								<td>手机</td>
							</tr>
						</thead>
						<tbody id="ContactsBody">
							<%--<tr>
								<td><input type="radio" name="activity"/></td>
								<td>李四</td>
								<td>lisi@bjpowernode.com</td>
								<td>12345678901</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
				<div style="height: 50px; position: relative;top: 20px;">
					<div id="pageDiv2"></div>
				</div>
			</div>
		</div>
	</div>
	
	
	<div style="position:  relative; left: 30px;">
		<h3>创建交易</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button type="button" class="btn btn-primary">保存</button>
			<button type="button" class="btn btn-default">取消</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form class="form-horizontal" role="form" style="position: relative; top: -30px;">
		<div class="form-group">
			<label for="create-transactionOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-transactionOwner">
				  <c:forEach items="${userList}" var="u">
					  <option value="${u.id}">${u.name}</option>
				  </c:forEach>
				</select>
			</div>
			<label for="create-amountOfMoney" class="col-sm-2 control-label">金额</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-amountOfMoney">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-transactionName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-transactionName">
			</div>
			<label for="create-expectedClosingDate" class="col-sm-2 control-label">预计成交日期<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control time" id="create-expectedClosingDate">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-accountName" class="col-sm-2 control-label">客户名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-accountName" placeholder="支持自动补全，输入客户不存在则新建">
			</div>
			<label for="create-transactionStage" class="col-sm-2 control-label">阶段<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
			  <select class="form-control" id="create-transactionStage">
				  <c:forEach items="${stageList}" var="s">
					  <option value="${s.value}">${s.text}</option>
				  </c:forEach>
			  </select>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-transactionType" class="col-sm-2 control-label">类型</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-transactionType">
					<c:forEach items="${transactionTypeList}" var="t">
						<option value="${t.value}">${t.text}</option>
					</c:forEach>
				</select>
			</div>
			<label for="create-possibility" class="col-sm-2 control-label">可能性</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-possibility">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-clueSource" class="col-sm-2 control-label">来源</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-clueSource">
					<c:forEach items="${sourceList}" var="s">
						<option value="${s.value}">${s.text}</option>
					</c:forEach>
				</select>
			</div>
			<label for="create-activitySrc" class="col-sm-2 control-label">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" id="openActivity"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-activitySrc" readonly>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactsName" class="col-sm-2 control-label">联系人名称&nbsp;&nbsp;<a href="javascript:void(0);" id="openContacts"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-contactsName" readonly>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-describe" class="col-sm-2 control-label">描述</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-describe"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control time" id="create-nextContactTime">
			</div>
		</div>
		
	</form>
</body>
</html>