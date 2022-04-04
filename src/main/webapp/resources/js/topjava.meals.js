const mealsAjaxUrl = "UI/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealsAjaxUrl
};

// $(document).ready(function () {
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
    /*alert($("#startDate").val());*/
    let data = {
        startDate:$("#startDate").val(),
        endDate:$("#endDate").val(),
        startTime:$("#startTime").val(),
        endTime:$("#endTime").val()
    };
    $.get(ctx.ajaxUrl+"filter",data, function (data) {
       ctx.datatableApi.clear().rows.add(data).draw();
    });
}

$(document).on('click', "#ResetForm", function() {
    // Reset the form
    document.getElementById('filterMeals').reset();
});

function clearForm(){
    document.getElementById('filterMeals').reset();
}
