<#include "security.ftlh">

<div class="row justify-content-center">
    <div class="col-3">
        <h5 class="align-middle">${userVisited.username}</h5>
    </div>
    <div class="col-3">
        <#if subscriptionButton??>
            <form action="/user/${userVisited.id}/${subscriptionButton}" method="POST">
                <input type="hidden" name="_csrf" value="${_csrf.token}" />
                <button type="submit" class="btn btn-outline-primary btn-sm my-2 text-capitalize align-middle">${subscriptionButton}</button>
            </form>
        </#if>
    </div>
    <div class="col-3">
        Subscriptions<br />
        <a href="/user/${userVisited.id}/subscrList/subscriptions">${subscriptionsCount}</a>
    </div>
    <div class="col-3">
        Subscribers<br />
        <a href="/user/${userVisited.id}/subscrList/subscribers">${subscribersCount}</a>
    </div>
</div>