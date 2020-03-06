$(document).ready(function () {
  var token = $("meta[name='_csrf']").attr("content"),
      header = $("meta[name='_csrf_header']").attr("content");
  $(document).ajaxSend(function (e, xhr, options) {
    xhr.setRequestHeader(header, token);
  });
  
  loadImage('medium');
});

function loadImage(size) {
  $("#mainpicture").attr('src','/files/' + folderId + '/' + size);
}

var currentValue = 0;
function handleImageSize(myRadio) {
  currentValue = myRadio.value;
  if(myRadio.value == "1") {
    loadImage('small');
  } else if(myRadio.value == "2") {
    loadImage('medium');
  } else if(myRadio.value == "3") {
    loadImage('large');
  }
}

function setAsFrontImage() {
  var requestUrl = '/api/picture/setfront';
  var folder = {
      id: folderId
  };
 
  $.ajax({
    cache: false,
    dataType: 'json',
    url: requestUrl,
    data: JSON.stringify(folder),
    type: "POST",
    contentType: "application/json;charset=utf-8",
    success: function(data, textStatus, jqXHR) {
      toastr.success("Success");
      getFolder();
    },
    error: function(jqXHR, textStatus, errorThrown) {
      scanRestForErrors(jqXHR);
      getFolder();
    }
  });
}


