<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name=ProgId content=Excel.Sheet>
    <meta name=Generator content="Microsoft Excel 15">

    <link rel=Stylesheet href=${ctxStatic}/common/css/profile_styleInbond.css>

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
        {margin:.39in .16in .39in .16in;
            mso-header-margin:.31in;
            mso-footer-margin:.31in;}
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
        </style>
</head>

<form:form id="inputForm" modelAttribute="billMain" action="#" method="post" class="form-horizontal">

<body link=blue vlink=purple>

<table border=0 cellpadding=0 cellspacing=0 width=770 style='border-collapse:
 collapse;table-layout:fixed;width:576pt'>
    <col class=xl65 width=31 style='mso-width-source:userset;mso-width-alt:998;
 width:23pt'>
    <col width=114 style='mso-width-source:userset;mso-width-alt:3635;width:85pt'>
    <col width=135 style='mso-width-source:userset;mso-width-alt:4326;width:101pt'>
    <col width=131 style='mso-width-source:userset;mso-width-alt:4198;width:98pt'>
    <col width=95 style='mso-width-source:userset;mso-width-alt:3046;width:71pt'>
    <col width=64 span=2 style='mso-width-source:userset;mso-width-alt:2048;
 width:48pt'>
    <col width=112 style='mso-width-source:userset;mso-width-alt:3584;width:84pt'>
    <col width=24 style='mso-width-source:userset;mso-width-alt:768;width:18pt'>
    <tr height=21 style='height:15.6pt'>
        <td colspan=8 height=21 width=746 style='height:15.6pt;width:558pt'
            align=left valign=top>

   <span style='mso-ignore:vglayout;
  position:absolute;z-index:1;margin-left:214px;margin-top:0px;width:32px;
  height:16px'><img width=32 height=16 src=${ctxStatic}/common/images/pofile_image.png v:shapes="Picture_x0020_1"></span><![endif]><span
                style='mso-ignore:vglayout2'>
  <table cellpadding=0 cellspacing=0>
   <tr>
    <td colspan=8 height=21 class=xl104 width=746 style='height:15.6pt;
    width:558pt'><span
            style='mso-spacerun:yes'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    </span>湖南中晟智造实业发展有限公司<span style='mso-spacerun:yes'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></td>
   </tr>
  </table>
  </span></td>
        <td width=24 style='width:18pt'></td>
    </tr>
    <tr height=27 style='height:20.4pt'>
        <td colspan=8 height=27 class=xl84 style='height:20.4pt'><span
                style='mso-spacerun:yes'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </span>（半）成品入库单<span
                style='mso-spacerun:yes'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </span>NO：${billMain.billNum}</td>
        <td></td>
    </tr>
    <tr class=xl67 height=22 style='mso-height-source:userset;height:16.5pt'>
        <td height=22 class=xl67 style='height:16.5pt'>车间：  </td>
        <td class=xl67 colspan=2 style='mso-ignore:colspan'>  ${billMain.deptName}</td>
        <td class=xl67></td>
        <td class=xl67></td>
        <td class=xl68></td>
        <td colspan=2 class=xl88>日期：<fmt:formatDate pattern="yyyy年MM月dd日" value="${billMain.billDate}"/></td>
        <td class=xl67></td>
    </tr>
    <tr class=xl66 height=44 style='mso-height-source:userset;height:33.0pt'>
        <td height=44 class=xl69 width=31 style='height:33.0pt;width:23pt'>序号</td>
        <td class=xl70 width=114 style='border-left:none;width:85pt'>生产单号</td>
        <td class=xl70 width=135 style='border-left:none;width:101pt'>产品料号</td>
        <td class=xl70 width=131 style='border-left:none;width:98pt'>产品名称</td>
        <td class=xl70 width=95 style='border-left:none;width:71pt'>规格型号</td>
        <td class=xl70 width=64 style='border-left:none;width:48pt'>数量</td>
        <td class=xl70 width=64 style='border-top:none;border-left:none;width:48pt'>单位</td>
        <td class=xl70 width=112 style='border-top:none;border-left:none;width:84pt'>备注</td>

    </tr>

    <tbody id="contractDetailList"></tbody>

    <tr class=xl75 height=14 style='mso-height-source:userset;height:10.2pt'>
        <td colspan=8 height=14 class=xl86 style='height:10.2pt'>　</td>
        <td class=xl75></td>
    </tr>
    <tr class=xl73 height=28 style='mso-height-source:userset;height:21.0pt'>
        <td height=28 class=xl73 colspan=2 style='height:21.0pt;mso-ignore:colspan'>车间统计：</td>
        <td class=xl73>车间主管：</td>
        <td class=xl68>Q<font class="font11">A:</font></td>
        <td class=xl73></td>
        <td class=xl73></td>
        <td class=xl74>成品仓：${billMain.wareName}</td>
        <td class=xl73></td>
    </tr>

    <![if supportMisalignedColumns]>
    <tr height=0 style='display:none'>
        <td width=31 style='width:23pt'></td>
        <td width=114 style='width:85pt'></td>
        <td width=135 style='width:101pt'></td>
        <td width=131 style='width:98pt'></td>
        <td width=95 style='width:71pt'></td>
        <td width=64 style='width:48pt'></td>
        <td width=64 style='width:48pt'></td>
        <td width=112 style='width:84pt'></td>
        <td width=24 style='width:18pt'></td>
    </tr>
    <![endif]>
</table>

    <script type="text/template" id="contractDetailTpl">
        <%--<tr class=xl66 height=50 style='mso-height-source:userset;height:37.8pt'>--%>
            <%--<td height=50 class=xl69 style='height:37.8pt;border-top:none'>${billMain.corBillNum}</td>--%>
            <%--<td class=xl69 style='border-top:none;border-left:none'>{{row.item.id}}</td>--%>
            <%--<td colspan=5 class=xl70 width=399 style='border-left:none;width:300pt'>{{row.itemName}}<br>--%>
                <%--{{row.itemSpec}}</td>--%>
            <%--<td class=xl69 style='border-top:none;border-left:none'>{{row.measUnit}}</td>--%>
            <%--<td class=xl70 width=75 style='border-top:none;border-left:none;width:56pt'>{{row.itemQtyTemp}}</td>--%>
            <%--<td class=xl69 style='border-top:none;border-left:none'>{{row.itemQty}}</td>--%>
        <%--</tr>--%>
        <tr height=30 style='mso-height-source:userset;height:22.5pt'>
            <td height=30 class=xl70 style='height:22.5pt;border-top:none'>{{row.serialNum}}</td>
            <td class=xl70 style='border-top:none;border-left:none'>　</td>
            <td class=xl70 width=135 style='border-top:none;border-left:none;width:101pt'>{{row.item.id}}</td>
            <td class=xl70 style='border-top:none;border-left:none'>{{row.itemName}}</td>
            <td class=xl70 style='border-top:none;border-left:none'>{{row.itemSpec}}</td>
            <td class=x170 style='border:.5pt solid windowtext;text-align:center;vertical-align:middle'>{{row.itemQty}}</td>
            <td class=xl70 >{{row.measUnit}}</td>
            <td class=xl70 style='border-top:none;border-left:none'>{{row.note}}</td>
        </tr>
    </script>


    <script type="text/javascript" >
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
