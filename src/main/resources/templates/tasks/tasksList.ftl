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
            <div class="card border-0 mb-3">
                <div class="card-title text-center bg-warning m-0">
                    ${t.getName()}
                </div>

                <div class="row no-gutters bg-dark">
                    <#if t.getPriority().name() == "NO">
                        <div class="col-md-1" style="background-color: white"></div>
                    <#elseif t.getPriority().name() == "LOW">
                        <div class="col-md-1" style="background-color: grey"></div>
                    <#elseif t.getPriority().name() == "MEDIUM">
                        <div class="col-md-1" style="background-color: green"></div>
                    <#elseif t.getPriority().name() == "HIGH">
                        <div class="col-md-1" style="background-color: red"></div>
                    </#if>

                    <div class="col-md-5 ml-3 mt-2">
                        <pre class="text-white">${t.getDescription()}</pre>
                    </div>

                    <div class="border-left border-secondary mr-2"></div>

                    <div class="col-md-2 mx-auto">
                        <div class="bg-success rounded-circle position-relative mx-auto mt-3 mb-3"
                             style="width: 60px; height: 60px">
                            <div class="text-white position-absolute"
                                 style="top: 50%; transform: translateY(-50%); text-align: center; width: inherit">
                                ${t.getProgress()}%
                            </div>
                        </div>

                        <form class="mb-2" action="/task/toNextWeek" method="post">
                            <input type="text" name="taskId" value="${t.getId()}" hidden="hidden"/>
                            <input type="text" name="taskCurrentDate" value="${t.getDate()}" hidden="hidden"/>

                            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                            <button class="btn btn-primary" type="submit">Перенести на следующую неделю</button>
                        </form>
                    </div>

                    <div class="border-left border-secondary mr-2"></div>

                    <div class="col-md-2">
                        <div class="mt-2">
                            <form action="/task/editPage" method="get">
                                <input type="text" name="taskId" value="${t.getId()}" hidden="hidden"/>
                                <button class="btn btn-primary" type="submit">Редактировать</button>
                            </form>

                            <form class="mt-2" action="/task/delete" method="post">
                                <input type="text" name="taskId" value="${t.getId()}" hidden="hidden"/>
                                <input type="text" name="taskCurrentDate" value="${t.getDate()}" hidden="hidden"/>

                                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                                <button class="btn btn-primary" type="submit">Удалить</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </#list>
    </div>
</@c.commonPage>