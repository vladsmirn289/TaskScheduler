<#import "../parts/common.ftl" as c>
<#import "/spring.ftl" as spring>

<@c.commonPage>
    <div class="mx-auto bg-dark p-3 col-md-5" style="border-radius: 10px;">
        <#if RequestParameters.error??>
            <div class="row mb-2">
                <div class="mx-auto text-danger">
                    <@spring.message "wrong_login_or_pass"/>
                </div>
            </div>
        </#if>

        <div class="row">
            <div class="mx-auto col-md-11">
                <form action="/login" method="post">
                    <div class="form-group">
                        <label for="username" class="text-white"><@spring.message "login_"/></label>
                        <input type="text" class="form-control" id="username" name="username"/>
                    </div>

                    <div class="form-group">
                        <label for="password" class="text-white"><@spring.message "password"/></label>
                        <input type="password" class="form-control" id="password" name="password"/>
                    </div>

                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                    <div class="row justify-content-center mb-2">
                        <button type="submit" class="btn btn-success"><@spring.message "login"/></button>
                    </div>
                </form>
            </div>
        </div>

        <div class="row">
            <div class="mx-auto">
                <a href="/registration"><@spring.message "register"/></a>
            </div>
        </div>
    </div>
</@c.commonPage>