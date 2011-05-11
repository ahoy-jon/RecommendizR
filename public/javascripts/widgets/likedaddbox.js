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
            plugins : "autolink,lists,pagebreak,style,layer,table,save,advhr,advimage,advlink,emotions,iespell,inlinepopups,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template,advlist",

            // Theme options
            theme_advanced_buttons1 : "save,newdocument,|,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,styleselect,formatselect,fontselect,fontsizeselect",
            theme_advanced_buttons2 : "cut,copy,paste,pastetext,pasteword,|,search,replace,|,bullist,numlist,|,outdent,indent,blockquote,|,undo,redo,|,link,unlink,anchor,image,cleanup,help,code,|,insertdate,inserttime,preview,|,forecolor,backcolor",
            theme_advanced_buttons3 : "tablecontrols,|,hr,removeformat,visualaid,|,sub,sup,|,charmap,emotions,iespell,media,advhr,|,print,|,ltr,rtl,|,fullscreen",
            theme_advanced_buttons4 : "insertlayer,moveforward,movebackward,absolute,|,styleprops,|,cite,abbr,acronym,del,ins,attribs,|,visualchars,nonbreaking,template,pagebreak",
            theme_advanced_toolbar_location : "top",
            theme_advanced_toolbar_align : "left",
            theme_advanced_statusbar_location : "bottom",
            theme_advanced_resizing : true,

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