<#include "/org/alfresco/components/form/controls/common/utils.inc.ftl" />
<#if field.control.params.size??><#assign size=field.control.params.size><#else><#assign size=5></#if>

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

<#if field.control.params.onlyEnabledValues??>
   <#assign onlyEnabledValues=field.control.params.onlyEnabledValues>
<#else>
   <#assign onlyEnabledValues="true">
</#if>

<#assign fieldValue=field.value>

<#if fieldValue?string == "" && field.control.params.defaultValueContextProperty??>
   <#if context.properties[field.control.params.defaultValueContextProperty]??>
      <#assign fieldValue = context.properties[field.control.params.defaultValueContextProperty]>
   <#elseif args[field.control.params.defaultValueContextProperty]??>
      <#assign fieldValue = args[field.control.params.defaultValueContextProperty]>
   </#if>
</#if>

<#if fieldValue?string != "">
   <#assign values=fieldValue?split(",")>
<#else>
   <#assign values=[]>
</#if>

<script type="text/javascript">//<![CDATA[
(function(TSG)
{
	//auto runs after the html element's parent has fired domReady since the item is registered with alfresco's component manager
	new TSG.DynamicDropdown("${fieldHtmlId}-entry").setOptions(
	{
		picklistName:"${field.control.params['picklistName']}",
		dependsOn:[
			<#if (field.control.params['dependsOn'])??>
				<#list (field.control.params['dependsOn'])?split(",") as dependsOn>
					"${dependsOn}"<#if dependsOn_has_next>,</#if>
				</#list>
			</#if>
		],
		initialValue:[
		              <#list values as value>
		              "${value}"<#if value_has_next>,</#if>
		              </#list>
		],
		itemId:"${(form.arguments.itemId!"")?js_string}",
        level:"${field.control.params['level']!"1"}",
        formId: "${(form.arguments.formId!"")?js_string}",
		onlyEnabledValues: "${onlyEnabledValues}"
	});

})(window.TSG = window.TSG || {});
//]]></script>

<div class="yui-gf">
	<div class="yui-u first" style="float: left;width: auto;">
		<div class="form-field">
        	<br/>
        	<input class="formsCheckBox" id="${fieldHtmlId}-entry" type="checkbox" tabindex="0" 
                   onchange='disableSiblingInputField("${fieldHtmlId}-entry");' />
        	<label for="${fieldHtmlId}-entry" class="checkbox">${msg("edit-details.label.edit-metadata")}</label>
      	</div>
	</div>
	<div class="yui-u" style="width: 85%;">
		<div class="form-field">
			<label for="${fieldHtmlId}-entry">${field.label?html}:<#if field.mandatory><span class="mandatory-indicator">${msg("form.required.fields.marker")}</span></#if></label>
			<input id="${fieldHtmlId}" type="hidden" name="${field.name}" value="${fieldValue?string}" />
			<input type="hidden" id="${fieldHtmlId}_isListProperty" name="${field.name}_isListProperty" value="true" />
			<select id="${fieldHtmlId}-entry" name="-" multiple="multiple" size="${size}" tabindex="0"
			       onchange="javascript:Alfresco.util.updateMultiSelectListValue('${fieldHtmlId}-entry', '${fieldHtmlId}', <#if field.mandatory>true<#else>false</#if>);"
			       <#if field.description??>title="${field.description}"</#if> 
			       <#if field.control.params.styleClass??>class="${field.control.params.styleClass}"</#if>
			       <#if field.control.params.style??>style="${field.control.params.style}"</#if> 
			       disabled="true" >
			</select>
			<@formLib.renderFieldHelp field=field />
			<#if field.control.params.mode?? && isValidMode(field.control.params.mode?upper_case)>
				<input id="${fieldHtmlId}-mode" type="hidden" name="${field.name}-mode" value="${field.control.params.mode?upper_case}" />
			</#if>
		</div>
    </div>
</div>

<script type="text/javascript">//<![CDATA[
(function(TSG)
{
	TSG.Dependency.registerHandler("${field.id}","${fieldHtmlId}-entry");
})(window.TSG = window.TSG || {});
//]]></script>

<#function isSelected optionValue>
   <#list values as value>
      <#if optionValue == value?string || (value?is_number && value?c == optionValue)>
         <#return true>
      </#if>
   </#list>
   <#return false>
</#function>

<#function isValidMode modeValue>
   <#return modeValue == "OR" || modeValue == "AND">
</#function>

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