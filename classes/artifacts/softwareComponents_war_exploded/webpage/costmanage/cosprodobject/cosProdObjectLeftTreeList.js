<%@ page contentType="text/html;charset=UTF-8" %>
	<script>
		$(document).ready(function() {
			var to = false;
			$('#search_q').keyup(function () {
				if(to) { clearTimeout(to); }
				to = setTimeout(function () {
					var v = $('#search_q').val();
					$('#cosProdObjectLeftjsTree').jstree(true).search(v);
				}, 250);
			});
			$('#cosProdObjectLeftjsTree').jstree({
				'core' : {
					"multiple" : false,
					"animation" : 0,
					"themes" : { "variant" : "large", "icons":true , "stripes":true},
					'data' : {
						"url" : "${ctx}/cosprodobject/cosProdObjectLeft/treeData",
						"dataType" : "json" 
					}
				},
				"conditionalselect" : function (node, event) {
					return false;
				},
				'plugins': ["contextmenu", 'types', 'wholerow', "search"],
				"contextmenu": {
					"items": function (node) {
						var tmp = $.jstree.defaults.contextmenu.items();
						delete tmp.create.action;
						delete tmp.rename.action;
						tmp.rename = null;
						tmp.create = {
							"label": "添加下级核算对象定义",
							"action": function (data) {
								var inst = jQuery.jstree.reference(data.reference),
									obj = inst.get_node(data.reference);
								
								$.get("${ctx}/cosprodobject/cosProdObjectLeft/hasItem?id="+obj.id, function(data){
									if(data.success){
										jp.openDialog('添加下级核算对象定义', '${ctx}/cosprodobject/cosProdObjectLeft/form?parent.id=' + obj.id + "&parent.name=" + obj.text, '800px', '500px', $('#cosProdObjectLeftjsTree'));
									}else{
										jp.error(data.msg);
									}
								})
								
							}
						};
						tmp.remove = {
							"label": "删除核算对象定义",
							"action": function (data) {
								var inst = jQuery.jstree.reference(data.reference),
									obj = inst.get_node(data.reference);
								jp.confirm('确认要删除核算对象定义吗？', function(){
									jp.loading();
									$.get("${ctx}/cosprodobject/cosProdObjectLeft/delete?id="+obj.id, function(data){
										if(data.success){
											$('#cosProdObjectLeftjsTree').jstree("refresh");
											jp.success(data.msg);
										}else{
											jp.error(data.msg);
										}
									})

								});
							}
						}
						tmp.ccp = {
							"label": "编辑核算对象定义",
							"action": function (data) {
								var inst = jQuery.jstree.reference(data.reference),
									obj = inst.get_node(data.reference);
								var parentId = inst.get_parent(data.reference);
								var parent = inst.get_node(parentId);
								jp.openDialog('编辑核算对象定义', '${ctx}/cosprodobject/cosProdObjectLeft/form?id=' + obj.id, '800px', '500px', $('#cosProdObjectLeftjsTree'));
							}
						}
						return tmp;
					}

				},
				"types":{
					'default' : { 'icon' : 'fa fa-file-text-o' },
					'1' : {'icon' : 'fa fa-home'},
					'2' : {'icon' : 'fa fa-umbrella' },
					'3' : { 'icon' : 'fa fa-group'},
					'4' : { 'icon' : 'fa fa-file-text-o' }
				}

			}).bind("activate_node.jstree", function (obj, e) {
				var node = $('#cosProdObjectLeftjsTree').jstree(true).get_selected(true)[0];
				var opt = {
					silent: true,
					query:{
						'fatherId.id':node.id
					}
				};
				$("#fatherIdId").val(node.id);
				$("#fatherIdName").val(node.text);
				$('#cosProdObjectTable').bootstrapTable('refresh',opt);
			}).on('loaded.jstree', function() {
				$("#cosProdObjectLeftjsTree").jstree('open_all');
			});
		});
	</script>