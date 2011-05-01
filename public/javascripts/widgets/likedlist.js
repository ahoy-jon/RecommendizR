require.def("widgets/likedlist", ["jquery"], function($) {
   var onSuccess = function(containerId, likedList){
      if(likedList.length ==0){
         $('#'+containerId).html('<p>No data.</p>');
      } else {
         $('#'+containerId).html('<ul id="'+containerId+'-list"></ul>');
         $(likedList).each(function(i, el){
               $('#'+containerId+'-list').append('<li><a href="#" alt="'+el.description+'">'+el.name+'</a></li>');
         });
      }
   }

   var onError = function(containerId){
        $('#'+containerId).html('<p>error</p>');
   }

   return {
      "Instance": function (containerId, resource, data) {
         $.ajax({
            url: resource,
            data: data,
            success: onSuccess.curry(containerId),
            error: onError.curry(containerId)
         });
      }
   };

});