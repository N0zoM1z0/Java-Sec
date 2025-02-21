
/*=====================================================
           Javascript Editable
======================================================*/

$(document).ready(function(){

    /*=== testimonial Slider ====*/
    $(".testimonial-slider").slick({
        autoplay: false,
        slidesToScroll: 1,
        dots: true,
        fade: true,
        infinite: true,
        arrows: false,
    });

    /* smooth scroll */
    $(function() {
        $('.scroll').click(function() {
            if (location.pathname.replace(/^\//,'') == this.pathname.replace(/^\//,'') && location.hostname == this.hostname) {
                var target = $(this.hash);
                target = target.length ? target : $('[name=' + this.hash.slice(1) +']');
                if (target.length) {
                    $('html, body').animate({
                        scrollTop: target.offset().top - 0
                    }, 800);
                    return false;
                }
            }
        });
    });
    
});

// 更新Bootstrap中的Dropdown下拉菜单更改为悬停(hover)触发
$(document).ready(function(){
    // 关闭click.bs.dropdown.data-api事件，使顶级菜单可点击
    $(document).off('click.bs.dropdown.data-api');
    // 鼠标划过就展开子菜单，免得需要点击才能展开
    $('li.dropdown').mouseover(function(){
        $(".dropdown-menu").hide();
        $(this).find(".dropdown-menu").show();
    })
    // 离开导航栏就隐藏所有的子栏目
    $(".header").mouseleave(function(){
        $(".dropdown-menu").hide();
    });
});
