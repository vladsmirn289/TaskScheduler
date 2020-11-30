<#import "parts/common.ftl" as c>
<#import "/spring.ftl" as spring>

<@c.commonPage>
    <div>
        <#if localDate.getMonthValue() == 1>
            <#assign month = springMacroRequestContext.getMessage("january")>
        <#elseif localDate.getMonthValue() == 2>
            <#assign month = springMacroRequestContext.getMessage("february")>
        <#elseif localDate.getMonthValue() == 3>
            <#assign month = springMacroRequestContext.getMessage("march")>
        <#elseif localDate.getMonthValue() == 4>
            <#assign month = springMacroRequestContext.getMessage("april")>
        <#elseif localDate.getMonthValue() == 5>
            <#assign month = springMacroRequestContext.getMessage("may")>
        <#elseif localDate.getMonthValue() == 6>
            <#assign month = springMacroRequestContext.getMessage("june")>
        <#elseif localDate.getMonthValue() == 7>
            <#assign month = springMacroRequestContext.getMessage("july")>
        <#elseif localDate.getMonthValue() == 8>
            <#assign month = springMacroRequestContext.getMessage("august")>
        <#elseif localDate.getMonthValue() == 9>
            <#assign month = springMacroRequestContext.getMessage("september")>
        <#elseif localDate.getMonthValue() == 10>
            <#assign month = springMacroRequestContext.getMessage("october")>
        <#elseif localDate.getMonthValue() == 11>
            <#assign month = springMacroRequestContext.getMessage("november")>
        <#elseif localDate.getMonthValue() == 12>
            <#assign month = springMacroRequestContext.getMessage("december")>
        </#if>

        <div class="row mx-auto col-md-auto mb-3">
            <div class="mx-auto form-inline">
                <form class="d-inline-block" action="/" method="get">
                    <input type="text" name="date" hidden="hidden"
                            <#if localDate.minusMonths(1).getMonthValue() gt 9>
                           value="${localDate.minusMonths(1).getYear()?c}-${localDate.minusMonths(1).getMonthValue()}"/>
                    <#else>
                        value="${localDate.minusMonths(1).getYear()?c}-0${localDate.minusMonths(1).getMonthValue()}"/>
                    </#if>

                    <button class="btn p-0" type="submit">
                        <img src="https://img.icons8.com/flat_round/40/000000/arrow-left.png" class="d-inline-block align-top" alt="US"/>
                    </button>
                </form>

                <form class="d-inline-block ml-1 mr-1" action="/" method="get">
                    <div class="form-group d-inline-block my-auto">
                        <input class="form-control" type="month" name="date"
                                <#if localDate.getMonthValue() gt 9>
                               value="${localDate.getYear()?c}-${localDate.getMonthValue()}"/>
                        <#else>
                            value="${localDate.getYear()?c}-0${localDate.getMonthValue()}"/>
                        </#if>
                    </div>

                    <div class="form-group d-inline-block my-auto">
                        <button class="btn btn-primary" type="submit">Ok</button>
                    </div>
                </form>

                <form class="d-inline-block" action="/" method="get">
                    <input type="text" name="date" hidden="hidden"
                            <#if localDate.plusMonths(1).getMonthValue() gt 9>
                           value="${localDate.plusMonths(1).getYear()?c}-${localDate.plusMonths(1).getMonthValue()}"/>
                    <#else>
                        value="${localDate.plusMonths(1).getYear()?c}-0${localDate.plusMonths(1).getMonthValue()}"/>
                    </#if>

                    <button class="btn p-0" type="submit">
                        <img src="https://img.icons8.com/flat_round/40/000000/arrow-right.png" class="d-inline-block align-top" alt="US"/>
                    </button>
                </form>
            </div>
        </div>

        <div class="mb-5 row">
            <div class="mx-auto text-white h2">
                ${month}, ${localDate.getYear()?string.computer}
            </div>
        </div>

        <#assign counter=0>
        <div class="row">
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th scope="col" class="text-center" style="background-color: #17a2b8"><@spring.message "mon"/></th>
                    <th scope="col" class="text-center" style="background-color: #17a2b8"><@spring.message "tue"/></th>
                    <th scope="col" class="text-center" style="background-color: #17a2b8"><@spring.message "wed"/></th>
                    <th scope="col" class="text-center" style="background-color: #17a2b8"><@spring.message "thu"/></th>
                    <th scope="col" class="text-center" style="background-color: #17a2b8"><@spring.message "fri"/></th>
                    <th scope="col" class="text-center" style="background-color: #17a2b8"><@spring.message "sat"/></th>
                    <th scope="col" class="text-center" style="background-color: #17a2b8"><@spring.message "sun"/></th>
                </tr>
                </thead>
                <tbody>
                <#list calendar as row>
                    <tr>
                        <#list row as col>
                            <#if col??>
                                <td class="p-0">
                                    <#if countForEachDay[counter] == 0>
                                    <a class="btn btn-block btn-warning pl-0 pt-0" href="/task/listOfTasks/${col}">
                                        <#else>
                                        <a class="btn btn-block btn-danger pl-0 pt-0" href="/task/listOfTasks/${col}">
                                            </#if>
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
                                                    <td class="p-1 text-right">${countForEachDay[counter]}</td>
                                                    <#assign counter+=1>
                                                </tr>
                                            </table>
                                        </a>
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
    </div>
</@c.commonPage>