<#macro pager url page>
    <#assign current = page.getNumber()
    total = page.getTotalPages()
    />

    <#if (total > 7)>
        <#assign
        first = (current+1 > 4)?then([1, -1], [1, 2, 3])
        last = (current+1 < total-3)?then([-1, total], [total-2, total-1, total])
        twoBefore = (current+1 > 4 && current+1 < total-1)?then([current-1, current], [])
        twoAfter = (current+1 > 2 && current+1 < total-3)?then([current+2, current+3], [])

        line = first + twoBefore + (current+1 > 3 && current+1 < total-2)?then([current+1], []) + twoAfter + last
        />
    <#else>
        <#assign
        line = 1..total
        />
    </#if>

    <div>
        <#if total != 0>
            <ul class="pagination justify-content-center">
                <#if current == 0>
                    <li class="page-item disabled">
                        <a class="page-link bg-dark border-dark" href="#" aria-disabled="true">&lt;&lt;</a>
                    </li>
                <#else>
                    <li class="page-item">
                        <a class="page-link bg-dark border-dark" href="${url}&amp;page=${current-1}">&lt;&lt;</a>
                    </li>
                </#if>

                <#list line as p>
                    <#if (p-1) == current>
                        <li class="page-item active">
                            <a class="page-link" href="#">${p}</a>
                        </li>
                        <#assign current = (p-1)/>
                    <#elseif p == -1>
                        <li class="page-item disabled">
                            <a class="page-link bg-dark border-dark text-white" href="#" aria-disabled="true">...</a>
                        </li>
                    <#else>
                        <li class="page-item">
                            <a class="page-link bg-dark border-dark" href="${url}&amp;page=${p-1}">${p}</a>
                        </li>
                    </#if>
                </#list>

                <#if current == total-1>
                    <li class="page-item disabled">
                        <a class="page-link bg-dark border-dark" href="#" aria-disabled="true">&gt;&gt;</a>
                    </li>
                <#else>
                    <li class="page-item">
                        <a class="page-link bg-dark border-dark" href="${url}&amp;page=${current+1}">&gt;&gt;</a>
                    </li>
                </#if>
            </ul>
        </#if>
    </div>
</#macro>