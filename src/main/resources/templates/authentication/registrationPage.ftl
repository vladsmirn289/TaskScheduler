<#import "../parts/common.ftl" as c>
<#import "/spring.ftl" as spring>

<@c.commonPage>
    <div class="mx-auto bg-dark" style="padding: 15px; width: 40%; border-radius: 10px;">
        <form action="/registration" name="newClient" method="post">
            <div class="form-group">
                <label for="username" class="text-white"><@spring.message "login_"/></label>
                <input type="text" class="form-control ${(userExistsError??)?string('is-invalid', '')}" id="username" name="login"
                        <#if client??>
                            value="${client.login}"
                        </#if>/>
                <#if userExistsError??>
                    <div class="text-danger">
                        <@spring.message "userExistsError"/>
                    </div>
                <#elseif loginIsEmpty??>
                    <div class="text-danger">
                        <@spring.message "loginIsEmpty"/>
                    </div>
                </#if>
            </div>

            <div class="form-group">
                <label for="password" class="text-white"><@spring.message "password"/></label>
                <input type="password" class="form-control" id="password" name="password"
                        <#if client??>
                            value="${client.password}"
                        </#if>/>
            </div>

            <div class="form-group">
                <label for="password_repeat" class="text-white"><@spring.message "repeat_password"/></label>
                <input type="password" class="form-control ${(passwordRepeatError??)?string('is-invalid', '')}" id="password_repeat" name="passwordRepeat"/>
                <#if passwordRepeatError??>
                    <div class="text-danger">
                        <@spring.message "passwordRepeatError"/>
                    </div>
                </#if>
            </div>

            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <div class="row justify-content-md-center mb-2">
                <button type="submit" class="btn btn-success"><@spring.message "register"/></button>
            </div>
        </form>
    </div>
</@c.commonPage>