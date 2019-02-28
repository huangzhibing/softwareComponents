<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name=ProgId content=Excel.Sheet>
<meta name=Generator content="Microsoft Excel 15">

<link rel=Stylesheet href=${ctxStatic}/common/css/pofile_CSS.css>

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
	{mso-footer-data:"第 &P 页，共 &N 页";
	margin:.75in .2in .75in .2in;
	mso-header-margin:.31in;
	mso-footer-margin:.31in;
	mso-horizontal-page-align:center;
	mso-vertical-page-align:center;}
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

<form:form id="inputForm" modelAttribute="contractMain" action="#" method="post" class="form-horizontal">


 <table border=0 cellpadding=0 cellspacing=0 width=762 style='border-collapse:collapse;table-layout:fixed;width:574pt'>
 <col width=52 style='mso-width-source:userset;mso-width-alt:1664;width:39pt'>
 <col width=50 style='mso-width-source:userset;mso-width-alt:1600;width:38pt'>
 <col width=54 style='mso-width-source:userset;mso-width-alt:1728;width:41pt'>
 <col width=98 style='mso-width-source:userset;mso-width-alt:3136;width:74pt'>
 <col width=88 style='mso-width-source:userset;mso-width-alt:2816;width:66pt'>
 <col width=41 style='mso-width-source:userset;mso-width-alt:1312;width:31pt'>
 <col width=61 style='mso-width-source:userset;mso-width-alt:1952;width:46pt'>
 <col width=70 style='mso-width-source:userset;mso-width-alt:2240;width:53pt'>
 <col width=88 style='mso-width-source:userset;mso-width-alt:2816;width:66pt'>
 <col width=83 style='mso-width-source:userset;mso-width-alt:2656;width:62pt'>
 <col width=77 style='mso-width-source:userset;mso-width-alt:2464;width:58pt'>
 <tr height=30 style='height:22.5pt'>
  <td colspan=11 height=30 width=762 style='height:22.5pt;width:574pt'
  align=left valign=top>
  <![if !vml]><span style='mso-ignore:vglayout;
  position:absolute;z-index:1;margin-left:595px;margin-top:0px;width:123px;
  height:44px'><img width=123 height=44 src=${ctxStatic}/common/images/pofile_image.png v:shapes="图片_x0020_1"></span><![endif]><span
  style='mso-ignore:vglayout2'>
 
  <table cellpadding=0 cellspacing=0>
   <tr>
    <td colspan=11 rowspan=2 height=49 class=xl180 width=762 style='height:36.5pt;
    width:574pt'>湖南中晟智造实业发展有限公司</td>
   </tr>
  </table>
  </span></td>
 </tr>
 <tr height=48 style='mso-height-source:userset;height:36.0pt'>
  <td colspan=11 height=48 class=xl181 width=762 style='height:36.0pt;
  width:574pt'>地址：湖南省常德市鼎城区灌溪镇高新技术开发区科技创新创业产业园厂房第10栋</td>
 </tr>
 <tr height=34 style='height:25.5pt'>
  <td colspan=11 height=34 class=xl182 style='height:25.5pt'>采 购 订 单</td>
 </tr>
 <tr height=29 style='mso-height-source:userset;height:21.95pt'>
  <td colspan=2 height=29 class=xl176 width=102 style='height:21.95pt;
  width:77pt'>供应商名称:</td>
  <td colspan=3 class=xl165 width=240 style='width:181pt'>${contractMain.supName}</td>
  <td colspan=2 class=xl176 width=102 style='width:77pt'>采购员：<span
  style='mso-spacerun:yes'>&nbsp;</span></td>
  <td colspan=4 class=xl165 width=318 style='width:239pt'>${contractMain.buyerName}</td>
 </tr>
 <tr height=29 style='mso-height-source:userset;height:21.95pt'>
  <td colspan=2 height=29 class=xl176 width=102 style='height:21.95pt;
  width:77pt'>联系人:</td>
  <td colspan=3 class=xl165 width=240 style='width:181pt'>${contractMain.accountLinkMam}</td>
  <td colspan=2 class=xl183 width=102 style='width:77pt'>付款方式：</td>
  <td colspan=4 class=xl174 width=318 style='width:239pt'>${contractMain.paymodeName}</td>
 </tr>
 <tr height=29 style='mso-height-source:userset;height:21.95pt'>
  <td colspan=2 height=29 class=xl176 width=102 style='height:21.95pt;
  width:77pt'>地址:</td>
  <td colspan=3 class=xl165 width=240 style='width:181pt'>${contractMain.supAddress}</td>
  <td colspan=2 class=xl171 width=102 style='width:77pt'>送货地址：</td>
  <td colspan=4 class=xl175 width=318 style='width:239pt'>${contractMain.deliveryAddress}</td>
 </tr>
 <tr height=29 style='mso-height-source:userset;height:21.95pt'>
  <td colspan=2 height=29 class=xl176 width=102 style='height:21.95pt;
  width:77pt'>电话:<span style='mso-spacerun:yes'>&nbsp;</span></td>
  <td colspan=3 class=xl165 width=240 style='width:181pt'>${contractMain.accountTelNum}</td>
  <td colspan=2 class=xl176 width=102 style='width:77pt'>电话：</td>
  <td colspan=4 class=xl174 width=318 style='width:239pt'>${contractMain.buyerPhoneNum}</td>
 </tr>
 <tr height=29 style='mso-height-source:userset;height:21.95pt'>
  <td colspan=2 height=29 class=xl176 width=102 style='height:21.95pt;
  width:77pt'>传真:<span style='mso-spacerun:yes'>&nbsp;</span></td>
  <td colspan=3 class=xl165 width=240 style='width:181pt'>${contractMain.accountFaxNum}</td>
  <td colspan=2 class=xl176 width=102 style='width:77pt'>传真：</td>
  <td colspan=4 class=xl174 width=318 style='width:239pt'>${contractMain.buyerFaxNum}</td>
 </tr>
 
 </table>  
 
 <table  border=0 cellpadding=0 cellspacing=0 width=737 style='border-collapse:collapse;width:100%'>   
   <thead style="display:table-header-group">
 
	 <tr class=xl130 height=28 style='mso-height-source:userset;height:21.0pt'>
	  <td colspan=2 height=28 class=xl188 style='height:21.0pt'>P/O NO订单号:</td>
	  <td colspan=2 class=xl169 style='border-right:.5pt solid black'>${contractMain.billNum}</td>
	  <td colspan=2 class=xl152 width=129 style='border-left:none;width:97pt'>币别:人民币</td>
	  <td class=xl152 width=61 style='border-left:none;width:46pt'>税率：</td>
	  <td class=xl151 style='border-left:none'>${contractMain.taxRatioPrint}</td>
	  <td class=xl152 width=88 style='border-left:none;width:66pt'>下单日期：</td>
	  <td colspan=2 class=xl184 style='border-left:none'><font class="font20">${contractMain.madeDate}</font></td>
	 </tr>
	 <tr height=32 style='height:24.0pt'>
	  <td height=32 class=xl164 width=52 style='height:24.0pt;border-top:none;
	  width:39pt'>行号</td>
	  <td colspan=2 class=xl164 width=104 style='border-left:none;width:79pt'>物料编号</td>
	  <td class=xl164 width=98 style='border-top:none;border-left:none;width:74pt'>物料名称</td>
	  <td class=xl164 width=88 style='border-top:none;border-left:none;width:66pt'>规<span
	  style='mso-spacerun:yes'>&nbsp; </span>格</td>
	  <td class=xl153 width=41 style='border-top:none;border-left:none;width:31pt'>单位</td>
	  <td class=xl164 width=61 style='border-top:none;border-left:none;width:46pt'>数量</td>
	  <td class=xl154 width=70 style='border-top:none;border-left:none;width:53pt'>含税单价</td>
	  <td class=xl154 width=88 style='border-top:none;border-left:none;width:66pt'>金<span
	  style='mso-spacerun:yes'>&nbsp; </span>额</td>
	  <td class=xl154 width=83 style='border-top:none;border-left:none;width:62pt'>到货日期</td>
	  <td class=xl154 width=77 style='border-top:none;border-left:none;width:58pt'>备 注</td>
	 </tr>
   
   </thead>
       <tbody id="contractDetailList">
	   </tbody>
	 <tr height=37 style='mso-height-source:userset;height:28.35pt'>
	  <td colspan=8 height=37 class=xl172 width=514 style='height:28.35pt;width:388pt'>合计：</td>
	  <td colspan=3 class=xl166 width=248 style='border-right:.5pt solid black; width:186pt'>&yen;${contractMain.goodsSumTaxedStr}</td>
	 </tr>
 
 </table>  
 
 <!-- <table>  -->
 
 <table  border=0 cellpadding=0 cellspacing=0 width=737 style='border-collapse:collapse;table-layout:fixed;width:552pt'>   

 <tr height=19 style='height:14.25pt'>
  <td height=19 class=xl132 width=52 style='height:14.25pt;width:39pt'></td>
  <td class=xl132 width=50 style='width:38pt'></td>
  <td class=xl133></td>
  <td colspan=2 class=xl186>　</td>
  <td class=xl134></td>
  <td class=xl133></td>
  <td class=xl135 width=70 style='width:53pt'></td>
  <td colspan=2 class=xl187>　</td>
  <td></td>
 </tr>
 <tr height=19 style='height:14.25pt'>
  <td height=19 class=xl138 style='height:14.25pt'>备注:</td>
  <td class=xl136></td>
  <td class=xl136></td>
  <td class=xl136></td>
  <td class=xl136></td>
  <td class=xl136></td>
  <td class=xl137></td>
  <td class=xl138></td>
  <td class=xl137></td>
  <td class=xl137></td>
  <td></td>
 </tr>
 <tr height=30 style='mso-height-source:userset;height:22.5pt'>
  <td colspan=11 height=30 class=xl177 width=762 style='height:22.5pt;
  width:574pt'>1.收到订单后，请与24小时内盖章签字回传或E-mail给我司，逾期没有回传，视为默认所有的交货日期。</td>
 </tr>
 <tr height=39 style='mso-height-source:userset;height:29.25pt'>
  <td colspan=11 height=39 class=xl185 width=762 style='height:29.25pt;
  width:574pt'>2.送货时必须有随货同行的送货单，注明P/O NO(订单号码)、物料编号、名称规格，数量，并符合买受人的包装的要求，否则买受人仓库有权拒绝签单。</font></td>
 </tr>
 <tr height=25 style='mso-height-source:userset;height:18.75pt'>
  <td colspan=8 height=25 class=xl177 width=514 style='height:18.75pt;
  width:388pt'>3.交货及运费承担：出卖人将货送至买受人指定仓库交货，运费由出卖人承担。</td>
  <td class=xl138></td>
  <td class=xl138></td>
  <td class=xl163></td>
 </tr>
 <tr class=xl131 height=27 style='mso-height-source:userset;height:20.25pt'>
  <td colspan=11 height=27 class=xl177 width=762 style='height:20.25pt;
  width:574pt'>4.付款前提条件：货物检验合格入库后，根据实际到货验收入库数出卖人开据增值税专用发票。</td>
 </tr>
 <tr height=34 style='mso-height-source:userset;height:25.5pt'>
  <td colspan=11 height=34 class=xl185 width=762 style='height:25.5pt;
  width:574pt'>5.质量要求：出卖人物料必须达到双方协定的质量验收标准及ROHS要求，送货时必需随货同行出货检验报告单，否则，仓库拒收所有的物料。</td>
 </tr>
 <tr height=45 style='mso-height-source:userset;height:33.75pt'>
  <td colspan=11 height=45 class=xl185 width=762 style='height:33.75pt;
  width:574pt'>6.本订单经买受人及出卖人签字或盖章后生效，与《采购框架协议》及其附件等构成买卖双方签订的协议整体，除订单特别注明的条款外，所有未尽事宜适用其他协议的相关条款。</font></td>
 </tr>
 <tr height=19 style='height:14.25pt'>
  <td height=19 class=xl139 style='height:14.25pt'></td>
  <td class=xl139></td>
  <td class=xl139></td>
  <td class=xl139></td>
  <td class=xl139></td>
  <td class=xl139></td>
  <td class=xl139></td>
  <td class=xl139></td>
  <td class=xl139></td>
  <td class=xl139></td>
  <td></td>
 </tr>
 <tr height=44 style='mso-height-source:userset;height:33.0pt'>
  <td height=44 class=xl140 colspan=3 style='height:33.0pt;mso-ignore:colspan'>买受人（盖章）：</td>
  <td class=xl140></td>
  <td class=xl141></td>
  <td class=xl141></td>
  <td class=xl140 colspan=3 style='mso-ignore:colspan'>出卖人（盖章）：</td>
  <td class=xl145></td>
  <td></td>
 </tr>
 <tr height=44 style='mso-height-source:userset;height:33.0pt'>
  <td height=44 class=xl140 colspan=3 style='height:33.0pt;mso-ignore:colspan'>授权人（签字）：</td>
  <td class=xl140></td>
  <td class=xl141></td>
  <td class=xl141></td>
  <td class=xl140 colspan=3 style='mso-ignore:colspan'>授权人（签字）：</td>
  <td class=xl140></td>
  <td></td>
 </tr>
 <tr height=44 style='mso-height-source:userset;height:33.0pt'>
  <td height=44 class=xl142 colspan=2 style='height:33.0pt;mso-ignore:colspan'>日期：</td>
  <td class=xl143></td>
  <td class=xl140></td>
  <td class=xl140></td>
  <td class=xl141></td>
  <td class=xl142>日期：</td>
  <td class=xl144></td>
  <td class=xl140></td>
  <td class=xl140></td>
  <td></td>
 </tr>
 <![if supportMisalignedColumns]>
 <tr height=0 style='display:none'>
  <td width=52 style='width:39pt'></td>
  <td width=50 style='width:38pt'></td>
  <td width=54 style='width:41pt'></td>
  <td width=98 style='width:74pt'></td>
  <td width=88 style='width:66pt'></td>
  <td width=41 style='width:31pt'></td>
  <td width=61 style='width:46pt'></td>
  <td width=70 style='width:53pt'></td>
  <td width=88 style='width:66pt'></td>
  <td width=83 style='width:62pt'></td>
  <td width=77 style='width:58pt'></td>
 </tr>
 <![endif]>
