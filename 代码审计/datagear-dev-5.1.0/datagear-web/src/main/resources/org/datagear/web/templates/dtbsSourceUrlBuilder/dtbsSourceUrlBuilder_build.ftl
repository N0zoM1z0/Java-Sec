<#--
 *
 * Copyright 2018-present datagear.tech
 *
 * This file is part of DataGear.
 *
 * DataGear is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * DataGear is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with DataGear.
 * If not, see <https://www.gnu.org/licenses/>.
 *
-->
<#include "../include/page_import.ftl">
<#include "../include/html_doctype.ftl">
<html>
<head>
<#include "../include/html_head.ftl">
<title>
	<@spring.message code='module.dtbsSourceUrlBuilder' />
	<#include "../include/html_request_action_suffix.ftl">
	<#include "../include/html_app_name_suffix.ftl">
</title>
</head>
<body class="p-card no-border">
<#include "../include/page_obj.ftl">
<div id="${pid}" class="page page-form horizontal">
	<div class="customScriptCode hidden">
		${scriptCode!''}
	</div>
	<div class="builtInJson hidden">
		${builtInBuildersJson!''}
	</div>
	<form id="${pid}form" class="flex flex-column" :class="{readonly: pm.isReadonlyAction}">
		<div class="page-form-content flex-grow-1 px-2 py-1 overflow-y-auto">
			<div class="field grid">
				<label for="${pid}dbType" class="field-label col-12 mb-2 md:col-3 md:mb-0">
					<@spring.message code='dbType' />
				</label>
		        <div class="field-input col-12 md:col-9">
		        	<p-dropdown v-model="fm.dbType" :options="pm.dbTypeDropdownItems" option-label="dbType" option-value="dbType"
		        		option-group-label="label" option-group-children="items" @change="onDbTypeChange" class="input w-full">
		        	</p-dropdown>
		        	<div class="validate-msg">
		        		<input name="dbType" required type="text" class="validate-proxy" />
		        	</div>
		        </div>
			</div>
			<div class="field grid">
				<label for="${pid}hostName" class="field-label col-12 mb-2 md:col-3 md:mb-0">
					<@spring.message code='hostNameOrIp' />
				</label>
		        <div class="field-input col-12 md:col-9">
		        	<p-inputtext id="${pid}hostName" v-model="fm.hostName" type="text" class="input w-full"
		        		name="hostName" maxlength="200">
		        	</p-inputtext>
		        	<!-- 上面不能使用：name="host"，会导致打开下拉框时死循环报错 -->
		        </div>
			</div>
			<div class="field grid">
				<label for="${pid}port" class="field-label col-12 mb-2 md:col-3 md:mb-0">
					<@spring.message code='port' />
				</label>
		        <div class="field-input col-12 md:col-9">
		        	<p-inputtext id="${pid}port" v-model="fm.port" type="text" class="input w-full"
		        		name="port" maxlength="20">
		        	</p-inputtext>
		        </div>
			</div>
			<div class="field grid">
				<label for="${pid}name" class="field-label col-12 mb-2 md:col-3 md:mb-0">
					<@spring.message code='dtbsSourceName' />
				</label>
		        <div class="field-input col-12 md:col-9">
		        	<p-inputtext id="${pid}name" v-model="fm.name" type="text" class="input w-full"
		        		name="name" maxlength="500">
		        	</p-inputtext>
		        </div>
			</div>
		</div>
		<div class="page-form-foot flex-grow-0 flex justify-content-center gap-2 pt-2">
			<p-button type="submit" label="<@spring.message code='confirm' />"></p-button>
		</div>
		<div class="page-form-foot flex-grow-0 flex justify-content-center gap-2 pt-2" v-if="pm.isPreviewAction">
			<p-inputtext id="${pid}previewResult" v-model="fm.previewResult" type="text" class="input w-8 text-center"
        		placeholder="<@spring.message code='previewResult' />" name="previewResult" maxlength="2000">
        	</p-inputtext>
		</div>
	</form>
</div>
<#include "../include/page_form.ftl">
<script>
(function(po)
{
	po.isPreviewAction = (po.action == "preview");
	po.initUrl = "${(url!'')?js_string?no_esc}";
	
	$.dtbsSourceUrlBuilder.clear();
	
	var topBuilders = [];
	var allBuilders = [];
	
	try
	{
		var scriptCode = po.element(".customScriptCode").text();
		var builtInBuildersJson = po.element(".builtInJson").text();
		
		if(builtInBuildersJson)
			allBuilders = eval("$.dtbsSourceUrlBuilder.add(" + builtInBuildersJson+");");
		
		if(scriptCode)
			topBuilders = eval("$.dtbsSourceUrlBuilder.add(" + scriptCode+");");
	}
	catch(e){}
	
	if(topBuilders.length <= 0)
		topBuilders = (allBuilders.length <= 3 ? allBuilders : allBuilders.slice(0, 3));
	
	$.dtbsSourceUrlBuilder.sortByDbType(topBuilders);
	$.dtbsSourceUrlBuilder.sortByDbType(allBuilders);
	
	var urlDbType = null;
	var urlValue = null;
	
	if(po.initUrl)
	{
		var urlInfo = $.dtbsSourceUrlBuilder.extract(po.initUrl);
		if(urlInfo != null)
		{
			urlDbType = urlInfo.dbType;
			urlValue = urlInfo.value;
		}
	}
	
	if(!urlValue)
		urlValue = $.dtbsSourceUrlBuilder.defaultValue(urlDbType);
	
	po.submitForm = function()
	{
		var fm = po.vueFormModel();
		var value = { host: fm.hostName, port: fm.port, name: fm.name };
		var url = $.dtbsSourceUrlBuilder.build(fm.dbType, value);
		
		if(po.isPreviewAction)
			fm.previewResult = url;
		po.defaultSubmitSuccessCallback({ data: url }, !po.isPreviewAction);
	};
	
	var formModel =
	{
		dbType: urlDbType,
		hostName: (urlValue && urlValue.host ? urlValue.host : ""),
		port: (urlValue && urlValue.port ? urlValue.port : ""),
		name: (urlValue && urlValue.name ? urlValue.name : ""),
		previewResult: " "
	};
	po.setupForm(formModel);
	
	po.vuePageModel(
	{
		isPreviewAction: po.isPreviewAction,
		dbTypeDropdownItems:
		[
			{
				label: "<@spring.message code='common' />",
				items: topBuilders
			},
			{
				label: "<@spring.message code='all' />",
				items: allBuilders
			}
		]
	});
	
	po.vueMethod(
	{
		onDbTypeChange: function(e)
		{
			var dbType = e.value;
			var dftValue = $.dtbsSourceUrlBuilder.defaultValue(dbType);
			
			var fm = po.vueFormModel();
			if(!fm.hostName)
				fm.hostName = (dftValue.host || "");
			if(!fm.port)
				fm.port = (dftValue.port || "");
			if(!fm.name)
				fm.name = (dftValue.name || "");
		}
	});
})
(${pid});
</script>
<#include "../include/page_vue_mount.ftl">
</body>
</html>