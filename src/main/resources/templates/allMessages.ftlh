<#include "parts/security.ftlh">
<#import "parts/common.ftlh" as c>
<#import "parts/editMessage.ftlh" as aF>

<@c.page>
<div class="form-row">
    <div class="form-group col-md-6">
        <form method="get" action="/messages" class="form-inline">
            <input type="text" name="filter" class="form-control" value = "${filter?ifExists}" placeholder="Search by tag" />
            <button type="submit" class="btn btn-primary ml-2">Search</button>
        </form>
    </div>
</div>

<@aF.saveEditForm false />
<#include "parts/listMessages.ftlh">

</@c.page>