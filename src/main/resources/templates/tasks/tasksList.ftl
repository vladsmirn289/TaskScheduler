<#import "../parts/common.ftl" as c>

<@c.commonPage>
    <div>
        <form class="form-inline justify-content-center mb-4" action="/task/${date}/addTaskPage" method="get">
            <div class="form-group">
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button class="btn btn-primary" type="submit">+ Добавить</button>
            </div>
        </form>

        <#list tasks as t>
            <div class="text-white">
                ${t.getName()}
            </div>
        </#list>
    </div>
</@c.commonPage>