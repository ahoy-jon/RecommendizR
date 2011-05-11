require(["jquery", "jquery.history", "functional"], function($) {

   var setContentPage = function(html) {
      $('#page_content').html(html);
   };

   var load = function(href) {
      href = href.replace(/^!/, '');

      $.ajax({
         url: href,
         success: setContentPage,
         complete: function() {
            // google analytics here.
         }
      });

   }

   $(document).ready(function() {
      Functional.install();

      $.history.init(function(url) {
         load(url == "" ? "/home" : url);
      },
      { unescape: "/,&!" });

      $('a.histolink').click(function(link) {
         var url = $(this).attr('href');
         url = url.replace(/^.*#/, '');
         $.history.load(url);
         return false;
      });

   });
});