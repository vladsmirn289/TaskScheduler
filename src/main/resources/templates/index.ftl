<#import "parts/common.ftl" as c>

<@c.commonPage>
    <div>
        <#if localDate.getMonthValue() == 1>
            <#assign month = "Январь">
        <#elseif localDate.getMonthValue() == 2>
            <#assign month = "Февраль">
        <#elseif localDate.getMonthValue() == 3>
            <#assign month = "Март">
        <#elseif localDate.getMonthValue() == 4>
            <#assign month = "Апрель">
        <#elseif localDate.getMonthValue() == 5>
            <#assign month = "Май">
        <#elseif localDate.getMonthValue() == 6>
            <#assign month = "Июнь">
        <#elseif localDate.getMonthValue() == 7>
            <#assign month = "Июль">
        <#elseif localDate.getMonthValue() == 8>
            <#assign month = "Август">
        <#elseif localDate.getMonthValue() == 9>
            <#assign month = "Сентябрь">
        <#elseif localDate.getMonthValue() == 10>
            <#assign month = "Октябрь">
        <#elseif localDate.getMonthValue() == 11>
            <#assign month = "Ноябрь">
        <#elseif localDate.getMonthValue() == 12>
            <#assign month = "Декабрь">
        </#if>

        <form class="form-inline justify-content-center mb-4" action="/" method="get">
            <div class="form-group">
                <input class="form-control" type="month" name="date"/>
            </div>

            <div class="form-group">
                <button class="btn btn-primary" type="submit">Ok</button>
            </div>
        </form>

        <div class="text-center text-white mb-5 h2">
            ${month}, ${localDate.getYear()?string.computer}
        </div>

        <table class="table table-bordered">
            <thead>
                <tr>
                    <th scope="col" class="text-center" style="background-color: #17a2b8">Пн</th>
                    <th scope="col" class="text-center" style="background-color: #17a2b8">Вт</th>
                    <th scope="col" class="text-center" style="background-color: #17a2b8">Ср</th>
                    <th scope="col" class="text-center" style="background-color: #17a2b8">Чт</th>
                    <th scope="col" class="text-center" style="background-color: #17a2b8">Пт</th>
                    <th scope="col" class="text-center" style="background-color: #17a2b8">Сб</th>
                    <th scope="col" class="text-center" style="background-color: #17a2b8">Вс</th>
                </tr>
            </thead>
            <tbody>
                <#list calendar as row>
                    <tr>
                        <#list row as col>
                            <#if col??>
                                <td class="p-0">
                                    <button class="btn btn-block btn-warning pl-0 pt-0">
                                    <!--<button class="button btn-block btn-danger">-->
                                        <table class="table table-borderless m-0">
                                            <tr>
                                                <td class="p-1 text-left">
                                                    <#if col.equals(now)>
                                                        <div class="btn btn-primary" style="border-radius: 50%">${col.getDayOfMonth()}</div>
                                                    <#else>
                                                        <div class="btn btn-dark" style="border-radius: 50%">${col.getDayOfMonth()}</div>
                                                    </#if>
                                                </td>
                                                <td class="p-1"></td>
                                            </tr>
                                            <tr>
                                                <td class="p-1"></td>
                                                <td class="p-1 text-right">0</td>
                                            </tr>
                                        </table>
                                    </button>
                                </td>
                            <#else>
                                <td style="background-color: grey"></td>
                            </#if>
                        </#list>
                    </tr>
                </#list>
            </tbody>
        </table>
    </div>
</@c.commonPage>