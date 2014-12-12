<#escape x as jsonUtils.encodeJSONString(x)>
<#if args['callback']??>
${args['callback']}(
</#if>
{
<#if error??>
	"error" : "${error}"
<#else>
	"result" : {
		<#if labels??>
			"labels": "${labels}"
		<#elseif picklistItems??>
			"picklist":
			[
				<#list picklistItems as picklistItem>
					{
						"label" : "${picklistItem.label}",
						"value" : "${picklistItem.value}"
					}
					<#if picklistItem_has_next>,</#if>
				</#list>
			]
		</#if>
	}
</#if>
}
<#if args['callback']??>
)
</#if>
</#escape>