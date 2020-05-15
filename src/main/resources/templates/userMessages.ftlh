<#include "parts/security.ftlh">
<#import "parts/common.ftlh" as c>
<#import "parts/editMessage.ftlh" as e>

<@c.page>


<#include "parts/subscriptionsHead.ftlh">

<#if showEditForm??>
    <@e.saveEditForm true />
</#if>

<#include "parts/listMessages.ftlh">

</@c.page>