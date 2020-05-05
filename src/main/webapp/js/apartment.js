function getVals(that){
    // Get slider values
    var parent = that.parentNode;
    var slides = parent.getElementsByTagName("input");
    var slide1 = parseFloat( slides[0].value );
    var slide2 = parseFloat( slides[1].value );
    // Neither slider will clip the other, so make sure we determine which is larger
    if( slide1 > slide2 ){ var tmp = slide2; slide2 = slide1; slide1 = tmp; }

    var displayElement = parent.getElementsByClassName("rangeValues")[0];
    displayElement.innerHTML = "UAN " + slide1 + " - " + slide2;
    return slides;
}

function ajaxGetApartments (slide1, slide2, offset){
    $.ajax({
        type: "GET",
        url: "/hotel/controller?command=showApartmentsContent",
        data: {
            offset: offset,
            statusId : $("#StatusSelect").children("option:selected").val(),
            classId :$("#ClassSelect").children("option:selected").val(),
            quantityOfRooms : $("#QuantityOfRoomsSelect").children("option:selected").val(),
            priceFrom : slide1,
            priceUntil: slide2,
            orderByASC : $("#PriceSelect").children("option:selected").val(),
            arrivalDate: $("#ArrivalDate").val(),
            departureDate :$("#DepartureDate").val()
        },
        success: function (result) {
            $("#ApartmentContent").html(result);
        },
        error: function (result) {

        }
    });
}


window.onload = function(){
    var slide1;
    var slide2;
    // Initialize Sliders
    var sliderSections = document.getElementsByClassName("range-slider");
    for( var x = 0; x < sliderSections.length; x++ ){
        var sliders = sliderSections[x].getElementsByTagName("input");
        for( var y = 0; y < sliders.length; y++ ){
            if( sliders[y].type ==="range" ){
                sliders[y].oninput = function(){
                    var slides = getVals(this);
                    slide1 = parseFloat(slides[0].value);
                    slide2 = parseFloat(slides[1].value);
                    if( slide1 > slide2 ){ var tmp = slide2; slide2 = slide1; slide1 = tmp; }
                    ajaxGetApartments(slide1, slide2, 0);
                }
                sliders[y].oninput();
            }
        }
    }


    $("#StatusSelect").change(function () {
        ajaxGetApartments(slide1, slide2,0);
    })

    $("#ClassSelect").change(function () {
        ajaxGetApartments(slide1, slide2, 0);
    })

    $("#QuantityOfRoomsSelect").change(function () {
        ajaxGetApartments(slide1, slide2,0);
    })

    $("#PriceSelect").change(function () {
        ajaxGetApartments(slide1, slide2,0);
    })

    $("#ArrivalDate").change(function () {
        ajaxGetApartments(slide1, slide2,0);
    })

    $("#DepartureDate").change(function () {
        ajaxGetApartments(slide1, slide2,0);
    })


    $(document).on("click", "#Next", function (e) {
        e.preventDefault();
        var offset =  $("#Next").attr("data-offset");
        ajaxGetApartments(slide1, slide2, offset);
    });

    $(document).on("click", "#Prev", function (e) {
        e.preventDefault();
        var offset =  $("#Prev").attr("data-offset");
        ajaxGetApartments(slide1, slide2, offset);
    });

    $(document).on("click", ".pageNumber", function (e) {
        e.preventDefault();
        var offset =  $(this).attr("data-offset");
        console.log(offset);
        ajaxGetApartments(slide1, slide2, offset);
    });

    $(document).on("click", ".Book", function (e) {
        e.preventDefault();
        var apartmentId = $(this).data("id");
        $.ajax({
            type: "POST",
            url: "/hotel/book",
            data: {
                arrivalDate: $("#ArrivalDate").val(),
                departureDate :$("#DepartureDate").val(),
                apartmentId: apartmentId
            },
            success: function (result) {
                $("#ModalLongTitle").text(result.title);
                $("#ModalBody").text(result.message);
                var f =new Function (result.js);
                f();

            },
            error: function (result) {

            }
        });

    });

}