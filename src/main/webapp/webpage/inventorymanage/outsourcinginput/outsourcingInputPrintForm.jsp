<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name=ProgId content=Excel.Sheet>
<meta name=Generator content="Microsoft Excel 15">

<link rel=Stylesheet href=${ctxStatic}/common/css/pofile_styleModify.css>

<!-- 引入jeeplus ajax版本库文件，该文件压缩了jQuery，datatime等常用js文件以提高加载速度 -->
<link rel="stylesheet" href="${ctxStatic}/common/css/vendor.css" />
<script src="${ctxStatic}/common/vendor/ckeditor/ckeditor.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/js/vendor.js"></script><!-- 该插件已经集成jquery 无需重复引入 -->
<script src="${ctxStatic}/common/js/moment-locales.js"></script><!-- 语言环境插件 -->
<link href="${ctxStatic}/plugin/awesome/4.4/css/font-awesome.min.css" rel="stylesheet" />
<script src="${ctxStatic}/plugin/tpl/mustache.min.js" type="text/javascript"></script>

 <style>
  <!--table
  {mso-displayed-decimal-separator:"\.";
   mso-displayed-thousand-separator:"\,";}
  @page
  {margin:.2in 0in .2in 0in;
   mso-header-margin:.12in;
   mso-footer-margin:.12in;
   mso-horizontal-page-align:center;}
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
</head>
<body link=blue vlink=purple>

