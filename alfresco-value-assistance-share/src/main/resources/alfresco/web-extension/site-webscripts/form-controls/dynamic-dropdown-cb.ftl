<#include "/org/alfresco/components/form/controls/common/utils.inc.ftl" />

<#if field.control.params.optionSeparator??>
<#assign optionSeparator=field.control.params.optionSeparator>
<#else>
<#assign optionSeparator=",">
</#if>
<#if field.control.params.labelSeparator??>
<#assign labelSeparator=field.control.params.labelSeparator>
<#else>
<#assign labelSeparator="|">
</#if>

<#assign fieldValue=field.value>

<#if fieldValue?string == "" && field.control.params.defaultValueContextProperty??>
<#if context.properties[field.control.params.defaultValueContextProperty]??>
   <#assign fieldValue = context.properties[field.control.params.defaultValueContextProperty]>
<#elseif args[field.control.params.defaultValueContextProperty]??>
   <#assign fieldValue = args[field.control.params.defaultValueContextProperty]>

</#if>
</#if>

<script type="text/javascript">//<![CDATA[
(function(TSG)
{
	new TSG.DynamicDropdown("${fieldHtmlId}").setOptions(
	{
		picklistName:"${field.control.params['picklistName']}",
		dependsOn:[
			<#if (field.control.params['dependsOn'])??>
				<#list (field.control.params['dependsOn'])?split(",") as dependsOn>
					"${dependsOn}"<#if dependsOn_has_next>,</#if>
				</#list>
			</#if>
		],
		initialValue:"${fieldValue}",
		itemId:"${(form.arguments.itemId!"")?js_string}",
		level:"${field.control.params['level']!"1"}",
		formId: "${(form.arguments.formId!"")?js_string}",
		<#if field.control.params.onlyEnabledValues?? && field.control.params.onlyEnabledValues == "false">
		   onlyEnabledValues: "false",
		<#else>
		   onlyEnabledValues: "true",
		</#if>
		<#if field.control.params.includeBlankItem?? && field.control.params.includeBlankItem == "false">
			includeBlankItem: "false"
        <#else>
        	includeBlankItem: "true"
  		</#if>
	});

})(window.TSG = window.TSG || {});
//]]></script>
<div class="yui-gf">
	<div class="yui-u first" style="float: left;width: auto;">
		<div class="form-field">
    		<br/>
    		<input class="formsCheckBox" id="${fieldHtmlId}-entry" type="checkbox" tabindex="0" 
               onchange='disableSiblingInputField("${fieldHtmlId}");' />
    		<label for="${fieldHtmlId}-entry" class="checkbox">${msg("edit-details.label.edit-metadata")}</label>
  		</div>
	</div>
	<div class="yui-u" style="width: 85%;">
  		<div class="form-field">
	      	<label for="${fieldHtmlId}">${field.label?html}:<#if field.mandatory><span class="mandatory-indicator">${msg("form.required.fields.marker")}</span></#if></label>
	      	<input type="hidden" id="${fieldHtmlId}_isListProperty" name="${field.name}_isListProperty" value="true" />
	     	<select id="${fieldHtmlId}" name="${field.name}" tabindex="0"
	           	<#if field.description??>title="${field.description}"</#if>
	           	<#if field.indexTokenisationMode??>class="non-tokenised"</#if>
	           	<#if field.control.params.size??>size="${field.control.params.size}"</#if> 
	           	<#if field.control.params.styleClass??>class="${field.control.params.styleClass}"</#if>
	           	<#if field.control.params.style??>style="${field.control.params.style}"</#if>
	           	disabled="true">
            	<option value="${fieldValue?html}" selected="selected">${fieldValue?html}</option>
	     	</select>
	    	<@formLib.renderFieldHelp field=field />
		</div>
	</div>
</div>
<script type="text/javascript">//<![CDATA[
function disableSiblingInputField(fieldId){
  var fieldToDisable = YAHOO.util.Dom.get(fieldId);
  if (fieldToDisable.disabled === true){
    fieldToDisable.disabled = false;
  }else {
    fieldToDisable.disabled = true;
  }
}
//]]></script>

<script type="text/javascript">//<![CDATA[
(function(TSG)
{
	TSG.Dependency.registerHandler("${field.id}","${fieldHtmlId}");
})(window.TSG = window.TSG || {});
//]]></script>