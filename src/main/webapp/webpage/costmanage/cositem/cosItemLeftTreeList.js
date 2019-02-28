<%@ page contentType="text/html;charset=UTF-8" %>
	<script>
		$(document).ready(function() {
			var to = false;
			$('#search_q').keyup(function () {
				if(to) { clearTimeout(to); }
				to = setTimeout(function () {
					var v = $('#search_q').val();
					$('#cosItemLeftjsTree').jstree(true).search(v);
				}, 250);
			});
			$('#cosItemLeftjsTree').jstree({
				'core' : {
					"multiple" : false,
					"animation" : 0,
					"themes" : { "variant" : "large", "icons":true , "stripes":true},
					'data' : {
						"url" : "${ctx}/cositem/cosItemLeft/treeData",
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
							"label": "添加下级科目定义",
							"action": function (data) {
								var inst = jQuery.jstree.reference(data.reference),
									obj = inst.get_node(data.reference);
								
								$.get("${ctx}/cositem/cosItemLeft/hasItem?id="+obj.id, function(data){
									if(data.success){
										jp.openDialog('添加下级科目定义', '${ctx}/cositem/cosItemLeft/form?parent.id=' + obj.id + "&parent.name=" + obj.text + "&itemCode=" + autoNumbering(obj.id), '800px', '500px', $('#cosItemLeftjsTree'));
									}else{
										jp.error(data.msg);
									}
								})
								
							}
						};
						tmp.remove = {
							"label": "删除科目定义",
							"action": function (data) {
								var inst = jQuery.jstree.reference(data.reference),
									obj = inst.get_node(data.reference);
								jp.confirm('确认要删除科目定义吗？', function(){
									jp.loading();
									$.get("${ctx}/cositem/cosItemLeft/delete?id="+obj.id, function(data){
										if(data.success){
											$('#cosItemLeftjsTree').jstree("refresh");
											jp.success(data.msg);
										}else{
											jp.error(data.msg);
										}
									})

								});
							}
						}
						tmp.ccp = {
							"label": "编辑科目定义",
							"action": function (data) {
								var inst = jQuery.jstree.reference(data.reference),
									obj = inst.get_node(data.reference);
								var parentId = inst.get_parent(data.reference);
								var parent = inst.get_node(parentId);
								
								jp.openDialog('编辑科目定义', '${ctx}/cositem/cosItemLeft/form?id=' + obj.id, '800px', '500px', $('#cosItemLeftjsTree'));
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
				var node = $('#cosItemLeftjsTree').jstree(true).get_selected(true)[0];
				var opt = {
					silent: true,
					query:{
						'fatherId.id':node.id
					}
				};
				$("#fatherIdId").val(node.id);
				$("#fatherIdName").val(node.text);
				$('#cosItemTable').bootstrapTable('refresh',opt);
			}).on('loaded.jstree', function() {
				$("#cosItemLeftjsTree").jstree('open_all');
			});
		});
		
		function autoNumbering(id){
			var itemcode = "";
			$.ajax({
				url:"${ctx}/cositem/cosItem/autoNumbering?id="+id,
				type: "GET",
				cache: false,
				dataType: "text",
				async:false,
				success:function(data){
					itemcode = data;
				}
			});
			return itemcode;
		}
	</script>