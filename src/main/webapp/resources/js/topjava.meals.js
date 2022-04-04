const mealsAjaxUrl = "UI/meals/";

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

function filterMeals() {
    let data = {
        startDate: $("#startDate").val(),
        endDate: $("#endDate").val(),
        startTime: $("#startTime").val(),
        endTime: $("#endTime").val()
    };
    $.get(ctx.ajaxUrl + "filter", data, function (data) {
        ctx.datatableApi.clear().rows.add(data).draw();
    });
}

function clearForm() {
    $("#filterMeals").reset();
}

