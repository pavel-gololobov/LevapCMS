$(document).ready(function () {
  $('#addPicturesModal').on('shown.bs.modal', function (e) {
    //document.getElementById('filelist').innerHTML = '';
    //document.getElementById('console').innerHTML = '';
  });
});

var uploader = new plupload.Uploader({
  runtimes : 'html5,html4',
  browse_button : 'pickfiles',
  container: document.getElementById('filesContainer'),
  url : '/files/upload/' + folderId,
  filters : {
    max_file_size : '50mb',
    mime_types: [
      {title : "Image files", extensions : "jpg,gif,png"},
      {title : "Zip files", extensions : "zip"}
    ]
  },
  headers: {
      "X-CSRF-TOKEN" : $("meta[name='_csrf']").attr("content")
  },

  init: {
    PostInit: function() {
      document.getElementById('filelist').innerHTML = '';
      document.getElementById('uploadfiles').onclick = function() {
        uploader.start();
        return false;
      };
    },

    FilesAdded: function(up, files) {
      plupload.each(files, function(file) {
        document.getElementById('filelist').innerHTML += '<div id="' + file.id + '">' + file.name + ' (' + plupload.formatSize(file.size) + ') <b></b> <a href="javascript:;" onclick="removeFile(\'' + file.id + '\');">X</a></div>';
      });
    },
    
    FilesRemoved: function(up, files) {
      plupload.each(files, function(file) {
        var elem = document.getElementById(file.id);
        elem.parentNode.removeChild(elem);
      });
    },

    UploadProgress: function(up, file) {
      document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = '<span>' + file.percent + "%</span>";
    },

    Error: function(up, err) {
      document.getElementById('console').appendChild(document.createTextNode("\nError #" + err.code + ": " + err.message));
    },
    
    FileUploaded: function(up, file, result) {
      document.getElementById('console').appendChild(document.createTextNode("\nFile uploaded #" + file.name + ": " + result.status));
    },
    
    UploadComplete: function(up, files) {
      document.getElementById('console').appendChild(document.createTextNode("\nAll done"));
    }
  }
});

uploader.init();

function removeFile(file) {
  uploader.removeFile(file);
}