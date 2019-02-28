<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<!-- _login_page_ --><!--登录超时标记 勿删-->
<!DOCTYPE html>
<html>

	<head>
	<link href='http://cdn.webfont.youziku.com/webfonts/nomal/115717/46306/5b1244e7f629d805b8a9799e.css' rel='stylesheet' type='text/css' />
	<link href='http://cdn.webfont.youziku.com/webfonts/nomal/115717/45902/5b12453ff629d805b8a979a0.css' rel='stylesheet' type='text/css' />
	<meta name="decorator" content="ani"/>
		<title>登录</title>
		<script>
			if (window.top !== window.self) {
				window.top.location = window.location;
			}
		</script>
		<script type="text/javascript">
				$(document).ready(function() {
					$("#loginForm").validate({
						rules: {
							validateCode: {remote: "${pageContext.request.contextPath}/servlet/validateCodeServlet"}
						},
						messages: {
							username: {required: "请填写用户名."},password: {required: "请填写密码."},
							validateCode: {remote: "验证码不正确.", required: "请填写验证码."}
						},
						errorLabelContainer: "#messageBox",
						errorPlacement: function(error, element) {
							error.appendTo($("#loginError").parent());
						} 
					});
				});
				// 如果在框架或在对话框中，则弹出提示并跳转到首页
				if(self.frameElement && self.frameElement.tagName == "IFRAME" || $('#left').length > 0 || $('.jbox').length > 0){
					alert('未登录或登录超时。请重新登录，谢谢！');
					top.location = "${ctx}";
				}
		</script>
	<style type="text/css">
		::-webkit-input-placeholder { /* WebKit browsers */
			color:black;
		}
		:-moz-placeholder { /* Mozilla Firefox 4 to 18 */
			color:black;
		}
		::-moz-placeholder { /* Mozilla Firefox 19+ */
			color:black;
		}
		:-ms-input-placeholder { /* Internet Explorer 10+ */
			color:black;
		}
	</style>
	</head>

	
	<body>
		<div class="login-page">
		<div class="row">
			<div class="col-md-8 col-lg-6 col-md-offset-2 col-lg-offset-6">
				<h1 class="css12648671b41c405">湖南中晟智造实业发展有限公司</h1>
				<h2 class="css126487c6761c405" style="color: #0d6dbe; margin-top: 8%;">智能制造管理系统</h2>
				<sys:message content="${message}" showType="1"/>
				<form id="loginForm" class=" col-md-8 col-lg-6 col-md-offset-2 col-lg-offset-3" role="form" action="${ctx}/login" method="post">
					<div class="form-content" >
						<div class="form-group">
							<input type="text" id="username" name="username" class="form-control input-underline input-lg required" placeholder="用户名 UserName">
						</div>

						<div class="form-group">
							<input type="password" id="password" name="password" class="form-control input-underline input-lg required" placeholder="密码 PassWord">
						</div>
						<c:if test="${isValidateCodeLogin}">
						<div class="form-group  text-muted">
								<label class="inline"><font color="white">验证码:</font></label>
							<sys:validateCode name="validateCode" inputCssStyle="margin-bottom:5px;" buttonCssStyle="color:white"/>
						</div>
						</c:if>
							<%--<ul class="pull-right btn btn-info btn-circle" style="background-color:white;height:45px;width:46px">	--%>
								<%--<li class="dropdown color-picker" >--%>
									<%--<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">--%>
										<%--<span><i class="fa fa-circle"></i></span>--%>
									<%--</a>--%>
									<%--<ul class="dropdown-menu pull-right animated fadeIn" role="menu">--%>
										<%--<li class="padder-h-xs">--%>
											<%--<table class="table color-swatches-table text-center no-m-b">--%>
												<%--<tr>--%>
													<%--<td class="text-center colorr">--%>
														<%--<a href="#" data-theme="blue" class="theme-picker">--%>
															<%--<i class="fa fa-circle blue-base"></i>--%>
														<%--</a>--%>
													<%--</td>--%>
													<%--<td class="text-center colorr">--%>
														<%--<a href="#" data-theme="green" class="theme-picker">--%>
															<%--<i class="fa fa-circle green-base"></i>--%>
														<%--</a>--%>
													<%--</td>--%>
													<%--<td class="text-center colorr">--%>
														<%--<a href="#" data-theme="red" class="theme-picker">--%>
															<%--<i class="fa fa-circle red-base"></i>--%>
														<%--</a>--%>
													<%--</td>--%>
												<%--</tr>--%>
												<%--<tr>--%>
													<%--<td class="text-center colorr">--%>
														<%--<a href="#" data-theme="purple" class="theme-picker">--%>
															<%--<i class="fa fa-circle purple-base"></i>--%>
														<%--</a>--%>
													<%--</td>--%>
													<%--<td class="text-center color">--%>
														<%--<a href="#" data-theme="midnight-blue" class="theme-picker">--%>
															<%--<i class="fa fa-circle midnight-blue-base"></i>--%>
														<%--</a>--%>
													<%--</td>--%>
													<%--<td class="text-center colorr">--%>
														<%--<a href="#" data-theme="lynch" class="theme-picker">--%>
															<%--<i class="fa fa-circle lynch-base"></i>--%>
														<%--</a>--%>
													<%--</td>--%>
												<%--</tr>--%>
											<%--</table>--%>
										<%--</li>--%>
									<%--</ul>--%>
								<%--</li>--%>
						<%--</ul>--%>
						<label class="inline">
								<input  type="checkbox" id="rememberMe" name="rememberMe" ${rememberMe ? 'checked' : ''} class="ace" />
								<span class="lbl"> 记住我</span>
						</label>
						<a class="2bl" style="font-weight: 300; margin-left: 70px;">忘记密码?</a>
					</div>
					<input type="submit" class="btn btn-primary btn-outline btn-lg btn-rounded progress-login"  value="登录">
					&nbsp;
				</form>
			</div>			
		</div>
	</div>
	
	<script>

		
$(function(){
		$('.theme-picker').click(function() {
			changeTheme($(this).attr('data-theme'));
		}); 	
	
});

function changeTheme(theme) {
	$('<link>')
	.appendTo('head')
	.attr({type : 'text/css', rel : 'stylesheet'})
	.attr('href', '${ctxStatic}/common/css/app-'+theme+'.css');
	//$.get('api/change-theme?theme='+theme);
	 $.get('${pageContext.request.contextPath}/theme/'+theme+'?url='+window.top.location.href,function(result){  });
}
</script>
<style>
li.color-picker i {
    font-size: 24px;
    line-height: 30px;
}

</style>
</body>
</html>