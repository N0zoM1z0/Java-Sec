/**
 * @Description: 通用框架
 * @Copyright: 2017 www.fallsea.com Inc. All rights reserved.
 * @author: fallsea
 * @version 1.6.3
 * @License：MIT
 */
/**
 * 去掉tree 默认使用zTree
 * @author: OF
 */
layui.use(['admin.form','admin.datagrid','admin.common','element'], function(){
	var adminForm = layui['admin.form'],
	adminDatagrid = layui['admin.datagrid'],
	element = layui.element;
	adminCommon = layui['admin.common'];
	/********* form 表单处理   start *************/
	var formDoms =$("form");
	if(formDoms.length>0){//如果没有查到form，自动关闭
    $(formDoms).each(function (index, domEle) { 
    	adminForm.render({elem:$(this)});
    });
	}
	/********* form 表单处理   end *************/

	/********* datagrid 处理   start *************/
  var tabs = $("table.adminDatagrid");
  var datagrids= {};//datagrid集合
  if(tabs.length > 0){
    $(tabs).each(function(){
      var tableId=$(this).attr("id");
      if(!$.isEmpty(datagrids[tableId])){
      	return;
      }
      var clickRenderTable = $(this).attr("clickRenderTable");//点击需要渲染的tableid
      var clickCallBack;//点击事件
  	  if(!$.isEmpty(clickRenderTable)){
  	  	var defaultForm= $("#"+clickRenderTable).attr("defaultForm");//默认form表单id
  	  	var clickRenderTableInputs = $(this).attr("clickRenderTableInputs");//点击需要传入的参数信息
  	  	clickCallBack = function(data){
  	  		//获取参数
  	  		var formData = adminCommon.getParamByInputs(clickRenderTableInputs,data);
  	  		//点击后，为查询form表单赋值
  	  		if(!$.isEmpty(defaultForm)){
  	  			$("#"+defaultForm).setFormData(formData);
  	  		}
	  			datagrids[clickRenderTable].reload(formData);
  	  	}
  	  }
          
      var datagrid = adminDatagrid.render({id:tableId,clickCallBack:clickCallBack});
      datagrid.bindDatagridTool(getDatagrid);
      if(tabs.length==1){
      	datagrids[tableId] = datagrid;
      }else{
      	//深度拷贝对象
      	datagrids[tableId] = $.extend(true,{},datagrid);
      }
    });
    adminCommon.buttionEvent("datagrid",getDatagrid);
  }else{
    //按钮绑定
  	adminCommon.buttionEvent("datagrid");
  }
  
  function getDatagrid(tableId){
    if($.isEmpty(tableId)){
      tableId = "adminDatagrid";
    }
    if($.isEmpty(datagrids)){
    	adminCommon.warnMsg("未配置datagrid！");
      return;
    }
    return datagrids[tableId];
  }
  /********* datagrid 处理   end *************/
	
});