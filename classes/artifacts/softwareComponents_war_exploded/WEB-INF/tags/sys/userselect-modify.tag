<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="编号"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="隐藏域名称（ID）"%>
<%@ attribute name="value" type="java.lang.String" required="false" description="隐藏域值（ID）"%>
<%@ attribute name="labelName" type="java.lang.String" required="false" description="输入框名称（Name）"%>
<%@ attribute name="labelValue" type="java.lang.String" required="false" description="输入框值（Name）"%>
<%@ attribute name="isMultiSelected" type="java.lang.Boolean" required="false" description="是否允许多选"%>
<%@ attribute name="cssClass" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="cssStyle" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="hideBtn" type="java.lang.Boolean" required="false" description="是否显示按钮"%>
<%@ attribute name="disabled" type="java.lang.String" required="false" description="是否限制选择，如果限制，设置为disabled"%>
<%@ attribute name="dataMsgRequired" type="java.lang.String" required="false" description=""%>
<%@ attribute name="targetId" type="java.lang.String" required="true" description="额外赋值目标Id"%>
<%@ attribute name="disableClick" type="java.lang.Boolean" required="false" %>
<%@ attribute name="extraId" type="java.lang.String" required="false" %>

	<input id="${id}Id" name="${name}" class="${cssClass} form-control" type="hidden" value="${value}"/>
	<div class="input-group" style="width:100%">
		<input id="${id}Names" name="${labelName}s" ${allowInput?'':'readonly="readonly"'}  type="text"  value="${labelValue}" data-msg-required="${dataMsgRequired}"
		class="${cssClass}" style="${cssStyle}"/>
       		 <span class="input-group-btn">
	              <button type="button"  id="${id}Button" class="btn <c:if test="${fn:contains(cssClass, 'input-sm')}"> btn-sm </c:if><c:if test="${fn:contains(cssClass, 'input-lg')}"> btn-lg </c:if>  btn-primary ${disabled} ${hideBtn ? 'hide' : ''}"><i class="fa fa-search"></i>
	              </button>
	             <button type="button" id="${id}DelButton" class="close" data-dismiss="alert" style="position: absolute; top: 5px; right: 53px; z-index: 999; display: block;">×</button>
       		 </span>
       		
    </div>
	 <label id="${id}Names-error" class="error" for="${id}Names" style="display:none"></label>
<script type="text/javascript">
	$("#${id}Button, #${id}Names").click(function(){
        var disableClick = ${disableClick ? true : false}
        if(disableClick){
            return false;
        }
		// 是否限制选择，如果限制，设置为disabled
		if ($("#${id}Button").hasClass("disabled")){
			return true;
		}
		// 正常打开	
		
		jp.openUserSelectDialog(${isMultiSelected? true:false},function(ids, names,nos){
			$("#${id}Id").val(ids.replace(/u_/ig,""));
			$("#${id}Names").val(nos);
			$("#${targetId}").val(names);
			$("#${id}Names").focus();
			$("#${extraId}").val(nos);
		})
	
	});
	
	$("#${id}DelButton").click(function(){
		// 是否限制选择，如果限制，设置为disabled
		if ($("#${id}Button").hasClass("disabled")){
			return true;
		}
		// 清除	
		$("#${id}Id").val("");
		$("#${id}Names").val("");
		$("#${id}Names").focus();
	
	});
</script>