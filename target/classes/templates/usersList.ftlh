<#import "parts/common.ftlh" as c>
<#include "parts/security.ftlh">

<@c.page>
<h5 class="m">List of users</h5>

<table  class="table table-bordered  table-hover">
    <thead class="thead-light">
        <tr>
            <th scope="col">Name</th>
            <th scope="col">Role</th>
            <th scope="col">Link to edit page</th>
        </tr>
    </thead>
    <tbody>
    <#list users as user>
    <tr>
        <td scope="row">${user.username}</td>
        <td><#list user.roles as role>${role}<#sep>, </#list></td>

        <td><a class="text-left text-decoration-none" href="/user/usersList/${user.id}">Edit</a>

                <#if !user.roles?seq_contains("ADMIN")>


                    <!-- Button trigger modal -->
                    <button type="button" class="btn btn-outline-danger btn-sm float-right" data-toggle="modal" data-target="#exampleModal${user.id}">
                        Delete user
                    </button>


                    <!-- Modal -->
                    <div class="modal fade" id="exampleModal${user.id}" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                        <div class="modal-dialog" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="exampleModalLabel">Modal title</h5>
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                                <div class="modal-body">
                                    Are you sure you want to delete ${user.username}?
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                                    <form action="/user/usersList/${user.id}/delete" method="POST">
                                        <input type="hidden" name="_csrf" value="${_csrf.token}" />
                                        <button type="submit" class="btn btn-primary">Delete</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </#if>

        </td>
    </tr>
    </#list>
    </tbody>
</table>
</@c.page>