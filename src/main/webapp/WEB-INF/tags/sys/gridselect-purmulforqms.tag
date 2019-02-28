<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="编号"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="隐藏域名称（ID）"%>
<%@ attribute name="value" type="java.lang.String" required="true" description="隐藏域值（ID）"%>
<%@ attribute name="labelName" type="java.lang.String" required="true" description="输入框名称（Name）"%>
<%@ attribute name="labelValue" type="java.lang.String" required="true" description="输入框值（Name）"%>
<%@ attribute name="fieldLabels" type="java.lang.String" required="true" description="表格Th里显示的名字"%>
<%@ attribute name="fieldKeys" type="java.lang.String" required="true" description="表格Td里显示的值"%>
<%@ attribute name="searchLabels" type="java.lang.String" required="true" description="表格Td里显示的值"%>
<%@ attribute name="searchKeys" type="java.lang.String" required="true" description="表格Td里显示的值"%>
<%@ attribute name="targetId" type="java.lang.String" required="true" description="额外赋值目标Id"%>
<%@ attribute name="targetField" type="java.lang.String" required="true" description="额外赋值字段"%>
<%@ attribute name="extraField" type="java.lang.String" required="false" description="额外赋值字段"%>
<%@ attribute name="title" type="java.lang.String" required="true" description="选择框标题"%>
<%@ attribute name="url" type="java.lang.String" required="true" description="数据地址"%>
<%@ attribute name="cssClass" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="isMultiSelected" type="java.lang.Boolean" required="false" description="css样式"%>
<%@ attribute name="cssStyle" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="disabled" type="java.lang.String" required="false" description="是否限制选择，如果限制，设置为disabled"%>
	<input id="${id}Id" name="${name}" type=hidden  value="${value}"/>
	<div class="input-group" style="width: 100%">
		<input id="${id}Names"  name="${labelName}s" ${allowInput?'':'readonly="readonly"'}  type="text" value="${labelValue}" data-msg-required="${dataMsgRequired}"
		class="${cssClass}" style="${cssStyle}"/>
       		 <span class="input-group-btn">
	       		 <button type="button"  id="${id}Button" class="btn <c:if test="${fn:contains(cssClass, 'input-sm')}"> btn-sm </c:if><c:if test="${fn:contains(cssClass, 'input-lg')}"> btn-lg </c:if>  btn-primary ${disabled} ${hideBtn ? 'hide' : ''}"><i class="fa fa-search"></i>
	             </button> 
	               <button type="button" id="${id}DelButton" class="close" data-dismiss="alert" style="position: absolute; top: 5px; right: 53px; z-index: 999; display: block;">×</button>
       		 </span>      		
    </div>
	 <label id="${id}Name-error" class="error" for="${id}Names" style="display:none"></label>
<script type="text/javascript">
$(document).ready(function(){
	$("#${id}Button, #${id}Names").click(function(){
	    //alert(${disabled});
		if ($("#${id}Button").hasClass("disabled")){
			return true;
		}
		
		
		
		
		
		top.layer.open({
		    type: 2,  
		    area: ['800px', '500px'],
		    title:"${title}",
		    auto:true,
		    name:'friend',
		    content: "${ctx}/tag/gridselect?url="+encodeURIComponent("${url}")+"&fieldLabels="+encodeURIComponent("${fieldLabels}")+"&fieldKeys="+encodeURIComponent("${fieldKeys}")+"&searchLabels="+encodeURIComponent("${searchLabels}")+"&searchKeys="+encodeURIComponent("${searchKeys}")+"&isMultiSelected=${isMultiSelected? true:false}",
		    btn: ['确定', '关闭'],
		    yes: function(index, layero){
		    	 var iframeWin = layero.find('iframe')[0].contentWindow; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
		    	 var items = iframeWin.getSelections();
		    	 if(items == ""){
			    	jp.warning("必须选择一条数据!");
			    	return;
		    	 }
                var extra = '${extraField}';
                var fieldArr;
                if (extra) {
                    fieldArr = extra.split(';');
                }
		    	 var ids = [];
		    	 var names = [];
		    	 var targetValue = [];
		    	 for(var i=0; i<items.length; i++){
		    		 var item = items[i];
		    	//	 console.log(item)
		    		 ids.push(item.id);
		    		 names.push(item${fn:substring(labelName, fns:lastIndexOf(labelName, '.'), fn:length(labelName))})
                     targetValue.push(item['${targetField}'])
                     if (fieldArr) {
                         for(var j = 0;j<fieldArr.length;j++) {
                             $('#' + fieldArr[j].substring(0, fieldArr[j].indexOf(':'))).val(item[fieldArr[j].substring(fieldArr[j].indexOf(':')+1)])

                         }
                     }
		    	 }
		    //获取name的值,得到gridselectr标签隐藏input的对象的属性
		     var name='${name}'.split(".");
		     var nam=name[name.length-1];
		    //获取lableName的值，得到gridselectr标签显示input的对象的属性
		     var labelName='${labelName}'.split(".");	 
		     var labNam=labelName[labelName.length-1]; 
		    	 
			  // 设置gridselectr标签显示input的值
		       $("#${id}Names").val(items[0][labNam]);
		       //设置到货数量为空（触发质检消息的显示）
		       var string = "${id}".split("_");
		       var checkqty = string[0]+"_checkQty"
		       $("#"+checkqty).val("");
		       		    
		       $("#${id}Id").trigger("change");
		    // 设置gridselectr标签隐藏input的值
		       $("#${id}Id").val(items[0][nam]);		       		  
		       //获取填充标签的id
		       var  targetId="${targetId}".split("|");
		       //获取填充标签的name
		       var target='${targetField}';
		       var targetField=target.split("|");  
		       var objs='#${id}';
               var obj=objs.split("_")[1];		     
		       var ht=objs.split("_")[0];
		       //填充第一条选项的值
		       for(var k=0;k<targetId.length;k++){	    	   
		    	      var tar=targetField[k];
			    	  $(ht+"_"+targetId[k]).val(items[0][tar]); 
			    	  if(tar=="code"){
			    	  	var itemCode = items[0][tar];
			    	  	findQmsFlag(itemCode,ht);
			    	  }
						
			      }
		       
                //填充除去第一个选项的值，第0行数据传给当前所点击的行，其余的赋值给添加行，所以遍历添加行时从1行添加开始  
               if(items.length>1) {
            	   setOtherValue(items,obj,targetField,targetId,nam,labNam);
               }              
				 top.layer.close(index);//关闭对话框。
			  },
			  cancel: function(index){ 
				  top.layer.close(index);//关闭对话框。
		       }
		}); 
	})
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
})
</script>
