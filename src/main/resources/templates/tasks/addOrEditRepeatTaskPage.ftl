<#import "../parts/common.ftl" as c>
<#import "/spring.ftl" as spring>

<@c.commonPage>
    <div class="mx-auto bg-dark text-center text-white p-3 col-md-5" style="border-radius: 10px;">
        <div class="row">
            <div class="mx-auto col-md-12">
                <form action="/repeatTask/createOrUpdateTask" name="repeatTask" method="post">
                    <#if repeatTask?? && repeatTask.getId()??>
                        <input type="text" value="${repeatTask.getId()}" name="id" hidden="hidden"/>
                    </#if>

                    <div class="row justify-content-center mb-3">
                        <a href="/repeatTask/list"><@spring.message "return_back"/></a>
                    </div>

                    <div class="form-group">
                        <label for="taskName"><@spring.message "task_name"/></label><br/>
                        <input type="text" id="taskName" name="name" style="width: 50%"
                               <#if repeatTask??>value="${repeatTask.getName()}"</#if>/>
                    </div>

                    <div class="form-group">
                        <label for="taskDesc"><@spring.message "description"/></label><br/>
                        <textarea rows="5" class="w-100" id="taskDesc" name="description"><#if repeatTask??>${repeatTask.getDescription()}</#if></textarea>
                    </div>

                    <div class="form-group">
                        <label for="inputPriority"><@spring.message "choose_priority"/></label><br/>
                        <select id="inputPriority" name="priority" class="form-control mx-auto" style="width: 50%">
                            <option value="NO" <#if repeatTask?? && "NO" == repeatTask.getPriority()>selected="selected"</#if>>
                                <@spring.message "no_priority"/>
                            </option>
                            <option value="LOW" <#if repeatTask?? && "LOW" == repeatTask.getPriority()>selected="selected"</#if>>
                                <@spring.message "low_priority"/>
                            </option>
                            <option value="MEDIUM" <#if repeatTask?? && "MEDIUM" == repeatTask.getPriority()>selected="selected"</#if>>
                                <@spring.message "medium_priority"/>
                            </option>
                            <option value="HIGH" <#if repeatTask?? && "HIGH" == repeatTask.getPriority()>selected="selected"</#if>>
                                <@spring.message "high_priority"/>
                            </option>
                        </select>
                    </div>

                    <#if dateError??>
                        <div class="text-danger">
                            <@spring.message "dateError"/>
                        </div>
                    </#if>

                    <#if dateStartError??>
                        <div class="text-danger">
                            <@spring.message "dateStartError"/>
                        </div>
                    </#if>

                    <div class="form-group">
                        <label for="startDate"><@spring.message "start_date"/></label><br/>
                        <input type="date" id="startDate" name="startDateString"
                               <#if repeatTask?? && repeatTask.getStartDate()??>value="${repeatTask.getStartDate()}" </#if>/>
                    </div>

                    <div class="form-group">
                        <label for="endDate"><@spring.message "end_date"/></label><br/>
                        <input type="date" id="endDate" name="endDateString"
                               <#if repeatTask?? && repeatTask.getEndDate()??>value="${repeatTask.getEndDate()}" </#if>/>
                    </div>

                    <div class="form-group">
                        <label for="inputPeriodMode"><@spring.message "choosing_period"/></label><br/>
                        <select id="inputPeriodMode" name="periodMode" class="form-control mx-auto" style="width: 50%">
                            <option class="each_day_opt" value="EACH_DAY" selected="selected">
                                <@spring.message "each_day"/>
                            </option>

                            <option class="each_week_opt" value="EACH_WEEK">
                                <@spring.message "each_week"/>
                            </option>

                            <option class="each_day_of_month_opt" value="EACH_DAY_OF_MONTH">
                                <@spring.message "each_day_of_month"/>
                            </option>

                            <option class="each_week_of_month_opt" value="EACH_WEEK_OF_MONTH">
                                <@spring.message "each_week_of_month"/>
                            </option>

                            <option class="each_day_of_week_of_month_opt" value="EACH_DAY_OF_WEEK_OF_MONTH">
                                <@spring.message "each_day_of_week_of_month"/>
                            </option>

                            <!-- With gaps -->

                            <option class="each_n_days_opt" value="EACH_DAY">
                                <@spring.message "after_n_days"/>
                            </option>

                            <option class="each_n_weeks_opt" value="EACH_WEEK">
                                <@spring.message "after_n_weeks"/>
                            </option>

                            <option class="each_n_month_with_day_opt" value="EACH_DAY_OF_MONTH">
                                <@spring.message "each_day_of_month_after_n_months"/>
                            </option>

                            <option class="each_n_month_with_week_opt" value="EACH_WEEK_OF_MONTH">
                                <@spring.message "each_week_of_month_after_n_months"/>
                            </option>

                            <option class="each_n_month_with_week_and_day_opt" value="EACH_DAY_OF_WEEK_OF_MONTH">
                                <@spring.message "each_day_of_week_of_month_after_n_months"/>
                            </option>
                        </select>

                        <div id="period_res" class="mt-2">

                        </div>
                    </div>

                    <div class="form-group">
                        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                        <#if repeatTask?? && repeatTask.getId()??>
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
        $(document).ready(function() {
            let days_of_week = "<label for='mon'><@spring.message 'mon'/></label>\n" +
                "                    <input type='checkbox' id='mon' name='monday'/>\n" +
                "\n" +
                "                    <label class='ml-1' for='tue'><@spring.message 'tue'/></label>\n" +
                "                    <input type='checkbox' id='tue' name='tuesday'/>\n" +
                "\n" +
                "                    <label class='ml-1' for='wed'><@spring.message 'wed'/></label>\n" +
                "                    <input type='checkbox' id='wed' name='wednesday'/>\n" +
                "\n" +
                "                    <label class='ml-1' for='thu'><@spring.message 'thu'/></label>\n" +
                "                    <input type='checkbox' id='thu' name='thursday'/>\n" +
                "\n" +
                "                    <label class='ml-1' for='fri'><@spring.message 'fri'/></label>\n" +
                "                    <input type='checkbox' id='fri' name='friday'/>\n" +
                "\n" +
                "                    <label class='ml-1' for='sat'><@spring.message 'sat'/></label>\n" +
                "                    <input type='checkbox' id='sat' name='saturday'/>\n" +
                "\n" +
                "                    <label class='ml-1' for='sun'><@spring.message 'sun'/></label>\n" +
                "                    <input type='checkbox' id='sun' name='sunday'/>";

            let day_of_month = "<label for='dayNum'><@spring.message 'day_of_the_month'/></label> " +
                "<input type='number' min='1' max='31' id='dayNum' name='dayOfMonth'/>";

            let week_of_month = "<select name='monthWeek' class='form-control mx-auto' style='width: 50%'>" +
                "<option value='first'><@spring.message 'first_week_of_month'/></option>" +
                "<option value='second'><@spring.message 'second_week_of_month'/></option>" +
                "<option value='third'><@spring.message 'third_week_of_month'/></option>" +
                "<option value='fourth'><@spring.message 'fourth_week_of_month'/></option>" +
                "<option value='fifth'><@spring.message 'fifth_week_of_month'/></option>" +
                "<option value='sixth'><@spring.message 'sixth_week_of_month'/></option>" +
                "<option value='last'><@spring.message 'last_week_of_month'/></option>" +
                "</select>"

            let day_of_week_of_month = "<select name='numberDayOfWeek' class='form-control mx-auto' style='width: 50%'>" +
                "<option value='first'><@spring.message 'first'/></option>" +
                "<option value='second'><@spring.message 'second'/></option>" +
                "<option value='third'><@spring.message 'third'/></option>" +
                "<option value='fourth'><@spring.message 'fourth'/></option>" +
                "<option value='fifth'><@spring.message 'fifth'/></option>" +
                "<option value='last'><@spring.message 'last'/></option>" +
                "</select>" +
                "" +
                "<select name='dayOfWeek' class='form-control mx-auto' style='width: 50%'>" +
                "<option value='monday'><@spring.message 'mon'/></option>" +
                "<option value='tuesday'><@spring.message 'tue'/></option>" +
                "<option value='wednesday'><@spring.message 'wed'/></option>" +
                "<option value='thursday'><@spring.message 'thu'/></option>" +
                "<option value='friday'><@spring.message 'fri'/></option>" +
                "<option value='saturday'><@spring.message 'sat'/></option>" +
                "<option value='sunday'><@spring.message 'sun'/></option>" +
                "</select>"

            let each_n_days = "<label for='each_n_days' class='mr-1'><@spring.message 'after'/></label>" +
                "<input type='number' id='each_n_days' name='gapDays' min='1'/>"

            let each_n_weeks = "<label for='each_n_weeks' class='mr-1'><@spring.message 'after'/></label>" +
                "<input type='number' id='each_n_weeks' name='gapWeeks' min='1'/></br>" + days_of_week

            let each_n_month_with_day = "<label for='each_n_month_with_day' class='mr-1'><@spring.message 'after'/></label>" +
                "<input type='number' min='1' id='each_n_month_with_day' name='gapMonths'/></br>" + day_of_month

            let each_n_month_with_week = "<label for='each_n_month_with_week' class='mr-1'><@spring.message 'after'/></label>" +
                "<input type='number' min='1' id='each_n_month_with_week' name='gapMonths'/></br>" + week_of_month

            let each_n_month_with_week_and_day = "<label for='each_n_month_with_week_and_day' class='mr-1'><@spring.message 'after'/></label>" +
                "<input type='number' min='1' id='each_n_month_with_week_and_day' name='gapMonths'/></br>" + day_of_week_of_month

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
                } else if ($('#inputPeriodMode option:selected').hasClass("each_n_days_opt")) {
                    $("#period_res").html(each_n_days)
                } else if ($('#inputPeriodMode option:selected').hasClass("each_n_weeks_opt")) {
                    $("#period_res").html(each_n_weeks)
                } else if ($('#inputPeriodMode option:selected').hasClass("each_n_month_with_day_opt")) {
                    $("#period_res").html(each_n_month_with_day)
                } else if ($('#inputPeriodMode option:selected').hasClass("each_n_month_with_week_opt")) {
                    $("#period_res").html(each_n_month_with_week)
                } else if ($('#inputPeriodMode option:selected').hasClass("each_n_month_with_week_and_day_opt")) {
                    $("#period_res").html(each_n_month_with_week_and_day)
                }
            })
        })
    </script>
</@c.commonPage>