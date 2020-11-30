<#macro commonPage>
    <!DOCTYPE html>
    <html lang="en">

    <head>
        <meta charset="UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>

        <title>TaskScheduler</title>

        <link type="text/css" rel="stylesheet" href="/webjars/bootstrap/4.5.0/css/bootstrap.min.css"/>
        <link type="text/css" rel="stylesheet" href="/css/common.css"/>

        <link rel="shortcut icon" type="image/x-icon" href="https://img.icons8.com/flat_round/32/000000/clock--v1.png"/>
    </head>

    <body>
        <script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
        <script src="/webjars/bootstrap/4.5.0/js/bootstrap.min.js"></script>
        <script src="/webjars/popper.js/1.16.0/umd/popper.min.js"></script>

        <#include "navbar.ftl">
        <div class="container">
            <#nested>
        </div>
    </body>

    </html>
</#macro>