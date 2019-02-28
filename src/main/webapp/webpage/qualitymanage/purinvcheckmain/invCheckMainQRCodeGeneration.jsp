<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>采购到货生成二维码</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">采购到货生成二维码</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
		<script>
            function printQRCode(){
                var obj = $("#QRCode").val();//物料二维码字符串，多个用逗号隔开：例如:1234556698725544,879357349525298
				console.log(obj);
                if(obj==""||obj==null){
                    jp.alert("请输入需要打印的物料二维码！");
                    return;
                }
                var codeNames;
                codeNames = getCodeNames(obj);

                $.ajax({
                    type: "POST",
                    url: '${ctx}/tools/TwoDimensionCodeController/createBatchTwoDimensionCode',
                    data: {encoderContent:obj,tm:new Date().getTime()},
                    dataType:'json',
                    cache: false,
                    success: function(data){
                        if(data.success){
                            $("#printContent").html("");
                            var names = data.body.names.split(",");
                            var item = "<table>";
                            for(var i = 0;i<names.length;i++){
                                var file = data.body.fartherFilePath+names[i]+".png";
                                var itemname = codeNames[i];
                                item += '<tr><td><label style="font-size: 1px;">'+itemname+'</label><br/><img cache="false"  width="65px" height="65px"  class="block" style="vertical-align: middle" src="'+file+'"/></td></tr>'
                            }
                            document.getElementById("printContent").style.visibility="visible";
                            $("#printContent").html(item+"</table>");
                            $("#printContent").jqprint();
                            $("#printContent").html("");
                            document.getElementById("printContent").style.visibility="hidden";
                        }else{
                            jp.alert('后台读取出错！');
                            return;
                        }
                    }
                })
            }
            function getCodeNames(obj){
                var names;
                $.ajax({
                    type: "POST",
                    url: '${ctx}/purinvcheckmain/invCheckMain/getItemNames?obj='+obj,
                    dataType:'TEXT',
                    cache: false,
                    async: false,
                    success: function(data){
						console.log(data);
                        names = String(data).split(",");
                    },error:function(data){
                        jp.alert("后台读取出错！");
                    }
                })
				return names;
			}
		</script>
		<div class="form-horizontal">
			<fieldset>
				<%--form-group 实现这个一个组--%>
				<div class="form-group">
					<%--label标签占十二列中间的两列 control-label使标签居左对齐--%>
					<label class="col-sm-1 control-label">输入物料二维码:</label>
					<%--占七列 并且造一个input组--%>
					<div class="col-sm-4 input-group">
						<input id = "QRCode" name="QRCodeName" type="text" placeholder="多个物料二维码使用英文逗号','隔开" class="form-control">
						<span class="input-group-btn">
						<button id="print" class="btn btn-success" onclick="printQRCode()">
							<i class="glyphicon glyphicon-folder-open"></i> 打印
						</button>
					</span>
					</div>
				</div>
			</fieldset>
		</div>
		<!--打印的div-->
		<div  id="printContent">
		</div>
	</div>
	</div>
	</div>
</body>
</html>