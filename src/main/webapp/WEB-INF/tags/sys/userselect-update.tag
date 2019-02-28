<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="编号"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="隐藏域名称（ID）"%>
<%@ attribute name="value" type="java.lang.String" required="false" description="隐藏域值（ID）"%>
<%@ attribute name="labelName" type="java.lang.String" required="fasle" description="输入框名称（Name）"%>
<%@ attribute name="labelValue" type="java.lang.String" required="false" description="输入框值（Name）"%>
<%@ attribute name="isMultiSelected" type="java.lang.Boolean" required="false" description="是否允许多选"%>
<%@ attribute name="cssClass" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="cssStyle" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="hideBtn" type="java.lang.Boolean" required="false" description="是否显示按钮"%>
<%@ attribute name="disabled" type="java.lang.String" required="false" description="是否限制选择，如果限制，设置为disabled"%>
<%@ attribute name="dataMsgRequired" type="java.lang.String" required="false" description=""%>
	<input id="${id}Id" name="${name}" class="${cssClass} form-control" type="hidden" value="${value}" />
	<div class="input-group" style="width:100%">
		<input id="${id}Name" name="${labelName}" ${allowInput?'':'readonly="readonly"'}  type="text"  value="${labelValue}" data-msg-required="${dataMsgRequired}"
		class="${cssClass}" style="${cssStyle}" />
       		 <span class="input-group-btn">
	              <button type="button"  id="${id}Button" class="btn <c:if test="${fn:contains(cssClass, 'input-sm')}"> btn-sm </c:if><c:if test="${fn:contains(cssClass, 'input-lg')}"> btn-lg </c:if>  btn-primary ${disabled} ${hideBtn ? 'hide' : ''}"><i class="fa fa-search"></i>
	              </button>
	             <button type="button" id="${id}DelButton" class="close" data-dismiss="alert" style="position: absolute; top: 5px; right: 53px; z-index: 999; display: block;">×</button>
       		 </span>      		
    </div>
	<label id="${id}Name-error" class="error" for="${id}Name" style="display:none"></label>
<script type="text/javascript">
	$("#${id}Button, #${id}Name").click(function(){
		// 是否限制选择，如果限制，设置为disabled
		if ($("#${id}Button").hasClass("disabled")){
			return true;
		}
	 // 正常打开
	 /**用户选择框**/
	  /**	
		jp.openUserSelectDialog(${isMultiSelected? true:false},function(ids, names){
			$("#${id}Id").val(ids.replace(/u_/ig,""));	
			$("#${id}Name").val(names);
			$("#${id}Name").focus();
		});
	 **/ 		  		
			   top.layer.open({
				    type: 2, 
				    area: ['900px', '560px'],
				    title:"选择用户",
				    auto:true,
			        maxmin: true, //开启最大化最小化按钮
				   content: ctx+"/sys/user/userSelect?isMultiSelect="+false,			 
				    btn: ['确定', '关闭'],
				    yes: function(index, layero){
				    	var ids = layero.find("iframe")[0].contentWindow.getIdSelections();				    	
				    	var names = layero.find("iframe")[0].contentWindow.getNameSelections();	
				    	//获取所有的数据(数组的形式)，这里为单选
				    	var users = layero.find("iframe")[0].contentWindow.getSelections();	
						if(ids.length ==0){
						      jp.warning("请选择至少一个用户!");
							return;
						}
				    	// 获取所选的用户id
				    	var ids=ids.join(",");
				    	//获取所选的用户名
				    	var names= names.join(",");				    	
				       // $("#${id}Id").val(names);				    	
				       // $("#${id}Name").attr("name",names);
				       $("#${id}Id").val(ids.replace(/u_/ig,""));	
				        //设置为用户的编号
				     	$("#${id}Name").val(users[0].no);
				    	//依据采购人员的编码设置采购人员的用户名
				     	setUsername("${id}",names);	
				    	
				        $("#${id}Name").focus();
				        
				    	top.layer.close(index);
					  },
					  cancel: function(index){ 
						  //取消默认为空，如需要请自行扩展。
						  top.layer.close(index);						  
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
		
	
</script>