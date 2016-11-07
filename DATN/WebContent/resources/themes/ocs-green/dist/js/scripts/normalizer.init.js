var normalizer = function () {

    var handleChangeTypeNormalizer = function () {
        $("#type-normalizer").on("change", function () {
           var dataId = $(this).val();
            console.log(dataId);
            $(".content-normalizer").addClass("hidden");
            $("#" + dataId).removeClass("hidden");
        });
    }

    return {
        //main function to initiate the module
        init: function () {
            handleChangeTypeNormalizer();
        }

    };
}();
