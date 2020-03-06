//<li class="breadcrumb-item"><a href="/">Home</a></li>
//<li class="breadcrumb-item"><a href="#">Library</a></li>
//<li class="breadcrumb-item active" aria-current="page">Data</li>
$(document).ready(function () {
  var token = $("meta[name='_csrf']").attr("content"),
      header = $("meta[name='_csrf_header']").attr("content");
  $(document).ajaxSend(function (e, xhr, options) {
    xhr.setRequestHeader(header, token);
  });
  
  getBreadcrumbs();
});

function getBreadcrumbs() {
  var global_html = '';
  if(folderId !== null) {
    $.getJSON('/api/folder/get/' + folderId + '/breadcrumbs', function () {
    }).done(function(data) {
      data.forEach(function(val) {
        console.log(val);
        var url = '';
        if(val.type == 'PICTURE') {
          url = '/picture/' + val.id;
        } else {
          url = '/folder/' + val.id;
        }
        global_html = '<li class="breadcrumb-item"><a href="' + url + '">' + val.name + '</a></li>' + global_html;
        console.log(global_html);
      });
    }).fail(function(jqxhr, textStatus, error) { 
        console.log("error");
        scanRestForErrors(jqxhr);
    }).always(function() { 
      global_html = '<li class="breadcrumb-item"><a href="/">Главная</a></li>' + global_html;
      $('#breadcrumbs').html(global_html);
    });
  } else {
    global_html = '<li class="breadcrumb-item"><a href="/">Главная</a></li>';
    $('#breadcrumbs').html(global_html);
  }
}

