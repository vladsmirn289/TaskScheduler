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
                    <option value="NO" <#if repeatTask?? && "NO" == repeatTask.getPriority()>selected="selected"</#if>>
                        Нет
                    </option>
                    <option value="LOW" <#if repeatTask?? && "LOW" == repeatTask.getPriority()>selected="selected"</#if>>
                        Низкий
                    </option>
                    <option value="MEDIUM" <#if repeatTask?? && "MEDIUM" == repeatTask.getPriority()>selected="selected"</#if>>
                        Средний
                    </option>
                    <option value="HIGH" <#if repeatTask?? && "HIGH" == repeatTask.getPriority()>selected="selected"</#if>>
                        Высокий
                    </option>
                </select>
            </div>

            <#if dateError??>
                <div class="text-danger">
                    Дата должна быть задана!
                </div>
            </#if>

            <#if dateStartError??>
                <div class="text-danger">
                    Начальная дата должна идти до конечной!
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
                    <option class="each_day_opt" value="EACH_DAY" selected="selected">
                        Каждый день
                    </option>

                    <option class="each_week_opt" value="EACH_WEEK">
                        Каждую неделю
                    </option>

                    <option class="each_day_of_month_opt" value="EACH_DAY_OF_MONTH">
                        Каждый день месяца
                    </option>

                    <option class="each_week_of_month_opt" value="EACH_WEEK_OF_MONTH">
                        Каждую неделю месяца
                    </option>

                    <option class="each_day_of_week_of_month_opt" value="EACH_DAY_OF_WEEK_OF_MONTH">
                        Каждый день недели месяца
                    </option>
                </select>

                <div id="period_res" class="mt-2">

                </div>
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

    <script>
        $(document).ready(function() {
            let days_of_week = "<label for='mon'>Пн</label>\n" +
                "                    <input type='checkbox' id='mon' name='monday'/>\n" +
                "\n" +
                "                    <label for='tue'>Вт</label>\n" +
                "                    <input type='checkbox' id='tue' name='tuesday'/>\n" +
                "\n" +
                "                    <label for='wed'>Ср</label>\n" +
                "                    <input type='checkbox' id='wed' name='wednesday'/>\n" +
                "\n" +
                "                    <label for='thu'>Чт</label>\n" +
                "                    <input type='checkbox' id='thu' name='thursday'/>\n" +
                "\n" +
                "                    <label for='fri'>Пт</label>\n" +
                "                    <input type='checkbox' id='fri' name='friday'/>\n" +
                "\n" +
                "                    <label for='sat'>Сб</label>\n" +
                "                    <input type='checkbox' id='sat' name='saturday'/>\n" +
                "\n" +
                "                    <label for='sun'>Вс</label>\n" +
                "                    <input type='checkbox' id='sun' name='sunday'/>";

            let day_of_month = "<label for='dayNum'></label> " +
                "<input type='number' min='1' max='31' id='dayNum' name='dayOfMonth'/>";

            let week_of_month = "<select name='monthWeek' class='form-control mx-auto' style='width: 50%'>" +
                "<option value='first'>Первая неделя месяца</option>" +
                "<option value='second'>Вторая неделя месяца</option>" +
                "<option value='third'>Третья неделя месяца</option>" +
                "<option value='fourth'>Четвёртая неделя месяца</option>" +
                "<option value='fifth'>Пятая неделя месяца</option>" +
                "<option value='sixth'>Шестая неделя месяца</option>" +
                "<option value='last'>Последняя неделя месяца</option>" +
                "</select>"

            let day_of_week_of_month = "<select name='numberDayOfWeek' class='form-control mx-auto' style='width: 50%'>" +
                "<option value='first'>1</option>" +
                "<option value='second'>2</option>" +
                "<option value='third'>3</option>" +
                "<option value='fourth'>4</option>" +
                "<option value='fifth'>5</option>" +
                "<option value='last'>Последний/яя/ее</option>" +
                "</select>" +
                "" +
                "<select name='dayOfWeek' class='form-control mx-auto' style='width: 50%'>" +
                "<option value='monday'>Пн</option>" +
                "<option value='tuesday'>Вт</option>" +
                "<option value='wednesday'>Ср</option>" +
                "<option value='thursday'>Чт</option>" +
                "<option value='friday'>Пт</option>" +
                "<option value='saturday'>Сб</option>" +
                "<option value='sunday'>Вс</option>" +
                "</select>"

            $('#inputPeriodMode').change(function() {
                if ($('#inputPeriodMode option:selected').hasClass("each_day_opt")) {
                    $("#period_res").html("")
                } else if ($('#inputPeriodMode option:selected').hasClass("each_week_opt")) {
                    $("#period_res").html(days_of_week)
                } else if ($('#inputPeriodMode option:selected').hasClass("each_day_of_month_opt")) {
                    $("#period_res").html(day_of_month)
                } else if ($('#inputPeriodMode option:selected').hasClass("each_week_of_month_opt")) {
                    $("#period_res").html(week_of_month)
                } else if ($('#inputPeriodMode option:selected').hasClass("each_day_of_week_of_month_opt")) {
                    $("#period_res").html(day_of_week_of_month)
                }
            })
        })
    </script>
</@c.commonPage>