<#list systems as sys>
    <#if sys.code==system>
        ${sys.value}
    </#if>
</#list>