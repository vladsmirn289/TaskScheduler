<nav class="navbar fixed-top navbar-expand-lg navbar-dark bg-success">
    <a class="navbar-brand" href="/">TaskScheduler</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link " href="/">Главная <span class="sr-only">(current)</span></a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/#">Повторяемая задача <span class="sr-only">(current)</span></a>
            </li>
        </ul>
    </div>

    <form class="mr-2" action="/#" method="post">
        <button class="btn p-0" type="submit">
            <img src="https://img.icons8.com/color/40/000000/usa-circular.png" class="d-inline-block align-top" alt="US">
        </button>
    </form>

    <form class="mr-3" action="/#" method="post">
        <button class="btn p-0">
            <img src="https://img.icons8.com/color/40/000000/russian-federation-circular.png" class="d-inline-block align-top" alt="RU"/>
        </button>
    </form>

    <#assign isExist = false>
    <#if isExist>
        <form action="/#" method="post">
            <input type="hidden" name="_csrf" <#--value="${_csrf.token}"-->/>
            <button type="submit" class="btn btn-outline-primary">Выйти</button>
        </form>
    <#else>
        <form action="/#" method="post">
            <input type="hidden" name="_csrf" <#--value="${_csrf.token}"-->/>
            <button type="submit" class="btn btn-primary">Войти</button>
        </form>
    </#if>
</nav>