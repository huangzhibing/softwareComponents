<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name=ProgId content=Excel.Sheet>
    <meta name=Generator content="Microsoft Excel 15">

    <link rel=Stylesheet href=${ctxStatic}/common/css/pofile_styleNew.css>

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
        .jz{
            text-align: center;
        }
        -->
    </style>

</head>
<body link=blue vlink=purple>

<form:form id="inputForm" modelAttribute="contractMain" action="#" method="post" class="form-horizontal">



    <table border=0 width:100% cellpadding=0 cellspacing=0 width=737 style='width:100%;border-collapse:
 collapse;table-layout:fixed;'>
        <col width=52 style='mso-width-source:userset;mso-width-alt:1664;width:39pt'>
        <col width=94 style='mso-width-source:userset;mso-width-alt:3008;width:71pt'>
        <col width=83 style='mso-width-source:userset;mso-width-alt:2656;width:62pt'>
        <col width=88 style='mso-width-source:userset;mso-width-alt:2816;width:66pt'>
        <col width=41 style='mso-width-source:userset;mso-width-alt:1312;width:31pt'>
        <col width=61 style='mso-width-source:userset;mso-width-alt:1952;width:46pt'>
        <col width=70 style='mso-width-source:userset;mso-width-alt:2240;width:53pt'>
        <col width=88 style='mso-width-source:userset;mso-width-alt:2816;width:66pt'>
        <col width=83 style='mso-width-source:userset;mso-width-alt:2656;width:62pt'>
        <col width=77 style='mso-width-source:userset;mso-width-alt:2464;width:58pt'>
        <col width=77 style='mso-width-source:userset;mso-width-alt:2464;width:58pt'>
        <col width=77 style='mso-width-source:userset;mso-width-alt:2464;width:58pt'>
        <tr height=30 style='height:22.5pt'>
            <td colspan=10 height=30 width=737 style='height:22.5pt;width:554pt'
                align=left valign=top>

                <![if !vml]><span style='mso-ignore:vglayout;
  position:absolute;z-index:1;margin-left:600px;margin-top:3px;width:123px;
  height:44px'><img width=123 height=44 src=${ctxStatic}/common/images/pofile_image.png v:shapes="图片_x0020_1"></span><![endif]><span
                    style='mso-ignore:vglayout2'>
  <table cellpadding=0 cellspacing=0>
   <tr>
    <td colspan=10 height=30 class=xl157 width=737 style='height:22.5pt;
    width:554pt'>湖南中晟智造实业发展有限公司</td>
   </tr>
  </table>
  </span></td>
        </tr>
        <tr height=48 style='mso-height-source:userset;height:36.0pt'>
            <td colspan=10 height=48 class=xl172 width=737 style='height:36.0pt;
  width:554pt'>地址：湖南省常德市鼎城区灌溪镇高新技术开发区科技创新创业产业园厂房第10栋</td>
        </tr>


        <tr height=34 style='height:25.5pt'>
            <td colspan=10 height=34 class=xl159 style='height:25.5pt'>中晟智造物品放行单 <span style="float: right;font-size: 10px">NO:${invOutMain.billNum}</span></td>
        </tr>
        <tr height=29 style='mso-height-source:userset;height:21.95pt'>
            <td colspan=8 height=29 class=xl155 width=52 style='height:21.95pt;width:39pt;text-align: left'></td>
            <td colspan=4 class=xl168 width=318 style='width:239pt'>放行日期:<fmt:formatDate value="${invOutMain.awayDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
        </tr>

    </table>

    <table  border=0 cellpadding=0 cellspacing=0 width=737 style='border-collapse:collapse;width: 100%'>
        <colgroup>
            <col width="52" style="mso-width-source:userset;mso-width-alt:1664;width:5%">
            <col width="50" style="mso-width-source:userset;mso-width-alt:3008;width:7%">
            <col width="83" style="mso-width-source:userset;mso-width-alt:2656;width:5%">
            <col width="88" style="mso-width-source:userset;mso-width-alt:2816;width:5%">
            <col width="41" style="mso-width-source:userset;mso-width-alt:1312;width:5%">
            <col width="61" style="mso-width-source:userset;mso-width-alt:1952;width:5%">
        </colgroup>
        <thead style="display:table-header-group">
        <tr class=xl130 height=28 style='mso-height-source:userset;height:21.0pt'>
            <td height=28 class=xl160 style='height:21.0pt'>序号</td>
            <td class=xl154  style='border-left:none;'>料号</td>
            <td class=xl156 style='border-left:none'>物品名称</td>
            <td class=xl161 style='border-left:none'>数量</td>
            <td class=xl154  style='border-left:none;'>单位</td>
            <td class=xl154  style='border-left:none;'>备注</td>
        </tr>
        </thead>
        <tbody id="contractDetailList">
        </tbody>
    </table>

    <table  style="table-layout:fixed;width: 100%">
        <td class=xl158 style="text-align: left">经办人:${billMain.wareName}</td>
        <td class=xl158 style="text-align: left">部门审批:</td>
        <td class=xl158 style="text-align: left">行政部签字:${billMain.accountName}</td>
    </table>

    <script type="text/template" id="contractDetailTpl">
        <tr height=37 style='mso-height-source:userset;height:28.35pt'>
            <td class=xl132 width=52 style='height:28.35pt;width:39pt'>{{idx}}</td>
            <td class=xl133 width=94  style='text-align: center'>　{{row.itemCode}}</td>
            <td class=xl134 width=83  style='text-align: center'>　{{row.itemName}}</td>
            <td class=xl134 width=88  style='text-align: center'>{{row.itemQty}}　</td>
            <td class=xl136 width=61 style='text-align: center'>{{row.measUnit}} </td>
            <td  class=xl137 width=70 style='border-left:none;width:53pt'></td>
        </tr>
    </script>

    <script type="text/javascript">
        var contractDetailRowIdx =1;
        var contractDetailTpl = $("#contractDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
        $(document).ready(function() {
            var data = ${fns:toJson(invOutMain.invOutDetailList)};
            console.log(data)
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