</table>
    
<script type="text/template" id="contractDetailTpl">			
 <tr height=37 style='mso-height-source:userset;height:28.35pt'>
  <td height=37 class=xl148 width=52 style='height:28.35pt;border-top:none;width:39pt'>{{idx}}</td>
  <td colspan=2 class=xl178 width=104 style='border-right:.5pt solid black;border-left:none;width:79pt'>{{row.item.code}}</td>
  <td class=xl155 width=98 style='border-left:none;width:74pt'>{{row.itemName}}</td>
  <td class=xl155 width=88 style='border-left:none;width:66pt'>{{row.itemModel}}</td>
  <td class=xl157 width=41 style='border-left:none;width:31pt'><span style='mso-spacerun:yes'>&nbsp;</span>{{row.measUnit}}</td>
  <td class=xl159 width=61 style='border-left:none;width:46pt'><span style='mso-spacerun:yes'>&nbsp; </span>{{row.itemQty}} </td>
  <td class=xl146 width=70 style='border-left:none;width:53pt'>&yen;{{row.itemPriceTaxed}}</td>
  <td class=xl146 width=88 style='border-left:none;width:66pt'>&yen;{{row.itemSumTaxed}}</td>
  <td class=xl147 width=83 style='border-left:none;width:62pt'>{{row.arriveDate}}</td>
  <td class=xl161 width=77 style='border-left:none;width:58pt'>{{row.massRequire}}</td>
 </tr>
