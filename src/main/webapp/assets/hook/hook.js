

/* ---------- URL ----------*/

history.pushState('','', PATH);


/* ---------- Ajax ----------*/

// Get Absolute URL
function getAbsUrl(url) {
  if (!url) {
    return url;
  } else  if (url.substr(0, 7) === 'http://' || url.substr(0, 8) === 'https://') {
    return url;
  } else if (url.substr(0, 2) === '//') {
    return document.location.protocol + url;
  } else if (url.substr(0, 1) === '/') {
    return BASE + url;
  } else {
    return BASE + '/' + url;
  }
}

// Time Of Last Ajax
var time = (new Date()).getTime();

// Hook All Ajax Request
xhook.before(function(request) {
  request.url = getAbsUrl(request.url);
  time = (new Date()).getTime();
});

// Check If Ajax Is Complete
function check() {
  if ((new Date()).getTime() - time > 3000) {
    fetchImages();
  } else {
    setTimeout(check, 1000);
  }
}


/* ---------- Images ----------*/

var imgs = [];

// Fetch Images For Page
function fetchImages() {
  imgs.length = 0;
  $('img').each(function () {
    addImage({
      url: getAbsUrl($(this).attr('src')),
      info: $(this).attr('alt')
    });
  });
  saveImages();
}

// Add An Image
function addImage(img) {
  var isAdded = false;
  imgs.forEach(function (value) { 
    if (value.url === img.url) {
      isAdded = true;
    }
  });
  if (!isAdded) {
    imgs.push(img);
  }
}

// Save Images
function saveImages() {
  localStorage.setItem('tmpItem', JSON.stringify({
    title: document.title,
    images: imgs
  }));
}

/* ---------- UI ---------- */

// Setup UI
function setupUI() {
  $('body').prepend('\
    <div class="hook-box">\
        <button class="hook-reload">🔄</button>\
    </div>\
  ');

  // Reload All
  $('.hook-reload').click(function () {
    fetchImages();
  });
}

// Document Onload Event Handler
$(document).ready(function () {
  setupUI();
  setTimeout(check, 3000);
});
