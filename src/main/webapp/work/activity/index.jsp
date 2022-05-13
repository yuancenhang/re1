<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String basePath = request.getScheme() + "://" + request.getServerName() + ":" +request.getServerPort() + request.getContextPath() + "/"; %>
<!--_________________________https__________________________localhost______________________8080_______________________crm-->
<html>
<head>
	<base href=<%=basePath%>>
<meta charset="UTF-8">
	<!--时间选择样式-->
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
	<!--jquery-->
<script type="text/javascript" src="jquery-3.6.0.js"></script>
<script type="text/javascript" src="jquery-1.11.1-min.js"></script>
	<!--时间选择插件-->
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
	<!--分页插件-->
	<!--分页样式-->
<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>
<link href="jquery/bs_pagination/jquery.bs_pagination.min.css" type="text/css" rel="stylesheet"/>

<script type="text/javascript">

	//页面加载完毕
	$(function(){
		var allBox = $("#allBox"); //全选框的变量

		//进入本页面时刷新展示列表
		loadActivityList(1,3);
		allBox.prop("checked",false);

		//给需要填日期的text框加上时间选择器插件
		$(".time").datetimepicker({
			minView:"month",
			language:'zh-CN',
			format:'yyyy-mm-dd',
			autoclose:true,
			todayBtn:true,
			pickerPosition:"bottom-left"
		})

		//创建活动按钮点击事件
		$("#btn-create").on("click",function (){
			//每次点开清空表单中的信息
			$("#create-Activity")[0].reset();

			//从数据库获取user名字填进下拉框中
			$.ajax({
				url:"work/activity/getUserList.sv",
				type:"get",
				dataType:"json",
				success:function (data) {
					var html = "";
					$.each(data,function (i,n) {
						html += "<option value='"+n.id+"'>"+n.name+"</option>";
					})
					$("#create-marketActivityOwner").html(html);
				}
			})
			$("#createActivityModal").modal("show");
			$("#create-marketActivityOwner").val("${user.id}")
		})

		//市场活动的保存活动按钮被点击
		$("#saveBtn").on("click",function () {
			//把表单提交到服务器保存
			$.ajax({
				url:"work/activity/save.sv",
				type:"post",
				dataType: "json",
				data:{
					"owner":$.trim($("#create-marketActivityOwner>option:selected").html()),
					"name":$.trim($("#create-marketActivityName").val()),
					"startDate":$.trim($("#create-startTime").val()),
					"endDate":$.trim($("#create-endTime").val()),
					"cost":$.trim($("#create-cost").val()),
					"description":$.trim($("#create-describe").val())
				},
				success:function (data){
					if (data.ok){
						alert("创建成功");
						//清空隐藏域和查询框，刷新展示列表
						$(".hidden-search").val();
						$(".search-in").val();
						loadActivityList(1,$("#pageDiv").bs_pagination("getOption","currentPage"));
						//关闭模态窗口
						$("#createActivityModal").modal("hide");
					}else {
						alert("创建失败,请检查");
					}
				}
			})
		})

		//查询按钮被单击
		$("#serachBtn").on("click",function (){
			//把查询框的信息存进隐藏域
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startTime").val($.trim($("#search-startTime").val()));
			$("#hidden-endTime").val($.trim($("#search-endTime").val()));
			//刷新展示列表
			loadActivityList(1,$("#pageDiv").bs_pagination("getOption","currentPage"));
		})

		//全选和取消全选的功能
		allBox.on("click",function (){
			$("input[name=box]").prop("checked",allBox.prop("checked"));
		})
		//框框被单击事件，因为框框是动态创建的，所以要给有效的上级标签创建点击事件，传入3个参数
		$("#activityList").on("click",$("input[name=box]"),function (){
			//如果框框总数量和被选中的数量相等，为true
			allBox.prop("checked",$("input[name=box]").length===$("input[name=box]:checked").length);
		})

		//删除活动按钮被单机
		$("#btn-delete").on("click",function (){
			var box = $("input[name=box]:checked");
			if (box.length===0){
				alert("请选择要删除的活动项目！");
				return false;
			}
			if (confirm("确定要删除所选的活动项目吗？")){
				var text = "";
				$.each(box,function (i,b) {
					text += "id=" + b.value;
					if (i!==box.length-1){
						text += "&";
					}
				})
				$.ajax({
					url:"work/activity/delete.sv",
					type:"post",
					dataType:"json",
					data:text,
					success:function (data) {
						if (data.ok){
							loadActivityList(1,$("#pageDiv").bs_pagination("getOption","currentPage"));
						}else {
							alert("删除活动失败！！！");
						}
					}
				})
			}
		})
		//活动页修改按钮被单击
		$("#btn_edit").on("click",function (){
			var box = $("input[name=box]:checked");
			if (box.length!==1){
				alert("被修改的活动项目只能有一个！");
				return false;
			}
			edit(box.val());
		})
		//编辑div的关闭按钮被单击，不保存
		$("#btn-editClose").on("click",function (){
			$("#editActivityModal").modal("hide");
		})
		//编辑div的保存按钮被单击，要保存
		$("#btn-editUpdate").on("click",function (){
			editSave();
		})
	});
	//封装的修改保存方法，update
	function editSave(){
		$.ajax({
			url:"work/activity/editSave.sv",
			dataType:"json",
			type:"post",
			data:{
				"activityId" : $("input[name=box]:checked").val(),
				"owner" : $("#edit-marketActivityOwner>option:selected").val(), //user的id,也就是activity的owner
				"activityName" : $("#edit-marketActivityName").val(),
				"startDate" : $("#edit-startTime").val(),
				"endDate" :$("#edit-endTime").val(),
		  		"cost" :$("#edit-cost").val(),
				"description" :$("#edit-describe").val()
			},
			success : function (data) {
				if (data.ok){
					alert("修改成功");
					loadActivityList($("#pageDiv").bs_pagination("getOption","rowsPerPage"),$("#pageDiv").bs_pagination("getOption","currentPage"));
					$("#editActivityModal").modal("hide");
				}else {
					alert("修改失败");
				}
			}
		})
	}
	//封装的活动修改方法,id是活动的id
	function edit(id){
		//获取数据，填入框框里，所有者，活动名称，开始日期，结束日期，成本，描述
		$.ajax({
			url:"work/activity/edit.sv",
			dataType:"json",
			type:"get",
			data:"id=" + id,
			success:function (data) {
				//设置所有者
				var html = "";
				$.each(data.userList,function (i,n){
					html += "<option value='"+n.id+"'>"+n.name+"</option>";
				})
				$("#edit-marketActivityOwner").html(html);
				//填活动名称
				$("#edit-marketActivityName").val(data.activityName);
				$("#edit-startTime").val(data.startDate);
				$("#edit-endTime").val(data.endDate);
				$("#edit-cost").val(data.cost);
				$("#edit-describe").val(data.description);
			}
		})
		$("#editActivityModal").modal("show");
	}

	//封装的刷新市场活动列表的方法,分页显示
	function loadActivityList(pageNo,pageSize){
		//把数据从隐藏域中取出来，
		var name = $("#hidden-name").val();
		var owner = $("#hidden-owner").val();
		var startTime = $("#hidden-startTime").val();
		var endTime = $("#hidden-endTime").val();
		//填在查询框中
		$("#search-name").val(name);
		$("#search-owner").val(owner);
		$("#search-startTime").val(startTime);
		$("#search-endTime").val(endTime);
		$.ajax({
			url:"work/activity/pageList.sv",
			type:"post",
			dataType: "json",
			data:{
				"pageNo":pageNo,
				"pageSize":pageSize,
				"name":name,
				"owner":owner,
				"startDate":startTime,
				"endDate":endTime
			},
			success:function (data){
				//查到的总条数total，activity的集合
				var html = "";
				$.each(data.list,function (i,n) {
					html += '<tr class="active">';
					html += '<td><input type="checkbox" name="box" value="'+n.id+'"/></td>';
					html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'work/activity/detail.sv?id='+n.id+'\';">'+n.name+'</a></td>';
					html += '<td>'+n.owner+'</td>';
					html += '<td>'+n.startDate+'</td>';
					html += '<td>'+n.endDate+'</td>';
					html += '</tr>';
				})
				$("#activityList").html(html);
				//加载完毕给显示分页的div加组件（插件完成）
				//计算：总页数=总条数/每页条数，取模判断是否整除
				var pageTotal = data.total%pageSize===0?data.total/pageSize:Math.floor(data.total/pageSize)+1;
				$("#pageDiv").bs_pagination({
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
						loadActivityList(data.currentPage,data.rowsPerPage);
					}
				})

			}
		})
	}
