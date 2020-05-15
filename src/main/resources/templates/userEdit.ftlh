<#import "parts/common.ftlh" as c>

<@c.page>
<h5>User editor</h5>
<form action="/user/usersList" method="post">
    <div class="form-group row">
        <label class="col-sm-2 col-form-label">Updated name</label>
        <div class="col-sm-10">
            <input type="text" name="username" value="${user.username}" />
        </div>
    </div>
    <div class="form-group row">
        <div class="col-sm-2">Roles</div>
        <div class="col-sm-10">
            <#list roles as role>
                <div class="form-check">
                    <input type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string('checked', "")} />
                    <label class="form-check-label">
                        ${role}
                    </label>
                </div>
            </#list>
        </div>
    </div>
    <input type="hidden" name="userId" value="${user.id}">
    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <div class="form-group row">
        <div class="col-sm-10">
            <button type="submit" class="btn btn-primary">Save</button>
        </div>
    </div>
</form>
</@c.page>