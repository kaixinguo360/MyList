
var client;

self.addEventListener('fetch', function(event) {

  var request = event.request;
  var url = new URL(request.url);

  if (request.mode !== 'navigate' && url.pathname.substr(0, 8) !== '/p/hook/') {
    if (client) {

      // Check and Replace Origin URL
      if (url.origin === self.location.origin) {
        request = new Request(client.base + url.pathname, {
          body: request.body,
          cache: request.cache,
          credentials: request.credentials,
          headers: request.headers,
          integrity: request.integrity,
          keepalive: request.keepalive,
          method: request.method,
          mode: request.mode,
          redirect: request.redirect,
          referrer: request.referrer,
          referrerPolicy: request.referrerPolicy,
          signal: request.signal,
          window: request.window
        });
      }

      // Record the URL of Image
      if (client && request.destination === 'image') {
        client.port.postMessage({
          message: 'addImage',
          img: { url: request.url }
        });
      }
    }
  } else {
    if (client) {
      client = null;
      console.log('Flush Cache.');
    }
  }

  event.respondWith(fetch(request));
});

self.addEventListener('message', function(event) {
  if (event.data.message === 'newItem') {
    client = {
      port: event.data.port,
      title: event.data.title,
      base: event.data.base,
      path: event.data.path
    };
  }
});
