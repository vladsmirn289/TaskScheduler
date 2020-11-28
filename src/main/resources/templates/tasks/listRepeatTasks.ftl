<#import "../parts/common.ftl" as c>
<#import "/spring.ftl" as spring>

<@c.commonPage>
    <div>
        <form class="form-inline justify-content-center mb-4" action="/repeatTask/addTaskPage" method="get">
            <div class="form-group">
                <button class="btn btn-primary" type="submit">+ <@spring.message "add"/></button>
            </div>
        </form>

        <#list repeatTasks as t>
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

                    <div class="col-md-2 mx-auto text-white">
                        <@spring.message "start"/>: ${t.getStartDate()}<br/>
                        <@spring.message "end"/>: ${t.getEndDate()}
                    </div>

                    <div class="border-left border-secondary mr-2"></div>

                    <div class="col-md-2 mb-2">
                        <div class="mt-2">
                            <form action="/repeatTask/editPage" method="get">
                                <input type="text" name="taskId" value="${t.getId()}" hidden="hidden"/>
                                <button class="btn btn-primary" type="submit"><@spring.message "edit"/></button>
                            </form>

                            <form class="mt-2" action="/repeatTask/delete" method="post">
                                <input type="text" name="taskId" value="${t.getId()}" hidden="hidden"/>

                                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                                <button class="btn btn-primary" type="submit"><@spring.message "delete"/></button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </#list>
    </div>
</@c.commonPage>