</script>
			
    <script type="text/javascript">
		var contractDetailRowIdx =1;
		var contractDetailTpl = $("#contractDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		$(document).ready(function() {
			var data = ${fns:toJson(contractMain.contractDetailList)};
			for (var i=0; i<data.length; i++){
				var d=(data[i].arriveDate).replace('-',"/");
				d=d.replace('-',"/");
				data[i].arriveDate=d.split(" ")[0];
				//含税单价逗号分隔显示
				data[i].itemPriceTaxed =dealNumber(data[i].itemPriceTaxed);
				//金额逗号分隔显示
				data[i].itemSumTaxed =dealNumber(data[i].itemSumTaxed);
				
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
		
		
		/*将100000转为100,000.00形式*///数字按千分位进行分割显示（逗号分隔）	
		var dealNumber = function(money){
		    if(money && money!=null){
		        money = String(money);
		        var left=money.split('.')[0],right=money.split('.')[1];
		        right = right ? (right.length>=2 ? '.'+right.substr(0,2) : '.'+right+'0') : '.00';
		        var temp = left.split('').reverse().join('').match(/(\d{1,3})/g);
		        return (Number(money)<0?"-":"") + temp.join(',').split('').reverse().join('')+right;
		    }else if(money===0){   //注意===在这里的使用，如果传入的money为0,if中会将其判定为boolean类型，故而要另外做===判断
		        return '0.00';
		    }else{
		        return "";
		    }
		};
		
		/*将100,000.00转为100000形式*/
		var undoNubmer = function(money){
		    if(money && money!=null){
		        money = String(money);
		        var group = money.split('.');
		        var left = group[0].split(',').join('');
		        return Number(left+"."+group[1]);
		    }else{
		        return "";
		    }
		};
		
   </script>
   

</form:form>
</body>
</html>