<#include "security.ftlh">

<div class="card-columns" id="messages-list">
    <#list messages as message>
    <div class="card my-3" style="width: 18rem;" data-id="${message.id}">
        <#if message.filename??>
            <img src="/img/${message.filename}" class="card-img-top" />
        </#if>
        <div class="card-body">
            <span>Message: ${message.text}</span><br />
            <i>#${message.tag}</i>
        </div>
        <div class="card-footer text-muted">
            Author: <a href="/messages/${message.author.id}">${message.authorName}</a>
            <#if message.author.id==currentUserId>
            <br /><a class="btn btn-outline-primary btn-sm my-2" href="/messages/${message.author.id}?message=${message.id}">Edit message</a><br />


            <!-- Button trigger modal -->
            <button type="button" class="btn btn-outline-danger btn-sm" data-toggle="modal" data-target="#exampleModal${message.id}">
                Delete message
            </button>

            <!-- Modal -->
            <div class="modal fade" id="exampleModal${message.id}" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="exampleModalLabel">Deleting message</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">x</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            Are you sure you want to delete this message?
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                            <form action="/messages/${message.author.id}/${message.id}/delete" method="POST">
                                <input type="hidden" name="_csrf" value="${_csrf.token}" />
                                <button type="submit" class="btn btn-primary">Delete</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>



            </#if>
        </div>
    </div>
    <#else>
        No messages
    </#list>
</div>