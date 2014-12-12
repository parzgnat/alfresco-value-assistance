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
	<#if form.mode == "view">
		<#if field.control.params['loadLabel']??>
			new TSG.LoadLabel("${fieldHtmlId}-value").setOptions(
			{
				picklistName:"${field.control.params['picklistName']}",
				initialValue:"${fieldValue}",
				itemId:"${(form.arguments.itemId!"")?js_string}"
			});
		</#if>
	<#else>
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
			itemId:"${(form.arguments.itemId!"")?js_string}"
		});
	</#if>

})(window.TSG = window.TSG || {});
//]]></script>

<div class="form-field">
   <#if form.mode == "view">
      <div class="viewmode-field">
         <#if field.mandatory && !(fieldValue?is_number) && fieldValue?string == "">
            <span class="incomplete-warning"><img src="${url.context}/res/components/form/images/warning-16.png" title="${msg("form.field.incomplete")}" /><span>
         </#if>
         <span class="viewmode-label">${field.label?html}:</span>
         <#if fieldValue?string == "">
            <#assign valueToShow=msg("form.control.novalue")>
         <#else>
            <#if field.control.params.options?? && field.control.params.options != "" &&
                 field.control.params.options?index_of(labelSeparator) != -1>
                 <#assign valueToShow="">
                 <#assign firstLabel=true>
                 <#list field.control.params.options?split(optionSeparator) as nameValue>
                    <#assign choice=nameValue?split(labelSeparator)>
                    <#if isSelected(choice[0])>
                       <#if !firstLabel>
                          <#assign valueToShow=valueToShow+",">
                       <#else>
                          <#assign firstLabel=false>
                       </#if>
                       <#assign valueToShow=valueToShow+choice[1]>
                    </#if>
                 </#list>
            <#else>
               <#assign valueToShow=fieldValue>
            </#if>
         </#if>
         <span id="${fieldHtmlId}-value" class="viewmode-value">${valueToShow?html}</span>
      </div>
   <#else>
      <label for="${fieldHtmlId}-entry">${field.label?html}:<#if field.mandatory><span class="mandatory-indicator">${msg("form.required.fields.marker")}</span></#if></label>
      <input id="${fieldHtmlId}" type="hidden" name="${field.name}" value="${fieldValue?string}" />
     <select id="${fieldHtmlId}-entry" name="-" multiple="multiple" size="${size}" tabindex="0"
           onchange="javascript:Alfresco.util.updateMultiSelectListValue('${fieldHtmlId}-entry', '${fieldHtmlId}', <#if field.mandatory>true<#else>false</#if>);"
           <#if field.description??>title="${field.description}"</#if> 
           <#if field.control.params.styleClass??>class="${field.control.params.styleClass}"</#if>
           <#if field.control.params.style??>style="${field.control.params.style}"</#if>
           <#if field.disabled && !(field.control.params.forceEditable?? && field.control.params.forceEditable == "true")>disabled="true"</#if>>
     </select>
     <@formLib.renderFieldHelp field=field />
   </#if>
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

