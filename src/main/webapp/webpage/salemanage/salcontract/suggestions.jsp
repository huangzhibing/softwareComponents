<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>销售合同管理</title>
	<meta name="decorator" content="ani"/>
	
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<input value="${id }" id="id" style="display:none;"/>
		<div class="panel-body">
				<div class="form-group">
					<label class="col-sm-2 control-label">审核不通过意见：</label>
					<div class="col-sm-10">
						<textarea id="suggestions" rows="4"    class="form-control "></textarea>
					</div>
				</div>
			</div>
		</div>
		</div>
				<div class="col-lg-2"></div>
		        <div class="col-lg-2">
		             <div class="form-group text-center">
		                 <div>
		                 	<a class="btn btn-primary btn-block btn-lg btn-parsley" onclick="yes()" href="#">确定</a>
		                 </div>
		             </div>
		        </div>
		        <div class="col-lg-2"></div>
		        <div class="col-lg-2">
		             <div class="form-group text-center">
		                 <div>
		                 	<a class="btn btn-primary btn-block btn-lg btn-parsley" onclick="cancle()" href="#">取消</a>
		                 </div>
		             </div>
		        </div>
		        <div class="col-lg-2"></div>
		        
		        
		</div>				
	</div>
	<script type="text/javascript">
		function yes(){
			var id=document.getElementById("id").value;
			var suggestions=document.getElementById("suggestions").value;
			if(suggestions=='' || suggestions ==null){
				layer.msg("意见不能为空");
				return;
			}
			layer.confirm("确认提交吗？",['确认','取消'],function(){
				var url="${ctx}/salcontract/contract/contractDeny";
				var parameter={id:id,suggestions:suggestions};
				$.post(url,parameter,function(data){
					if(data=="yes"){
						layer.msg("审核不通过成功");
						//parent.location.reload(true);
						parent.location.href="${ctx}/salcontract/contract/contractAudit"
					}else{
						layer.msg("审核不通过失败");
					}
				});
			})
		}
	</script>

</body>
</html>