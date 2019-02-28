<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="编号"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="隐藏域名称（ID）"%>
<%@ attribute name="value" type="java.lang.String" required="false" description="隐藏域值（ID）"%>
<%@ attribute name="labelName" type="java.lang.String" required="false" description="输入框名称（Name）"%>
<%@ attribute name="labelValue" type="java.lang.String" required="false" description="输入框值（Name）"%>
<%@ attribute name="title" type="java.lang.String" required="true" description="选择框标题"%>
<%@ attribute name="url" type="java.lang.String" required="true" description="树结构数据地址"%>
<%@ attribute name="checked" type="java.lang.Boolean" required="false" description="是否显示复选框，如果不需要返回父节点，请设置notAllowSelectParent为true"%>
<%@ attribute name="extId" type="java.lang.String" required="false" description="排除掉的编号（不能选择的编号）"%>
<%@ attribute name="isAll" type="java.lang.Boolean" required="false" description="是否列出全部数据，设置true则不进行数据权限过滤（目前仅对Office有效）"%>
<%@ attribute name="notAllowSelectRoot" type="java.lang.Boolean" required="false" description="不允许选择根节点"%>
<%@ attribute name="notAllowSelectParent" type="java.lang.Boolean" required="false" description="不允许选择父节点"%>
<%@ attribute name="allowSearch" type="java.lang.Boolean" required="false" description="是否允许搜索"%>
<%@ attribute name="allowClear" type="java.lang.Boolean" required="false" description="是否允许清除"%>
<%@ attribute name="allowInput" type="java.lang.Boolean" required="false" description="文本框可填写"%>
<%@ attribute name="cssClass" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="cssStyle" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="targetId" type="java.lang.String" required="false" description="要显示的文本框的id"%>
<%@ attribute name="targetField" type="java.lang.String" required="false" description="要显示的字段"%>
<%@ attribute name="hideBtn" type="java.lang.Boolean" required="false" description="是否显示按钮"%>
<%@ attribute name="disabled" type="java.lang.String" required="false" description="是否限制选择，如果限制，设置为disabled"%>
<%@ attribute name="dataMsgRequired" type="java.lang.String" required="false" description=""%>
<%@ attribute name="disableClick" type="java.lang.Boolean" required="false" %>
	<input id="${id}Id" name="${name}" class="${cssClass} form-control" type="hidden" value="${value}"/>
	<div class="input-group" style="width:100%">
		<input id="${id}Name" name="${labelName}s" ${allowInput?'':'readonly="readonly"'}  type="text" value="${labelValue}" data-msg-required="${dataMsgRequired}"
		class="${cssClass}" style="${cssStyle}"/>
       		 <span class="input-group-btn">
	       		 <button type="button"  id="${id}Button" class="btn <c:if test="${fn:contains(cssClass, 'input-sm')}"> btn-sm </c:if><c:if test="${fn:contains(cssClass, 'input-lg')}"> btn-lg </c:if>  btn-primary ${disabled} ${hideBtn ? 'hide' : ''}"><i class="fa fa-search"></i>
	             </button> 
	             <c:if test="${allowClear}">
	             	 <button type="button" id="${id}DelButton" class="close" data-dismiss="alert" style="position: absolute; top: 5px; right: 53px; z-index: 999; display: block;">×</button>
	             
	             </c:if>
	             
       		 </span>
       		
    </div>
	<label id="${id}Name-error" class="error" for="${id}Name" style="display:none"></label>
<script type="text/javascript">
$(document).ready(function(){
	$("#${id}Button, #${id}Name").click(function(){
        var disableClick = ${disableClick ? true : false}
        if(disableClick){
            return false;
        }
		// 是否限制选择，如果限制，设置为disabled
		if ($("#${id}Button").hasClass("disabled")){
			return true;
		}
		// 正常打开	
		top.layer.open({
		    type: 2, 
		    area: ['300px', '420px'],
		    title:"选择${title}",
		    ajaxData:{selectIds: $("#${id}Id").val()},
		    content: "${ctx}/tag/treeselect?url="+encodeURIComponent("${url}")+"&module=${module}&checked=${checked}&extId=${extId}&isAll=${isAll}&allowSearch=${allowSearch}" ,
		    btn: ['确定', '关闭']
    	       ,yes: function(index, layero){ //或者使用btn1
						var tree = layero.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
						var ids = [], names = [], nodes = [],targetIds=[];
						if ("${checked}" == "true"){
							nodes = tree.get_checked(true);
						}else{
							nodes = tree.get_selected(true);
						}
						for(var i=0; i<nodes.length; i++) {//<c:if test="${checked && notAllowSelectParent}">
							if (nodes[i].original.isParent){
								continue; // 如果为复选框选择，则过滤掉父节点
							}//</c:if><c:if test="${notAllowSelectRoot}">
							if (nodes[i].original.level == 0){
								top.layer.msg("不能选择根节点（"+nodes[i].text+"）请重新选择。", {icon: 0});
								return false;
							}//</c:if><c:if test="${notAllowSelectParent}">
							if (nodes[i].original.isParent){
								top.layer.msg("不能选择父节点（"+nodes[i].text+"）请重新选择。", {icon: 0});
								return false;
							}//</c:if>
							ids.push(nodes[i].id);
							names.push(nodes[i].text);//<c:if test="${!checked}">
							targetIds.push(nodes[i].original['${targetField}']);console.log(nodes[i])
							break; // 如果为非复选框选择，则返回第一个选择  </c:if>
						}
						$("#${id}Id").val(ids.join(",").replace(/u_/ig,""));
						$("#${id}Name").val(targetIds.join(","));
						$("#${targetId}").val(names.join(","));
						$("#${targetId}").trigger("change");
						$("#${targetField}").val(targetIds.join(","));
						$("#${id}Name").focus();
						top.layer.close(index);
				    	       },
    	cancel: function(index){ //或者使用btn2
    	           //按钮【按钮二】的回调
    	       }
		}); 
	
	});
	
	$("#${id}DelButton").click(function(){
		// 是否限制选择，如果限制，设置为disabled
		if ($("#${id}Button").hasClass("disabled")){
			return true;
		}
		// 清除	
		$("#${id}Id").val("");
		$("#${id}Name").val("");
		$("#${id}Name").focus();
	
	});
})
</script>