<#import "parts/common.ftlh" as c>

<@c.page>

${message?ifExists}

<h5>${user.username}</h5>
<form method="post">
    <div class="form-group row">
        <label class="col-sm-2 col-form-label"> Password: </label>
        <div class="col-sm-6">
            <input class="form-control ${(passwordError??)?string('is-invalid', '')}" type="password" name="password" placeholder="Password" />
            <#if passwordError??>
                <div class="invalid-feedback">
                ${passwordError}
                </div>
            </#if>
        </div>
    </div>
    <div class="form-group row">
        <label class="col-sm-2 col-form-label"> Confirm password: </label>
        <div class="col-sm-6">
            <input class="form-control ${(passwordConfirmationError??)?string('is-invalid', '')}"
                   type="password" name="passwordConfirmation" placeholder="Password" />
            <#if passwordConfirmationError??>
            <div class="invalid-feedback">
                ${passwordConfirmationError}
            </div>
        </#if>
        </div>
    </div>
    <div class="form-group row">

        <label class="col-sm-2 col-form-label"> Email: </label>
        <div class="col-sm-6">
            <input class="form-control" type="email" name="email" value="${user.email}" placeholder="${user.email?ifExists}" />
        </div>

    </div>
    <button type="submit" class="btn btn-primary">Update</button>
    <input type="hidden" name="_csrf" value="${_csrf.token}" />
</form>

</@c.page>