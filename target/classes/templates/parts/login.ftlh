<#macro login path isRegisterForm>
<form action="${path}" method="post">
    <div class="form-group row">
        <label class="col-sm-2 col-form-label"> User Name: </label>
        <div class="col-sm-6">
            <input class="form-control ${(usernameError??)?string('is-invalid', '')}" type="text" name="username"
                   value="<#if user??>${user.username}</#if>" placeholder="User name" />
            <#if usernameError??>
                <div class="invalid-feedback">
                    ${usernameError}
                </div>
            </#if>
        </div>
    </div>
    <div class="form-group row">
        <label class="col-sm-2 col-form-label"> Password: </label>
        <div class="col-sm-6">
            <input class="form-control ${(passwordError??)?string('is-invalid', '')}" type="password" name="password"
                   placeholder="Password" />
            <#if passwordError??>
                <div class="invalid-feedback">
                    ${passwordError}
                </div>
            </#if>
        </div>
    </div>
    <#if isRegisterForm>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label"> Confirm password: </label>
            <div class="col-sm-6">
                <input class="form-control ${(passwordConfirmationError??)?string('is-invalid', '')}" type="password" name="passwordConfirmation"
                       placeholder="Confirm password" />
                <#if passwordConfirmationError??>
                <div class="invalid-feedback">
                    ${passwordConfirmationError}
                </div>
                </#if>
            </div>
        </div>
    </#if>
    <#if isRegisterForm>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label"> Email: </label>
            <div class="col-sm-6">
                <input class="form-control ${(emailError??)?string('is-invalid', '')}" type="email" name="email"
                       value="<#if user??>${user.email}</#if>" placeholder="example@examle.com" />
                <#if emailError??>
                    <div class="invalid-feedback">
                        ${emailError}
                    </div>
                </#if>
            </div>
        </div>
    </#if>
    <#if !isRegisterForm><a href="/registration">Registration</a></#if>
    <button type="submit" class="btn btn-primary"><#if isRegisterForm>Create<#else>Sign In</#if></button>
    <input type="hidden" name="_csrf" value="${_csrf.token}" />
</form>
</#macro>

<#macro logout>
<form action="/logout" method="post">
    <input type="hidden" name="_csrf" value="${_csrf.token}" />
    <button type="submit" class="btn btn-primary">Sign Out</button>
</form>
</#macro>