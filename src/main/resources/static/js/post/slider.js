$(document).ready(function () {
    $('.event-slider').slick({
        infinite: true,
        slidesToShow: 4,
        slidesToScroll: 1,
        // mobileFirst: true,
        dots: true,
        arrows: true,
        autoplay: true,
        swipeToSlide: true,
        autoplaySpeed: 3000,
        prevArrow: '<button type="button" class="slick-prev"><i class="fa-solid fa-square-caret-left"></i></button>',
        nextArrow: '<button type="button" class="slick-next"><i class="fa-solid fa-square-caret-right"></i></button>',
        responsive: [
            {
                breakpoint: 480,
                settings: {
                    rows: 2,
                    slidesToShow: 2,
                    slidesToScroll: 1,
                    arrows: false
                }
            },
            {
                breakpoint: 768,
                settings: {
                    rows: 2,
                    slidesToShow: 2,
                    slidesToScroll: 1,
                    arrows: false
                }
            },
            {
                breakpoint: 1024,
                settings: {
                    slidesToShow: 4,
                    slidesToScroll: 1,
                }
            }
        ]
    });

    $('.top-slider, .recommend-slider').slick({
        infinite: true,
        slidesToShow: 1,
        slidesToScroll: 1,
        // mobileFirst: true,
        dots: true,
        arrows: true,
        autoplay: true,
        swipeToSlide: true,
        autoplaySpeed: 5000,
        responsive: [
            {
                breakpoint: 480,
                settings: {
                    slidesToShow: 1,
                    slidesToScroll: 1,
                    arrows: false
                }
            },
            {
                breakpoint: 768,
                settings: {
                    slidesToShow: 1,
                    slidesToScroll: 1,
                    arrows: false
                }
            },
            {
                breakpoint: 1024,
                settings: {
                    slidesToShow: 1,
                    slidesToScroll: 1,
                }
            }
        ]
    });

    $('.vertical-slider').slick({
        vertical: true,
        slidesToShow: 5,
        slidesToScroll: 1,
        mobileFirst: true,
        infinite: true,
        autoplay: true,
        swipeToSlide: true,
        arrows: false,
        autoplaySpeed: 2500,
        responsive: [
            {
                breakpoint: 480,
                settings: {
                    slidesToShow: 5,
                    slidesToScroll: 1,
                }
            },
            {
                breakpoint: 768,
                settings: {
                    slidesToShow: 5,
                    slidesToScroll: 1,
                }
            },
            {
                breakpoint: 1024,
                settings: {
                    slidesToShow: 5,
                    slidesToScroll: 1,
                }
            }
        ]
    });

    $('.hotDeal-slider').slick({
        infinite: true,
        slidesToShow: 5,
        slidesToScroll: 1,
        dots: true,
        arrows: false,
        autoplay: true,
        swipeToSlide: true,
        autoplaySpeed: 0,
        speed: 3000,
        cssEase: 'linear',
        responsive: [
            {
                breakpoint: 480,
                settings: {
                    rows: 2,
                    slidesToShow: 2,
                }
            },
            {
                breakpoint: 768,
                settings: {
                    rows: 2,
                    slidesToShow: 2,
                }
            },
            {
                breakpoint: 1024,
                settings: {
                    slidesToShow: 5,
                }
            }
        ]
    });

    $('.community-slider').slick({
        infinite: true,
        slidesToShow: 5,
        slidesToScroll: 5,
        dots: true,
        arrows: true,
        autoplay: true,
        swipeToSlide: true,
        autoplaySpeed: 5000,
        prevArrow: '<button type="button" class="slick-prev"><i class="fa-solid fa-square-caret-left"></i></button>',
        nextArrow: '<button type="button" class="slick-next"><i class="fa-solid fa-square-caret-right"></i></button>',
        responsive: [
            {
                breakpoint: 480,
                settings: {
                    rows: 2,
                    slidesToShow: 2,
                    slidesToScroll: 2,
                    arrows: false,
                }
            },
            {
                breakpoint: 768,
                settings: {
                    rows: 2,
                    slidesToShow: 2,
                    slidesToScroll: 2,
                    arrows: false
                }
            },
            {
                breakpoint: 1024,
                settings: {
                    slidesToShow: 5,
                    slidesToScroll: 5,
                }
            }
        ]
    });
});
