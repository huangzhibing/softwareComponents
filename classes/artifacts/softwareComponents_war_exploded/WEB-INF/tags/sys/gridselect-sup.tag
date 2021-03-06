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
		<input id="${id}Names"  name="${labelName}" ${allowInput?'':'readonly="readonly"'}   type="text" value="${labelValue}" data-msg-required="${dataMsgRequired}"
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
		    	 /*
		    	 for(var i=0; i<items.length; i++){
		    		 var item = items[i];
		    		 console.log(item)
		    		 ids.push(item.id);
		    		 names.push(item${fn:substring(labelName, fns:lastIndexOf(labelName, '.'), fn:length(labelName))});
                     targetValue.push(item['${targetField}'])
                     if (fieldArr) {
                         for(var j = 0;j<fieldArr.length;j++) {
                             $('#' + fieldArr[j].substring(0, fieldArr[j].indexOf(':'))).val(item[fieldArr[j].substring(fieldArr[j].indexOf(':')+1)])

                         }
                     }
		    	 }*/
			//  console.log(items);
			 //获取显示标签所对应对象的属性名的
			  var labelName="${labelName}".split(".");			  
			 if(labelName.length==1){
				 
				 $("#${id}Names").val(items[0][labelName]);
				 $("#${id}Names").trigger("change");

				 var  haddenName="${name}".split(".");
			       //设置隐藏input标签的值    
			       $("#${id}Id").val(items[0][haddenName]);
			       $("#${id}Id").trigger("change");

			 }
			 if(labelName.length==2){
				 var   labName=labelName[labelName.length-1];
				 //获取隐藏标签所对应对象的属性名的
				 var  haddenName="${name}".split(".");
				 var   hadName=haddenName[haddenName.length-1];
				
 
				 //先填充本gridselect标签input显示标签的值	
			       $("#${id}Names").val(items[0][labName]);
			       $("#${id}Names").trigger("change");
			       //设置隐藏input标签的值    			      
			       $("#${id}Id").val(items[0][hadName]);
			       $("#${id}Id").trigger("change");
			}
			 if(labelName.length==3){
				 var   labName1=labelName[1];
				 var labName2=labelName[2]
				 //获取隐藏标签所对应对象的属性名的
				 var  haddenName="${name}".split(".");
				 var   hadName1=haddenName[1];
			//	 var   hadName2=haddenName[2];
				  //先填充本gridselect标签input显示标签的值	
			       $("#${id}Names").val(items[0][labName1][labName2]);
			       $("#${id}Names").trigger("change");
			       //设置隐藏input标签的值    
			  //     $("#${id}Id").val(items[0][hadName1][hadName2]);
			       $("#${id}Id").val(items[0][hadName1]);
			       $("#${id}Id").trigger("change");

			 }
			
			  
			   //获取填充标签的id
		       var  targetId="${targetId}".split("|");
		       //获取填充标签的name
		       var target='${targetField}';
		       var targetField=target.split("|");  
       
		       //填充其他关联标签的值{targetId中填写需关联标签的id,targetField关联的属性值，items表示所选项的值（此处为单选）
               addValue(items,targetField,targetId);
               //依据采购员筛选采购计划
               var buyerId=items[0][hadName1];
             //  selectPlan(buyerId);
			   top.layer.close(index);//关闭对话框。
			},
			  cancel: function(index){ 
				  top.layer.close(index);//关闭对话框。
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
		$("#${id}Names").val("");
		$("#${id}Names").focus();

	});
	
	function addValue(items,targetField,targetId){				
			for(var i=0;i<targetField.length;i++){
				var ind=targetField[i];
				var tId=targetId[i];
				// alert(tId);
			    // alert(items[0][ind]);
				$("#"+tId).val(items[0][ind]);
			}
									
		}
})
</script>
