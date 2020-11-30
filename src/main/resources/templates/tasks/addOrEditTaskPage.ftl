<#import "../parts/common.ftl" as c>
<#import "/spring.ftl" as spring>

<@c.commonPage>
    <div class="mx-auto bg-dark text-center text-white p-3 col-md-5" style="border-radius: 10px;">
        <div class="row">
            <div class="mx-auto col-md-12">
                <form action="/task/createOrUpdateTask" name="task" method="post">
                    <#if task??>
                        <input type="text" value="${task.getId()}" name="id" hidden="hidden"/>
                    </#if>

                    <div class="form-group">
                        <label for="taskName"><@spring.message "task_name"/></label><br/>
                        <input type="text" id="taskName" name="name" style="width: 50%"
                               <#if task??>value="${task.getName()}"</#if>/>
                    </div>

                    <div class="form-group">
                        <label for="taskDesc"><@spring.message "description"/></label><br/>
                        <textarea rows="5" class="w-100" id="taskDesc" name="description"><#if task??>${task.getDescription()}</#if></textarea>
                    </div>

                    <div class="form-group">
                        <label for="inputPriority"><@spring.message "choose_priority"/></label><br/>
                        <select id="inputPriority" name="priority" class="form-control mx-auto" style="width: 50%">
                            <option value="NO" <#if task?? && "NO" == task.getPriority()>selected="selected"</#if>>
                                <@spring.message "no_priority"/>
                            </option>
                            <option value="LOW" <#if task?? && "LOW" == task.getPriority()>selected="selected"</#if>>
                                <@spring.message "low_priority"/>
                            </option>
                            <option value="MEDIUM" <#if task?? && "MEDIUM" == task.getPriority()>selected="selected"</#if>>
                                <@spring.message "medium_priority"/>
                            </option>
                            <option value="HIGH" <#if task?? && "HIGH" == task.getPriority()>selected="selected"</#if>>
                                <@spring.message "high_priority"/>
                            </option>
                        </select>
                    </div>

                    <#if task??>
                        <div class="form-group">
                            <input type="date" value="${task.getDate()}" name="textDate"/>
                        </div>
                    <#else>
                        <input type="text" value="${date}" hidden="hidden" name="textDate"/>
                    </#if>

                    <div class="form-group">
                        <label for="inputProgress"><@spring.message "task_progress"/></label><br/>
                        <div class="form-inline">
                            <div class="mx-auto">
                                <input type="number" id="progressValue"
                                       <#if task??>value="${task.getProgress()}" <#else>value="0"</#if>
                                       class="col-2 col-md-2 p-0"
                                       oninput="changeProgressBar(this.value)"/>
                                <input class="col-8 col-md-8 p-0" type="range" min="0" max="100" id="inputProgress" name="progress"
                                       <#if task??>value="${task.getProgress()}" <#else>value="0"</#if>
                                       oninput="changeProgressValue(this.value)"/>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                        <#if task??>
                            <button class="btn btn-primary" type="submit"><@spring.message "edit"/></button>
                        <#else>
                            <button class="btn btn-primary" type="submit"><@spring.message "create"/></button>
                        </#if>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        function changeProgressValue(val) {
            document.getElementById("progressValue").value = val;
        }

        function changeProgressBar(val) {
            document.getElementById("inputProgress").value = val;
        }
    </script>
</@c.commonPage>