<form:form id="inputForm" modelAttribute="billMain" action="#" method="post" class="form-horizontal">


 <table border=0 cellpadding=0 cellspacing=0 width=100% style='border-collapse:
 collapse;table-layout:fixed'>
  <col width=136 style='mso-width-source:userset;mso-width-alt:4352;width:102pt'>
  <col width=103 style='mso-width-source:userset;mso-width-alt:3302;width:102pt'>
  <col width=25 style='mso-width-source:userset;mso-width-alt:793;width:19pt'>
  <col width=46 style='mso-width-source:userset;mso-width-alt:1459;width:120pt'>
  <col width=126 style='mso-width-source:userset;mso-width-alt:4044;width:95pt'>
  <col width=74 style='mso-width-source:userset;mso-width-alt:2380;width:56pt'>
  <col width=128 style='mso-width-source:userset;mso-width-alt:4096;width:96pt'>
  <col width=46 style='mso-width-source:userset;mso-width-alt:1484;width:35pt'>
  <col width=75 style='mso-width-source:userset;mso-width-alt:2406;width:56pt'>
  <col width=78 style='mso-width-source:userset;mso-width-alt:2508;width:59pt'>
  <tr height=36 style='mso-height-source:userset;height:27.0pt'>
   <td colspan=10 height=36 width=837 style='height:27.0pt;width:629pt'
       align=left valign=top>

    <![if !vml]><span style='mso-ignore:vglayout;
  position:absolute;z-index:1;margin-left:44px;margin-top:1px;width:58px;
  height:22px'><img width=58 height=22 src=${ctxStatic}/common/images/pofile_image.png v:shapes="Picture_x0020_1"></span><![endif]><span
           style='mso-ignore:vglayout2'>
  <table cellpadding=0 cellspacing=0>
   <tr>
    <td colspan=10 height=36  width=837 style='height:27.0pt;
    width:629pt'><span
            style='mso-spacerun:yes'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    </span><font class="font7">湖南中晟智造实业发展有限公司验收入库单</font><font class="font8"><span
            style='mso-spacerun:yes'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    </span>NO</font><font class="font7">：</font><font class="font8">${billMain.billNum}<span
            style='mso-spacerun:yes'>&nbsp;&nbsp;&nbsp;</span></font></td>
   </tr>
  </table>
  </span></td>
  </tr>
  <tr class=xl68 height=24 style='mso-height-source:userset;height:18.0pt'>
   <td colspan=7 height=24 class=xl74 style='border-right:.5pt solid black;
  height:18.0pt'>供应商：${billMain.corName}</td>
   <td colspan=3 class=xl71 style='border-right:.5pt solid black'>入库日期：<fmt:formatDate pattern="yyyy年MM月dd日" value="${billMain.billDate}"/></td>
  </tr>
  <tr class=xl66 height=18 style='mso-height-source:userset;height:14.25pt'>
   <td rowspan=2 height=44 class=xl69 style='height:33.45pt;border-top:none'>采购单号</td>
   <td rowspan=2 class=xl69 style='border-top:none'>料<span
           style='mso-spacerun:yes'>&nbsp; </span>号</td>
   <td colspan=5 rowspan=2 class=xl69><a name=品名规格>品名规格</a></td>
   <td rowspan=2 class=xl69 style='border-top:none'>单位</td>
   <td rowspan=2 class=xl70 width=75 style='border-top:none;width:56pt'>送货数量</td>
   <td rowspan=2 class=xl69 style='border-top:none'>实收数量</td>
  </tr>
  <tr class=xl66 height=26 style='mso-height-source:userset;height:19.2pt'>
  </tr>
  <tbody id="contractDetailList">
  </tbody>
  <tr class=xl66 height=30 style='mso-height-source:userset;height:23.25pt'>
   <td height=30 class=xl69 style='height:23.25pt;border-top:none'>送货单号</td>
   <td class=xl69 style='border-top:none;border-left:none'>收货仓</td>
   <td colspan=2 class=xl69 style='border-left:none'>供应商签名</td>
   <td colspan=2 class=xl69 style='border-left:none'>车间审核人</td>
   <td colspan=2 class=xl69 style='border-left:none'>检验签名</td>
   <td colspan=2 class=xl69 style='border-left:none'>仓管签名</td>
  </tr>
  <tr class=xl66 height=46 style='mso-height-source:userset;height:34.2pt'>
   <td height=46 class=xl69 style='height:34.2pt;border-top:none'>${billMain.corBillNum}</td>
   <td class=xl69 style='border-top:none;border-left:none'>${billMain.wareName}</td>
   <td colspan=2 class=xl69 style='border-left:none'>　</td>
   <td colspan=2 class=xl69 style='border-left:none'>　</td>
   <td colspan=2 class=xl69 style='border-left:none'>　</td>
   <td colspan=2 class=xl69 style='border-left:none'>　</td>
  </tr>
  <tr class=xl66 height=36 style='mso-height-source:userset;height:27.0pt'>
   <td colspan=5 class=xl73 height=36 style="text-align:left;" class=xl71 style='height:27.0pt;border-top:none'>通知验收时间：<fmt:formatDate pattern="yyyy年MM月dd日HH时mm分" value="${invCheckMain.makeDate}" type="both"/></td>
   <td colspan=2 class=xl73 style='text-align:left;border-left:none'>检验时间：</td>
   <td colspan=3 class=xl73 style='text-align:left;border-left:none'>接收时间：</td>
  </tr>
  <tr class=xl66 height=34 style='mso-height-source:userset;height:26.25pt'>
   <td height=34 class=xl69 style='height:26.25pt;border-top:none'>备注</td>
   <td colspan=9 class=xl72 style='border-left:none;border-right:.5pt solid black'>${billMain.note}</td>
  </tr>
  <tr height=21 style='height:15.6pt'>
   <td height=21 colspan=10 style='height:15.6pt;mso-ignore:colspan'></td>
  </tr>
  <tr height=29 style='mso-height-source:userset;height:21.75pt'>
   <td height=29 colspan=2 style='height:21.75pt;mso-ignore:colspan'></td>
   <td class=xl67></td>
   <td class=xl67></td>
   <td class=xl67></td>
   <td class=xl67></td>
   <td class=xl67></td>
   <td class=xl67></td>
   <td class=xl67></td>
   <td class=xl67></td>
  </tr>
  <![if supportMisalignedColumns]>
  <tr height=0 style='display:none'>
   <td width=136 style='width:102pt'></td>
   <td width=103 style='width:77pt'></td>
   <td width=25 style='width:19pt'></td>
   <td width=46 style='width:34pt'></td>
   <td width=126 style='width:95pt'></td>
   <td width=74 style='width:56pt'></td>
   <td width=128 style='width:96pt'></td>
   <td width=46 style='width:35pt'></td>
   <td width=75 style='width:56pt'></td>
   <td width=78 style='width:59pt'></td>
  </tr>
  <![endif]>
 </table>


    <script type="text/template" id="contractDetailTpl">
     <tr class=xl66 height=50 style='mso-height-source:userset;height:37.8pt'>
      <td height=50 class=xl69 style='height:37.8pt;border-top:none'>${billMain.corBillNum}</td>
      <td class=xl69 style='border-top:none;border-left:none'>{{row.item.id}}</td>
      <td colspan=5 class=xl70 width=399 style='border-left:none;width:300pt'>{{row.itemName}}<br>
       {{row.itemSpec}}</td>
      <td class=xl69 style='border-top:none;border-left:none'>{{row.measUnit}}</td>
      <td class=xl70 width=75 style='border-top:none;border-left:none;width:56pt'>{{row.itemQtyTemp}}</td>
      <td class=xl69 style='border-top:none;border-left:none'>{{row.itemQty}}</td>
     </tr>
	</script>
		
		<script type="text/javascript">
		var contractDetailRowIdx =1;
		var contractDetailTpl = $("#contractDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		$(document).ready(function() {
			var data = ${fns:toJson(billMain.billDetailList)};
			console.log(data);
			for (var i=0; i<data.length; i++){
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