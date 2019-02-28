<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- 引入jeeplus ajax版本库文件，该文件压缩了jQuery，datatime等常用js文件以提高加载速度 -->
<link rel="stylesheet" href="${ctxStatic}/common/css/vendor.css" />
<script src="${ctxStatic}/common/vendor/ckeditor/ckeditor.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/js/vendor.js"></script><!-- 该插件已经集成jquery 无需重复引入 -->
<script src="${ctxStatic}/common/js/moment-locales.js"></script><!-- 语言环境插件 -->
<link href="${ctxStatic}/plugin/awesome/4.4/css/font-awesome.min.css" rel="stylesheet" />
<script src="${ctxStatic}/plugin/tpl/mustache.min.js" type="text/javascript"></script>
<link rel=File-List href="${ctxStatic}/common/incomingstatistics.files/filelist.xml">
<link rel=Stylesheet href="${ctxStatic}/common/incomingstatistics.files/stylesheet.css">

<style>
<!--table
	{mso-displayed-decimal-separator:"\.";
	mso-displayed-thousand-separator:"\,";}
@page
	{margin:1.0in .75in 1.0in .75in;
	mso-header-margin:.5in;
	mso-footer-margin:.5in;}
ruby
	{ruby-align:left;}
rt
	{color:windowtext;
	font-size:9.0pt;
	font-weight:400;
	font-style:normal;
	text-decoration:none;
	font-family:宋体;
	mso-generic-font-family:auto;
	mso-font-charset:134;
	mso-char-type:none;
	display:none;}
-->
</style>


<title></title>
</head>
<body link=blue vlink=purple>

	 <form:form id="searchForm" modelAttribute="contractDetailWarningList" class="form form-horizontal well clearfix">
	 	<table border=0 cellpadding=0 cellspacing=0 width=367 style='border-collapse:collapse;table-layout:fixed;width:276pt;margin:auto'>
	 		<col width=229 style='mso-width-source:userset;mso-width-alt:8135;width:172pt'>
 			<col width=69 span=2 style='mso-width-source:userset;mso-width-alt:2446;width:52pt'>
	 		
	 		<thead>
	 			<tr height=19 style='height:14.4pt'>
  					<td height=19 class=xl65 width=229 style='height:14.4pt;width:172pt'>供应商名称</td>
  					<td class=xl65 width=69 style='border-left:none;width:52pt'>来料批次</td>
  					<td class=xl65 width=69 style='border-left:none;width:52pt'>不良批次</td>
 				</tr>
	 		
	 		</thead>
	 		
	 		<tbody id="contractDetailList">
			</tbody>
	 	</table>
	 	 <script type="text/template" id="contractDetailTpl">
				<tr height=19 style='height:14.4pt'>
  					<td height=19 class=xl66 width=229 style='height:14.4pt;border-top:none;width:172pt'>{{row.supName}}</td>
  					<td class=xl66 align=right width=69 style='border-top:none;border-left:none;width:52pt'>{{row.sumBatch}}</td>
  					<td class=xl66 align=right width=69 style='border-top:none;border-left:none;width:52pt'>{{row.badBatch}}</td>
 				</tr>

		 </script>
		 
		 <script type="text/javascript">
		var contractDetailRowIdx =1;
		var contractDetailTpl = $("#contractDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		$(document).ready(function() {
			var data = ${fns:toJson(contractDetailWarningList)};
			for (var i=0; i<data.length; i++){
				
				
				//data[i].createDate=data[i].createDate.split(" ")[0];
				//data[i].arriveDate=data[i].arriveDate.split(" ")[0];
				addRow('#contractDetailList', contractDetailRowIdx, contractDetailTpl, data[i]);
				contractDetailRowIdx = contractDetailRowIdx + 1;
			}	
		});
		
		function addRow(list, idx, tpl, row){
			$(list).append(Mustache.render(tpl, {
				idx: idx, delBtn: true, row: row
			}));
			$(list+idx).find("select").each(function(){
				$(this).val($(this).attr("data-value"));
			});
			$(list+idx).find("input[type='checkbox'], input[type='radio']").each(function(){
				var ss = $(this).attr("data-value").split(',');
				for (var i=0; i<ss.length; i++){
					if($(this).val() == ss[i]){
						$(this).attr("checked","checked");
					}
				}
			});
			$(list+idx).find(".form_datetime").each(function(){
				 $(this).datetimepicker({
					// format: "YYYY-MM-DD",
					 format: "YYYY-MM-DD HH:mm:ss",
					 widgetPositioning:{vertical :"bottom"}
			    });
			});
		}
		
		</script>
	 	
	 	
	 </form:form>

</body>
</html>