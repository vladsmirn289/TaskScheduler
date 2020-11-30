<#import "../parts/common.ftl" as c>
<#import "/spring.ftl" as spring>

<@c.commonPage>
    <div class="mx-auto bg-dark p-3 col-md-4" style="border-radius: 10px;">
        <div class="row">
            <div class="mx-auto col-md-10">
                <form action="/client/changePassword" method="post">
                    <div class="form-group">
                        <label for="newPassword" class="text-white"><@spring.message "new_password"/></label>
                        <input type="password" class="form-control" id="newPassword" name="newPassword"/>
                    </div>

                    <div class="form-group">
                        <label for="retypePassword" class="text-white"><@spring.message "repeat_password"/></label>
                        <input type="password" class="form-control ${(retypePasswordError??)?string('is-invalid', '')}" id="retypePassword" name="retypePassword"/>
                        <#if retypePasswordError??>
                            <div class="text-danger">
                                <@spring.message "passwordRepeatError"/>
                            </div>
                        </#if>
                    </div>

                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                    <div class="row justify-content-center mb-2">
                        <button type="submit" class="btn btn-success"><@spring.message "change_password"/></button>
                    </div>
                </form>
            </div>
        </div>

        <div class="row justify-content-center">
            <a href="/registration"><@spring.message "register"/></a>
        </div>
    </div>
</@c.commonPage>