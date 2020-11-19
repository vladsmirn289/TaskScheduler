<#import "../parts/common.ftl" as c>

<@c.commonPage>
    <div class="mx-auto bg-dark text-center text-white" style="padding: 15px; width: 50%; border-radius: 10px;">
        <form action="/repeatTask/createOrUpdateTask" name="repeatTask" method="post">
            <#if repeatTask?? && repeatTask.getId()??>
                <input type="text" value="${repeatTask.getId()}" name="id" hidden="hidden"/>
            </#if>

            <div class="form-group">
                <label for="taskName">Название задачи</label><br/>
                <input type="text" id="taskName" name="name" style="width: 50%"
                       <#if repeatTask??>value="${repeatTask.getName()}"</#if>/>
            </div>

            <div class="form-group">
                <label for="taskDesc">Описание задачи</label><br/>
                <textarea rows="5" cols="50" id="taskDesc" name="description"><#if repeatTask??>${repeatTask.getDescription()}</#if></textarea>
            </div>

            <div class="form-group">
                <label for="inputPriority">Выберите приоритет</label><br/>
                <select id="inputPriority" name="priority" class="form-control mx-auto" style="width: 50%">
                    <#list priorities as priority>
                        <option <#if repeatTask?? && priority == repeatTask.getPriority()>selected="selected"</#if>>
                            ${priority}
                        </option>
                    </#list>
                </select>
            </div>

            <#if dateError??>
                <div class="text-danger">
                    Дата должна быть задана!
                </div>
            </#if>

            <div class="form-group">
                <label for="startDate">Начальная дата</label><br/>
                <input type="date" id="startDate" name="startDateString"
                       <#if repeatTask?? && repeatTask.getStartDate()??>value="${repeatTask.getStartDate()}" </#if>/>
            </div>

            <div class="form-group">
                <label for="endDate">Конечная дата</label><br/>
                <input type="date" id="endDate" name="endDateString"
                        <#if repeatTask?? && repeatTask.getEndDate()??>value="${repeatTask.getEndDate()}" </#if>/>
            </div>

            <div class="form-group">
                <label for="inputPeriodMode">Выберите период</label><br/>
                <select id="inputPeriodMode" name="periodMode" class="form-control mx-auto" style="width: 50%">
                    <#list periodModes as periodMode>
                        <option <#if repeatTask?? && periodMode == repeatTask.getPeriodMode()>selected="selected"</#if>>
                            ${periodMode}
                        </option>
                    </#list>
                </select>
            </div>

            <div class="form-group">
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <#if repeatTask?? && repeatTask.getId()??>
                    <button class="btn btn-primary" type="submit">Редактировать</button>
                <#else>
                    <button class="btn btn-primary" type="submit">Создать</button>
                </#if>
            </div>
        </form>
    </div>
</@c.commonPage>