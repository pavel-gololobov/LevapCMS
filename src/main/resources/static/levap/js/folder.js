var currentPage = 0;

$(document).ready(function () {
  var token = $("meta[name='_csrf']").attr("content"),
      header = $("meta[name='_csrf_header']").attr("content");
  $(document).ajaxSend(function (e, xhr, options) {
    xhr.setRequestHeader(header, token);
  });
  getFolder(currentPage);
});

function getFolder(page) {
  var url = '/api/folder/get/root';
  if(pageType === 'STREAM') {
    url = '/api/stream/get';
  } else {
    if(folderId !== null) {
      url = '/api/folder/get/' + folderId;
    }
  }
  $.getJSON(url + '?page=' + page, function () {
  }).done(function(data) {
    var global_output = '';
    $.each(data.folders, function (key, val) {
      var output = '<div class="col-md-4">';
      output += '<div class="img_preview">';
      if(val.picture !== null) {
        if(pageType === 'STREAM') {
          output += '<a href="/stream/' + val.id + '"><img src="/files/' + val.id + '/thumbnail"/></a>';
        } else {
          output += '<a href="/picture/' + val.id + '"><img src="/files/' + val.id + '/thumbnail"/></a>';
        }
        output += '<span class="material-icons">photo</span>';
      } else {
        if(val.frontImageId !== null) {
          output += '<a href="/folder/' + val.id + '"><img src="/files/' + val.frontImageId + '/thumbnail"/></a>';
        } else {
          output += '<a href="/folder/' + val.id + '"><img src="https://dummyimage.com/300x300/ccc/000"/></a>';
        }
        output += '<span class="material-icons">folder</span>';
      }
      if(val.position !== null) {
        output += '<div class="h5">' + val.name + ' (' + val.position + ')</div>';
      } else {
        output += '<div class="h5">' + val.name + '</div>';
      }
      output += '</div>';
      output += '</div>';
      global_output += output;
    });
    $('#foldersRow').html(global_output);
    currentPage = page;
    
    if(data.page !== null) {
      if(currentPage > 0) {
        $('#prev_page').html('<a type="button" class="btn btn-secondary" onclick="getFolder(' + (currentPage - 1) + ');">Вперед</a>');
      } else {
        $('#prev_page').html('');
      }
      
      if(currentPage < (data.page.pagesCount - 1)) {
        $('#next_page').html('<a type="button" class="btn btn-secondary" onclick="getFolder(' + (currentPage + 1) + ');">Назад</a>');
      } else {
        $('#next_page').html('');
      }
    }

  }).fail(function(jqxhr, textStatus, error) { 
      console.log("error");
      scanRestForErrors(jqxhr);
  }).always(function() { 
  });
}

function saveFolder(event) {
  event.preventDefault();

  var name = $('#folderName').val(),
      id = $('#folderId').val(),
      position = $('#folderPosition').val(),
      folder = {},
      description = $('#folderDescription').val();
  
  if(name === null || name.length < 3 || name.length > 30) {
    toastr.error('Wrong folder name');
    return false;
  }

  var requestUrl = '/api/folder/create';
  if(id !== null && id.length > 0) {
    requestUrl = '/api/folder/update';
  }
  
  folder = {
      id: id,
      name: name,
      description: description,
      position: position,
      parentId: folderId
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
      getFolder(currentPage);
    },
    error: function(jqXHR, textStatus, errorThrown) {
      scanRestForErrors(jqXHR);
      getFolder(currentPage);
    }
  });
  return true;
}

function handleEditFolder(id) {
  $.getJSON('/api/folder/getone/' + id, function () {
  }).done(function(data) {
    if(scanForErrors(data)) {
      return;
    }
    $("#folderId").val(data.id);
    $("#folderName").val(data.name);
    $("#folderDescription").val(data.description);
    $('#folderPosition').val(data.position);
  }).fail(function(jqxhr, textStatus, error) { 
    console.log("error");
    scanRestForErrors(jqxhr);
  }).always(function() { 
    console.log("always"); 
  });
}

function handleAddFolder() {
  $("#folderName").val('');
  $("#folderDescription").val('');
  $('#folderPosition').val('');
  $("#folderId").val('');
}

function handleDeleteFolder(id) {
  $.getJSON('/api/folder/getone/' + id, function () {
  }).done(function(data) {
    if(scanForErrors(data)) {
      return;
    }
    $("#deleteFolderId").val(data.id);
    $("#deleteFolderName").val(data.name);
  }).fail(function(jqxhr, textStatus, error) { 
    console.log("error");
    scanRestForErrors(jqxhr);
  }).always(function() { 
    console.log("always"); 
  });
}

function deleteFolder(event) {
  event.preventDefault();

  var id = $('#deleteFolderId').val(),
      folder = {};

  var requestUrl = '/api/folder/delete';

  folder = {
      id: id
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
    },
    error: function(jqXHR, textStatus, errorThrown) {
      scanRestForErrors(jqXHR);
    }
  });
  return true;
}

