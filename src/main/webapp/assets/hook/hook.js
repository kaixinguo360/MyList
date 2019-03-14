

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
    return url;
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

function addImage(img) {
  var isAdded = false;
  imgs.forEach(function (value) { 
    if (value.url === img.url) {
      isAdded = true;
    }
  });
  if (!isAdded) {
    imgs.push(img);
    showImages();
  }
}


/* ---------- UI ----------*/

// Add Click Event Handler To Each Img Tag
$(document).on('click','img',function(){
  var img = {
    url: $(this).attr('src'),
    info: $(this).attr('alt'),
    width: $(this)[0].naturalWidth,
    height: $(this)[0].naturalHeight
  };
  if (confirm('尺寸: ' + img.width + 'x' + img.height + '\n描述: ' + (img.info ? img.info : '无') + '\n是否添加至收藏?')) {
    addImage(img);
  }
});

var imgs = [];

// Show Fetched Image
function showImages() {
  $('.hook-image').remove();
  var i = 0;
  imgs.forEach(function (img) {
    var newImage = $('<div class="hook-image"></div>');
    newImage.css('background-image', 'url(' + img.url + ')');
    newImage.attr('index', i);
    $('.hook-images').append(newImage);
    i++;
  });
}

// Fetch Images For Page
function fetchImages() {
  imgs.length = 0;
  $('img').each(function () {
    addImage({
      url: $(this).attr('src'),
      info: $(this).attr('alt'),
      width: $(this)[0].naturalWidth,
      height: $(this)[0].naturalHeight
    });
  });
}

// Setup UI
function setupUI() {
  $('body').prepend('\
    <div class="hook-box hook-box-open">\
      <div class="hook-images hook-images-close"></div>\
      <div class="hook-buttons">\
        <button class="hook-button hook-arrow">🔽</button>\
        <button class="hook-button-hidden hook-save">💾</button>\
        <button class="hook-button-hidden hook-remove">🗑️</button>\
        <button class="hook-button-hidden hook-reload">🔄</button>\
      </div>\
    </div>\
  ');
  
  var box = $('.hook-box');
  var images = $('.hook-images');
  var arrow = $('.hook-arrow');
  var hiddenButtons = $('.hook-button-hidden');
  hiddenButtons.hide();

  // Save All
  $('.hook-save').click(function () {
    localStorage.setItem('imagesToSave', JSON.stringify(imgs));
    alert('正在跳转...');
  });

  // Remove All
  $('.hook-remove').click(function () {
    imgs.length = 0;
    showImages();
  });

  // Reload All
  $('.hook-reload').click(function () {
    fetchImages();
  });
  
  // Remove or Show
  $(document).on('click','div.hook-image',function(){
    if (box.hasClass('hook-box-open')) {
      var img = imgs[$(this).attr('index')];
      if (confirm('尺寸: ' + img.width + 'x' + img.height + '\n描述: ' + (img.info ? img.info : '无') + '\n确认从收藏中移除?')) {
        imgs.splice($(this).attr('index'), 1);
        showImages();
      }
    }
  });
  
  // Show or Hide Box
  box.click(function () {
    if (!images.hasClass('hook-images-open') && box.hasClass('hook-box-open')) {
      box.removeClass('hook-box-open');
      box.addClass('hook-box-close');
    } else {
      box.removeClass('hook-box-close');
      box.addClass('hook-box-open');
    }
  });
  
  // Open or Close Box
  arrow.click(function (e) {
    e.stopPropagation();
    if (box.hasClass('hook-box-open')) {
      if (images.hasClass('hook-images-open')) {
        images.removeClass('hook-images-open');
        images.addClass('hook-images-close');
        hiddenButtons.hide();
      } else {
        images.removeClass('hook-images-close');
        images.addClass('hook-images-open');
        hiddenButtons.show();
      }
    }
  });
}

// Document Onload Event Handler
$(document).ready(function () {
  setupUI();
  setTimeout(check, 3000);
});
