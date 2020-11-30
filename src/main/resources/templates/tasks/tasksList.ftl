<#import "../parts/common.ftl" as c>
<#import "../parts/pager.ftl" as p>
<#import "/spring.ftl" as spring>

<@c.commonPage>
    <div>
        <div class="row mb-3">
            <form class="form-inline mx-auto" action="/task/${date}/addTaskPage" method="get">
                <div class="form-group">
                    <button class="btn btn-primary" type="submit">+ <@spring.message "add"/></button>
                </div>
            </form>
        </div>

        <div class="row">
            <div class="mx-auto">
                <#if tasks.content?size != 0>
                    <@p.pager url tasks/>
                </#if>
            </div>
        </div>

        <#list tasks.content as t>
            <div class="row mb-3">
                <table class="table table-bordered table-dark">
                    <tbody>
                    <tr>
                        <td class="p-0" colspan="4">
                            <div class="text-dark text-center bg-warning m-0">
                                ${t.getName()}
                            </div>
                        </td>
                    </tr>

                    <tr>
                        <#if t.getPriority().name() == "NO">
                            <td class="p-0" style="background-color: white">
                                <div class="col-md-1"></div>
                            </td>
                        <#elseif t.getPriority().name() == "LOW">
                            <td class="p-0" style="background-color: grey">
                                <div class="col-md-1"></div>
                            </td>
                        <#elseif t.getPriority().name() == "MEDIUM">
                            <td class="p-0" style="background-color: green">
                                <div class="col-md-1"></div>
                            </td>
                        <#elseif t.getPriority().name() == "HIGH">
                            <td class="p-0" style="background-color: red">
                                <div class="col-md-1"></div>
                            </td>
                        </#if>

                        <td class="p-0">
                            <div class="col-md-auto p-0 ml-3 mr-3 mt-2">
                                <pre class="text-white">${t.getDescription()}</pre>
                            </div>
                        </td>

                        <td class="p-0">
                            <div class="col-md-auto mx-auto p-0">
                                <div class="bg-success rounded-circle position-relative mx-auto mt-3 mb-3"
                                     style="width: 60px; height: 60px">
                                    <div class="text-white position-absolute"
                                         style="top: 50%; transform: translateY(-50%); text-align: center; width: inherit">
                                        ${t.getProgress()}%
                                    </div>
                                </div>

                                <form class="mb-2 justify-content-center form-inline" action="/task/toNextWeek" method="post">
                                    <input type="text" name="taskId" value="${t.getId()}" hidden="hidden"/>
                                    <input type="text" name="taskCurrentDate" value="${t.getDate()}" hidden="hidden"/>

                                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                                    <button class="btn btn-primary" type="submit"><@spring.message "to_next_week"/></button>
                                </form>
                            </div>
                        </td>

                        <td class="p-0">
                            <div class="col-md-auto mr-3">
                                <div class="mt-2">
                                    <form action="/task/editPage" method="get">
                                        <input type="text" name="taskId" value="${t.getId()}" hidden="hidden"/>
                                        <button class="btn btn-primary" type="submit"><@spring.message "edit"/></button>
                                    </form>

                                    <form class="mt-2" action="/task/delete" method="post">
                                        <input type="text" name="taskId" value="${t.getId()}" hidden="hidden"/>
                                        <input type="text" name="taskCurrentDate" value="${t.getDate()}" hidden="hidden"/>

                                        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                                        <button class="btn btn-primary" type="submit"><@spring.message "delete"/></button>
                                    </form>
                                </div>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </#list>

        <div class="row">
            <div class="mx-auto">
                <#if tasks.content?size != 0>
                    <@p.pager url tasks/>
                </#if>
            </div>
        </div>
    </div>
</@c.commonPage>