<%@ page contentType="text/html;charset=UTF-8" %>
    <script>
$(document).ready(function() {
    var to = false;
    var hrefAttr = $('#add').attr('href');
    $('#search_q').keyup(function () {
        if(to) { clearTimeout(to); }
        to = setTimeout(function () {
            var v = $('#search_q').val();
            $('#employeeJsTree').jstree(true).search(v);
        }, 250);
    });
    $('#employeeJsTree').jstree({
        'core' : {
            "multiple" : false,
            "animation" : 0,
            "themes" : { "variant" : "large", "icons":true , "stripes":true},
            'data' : {
                "url" : "${ctx}/employee/employee/treeData",
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
        var node = $('#employeeJsTree').jstree(true).get_selected(true)[0];
        console.log(node);
        //设置查询条件
        var opt = {
            silent: true,
            query:{
            }
        };
        // console.log(node.id.length)
        var add = $('#add')
        var del = $('#remove')
        if(node.id.length === 4 && node.text !== '总结点'){
            add.attr("disabled",false);
            add.attr('href',hrefAttr+'?type=ware&typeid='+node.id);
            del.attr('typeid',node.id)
            del.attr('type',"ware")
            var opt = {
                silent: true,
                query:{
                    'type':'ware',
                    'typeid':node.id
                }
            };

            $('#employeeTable').bootstrapTable('refresh',opt);
        } else if (node.id.length === 6){
            add.attr("disabled",false);

            add.attr('href',hrefAttr+'?type=bin&typeid='+node.id);
            del.attr('typeid',node.id)
            del.attr('type',"bin")
            var opt = {
                silent: true,
                query:{
                    'type':'bin',
                    'typeid':node.id
                }
            };
            $('#employeeTable').bootstrapTable('refresh',opt);
        } else if (node.id.length === 8){
            add.attr("disabled",false);
            del.attr('typeid',node.id)
            add.attr('href',hrefAttr+'?type=loc&typeid='+node.id);
            del.attr('type',"loc")
            var opt = {
                silent: true,
                query:{
                    'type':'loc',
                    'typeid':node.id
                }
            };

            $('#employeeTable').bootstrapTable('refresh',opt);
        } else {
             add.attr("disabled",true);
            var opt = {
                silent: true,
                query:{
                }
            };

            $('#employeeTable').bootstrapTable('refresh',opt);

        }
    }).on('loaded.jstree', function() {
        $("#employeeJsTree").jstree('close_all');
    });
});
</script>