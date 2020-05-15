<#import "parts/common.ftlh" as c>

<@c.page>

<nav>
    <div class="nav nav-tabs" id="nav-tab" role="tablist">
        <a class="nav-item nav-link ${(activeTab=='subscriptions')?string('active', '')}" id="nav-subscriptions-tab" data-toggle="tab" href="#nav-subscriptions" role="tab" aria-controls="nav-subscriptions" aria-selected="${(activeTab=='subscriptions')?string('true', 'false')}">Subscriptions</a>
        <a class="nav-item nav-link ${(activeTab=='subscribers')?string('active', '')}" id="nav-subscribers-tab" data-toggle="tab" href="#nav-subscribers" role="tab" aria-controls="nav-subscribers" aria-selected="${(activeTab=='subscribers')?string('true', '')}">Subscribers</a>
    </div>
</nav>



<div class="tab-content" id="nav-tabContent">
    <div class="tab-pane fade ${(activeTab=='subscriptions')?string('show active', '')}" id="nav-subscriptions" role="tabpanel" aria-labelledby="nav-subscriptions-tab">

        <ul class="list-group">
            <li class="list-group-item d-flex justify-content-between align-items-center w-25 p-3">
                Name
            </li>
            <#list subscriptions as user>
            <li class="list-group-item d-flex justify-content-between align-items-center w-25 p-3">
                <a class="align-middle" href="/messages/${user.id}">${user.username}</a>
                <form action="/user/${user.id}/${(listOfSubscriptionsCurrentUser?seq_contains(user))?string('unsubscribe', 'subscribe')}" method="POST">
                    <input type="hidden" name="_csrf" value="${_csrf.token}" />
                    <button type="submit" class="btn btn-outline-primary btn-sm mx-2 float-right align-middle">${(listOfSubscriptionsCurrentUser?seq_contains(user))?string('Unsubscribe', 'Subscribe')}</button>
                </form>

            </li>
            <#else>
            <li class="list-group-item d-flex justify-content-between align-items-center w-25 p-3">
                No subscriptions
            </li>
            </#list>
        </ul>


    </div>

    <div class="tab-pane fade ${(activeTab=='subscribers')?string('show active', '')}" id="nav-subscribers" role="tabpanel" aria-labelledby="nav-subscribers-tab">

        <ul class="list-group">
            <li class="list-group-item d-flex justify-content-between align-items-center w-25 p-3">
                Name
            </li>
            <#list subscribers as user>
            <li class="list-group-item d-flex justify-content-between align-items-center w-25 p-3">
                <a class="align-middle" href="/messages/${user.id}">${user.username}</a>
                <form action="/user/${user.id}/${(listOfSubscriptionsCurrentUser?seq_contains(user))?string('unsubscribe', 'subscribe')}" method="POST">
                    <input type="hidden" name="_csrf" value="${_csrf.token}" />
                    <button type="submit" class="btn btn-outline-primary btn-sm mx-2 float-right align-middle">${(listOfSubscriptionsCurrentUser?seq_contains(user))?string('Unsubscribe', 'Subscribe')}</button>
                </form>

            </li>
            <#else>
            <li class="list-group-item d-flex justify-content-between align-items-center w-25 p-3">
                No subscribers
            </li>
            </#list>
        </ul>

    </div>

</div>


</@c.page>