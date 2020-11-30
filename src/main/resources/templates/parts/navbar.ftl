<#include "security.ftl">
<#import "/spring.ftl" as spring>

<nav class="navbar fixed-top navbar-expand-lg navbar-dark bg-success">
    <a class="navbar-brand" href="/">TaskScheduler</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link " href="/"><@spring.message "main_page"/> <span class="sr-only">(current)</span></a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/repeatTask/list"><@spring.message "repeatable_tasks"/> <span class="sr-only">(current)</span></a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/client/personalRoom"><@spring.message "personal_room"/> <span class="sr-only">(current)</span></a>
            </li>
        </ul>

        <a class="btn p-0" id="usaLocale" href="" onclick="toUSALocale()">
            <img src="https://img.icons8.com/color/40/000000/usa-circular.png" class="d-inline-block align-top" alt="US"/>
        </a>

        <a class="btn p-0" id="ruLocale" href="" onclick="toRussianLocale()">
            <img src="https://img.icons8.com/color/40/000000/russian-federation-circular.png" class="d-inline-block align-top" alt="RU"/>
        </a>
    </div>

    <div class="navbar-text text-white mr-3">
        ${login_name}
    </div>

    <#if isExist>
        <form action="/logout" method="post">
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button type="submit" class="btn btn-primary"><@spring.message "logout"/></button>
        </form>
    <#else>
        <form action="/login" method="get">
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button type="submit" class="btn btn-primary"><@spring.message "login"/></button>
        </form>
    </#if>

    <script>
        function toUSALocale() {
            let url = window.location.href;
            if (url.includes("?")) {
                if (url.includes("lang")) {
                    let index = url.indexOf("lang");
                    url = url.slice(0, index)
                }
                url+="&amp;lang=US";
            } else {
                url+="?lang=US";
            }

            $("#usaLocale").attr("href", url);
        }

        function toRussianLocale() {
            let url = window.location.href;
            if (url.includes("?")) {
                if (url.includes("lang")) {
                    let index = url.indexOf("lang");
                    url = url.slice(0, index)
                }
                url+="&amp;lang=RU";
            } else {
                url+="?lang=RU";
            }

            $("#ruLocale").attr("href", url);
        }
    </script>
</nav>