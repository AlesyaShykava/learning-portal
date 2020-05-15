<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>

<@c.page>

<#if message??>
    <div class="alert alert-${messageType}" role="alert">
        ${message}
    </div>
</#if>


<@l.login "/login" false/>

</@c.page>