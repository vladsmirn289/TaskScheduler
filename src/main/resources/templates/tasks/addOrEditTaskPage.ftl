<#import "../parts/common.ftl" as c>

<@c.commonPage>
    <div class="mx-auto bg-dark text-center text-white" style="padding: 15px; width: 50%; border-radius: 10px;">
        <form action="/task/addTask" name="task" method="post">
            <div class="form-group">
                <label for="taskName">Название задачи</label><br/>
                <input type="text" id="taskName" name="name" style="width: 50%"/>
            </div>

            <div class="form-group">
                <label for="taskDesc">Описание задачи</label><br/>
                <textarea rows="5" cols="50" id="taskDesc" name="description"></textarea>
            </div>

            <div class="form-group">
                <label for="inputPriority">Выберите приоритет</label><br/>
                <select id="inputPriority" name="priority" class="form-control mx-auto" style="width: 50%">
                    <#list priorities as priority>
                        <option>${priority}</option>
                    </#list>
                </select>
            </div>

            <input type="text" value="${date}" hidden="hidden" name="textDate"/>

            <div class="form-group">
                <label for="inputProgress">Прогресс задачи</label><br/>
                <div style="display: inline-block">
                    <input type="number" id="progressValue" value="" class="col-sm-2 p-0"
                            oninput="changeProgressBar(this.value)"/>
                    <input class="col-sm-8 p-0" type="range" min="0" max="100" value="0" id="inputProgress" name="progress"
                           oninput="changeProgressValue(this.value)"/>
                </div>
            </div>

            <div class="form-group">
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button class="btn btn-primary" type="submit">Создать</button>
            </div>
        </form>
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