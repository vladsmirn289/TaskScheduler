<#import "../parts/common.ftl" as c>
<#import "/spring.ftl" as spring>

<@c.commonPage>
    <div class="mx-auto bg-dark" style="padding: 15px; width: 40%; border-radius: 10px;">
        <div class="bg-warning p-1 mb-2" style="border-radius: 10px">
            <@spring.message "change_login_warning"/>
        </div>

        <form action="/client/personalRoom" name="changedPerson" method="post">
            <input type="hidden" name="id" value="${client.id}"/>
            <input type="hidden" name="password" value="${client.password}"/>
            <div class="form-group">
                <label for="username" class="text-white"><@spring.message "login_"/></label>
                <input type="text" class="form-control ${(loginIsEmpty?? || userExistsError??)?string('is-invalid', '')}" id="username" name="login"
                       value="${client.login}"/>
                <#if loginIsEmpty??>
                    <div class="text-danger">
                        <@spring.message "loginIsEmpty"/>
                    </div>
                </#if>
                <#if userExistsError??>
                    <div class="text-danger">
                        <@spring.message "userExistsError"/>
                    </div>
                </#if>
            </div>

            <div class="form-group">
                <a class="btn btn-primary" href="/client/resetPasswordPage"><@spring.message "change_password"/></a>
            </div>

            <div class="form-group">
                <a class="btn btn-primary" onclick="return deletePermission()" href="/client/deleteAccount">
                    <@spring.message "delete_account"/>
                </a>
            </div>

            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <div class="row justify-content-md-center mb-2">
                <button type="submit" class="btn btn-success"><@spring.message "save_changes"/></button>
            </div>
        </form>

        <script>
            function deletePermission() {
                return window.confirm("<@spring.message 'delete_permission'/>");
            }
        </script>
    </div>
</@c.commonPage>