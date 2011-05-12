require.def("widgets/likedaddbox", ["jquery", "utils", "jquery.tinymce"], function($, Utils) {
   return {
      "Instance": function (containerId, resource) {

         var self = this;
         self.onLikedAdded = new Utils.Event();

         $('#' + containerId + '-button').click(function(e) {
            e.preventDefault();
            var name = $('#' + containerId + '-input-name').val();
            var description = $('#' + containerId + '-input-description').val();
            $.ajax({
               url: resource,
               data: {'liked.name':name, 'liked.description':description},
               success: onSuccess.curry(containerId),
               error: onError.curry(containerId),
               type:'POST'
            });
         });

         var onSuccess = function(containerId) {
            self.onLikedAdded.execute();
            $('#' + containerId + '-input-name').val("");
            $('#' + containerId + '-input-description').val("");
            $('#' + containerId + "-error").hide();
         }

         var onError = function(containerId, xhr) {
            $('#' + containerId + "-error").html($('#' + containerId + "-error").html() + xhr.responseText);
            $('#' + containerId + "-error").show();
         }

         $('#' + containerId + '-input-description').tinymce({
            // Location of TinyMCE script
            script_url : './public/javascripts/tiny_mce.js',

            // General options
            theme : "advanced",
            plugins : "autolink,lists,pagebreak,style,save,advhr,advimage,advlink,emotions,iespell,inlinepopups,preview,media,searchreplace,contextmenu,paste,directionality,noneditable,visualchars,nonbreaking,xhtmlxtras,template,advlist",

            // Theme options
            theme_advanced_buttons1 : "newdocument,|,bold,italic,underline,|,justifyleft,justifycenter,justifyright,justifyfull,formatselect,fontselect,fontsizeselect",
            theme_advanced_buttons2 : "bullist,numlist,|,outdent,indent,|,undo,redo,|,link,unlink,cleanup,code,|,preview,|,forecolor,backcolor,|,charmap,emotions",
            theme_advanced_buttons3 : "",
            theme_advanced_buttons4 : "",
            theme_advanced_toolbar_location : "top",
            theme_advanced_toolbar_align : "left",
            theme_advanced_statusbar_location : "bottom",
            theme_advanced_resizing : false,

            // Example content CSS (should be your site CSS)
            content_css : "css/content.css",

            // Drop lists for link/image/media/template dialogs
            template_external_list_url : "lists/template_list.js",
            external_link_list_url : "lists/link_list.js",
            external_image_list_url : "lists/image_list.js",
            media_external_list_url : "lists/media_list.js",

            // Replace values for the template plugin
            template_replace_values : {
               username : "Some User",
               staffid : "991234"
            }
         });

      }
   };

});