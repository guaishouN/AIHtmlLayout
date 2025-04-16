const socketHostname = window.location.hostname;
const socketPort = window.location.port;


$(document).ready(function () {
    init_click()
})

function init_click() {
    $('.submitpromot').on('click', function () {
        const textareaContent = $('#userPrompt').val();
        $.ajax({
            url: `/request_ai`,
            method: "POST",
            dataType: "json",
            contentType: "application/json",   // Set the content type to JSON
            data: JSON.stringify({
                'userText': textareaContent    // Data to be sent to the server
            }),
            success: function (response) {
                console.log('plantuml_code_bt_ai_help:', response);
                if (response.status === "success") {
                    console.log("strID")
                }
            },
            error: function (error) {
                console.error("Error fetching plantuml_code_bt_ai_help:", error);
            },
        })

    });
}
