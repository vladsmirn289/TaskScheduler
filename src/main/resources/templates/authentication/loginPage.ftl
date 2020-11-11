<#import "../parts/common.ftl" as c>

<@c.commonPage>
    <div class="mx-auto bg-dark" style="padding: 15px; width: 40%; border-radius: 10px;">
        <#if RequestParameters.error??>
            <div class="text-danger mb-2">
                Неверный логин или пароль!
            </div>
        </#if>
        <form action="/login" method="post">
            <div class="form-group">
                <label for="username" class="text-white">Логин</label>
                <input type="text" class="form-control" id="username" name="username"/>
            </div>

            <div class="form-group">
                <label for="password" class="text-white">Пароль</label>
                <input type="password" class="form-control" id="password" name="password"/>
            </div>

            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <div class="row justify-content-md-center mb-2">
                <button type="submit" class="btn btn-success">Войти</button>
            </div>
        </form>

        <div class="row justify-content-md-center">
            <a href="/registration">Зарегестрироваться</a>
        </div>
    </div>
</@c.commonPage>