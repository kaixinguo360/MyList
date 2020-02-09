
var client;

self.addEventListener('fetch', function(event) {

  var request = event.request;

  if (client) {
    var url = new URL(request.url);
    if (request.mode !== 'navigate' && url.pathname.substr(0, 8) !== '/proxy/hook/') {
      // Check and Replace Origin URL
      if (request.url.match(self.location.origin)) {
        request = new Request(request.url.replace(self.location.origin, client.origin), {
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
    } else {
      client = null;
      console.log('[SW:Flush Cache]');
    }
  }

  event.respondWith(fetch(request));
});

self.addEventListener('message', function(event) {
  if (event.data.message === 'newItem') {
    client = {
      port: event.data.port,
      title: event.data.title,
      origin: event.data.origin,
      path: event.data.path
    };
  }
  console.log('[SW:New Client] ' + event.data.origin + ' ' + event.data.path);
});
