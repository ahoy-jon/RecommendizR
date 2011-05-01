require.def("widgets/likedlist", ["jquery"], function($) {
   var onSuccess = function(containerId, likedList){
      if(likedList.length ==0){
         $('#'+containerId).html('<p>No data.</p>');
      }
   }

   var onError = function(containerId){
      alert("error"+containerId);
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