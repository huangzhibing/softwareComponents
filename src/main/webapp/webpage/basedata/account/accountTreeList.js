<%@ page contentType="text/html;charset=UTF-8" %>
	<script>
		$(document).ready(function() {
			var to = false;
			var hrefAttr = $('#add').attr('href');
			//记录add默认访问路径
			$('#search_q').keyup(function () {
				if(to) { clearTimeout(to); }
				to = setTimeout(function () {
					var v = $('#search_q').val();
					$('#accTypeJsTree').jstree(true).search(v);
				}, 250);
			});
			$('#accTypeJsTree').jstree({
				'core' : {
					"multiple" : false,
					"animation" : 0,
					"themes" : { "variant" : "large", "icons":true , "stripes":true},
					'data' : {
						"url" : "${ctx}/accounttype/accountType/treeData",
						"dataType" : "json" 
					}
				},
				"conditionalselect" : function (node, event) {
					return false;
				},
				'plugins': [ 'types', 'wholerow', "search"],
				"types":{
					'default' : { 'icon' : 'fa fa-file-text-o' },
					'1' : {'icon' : 'fa fa-home'},
					'2' : {'icon' : 'fa fa-umbrella' },
					'3' : { 'icon' : 'fa fa-group'},
					'4' : { 'icon' : 'fa fa-file-text-o' }
				}

			}).bind("activate_node.jstree", function (obj, e) {
				var node = $('#accTypeJsTree').jstree(true).get_selected(true)[0];
				if(node.text === '关系企业类型'){
                    $("#accTypeCode").val("");
                    $("#accTypeNameRu").val("");
                    $('#accountTable').bootstrapTable('refresh');
                    $('#add').attr('href',hrefAttr);
                    return false;
                }
				//设置查询条件
                var code = node.text.substring(0,2);
                var name = node.text.substring(2);
				var opt = {
					silent: true,
					query:{
						// 'accType.id':node.id,
						'accTypeNameRu':name
					}
				};
				//设置页面查询元素
				$("#accTypeCode").val(code);
				$("#accTypeNameRu").val(name);
				$('#accountTable').bootstrapTable('refresh',opt);
				$('#add').attr('href',hrefAttr+'?accTypeCode='+node.id+'&accTypeName='+node.text);
				//拼装add路径
			}).on('loaded.jstree', function() {
				$("#accTypeJsTree").jstree('open_all');
			});
		});
	</script>