function scanRestForErrors(data) {
  if($.isPlainObject(data.responseJSON)) {
    toastr.error(data.responseJSON.message, 'ERROR');
  } else {
    if(data.responseText.search('LOGIN_PAGE') != -1) {
      location.reload();
      return true;
    }
  }
}

function scanForErrors(data) {
  // check for redirect to login page
  if($.isPlainObject(data)) {
    return false;
  } else {
    if(data.search('LOGIN_PAGE') != -1) {
      location.reload();
      return true;
    }
    // check for redirect to login page
    if(data.search('ERROR_PAGE') != -1) {
      toastr.error(msg_error_support);
      return true;
    }
  }
  return false;
}
