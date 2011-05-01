require.def("widgets/likedaddbox", ["jquery"], function($) {
   var onSuccess = function(containerId){

   }

   var onError = function(containerId){
        $('#'+containerId).html('<p>error</p>');
   }

   return {
      "Instance": function (containerId, resource) {
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
      }
   };

});