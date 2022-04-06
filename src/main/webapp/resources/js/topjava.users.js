const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );
});
$(document).on("change", ".checkenable", function () {
    let en;

    if (this.checked) {
        en = true;
    } else {
        en = false;
    }
    let id = $(this).closest("tr").attr('id');
    let urlRest = "rest/" + ctx.ajaxUrl + id;
    $.ajax({
        type: "PATCH",
        url: urlRest,
        data: JSON.stringify(en),
        contentType: 'application/json',
        success: updateTable()
    });
})
