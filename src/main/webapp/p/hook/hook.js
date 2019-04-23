
/* ---------- URL ---------- */

history.pushState('','', PATH);

/* ---------- Service Worker ---------- */

// Create Message Channel
var channel = new MessageChannel();
channel.port1.onmessage = function (e) {
  addImage(e.data.img);
};

// Install Service Worker
if ('serviceWorker' in navigator) {
  navigator.serviceWorker.register('/p/hook-sw.js', { scope: '/p/page/' })
    .then(function(reg) {
      
      if (!reg.active) {
        window.location.reload();
      }

      // registration worked
      console.log('Registration succeeded.');
    }).catch(function(error) {
    // registration failed
    console.log('Registration failed with ' + error);
    });
}

// Send Message When Load
navigator.serviceWorker.controller.postMessage({
  message: 'newItem',
  port: channel.port2,
  title: document.title,
  base: BASE,
  path: PATH
}, [channel.port2]);

/* ---------- Check ---------- */

// Check If Load Is Complete
var time = (new Date()).getTime();
function check() {
  if ((new Date()).getTime() - time > 3000) {
    commit();
  } else {
    setTimeout(check, 1000);
  }
}

// Commit All Images To New-Item Component
function commit() {
  fetchImages();
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
    commit();
  });
}

// Document Onload Event Handler
$(document).ready(function () {
  setupUI();
  setTimeout(check, 3000);
});


/* ---------- Images ---------- */

// Add An Image
var imgs = [];
function addImage(img) {
  var isAdded = false;
  imgs.forEach(function (value) {
    if (value.url === img.url) {
      value.info = value.info ? value.info : img.info;
      isAdded = true;
    }
  });
  if (!isAdded) {
    imgs.push(img);
  }
}

// Fetch Images For Page
function fetchImages() {
  imgs.length = 0;
  $('img').each(function () {
    addImage({
      url: getAbsUrl($(this).attr('src')),
      info: $(this).attr('alt')
    });
  });
}

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
    return BASE + PATH + '/' + url;
  }
}
