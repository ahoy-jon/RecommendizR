require.def("widgets/likedlist", ["jquery"], function($) {

   var switchlike = function(likedId, switchLikeResource, e) {
      e.preventDefault();
      var el = $(this);
      $.ajax({
         url: switchLikeResource,
         data: {likedId:likedId},
         success: function(liked) {
            if (liked == 'true') {
               el.text('unlike');
            } else {
               el.text('like');
            }
         },
         error: function() {
         }
      })
   }

   var onSuccess = function(containerId, isLikedResource, switchLikeResource, likedList) {
      if (likedList.length == 0) {
         $('#' + containerId).html('<p>No data.</p>');
      } else {
         $('#' + containerId).html('<ul id="' + containerId + '-list"></ul>');
         $(likedList).each(function(i, el) {
            var likeOrUnlikeButton = "";
            if (el.liked==true){
                likeOrUnlikeButton = " <a id='" + containerId + "-li-a-" + el.id + "' href='#'>unlike</a>";
            }else if(el.liked==false){
                likeOrUnlikeButton = " <a id='" + containerId + "-li-a-" + el.id + "' href='#'>like</a>";
            }
            $('#' + containerId + "-li-a-" + el.id).live('click', switchlike.curry(el.id, switchLikeResource));
            $('#' + containerId + '-list').append('<li id="' + containerId + '-li-' + el.id + '"><a href="#" alt="' + el.description + '">' + el.name + '</a> ' + likeOrUnlikeButton + '</li>');
         });

      }
   }

   var onError = function(containerId, xhr) {
      $('#' + containerId).html('<p>'+xhr.responseText+'</p>');
   }

   return {
      "Instance": function (containerId, resource, data, isLikedResource, switchlikeResource) {
         $.ajax({
            url: resource,
            data: data,
            success: onSuccess.curry(containerId, isLikedResource, switchlikeResource),
            error: onError.curry(containerId)
         });
      }
   };

})