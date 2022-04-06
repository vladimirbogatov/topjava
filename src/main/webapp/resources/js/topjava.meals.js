const mealsAjaxUrl = "ui/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealsAjaxUrl
};

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
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

function filter_meals() {
    $.ajax({
        type: "GET",
        url: ctx.ajaxUrl + "filter",
        data: $('#filterMeals').serialize(),
    }).done(function (data) {
        redrawTable(data)
    })
}

function clearForm() {
    $("#filterMeals").trigger("reset");
    updateTable();
}


