/*
 # =============================================================================
 #   Sparkline Linechart JS
 # =============================================================================
 */


(function() {


    $(document).ready(function() {


        /*
         # =============================================================================
         #   Navbar scroll animation
         # =============================================================================
         */

        $(".navbar.scroll-hide").mouseover(function() {
            $(".navbar.scroll-hide").removeClass("closed");
            return setTimeout((function() {
                return $(".navbar.scroll-hide").css({
                            overflow: "visible"
                        });
            }), 150);
        });
        $(function() {
            var delta, lastScrollTop;
            lastScrollTop = 0;
            delta = 50;
            return $(window).scroll(function(event) {
                var st;
                st = $(this).scrollTop();
                if (Math.abs(lastScrollTop - st) <= delta) {
                    return;
                }
                if (st > lastScrollTop) {
                    $('.navbar.scroll-hide').addClass("closed");
                } else {
                    $('.navbar.scroll-hide').removeClass("closed");
                }
                return lastScrollTop = st;
            });
        });
        /*
         # =============================================================================
         #   Mobile Nav
         # =============================================================================
         */

        $('.navbar-toggle').click(function() {
            return $('body, html').toggleClass("nav-open");
        });
        /*
         # =============================================================================
         #   Sparkline Resize Script
         # =============================================================================
         */

        /*
         # =============================================================================
         #   Form wizard
         # =============================================================================
         */

        $("#wizard").bootstrapWizard({
                    nextSelector: ".btn-next",
                    previousSelector: ".btn-previous",
                    onNext: function(tab, navigation, index) {
                        var $current, $percent, $total;
                        if (index === 1) {
                            if (!$("#name").val()) {
                                $("#name").focus();
                                $("#name").addClass("has-error");
                                return false;
                            }
                        }
                        $total = navigation.find("li").length;
                        $current = index + 1;
                        $percent = ($current / $total) * 100;
                        return $("#wizard").find(".progress-bar").css("width", $percent + "%");
                    },
                    onPrevious: function(tab, navigation, index) {
                        var $current, $percent, $total;
                        $total = navigation.find("li").length;
                        $current = index + 1;
                        $percent = ($current / $total) * 100;
                        return $("#wizard").find(".progress-bar").css("width", $percent + "%");
                    },
                    onTabShow: function(tab, navigation, index) {
                        var $current, $percent, $total;
                        $total = navigation.find("li").length;
                        $current = index + 1;
                        $percent = ($current / $total) * 100;
                        return $("#wizard").find(".progress-bar").css("width", $percent + "%");
                    }
                });


        /*
         # =============================================================================
         #   Form Input Masks
         # =============================================================================
         */

        $(":input").inputmask();
        /*
         # =============================================================================
         #   Validation
         # =============================================================================
         */

        $("#validate-form").validate({
                    rules: {
                        firstname: "required",
                        lastname: "required",
                        username: {
                            required: true,
                            minlength: 2
                        },
                        password: {
                            required: true,
                            minlength: 5
                        },
                        confirm_password: {
                            required: true,
                            minlength: 5,
                            equalTo: "#password"
                        },
                        email: {
                            required: true,
                            email: true
                        }
                    },
                    messages: {
                        firstname: "Please enter your first name",
                        lastname: "Please enter your last name",
                        username: {
                            required: "Please enter a username",
                            minlength: "Your username must consist of at least 2 characters"
                        },
                        password: {
                            required: "Please provide a password",
                            minlength: "Your password must be at least 5 characters long"
                        },
                        confirm_password: {
                            required: "Please provide a password",
                            minlength: "Your password must be at least 5 characters long",
                            equalTo: "Please enter the same password"
                        },
                        email: "Please enter a valid email address"
                    }
                });


        /*
         # =============================================================================
         #   Skycons
         # =============================================================================
         */

        $('.skycons-element').each(function() {
            var canvasId, skycons, weatherSetting;
            skycons = new Skycons({
                        color: "white"
                    });
            canvasId = $(this).attr('id');
            weatherSetting = $(this).data('skycons');
            skycons.add(canvasId, Skycons[weatherSetting]);
            return skycons.play();
        });
        /*
         # =============================================================================
         #   Login/signup animation
         # =============================================================================
         */

        $(window).load(function() {
            return $(".login-container").addClass("active");
        });

        /*
         # =============================================================================
         #   Timeline animation
         # =============================================================================
         */

        timelineAnimate = function(elem) {
            return $(".timeline.animated li").each(function(i) {
                var bottom_of_object, bottom_of_window;
                bottom_of_object = $(this).position().top + $(this).outerHeight();
                bottom_of_window = $(window).scrollTop() + $(window).height();
                if (bottom_of_window > bottom_of_object) {
                    return $(this).addClass("active");
                }
            });
        };
        timelineAnimate();
        $(window).scroll(function() {
            return timelineAnimate();
        });

    });

}).call(this);