</script>
	<title>市场活动</title>
</head>
<body>
	<%--记录查询用的输入框的隐藏域--%>
	<input type="hidden" id="hidden-name" class="hidden-search">
	<input type="hidden" id="hidden-owner" class="hidden-search">
	<input type="hidden" id="hidden-startTime" class="hidden-search">
	<input type="hidden" id="hidden-endTime" class="hidden-search">
	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form" id="create-Activity">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startTime">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endTime">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">

								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName"/>
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label time">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startTime"/>
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label time">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endTime"/>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost"/>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" id="btn-editClose">关闭</button>
					<button type="button" class="btn btn-primary" id="btn-editUpdate">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input id="search-name" class="form-control search-in" type="text">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input id="search-owner" class="form-control search-in" type="text">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input id="search-startTime" class="form-control time search-in" type="text" id="startTime" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input id="search-endTime" class="form-control time search-in" type="text" id="endTime">
				    </div>
				  </div>
				  
				  <button type="button" id="serachBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="btn-create"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="btn_edit"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="btn-delete"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="allBox"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>

					<%--已经存在的市场活动的列表--%>
					<tbody id="activityList"></tbody>
				</table>
			</div>
			<!--放分页组件的div-->
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="pageDiv"></div>
			</div>
			
		</div>
		
	</div>
</body>
</html>